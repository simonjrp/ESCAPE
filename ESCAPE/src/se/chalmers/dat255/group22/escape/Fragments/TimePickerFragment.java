package se.chalmers.dat255.group22.escape.fragments;

import java.sql.Date;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTimeAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Spinner;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	private int spinnerId;
	public static final String SPINNER_ID = "SPINNER_ID";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this,
				hour, minute, DateFormat.is24HourFormat(getActivity()));
		spinnerId = this.getArguments().getInt("SPINNER_ID");

		return timePicker;
	}

	@Override
	public void onTimeSet(TimePicker timePicker, int hour, int minute) {
		Activity activity = getActivity();

		// Retrieve spinner and adapter to be able to add new custom time
		Spinner spinner = (Spinner) activity.findViewById(spinnerId);
		SpinnerTimeAdapter adapter = (SpinnerTimeAdapter) spinner.getAdapter();
		adapter.clear();

		// Add the standard time labels to the spinner again
		adapter.add(activity.getString(R.string.morning));
		adapter.add(activity.getString(R.string.afternoon));
		adapter.add(activity.getString(R.string.evening));
		adapter.add(activity.getString(R.string.night));
		adapter.add(hour + ":" + minute);
		adapter.add(activity.getString(R.string.pickTimeLabel));
		spinner.setSelection(adapter.getCount() - 2, true);

		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.YEAR, 0);
		tempCalendar.set(Calendar.MONTH, 0);
		tempCalendar.set(Calendar.DAY_OF_MONTH, 0);
		tempCalendar.set(Calendar.HOUR_OF_DAY, hour);
		tempCalendar.set(Calendar.MINUTE, minute);
		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		adapter.addData(new Date(tempCalendar.getTimeInMillis()));

	}
}
