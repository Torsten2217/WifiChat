package hillfly.wifichat.socket.tcp;

import android.content.Context;
import android.os.Handler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import hillfly.wifichat.BaseApplication;
import hillfly.wifichat.bean.Message;
import hillfly.wifichat.file.Constant;
import hillfly.wifichat.file.FileState;
import hillfly.wifichat.file.FileStyle;
import hillfly.wifichat.socket.udp.IPMSGConst;
import hillfly.wifichat.socket.udp.UDPMessageListener;
import hillfly.wifichat.util.DateUtils;
import hillfly.wifichat.util.FileUtils;
import hillfly.wifichat.util.LogUtils;
import hillfly.wifichat.util.SessionUtils;

public class TcpClient implements Runnable {
    private static final String TAG = "SZU_TcpClient";

    private Thread mThread;
    private boolean IS_THREAD_STOP = false; // 
    private boolean SEND_FLAG = false; // 
    private static Context mContext = null;
    private static TcpClient instance;
    // private ArrayList<FileStyle> fileStyles;
    // private ArrayList<FileState> fileStates;
    private ArrayList<SendFileThread> sendFileThreads;
    private SendFileThread sendFileThread;
    private static Handler mHandler = null;

    private TcpClient() {
        sendFileThreads = new ArrayList<TcpClient.SendFileThread>();
        mThread = new Thread(this);
        LogUtils.d(TAG, "To create a thread is successfully");

    }

    public static void setHandler(Handler paramHandler) {
        mHandler = paramHandler;
    }

    public Thread getThread() {
        return mThread;
    }

    /**
     * <p>
     * 
     * <p>
     * 
     */
    public static TcpClient getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new TcpClient();
        }
        return instance;
    }

    public void sendFile(ArrayList<FileStyle> fileStyles, ArrayList<FileState> fileStates,
            String target_IP) {
        while (SEND_FLAG == true)
            ;

        for (FileStyle fileStyle : fileStyles) {
            SendFileThread sendFileThread = new SendFileThread(target_IP, fileStyle.fullPath);
            sendFileThreads.add(sendFileThread);
        }
        SEND_FLAG = true;
    }

    private TcpClient(Context context) {
        this();
        LogUtils.d(TAG, "TCP_Client initiate complete");
    }

    public void startSend() {
        LogUtils.d(TAG, "Sending thread open");
        IS_THREAD_STOP = false; // 
        if (!mThread.isAlive())
            mThread.start();
    }

    public void sendFile(String filePath, String target_IP) {
        SendFileThread sendFileThread = new SendFileThread(target_IP, filePath);
        while (SEND_FLAG == true)
            ;
        sendFileThreads.add(sendFileThread);
        SEND_FLAG = true;
    }

    public void sendFile(String filePath, String target_IP, Message.CONTENT_TYPE type) {
        SendFileThread sendFileThread = new SendFileThread(target_IP, filePath, type);
        while (SEND_FLAG == true)
            ;
        sendFileThreads.add(sendFileThread);
        FileState sendFileState = new FileState(filePath);
        BaseApplication.sendFileStates.put(filePath, sendFileState);// 
        SEND_FLAG = true;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        LogUtils.d(TAG, "TCP_Client initiation ");

        while (!IS_THREAD_STOP) {
            if (SEND_FLAG) {
                for (SendFileThread sendFileThread : sendFileThreads) {
                    sendFileThread.start();
                }
                sendFileThreads.clear();
                SEND_FLAG = false;
            }

        }
    }

    public void release() {
        while (SEND_FLAG == true)
            ;
        while (sendFileThread.isAlive())
            ;
        IS_THREAD_STOP = false;
    }

    public class SendFileThread extends Thread {
        private static final String TAG = "SZU_SendFileThread";
        private boolean SEND_FLAG = true; // 
        private byte[] mBuffer = new byte[Constant.READ_BUFFER_SIZE]; // 
        private OutputStream output = null;
        private DataOutputStream dataOutput;
        private FileInputStream fileInputStream;
        private Socket socket = null;
        private String target_IP;
        private String filePath;
        private Message.CONTENT_TYPE type;

        public SendFileThread(String target_IP, String filePath) {
            this.target_IP = target_IP;
            this.filePath = filePath;
        }

        public SendFileThread(String target_IP, String filePath, Message.CONTENT_TYPE type) {
            this(target_IP, filePath);
            this.type = type;
        }

        public void sendFile() {
            int readSize = 0;
            try {
                socket = new Socket(target_IP, Constant.TCP_SERVER_RECEIVE_PORT);
                fileInputStream = new FileInputStream(new File(filePath));
                output = socket.getOutputStream();
                dataOutput = new DataOutputStream(output);
                int fileSize = fileInputStream.available();
                dataOutput.writeUTF(filePath.substring(filePath.lastIndexOf(File.separator) + 1)
                        + "!" + fileSize + "!" + SessionUtils.getIMEI() + "!" + type);
                int count = 0;
                long length = 0;

                FileState fs = BaseApplication.sendFileStates.get(filePath);
                fs.fileSize = fileSize;
                fs.type = type;
                while (-1 != (readSize = fileInputStream.read(mBuffer))) {
                    length += readSize;
                    dataOutput.write(mBuffer, 0, readSize);
                    count++;
                    fs.percent = (int) (length * 100 / fileSize);

                    switch (type) {
                        case IMAGE:
                            break;

                        case VOICE:
                            break;

                        case FILE:
                            android.os.Message msg = mHandler.obtainMessage();
                            msg.obj = fs;
                            msg.sendToTarget();

                            break;

                        default:
                            break;
                    }
                    dataOutput.flush();
                }
                LogUtils.d(TAG, fs.fileName + "Send completed");

                output.close();
                dataOutput.close();
                socket.close();

                switch (type) {
                    case IMAGE:
                        Message imageMsg = new Message(SessionUtils.getIMEI(),
                                DateUtils.getNowtime(), fs.fileName, type);
                        imageMsg.setMsgContent(FileUtils.getNameByPath(imageMsg.getMsgContent()));
                        UDPMessageListener.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, target_IP, imageMsg);
                        LogUtils.d(TAG, "Image transmission is completed");
                        break;

                    case VOICE:
                        Message voiceMsg = new Message(SessionUtils.getIMEI(),
                                DateUtils.getNowtime(), fs.fileName, type);
                        voiceMsg.setMsgContent(FileUtils.getNameByPath(voiceMsg.getMsgContent()));
                        UDPMessageListener.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, target_IP, voiceMsg);
                        LogUtils.d(TAG, "Voice transmission is completed");
                        break;

                    case FILE:
                        android.os.Message msg = mHandler.obtainMessage();
                        fs.percent = 100;
                        msg.obj = fs;
                        msg.sendToTarget();
                        break;

                    default:
                        break;
                }

                BaseApplication.sendFileStates.remove(fs.fileName);
            }
            catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                LogUtils.d(TAG, "Failure to establish a client socket.");
                SEND_FLAG = false;
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                LogUtils.d(TAG, "Failure to establish a client socket");
                SEND_FLAG = false;
                e.printStackTrace();
            }
            finally {
                // IS_THREAD_STOP=true;
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogUtils.d(TAG, "SendFileThread initialization");
            if (SEND_FLAG) {
                sendFile();
            }
        }
    }
}
