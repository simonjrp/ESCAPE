package se.chalmers.dat255.group22.escape.adapters;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.chalmers.dat255.group22.escape.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * An adapter that customizes the way spinners for choosing a day are presented.
 * 
 * @author tholene, Simon Persson
 */
public class SpinnerDayAdapter extends ArrayAdapter<String> {

	public static final long oneDayInMillis = 1000 * 60 * 60 * 24;
	public static final long oneWeekInMillis = oneDayInMillis * 7;
	private ArrayList<String> days;
	private Context context;
	private List<Date> dateData;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different days.
	 * 
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 * @param days
	 *            a stringarray that contains the string to be set for each item
	 *            in the dropdown list.
	 */
	public SpinnerDayAdapter(Context context, int textViewResourceId,
			ArrayList<String> days) {
		super(context, textViewResourceId, days);
		this.context = context;
		this.days = days;

		// Sets seconds and milliseconds of reference time
		// to zero
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		dateData = new ArrayList<Date>();
		long currentTimeInMillis = tempCalendar.getTimeInMillis();
		dateData.add(new Date(currentTimeInMillis));
		dateData.add(new Date(currentTimeInMillis + 86400000));
		dateData.add(new Date(currentTimeInMillis + oneWeekInMillis));
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater
				.inflate(R.layout.simple_spinner_item, parent, false);

		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

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
		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

		day.setText(days.get(position));

		// TODO How the **** did this do the trick???
		parent.getLayoutParams().width = day.getLayoutParams().width - 100;
		row.getLayoutParams().width = day.getLayoutParams().width - 100;

		return row;

	}

	public Date getData(int position) {
		return dateData.get(position);
	}

	public void addData(Date date) {
		dateData.add(date);
	}

}
