package se.chalmers.dat255.group22.escape.adapters;

import java.sql.Date;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.fragments.dialogfragments.DatePickerFragment;
import se.chalmers.dat255.group22.escape.fragments.dialogfragments.TimePickerFragment;
import se.chalmers.dat255.group22.escape.utils.Constants;
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
public class EventSpinnerSyncer {

	private Activity activity;
	private Spinner dayFromSpinner;
	private Spinner dayToSpinner;
	private SpinnerDayAdapter dayFromAdapter;
	private SpinnerDayAdapter dayToAdapter;
	private Spinner timeFromSpinner;
	private Spinner timeToSpinner;
	private SpinnerTimeAdapter timeFromAdapter;
	private SpinnerTimeAdapter timeToAdapter;

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
	public EventSpinnerSyncer(Activity activity, Spinner dayFromSpinner,
			Spinner dayToSpinner, Spinner timeFromSpinner, Spinner timeToSpinner) {

		this.activity = activity;

		this.dayFromSpinner = dayFromSpinner;
		dayFromAdapter = (SpinnerDayAdapter) dayFromSpinner.getAdapter();
		this.dayFromSpinner
				.setOnItemSelectedListener(new DateFromOnItemSelectedListener());

		this.dayToSpinner = dayToSpinner;
		dayToAdapter = (SpinnerDayAdapter) dayToSpinner.getAdapter();
		this.dayToSpinner
				.setOnItemSelectedListener(new DateToOnItemSelectedListener());

		this.timeFromSpinner = timeFromSpinner;
		timeFromAdapter = (SpinnerTimeAdapter) timeFromSpinner.getAdapter();
		this.timeFromSpinner
				.setOnItemSelectedListener(new TimeFromOnItemSelectedListener());

		this.timeToSpinner = timeToSpinner;
		timeToAdapter = (SpinnerTimeAdapter) timeToSpinner.getAdapter();
		this.timeToSpinner
				.setOnItemSelectedListener(new TimeToOnItemSelectedListener());

		initTimeSpinners();

	}

