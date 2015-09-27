package hillfly.wifichat.util;

import java.util.Calendar;
import java.util.Random;

import android.widget.EditText;

/**
 * @fileName TextUtils.java
 * @package szu.wifichat.android.util
 * @description 
 */
public class TextUtils {

    /**
     * 
     * 
     * @param month
     *            
     * @param day
     *            
     * @return
     */
    public static String getConstellation(int month, int day) {
        String[] constellationArr = { "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
                "Libra", "Scorpio", "Sagittarius", "Capricorn" };
        int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 
        return constellationArr[11];
    }

    /**
     * 
     * 
     * @param year
     *            
     * @param month
     *            
     * @param day
     *            
     * @return
     */
    public static int getAge(int year, int month, int day) {
        int age = 0;
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == year) {
            if (calendar.get(Calendar.MONTH) == month) {
                if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
                    age = calendar.get(Calendar.YEAR) - year + 1;
                }
                else {
                    age = calendar.get(Calendar.YEAR) - year;
                }
            }
            else if (calendar.get(Calendar.MONTH) > month) {
                age = calendar.get(Calendar.YEAR) - year + 1;
            }
            else {
                age = calendar.get(Calendar.YEAR) - year;
            }
        }
        else {
            age = calendar.get(Calendar.YEAR) - year;
        }
        if (age < 0) {
            return 0;
        }
        return age;
    }

    /**
     * 
     * 
     * @param editText
     *            
     * @return boolean 
     */
    public static boolean isNull(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text != null && text.length() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param NumLen
     *           
     * @return
     */
    public static String getRandomNumStr(int NumLen) {
        Random random = new Random(System.currentTimeMillis());
        StringBuffer str = new StringBuffer();
        int i, num;
        for (i = 0; i < NumLen; i++) {
            num = random.nextInt(10); // 0-10
            str.append(num);
        }
        return str.toString();
    }

}
