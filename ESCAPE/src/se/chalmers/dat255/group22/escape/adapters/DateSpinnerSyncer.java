package se.chalmers.dat255.group22.escape.adapters;

import se.chalmers.dat255.group22.escape.fragments.dialogfragments.DatePickerFragment;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Class used to sync a "from-date-spinner" and a "to-date-spinner"
 * 
 * @author Simon Persson
 */
public class DateSpinnerSyncer {

	private Activity activity;
	private Spinner dayFromSpinner;
	private Spinner dayToSpinner;
	private SpinnerDayAdapter dayFromAdapter;
	private SpinnerDayAdapter dayToAdapter;

	/**
	 * Create a new syncer for a date-from and a to-date-spinner.
	 * 
	 * @param activity
	 *            The activity that should be used to bring up dialogs.
	 * @param dayFromSpinner
	 *            The spinner used to select a "from date".
	 * @param dayToSpinner
	 *            The spinner used to select a "to date".
	 */
	public DateSpinnerSyncer(Activity activity, Spinner dayFromSpinner,
			Spinner dayToSpinner) {

		this.activity = activity;

		this.dayFromSpinner = dayFromSpinner;
		this.dayFromSpinner
				.setOnItemSelectedListener(new DateFromOnItemSelectedListener());
		dayFromAdapter = (SpinnerDayAdapter) dayFromSpinner.getAdapter();

		this.dayToSpinner = dayToSpinner;
		this.dayToSpinner
				.setOnItemSelectedListener(new DateToOnItemSelectedListener());
		dayToAdapter = (SpinnerDayAdapter) dayToSpinner.getAdapter();
	}

	/*
	 * The OnItemSelectedListener for the from-date-spinner.
	 */
	private class DateFromOnItemSelectedListener
			implements
				AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// If pick a date is pressed, bring up date picker dialog.
			if (position == dayFromAdapter.getCount() - 1) {
				showDatePickerDialog(dayFromSpinner.getId());
			} else {

				// If the new "from-date-value" is later than the currently
				// selected "to-date-value", this gives the to-date-spinner
				// the "from-date-spinner"'s value.
				long fromTimeInMillis = dayFromAdapter.getData(position)
						.getTime();
				long toTimeInMillis = dayToAdapter.getData(
						dayToSpinner.getSelectedItemPosition()).getTime();
				long timeDiff = toTimeInMillis - fromTimeInMillis;
				boolean fromTimeLaterThanToTime = timeDiff < 0;

				if (fromTimeLaterThanToTime) {
					if (position == 3) {
						dayToAdapter.addDate(dayFromAdapter.getData(3));
					} else {
						dayToSpinner.setSelection(position);
					}
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
			// Do nothing...
		}
	}

	/*
	 * The OnItemSelectedListener for the to-date-spinner.
	 */
	private class DateToOnItemSelectedListener
			implements
				AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			if (position == dayToAdapter.getCount() - 1) {
				showDatePickerDialog(dayToSpinner.getId());
			} else {
				// If the new "to-date-value" is earlier than the currently
				// selected "from-date-value", this gives the "to-date-spinner"
				// the
				// "from-date-spinner"'s value.
				long fromTimeInMillis = dayFromAdapter.getData(
						dayFromSpinner.getSelectedItemPosition()).getTime();
				long toTimeInMillis = dayToAdapter.getData(
						dayToSpinner.getSelectedItemPosition()).getTime();
				long timeDiff = toTimeInMillis - fromTimeInMillis;
				boolean toTimeEarlierThanFromTime = timeDiff < 0;

				// Resets the spinner to previous value if the
				// "from-date-spinner"
				// has
				// a date value that's after the newly selected "to-date".
				if (toTimeEarlierThanFromTime) {
					if (dayFromAdapter.getCount() == 4) {
						dayToAdapter.addDate(dayFromAdapter
								.getData(dayFromAdapter.getCount() - 2));
					}

					dayToSpinner.setSelection(dayFromSpinner
							.getSelectedItemPosition());
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
			// Do nothing...
		}
	}

	/*
	 * Method used to bring up a date picker dialog.
	 */
	private void showDatePickerDialog(int id) {
		// Opens a date or time picker dialog if user clicks on last item in a
		// spinner that's using this class as a onItemSelectedListener.
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment previousDialog = fragmentManager.findFragmentByTag("dialog");

		if (previousDialog != null) {
			transaction.remove(previousDialog);
		}

		transaction.addToBackStack(null);

		// Start the fragment corresponding to the spinner type (date or
		// time)
		DialogFragment dialogFragment = new DatePickerFragment();

		Bundle args = new Bundle();
		args.putInt(DatePickerFragment.SPINNER_ID, id);
		if (dialogFragment != null)
			dialogFragment.setArguments(args);
		dialogFragment.show(fragmentManager, "dialog");
	}
}
