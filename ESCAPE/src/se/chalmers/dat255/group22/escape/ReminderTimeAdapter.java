package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author tholene
 */
public class ReminderTimeAdapter extends ArrayAdapter<String> {

	private String[] strings;
	private Context context;

	public ReminderTimeAdapter(Context context, int textViewResourceId,
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

		if (timeAsText.getText().equals("Morning"))
			timeAsTime.setText("09:00");
		if (timeAsText.getText().equals("Afternoon"))
			timeAsTime.setText("13:00");
		if (timeAsText.getText().equals("Evening"))
			timeAsTime.setText("17:00");
		if (timeAsText.getText().equals("Night"))
			timeAsTime.setText("20:00");
		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View row = inflater.inflate(R.layout.time_spinner_item, parent, false);

        TextView timeAsText = (TextView) row
                .findViewById(R.id.spinnerTimeAsText);
        TextView timeAsTime = (TextView) row
                .findViewById(R.id.spinnerTimeAsTime);
        timeAsTime.setText("");
        return row;
	}

}
