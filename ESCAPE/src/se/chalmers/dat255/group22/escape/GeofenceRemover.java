package se.chalmers.dat255.group22.escape;

import java.util.List;

import se.chalmers.dat255.group22.escape.utils.Constants;
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
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

/**
 * Class to be used for removing old/unwanted geofences.
 * 
 * @author Simon Persson
 * 
 */
public class GeofenceRemover
		implements
			ConnectionCallbacks,
			OnConnectionFailedListener,
			OnRemoveGeofencesResultListener {

	// The calling activity
	private Context context;

	// List of all ids for current geofences
	private List<String> currentGeofenceIds;

	// The location client used to connect to the Location Services
	private LocationClient locationClient;

	// Placeholder for a pending intent to use when removing old geofences by
	// intent.
	private PendingIntent pendingIntent;

	// Placeholder for the remove type (by intent or by id)
	private Constants.REMOVE_TYPE removeType;

	// Flag used for indicating if an add or remove request of a geofence is
	// active or not
	private boolean inProgress;

	/**
	 * Constructor for creating a new GeofenceRemover.
	 * 
	 * @param context
	 *            The activity context. Used to create a location client, make
	 *            broadcasts etc.
	 */
	public GeofenceRemover(Context context) {
		this.context = context;

		currentGeofenceIds = null;
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
	 * Method for removing one or more geofences by their id.
	 * 
	 * @param geofenceIds
	 *            A list of ids of the geofences to be removed.
	 * @throws IllegalArgumentException
	 *             If the id list is null or empty.
	 * @throws UnsupportedOperationException
	 *             If a request already is in progress.
	 */
	public void removeGeoFencesById(List<String> geofenceIds)
			throws IllegalArgumentException, UnsupportedOperationException {

		// If list if empty or null, throw an exception
		if ((geofenceIds == null) || (geofenceIds.size() == 0)) {
			throw new IllegalArgumentException();
		} else {

			// Request connection if no other request is in progress, and set in
			// progress flag to true
			if (!inProgress) {
				removeType = Constants.REMOVE_TYPE.LIST;
				currentGeofenceIds = geofenceIds;
				requestConnection();
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

	/**
	 * Method for removing one or more geofences with a request intent.
	 * 
	 * @param requestIntent
	 *            The pending intent that the geofence(s) was added with.
	 * @throws UnsupportedOperationException
	 *             If a request is already in progress.
	 */
	public void removeGeoFencesByIntent(PendingIntent requestIntent)
			throws UnsupportedOperationException {
		// Request connection if no other request is in progress, and set in
		// progress flag to true
		if (!inProgress) {
			removeType = Constants.REMOVE_TYPE.INTENT;
			pendingIntent = requestIntent;
			requestConnection();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	// Tries to connect to Location Services
	private void requestConnection() {
		getLocationClient().connect();
	}

	// Creates and returns a new Location Client (later used to remove
	// geofences)
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
		// removing the geofences.
		continueRemoveGeofences();

	}

	/**
	 * Sends a request to the location client to remove all geofences.
	 */
	private void continueRemoveGeofences() {
		// Remove geofences with the appropriate method for the remove type

		switch (removeType) {
			case INTENT :
				locationClient.removeGeofences(pendingIntent, this);
				break;
			case LIST :
				locationClient.removeGeofences(currentGeofenceIds, this);
				break;
		}
	}

	@Override
	public void onRemoveGeofencesByPendingIntentResult(int statusCode,
			PendingIntent pendingIntent) {
		// Intent to be broadcasted with either success or failure message
		// attached.
		Intent broadcastIntent = new Intent();

		if (statusCode == LocationStatusCodes.SUCCESS) {
			Log.d(Constants.APPTAG,
					context.getString(R.string.geofence_remove_success));

			broadcastIntent.setAction(Constants.ACTION_GEOFENCES_REMOVED);
		} else {
			Log.e(Constants.APPTAG,
					context.getString(R.string.geofence_remove_error));

			broadcastIntent.setAction(Constants.ACTION_GEOFENCES_REMOVE_ERROR);
		}

		LocalBroadcastManager.getInstance(context).sendBroadcast(
				broadcastIntent);

		requestDisconnection();

	}

	@Override
	public void onRemoveGeofencesByRequestIdsResult(int statusCode,
			String[] geofenceRequestIds) {
		// Intent to be broadcasted with either success or failure message
		// attached.
		Intent broadcastIntent = new Intent();

		if (statusCode == LocationStatusCodes.SUCCESS) {
			Log.d(Constants.APPTAG,
					context.getString(R.string.geofence_remove_success));

			broadcastIntent.setAction(Constants.ACTION_GEOFENCES_REMOVED);
		} else {
			Log.e(Constants.APPTAG,
					context.getString(R.string.geofence_remove_error));

			broadcastIntent.setAction(Constants.ACTION_GEOFENCES_REMOVE_ERROR);
		}

		LocalBroadcastManager.getInstance(context).sendBroadcast(
				broadcastIntent);

		requestDisconnection();

	}

	// Disconnects from the location services
	private void requestDisconnection() {

		inProgress = false;

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
		// // try to start a Google Play service activity that resolves it.
		// try {
		// // The requestcode is the one received by the activity when the
		// // started resolution returns some results.
		// result.startResolutionForResult(activity,
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
