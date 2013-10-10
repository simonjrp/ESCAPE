package se.chalmers.dat255.group22.escape;

import java.sql.Date;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
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
 * Custom broadcast receiver used to create notifications for task/event. Also
 * handles user clicks on the action buttons of the notifications created with
 * it.
 * 
 * @author Simon Persson
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * String constant representing the intent filter action name for creating a
	 * new time reminder notification.
	 */
	public static final String NEW_TIME_NOTIFICATION = "se.chalmers.dat255.group22.escape.AlarmReceiver.NEW_TIME_NOTIFICATION";
	/**
	 * String constant representing the intent filter action name for creating a
	 * new location reminder notification.
	 */
	public static final String NEW_LOCATION_NOTIFICATION = "se.chalmers.dat255.group22.escape.AlarmReceiver.NEW_LOCATION_NOTIFICATION";
	/**
	 * String constant representing the intent filter action name for marking a
	 * task as done directly from the notification itself.
	 */
	public static final String NOTIFICATION_DONE = "se.chalmers.dat255.group22.escape.AlarmReceiver.NOTIFICATION_ACTION_DONE";
	/**
	 * String constant representing the intent filter action name for snoozing a
	 * task reminder directly from the notification itself.
	 */
	public static final String NOTIFICATION_SNOOZE = "se.chalmers.dat255.group22.escape.AlarmReceiver.NOTIFICATION_ACTION_SNOOZE";

	/*
	 * Method called when a new location reminder notification is to be created
	 */
	private void newLocationNotification(Context context, Intent intent) {
		Bundle args = intent.getExtras();

		// Creates the description text for the notification. Includes date and
		// time if the source for the notification is an event, otherwise not.
		String description = args
				.getBoolean(NotificationHandler.NOTOFICATION_IS_EVENT) ? args
				.getString(NotificationHandler.NOTIFICATION_EVENT_TIME)
				+ ", "
				+ args.getString(NotificationHandler.NOTIFICATION_DESC) : args
				.getString(NotificationHandler.NOTIFICATION_DESC);

		// Creates a notification with data such as title and comment
		// from the ListObject for which the notification is created
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(
						args.getString(NotificationHandler.NOTIFICATION_TITLE))
				.setContentText(description)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(description));

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
		mNotificationManager.notify(
				args.getInt(NotificationHandler.NOTIFICATION_ID),
				notificationBuilder.build());
	}

	/*
	 * Method called when a new time reminder notification is to be created
	 */
	private void newTimeNotification(Context context, Intent intent) {
		Bundle args = intent.getExtras();

		// Creates the description text for the notification. Includes date and
		// time if the source for the notification is an event, otherwise not.
		String description = args
				.getBoolean(NotificationHandler.NOTOFICATION_IS_EVENT) ? args
				.getString(NotificationHandler.NOTIFICATION_EVENT_TIME)
				+ ", "
				+ args.getString(NotificationHandler.NOTIFICATION_DESC) : args
				.getString(NotificationHandler.NOTIFICATION_DESC);

		// Creates the intents containing the actions to be performed when
		// clicking on notification action buttons
		Intent doneIntent = new Intent();
		doneIntent.setAction(NOTIFICATION_DONE);
		doneIntent.putExtra(NotificationHandler.NOTIFICATION_ID,
				args.getInt(NotificationHandler.NOTIFICATION_ID));
		PendingIntent donePendingIntent = PendingIntent.getBroadcast(context,
				0, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent snoozeIntent = new Intent();
		snoozeIntent.setAction(NOTIFICATION_SNOOZE);
		snoozeIntent.putExtra(NotificationHandler.NOTIFICATION_ID,
				args.getInt(NotificationHandler.NOTIFICATION_ID));
		PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context,
				0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Creates a notification with data such as title and comment
		// from the ListObject for which the notification is created
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(
						args.getString(NotificationHandler.NOTIFICATION_TITLE))
				.setContentText(description)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(description))
				.addAction(R.drawable.task_done,
						context.getString(R.string.notification_done),
						donePendingIntent)
				.addAction(R.drawable.task_snooze,
						context.getString(R.string.notification_snooze),
						snoozePendingIntent);

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
		mNotificationManager.notify(
				args.getInt(NotificationHandler.NOTIFICATION_ID),
				notificationBuilder.build());
	}

	/*
	 * Method called when a task should be marked as done.
	 */
	private void notificationDone(Context context, Intent intent) {
		long id = intent.getIntExtra(NotificationHandler.NOTIFICATION_ID, 0);
		DBHandler dbH = new DBHandler(context);
		dbH.purgeListObject(dbH.getListObject(id));
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel((int) id);
	}

	/*
	 * Method called when a task reminder should be snoozed one hour.
	 */
	private void notificationSnooze(Context context, Intent intent) {
		long id = intent.getIntExtra(NotificationHandler.NOTIFICATION_ID, 0);
		DBHandler dbH = new DBHandler(context);
		ListObject listObject = dbH.getListObject(id);

		// Deletes old time alarm.
		TimeAlarm oldTimeAlarm = dbH.getTimeAlarm(listObject);
		dbH.deleteListObjectWithTimeAlarm(listObject);
		dbH.deleteTimeAlarm(oldTimeAlarm);

		// Creates new time alarm, one hour from current time.
		long oneHourInMillis = 3600000;
		Date newDate = new Date(System.currentTimeMillis() + oneHourInMillis);
		TimeAlarm newTimeAlarm = new TimeAlarm(0, newDate);
		long idTimeAlarm = dbH.addTimeAlarm(newTimeAlarm);
		dbH.addListObjectWithTimeAlarm(listObject,
				dbH.getTimeAlarm(idTimeAlarm));

		// Creates new notification reminder with the new time alarm.
		NotificationHandler.getInstance().addTimeReminder(listObject);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel((int) id);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(NEW_TIME_NOTIFICATION)) {
			newTimeNotification(context, intent);
		} else if (action.equals(NEW_LOCATION_NOTIFICATION)) {
			newLocationNotification(context, intent);
		} else if (action.equals(NOTIFICATION_DONE)) {
			notificationDone(context, intent);
		} else if (action.equals(NOTIFICATION_SNOOZE)) {
			notificationSnooze(context, intent);
		}
	}

}
