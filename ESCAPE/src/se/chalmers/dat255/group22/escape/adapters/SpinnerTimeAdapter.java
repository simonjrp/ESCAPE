package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.utils.Constants;

/**
 * An adapter that customizes the way spinners for choosing a time are
 * presented.
 * 
 * @author tholene, Simon Persson
 */
public class SpinnerTimeAdapter extends ArrayAdapter<String> {

	private ArrayList<String> times;
	private List<Date> timeData;
	private Context context;
	private Spinner spinner;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different times.
	 * 
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 */
	public SpinnerTimeAdapter(Context context, int textViewResourceId,
			Spinner spinner) {
		super(context, textViewResourceId);
		// Array containing different times for an event
		this.context = context;
		this.spinner = spinner;

		// Create data array
		timeData = new ArrayList<Date>();
		Calendar tempCalendar = Calendar.getInstance();

		// Sets minutes and seconds of reference time to zero
		tempCalendar.set(0, 0, 0, 0, 0, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		// Saves the standard time data relative to current day
		tempCalendar.set(Calendar.HOUR_OF_DAY, 9);
		timeData.add(new Date(tempCalendar.getTimeInMillis()));
		tempCalendar.set(Calendar.HOUR_OF_DAY, 13);
		timeData.add(new Date(tempCalendar.getTimeInMillis()));
		tempCalendar.set(Calendar.HOUR_OF_DAY, 17);
		timeData.add(new Date(tempCalendar.getTimeInMillis()));
		tempCalendar.set(Calendar.HOUR_OF_DAY, 20);
		timeData.add(new Date(tempCalendar.getTimeInMillis()));

		// Create labels and add to adapter.
		times = new ArrayList<String>();
		times.add(context.getString(R.string.morning));
		times.add(context.getString(R.string.afternoon));
		times.add(context.getString(R.string.evening));
		times.add(context.getString(R.string.night));
		times.add(context.getString(R.string.pick_time_label));

		clear();
		addAll(times);

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.time_spinner_item, parent, false);

		TextView timeAsText = (TextView) row
				.findViewById(R.id.spinner_time_as_text);
		timeAsText.setText(times.get(position));

		TextView timeAsTime = (TextView) row
				.findViewById(R.id.spinner_time_as_time);

		if (position < getCount() - 1) {
			Date date = getData(position);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			timeAsTime.setText(dateFormat.format(date));
		}

		timeAsTime.setVisibility(View.VISIBLE);

		if (position == getCount() - 1) {

			View v = (View) timeAsText.getParent();
			v.setBackgroundResource(R.drawable.spinner_button_colors);
		}
		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.time_spinner_item_single, parent,
				false);

		TextView timeAsText = (TextView) row
				.findViewById(R.id.spinner_time_as_text);

		timeAsText.setText(times.get(position));

		// Wierd hack to make the width of the spinner relative to its current
		// item
		parent.getLayoutParams().width = timeAsText.getLayoutParams().width - 100;
		row.getLayoutParams().width = timeAsText.getLayoutParams().width - 100;

		return row;
	}

	public Date getData(int position) {
		return timeData.get(position);
	}

	public List<Date> getAllData() {
		return timeData;
	}

	public void addTime(Date newTime) {
		Calendar newTimeAsCal = dateToCalendar(newTime);
		int timeExists = timeExists(newTimeAsCal);

		if (timeExists != -1) {
			spinner.setSelection(timeExists);
		} else {
			// Formats the time so that, for example, 12 o clock is shown as
			// 12:00 instead of 12:0

			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm",
					Locale.getDefault());
			String customLabel = timeFormatter.format(newTimeAsCal.getTime());

			// Finally, add the data to the adapter and select new item in
			// spinner.
			if (timeData.size() > Constants.NBR_OF_REL_TIMES) {
				timeData.remove(timeData.size() - 2);
			}
			timeData.add(new Date(newTimeAsCal.getTimeInMillis()));

			// Refresh adapter's internal list.
			clear();
			add(context.getString(R.string.morning));
			add(context.getString(R.string.afternoon));
			add(context.getString(R.string.evening));
			add(context.getString(R.string.night));
			add(customLabel);
			add(context.getString(R.string.pick_time_label));

			// Refresh local list. This
			// is necessary because the adapters internal list of items and
			// the local list is unsynced.
			times.clear();
			times.add(context.getString(R.string.morning));
			times.add(context.getString(R.string.afternoon));
			times.add(context.getString(R.string.evening));
			times.add(context.getString(R.string.night));
			times.add(customLabel);
			times.add(context.getString(R.string.pick_time_label));
			this.notifyDataSetChanged();

			// The first line below is necessary to fire a onItemSelected event.
			spinner.setSelection(0, false);
			spinner.setSelection(spinner.getCount() - 2, true);
		}

	}

	private int timeExists(Calendar newTimeAsCal) {

		int itemPosition = -1;
		for (Date spinnerTime : timeData) {
			Calendar spinnerTimeAsCal = dateToCalendar(spinnerTime);

			int spinnerHour = spinnerTimeAsCal.get(Calendar.HOUR_OF_DAY);
			int spinnerMinute = spinnerTimeAsCal.get(Calendar.MINUTE);

			int newHour = newTimeAsCal.get(Calendar.HOUR_OF_DAY);
			int newMinute = newTimeAsCal.get(Calendar.MINUTE);

			boolean alreadyExists = (spinnerHour == newHour && spinnerMinute == newMinute);
			if (alreadyExists) {
				itemPosition = timeData.indexOf(spinnerTime);
				break;
			}
		}

		return itemPosition;
	}

	private Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// Set all irrelevant values to 0
		calendar.set(Calendar.YEAR, 0);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

}
