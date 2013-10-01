package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import se.chalmers.dat255.group22.escape.R;

/**
 * An adapter that customizes the way spinners for choosing a time are presented.
 *
 * @author tholene
 */
public class SpinnerTimeAdapter extends ArrayAdapter<String> {

	private String[] times;
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
			String[] times) {
		super(context, textViewResourceId, times);
		this.context = context;
		this.times = times;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.time_spinner_item, parent, false);

		TextView timeAsText = (TextView) row
				.findViewById(R.id.spinnerTimeAsText);
		timeAsText.setText(times[position]);

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

		// set gray color to "Pick a time" item
		if (position == 4) {
			timeAsText.setBackgroundResource(R.color.light_gray);
			timeAsTime.setBackgroundResource(R.color.light_gray);

            View v = (View)timeAsText.getParent();
            v.setBackgroundResource(R.color.light_gray);
            v.setAlpha(0.75f);

            // TODO Add clickListener that opens dialog
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

		timeAsText.setText(times[position]);

		return row;
	}

}
