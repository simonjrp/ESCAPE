package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.utils.Constants;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
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

/**
 * Class to be used for adding geofences for location reminders. Since it
 * connects to the Location Services during the request process, it might
 * produce some errors.
 * 
 * @author Simon Persson
 * 
 */
public class GeofenceRequester implements OnAddGeofencesResultListener,
		ConnectionCallbacks, OnConnectionFailedListener {

	// The calling activity
	private final Activity activity;

	// The pending intent containing actions to be taken when a transition has
	// taken place
	private PendingIntent pendingIntent;

	// A list for storing all current geofences.
	private ArrayList<Geofence> currentGeoFences;

	// TODO add comment
	private LocationClient locationClient;

	// Flag used for indicating if an add or remove request of a geofence is
	// active or not
	private boolean inProgress;

	public GeofenceRequester(Activity activity) {
		this.activity = activity;

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

	public void addGeofences(List<Geofence> geofences)
			throws UnsupportedOperationException {
		currentGeoFences = (ArrayList<Geofence>) geofences;

		// Request connection if now other request is in progress, and set in
		// progress flag to true
		if (!inProgress) {
			inProgress = true;

			requestConnection();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public void requestConnection() {
		getLocationClient().connect();
	}

	private GooglePlayServicesClient getLocationClient() {
		if (locationClient == null) {
			locationClient = new LocationClient(activity, this, this);
		}

		return locationClient;
	}

	// This method is called once the location client is connected
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d("Geofences", "Connected to Location Services");

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

		String message;

		// TODO fix hardcoded messages
		if (LocationStatusCodes.SUCCESS == statusCode) {
			message = "Geofences was successfully added";

			Log.d("Geofences", message);

			broadcast.setAction("GEOFENCES_ADDED");
		} else {
			message = "Geofences coudln't be added, error!";

			Log.d("Geofences", message);

			broadcast.setAction("GEOFENCES_ADD_ERROR");
		}

		LocalBroadcastManager.getInstance(activity).sendBroadcast(broadcast);

		requestDisconnection();

	}

	// Disconnects from location services
	public void requestDisconnection() {
		getLocationClient().disconnect();
	}

	@Override
	public void onDisconnected() {
		inProgress = false;

		Log.d("Geofences", "Disconnected from Location Services");

		// Reset current location client
		locationClient = null;

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		inProgress = false;

		if (result.hasResolution()) {
			// If Google Play services have a solution to the failed connection,
			// try to start a Google Play services activity that resolves it.
			try {
				// The requestcode is the one received by the activity when the
				// started activity returns some results.
				result.startResolutionForResult(activity,
						Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Intent errorBroadcast = new Intent();
			errorBroadcast.setAction("LOCATION_SERVICES_DISCONNECT_FAIL");
			errorBroadcast.putExtra("ERROR_CODE", result.getErrorCode());

			LocalBroadcastManager.getInstance(activity).sendBroadcast(
					errorBroadcast);
		}

	}

}
