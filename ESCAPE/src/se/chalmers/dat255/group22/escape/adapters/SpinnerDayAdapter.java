package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import se.chalmers.dat255.group22.escape.R;

/**
 * @author tholene
 */
public class SpinnerDayAdapter extends ArrayAdapter<String> {

	private String[] strings;
	private Context context;

	public SpinnerDayAdapter(Context context, int textViewResourceId,
                             String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.strings = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.day_spinner_item, parent, false);

		TextView day = (TextView) row.findViewById(R.id.spinnerDayText);

        day.setText(strings[position]);

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.day_spinner_item_single, parent,
				false);
        TextView day = (TextView) row.findViewById(R.id.spinnerDayText);

        day.setText(strings[position]);
        return row;

	}

}
