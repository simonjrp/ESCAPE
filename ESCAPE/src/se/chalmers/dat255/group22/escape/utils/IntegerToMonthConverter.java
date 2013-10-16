package se.chalmers.dat255.group22.escape.utils;

import android.content.Context;

import se.chalmers.dat255.group22.escape.R;

public class IntegerToMonthConverter {
	/**
	 * Method for getting the text representation of a month, providing the
	 * number of the month.
	 * 
	 * @param month
	 *            The number of the month.
	 * @return A String containing the text representation of the wanted month.
	 */
	public String getMonthLabel(Context context, int month) {
		String[] months = context.getResources().getStringArray(R.array.months);
		return months[month];
	}
}
