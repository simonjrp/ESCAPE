package se.chalmers.dat255.group22.escape;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * A notofication handler for creating notifications that should appear at a
 * specific time.
 * 
 * @author Simon Persson
 * 
 */
public class NotificationHandler {

	private Context context;

	/**
	 * Creates a new notification handler.
	 * 
	 * @param context
	 *            Should be the context of the main activity of the app.
	 */
	public NotificationHandler(Context context) {
		this.context = context;
	}

	/**
	 * Method for adding a notifcation, or alarm, to the system's alarm service.
	 * 
	 * @param timeAlarm
	 *            A TimeAlarm object containing the time on which a notification
	 *            should appear.
	 */
	public void addNotification(TimeAlarm timeAlarm) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Date date = timeAlarm.getDate();

		// Creates an intent holding the AlarmReceiver
		Intent alarmIntent = new Intent(context, AlarmReceiver.class);

		/*
		 * Creates a PendingIntent that'll be used by the alarm manager
		 * (external application) to send a broadcast at a given time from the
		 * context (in this case, the MainActivity) to all receivers matching
		 * the alarmIntent. When AlarmReceiver (the matching receiver) receives
		 * the broadcast, it generates and sends a custom notification to the
		 * system.
		 */
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				alarmIntent, 0);

		// Gives the alarm manager a time and an operation to be performed (the
		// pending intent) at that time. RTC_WAKEUP tells the alarm manager to
		// wake upp the device when alarm goes off.
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
	}
}
