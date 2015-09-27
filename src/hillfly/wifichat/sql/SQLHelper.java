package hillfly.wifichat.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper
{

	private static final int VERSION = 1;               //
	private static final String DBNAME="chatting.db";   //
	private static final String T_NAME="chatting";      //
	
	/**/
	public String getTableName()
	{
		return T_NAME;
	}
	
	/*
	 * 
	 */
	public SQLHelper(Context context)
	{
		super(context, DBNAME, null, VERSION);
	}

	
	/* T_NAME
	 * 
	 * id(int) 
	 * sendID(int)
	 * receiverID(int)
	 * chatting(text)
	 * date(text)
	 * style(integer) TEXT, IMAGE, FILE, VOICE;
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table " + T_NAME + " (id integer primary key,sendID integer,receiverID integer," +
				"chatting text,date text,style integer)");
	}


	/*
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}

}
