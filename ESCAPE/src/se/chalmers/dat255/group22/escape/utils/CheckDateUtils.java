package se.chalmers.dat255.group22.escape.utils;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class containing methods for checking when a {@link java.sql.Date} takes
 * place relative to system calendar.
 * 
 * @author Carl Jansson
 */
public class CheckDateUtils {

	/**
	 * Method to check if a date is today. Also returns true if the date is
	 * earlier than today
	 * 
	 * @param theDate
	 *            the date to see if it is today
	 * @return true if theDate is today or earlier
	 */
	public static boolean isToday(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time to compare with
		Calendar systemCalendar = Calendar.getInstance();
		// If it should return true only if today and not before use == instead
		// of >=
		return systemCalendar.get(Calendar.YEAR) >= theCalendar
				.get(Calendar.YEAR)
				&& systemCalendar.get(Calendar.DAY_OF_YEAR) >= theCalendar
						.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Method to check if a date is tomorrow
	 * 
	 * @param theDate
	 *            the date to see if it is tomorrow
	 * @return true of it is tomorrow
	 */
	public static boolean isTomorrow(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time and change its value so it
		// can be used in comparison with the given date.
		Calendar tmpDate = Calendar.getInstance();
		tmpDate.roll(Calendar.DAY_OF_YEAR, true);

		return tmpDate.get(Calendar.YEAR) == theCalendar.get(Calendar.YEAR)
				&& tmpDate.get(Calendar.DAY_OF_YEAR) == theCalendar
						.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Method to check if a date is this week. Note that it checks if it is this
	 * week and not 7 days ahead!
	 * 
	 * @param theDate
	 *            the date to see if it is this week
	 * @return true if it is this week
	 */
	public static boolean isThisWeek(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time
		Calendar tmpDate = Calendar.getInstance();

		return tmpDate.get(Calendar.YEAR) == theCalendar.get(Calendar.YEAR)
				&& tmpDate.get(Calendar.WEEK_OF_YEAR) == theCalendar
						.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Method to see if a date has already passed. the time of the day has no
	 * relevance.
	 * 
	 * @param theDate
	 *            the date to see if it has passed
	 * @return true if the date has passed
	 */
	public static boolean dateHasPassed(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time to compare with
		Calendar systemCalendar = Calendar.getInstance();
		// If it should return true only if today and not before use == instead
		// of >=
		return systemCalendar.get(Calendar.YEAR) > theCalendar
				.get(Calendar.YEAR)
				|| (systemCalendar.get(Calendar.YEAR) == theCalendar
						.get(Calendar.YEAR) && systemCalendar
						.get(Calendar.DAY_OF_YEAR) > theCalendar
						.get(Calendar.DAY_OF_YEAR));
	}
}
