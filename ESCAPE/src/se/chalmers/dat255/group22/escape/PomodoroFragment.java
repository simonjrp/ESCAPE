package se.chalmers.dat255.group22.escape;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PomodoroFragment extends Fragment implements OnClickListener {

	private CountDownTimer countDownTimer;
	private boolean timerHasStarted = false;

	// Button to start pomodoro timer
	private Button startB;

	// TextView that shows the time left on the timer in seconds(shows
	// minutes:seconds format after running formatTime())
	public TextView timeLeftText;

	// startTime defines the amount of time in the pomodoro timer. 1500 seconds
	// = 25 minutes.
	private final long startTime = 1500 * 1000;
	private final long interval = 1 * 1000;

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

		// Initializing count down timer
		countDownTimer = new PomodoroTimer(startTime, interval);

		// Setting start time of the pomodoro timer
		timeLeftText.setText(formatTime(startTime));

		return v;
	}

	/*
	 * When clicking on start button, either start pomodoro timer (if it hasn't
	 * started) and change button text to STOP, otherwise stop running timer and
	 * change button text to RESTART.
	 */@Override
	public void onClick(View v) {
		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
			startB.setText("STOP");
		} else {
			countDownTimer.cancel();
			timerHasStarted = false;
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
			timeLeftText.setText("Done!");
		}

		// Method to update the display of minutes:seconds left
		@Override
		public void onTick(long millisUntilFinished) {
			timeLeftText.setText(formatTime(millisUntilFinished));
		}
	}

}