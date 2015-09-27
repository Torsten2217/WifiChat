package hillfly.wifichat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

/**
 * @fileName DateUtils.java
 * @package szu.wifichat.android.util
 * @description 
 * @author _Hill3
 */
public class DateUtils {

    public static String FORMATTIMESTR = "yyyy MM dd HH:mm:ss"; //

    /**
     *
     * 
     * @param time
     * @return
     */
    public static Date getDate(String time) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            date = format.parse(time);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDate(Context context, long date) {
        @SuppressWarnings("deprecation")
        int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | android.text.format.DateUtils.FORMAT_ABBREV_ALL
                | android.text.format.DateUtils.FORMAT_CAP_AMPM
                | android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_SHOW_TIME;
        return android.text.format.DateUtils.formatDateTime(context, date, format_flags);
    }

    /**
     * 
     * 
     * @return String: 
     */
    public static String getNowtime() {
        return new SimpleDateFormat(FORMATTIMESTR).format(new Date());
    }

    /**
     * 
     * 
     * @param paramTime
     *            
     * @return 
     */
    public static String getBetweentime(String paramTime) {
        String returnStr = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATTIMESTR);
        try {
            Date nowData = new Date();
            Date mDate = dateFormat.parse(paramTime);
            long betweenForSec = Math.abs(mDate.getTime() - nowData.getTime()) / 1000; // 
            if (betweenForSec < 60) {
                returnStr = betweenForSec + "seconds ago";
            }
            else if (betweenForSec < (60 * 60)) {
                returnStr = betweenForSec / 60 + "minutes ago";
            }
            else if (betweenForSec < (60 * 60 * 24)) {
                returnStr = betweenForSec / (60 * 60) + "hours ago";
            }
            else if (betweenForSec < (60 * 60 * 24 * 30)) {
                returnStr = betweenForSec / (60 * 60 * 24) + "days ago";
            }
            else if (betweenForSec < (60 * 60 * 24 * 30 * 12)) {
                returnStr = betweenForSec / (60 * 60 * 24 * 30) + "months ago";
            }
            else
                returnStr = betweenForSec / (60 * 60 * 24 * 30 * 12) + "years ago";
        }
        catch (ParseException e) {
            returnStr = "TimeError"; // 
        }
        return returnStr;
    }
}
