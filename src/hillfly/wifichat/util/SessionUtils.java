package hillfly.wifichat.util;

import hillfly.wifichat.bean.Users;

import java.util.HashMap;

public class SessionUtils {

    private static Users localUserInfo;
    private static HashMap<String, String> mlocalUserSession = new HashMap<String, String>(15);

    public static Users getLocalUserInfo() {
        if (localUserInfo == null) {
            localUserInfo = new Users(getAge(), getAvatar(), getOnlineStateInt(), getNickname(),
                    getGender(), getIMEI(), getDevice(), getBirthday(), getConstellation(),
                    getLocalIPaddress(), getLoginTime());

        }
        return localUserInfo;
    }

    public static void setLocalUserInfo(Users pUsers) {
        localUserInfo = pUsers;
        mlocalUserSession.put(Users.AGE, String.valueOf(pUsers.getAge()));
        mlocalUserSession.put(Users.AVATAR, String.valueOf(pUsers.getAvatar()));
        mlocalUserSession.put(Users.ONLINESTATEINT, String.valueOf(pUsers.getOnlineStateInt()));
        mlocalUserSession.put(Users.NICKNAME, pUsers.getNickname());
        mlocalUserSession.put(Users.GENDER, pUsers.getGender());
        mlocalUserSession.put(Users.IMEI, pUsers.getIMEI());
        mlocalUserSession.put(Users.DEVICE, pUsers.getDevice());
        mlocalUserSession.put(Users.BIRTHDAY, pUsers.getBirthday());
        mlocalUserSession.put(Users.CONSTELLATION, pUsers.getConstellation());
        mlocalUserSession.put(Users.IPADDRESS, pUsers.getIpaddress());
        mlocalUserSession.put(Users.LOGINTIME, pUsers.getLogintime());

    }

    public static void updateUserInfo() {
        localUserInfo = new Users(getAge(), getAvatar(), getOnlineStateInt(), getNickname(),
                getGender(), getIMEI(), getDevice(), getBirthday(), getConstellation(),
                getLocalIPaddress(), getLoginTime());
    }

    public static String getBirthday() {
        return mlocalUserSession.get(Users.BIRTHDAY);
    }

    /**
     * 
     * 
     * @return
     */
    public static int getLocalUserID() {
        return Integer.parseInt(mlocalUserSession.get(Users.ID));
    }

    /**
     * 
     * 
     * @return localIPaddress
     */
    public static String getLocalIPaddress() {
        return mlocalUserSession.get(Users.IPADDRESS);
    }

    /**
     * 
     * 
     * @return serverIPaddress
     */
    public static String getServerIPaddress() {
        return mlocalUserSession.get(Users.SERVERIPADDRESS);
    }

    /**
     * 
     * 
     * @return Nickname
     */
    public static String getNickname() {
        return mlocalUserSession.get(Users.NICKNAME);
    }

    /**
     * 
     * 
     * @return Gender
     */
    public static String getGender() {
        return mlocalUserSession.get(Users.GENDER);
    }

    /**
     * 
     * 
     * @return IMEI
     */
    public static String getIMEI() {
        return mlocalUserSession.get(Users.IMEI);
    }

    /**
     * 
     * 
     * @return device
     */
    public static String getDevice() {
        return mlocalUserSession.get(Users.DEVICE);
    }

    /**
     * 
     * 
     * @return AvatarNum
     */
    public static int getAvatar() {
        return Integer.parseInt(mlocalUserSession.get(Users.AVATAR));
    }

    /**
     * 
     * 
     * @return
     */
    public static String getConstellation() {
        return mlocalUserSession.get(Users.CONSTELLATION);
    }

    /**
     * 
     * 
     * @return Age
     */
    public static int getAge() {
        return Integer.parseInt(mlocalUserSession.get(Users.AGE));
    }

    /**
     * 
     * 
     * @return OnlineStateInt
     */
    public static int getOnlineStateInt() {
        return Integer.parseInt(mlocalUserSession.get(Users.ONLINESTATEINT));
    }

    /**
     * 
     * 
     * @return isClient
     */
    public static boolean getIsClient() {
        return Boolean.parseBoolean(mlocalUserSession.get(Users.ISCLIENT));
    }

    /**
     * 
     * 
     * @return Data 
     */
    public static String getLoginTime() {
        return mlocalUserSession.get(Users.LOGINTIME);
    }

    public static void setBirthday(String birthday) {
        mlocalUserSession.put(Users.BIRTHDAY, birthday);
    }

    /**
     * 
     * 
     * @param paramID
     */
    public static void setLocalUserID(int paramID) {
        mlocalUserSession.put(Users.ID, String.valueOf(paramID));
    }

    /**
     * 
     * 
     * @param paramLoginTime
     */
    public static void setLoginTime(String paramLoginTime) {
        mlocalUserSession.put(Users.LOGINTIME, paramLoginTime);
    }

    /**
     * 
     * 
     * @param paramLocalIPaddress
     *            
     */
    public static void setLocalIPaddress(String paramLocalIPaddress) {
        mlocalUserSession.put(Users.IPADDRESS, paramLocalIPaddress);
    }

    /**
     * 
     * 
     * @param paramServerIPaddress
     *            
     */
    public static void setServerIPaddress(String paramServerIPaddress) {
        mlocalUserSession.put(Users.SERVERIPADDRESS, paramServerIPaddress);
    }

    /**
     * 
     * 
     * @param paramNickname
     * 
     */
    public static void setNickname(String paramNickname) {
        mlocalUserSession.put(Users.NICKNAME, paramNickname);
    }

    /**
     * 
     * 
     * @param paramConstellation
     */
    public static void setConstellation(String paramConstellation) {
        mlocalUserSession.put(Users.CONSTELLATION, paramConstellation);
    }

    /**
     * 
     * 
     * @param paramGender
     * 
     */
    public static void setGender(String paramGender) {
        mlocalUserSession.put(Users.GENDER, paramGender);
    }

    /**
     * 
     * 
     * @param paramIMEI
     *            
     */
    public static void setIMEI(String paramIMEI) {
        mlocalUserSession.put(Users.IMEI, paramIMEI);
    }

    /**
     * 
     * 
     * @param paramDevice
     */
    public static void setDevice(String paramDevice) {
        mlocalUserSession.put(Users.DEVICE, paramDevice);
    }

    /**
     * 
     * 
     * <p>
     * 
     * </p>
     * 
     * @param paramOnlineStateInt
     *            
     */
    public static void setOnlineStateInt(int paramOnlineStateInt) {
        mlocalUserSession.put(Users.ONLINESTATEINT, String.valueOf(paramOnlineStateInt));
    }

    /**
     * 
     * 
     * @param paramAvatar
     *            
     */
    public static void setAvatar(int paramAvatar) {
        mlocalUserSession.put(Users.AVATAR, String.valueOf(paramAvatar));
    }

    /**
     * 
     * 
     * @param paramAge
     */
    public static void setAge(int paramAge) {
        mlocalUserSession.put(Users.AGE, String.valueOf(paramAge));
    }

    /**
     * 
     * 
     * @param paramIsClient
     */
    public static void setIsClient(boolean paramIsClient) {
        mlocalUserSession.put(Users.ISCLIENT, String.valueOf(paramIsClient));
    }

    public static boolean isLocalUser(String paramIMEI) {
        if (paramIMEI == null) {
            return false;
        }
        else if (getIMEI().equals(paramIMEI)) {
            return true;
        }
        return false;
    }

    /** **/
    public static void clearSession() {
        mlocalUserSession.clear();
    }

}
