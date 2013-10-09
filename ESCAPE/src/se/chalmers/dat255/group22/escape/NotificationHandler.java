package se.chalmers.dat255.group22.escape;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.fragments.dialogfragments.ErrorGPlayFragment;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.SimpleGeofence;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * A notification handler for creating notifications that should appear at a
 * specific time.
 * 
 * @author Simon Persson
 * 
 */

public class NotificationHandler implements ConnectionCallbacks, OnConnectionFailedListener, OnAddGeofencesResultListener {

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
	
	private static final int GPLAY_FAILURE_REQUEST = 9000;

	public static String NOTIFICATION_ID = "ID";
	private DBHandler dBH;
	private Context context;
	
	// Location reminder specific variables
	private LocationClient locationClient;
	private PendingIntent locationPendingIntent;
	public enum REQUEST_TYPE {ADD, REMOVE};
	private REQUEST_TYPE requestType;
	private boolean reqInProgress;
	
	

	/**
	 * Creates a new notification handler.
	 * 
	 * @param context
	 *            Should be the context of the main activity of the app.
	 */
	public NotificationHandler(Context context) {
		this.context = context;
		dBH = new DBHandler(context);
	}

	/**
	 * Method for adding a time reminder notification for a task or event.
	 * 
	 * @param listObject
	 *            The ListObject describing the task or event.
	 * @throws IllegalArgumentException
	 *             If the ListObject's TimeAlarm object is null (i.e no alarm
	 *             time specified)
	 */
	public void addTimeReminder(ListObject listObject)
			throws IllegalArgumentException {

		TimeAlarm timeAlarm = dBH.getTimeAlarm(listObject);
		if (timeAlarm == null) {
			throw new IllegalArgumentException(
					"ListObject must have a TimeAlarm object");
		}
		Date date = timeAlarm.getDate();

		Bundle args = generateBundle(listObject);

		AlarmManager alarmManager = (AlarmManager) context
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
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Gives the alarm manager a time and an operation to be performed (the
		// pending intent) at that time. RTC_WAKEUP tells the alarm manager to
		// wake upp the device when alarm goes off.
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

	}

	// Method for generating a bundle object that contains all the important
	// data from the ListObject
	private Bundle generateBundle(ListObject listObject) {
		Bundle bundle = new Bundle();

		// Gets all necessary data from the ListObject
		int id = listObject.getId();
		String title = listObject.getName();
		String description = listObject.getComment();
		Time time = dBH.getTime(listObject);
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

	public void addPlaceReminder(ListObject listObject) {
		DBHandler dbHandler = new DBHandler(context);
		Place place = dbHandler.getPlace(listObject);
		Geocoder geocoder = new Geocoder(context);

		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocationName(place.getName(), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Address address = addresses.get(0);
		SimpleGeofence geofence = new SimpleGeofence(place.getId() + "",
				address.getLatitude(), address.getLongitude(), 200, 0,
				Geofence.GEOFENCE_TRANSITION_ENTER);

		Bundle args = generateBundle(listObject);
		// Creates an intent holding the AlarmReceiver, and attaches the
		// generated bundle
		Intent alarmIntent = new Intent();
		alarmIntent.setAction(AlarmReceiver.NEW_LOCATION_NOTIFICATION);
		alarmIntent.putExtras(args);

		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	}
	
	/**
	 * Method for checking if Google Play services are connected on the device
	 * @return true if connected, false otherwise
	 */
	protected boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		
		if(resultCode == ConnectionResult.SUCCESS) {
			Log.d("Geofnce Detection", "Google Play services is available.");
			return true;
		} else {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, GPLAY_FAILURE_REQUEST);
			if(errorDialog != null) {
				ErrorGPlayFragment errorDialogFragment = new ErrorGPlayFragment();
				errorDialogFragment.setDialog(errorDialog);
				errorDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "Geofence Detecion");
			}
		}
		return false;
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
}
