package se.chalmers.dat255.group22.escape;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.fragments.dialogfragments.ErrorGPlayFragment;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.SimpleGeofence;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import se.chalmers.dat255.group22.escape.utils.Constants;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

/**
 * A notification handler for creating notifications that should appear at a
 * specific time. This class is a singleton.
 * 
 * @author Simon Persson
 * 
 */

public class NotificationHandler {

	/**
	 * Constant to use when setting/getting a ListObject title from a bundle.
	 */
	public static String NOTIFICATION_TITLE = "TITLE";
	/**
	 * Constant to use when setting/getting a ListObject comment from a bundle.
	 */
	public static String NOTIFICATION_DESC = "DESC";
	/**
	 * Constant to use when setting/getting a ListObject start time from a
	 * bundle.
	 */
	public static String NOTIFICATION_EVENT_TIME = "EVENT_TIME";
	/**
	 * Constant to use when setting/getting a boolean describing whether a
	 * ListObject is an event or not from a bundle.
	 */
	public static String NOTOFICATION_IS_EVENT = "IS_EVENT";
	/**
	 * Constant to use when setting/getting a ListObject id from a bundle
	 */
	public static String NOTIFICATION_ID = "ID";

	private static DBHandler dbH;
	private static FragmentActivity contextActivity;
	private static NotificationHandler instance;

	/**
	 * Private constructor, to make class singleton.
	 */
	private NotificationHandler() {

	}

	/**
	 * Get the instance of a NotificationHandler. <b>NOTE: You must run init()
	 * during startup and provide a FragmentActivity object. This is to be able
	 * to bring up dialogs with fragmentmanager etc.</b>
	 * 
	 * @return A NotificationHandler object
	 */
	public static NotificationHandler getInstance() {
		if (instance == null) {
			instance = new NotificationHandler();
		}
		return instance;
	}

	/**
	 * Initializes the notification handler with a fragment activity. Has to be
	 * called one time before using some of the methods for adding reminders.
	 * 
	 * @param fragmentActivity
	 *            The fragment activity to use when bringing up error dialogs.
	 */
	public void init(FragmentActivity fragmentActivity) {
		contextActivity = fragmentActivity;
		dbH = new DBHandler(contextActivity);
		Log.d(Constants.APPTAG, "NotificationHandler initialized");
	}

	public boolean isInitialized() {
		return contextActivity != null;
	}

