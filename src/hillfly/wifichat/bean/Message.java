package hillfly.wifichat.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 
 * @author _Hill3
 */
public class Message extends Entity {

    private String senderIMEI;
    private String sendTime;
    private String MsgContent;
    private CONTENT_TYPE contentType;
    private int percent;

    public Message() {
    }

    public Message(String paramSenderIMEI, String paramSendTime, String paramMsgContent,
            CONTENT_TYPE paramContentType) {
        this.senderIMEI = paramSenderIMEI;
        this.sendTime = paramSendTime;
        this.MsgContent = paramMsgContent;
        this.contentType = paramContentType;
    }

    /**  **/
    public enum CONTENT_TYPE {
        TEXT, IMAGE, FILE, VOICE;
    }

    /**
     * IMEI
     * 
     * @return
     */

    public String getSenderIMEI() {
        return senderIMEI;
    }

    /**
     * IMEI
     * 
     * @param paramSenderIMEI
     * 
     */
    public void setSenderIMEI(String paramSenderIMEI) {
        this.senderIMEI = paramSenderIMEI;
    }

    /**
     * 
     * 
     * @return
     * @see CONTENT_TYPE
     */
    public CONTENT_TYPE getContentType() {
        return contentType;
    }

    /**
     * 
     * 
     * @param paramContentType
     * @see CONTENT_TYPE
     */
    public void setContentType(CONTENT_TYPE paramContentType) {
        this.contentType = paramContentType;
    }

    /**
     * 
     * 
     * @return
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * 
     * 
     * @param paramSendTime
     *            
     */
    public void setSendTime(String paramSendTime) {
        this.sendTime = paramSendTime;
    }

    /**
     * 
     * 
     * @return
     */
    public String getMsgContent() {
        return MsgContent;
    }

    /**
     * 
     * 
     * @param paramMsgContent
     */
    public void setMsgContent(String paramMsgContent) {
        this.MsgContent = paramMsgContent;
    }

    /**
     * 
     * 
     * @param
     */

    public Message clone() {
        return new Message(senderIMEI, sendTime, MsgContent, contentType);
    }

    @JSONField(serialize = false)
    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

}
