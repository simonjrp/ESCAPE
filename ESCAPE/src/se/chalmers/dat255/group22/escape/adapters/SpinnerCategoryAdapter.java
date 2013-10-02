package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import se.chalmers.dat255.group22.escape.R;

/**
 * An adapter that customizes the way spinners for choosing a category are presented.
 * 
 * @author tholene
 */
public class SpinnerCategoryAdapter extends ArrayAdapter<String> {

	private ArrayList<String> categories;
	private Context context;

	/**
	 * Create a new Adapter. This one is suited for a spinner containing
	 * different categories.
	 *
	 * @param context
	 *            the context that is relevant, usually "this".
	 * @param textViewResourceId
	 *            the resource ID for the layout that each item in the dropdown
	 *            list will use.
	 * @param categories
	 *            a ArrayList that contains the string to be set for each item
	 *            in the dropdown list.
	 */
	public SpinnerCategoryAdapter(Context context, int textViewResourceId,
                                  ArrayList<String> categories) {
		super(context, textViewResourceId, categories);
		this.context = context;
		this.categories = categories;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.simple_spinner_item, parent, false);

		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

		day.setText(categories.get(position));
		// set gray color to "New category" item
		if (position == getCount()-1) {

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

		View row = inflater.inflate(R.layout.simple_spinner_item_single, parent,
				false);
		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

		day.setText(categories.get(position));

		return row;

	}

}
