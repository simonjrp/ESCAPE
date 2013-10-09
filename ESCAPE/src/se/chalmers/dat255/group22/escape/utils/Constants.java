package se.chalmers.dat255.group22.escape.utils;

/**
 * A "bag" full of constants for readability as well as consistency.
 * 
 * @author tholene
 */
public final class Constants {
	/**
	 * The ID that should be used with an intent to indicate that you want to
	 * edit a task
	 */
	public static final int EDIT_TASK_ID = 0;
	/**
	 * The string that should be used with an intent to indicate that you want
	 * to edit a task
	 */
	public static final String EDIT_TASK_MSG = "Edit task";
	/**
	 * The string that should be used with an intent to get the ID(flag) from an
	 * intent
	 */
	public static final String INTENT_GET_ID = "ID";
	/**
	 * The string to be used for a time format as HH:MM
	 */
	public static final String HOUR_MINUTE_FORMAT = "%H:%M";

	private Constants() {
	}

	/*
	 * START GEOFENCE
	 */

	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Debug constants
	public static final String DEBUG_GEOFENCES_TAG = "Geofences";

	public static final String DEBUG_GEOFENCES_CONNECTED = "Connects to Location Services";
	
	public static final String DEBUG_GEOFENCES_DISCONNECTED = "Disconnects from Location Services";

	public static final String DEBUG_GEOFENCES_ADDED_SUCCESS = "Successfully added all geofences";
	
	public static final String DEBUG_GEOFENCES_ADDED_ERROR = "An error occured when adding geofences";

	// Intent actions
	public static final String ACTION_GEOFENCES_ADDED = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_ADDED";

	public static final String ACTION_GEOFENCES_ADD_ERROR = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_ADD_ERROR";
	
	public static final String ACTION_GEOFENCES_CONNECTION_FAILED = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_CONNECTION_FAILED";
	
	// Intent extras
	public static final String EXTRAS_TAG_GEOFENCES_ERROR_CODE = "ERROR_CODE";

	/*
	 * END GEOFENCE
	 */

}
