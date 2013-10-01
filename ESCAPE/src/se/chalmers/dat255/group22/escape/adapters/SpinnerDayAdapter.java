package se.chalmers.dat255.group22.escape.adapters;

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
 * @author tholene
 */
public class SpinnerDayAdapter extends ArrayAdapter<String> {

	private String[] days;
	private Context context;

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
			String[] days) {
		super(context, textViewResourceId, days);
		this.context = context;
		this.days = days;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.day_spinner_item, parent, false);

		TextView day = (TextView) row.findViewById(R.id.spinnerDayText);

		day.setText(days[position]);
		// set gray color to "Pick a date" item
		if (position == 3) {
			day.setBackgroundResource(R.color.light_gray);

			View v = (View) day.getParent();
			v.setBackgroundResource(R.color.light_gray);
			v.setAlpha(0.75f);

			// TODO Add clickListener that opens dialog
		}

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.day_spinner_item_single, parent,
				false);
		TextView day = (TextView) row.findViewById(R.id.spinnerDayText);

		day.setText(days[position]);

		return row;

	}

}
