package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTimeAdapter;

/**
 * Class representing a time picker fragment.
 * 
 * @author Simon Persson
 */
public class TimePickerFragment extends DialogFragment
		implements
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

		adapter.clear();

		// Formats the time so that, for example, 12 o clock is shown as 12:00
		// instead of 12:0
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
		String formattedTime = timeFormatter.format(calendar.getTime());

		// Add the standard time labels to the spinner again.
		adapter.add(activity.getString(R.string.morning));
		adapter.add(activity.getString(R.string.afternoon));
		adapter.add(activity.getString(R.string.evening));
		adapter.add(activity.getString(R.string.night));
		adapter.add(formattedTime);
		adapter.add(activity.getString(R.string.pickTimeLabel));
		spinner.setSelection(adapter.getCount() - 2, true);

		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.HOUR_OF_DAY, hour);
		tempCalendar.set(Calendar.MINUTE, minute);

		adapter.addTime(new Date(tempCalendar.getTimeInMillis()));
	}
}
