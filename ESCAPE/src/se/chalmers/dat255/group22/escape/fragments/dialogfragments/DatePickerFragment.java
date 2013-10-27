package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import java.sql.Date;
import java.util.Calendar;

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

		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.YEAR, year);
		tempCalendar.set(Calendar.MONTH, month);
		tempCalendar.set(Calendar.DAY_OF_MONTH, day);

		adapter.addDate(new Date(tempCalendar.getTimeInMillis()));
	}
}
