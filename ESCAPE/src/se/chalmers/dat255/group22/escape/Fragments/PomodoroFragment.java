package se.chalmers.dat255.group22.escape.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.R;

public class PomodoroFragment extends Fragment implements OnClickListener {

	private CountDownTimer pomodoroCountDownTimer;
	private CountDownTimer breakCountDownTimer;
	private boolean pomodoroTimerHasStarted = false;
	private boolean breakTimerHasStarted = false;
	private boolean onBreak = false;

	// Button to start pomodoro timer
	private Button startB;

	// TextView that shows the time left on the timer in seconds(shows
	// minutes:seconds format after running formatTime())
	public TextView timeLeftText;

	// startTime defines the amount of time in the pomodoro timer. 
	// 1500 seconds = 25 minutes.
	private long pomodoroStartTime=10*1000;
	private long breakStartTime = 5*1000;
	private final long interval = 1 * 1000;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		if(onBreak==false){
//			startTime=10*1000;
//		}
//		else{
//			startTime=5*1000;
//		}
		
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
		
		//Setting start time of the break timer
		timeLeftText.setText(formatTime(breakStartTime));
		
		//Setting up notifications
		
//				NotificationCompat.Builder mBuilder =
//			    new NotificationCompat.Builder(this)
//			    .setSmallIcon(R.drawable.ic_launcher)
//			    .setContentTitle("My notification")
//			    .setContentText("Hello World!");
//				
//				Intent resultIntent = new Intent(this, ResultActivity.class);
//				
//				// Because clicking the notification opens a new ("special") activity, there's
//				// no need to create an artificial back stack.
//				PendingIntent resultPendingIntent =
//				    PendingIntent.getActivity(
//				    this,
//				    0,
//				    resultIntent,
//				    PendingIntent.FLAG_UPDATE_CURRENT
//				);
//				PendingIntent resultPendingIntent;
//				mBuilder.setContentIntent(resultPendingIntent);
//				
//				NotificationCompat.Builder mBuilder;
//				
//				// Sets an ID for the notification
//				int mNotificationId = 001;
//				// Gets an instance of the NotificationManager service
//				NotificationManager mNotifyMgr = 
//				        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//				// Builds the notification and issues it.
//				mNotifyMgr.notify(mNotificationId, mBuilder.build());
				
				
				
				
				
		//End of notifications setup
		return v;
	}

	//Notifications handled below	
	
	/*
	 * When clicking on start button, either start pomodoro timer (if it hasn't
	 * started) and change button text to STOP, otherwise stop running timer and
	 * change button text to RESTART.
	 */@Override
	public void onClick(View v) {
		if (!pomodoroTimerHasStarted && onBreak==false) {
		//	startTime=15*1000;
			pomodoroCountDownTimer.start();
			pomodoroTimerHasStarted = true;
			startB.setText("STOP");
		} 
		else if (!breakTimerHasStarted && onBreak==true){
		//	startTime=300*1000;
			breakCountDownTimer.start();
			breakTimerHasStarted = true;
			startB.setText("STOP");
		}
		else if (pomodoroTimerHasStarted && onBreak==false) {
			pomodoroCountDownTimer.cancel();
			pomodoroTimerHasStarted = false;
			onBreak=false;
			startB.setText("RESTART");
		}
		else if (breakTimerHasStarted && onBreak==true){
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

	
	// Count down timer is handled below

	public class PomodoroTimer extends CountDownTimer {
		public PomodoroTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		// When timer has reached zero, display "Done!" instead of time.
		@Override
		public void onFinish() {
			//timeLeftText.setText("Done!");
			if(onBreak==false){
				onBreak=true;
				timeLeftText.setText("Breaktime!");
				Intent intent = new Intent(getActivity(),MainActivity.class);
				
				 Notification noti = new Notification.Builder(getActivity())
			        .setContentTitle("ESCAPE")
			        .setContentText("Time for a Pomodoro break!").setSmallIcon(R.drawable.ic_launcher)
			        .build();
			    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
			    // Hide the notification after its selected
			    noti.flags |= Notification.FLAG_AUTO_CANCEL;

			    notificationManager.notify(0, noti);
				
				
			}
			else if(onBreak==true){
				onBreak=false;
				timeLeftText.setText("Break over!");
				
				Intent intent = new Intent(getActivity(),MainActivity.class);
				
				 Notification noti = new Notification.Builder(getActivity())
			        .setContentTitle("ESCAPE")
			        .setContentText("Pomodoro break over!").setSmallIcon(R.drawable.ic_launcher)
			        .build();
			    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
			    // Hide the notification after its selected
			    noti.flags |= Notification.FLAG_AUTO_CANCEL;

			    notificationManager.notify(0, noti);
			}
		}

		// Method to update the display of minutes:seconds left
		@Override
		public void onTick(long millisUntilFinished) {
			timeLeftText.setText(formatTime(millisUntilFinished));
		}
	}

}