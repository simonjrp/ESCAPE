package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

		// Stores new date in a Calendar object.
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.YEAR, 0);
		tempCalendar.set(Calendar.MONTH, 0);
		tempCalendar.set(Calendar.DAY_OF_MONTH, 0);
		tempCalendar.set(Calendar.HOUR_OF_DAY, hour);
		tempCalendar.set(Calendar.MINUTE, minute);
		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		// Check if the custom date equals to some predefined value in the
		// spinner
		List<Date> spinnerData = adapter.getAllData();
		int itemPosition = -1;
		for (int i = 0; i < 4; i++) {
			Calendar spinnerTimeAsCal = Calendar.getInstance();
			spinnerTimeAsCal.setTime(spinnerData.get(i));
			int spinnerHour = spinnerTimeAsCal.get(Calendar.HOUR_OF_DAY);
			int spinnerMinute = spinnerTimeAsCal.get(Calendar.MINUTE);
			boolean alreadyExists = (spinnerHour == hour && spinnerMinute == minute);
			if (alreadyExists) {
				itemPosition = i;
				break;
			}
		}

		if (itemPosition != -1) {
			spinner.setSelection(itemPosition);
		} else {
			// Formats the time so that, for example, 12 o clock is shown as
			// 12:00 instead of 12:0

			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
			String customLabel = timeFormatter.format(tempCalendar.getTime());
			adapter.addCustomEntry(customLabel,
					new Date(tempCalendar.getTimeInMillis()));
			spinner.setSelection(spinner.getCount() - 2, true);
		}

	}
}
