package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.utils.Constants;
import se.chalmers.dat255.group22.escape.utils.IntegerToMonthConverter;

/**
 * An adapter that customizes the way spinners for choosing a day are presented.
 * 
 * @author tholene, Simon Persson
 */
public class SpinnerDayAdapter extends ArrayAdapter<String> {

	public static final long ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	public static final long ONE_WEEK_IN_MILLIS = ONE_DAY_IN_MILLIS * 7;
	private ArrayList<String> days;
	private Context context;
	private List<Date> dateData;
	private Spinner spinner;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different days.
	 * 
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 * 
	 * @param spinner
	 *            the spinner that gets assigned to this adapter.
	 */
	public SpinnerDayAdapter(Context context, int textViewResourceId,
			Spinner spinner) {
		super(context, textViewResourceId);
		this.context = context;
		this.spinner = spinner;

		// Sets seconds and milliseconds of reference time
		// to zero
		// Get the current day to display as the dynamic spinner item
		Calendar tempCalendar = Calendar.getInstance();
		int weekday = tempCalendar.get(Calendar.DAY_OF_WEEK);
		String[] weekDays = context.getResources().getStringArray(
				R.array.weekdays);
		String nextWeekSameDay = context.getResources().getString(
				R.string.nextweeksameday)
				+ " " + weekDays[weekday];

		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		dateData = new ArrayList<Date>();
		long currentTimeInMillis = tempCalendar.getTimeInMillis();
		dateData.add(new Date(currentTimeInMillis));
		dateData.add(new Date(currentTimeInMillis + 86400000));
		dateData.add(new Date(currentTimeInMillis + ONE_WEEK_IN_MILLIS));

		days = new ArrayList<String>();
		days.add(context.getString(R.string.today_label));
		days.add(context.getString(R.string.tomorrow_label));
		days.add(nextWeekSameDay);
		days.add(context.getString(R.string.pick_day_label));

		clear();
		addAll(days);

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater
				.inflate(R.layout.simple_spinner_item, parent, false);

		TextView day = (TextView) row.findViewById(R.id.simple_spinner_text);

		day.setText(days.get(position));
		// set gray color to "Pick a date" item
		if (position == getCount() - 1) {
			row.setBackgroundResource(R.drawable.spinner_button_colors);
		}

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.simple_spinner_item_single,
				parent, false);
		TextView day = (TextView) row.findViewById(R.id.simple_spinner_text);

		day.setText(days.get(position));
		// Wierd hack to make the width of the spinner relative to its current
		// item
		parent.getLayoutParams().width = day.getLayoutParams().width - 100;
		row.getLayoutParams().width = day.getLayoutParams().width - 100;

		return row;

	}

	public Date getData(int position) {
		return dateData.get(position);
	}

	public List<Date> getAllData() {
		return dateData;
	}

	public void addDate(Date newDate) {
		Calendar todayCalendar = Calendar.getInstance();
		Calendar newDateAsCal = dateToCalendar(newDate);
		int dateExists = dateExists(newDateAsCal);

		/*
		 * If the new date equals to a predefined existing date, select the
		 * predefined in the spinner. If not, add a new one.
		 */
		if (dateExists != -1) {
			spinner.setSelection(dateExists);
		} else {

			// Creates a custom day label for the spinner
			String customLabel = null;
			IntegerToMonthConverter converter = new IntegerToMonthConverter();

			int newYear = newDateAsCal.get(Calendar.YEAR);
			String newMonth = converter.getMonthLabel(context,
					newDateAsCal.get(Calendar.MONTH));
			int newDay = newDateAsCal.get(Calendar.DAY_OF_MONTH);

			if (newYear == todayCalendar.get(Calendar.YEAR)) {
				customLabel = newMonth + " " + newDate;
			} else {
				customLabel = newMonth + " " + newDay + ", " + newYear;
			}

			// Finally, add the data to the adapter and select new item in
			// spinner.
			if (dateData.size() > Constants.NBR_OF_REL_DATES) {
				dateData.remove(dateData.size() - 2);
			}
			dateData.add(new Date(newDateAsCal.getTimeInMillis()));

			String nextWeekSameDayLabel = days.get(2);

			// Refresh adapter's internal list.
			clear();
			add(context.getString(R.string.today_label));
			add(context.getString(R.string.tomorrow_label));
			add(nextWeekSameDayLabel);
			add(customLabel);
			add(context.getString(R.string.pick_day_label));
			this.notifyDataSetChanged();

			// Refresh local list. This
			// is necessary because the adapters internal list of items and
			// the local list is unsynced.

			days.clear();
			days.add(context.getString(R.string.today_label));
			days.add(context.getString(R.string.tomorrow_label));
			days.add(nextWeekSameDayLabel);
			days.add(customLabel);
			days.add(context.getString(R.string.pick_day_label));

			// The first line below is necessary to fire a onItemSelected event.
			spinner.setSelection(0, false);
			spinner.setSelection(spinner.getCount() - 2, true);
		}

	}

	private int dateExists(Calendar newDateAsCal) {
		int itemPosition = -1;
		for (Date date : dateData) {
			Calendar spinnerDateAsCal = dateToCalendar(date);
			int spinnerYear = spinnerDateAsCal.get(Calendar.YEAR);
			int spinnerMonth = spinnerDateAsCal.get(Calendar.MONTH);
			int spinnerDay = spinnerDateAsCal.get(Calendar.DAY_OF_MONTH);

			int newYear = newDateAsCal.get(Calendar.YEAR);
			int newMonth = newDateAsCal.get(Calendar.MONTH);
			int newDay = newDateAsCal.get(Calendar.DAY_OF_MONTH);
			boolean alreadyExists = (spinnerYear == newYear
					&& spinnerMonth == newMonth && spinnerDay == newDay);
			if (alreadyExists) {
				itemPosition = dateData.indexOf(date);
				break;
			}
		}

		return itemPosition;

	}

	private Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// Set all irrelevant values to 0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

}
