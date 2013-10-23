package se.chalmers.dat255.group22.escape.fragments;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PomodoroFragment extends Fragment implements OnClickListener {

	private CountDownTimer pomodoroCountDownTimer;
	private CountDownTimer breakCountDownTimer;
	private boolean pomodoroTimerHasStarted = false;
	private boolean breakTimerHasStarted = false;
	private boolean onBreak = false;
	private String pomodoroServiceRunning;
	private String serviceTimeString;

	// Button to start pomodoro timer
	private Button startB;

	// TextView that shows the time left on the timer in seconds(shows
	// minutes:seconds format after running formatTime())
	public TextView timeLeftText;

	// startTime defines the amount of time in the pomodoro timer.
	// 1500 seconds = 25 minutes.
	private long pomodoroStartTime = 10 * 1000;
	private long breakStartTime = 5 * 1000;
	private final long interval = 1 * 1000;
	
	public static final String RECEIVE_TIME = "se.chalmers.dat255.group22.escape.fragments.PomodoroFragment.RECEIVE_TIME";
	public static final String POMODORO_SERVICE = "se.chalmers.dat255.group22.escape.fragments.PomodoroFragment.POMODORO_SERVICE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.pomodoro_fragment, container, false);

		
		
		// Layout for the pomodoro start button
		startB = (Button) v.findViewById(R.id.pomodoro_button);

		// Listener for pomodoro start button
		startB.setOnClickListener(this);

		// Layout for the time display in the pomodoro timer
		timeLeftText = (TextView) v.findViewById(R.id.pomodoro_timer);

		// Initializing pomodoro count down timer
		pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime, interval);

		// Initializing break count down timer
		breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

		// Setting start time of the pomodoro timer
		timeLeftText.setText(formatTime(pomodoroStartTime));

		// Setting start time of the break timer
		timeLeftText.setText(formatTime(breakStartTime));
		
		//Setting up broadcast manager to handle communication between service and activity
				LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(getActivity());
				//Getting time from service
				IntentFilter receiveTimeFilter = new IntentFilter();
				receiveTimeFilter.addAction(RECEIVE_TIME);
				bManager.registerReceiver(bReceiver, receiveTimeFilter);
				//Asking if service is running
				IntentFilter pomodoroRunningFilter = new IntentFilter();
				pomodoroRunningFilter.addAction(POMODORO_SERVICE);
				bManager.registerReceiver(bReceiver, pomodoroRunningFilter);
		
		if(pomodoroServiceRunning=="RUNNING" && serviceTimeString=="SecondFilterWorks"){
			Log.d("Pomodoro","OnCreate - Service was running before this! AND second filter works!");
			//Log.d("Pomodoro","serviceTimeString shows " + serviceTimeString + " seconds!");
		}
		
		else if(pomodoroServiceRunning=="RUNNING" && serviceTimeString!="SecondFilterWorks"){
			Log.d("Pomodoro","OnCreate - Service was running before this! Second filter FUCKED UP");
			//Log.d("Pomodoro","serviceTimeString shows " + serviceTimeString + " seconds!");
		}
		
		else{
			Log.d("Pomodoro","OnCreate - Service was not running before this!");
		}


		
		return v;
	}
	//Used to be private
	public BroadcastReceiver bReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(RECEIVE_TIME)){
				serviceTimeString = intent.getStringExtra("serviceToActivity");
				//Log.d("Pomodoro","From broadcast receiver:");
				//Log.d("Pomodoro",serviceTimeString);
				//Log.d("Pomodoro","end of broadcast receiver message.");
			}
			if(intent.getAction().equals(POMODORO_SERVICE)){
				pomodoroServiceRunning = intent.getStringExtra("serviceRunningMsg");
				Log.d("Pomodoro", pomodoroServiceRunning);
			}
		}
	};

	
	
	public void startService(View view) {

		Log.d("Pomodoro","In the PomodoroFragment/startService method - before");
		Intent intent = new Intent(getActivity(),PomodoroService.class);
		intent.putExtra("ServiceTest", "Sending data from Activity to Service now works!");
		getActivity().startService(intent);		
		Log.d("Pomodoro","In the PomodoroFragment/startService method - after");
		
	}

	public void stopService(View view) {
		Log.d("Pomodoro","In the PomodoroFragment/stopService method");
		getActivity().stopService(new Intent(getActivity(), PomodoroService.class));
	}
	
	/*
	 * When clicking on start button, either start the timer (if it hasn't
	 * started) and change button text to STOP, otherwise stop running timer and
	 * change button text to RESTART.
	 */
	@Override
	public void onClick(View v) {
		if (!pomodoroTimerHasStarted && onBreak == false) {
			// startTime=15*1000;
			pomodoroCountDownTimer.start();
			pomodoroTimerHasStarted = true;
			startB.setText("STOP");
			Log.d("Pomodoro","Pomodoro - pressing start button");
//			startService(getActivity().findViewById(R.id.pomodoro_button)); //-- this one works	
		} else if (!breakTimerHasStarted && onBreak == true) {
			breakCountDownTimer.start();
			breakTimerHasStarted = true;
			startB.setText("STOP");
		} else if (pomodoroTimerHasStarted && onBreak == false) {
			pomodoroCountDownTimer.cancel();
			pomodoroTimerHasStarted = false;
			onBreak = false;
			startB.setText("RESTART");
			Log.d("PomodoroService","Pomodoro - pressing stop button");
//			stopService(getActivity().findViewById(R.id.pomodoro_button));
		} else if (breakTimerHasStarted && onBreak == true) {
			breakCountDownTimer.cancel();
			breakTimerHasStarted = false;
			startB.setText("RESTART");
		}
	}

	/**
	 * Method to format time from seconds to minutes:seconds
	 * 
	 * @param startTime
	 *            the initial time in seconds, set to 1500 seconds
	 * @return The formatted seconds displayed as minutes:seconds, for example
	 *         "25:00" instead of 1500 seconds
	 */
	public String formatTime(long startTime) {

		String output = "00:00";
		long seconds = startTime / 1000;
		long minutes = seconds / 60;

		seconds = seconds % 60;
		minutes = minutes % 60;

		String sec = String.valueOf(seconds);
		String min = String.valueOf(minutes);

		if (seconds < 10)
			sec = "0" + seconds;
		if (minutes < 10)
			min = "0" + minutes;

		output = min + " : " + sec;
		return output;
	}
	@Override
	public void onPause(){
		super.onPause();
		Log.d("Pomodoro","Fragment onPause");
		startService(getActivity().findViewById(R.id.pomodoro_button));
		//startService(getActivity().findViewById(R.id.pomodoro_button));
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d("Pomodoro","Fragment onResume");
		stopService(getActivity().findViewById(R.id.pomodoro_button));
	
		if(pomodoroServiceRunning=="RUNNING"){
			Log.d("Pomodoro","onResume - Service was running before this!");
			//Log.d("Pomodoro","serviceTimeString shows");
			//Log.d("Pomodoro",serviceTimeString);
		}
		else{
			Log.d("Pomodoro","onResume - Service was not running before this!");
		}
		
		//Log.d("Pomodoro",pomodoroServiceRunning);
		
	}

	// Count down timer is handled below
	/**
	 * 
	 * @author Adam
	 *
	 */
	public class PomodoroTimer extends CountDownTimer {
		public PomodoroTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		// When timer has reached zero, display "Break time!" instead of time.
		@Override
		public void onFinish() {

			if (onBreak == false) {
				onBreak = true;
				timeLeftText.setText("Break time!");

				// Creates a notification that informs the user it's time for a
				// break
				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
						getActivity()).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle("ESCAPE")
						.setContentText("Time for a break");

				// Enables sound and vibration for the notification
				notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

				// Creates an intent with the activity to launch when
				// notification is
				// clicked
				Intent resultIntent = new Intent(getActivity(),
						MainActivity.class);

				// Creates a back stack to be used by the launched activity.
				TaskStackBuilder stackBuilder = TaskStackBuilder
						.create(getActivity());

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
				PendingIntent resultPendingIntent = stackBuilder
						.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

				// Assigns the pending intent (containing MainActivity with a
				// proper
				// back stack) to the notification
				notificationBuilder.setContentIntent(resultPendingIntent);

				// Sends the notification to the android system
				NotificationManager mNotificationManager = (NotificationManager) getActivity()
						.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(0, notificationBuilder.build());
			} else if (onBreak == true) {
				onBreak = false;
				timeLeftText.setText("Break over!");

				// Creates a notification that informs the user it's time for a
				// break
				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
						getActivity()).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle("ESCAPE").setContentText("Break over");

				// Enables sound and vibration for the notification
				notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

				// Creates an intent with the activity to launch when
				// notification is
				// clicked
				Intent resultIntent = new Intent(getActivity(),
						MainActivity.class);

				// Creates a back stack to be used by the launched activity.
				TaskStackBuilder stackBuilder = TaskStackBuilder
						.create(getActivity());

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
				PendingIntent resultPendingIntent = stackBuilder
						.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

				// Assigns the pending intent (containing MainActivity with a
				// proper
				// back stack) to the notification
				notificationBuilder.setContentIntent(resultPendingIntent);

				// Sends the notification to the android system
				NotificationManager mNotificationManager = (NotificationManager) getActivity()
						.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(0, notificationBuilder.build());
			}
		}

		// Method to update the display of minutes:seconds left
		@Override
		public void onTick(long millisUntilFinished) {
			timeLeftText.setText(formatTime(millisUntilFinished));
		}
	}

}