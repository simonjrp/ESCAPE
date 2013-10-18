package se.chalmers.dat255.group22.escape;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.GPSAlarm;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.SimpleGeofence;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import se.chalmers.dat255.group22.escape.utils.Constants;

/**
 * A notification handler for creating notifications that should appear at a
 * specific time. This class is a singleton.
 * 
 * @author Simon Persson
 * 
 */

public class NotificationHandler {

	private static DBHandler dbH;
	private static Context context;
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
	 * @param context
	 *            The fragment activity to use when bringing up error dialogs.
	 */
	public void init(Context context) {
		this.context = context.getApplicationContext();
		dbH = new DBHandler(context);
		Log.d(Constants.APPTAG, context.getString(R.string.nh_init));
	}

	private boolean isInitialized() {
		return context != null;
	}

	/**
	 * Method for adding a time reminder notification for a task or event.
	 * 
	 * @param idListObject
	 *            The database id of the ListObject describing a task or event.
	 * @throws NullPointerException
	 *             If the ListObject's TimeAlarm object is null.
	 * @throws UnsupportedOperationException
	 *             If the notification handler hasn't been initialized.
	 */
	public void addTimeReminder(long idListObject)
			throws NullPointerException, UnsupportedOperationException {

		if (!isInitialized()) {
			throw new UnsupportedOperationException(
					context.getString(R.string.nh_not_init));
		}

        ListObject listObject = dbH.getListObject(idListObject);
        if(listObject == null) {
            throw new NullPointerException("Couldn't find a ListObject with provided id.");
        }

		TimeAlarm timeAlarm = dbH.getTimeAlarm(listObject);
		if (timeAlarm == null) {
			throw new NullPointerException();
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
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				args.getInt(Constants.NOTIFICATION_ID), alarmIntent,
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
	 * @param idListObject
	 *            The database id of the ListObject describing a task or event.
	 * @throws NullPointerException
	 *             If the ListObject's GPSAlarm object is null.
	 * @throws UnsupportedOperationException
	 *             If the notification handler hasn't been initialized.
	 */
	public void addPlaceReminder(long idListObject)
			throws NullPointerException, UnsupportedOperationException {
		if (!isInitialized()) {
			throw new UnsupportedOperationException(
					"NotificationHandler hasn't been initialized yet. Run init(FragmentActivity) when starting application.");
		}

        ListObject listObject = dbH.getListObject(idListObject);
        if(listObject == null) {
            throw new NullPointerException("Couldn't find a ListObject with provided id.");
        }

		GPSAlarm gpsAlarm = dbH.getGPSAlarm(listObject);

		if (gpsAlarm == null) {
			throw new NullPointerException();
		}

		SimpleGeofence simpleGeofence = new SimpleGeofence(listObject.getId()
				+ "", gpsAlarm.getLatitude(), gpsAlarm.getLongitude(),
				Constants.GEOFENCE_DEFAULT_RADIUS, Geofence.NEVER_EXPIRE,
				Geofence.GEOFENCE_TRANSITION_ENTER);

		Bundle args = generateBundle(listObject);

		// Creates an intent holding the AlarmReceiver, and attaches the
		// generated bundle
		Intent alarmIntent = new Intent();
		alarmIntent.setAction(AlarmReceiver.NEW_LOCATION_NOTIFICATION);
		alarmIntent.putExtras(args);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		List<Geofence> geofenceList = new ArrayList<Geofence>();
		geofenceList.add(simpleGeofence.toGeofence());

		if (servicesConnected()) {
			GeofenceRequester requester = new GeofenceRequester(
					context.getApplicationContext());
			requester.setPendingIntent(pendingIntent);
			requester.addGeofences(geofenceList);
		} else {
			Toast.makeText(context,
					context.getString(R.string.nh_location_reminder_not_added),
					Toast.LENGTH_LONG).show();

		}

	}

	/**
	 * Method for generating a bundle object that contains all the important
	 * data from the ListObject
	 * 
	 * @param listObject
	 *            The ListObject from which to get all data to put in bundle
	 *            from.
	 * @return A Bundle containing data extracted from the list object.
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

		bundle.putInt(Constants.NOTIFICATION_ID, id);
		bundle.putString(Constants.NOTIFICATION_TITLE, title);
		bundle.putString(Constants.NOTIFICATION_DESC, description);
		bundle.putBoolean(Constants.NOTOFICATION_IS_EVENT, isEvent);
		bundle.putString(Constants.NOTIFICATION_EVENT_TIME,
				timeString.toString());

		return bundle;

	}

	/**
	 * Method for removing an existing time reminder from the system.
	 * 
	 * @param listObject
	 *            The ListObject associated with the time reminder.
	 */
	public void removeTimeReminder(ListObject listObject) {
		// This method created a pending intent exactly as it's done in
		// addTimeReminder(). Then it sends this pending intent object to the
		// AlarmManager, which in turn removes the pending intent form the queue
		// and therefore removes the reminder notification from the system.

		Bundle args = generateBundle(listObject);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent alarmIntent = new Intent();
		alarmIntent.setAction(AlarmReceiver.NEW_TIME_NOTIFICATION);
		alarmIntent.putExtras(args);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				args.getInt(Constants.NOTIFICATION_ID), alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		alarmManager.cancel(pendingIntent);

		Log.d(Constants.APPTAG,
				"Time reminder for: "
						+ alarmIntent.getExtras().getString(
								Constants.NOTIFICATION_TITLE) + " removed.");
	}

	/**
	 * Method for removing an existing location reminder (geofence) from the
	 * system.
	 * 
	 * @param listObject
	 *            The ListObject associated with the location reminder.
	 */
	public void removeLocationReminder(ListObject listObject) {
		long id = listObject.getId();
		if (servicesConnected()) {
			List<String> idList = new ArrayList<String>();
			idList.add(id + "");

			GeofenceRemover remover = new GeofenceRemover(context);
			remover.removeGeoFencesById(idList);
		} else {
			Toast.makeText(
					context,
					context.getString(R.string.nh_location_reminder_not_removed),
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method for checking if Google Play services are connected on the device
	 * 
	 * @return true if connected, false otherwise
	 */
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);

		if (resultCode == ConnectionResult.SUCCESS) {
			Log.d(Constants.APPTAG,
					context.getString(R.string.gplay_services_available));

			Intent broadcastIntent = new Intent();

			broadcastIntent.putExtra("RESULT_CODE", resultCode);

			LocalBroadcastManager.getInstance(context).sendBroadcast(
					broadcastIntent);
			return true;
		} else {
			// Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
			// resultCode, contextActivity,
			// Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
			//
			// if (errorDialog != null) {
			// ErrorGPlayFragment errorDialogFragment = new
			// ErrorGPlayFragment();
			// errorDialogFragment.setDialog(errorDialog);
			// errorDialogFragment.show(
			// contextActivity.getSupportFragmentManager(),
			// "Geofence Detecion");
			// }

			Log.d(Constants.APPTAG,
					context.getString(R.string.gplay_services_unavailable));
			Toast.makeText(context,
					context.getString(R.string.gplay_services_unavailable),
					Toast.LENGTH_SHORT).show();

		}

		return false;
	}

}
