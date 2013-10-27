package se.chalmers.dat255.group22.escape.fragments;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class PomodoroService extends Service {

	public PomodoroService() {

	}

	private CountDownTimer servicePomodoroTimer;
	private long activityTimerSecondsLong;
	private final long interval = 1 * 1000;
	public String secondsInString;
	public String secondsInStringTest;
	private long secondsInLong;

	public String serviceActiveTimer;
	public String activityTimerSeconds;

	int counter = 0;
	static final int UPDATE_INTERVAL = 1000;

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("Pomodoro", "Service - onBind");
		return null;
	}

	public void onStart(Intent intent, int startId) {
		Log.d("Pomodoro", "In the service - onStart");
	}

	public void onCreate(Intent intent, int flags, int startId) {
		Log.d("Pomodoro", "In the service - onCreate");
	}

	// When the service starts, get the active timer and the seconds
	// on the active timer
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("Pomodoro", "PomodoroService started!!!!!!");

		// This is where the data from the activity is received
		Log.d("Pomodoro", intent.getStringExtra("activeTimer"));
		Log.d("Pomodoro", intent.getStringExtra("secondsOnTimer"));

		serviceActiveTimer = intent.getStringExtra("activeTimer");
		activityTimerSeconds = intent.getStringExtra("secondsOnTimer");
		activityTimerSecondsLong = Long.parseLong(activityTimerSeconds);
		activityTimerSecondsLong = activityTimerSecondsLong * 1000;
		Log.d("Pomodoro", serviceActiveTimer);

		// Starting service timer
		servicePomodoroTimer = new ServiceTimer(activityTimerSecondsLong,
				interval);
		servicePomodoroTimer.start();
		Log.d("Pomodoro", "Service timer started with hopefully 150 seconds???");

		// This is where messages are sent from the service to the activity
		// String serviceTestString = secondsInString;
		// secondsInStringTest = "20";

		// If the Pomodoro timer was active in the activity, inform activity
		// about it (the activity
		// reads this message when it is resumed, so that it knows if it should
		// start the pomodoro
		// timer or the break timer)
		if (serviceActiveTimer.contains("ON_POMODORO")) {

			Log.d("Pomodoro", "The service timer is ON_POMODORO!");
			Intent serviceRunningIntent = new Intent(
					PomodoroFragment.RECEIVE_STATUS);
			serviceRunningIntent
					.putExtra("serviceRunningMsg", "POMODORO_TIMER");
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					serviceRunningIntent);
		}
		// If break timer was active in the activity, inform activity break
		// timer was active
		else if (serviceActiveTimer.contains("ON_BREAK")) {

			Log.d("Pomodoro", "The service timer is ON_BREAK!");
			Intent serviceRunningIntent = new Intent(
					PomodoroFragment.RECEIVE_STATUS);
			serviceRunningIntent.putExtra("serviceRunningMsg", "BREAK_TIMER");
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					serviceRunningIntent);
		}
		// Else, inform activity that the service timer was active
		else {

			Log.d("Pomodoro", "The service timer is ON_SERVICE!");
			Intent serviceRunningIntent = new Intent(
					PomodoroFragment.RECEIVE_STATUS);
			serviceRunningIntent.putExtra("serviceRunningMsg", "SERVICE_TIMER");
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					serviceRunningIntent);

		}

		return START_STICKY;
	}

	// Countdown timer for the service
	public class ServiceTimer extends CountDownTimer {
		public ServiceTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		// When timer has reached zero, display "Timer up!" instead of time.
		@Override
		public void onFinish() {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					PomodoroService.super.getApplicationContext());
			builder.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Pomodoro").setContentText("Timer up!");

			// Enables sound and vibration for the notification
			builder.setDefaults(Notification.DEFAULT_ALL);

			// Creates an intent with the activity to launch when
			// notification is
			// clicked
			Intent resultIntent = new Intent(
					PomodoroService.super.getApplicationContext(),
					MainActivity.class);

			// Creates a back stack to be used by the launched activity.
			TaskStackBuilder stackBuilder = TaskStackBuilder
					.create(PomodoroService.super.getApplicationContext());

			// Adds all the parent activities/views of the
			// activity to be opened when clicking on the notification to
			// the back
			// stack. Might be useful later, if clicking on the notification
			// does
			// not take you to a parent view.
			stackBuilder.addParentStack(MainActivity.class);

			// Adds the Intent that starts the Activity to the top of the
			// stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);

			// Assigns the pending intent (containing MainActivity with a
			// proper
			// back stack) to the notification
			builder.setContentIntent(resultPendingIntent);

			// Sends the notification to the android system
			NotificationManager mNotificationManager = (NotificationManager) PomodoroService.super
					.getApplicationContext().getSystemService(
							Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(0, builder.build());

			Log.d("Pomodoro", "ServiceTimer is at 0!");
		}

		// PROBLEM - secondsInString here is not visible when sending it to
		// activity :X

		// Method to update the display of minutes:seconds left
		@Override
		public void onTick(long millisUntilFinished) {
			secondsInLong = millisUntilFinished / 1000;
			secondsInString = Long.toString(secondsInLong);

			Intent serviceIntent = new Intent(PomodoroFragment.RECEIVE_TIME);
			serviceIntent.putExtra("serviceToActivity", secondsInString);
			LocalBroadcastManager.getInstance(
					PomodoroService.super.getApplicationContext())
					.sendBroadcast(serviceIntent);
			Log.d("Pomodoro", secondsInString);
		}
	}

	// When the service is destroyed, stop the service timer
	@Override
	public void onDestroy() {
		super.onDestroy();
		servicePomodoroTimer.cancel();
		Log.d("Pomodoro", "Service - onDestroy");

	}
}
