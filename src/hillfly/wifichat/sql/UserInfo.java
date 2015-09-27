package hillfly.wifichat.sql;

public class UserInfo {
    private int id; // 
    private int age; // 
    private int avater; // 
    private int onlinestate; // 
    private String name; // 
    private String sex; // 
    private String imei; // 
    private String ipAddr; // 
    private String lastLogintime; // 
    private String device; // 
    private String constellation; // 

    // 

    public UserInfo() {
        super();
    }

    public UserInfo(String name, String imei) {
        this.name = name;
        this.imei = imei;
    }

    public UserInfo(String name, String sex, String imei, int isOnline, int avater) {
        this(name, imei);
        this.onlinestate = isOnline;
        this.sex = sex;
        this.avater = avater;
    }

    public UserInfo(String name, int age, String sex, String imei, String ipAddr, int isOnline,
            int avater) {
        this(name, sex, imei, isOnline, avater);
        this.age = age;
        this.ipAddr = ipAddr;
    }

    public UserInfo(int id, String name, int age, String sex, String imei, String ipAddr,
            int isOnline, int avater) {
        this(name, age, sex, imei, ipAddr, isOnline, avater);
        this.id = id;
    }

    public UserInfo(int id, String name, int age, String sex, String imei, String ipAddr,
            int isOnline, int avater, String lastDate, String device, String constellation) {
        this(id, name, age, sex, imei, ipAddr, isOnline, avater);
        this.lastLogintime = lastDate;
        this.device = device;
        this.constellation = constellation;
    }

    // 
    public void setId(int id) {
        this.id = id;
    }

    // 
    public int getId() {
        return id;
    }

    // 
    public void setName(String name) {
        this.name = name;
    }

    // 
    public String getName() {
        return name;
    }

    // 
    public void setAge(int age) {
        this.age = age;
    }

    // 
    public int getAge() {
        return age;
    }

    // 
    public void setSex(String sex) {
        this.sex = sex;
    }

    // 
    public String getSex() {
        return sex;
    }

    // 
    public void setIMEI(String imei) {
        this.imei = imei;
    }

    // 
    public String getIMEI() {
        return imei;
    }

    // 
    public void setIPAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    // 
    public String getIPAddr() {
        return ipAddr;
    }

    // 
    public void setOnlineState(int onlineState) {
        this.onlinestate = onlineState;
    }

    // 
    public int getIsOnline() {
        return onlinestate;
    }

    // 
    public void setAvater(int avater) {
        this.avater = avater;
    }

    // 
    public int getAvater() {
        return avater;
    }

    // 
    public void setLastDate(String lastDate) {
        this.lastLogintime = lastDate;
    }

    // 
    public String getLastDate() {
        return lastLogintime;
    }

    /*  */
    public String getDevice() {
        return device;
    }

    /*  */
    public void setDevice(String device) {
        this.device = device;
    }

    /*  */
    public String getConstellation() {
        return constellation;
    }

    /*  */
    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    // 
    public String toString() {
        return "id:" + getId() + " name:" + getName() + " sex:" + getSex() + " age:" + getAge()
                + " IMEI:" + getIMEI() + " ip:" + getIPAddr() + " status:" + getIsOnline()
                + " avaert:" + getAvater() + " lastDate:" + getLastDate() + " device:"
                + getDevice() + " constellation:" + getConstellation();
    }
}
