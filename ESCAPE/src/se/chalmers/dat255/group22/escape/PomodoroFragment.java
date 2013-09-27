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
	private Button startB;
	public TextView secondsLeftText;
	
	private final long startTime = 300 * 1000;
	private final long interval = 1 * 1000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.pomodoro_fragment, container, false);
		startB = (Button) v.findViewById(R.id.pomodoro_button);
		startB.setOnClickListener(this);
		secondsLeftText = (TextView) v.findViewById(R.id.pomodoro_timer);
		countDownTimer = new PomodoroTimer(startTime, interval);
		secondsLeftText.setText(formatTime(startTime));
		
		return v;
	}

	@Override
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

	public String formatTime(long startTime){
		
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
	        min= "0" + minutes;  

	    output = min + " : " + sec;  
	    return output;
	}
	
	public class PomodoroTimer extends CountDownTimer {
		public PomodoroTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			secondsLeftText.setText("Done!");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			secondsLeftText.setText(formatTime(millisUntilFinished));
		}
	}

}