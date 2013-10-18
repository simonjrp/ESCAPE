package se.chalmers.dat255.group22.escape.fragments;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PomodoroService extends Service {
	
	public PomodoroService() {
		
	}
	
	int counter = 0;
	static final int UPDATE_INTERVAL = 1000;
	private Timer timer = new Timer();
	
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
		Log.d("Pomodoro",intent.getStringExtra("ServiceTest"));
		
		

		return START_STICKY;
	}

	private void doSomethingRepeatedly() {
	
		Log.d("Pomodoro","Service - doSomethingRepeatedly");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("Pomodoro","Service - onDestroy");
		if (timer != null) {
			timer.cancel();
		}
	}
}