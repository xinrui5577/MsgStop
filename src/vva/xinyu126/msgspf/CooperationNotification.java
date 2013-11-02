package vva.xinyu126.msgspf;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class CooperationNotification {
	private List<SMSEntity> smsEntitys;
	private NotificationManager manager;
	private Context ctx;
	
	public static final int SIMPLE_NOTIFICATION_ID = 1;
	public CooperationNotification(Context ctx,List<SMSEntity> smsEntitys,NotificationManager manager) {
		this.smsEntitys = smsEntitys;
		this.manager = manager;
		this.ctx = ctx;
	}
	
	public void send(){
		int size = smsEntitys.size();
		if(size == 1){
			SMSEntity sms = smsEntitys.get(0);
			Notification notify ;
			Intent notifyIntent = new Intent(ctx,MainActivity.class);
			PendingIntent intent = PendingIntent.getActivity(ctx, 0, notifyIntent,
					android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
			notify = new Notification.Builder(ctx)
				.setSmallIcon(R.drawable.a6icon)
				.setContentTitle(sms.getFrom())
				.setContentText(sms.getBody())
				.setContentIntent(intent)
				.build();
			manager.notify(SIMPLE_NOTIFICATION_ID,notify);
		}if(size == 0){
			return;
		}else{
			Intent notifyIntent = new Intent(ctx,MainActivity.class);
			PendingIntent intent = PendingIntent.getActivity(ctx, 0, notifyIntent,
					android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
			Notification notify = new Notification.Builder(ctx)
			.setContentTitle("协同信息")
			.setContentText("你有" + size + "条新的协同信息未查看")
			.setContentIntent(intent)
			.build();
			notify.number = size;
			manager.notify(SIMPLE_NOTIFICATION_ID,notify);
		}
	}
}
