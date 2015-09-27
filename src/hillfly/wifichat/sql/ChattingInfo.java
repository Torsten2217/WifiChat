package hillfly.wifichat.sql;


/*
 *
 *
 */
public class ChattingInfo {
    private int id; // 
    private int sendID; // 
    private int receiverID; // 
    private String date; // 
    private String info; // 
    private int style;//

    // 
    public ChattingInfo() {

    }

    public ChattingInfo(int sendID, int receiverID, String date, String info) {
        this.sendID = sendID;
        this.receiverID = receiverID;
        this.date = date;
        this.info = info;
    }

    public ChattingInfo(int id, int sendID, int receiverID, String date, String info) {
        this(sendID, receiverID, date, info);
        this.id = id;
    }
    
    public ChattingInfo(int id, int sendID, int receiverID, String date, String info,int style) {
        this(sendID, receiverID, date, info);
        this.id = id;
        this.style=style;
    }

    /*  */
    public void setID(int id) {
        this.id = id;
    }

    /*  */
    public int getId() {
        return id;
    }

    /*  */
    public void setSendID(int sendID) {
        this.sendID = sendID;
    }

    /* */
    public int getSendID() {
        return sendID;
    }

    /* */
    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    /*  */
    public int getReceiverID() {
        return receiverID;
    }

    /*  */
    public void setDate(String date) {
        this.date = date;
    }

    /*  */
    public String getDate() {
        return date;
    }

    /*  */
    public void setInfo(String info) {
        this.info = info;
    }

    /*  */
    public String getInfo() {
        return info;
    }
    
    /*  */
    public void setSytle(int style) {
        this.style = style;
    }

    /*  */
    public int getStyle() {
        return style;
    }
    /*  */
    public String toString() {
        return "ID:" + getId() + " sendID:" + getSendID() + " receiverID:" + getReceiverID()
                + " date:" + getDate() + " info:" + getInfo()+" style："+style;
    }
}
