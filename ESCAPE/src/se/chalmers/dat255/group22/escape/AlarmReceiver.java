package se.chalmers.dat255.group22.escape;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Custom broadcast receiver used to create notifications for task/event
 * reminders
 * 
 * @author Simon Persson
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle args = intent.getExtras();
		// Creates a notification with some simple test text (for now)
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(args.getString("TITLE"))
				.setContentText(args.getString("DESC"));

		// Enables sound and vibration for the notification
		notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

		// Creates an intent with the activity to launch when notification is
		// clicked
		Intent resultIntent = new Intent(context, MainActivity.class);

		// Creates a back stack to be used by the launched activity.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		// Adds all the parent activities/views of the
		// activity to be opened when clicking on the notification to the back
		// stack. Might be useful later, if clicking on the notification does
		// not take you to a parent view.
		stackBuilder.addParentStack(MainActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Assigns the pending intent (containing MainActivity with a proper
		// back stack) to the notification
		notificationBuilder.setContentIntent(resultPendingIntent);

		// Sends the notification to the android system
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, notificationBuilder.build());
	}

}
