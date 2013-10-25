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
        public String secondsInString;
        public String secondsInStringTest;
        private long secondsInLong;
        
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
                
                //Starting service timer
                servicePomodoroTimer = new ServiceTimer(activityTimerSecondsLong, interval);
                servicePomodoroTimer.start();
                Log.d("Pomodoro","Service timer started with hopefully 150 seconds???");
                
                
                //This is where messages are sent from the service to the activity
                //String serviceTestString = secondsInString;        
                secondsInStringTest = "20";
           		Intent serviceIntent = new Intent(PomodoroFragment.RECEIVE_TIME);
                serviceIntent.putExtra("serviceToActivity", secondsInStringTest);
                LocalBroadcastManager.getInstance(this).sendBroadcast(serviceIntent);
                
                //If the Pomodoro timer was active in the activity, inform activity about it (the activity
                //reads this message when it is resumed, so that it knows if it should start the pomodoro
                //timer or the break timer)
                if(serviceActiveTimer.contains("ON_POMODORO")){
                
                	Log.d("Pomodoro","The service timer is ON_POMODORO!");
                	Intent serviceRunningIntent = new Intent(PomodoroFragment.RECEIVE_STATUS);
                    serviceRunningIntent.putExtra("serviceRunningMsg", "POMODORO_TIMER");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(serviceRunningIntent);
                }
                //Else, inform activity break timer was active
                else{
                	
                	Log.d("Pomodoro","The service timer is ON_BREAK!");
                	Intent serviceRunningIntent = new Intent(PomodoroFragment.RECEIVE_STATUS);
                    serviceRunningIntent.putExtra("serviceRunningMsg", "BREAK_TIMER");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(serviceRunningIntent);
                }
                
                
                
                
                

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

                //PROBLEM - secondsInString here is not visible when sending it to activity :X
                
                // Method to update the display of minutes:seconds left
                @Override
                public void onTick(long millisUntilFinished) {
                secondsInLong = millisUntilFinished/1000;
                //secondsInString = String.valueOf(secondsInLong);
                secondsInString = Long.toString(secondsInLong);
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