package com.example.mah.services;

import com.example.mah.AppRes;
import com.example.mah.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Notifications {

	private NotificationManager notMng = null;
	private Context context;
	
	public Notifications(Context context) {
		this.context = context;
		notMng = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/** create notification with a unique ID and the class you want to open */
	public void initNotification(int notify_ID, Class cls, String content) {
		Intent noteIntent = new Intent(context, cls);
		noteIntent.putExtra(AppRes.DIFFICULTY , notify_ID);
		initNotification(notify_ID, noteIntent, content);
	}
	
	/** create notification with a unique ID and Intent to the class you want to open */
	public void initNotification(int notify_ID, Intent noteIntent, String content) {
		Notification note = new Notification(R.drawable.ic_launcher, "Heroes & Monsters", System.currentTimeMillis());
		// create the intent with the class you want to open when you press the notification
    	PendingIntent pendIntent = PendingIntent.getActivity(context, 0, noteIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
    	note.setLatestEventInfo(context, "Heroes & Monsters", content, pendIntent);
    	//note.number = ++count;
    	note.flags |= Notification.FLAG_AUTO_CANCEL;
    	note.defaults |= Notification.DEFAULT_ALL;
    	notMng.notify(notify_ID, note);
	}
	
	public void cancelNotification(int notify_ID){
		notMng.cancel(notify_ID);
	}
}
