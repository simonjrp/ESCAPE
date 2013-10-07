package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import java.sql.Date;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.NewTaskActivity;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.SpinnerDayAdapter;
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
		NewTaskActivity activity = (NewTaskActivity) getActivity();

		// Used to check if year chosen is current year
		Calendar tempCalendar = Calendar.getInstance();

		// Retrieve spinner and adapter to be able to add new custom date
		Spinner spinner = (Spinner) activity.findViewById(spinnerId);
		SpinnerDayAdapter adapter = (SpinnerDayAdapter) spinner.getAdapter();
		// TODO hacky code
		String nextWeekSameDayLabel = adapter.getItem(2);
		adapter.clear();

		// Add the standard date labels to the spinner again
		adapter.add(getActivity().getString(R.string.todayLabel));
		adapter.add(nextWeekSameDayLabel);
		adapter.add(getActivity().getString(R.string.thisWeekLabel));

		// Doesn't add year to text in spinner if chosen year equals current
		// year
		if (year == tempCalendar.get(Calendar.YEAR)) {
			adapter.add(getMonthLabel(month) + " " + day);
		} else {
			adapter.add(getMonthLabel(month) + " " + day + "," + year);
		}
		adapter.add(getActivity().getString(R.string.pickDayLabel));
		spinner.setSelection(adapter.getCount() - 2, true);

		tempCalendar.set(Calendar.YEAR, year);
		tempCalendar.set(Calendar.MONTH, month);
		tempCalendar.set(Calendar.DAY_OF_MONTH, day);
		tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tempCalendar.set(Calendar.MINUTE, 0);
		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		adapter.addData(new Date(tempCalendar.getTimeInMillis()));

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
