package vva.xinyu126.msgspf;

import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SMSDatabaseHelper extends SQLiteOpenHelper {
	public static final String TB_NAME = "cooperation_sms";
	public static final String ID = "id";
	public static final String THREAD_ID = "thread_id";
	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String PROTOCOL = "protocol";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String REPLY_PATH_PRESENT = "reply_path_present";
	public static final String SUBJECT = "subject";
	public static final String BODY = "body";
	public static final String SERVICE_CENTER = "service_center";
	
	public SMSDatabaseHelper(Context context,String name,
			CursorFactory fac,int version){
		super(context,name,fac,version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(TB_NAME);
		sb.append(" (");
		sb.append(ID + " INTEGER PRIMARY KEY,");
		sb.append(THREAD_ID + " VARCHAR,");
		sb.append(ADDRESS + " VARCHAR,");
		sb.append(PERSON + " VARCHAR,");
		sb.append(DATE + " VARCHAR,");
		sb.append(PROTOCOL + " VARCHAR,");
		sb.append(READ + " VARCHAR,");
		sb.append(STATUS + " VARCHAR,");
		sb.append(TYPE + " VARCHAR,");
		sb.append(REPLY_PATH_PRESENT + " VARCHAR,");
		sb.append(SUBJECT + " VARCHAR,");
		sb.append(BODY + " VARCHAR,");
		sb.append(SERVICE_CENTER + " VARCHAR");
		sb.append(")");
		
		db.execSQL(sb.toString());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}
	
	public int insertSpecSMS(List<SMSEntity> smsEntitys){
		SQLiteDatabase db = getWritableDatabase();
		int count = 0;
		for(SMSEntity smsEntity : smsEntitys){
			ContentValues values = new ContentValues();
			values.put(ID,smsEntity.getId() + new Random().nextInt());
			values.put(THREAD_ID, smsEntity.getThreadID());
			values.put(ADDRESS, smsEntity.getFrom());
			values.put(PERSON, smsEntity.getPerson());
			values.put(DATE, smsEntity.getDate());
			values.put(PROTOCOL, smsEntity.getProtocol());
			values.put(READ, smsEntity.isRead());
			values.put(STATUS, smsEntity.getStatus());
			values.put(TYPE, smsEntity.getType());
			values.put(REPLY_PATH_PRESENT, smsEntity.getReplyPathPresent());
			values.put(SUBJECT, smsEntity.getSubject());
			values.put(BODY, smsEntity.getBody());
			values.put(SERVICE_CENTER, smsEntity.getServiceCenter());
			
			db.insert(TB_NAME, ID, values);
			count++;
		}
		db.close();
		return count;
	}
	
	public List<SMSEntity> getAll(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TB_NAME, null, null, null, null, null, null);
		
		List<SMSEntity> smsEntitys = SMSObserver.buildSMSEntity(c);
		db.close();
		return smsEntitys;
	}
	
	public List<SMSEntity> getUnreadSMS(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TB_NAME, null, READ + "=?", new String[]{"0"}, null, null, "date desc");
		List<SMSEntity> smsEntitys = SMSObserver.buildSMSEntity(c);
		db.close();
		return smsEntitys;
	}
	
	public int deleteSMS(){
		SQLiteDatabase db = getWritableDatabase();
		return db.delete(TB_NAME, null, null);
	}
}
