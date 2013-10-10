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
}
