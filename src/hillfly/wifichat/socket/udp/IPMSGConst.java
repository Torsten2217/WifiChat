package hillfly.wifichat.socket.udp;

public class IPMSGConst {
	public static final int VERSION = 0x001;		// 
	public static final int PORT = 0x0979;			// 
	
	public static final int IPMSG_NOOPERATION		 = 0x00000000;	//
	public static final int IPMSG_BR_ENTRY			 = 0x00000001;	//
	public static final int IPMSG_BR_EXIT		 	 = 0x00000002;	//
	public static final int IPMSG_ANSENTRY			 = 0x00000003;	//
	public static final int IPMSG_BR_ABSENCE		 = 0x00000004;	//
	
	public static final int IPMSG_BR_ISGETLIST		 = 0x00000010;	//
	public static final int IPMSG_OKGETLIST			 = 0x00000011;	//
	public static final int IPMSG_GETLIST			 = 0x00000012;	//
	public static final int IPMSG_ANSLIST			 = 0x00000013;	//
	
	public static final int IPMSG_SENDMSG    		 = 0x00000020;	//
	public static final int IPMSG_RECVMSG 			 = 0x00000021;	//
	public static final int IPMSG_READMSG 			 = 0x00000030;	//
	public static final int IPMSG_DELMSG 			 = 0x00000031;	//
	public static final int IPMSG_ANSREADMSG		 = 0x00000032;	//
	
	public static final int IPMSG_GETINFO			 = 0x00000040;	//
	public static final int IPMSG_SENDINFO			 = 0x00000041;	//
	
	public static final int IPMSG_GETABSENCEINFO	 = 0x00000050;	//
	public static final int IPMSG_SENDABSENCEINFO	 = 0x00000051;	//

    public static final int IPMSG_UPDATE_FILEPROCESS = 0x00000060;  //
    public static final int IPMSG_SEND_FILE_SUCCESS  = 0x00000061;  //
    public static final int IPMSG_GET_FILE_SUCCESS   = 0x00000062;  //
    
	public static final int IPMSG_REQUEST_IMAGE_DATA = 0x00000063;  //
	public static final int IPMSG_CONFIRM_IMAGE_DATA = 0x00000064;  //
	public static final int IPMSG_SEND_IMAGE_SUCCESS = 0x00000065;  //
	public static final int IPMSG_REQUEST_VOICE_DATA = 0x00000066;  //
	public static final int IPMSG_CONFIRM_VOICE_DATA = 0x00000067;  //
	public static final int IPMSG_SEND_VOICE_SUCCESS = 0x00000068;  //
	public static final int IPMSG_REQUEST_FILE_DATA  = 0x00000069;  //
	public static final int IPMSG_CONFIRM_FILE_DATA  = 0x00000070;  //
	
	public static final int IPMSG_GETPUBKEY			 = 0x00000072;	//
	public static final int IPMSG_ANSPUBKEY			 = 0x00000073;	//
	
	/* option for all command */
	public static final int IPMSG_ABSENCEOPT 		 = 0x00000100;	//
	public static final int IPMSG_SERVEROPT 		 = 0x00000200;	//
	public static final int IPMSG_DIALUPOPT 		 = 0x00010000;	//
	public static final int IPMSG_FILEATTACHOPT 	 = 0x00200000;	//
	public static final int IPMSG_ENCRYPTOPT		 = 0x00400000;	//

}
