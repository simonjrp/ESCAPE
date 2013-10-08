package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.SpinnerDayAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Spinner;

/**
 * Class representing a date picker fragment.
 * 
 * @author Simon Persson
 */
public class DatePickerFragment extends DialogFragment
		implements
			OnDateSetListener {

	/**
	 * Constant used to get the ID of the spinner that wants to bring up this
	 * dialog
	 */
	public static final String SPINNER_ID = "SPINNER_ID";
	private int spinnerId;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Gets the current date and initializes the date picker with
		// it.
		final Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this,
				year, month, day);
		spinnerId = this.getArguments().getInt("SPINNER_ID");
		return datePicker;
	}

	@Override
	public void onDateSet(DatePicker datePicker, int year, int month, int day) {

		// Retrieve spinner and adapter to be able to add new custom date
		Activity activity = getActivity();
		Spinner spinner = (Spinner) activity.findViewById(spinnerId);
		SpinnerDayAdapter adapter = (SpinnerDayAdapter) spinner.getAdapter();

		// Stores new date in a Calendar object.
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.YEAR, year);
		tempCalendar.set(Calendar.MONTH, month);
		tempCalendar.set(Calendar.DAY_OF_MONTH, day);
		tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tempCalendar.set(Calendar.MINUTE, 0);
		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		// Check if the custom date equals to some predefined value in the
		// spinner
		List<Date> spinnerData = adapter.getAllData();
		int itemPosition = -1;
		for (Date spinnerDate : spinnerData) {
			Calendar spinnerDateAsCalendar = Calendar.getInstance();
			spinnerDateAsCalendar.setTime(spinnerDate);
			int spinnerYear = spinnerDateAsCalendar.get(Calendar.YEAR);
			int spinnerMonth = spinnerDateAsCalendar.get(Calendar.MONTH);
			int spinnerDay = spinnerDateAsCalendar.get(Calendar.DAY_OF_MONTH);
			boolean alreadyExists = (spinnerYear == year
					&& spinnerMonth == month && spinnerDay == day);
			if (alreadyExists) {
				itemPosition = spinnerData.indexOf(spinnerDate);
				break;
			}
		}

		if (itemPosition != -1) {
			spinner.setSelection(itemPosition);
		} else {
			// Creates a custom day label for the spinner
			String customLabel = null;
			if (year == tempCalendar.get(Calendar.YEAR)) {
				customLabel = getMonthLabel(month) + " " + day;
			} else {
				customLabel = getMonthLabel(month) + " " + day + "," + year;
			}
            adapter.addCustomEntry(customLabel,
                    new Date(tempCalendar.getTimeInMillis()));
			spinner.setSelection(spinner.getCount() - 1, true);
		}

	}

	// TODO make more general.

	/**
	 * Method for getting the text representation of a month, providing the
	 * number of the month.
	 * 
	 * @param month
	 *            The number of the month.
	 * @return A String containing the text representation of the wanted month.
	 */
	public String getMonthLabel(int month) {
		String[] months = getActivity().getResources().getStringArray(
				R.array.months);
		return months[month];
	}

}
