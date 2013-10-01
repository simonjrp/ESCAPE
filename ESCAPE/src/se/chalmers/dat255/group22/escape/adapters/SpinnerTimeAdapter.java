package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import se.chalmers.dat255.group22.escape.R;

/**
 * @author tholene
 */
public class SpinnerTimeAdapter extends ArrayAdapter<String> {

	private String[] strings;
	private Context context;

	public SpinnerTimeAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.strings = objects;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.time_spinner_item, parent, false);

		TextView timeAsText = (TextView) row
				.findViewById(R.id.spinnerTimeAsText);
		timeAsText.setText(strings[position]);

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

		// set gray color to "Pick a date" item
		if (position == 4) {
			timeAsText.setBackgroundResource(R.color.light_gray);
			timeAsTime.setBackgroundResource(R.color.light_gray);
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

		timeAsText.setText(strings[position]);

		return row;
	}

}
