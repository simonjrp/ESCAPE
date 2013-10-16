package se.chalmers.dat255.group22.escape;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.utils.Constants;

/**
 * Class to be used for adding geofences for location reminders.
 * 
 * @author Simon Persson
 * 
 */
public class GeofenceRequester
		implements
			OnAddGeofencesResultListener,
			ConnectionCallbacks,
			OnConnectionFailedListener {

	// The calling activity
	private final Context context;

	// The pending intent containing actions to be taken when a transition has
	// taken place
	private PendingIntent pendingIntent;

	// A list for storing all current geofences.
	private ArrayList<Geofence> currentGeoFences;

	// The location client used to connect to the Location Services
	private LocationClient locationClient;

	// Flag used for indicating if an add or remove request of a geofence is
	// active or not
	private boolean inProgress;

	/**
	 * Constructor for creating a new GeofenceRequester.
	 * 
	 * @param context
	 *            The activity context. Used to create a location client, make
	 *            broadcasts etc.
	 */
	public GeofenceRequester(Context context) {
		this.context = context;

		// Initializes the instance variables
		pendingIntent = null;
		locationClient = null;
		inProgress = false;
	}

	/**
	 * Method for setting the in progress flag.
	 * 
	 * @param inProgress
	 *            Should be set to true if a request is in progress, false
	 *            otherwise.
	 */
	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	/**
	 * Method for checking if a request is currently in progress.
	 * 
	 * @return true if a request is in progress, false otherwise
	 */
	public boolean getInProgress() {
		return inProgress;
	}

	/**
	 * Method for setting the request's pending intent
	 * 
	 * @param pendingIntent
	 *            A pending intent object containing which actions to be taken
	 *            when a transition takes place.
	 */
	public void setPendingIntent(PendingIntent pendingIntent) {
		this.pendingIntent = pendingIntent;
	}

	/**
	 * Method for getting the pending intent.
	 * 
	 * @return The pending intent, or null if none has been set.
	 */
	public PendingIntent getPendingIntent() {
		return pendingIntent;
	}

	/**
	 * Method for adding one or more geofences.
	 * 
	 * @param geofences
	 *            A list of one or more geofences to add
	 * @throws UnsupportedOperationException
	 *             If a request already is in progress.
	 */
	public void addGeofences(List<Geofence> geofences)
			throws UnsupportedOperationException {
		currentGeoFences = (ArrayList<Geofence>) geofences;

		// Request connection if no other request is in progress, and set in
		// progress flag to true
		if (!inProgress) {
			inProgress = true;

			requestConnection();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	// Tries to connect to Location Services
	private void requestConnection() {
		getLocationClient().connect();
	}

	// Creates and returns a new Location Client (later used to add geofences)
	private GooglePlayServicesClient getLocationClient() {
		if (locationClient == null) {
			locationClient = new LocationClient(context, this, this);
		}

		return locationClient;
	}

	// This method is called once the location client is connected
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d(Constants.APPTAG, context.getString(R.string.geofence_connected));

		// Now that the location client is connected, continue with actually
		// adding the geofences.
		continueAddGeoFences();

	}

	/**
	 * Sends a request to the location client to add all geofences.
	 */
	private void continueAddGeoFences() {
		locationClient.addGeofences(currentGeoFences, pendingIntent, this);
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// Intent to be broadcasted with either success or failure message
		// attached.
		Intent broadcast = new Intent();

		if (LocationStatusCodes.SUCCESS == statusCode) {

			Log.d(Constants.APPTAG,
					context.getString(R.string.geofence_add_success));

			broadcast.setAction(Constants.ACTION_GEOFENCES_ADDED);
		} else {

			Log.e(Constants.APPTAG,
					context.getString(R.string.geofence_add_error));

			broadcast.setAction(Constants.ACTION_GEOFENCES_ADD_ERROR);
		}

		LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast);

		requestDisconnection();

	}

	// Disconnects from location services
	private void requestDisconnection() {
		getLocationClient().disconnect();
	}

	@Override
	public void onDisconnected() {
		inProgress = false;

		Log.d(Constants.APPTAG,
				context.getString(R.string.geofence_disconnected));

		// Reset current location client
		locationClient = null;

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		inProgress = false;

		// if (result.hasResolution()) {
		// // If Google Play services have a solution to the failed connection,
		// // try to start a Google Play services activity that resolves it.
		// try {
		// // The requestcode is the one received by the activity when the
		// // started resolution returns some results.
		// result.startResolutionForResult(context,
		// Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
		// } catch (SendIntentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else {
		// Notify interested components that the connection failed. The
		// MainActivity should bring up an error dialog.
		Intent errorBroadcast = new Intent();
		errorBroadcast.setAction(Constants.ACTION_GEOFENCES_CONNECTION_FAILED);
		errorBroadcast.putExtra(Constants.EXTRAS_TAG_GEOFENCES_ERROR_CODE,
				result.getErrorCode());

		LocalBroadcastManager.getInstance(context)
				.sendBroadcast(errorBroadcast);
		// }

	}

}
