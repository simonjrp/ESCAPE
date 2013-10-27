package se.chalmers.dat255.group22.escape.fragments;

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
import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.R;
/**
 * 
 * @author Adam Book
 * @Info Pomodoro productivity clock Provides a 25 minute-to-5-minute cycle
 *       timer to maximize productivity
 */

public class PomodoroFragment extends Fragment implements OnClickListener {

        private CountDownTimer pomodoroCountDownTimer;
        private CountDownTimer breakCountDownTimer;
        private CountDownTimer serviceCountDownTimer;
        private boolean pomodoroTimerHasStarted = false;
        private boolean breakTimerHasStarted = false;
        private boolean serviceTimerHasStarted = false;
        private boolean onBreak = false;
        private boolean serviceBreakTimerHasStarted = false;
        private boolean servicePomodoroTimerHasStarted = false;
        private String pomodoroServiceRunning;
        public String serviceTimeString;
        private long serviceTimeStringToLong;

        public long timeOnTimer;
        public String timeOnTimerString;

        // Button to start pomodoro timer
        private Button startB;

        // TextView that shows the time left on the timer in seconds(shows
        // minutes:seconds format after running formatTime())
        public TextView timeLeftText;

        // startTime defines the amount of time in the pomodoro timer.
        // 1500 seconds = 25 minutes.
        private long pomodoroStartTime = 1500 * 1000;
        private long breakStartTime = 300 * 1000;
        private final long interval = 1 * 1000;

