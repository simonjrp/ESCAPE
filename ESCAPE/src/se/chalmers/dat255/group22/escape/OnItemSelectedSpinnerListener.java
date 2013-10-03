package se.chalmers.dat255.group22.escape;

import se.chalmers.dat255.group22.escape.Fragments.DatePickerFragment;
import se.chalmers.dat255.group22.escape.Fragments.TimePickerFragment;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

public class OnItemSelectedSpinnerListener implements OnItemSelectedListener {

	private Activity activity;
	private String spinnerType;
	private int spinnerId;
	public static final String DATE_SPINNER = "DATE";
	public static final String TIME_SPINNER = "TIME";

	public OnItemSelectedSpinnerListener(Activity activity, String spinnerType,
			int spinnerId) {
		this.activity = activity;
		this.spinnerType = spinnerType;
		this.spinnerId = spinnerId;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent
				.getAdapter();

		if (position == adapter.getCount() - 1) {
			FragmentManager fragmentManager = activity.getFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment previousDialog = fragmentManager
					.findFragmentByTag("dialog");

			if (previousDialog != null) {
				transaction.remove(previousDialog);
			}

			transaction.addToBackStack(null);

			// Start the fragment corresponding to the spinner type (date or
			// time)
			DialogFragment dialogFragment = spinnerType.equals(DATE_SPINNER) ? new DatePickerFragment()
					: new TimePickerFragment();
			Bundle args = new Bundle();
			args.putInt(DatePickerFragment.SPINNER_ID, spinnerId);
			dialogFragment.setArguments(args);
			dialogFragment.show(fragmentManager, "dialog");
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
	}

}
