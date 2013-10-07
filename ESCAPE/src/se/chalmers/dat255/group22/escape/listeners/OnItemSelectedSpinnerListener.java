package se.chalmers.dat255.group22.escape.listeners;

import se.chalmers.dat255.group22.escape.fragments.dialogfragments.DatePickerFragment;
import se.chalmers.dat255.group22.escape.fragments.dialogfragments.TimePickerFragment;
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

/**
 * Class representing an OnItemSelectedListener for spinners. Used mainly to
 * bring up date or time dialog pickers when clicking on a specific item in some
 * spinners.
 * 
 * @author Simon Persson
 */
public class OnItemSelectedSpinnerListener implements OnItemSelectedListener {

	/**
	 * Constant used to determine whether to bring up a time or date picker.
	 */
	public static final String DATE_SPINNER = "DATE";
	/**
	 * Constant used to determine whether to bring up a time or date picker.
	 */
	public static final String TIME_SPINNER = "TIME";
	private Activity activity;
	private String spinnerType;
	private int spinnerId;

	/**
	 * Constructor for creating a new OnItemSelectedSpinnerListener.
	 * 
	 * @param activity
	 *            An activity holding a fragment manager to use when bringing up
	 *            dialogs.
	 * @param spinnerType
	 *            Should be DATE_SPINNER or TIME_SPINNER.
	 * @param spinnerId
	 *            The ID of the spinner assigned to this listener.
	 */
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

		// Opens a date or time picker dialog if user clicks on last item in a
		// spinner that's using this class as a onItemSelectedListener.
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
			DialogFragment dialogFragment = spinnerType.equals(DATE_SPINNER)
					? new DatePickerFragment()
					: new TimePickerFragment();
			Bundle args = new Bundle();
			args.putInt(DatePickerFragment.SPINNER_ID, spinnerId);
			dialogFragment.setArguments(args);
			dialogFragment.show(fragmentManager, "dialog");
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing
	}

}
