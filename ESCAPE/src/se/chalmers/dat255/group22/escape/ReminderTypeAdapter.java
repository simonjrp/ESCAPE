package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author tholene
 */
public class ReminderTypeAdapter extends ArrayAdapter<String> {

	private String[] strings;
	private int imgArr[];
	private Context context;

	public ReminderTypeAdapter(Context context, int textViewResourceId,
			String[] objects, int imgArr[]) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.strings = objects;
		this.imgArr = imgArr;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.type_spinner_item, parent, false);

		TextView label = (TextView) row.findViewById(R.id.spinnerText);

		label.setText(strings[position]);

		ImageView icon = (ImageView) row.findViewById(R.id.spinnerImage);

		icon.setImageResource(imgArr[position]);

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.type_spinner_item_single, parent,
				false);

		ImageView icon = (ImageView) row.findViewById(R.id.spinnerImage);

		icon.setImageResource(imgArr[position]);
		return row;
	}

}
