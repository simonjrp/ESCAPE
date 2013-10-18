package se.chalmers.dat255.group22.escape.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.Category;

/**
 * An adapter that customizes the way spinners for choosing a category are
 * presented.
 * 
 * @author tholene
 */
public class SpinnerCategoryAdapter extends ArrayAdapter<String> {

	private ArrayList<String> categories;
	private Context context;
    private DBHandler dbHandler;

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
        this.dbHandler = new DBHandler(context);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater
				.inflate(R.layout.simple_spinner_item, parent, false);

		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

		day.setText(categories.get(position));
		// set gray color to "New category" item

		if (position == getCount() - 1) {
			row.setBackgroundResource(R.drawable.spinner_button_colors);
		} else {
            Category category = dbHandler.getCategory(categories.get(position));
            row.setBackgroundColor(Color.parseColor("#" + category.getBaseColor()));
        }

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);

		View row = inflater.inflate(R.layout.simple_spinner_item_single,
				parent, false);
        View ribbon = row.findViewById(R.id.category_spinner_ribbon);
        Category category = dbHandler.getCategory(categories.get(position));
        ribbon.setBackgroundColor(Color.parseColor("#" + category.getBaseColor()));
        ribbon.setVisibility(View.VISIBLE);
		TextView day = (TextView) row.findViewById(R.id.simpleSpinnerText);

		day.setText(categories.get(position));

		return row;

	}

}
