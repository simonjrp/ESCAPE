package se.chalmers.dat255.group22.escape.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A container for constants used in the application.
 * 
 * @author tholene, Simon Persson
 */
public final class Constants {

	private Constants() {
	}

	/**
	 * The tag of the app, to use when debugging
	 */
	public static final String APPTAG = "ESCAPE";
	/**
	 * Improve readability by using this when creating a new row in a one-line
	 * string.
	 */
	public static final String NEW_ROW = "\n";
	/**
	 * The ID that should be used with an intent to indicate that you want to
	 * edit a task
	 */
	public static final int EDIT_TASK_ID = 0;
	/**
	 * This value should be used when manually setting the height of a view to 0
	 * (invisible) during runtime.
	 */
	public static final int NO_HEIGHT = 0;
	/**
	 * Improve readability when checking lengths by using this value.
	 */
	public static final int EMPTY = 0;
	/**
	 * This value is used when setting a paint flag for a TextView.
	 */
	public static final int DEFAULT_PAINT_FLAG = 1;
	/**
	 * Offset to be used when setting the height of a TextView.
	 */
	public static final int TEXTVIEW_HEIGHT_OFFSET = 5;
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
	/**
	 * The integer describing how many relative date values there is in a date
	 * spinner.
	 */
	public static final int NBR_OF_REL_DATES = 3;
	/**
	 * The integer describing how many relative time values there is in a time
	 * spinner.
	 */
	public static final int NBR_OF_REL_TIMES = 4;
	/**
	 * The key to use when getting reminder type from an intent.
	 */
	public static final String REMINDER_TYPE = "REMINDER_TYPE";
	/*
	 * ******** NOTIFICATION HANDLER ******** *
	 */

	public static final String DEBUG_GEOFENCES_CONNECTED = "Connects to Location Services";
	// Debug constants
	public static final String DEBUG_GEOFENCES_DISCONNECTED = "Disconnects from Location Services";
	public static final String DEBUG_GEOFENCES_ADD_SUCCESS = "Successfully added specified geofences";
	public static final String DEBUG_GEOFENCES_ADD_ERROR = "An error occured when adding specified geofences";
	public static final String DEBUG_GEOFENCES_REMOVE_SUCCESS = "Successfully removed specified geofences";
	public static final String DEBUG_GEOFENCES_REMOVE_ERROR = "An error occured when removing specified geofences";

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

	/*
	 * ******** GEOFENCE ******** *
	 */

	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final float GEOFENCE_DEFAULT_RADIUS = 100;

	// Intent actions
	public static final String ACTION_GEOFENCES_ADDED = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_ADDED";
	public static final String ACTION_GEOFENCES_ADD_ERROR = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_ADD_ERROR";
	public static final String ACTION_GEOFENCES_REMOVED = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_REMOVED";
	public static final String ACTION_GEOFENCES_REMOVE_ERROR = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_REMOVE_ERROR";
	public static final String ACTION_GEOFENCES_CONNECTION_FAILED = "se.chalmers.dat255.group22.escape.ACTION_GEOFENCES_CONNECTION_FAILED";
	// Intent extras
	public static final String EXTRAS_TAG_GEOFENCES_ERROR_CODE = "ERROR_CODE";

	// Enums

	public static enum REMOVE_TYPE {
		INTENT, LIST
	};

	/**
	 * Enum for different reminder types (time or gps).
	 * 
	 * @author Simon Persson
	 * 
	 */
	public enum ReminderType implements Parcelable {
		TIME, GPS;
		public static final Creator<ReminderType> CREATOR = new Creator<ReminderType>() {

			@Override
			public ReminderType createFromParcel(Parcel source) {
				return ReminderType.values()[source.readInt()];
			}

			@Override
			public ReminderType[] newArray(int size) {
				return new ReminderType[size];
			}

		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(ordinal());
		}
	};

}
