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
 * An adapter that customizes the way spinners for choosing a time are
 * presented.
 * 
 * @author tholene
 */
public class SpinnerTimeAdapter extends ArrayAdapter<String> {

	private ArrayList<String> times;
	private List<Date> timesData;
	private Context context;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different times.
	 * 
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 * @param times
	 *            a stringarray that contains the string to be set for each item
	 *            in the dropdown list.
	 */
	public SpinnerTimeAdapter(Context context, int textViewResourceId,
			ArrayList<String> times) {
		super(context, textViewResourceId, times);
		this.context = context;
		this.times = times;

		timesData = new ArrayList<Date>();
		Calendar tempCalendar = Calendar.getInstance();
		
		// Sets minutes and seconds of reference time to zero
		tempCalendar.set(Calendar.MINUTE, 0);
		tempCalendar.set(Calendar.SECOND, 0);
		tempCalendar.set(Calendar.MILLISECOND, 0);

		// Saves the standard time data relative to current day
		tempCalendar.set(Calendar.HOUR_OF_DAY, 9);
		timesData.add(new Date(tempCalendar.getTimeInMillis()));
		tempCalendar.set(Calendar.HOUR_OF_DAY, 13);
		timesData.add(new Date(tempCalendar.getTimeInMillis()));
		tempCalendar.set(Calendar.HOUR_OF_DAY, 17);
		timesData.add(new Date(tempCalendar.getTimeInMillis()));
		tempCalendar.set(Calendar.HOUR_OF_DAY, 20);
		timesData.add(new Date(tempCalendar.getTimeInMillis()));

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.time_spinner_item, parent, false);

		TextView timeAsText = (TextView) row
				.findViewById(R.id.spinnerTimeAsText);
		timeAsText.setText(times.get(position));

		TextView timeAsTime = (TextView) row
				.findViewById(R.id.spinnerTimeAsTime);

		timeAsTime.setVisibility(View.VISIBLE);

		if (timeAsText.getText().equals(context.getString(R.string.morning)))
			timeAsTime.setText("09:00");
		if (timeAsText.getText().equals(context.getString(R.string.afternoon)))
			timeAsTime.setText("13:00");
		if (timeAsText.getText().equals(context.getString(R.string.evening)))
			timeAsTime.setText("17:00");
		if (timeAsText.getText().equals(context.getString(R.string.night)))
			timeAsTime.setText("20:00");

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
				.findViewById(R.id.spinnerTimeAsText);

		timeAsText.setText(times.get(position));

		return row;
	}

	public void addData(Date date) {
		timesData.add(date);
	}

	public Date getData(int position) {
		return timesData.get(position);
	}

}