	public void initTimeSpinners() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.MINUTE, 0);

		today.set(Calendar.HOUR_OF_DAY,
				(today.get(Calendar.HOUR_OF_DAY) + 1) % 23);
		timeFromAdapter.addTime(new Date(today.getTimeInMillis()));

		today.set(Calendar.HOUR_OF_DAY,
				(today.get(Calendar.HOUR_OF_DAY) + 1) % 23);
		timeToAdapter.addTime(new Date(today.getTimeInMillis()));

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
				long fromDateInMillis = dayFromAdapter.getData(position)
						.getTime();
				long toDateInMillis = dayToAdapter.getData(
						dayToSpinner.getSelectedItemPosition()).getTime();
				long dateDiff = toDateInMillis - fromDateInMillis;
				boolean fromDateLaterThanToDate = dateDiff < 0;

				if (fromDateLaterThanToDate) {
					if (position == 3) {
						dayToAdapter.addDate(dayFromAdapter.getData(3));
					} else {
						dayToSpinner.setSelection(position);
					}
				}

				boolean toDateEqualsFromDate = toDateInMillis == fromDateInMillis;

				long fromTimeInMillis = timeFromAdapter.getData(
						timeFromSpinner.getSelectedItemPosition()).getTime();
				long toTimeInMillis = timeToAdapter.getData(
						timeToSpinner.getSelectedItemPosition()).getTime();
				long timeDiff = toTimeInMillis - fromTimeInMillis;
				boolean toTimeLaterThanFromTime = timeDiff < 0;

				if (toDateEqualsFromDate && toTimeLaterThanFromTime) {
					dayToAdapter.addDate(new Date(dayToAdapter.getData(
							dayToSpinner.getSelectedItemPosition()).getTime()
							+ Constants.ONE_DAY_IN_MILLIS));
				}
			}

		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
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
				long fromDateInMillis = dayFromAdapter.getData(
						dayFromSpinner.getSelectedItemPosition()).getTime();
				long toDateInMillis = dayToAdapter.getData(
						dayToSpinner.getSelectedItemPosition()).getTime();
				long dateDiff = toDateInMillis - fromDateInMillis;
				boolean toDateEarlierThanFromDate = dateDiff < 0;

				// Resets the spinner to previous value if the
				// "from-date-spinner"
				// has
				// a date value that's after the newly selected "to-date".
				if (toDateEarlierThanFromDate) {
					if (dayFromAdapter.getCount() == 4) {
						dayToAdapter.addDate(dayFromAdapter
								.getData(dayFromAdapter.getCount() - 2));
					}

					dayToSpinner.setSelection(dayFromSpinner
							.getSelectedItemPosition());
				}

				boolean toDateEqualsFromDate = toDateInMillis == fromDateInMillis;

				long fromTimeInMillis = timeFromAdapter.getData(
						timeFromSpinner.getSelectedItemPosition()).getTime();
				long toTimeInMillis = timeToAdapter.getData(
						timeToSpinner.getSelectedItemPosition()).getTime();
				long timeDiff = toTimeInMillis - fromTimeInMillis;
				boolean toTimeEarlierThanFromTime = timeDiff < 0;

				if (toDateEqualsFromDate && toTimeEarlierThanFromTime) {
					timeToAdapter
							.addTime(timeFromAdapter.getData(timeFromSpinner
									.getSelectedItemPosition()));
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
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

	private class TimeFromOnItemSelectedListener
			implements
				AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (position == timeFromAdapter.getCount() - 1) {
				showTimePickerDialog(timeFromSpinner.getId());
			} else {
				long timeFromInMillis = timeFromAdapter.getData(position)
						.getTime();

				long timeToInMillis = timeToAdapter.getData(
						timeToSpinner.getSelectedItemPosition()).getTime();

				long timeDiff = timeToInMillis - timeFromInMillis;

				boolean startDateEqualsEndDate = dayFromAdapter.getData(
						dayFromSpinner.getSelectedItemPosition()).getTime() == dayToAdapter
						.getData(dayToSpinner.getSelectedItemPosition())
						.getTime();
				boolean fromTimeLaterThanToTime = timeDiff < 0;
				if (startDateEqualsEndDate && fromTimeLaterThanToTime) {
					timeToAdapter.addTime(new Date(timeFromInMillis
							+ Constants.ONE_HOUR_IN_MILLIS));
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing...
		}
	}

	private class TimeToOnItemSelectedListener
			implements
				AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			if (position == timeToAdapter.getCount() - 1) {
				showTimePickerDialog(timeToSpinner.getId());
			} else {
				long timeFromInMillis = timeFromAdapter.getData(
						timeFromSpinner.getSelectedItemPosition()).getTime();

				long timeToInMillis = timeToAdapter.getData(position).getTime();

				long timeDiff = timeToInMillis - timeFromInMillis;

				boolean startDateEqualsEndDate = dayFromAdapter.getData(
						dayFromSpinner.getSelectedItemPosition()).getTime() == dayToAdapter
						.getData(dayToSpinner.getSelectedItemPosition())
						.getTime();

				boolean toTimeEarlierThanFromTime = timeDiff < 0;

				if (startDateEqualsEndDate && toTimeEarlierThanFromTime) {
					dayToAdapter.addDate(new Date(dayToAdapter.getData(
							dayToSpinner.getSelectedItemPosition()).getTime()
							+ Constants.ONE_DAY_IN_MILLIS));
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing...
		}
	}

	/*
	 * Method used to bring up a date picker dialog.
	 */
	private void showTimePickerDialog(int id) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment previousDialog = fragmentManager.findFragmentByTag("dialog");

		if (previousDialog != null) {
			transaction.remove(previousDialog);
		}

		transaction.addToBackStack(null);

		// Start the fragment corresponding to the spinner type (date or
		// time)
		DialogFragment dialogFragment = dialogFragment = new TimePickerFragment();

		Bundle args = new Bundle();
		args.putInt(DatePickerFragment.SPINNER_ID, id);
		if (dialogFragment != null)
			dialogFragment.setArguments(args);
		dialogFragment.show(fragmentManager, "dialog");
	}
}
