package com.cnm.cnmrc.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import com.cnm.cnmrc.TvReservingConfirm;

public class AlarmReceiver extends BroadcastReceiver {
	private NotificationManager notificationManager;

	public void onReceive(Context context, Intent intent) {
		String mTitle = intent.getStringExtra("title");
		String mTime = intent.getStringExtra("time");
		
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification updateComplete = new Notification();
		updateComplete.icon = android.R.drawable.stat_notify_sync;
		updateComplete.tickerText = mTitle;
		updateComplete.when = System.currentTimeMillis();
		updateComplete.flags = Notification.FLAG_AUTO_CANCEL;
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		updateComplete.sound = uri;
		
		// error
//		Bundle bundle = new Bundle();
//		bundle.putString("title", mTitle);
//		bundle.putString("time", mTime);
//		updateComplete.extras = bundle;

		Intent notificationIntent = new Intent(context, TvReservingConfirm.class);
		notificationIntent.putExtra("title", mTitle);
		notificationIntent.putExtra("time", mTime);
		//PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		PendingIntent contentIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), notificationIntent, 0);
		
		String contentTitle = mTitle;
		String contentText = mTime;
		updateComplete.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notificationManager.notify((int)System.currentTimeMillis(), updateComplete);
	}
}