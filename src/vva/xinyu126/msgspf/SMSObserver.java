package vva.xinyu126.msgspf;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
/**
 * 拦截短信并将符合条件的短信插入到指定数据库，并删除inbox中的数据
 * @author Administrator
 *
 */
public class SMSObserver extends ContentObserver{
	private Context ctx;
	private ContentResolver contentResolver;
	private final static String SMS_INBOX_URI = "content://sms//inbox";
	private final static String CONVERSATION_URI = "content://sms/conversations/";
	private SMSDatabaseHelper helper;
	
	public static final String TAG = "sms";
	
	public final static String DB_NAME = "cooperation_db";
	public final static int VERSION = 1;
	
	public final static String FILTER_BODY = "seeyou";
	public SMSObserver(Handler handler,Context ctx){
		super(handler);
		this.ctx = ctx;
		this.contentResolver = ctx.getContentResolver();
		helper = new SMSDatabaseHelper(ctx,DB_NAME,null,VERSION);
	}
	
	
	
	@Override
	public void onChange(boolean selfChange) {
		Log.v(TAG, "capture a message into inbox!");
		super.onChange(selfChange);
		contentResolver = ctx.getContentResolver();
		Uri uri = Uri.parse(SMS_INBOX_URI);
		String [] queryColumn = new String[]{"_id,thread_id,address,person,date,protocol,read,status,type,reply_path_present,subject,body,service_center"};
		Cursor cursor = contentResolver.query(uri, queryColumn, " read=?", new String[]{"0"}, "date desc");
//		Cursor cursor = contentResolver.query(uri, null, null, new String[]{"0"}, null);
		
		if(cursor != null){
			List<SMSEntity> smsEntitys = buildSMSEntity(cursor);
			List<SMSEntity> filteredEntitys = filterSMSEntity(FILTER_BODY, smsEntitys);
			storeSpecSMS(filteredEntitys);
			deleteSpecSMS(filteredEntitys);
			
			smsEntitys = helper.getUnreadSMS();
			NotificationManager manager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			CooperationNotification cn = new CooperationNotification(ctx,smsEntitys,manager);
			cn.send();
		}
	}
	
	private int deleteSpecSMS(List<SMSEntity> smsEntitys){
		int count = 0;
		for(SMSEntity smsEntity : smsEntitys){
			Uri thread = Uri.parse(CONVERSATION_URI + smsEntity.getThreadID());
			contentResolver.delete(thread, "_id=?", new String[]{String.valueOf(smsEntity.getId())});
			count++;
		}
		return count;
	}
	
	private void storeSpecSMS(List<SMSEntity> smsEntitys){
		helper.insertSpecSMS(smsEntitys);
	}
	
	public static List<SMSEntity> buildSMSEntity(Cursor cursor){
		List<SMSEntity> smsEntitys = new ArrayList<SMSEntity>(10);		
		while(cursor.moveToNext()){
			SMSEntity smsEntity = new SMSEntity();
			smsEntity.setId(cursor.getLong(0));
			smsEntity.setThreadID(cursor.getLong(1));
			smsEntity.setFrom(cursor.getString(2));
			smsEntity.setPerson(cursor.getString(3));
			smsEntity.setDate(cursor.getString(4));
			smsEntity.setProtocol(cursor.getString(5));
			smsEntity.setRead(cursor.getInt(6));
			smsEntity.setStatus(cursor.getInt(7));
			smsEntity.setType(cursor.getString(8));
			smsEntity.setReplyPathPresent(cursor.getString(9));
			smsEntity.setSubject(cursor.getString(10));
			smsEntity.setBody(cursor.getString(11));
			smsEntity.setServiceCenter(cursor.getString(12));			
			smsEntitys.add(smsEntity);
		}
		return smsEntitys;
	}
	
	private List<SMSEntity> filterSMSEntity(String specStr,List<SMSEntity> smsEntitys){
		List<SMSEntity> filteredSMSEntitys = new ArrayList<SMSEntity>();
		for(SMSEntity smsEntity : smsEntitys){
			if(smsEntity.getFrom().startsWith("1065")){
				filteredSMSEntitys.add(smsEntity);
			}
		}
		return filteredSMSEntitys;
	}
}
