package se.chalmers.dat255.group22.escape.listeners;

import se.chalmers.dat255.group22.escape.fragments.dialogfragments.CategoryCreatorFragment;
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

import se.chalmers.dat255.group22.escape.fragments.dialogfragments.DatePickerFragment;
import se.chalmers.dat255.group22.escape.fragments.dialogfragments.TimePickerFragment;

/**
 * Class representing an OnItemSelectedListener for spinners. Used mainly to
 * bring up date or time dialog pickers when clicking on a specific item in some
 * spinners.
 * 
 * @author Simon Persson
 */
public class OnItemSelectedSpinnerListener implements OnItemSelectedListener {

	/**
	 * Constant used to determine whether to bring up a time, date or category
	 * picker.
	 */
	public static final int DATE_SPINNER = 419;
	/**
	 * * Constant used to determine whether to bring up a time, date or category
	 * picker.
	 */
	public static final int TIME_SPINNER = 2019;
	/**
	 * Constant used to determine whether to bring up a time, date or category
	 * picker.
	 */
	public static final int CATEGORY_SPINNER = 319;
	private Activity activity;
	private int spinnerType;
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
	public OnItemSelectedSpinnerListener(Activity activity, int spinnerType,
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
			DialogFragment dialogFragment = null;
			switch (spinnerType) {
				case DATE_SPINNER :
					dialogFragment = new DatePickerFragment();
					break;
				case TIME_SPINNER :
					dialogFragment = new TimePickerFragment();
					break;
				case CATEGORY_SPINNER :
					dialogFragment = new CategoryCreatorFragment();
					break;
				default :
					break;
			}
			Bundle args = new Bundle();
			args.putInt(DatePickerFragment.SPINNER_ID, spinnerId);
			if (dialogFragment != null)
				dialogFragment.setArguments(args);
			dialogFragment.show(fragmentManager, "dialog");
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing
	}

}
