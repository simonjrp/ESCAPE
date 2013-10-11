package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import java.sql.Date;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.adapters.SpinnerTimeAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Spinner;
import android.widget.TimePicker;

/**
 * Class representing a time picker fragment.
 * 
 * @author Simon Persson
 */
public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

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
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this,
				hour, minute, DateFormat.is24HourFormat(getActivity()));
		spinnerId = this.getArguments().getInt(SPINNER_ID);

		return timePicker;
	}

	@Override
	public void onTimeSet(TimePicker timePicker, int hour, int minute) {

		// Retrieve spinner and adapter to be able to add new custom date
		Activity activity = getActivity();
		Spinner spinner = (Spinner) activity.findViewById(spinnerId);
		SpinnerTimeAdapter adapter = (SpinnerTimeAdapter) spinner.getAdapter();

		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.HOUR_OF_DAY, hour);
		tempCalendar.set(Calendar.MINUTE, minute);

		adapter.addTime(new Date(tempCalendar.getTimeInMillis()));
	}
}