	/**
	 * Method for adding a time reminder notification for a task or event.
	 * 
	 * @param listObject
	 *            The ListObject describing the task or event.
	 * @throws NullPointerException
	 *             If the ListObject's TimeAlarm object is null.
	 * @throws UnsupportedOperationException
	 *             If the notification handler hasn't been initialized.
	 */
	public void addTimeReminder(ListObject listObject)
			throws NullPointerException, UnsupportedOperationException {

		if (!isInitialized()) {
			throw new UnsupportedOperationException(
					"NotificationHandler hasn't been initialized yet. Run init(FragmentActivity) when starting application.");
		}

		TimeAlarm timeAlarm = dbH.getTimeAlarm(listObject);
		if (timeAlarm == null) {
			throw new NullPointerException();
		}

		Date date = timeAlarm.getDate();

		Bundle args = generateBundle(listObject);

		AlarmManager alarmManager = (AlarmManager) contextActivity
				.getSystemService(Context.ALARM_SERVICE);

		// Creates an intent holding the AlarmReceiver, and attaches the
		// generated bundle
		Intent alarmIntent = new Intent();
		alarmIntent.setAction(AlarmReceiver.NEW_TIME_NOTIFICATION);
		alarmIntent.putExtras(args);

		/*
		 * Creates a PendingIntent that'll be used by the alarm manager
		 * (external application) to send a broadcast at a given time from the
		 * context (in this case, the MainActivity) to all receivers matching
		 * the alarmIntent. When AlarmReceiver (the matching receiver) receives
		 * the broadcast, it generates and sends a custom notification to the
		 * system.
		 */
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				contextActivity, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Gives the alarm manager a time and an operation to be performed (the
		// pending intent) at that time. RTC_WAKEUP tells the alarm manager to
		// wake upp the device when alarm goes off.
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

	}

	/**
	 * Method for adding a place reminder notification for a task or event.
	 * 
	 * @param listObject
	 *            The ListObject describing the task or event.
	 * @throws NullPointerException
	 *             If the ListObject's Place object is null.
	 * @throws UnsupportedOperationException
	 *             If the notification handler hasn't been initialized.
	 */
	public void addPlaceReminder(ListObject listObject)
			throws NullPointerException, UnsupportedOperationException {
		if (!isInitialized()) {
			throw new UnsupportedOperationException(
					"NotificationHandler hasn't been initialized yet. Run init(FragmentActivity) when starting application.");
		}

		Place place = dbH.getPlace(listObject);

		if (place == null) {
			throw new NullPointerException();
		}
		Geocoder geocoder = new Geocoder(contextActivity);

		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocationName(place.getName(), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create a geofence from first item in the addresses list
		Address address = addresses.get(0);
		SimpleGeofence simpleGeofence = new SimpleGeofence(place.getId() + "",
				address.getLatitude(), address.getLongitude(), 200,
				Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER);

		Bundle args = generateBundle(listObject);

		// Creates an intent holding the AlarmReceiver, and attaches the
		// generated bundle
		Intent alarmIntent = new Intent();
		alarmIntent.setAction(AlarmReceiver.NEW_LOCATION_NOTIFICATION);
		alarmIntent.putExtras(args);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				contextActivity, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		List<Geofence> geofenceList = new ArrayList<Geofence>();
		geofenceList.add(simpleGeofence.toGeofence());

		if (servicesConnected()) {
			GeofenceRequester requester = new GeofenceRequester(contextActivity);
			requester.setPendingIntent(pendingIntent);
			requester.addGeofences(geofenceList);
		} else {
			String msg = "No Google Play Services installed";
			Log.d(Constants.APPTAG, msg);
			Toast.makeText(contextActivity, msg, Toast.LENGTH_LONG).show();
		}

	}

	/*
	 * Method for generating a bundle object that contains all the important
	 * data from the ListObject
	 */
	private Bundle generateBundle(ListObject listObject) {
		Bundle bundle = new Bundle();

		// Gets all necessary data from the ListObject
		int id = listObject.getId();
		String title = listObject.getName();
		String description = listObject.getComment();
		Time time = dbH.getTime(listObject);
		boolean isEvent = (time != null);

		// Builds a date/time string to show in notification
		StringBuilder timeString = new StringBuilder();
		if (isEvent) {
			Date startDate = time.getStartDate();

			SimpleDateFormat yearFormatter = new SimpleDateFormat("yy");
			SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
			SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");
			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

			timeString = new StringBuilder();
			timeString.append(yearFormatter.format(startDate));
			timeString.append("/");
			timeString.append(monthFormatter.format(startDate));
			timeString.append("/");
			timeString.append(dayFormatter.format(startDate));
			timeString.append(" ");
			timeString.append(timeFormatter.format(startDate));

		}

		bundle.putInt(NOTIFICATION_ID, id);
		bundle.putString(NOTIFICATION_TITLE, title);
		bundle.putString(NOTIFICATION_DESC, description);
		bundle.putBoolean(NOTOFICATION_IS_EVENT, isEvent);
		bundle.putString(NOTIFICATION_EVENT_TIME, timeString.toString());

		return bundle;

	}

	/**
	 * Method for checking if Google Play services are connected on the device
	 * 
	 * @return true if connected, false otherwise
	 */
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(contextActivity);

		if (resultCode == ConnectionResult.SUCCESS) {
			Log.d(Constants.APPTAG, "Google Play services is available.");

			Intent broadcastIntent = new Intent();

			broadcastIntent.putExtra("RESULT_CODE", resultCode);

			LocalBroadcastManager.getInstance(contextActivity).sendBroadcast(
					broadcastIntent);
			return true;
		} else {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, contextActivity,
					Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);

			if (errorDialog != null) {
				ErrorGPlayFragment errorDialogFragment = new ErrorGPlayFragment();
				errorDialogFragment.setDialog(errorDialog);
				errorDialogFragment.show(
						contextActivity.getSupportFragmentManager(),
						"Geofence Detecion");
			}

		}

		return false;
	}
}