        public static final String RECEIVE_TIME = "se.chalmers.dat255.group22.escape.fragments.PomodoroFragment.RECEIVE_TIME";
        public static final String RECEIVE_STATUS = "se.chalmers.dat255.group22.escape.fragments.PomodoroFragment.POMODORO_SERVICE";

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.d("Pomodoro", "onCreate------------");
                stopService(getActivity().findViewById(R.id.pomodoro_button));
                Log.d("Pomdoro", "onCreate--------------END");

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {

                Log.d("Pomodoro", "onCreateView --------------------");
                View v = inflater.inflate(R.layout.pomodoro_fragment, container, false);

                // Setting up broadcast manager to handle communication between service
                // and activity
                LocalBroadcastManager bManager = LocalBroadcastManager
                                .getInstance(getActivity());

                // This is where the pomodoro fragment receives data from the service
                // If the Pomodoro service was running with a timer, it gets that info
                // here
                // Getting time from service
                IntentFilter receiveTimeFilter = new IntentFilter();
                receiveTimeFilter.addAction(RECEIVE_TIME);
                bManager.registerReceiver(bReceiver, receiveTimeFilter);
                // Asking if service is running
                IntentFilter receiveRunningStatusFilter = new IntentFilter();
                receiveRunningStatusFilter.addAction(RECEIVE_STATUS);
                bManager.registerReceiver(bReceiver, receiveRunningStatusFilter);

                // If the pomodoro service was running a Pomodoro Timer, continue that
                // timer now

                if (pomodoroServiceRunning == "POMODORO_TIMER") {
                        stopService(getActivity().findViewById(R.id.pomodoro_button));
                        Log.d("Pomodoro",
                                        "onRESUME - Service was running POMODORO-TIMER before this!");
                        Log.d("Pomodoro",
                                        "And there was this much time on the service timer:");
                        Log.d("Pomodoro", serviceTimeString);

                        // converting string message from service to a long
                        serviceTimeStringToLong = Long.parseLong(serviceTimeString);

                        // Since timer is managed in milliseconds, multiply value by 1000
                        serviceTimeStringToLong = serviceTimeStringToLong * 1000;

                        // Layout for the pomodoro start button
                        startB = (Button) v.findViewById(R.id.pomodoro_button);

                        // Listener for pomodoro start button
                        startB.setOnClickListener(this);

                        // Layout for the time display in the pomodoro timer
                        timeLeftText = (TextView) v.findViewById(R.id.pomodoro_timer);

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        // Initializing service count down timer
                        serviceCountDownTimer = new PomodoroTimer(serviceTimeStringToLong,
                                        interval);
                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // If the pomodoro service timer had more than 1 second on it,
                        // continue counting
                        if (serviceTimeStringToLong > 1000) {
                                Log.d("Pomodoro",
                                                "-------------------------Service timer >>>>> 0!!!!!!!!!--------------");
                                // Setting start time of the pomodoro timer
                                timeLeftText.setText(formatTime(serviceTimeStringToLong));

                                pomodoroTimerHasStarted = false;
                               //TESTTEST
                                // serviceCountDownTimer.start();
                                servicePomodoroTimerHasStarted = true;
                                serviceTimerHasStarted = true;
                                // pomodoroTimerHasStarted = true;
                                startB.setText("STOP");

                        }

                        // If the pomodoro service counter had 1 second or less on it,
                        // display "Break time!" message
                        else {
                                Log.d("Pomodoro",
                                                "----------------Service timer was at 0!!!!--------------");
                                serviceCountDownTimer.cancel();
                                pomodoroCountDownTimer.cancel();
                                breakCountDownTimer.cancel();
                                servicePomodoroTimerHasStarted = false;
                                pomodoroTimerHasStarted = false;
                                onBreak = true;
                                breakTimerHasStarted = false;
                                startB.setText("RESTART");
                                timeLeftText.setText("Break time!");

                        }

                }
                // If a pomodoro service break timer was running, continue the break
                // timer

                else if (pomodoroServiceRunning == "BREAK_TIMER") {

                        Log.d("Pomodoro",
                                        "onRESUME - Service was running BREAK-TIMER before this!");
                        Log.d("Pomodoro",
                                        "And there was this much time on the service timer:");
                        Log.d("Pomodoro", serviceTimeString);
                        stopService(getActivity().findViewById(R.id.pomodoro_button));

                        // converting string message from service to a long
                        serviceTimeStringToLong = Long.parseLong(serviceTimeString);

                        // Since timer is managed in milliseconds, multiply value by 1000
                        serviceTimeStringToLong = serviceTimeStringToLong * 1000;

                        // Layout for the pomodoro start button
                        startB = (Button) v.findViewById(R.id.pomodoro_button);

                        // Listener for pomodoro start button
                        startB.setOnClickListener(this);

                        // Layout for the time display in the pomodoro timer
                        timeLeftText = (TextView) v.findViewById(R.id.pomodoro_timer);

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        // Initializing service count down timer
                        serviceCountDownTimer = new PomodoroTimer(serviceTimeStringToLong,
                                        interval);
                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // If the pomodoro service break timer had more than 1 second on it,
                        // continue counting here
                        if (serviceTimeStringToLong > 1000) {
                                Log.d("Pomodoro",
                                                "-------------------------Service timer >>>>> 0!!!!!!!!!--------------");
                                // Setting start time of the pomodoro timer
                                timeLeftText.setText(formatTime(serviceTimeStringToLong));

                                pomodoroTimerHasStarted = false;
                                serviceCountDownTimer.start();
                                serviceBreakTimerHasStarted = true;
                                serviceTimerHasStarted = true;
                                startB.setText("STOP");

                        }
                        // If the pomodoro service break timer had 1 second or less on it,
                        // display "Break time!" message
                        else {
                                Log.d("Pomodoro",
                                                "----------------Service timer was at 0!!!!--------------");
                                serviceCountDownTimer.cancel();
                                serviceBreakTimerHasStarted = false;
                                pomodoroCountDownTimer.cancel();
                                breakCountDownTimer.cancel();
                                onBreak = false;
                                pomodoroTimerHasStarted = false;
                                startB.setText("RESTART");
                                timeLeftText.setText("Break time!");

                        }

                }
                // If the pomodoro service was doing a countdown from an interrupted
                // service count down
                // Continue the count down here
                else if (pomodoroServiceRunning == "SERVICE_TIMER") {

                        Log.d("Pomodoro",
                                        "onRESUME - Service was running SERVICE-TIMER before this!");
                        Log.d("Pomodoro",
                                        "And there was this much time on the service timer:");
                        Log.d("Pomodoro", serviceTimeString);
                        stopService(getActivity().findViewById(R.id.pomodoro_button));

                        // converting string message from service to a long
                        serviceTimeStringToLong = Long.parseLong(serviceTimeString);

                        // Since timer is managed in milliseconds, multiply value by 1000
                        serviceTimeStringToLong = serviceTimeStringToLong * 1000;

                        // Layout for the pomodoro start button
                        startB = (Button) v.findViewById(R.id.pomodoro_button);

                        // Listener for pomodoro start button
                        startB.setOnClickListener(this);

                        // Layout for the time display in the pomodoro timer
                        timeLeftText = (TextView) v.findViewById(R.id.pomodoro_timer);

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        serviceCountDownTimer = new PomodoroTimer(serviceTimeStringToLong,
                                        interval);
                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // Setting start time of the pomodoro timer
                        timeLeftText.setText(formatTime(serviceTimeStringToLong));

                        pomodoroTimerHasStarted = false;
                        breakTimerHasStarted = false;
                        //TESTEST
                        //serviceCountDownTimer.start();
                        serviceTimerHasStarted = true;
                        // pomodoroTimerHasStarted = true;
                        startB.setText("STOP");
                }
                // If there was no previous pomodoro service running, display Pomodoro
                // startup screen
                else {
                        Log.d("Pomodoro", "onRESUME - Service was not running before this!");

                        // Layout for the pomodoro start button
                        startB = (Button) v.findViewById(R.id.pomodoro_button);

                        // Listener for pomodoro start button
                        startB.setOnClickListener(this);

                        // Layout for the time display in the pomodoro timer
                        timeLeftText = (TextView) v.findViewById(R.id.pomodoro_timer);

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // Setting start time of the break timer
                        timeLeftText.setText(formatTime(breakStartTime));

                        // Setting start time of the pomodoro timer
                        timeLeftText.setText(formatTime(pomodoroStartTime));

                        // end of stuff that used to be outside else statement
                }

                return v;
        }

