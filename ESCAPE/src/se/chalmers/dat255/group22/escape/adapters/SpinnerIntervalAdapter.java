package se.chalmers.dat255.group22.escape.adapters;

import se.chalmers.dat255.group22.escape.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * An adapter that customizes the way spinners for choosing an interval are
 * presented.
 * 
 * @author tholene
 */
public class SpinnerIntervalAdapter extends ArrayAdapter<String> {

	private String[] intervals;
	private Context context;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different intervals.
	 * 
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 * @param intervals
	 *            a stringarray that contains the string to be set for each item
	 *            in the dropdown list.
	 */
	public SpinnerIntervalAdapter(Context context, int textViewResourceId,
			String[] intervals) {
		super(context, textViewResourceId, intervals);
		this.context = context;
		this.intervals = intervals;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.simple_spinner_item, parent, false);

		TextView interval = (TextView) row.findViewById(R.id.simpleSpinnerText);

		interval.setText(intervals[position]);

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.simple_spinner_item_single, parent,
				false);
		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

		day.setText(intervals[position]);
		return row;

	}

}
