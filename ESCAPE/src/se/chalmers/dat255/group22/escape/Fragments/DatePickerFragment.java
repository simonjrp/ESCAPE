package se.chalmers.dat255.group22.escape.Fragments;

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

public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {

	private int spinnerId;
	public static final String SPINNER_ID = "SPINNER_ID";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
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

		// Used to check if year chosen is current year
		Calendar tempCalendar = Calendar.getInstance();
		NewTaskActivity activity = (NewTaskActivity) getActivity();

		// Retrieve spinner and adapter to be able to add new custom date
		Spinner spinner = (Spinner) activity.findViewById(spinnerId);
		SpinnerDayAdapter adapter = (SpinnerDayAdapter) spinner.getAdapter();
		adapter.clear();

		// Add the standard date labels to the spinner again
		adapter.add(getActivity().getString(R.string.todayLabel));
		adapter.add(getActivity().getString(R.string.tomorrowLabel));
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
		adapter.addData(new Date(tempCalendar.getTimeInMillis()));

	}

	public String getMonthLabel(int month) {
		String[] months = getActivity().getResources().getStringArray(
				R.array.months);
		return months[month];
	}

}