        // This is where the data from the service is received
        public BroadcastReceiver bReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                        // Time from service is received and stored in variable
                        // serviceTimeString
                        if (intent.getAction().equals(RECEIVE_TIME)) {
                                serviceTimeString = intent.getStringExtra("serviceToActivity");
                        }
                        // Running status from service is received and stored in variable
                        // pomodoroServiceRunning
                        if (intent.getAction().equals(RECEIVE_STATUS)) {
                                pomodoroServiceRunning = intent
                                                .getStringExtra("serviceRunningMsg");
                                Log.d("Pomodoro", pomodoroServiceRunning);
                        }
                }
        };

        public void startService(View view) {

                // This is where the data is sent from the activity to the service
                Log.d("Pomodoro",
                                "In the PomodoroFragment/startService method - before");
                Intent dataToPomodoroService = new Intent(getActivity(),
                                PomodoroService.class);

                // If pomodoro timer is active, send "ON_POMODORO" to service, otherwise
                // send "ON_BREAK" to service
                if (pomodoroTimerHasStarted == true) {
                        dataToPomodoroService.putExtra("activeTimer", "ON_POMODORO");
                } else if (breakTimerHasStarted == true) {
                        dataToPomodoroService.putExtra("activeTimer", "ON_BREAK");
                } else if (serviceTimerHasStarted == true) {
                        dataToPomodoroService.putExtra("activeTimer", "ON_SERVICE");
                }
                // Send the amount of seconds on the timer to the service
                dataToPomodoroService.putExtra("secondsOnTimer", timeOnTimerString);

                // Start the service, and send the parameters with it
                getActivity().startService(dataToPomodoroService);
                Log.d("Pomodoro", "In the PomodoroFragment/startService method - after");

        }

        public void stopService(View view) {
                Log.d("Pomodoro", "In the PomodoroFragment/stopService method");
                getActivity().stopService(
                                new Intent(getActivity(), PomodoroService.class));
        }

        /*
         * When clicking on start button, either start the timer (if it hasn't
         * started) and change button text to STOP, otherwise stop running timer and
         * change button text to RESTART.
         */
        @Override
        public void onClick(View v) {
                // If button is clicked while a service timer is active and on a
                // pomodoro,
                // stop the service count down
                if (serviceTimerHasStarted == true && onBreak == false) {
                    Log.d("Pomodoro","-----------------Button1-----------------");    
                	serviceCountDownTimer.cancel();
                        serviceTimerHasStarted = false;
                        startB.setText("RESTART");
                }
                // If button is clicked when there is no active pomodoro timer
                // and while not on a break, start a pomodoro timer
                else if (!pomodoroTimerHasStarted && onBreak == false) {
                        pomodoroCountDownTimer.start();
                        pomodoroTimerHasStarted = true;
                        startB.setText("STOP");
                        Log.d("Pomodoro", "Pomodoro - pressing start button");
                }
                // If button is clicked when a break timer hasn't started
                // while on a break, start a break timer
                else if (!breakTimerHasStarted && onBreak == true) {
                        if (serviceTimerHasStarted == true) {
                                serviceCountDownTimer.cancel();
                        }
                        breakCountDownTimer.start();
                        breakTimerHasStarted = true;
                        pomodoroTimerHasStarted = false;
                        startB.setText("STOP");
                }
                // If button is clicked when a pomodoro timer has started
                // during a pomodoro session, stop the pomodoro timer
                else if (pomodoroTimerHasStarted && onBreak == false) {
                        pomodoroCountDownTimer.cancel();
                        pomodoroTimerHasStarted = false;
                        onBreak = false;
                        startB.setText("RESTART");
                        Log.d("PomodoroService", "Pomodoro - pressing stop buttonBBQ");
                }
                // If button is clicked while a break timer has started during
                // a break, stop the break timer
                else if (breakTimerHasStarted && onBreak == true) {
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

        // When clicking away from the Pomodoro fragment,
        // stop current timers and start service
        @Override
        public void onPause() {
                super.onPause();
                Log.d("Pomodoro", "Fragment onPause");
                Log.d("Pomodoro", "Time on Timer:");

                if (pomodoroTimerHasStarted == true) {
                        pomodoroCountDownTimer.cancel();
                        Log.d("Pomodoro", timeOnTimerString);
                        startService(getActivity().findViewById(R.id.pomodoro_button));
                } else if (breakTimerHasStarted == true) {
                        breakCountDownTimer.cancel();
                        Log.d("Pomodoro", timeOnTimerString);
                        startService(getActivity().findViewById(R.id.pomodoro_button));
                } else if (serviceTimerHasStarted == true) {
                        serviceCountDownTimer.cancel();
                        Log.d("Pomdoro", timeOnTimerString);
                        startService(getActivity().findViewById(R.id.pomodoro_button));
                }
                Log.d("Pomodoro", "onPause = done");
        }

        // On resume behavior is mostly identical to onCreateView
        // Apart from initially drawing the interface
        // See the corresponding code in onCreateView for
        // more cohesive comments

        @Override
        public void onResume() {
                super.onResume();
                Log.d("Pomodoro", "onResume----------------");

                // Setting up broadcast manager to handle communication between service
                // and activity
                LocalBroadcastManager bManager = LocalBroadcastManager
                                .getInstance(getActivity());

                // This is where the pomodoro fragment receives data from the service
                // Getting time from service
                IntentFilter receiveTimeFilter = new IntentFilter();
                receiveTimeFilter.addAction(RECEIVE_TIME);
                bManager.registerReceiver(bReceiver, receiveTimeFilter);
                // Asking if service is running
                IntentFilter receiveRunningStatusFilter = new IntentFilter();
                receiveRunningStatusFilter.addAction(RECEIVE_STATUS);
                bManager.registerReceiver(bReceiver, receiveRunningStatusFilter);

                if (pomodoroServiceRunning == "POMODORO_TIMER") {
                        stopService(getActivity().findViewById(R.id.pomodoro_button));
                        Log.d("Pomodoro",
                                        "onRESUME - Service was running POMODORO-TIMER before this!");
                        Log.d("Pomodoro",
                                        "And there was this much time on the service timer:");
                        Log.d("Pomodoro", serviceTimeString);

                        // converting string message from service to a long
                        serviceTimeStringToLong = Long.parseLong(serviceTimeString);

                        // Since timer is managed in milliseconds, multiply value by 1000
                        serviceTimeStringToLong = serviceTimeStringToLong * 1000;

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        serviceCountDownTimer = new PomodoroTimer(serviceTimeStringToLong,
                                        interval);
                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        if (serviceTimeStringToLong > 1000) {
                                Log.d("Pomodoro",
                                                "-------------------------Service timer >>>>> 0!!!!!!!!!--------------");
                                // Setting start time of the pomodoro timer
                                timeLeftText.setText(formatTime(serviceTimeStringToLong));

                                pomodoroTimerHasStarted = false;
                                serviceCountDownTimer.start();
                                serviceTimerHasStarted = true;
                                startB.setText("STOP");

                        }

                        else {
                                Log.d("Pomodoro",
                                                "----------------Service timer was at 0!!!!--------------");
                                serviceCountDownTimer.cancel();
                                pomodoroCountDownTimer.cancel();
                                breakCountDownTimer.cancel();
                                pomodoroTimerHasStarted = false; // test!
                                onBreak = true;
                                breakTimerHasStarted = false;
                                startB.setText("RESTART");
                                timeLeftText.setText("Break time!");

                        }

                }

                else if (pomodoroServiceRunning == "BREAK_TIMER") {

                        Log.d("Pomodoro",
                                        "onRESUME - Service was running BREAK-TIMER before this!");
                        Log.d("Pomodoro",
                                        "And there was this much time on the service timer:");
                        Log.d("Pomodoro", serviceTimeString);
                        stopService(getActivity().findViewById(R.id.pomodoro_button));

                        // converting string message from service to a long
                        serviceTimeStringToLong = Long.parseLong(serviceTimeString);

                        // Since timer is managed in milliseconds, multiply value by 1000
                        serviceTimeStringToLong = serviceTimeStringToLong * 1000;

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        serviceCountDownTimer = new PomodoroTimer(serviceTimeStringToLong,
                                        interval);
                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // Setting start time of the pomodoro timer
                        if (serviceTimeStringToLong > 1000) {
                                Log.d("Pomodoro",
                                                "-------------------------Service timer >>>>> 0!!!!!!!!!--------------");
                                // Setting start time of the pomodoro timer
                                timeLeftText.setText(formatTime(serviceTimeStringToLong));

                                pomodoroTimerHasStarted = false;
                                serviceCountDownTimer.start();
                                serviceTimerHasStarted = true;
                                startB.setText("STOP");

                        }

                        else {
                                Log.d("Pomodoro",
                                                "----------------Service timer was at 0!!!!--------------");
                                serviceCountDownTimer.cancel();
                                pomodoroCountDownTimer.cancel();
                                breakCountDownTimer.cancel();
                                onBreak = false;
                                pomodoroTimerHasStarted = false;
                                startB.setText("RESTART");
                                timeLeftText.setText("Break over!");

                        }

                }

                else if (pomodoroServiceRunning == "SERVICE_TIMER") {

                        Log.d("Pomodoro",
                                        "onRESUME - Service was running SERVICE-TIMER before this!");
                        Log.d("Pomodoro",
                                        "And there was this much time on the service timer:");
                        Log.d("Pomodoro", serviceTimeString);
                        stopService(getActivity().findViewById(R.id.pomodoro_button));

                        // converting string message from service to a long
                        serviceTimeStringToLong = Long.parseLong(serviceTimeString);

                        // Since timer is managed in milliseconds, multiply value by 1000
                        serviceTimeStringToLong = serviceTimeStringToLong * 1000;

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        serviceCountDownTimer = new PomodoroTimer(serviceTimeStringToLong,
                                        interval);
                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // Setting start time of the pomodoro timer
                        timeLeftText.setText(formatTime(serviceTimeStringToLong));

                        pomodoroTimerHasStarted = false;
                        breakTimerHasStarted = false;
                        serviceCountDownTimer.start();
                        serviceTimerHasStarted = true;
                        // pomodoroTimerHasStarted = true;
                        startB.setText("STOP");

                }

                else {
                        Log.d("Pomodoro", "onRESUME - Service was not running before this!");

                        // Initializing pomodoro count down timer
                        pomodoroCountDownTimer = new PomodoroTimer(pomodoroStartTime,
                                        interval);

                        // Initializing break count down timer
                        breakCountDownTimer = new PomodoroTimer(breakStartTime, interval);

                        // Setting start time of the break timer
                        timeLeftText.setText(formatTime(breakStartTime));

                        // Setting start time of the pomodoro timer
                        timeLeftText.setText(formatTime(pomodoroStartTime));
                }

        }

        // Count down timer is handled below
        /**
         * 
         * @author Adam
         * @Info A timer that counts down the different timers, with functionality
         *       both on each individual second tick and when the timer reaches
         *       zero.
         * @onTick - Updates the time view and saves the time left in a variable
         * @onFinish - Sends a notification when the timer is finished
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
                                // BUGFIX?
                                // breakTimerHasStarted = false;
                                // ENDOF BUGFIX?
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
                        timeOnTimer = millisUntilFinished;
                        timeOnTimer = timeOnTimer / 1000;
                        timeOnTimerString = String.valueOf(timeOnTimer);
                }
        }

}
