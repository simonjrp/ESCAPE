package se.chalmers.dat255.group22.escape.adapters;

import se.chalmers.dat255.group22.escape.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * An adapter that customizes the way spinners for choosing differnet types of
 * reminders are presented.
 * 
 * @author tholene
 */
public class SpinnerTypeAdapter extends ArrayAdapter<String> {

	private String[] types;
	private int imgArr[];
	private Context context;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different types of reminders.
	 * 
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 * @param types
	 *            a stringarray that contains the string to be set for each item
	 *            in the dropdown list.
	 * 
	 * @param imgArr
	 *            an array containing the images to be associated with the
	 *            strings in the previous array.
	 */
	public SpinnerTypeAdapter(Context context, int textViewResourceId,
			String[] types, int imgArr[]) {
		super(context, textViewResourceId, types);
		this.context = context;
		this.types = types;
		this.imgArr = imgArr;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.type_spinner_item, parent, false);

		TextView label = (TextView) row.findViewById(R.id.spinnerTypeText);

		label.setText(types[position]);

		ImageView icon = (ImageView) row.findViewById(R.id.spinnerImage);

		icon.setImageResource(imgArr[position]);

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.type_spinner_item_single, parent,
				false);

        Spinner currentSpinner = (Spinner) parent.findViewById(R.id.reminderTypeSpinner);


		ImageView icon = (ImageView) row.findViewById(R.id.spinnerImage);

		icon.setImageResource(imgArr[position]);
        currentSpinner.getLayoutParams().width = currentSpinner.getLayoutParams().WRAP_CONTENT;
        currentSpinner.getLayoutParams().height = currentSpinner.getLayoutParams().WRAP_CONTENT;
		return row;
	}

}
