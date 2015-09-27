package hillfly.wifichat.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.net.Uri;
import android.os.Environment;

/**
 * @fileName FileUtils.java
 * @package szu.wifichat.android.util
 * @description 
 */
public class FileUtils {

    /**
     * SD
     * 
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 
     * @param path
     *            
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 
     * 
     * @param path
     *            
     * @return 
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 
     * 
     * @param folderPath
     *            
     */
    public static void delFolder(String folderPath) {
        delAllFile(folderPath);
        String filePath = folderPath;
        filePath = filePath.toString();
        java.io.File myFilePath = new java.io.File(filePath);
        myFilePath.delete();
    }

    /**
     * 
     * 
     * @param path
     *            
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        int mLength = tempList.length;
        for (int i = 0; i < mLength; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            }
            else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
            }
        }
    }

    /**
     * 
     * 
     * @param path
     *            
     * @return
     */
    public static Uri getUriFromFile(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    /**
     * 
     * 
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "Unknown size";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        }
        else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        }
        else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        }
        else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 
     * 
     * @param path
     * @return
     */
    public static String getPathByFullPath(String fullpath) {
        return fullpath.substring(0, fullpath.lastIndexOf(File.separator));
    }

    /**
     * 
     * 
     * @param path
     * @return
     */
    public static String getNameByPath(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    /**
     * 
     * 
     * @param path
     * @return
     */

    public static boolean isFileExists(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return false;
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param
     * @return String
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 
            return sdDir.toString();
        }
        return null;
    }

}
