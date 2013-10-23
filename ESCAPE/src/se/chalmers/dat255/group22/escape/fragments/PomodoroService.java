package se.chalmers.dat255.group22.escape.fragments;
import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.fragments.PomodoroFragment.PomodoroTimer;
import android.os.CountDownTimer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class PomodoroService extends Service {
	
	public PomodoroService() {
		
	}
	private CountDownTimer servicePomodoroTimer;
	private long serviceStartTime = 10 * 1000;
	private long activityTimerSecondsLong;
	private final long interval = 1 * 1000;
	private String secondsInString;
	public String serviceActiveTimer;
	public String activityTimerSeconds;
	
	int counter = 0;
	static final int UPDATE_INTERVAL = 1000;
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("Pomodoro","Service - onBind");
		return null;
	}

	public void onStart(Intent intent, int startId){
		Log.d("Pomodoro","In the service - onStart");
	}
	
	public void onCreate(Intent intent, int flags, int startId){
		Log.d("Pomodoro", "In the service - onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		Log.d("Pomodoro","PomodoroService started!!!!!!");
		doSomethingRepeatedly();
		
		//This is where the data from the activity is received
		Log.d("Pomodoro",intent.getStringExtra("activeTimer"));
		Log.d("Pomodoro",intent.getStringExtra("secondsOnTimer"));
		
		serviceActiveTimer = intent.getStringExtra("activeTimer");
		activityTimerSeconds = intent.getStringExtra("secondsOnTimer");
		activityTimerSecondsLong = Long.parseLong(activityTimerSeconds);
		activityTimerSecondsLong = activityTimerSecondsLong*1000;
		Log.d("Pomodoro",serviceActiveTimer);
		

		// App crashes if I use this if statement, wtf?!?!
		//		if(serviceActiveTimer=="ON_BREAK"){
//			
//			servicePomodoroTimer = new ServiceTimer(activityTimerSecondsLong, interval);
//			servicePomodoroTimer.start();
//			Log.d("Pomodoro","Service timer started with hopefully 150 seconds???");
//			
//		}
//		else {
//			Log.d("Pomodoro","I guess the Pomodoro timer is active");
//		}
		
		servicePomodoroTimer = new ServiceTimer(activityTimerSecondsLong, interval);
		servicePomodoroTimer.start();
		Log.d("Pomodoro","Service timer started with hopefully 150 seconds???");
		
		
		//This is where messages are sent from the service to the activity
		//String serviceTestString = secondsInString;	
				Intent serviceIntent = new Intent(PomodoroFragment.RECEIVE_TIME);
//				serviceIntent.putExtra("serviceToActivity", serviceTestString);
				serviceIntent.putExtra("serviceToActivity", "SecondFilterWorks");
				LocalBroadcastManager.getInstance(this).sendBroadcast(serviceIntent);
		
		Intent serviceRunningIntent = new Intent(PomodoroFragment.POMODORO_SERVICE);
//		serviceRunningIntent.putExtra("serviceRunningMsg", serviceRunningString);
		serviceRunningIntent.putExtra("serviceRunningMsg", "RUNNING");
		LocalBroadcastManager.getInstance(this).sendBroadcast(serviceRunningIntent);
		
		
		
		

		return START_STICKY;
	}

	private void doSomethingRepeatedly() {
	
		Log.d("Pomodoro","Service - doSomethingRepeatedly");
	}

	public class ServiceTimer extends CountDownTimer {
		public ServiceTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		// When timer has reached zero, display "Break time!" instead of time.
		@Override
		public void onFinish() {

			Log.d("Pomodoro","ServiceTimer is at 0!");
			
		}

		// Method to update the display of minutes:seconds left
		@Override
		public void onTick(long millisUntilFinished) {
		long secondsInLong = millisUntilFinished/1000;
		secondsInString = String.valueOf(secondsInLong);
		Log.d("Pomodoro",secondsInString);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	
		servicePomodoroTimer.cancel();
		Log.d("Pomodoro","Service - onDestroy");
		
	}
}