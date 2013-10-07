package se.chalmers.dat255.group22.escape.listeners;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class TimeSetListener implements TimePickerDialog.OnTimeSetListener {

	private int spinnerId;

	public TimeSetListener(int spinnerId) {
		this.spinnerId = spinnerId;
	}

	@Override
	public void onTimeSet(TimePicker timePicker, int hour, int minute) {

	}

}
