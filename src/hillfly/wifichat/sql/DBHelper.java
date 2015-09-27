package hillfly.wifichat.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private static final int VERSION = 1;          //
	private static final String DBNAME="user.db";  //
	private static final String T_NAME="t_user";   //
	
	/**/
	public String getTableName()
	{
		return T_NAME;
	}
	
	/*
	 * 
	 */
	public DBHelper(Context context)
	{
		super(context, DBNAME, null, VERSION);
	}
	
	/* 
	 * 
	 * id(int) 
	 * name(varchar)
	 * sex(char)
	 * IMEI(varchar)
	 * status(int)
	 * avatar(int)
	 * lastdate(text)
	 * device(text)
	 * constellation(text)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table " + T_NAME +
				" (id integer primary key,name varchar(20),sex char,age integer,IMEI varchar(20),ip varchar(20),status integer,avater integer,lastdate text,device text,constellation text)");
	}

	/*
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
	

}
