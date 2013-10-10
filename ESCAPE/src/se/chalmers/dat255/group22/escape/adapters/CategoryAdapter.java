package se.chalmers.dat255.group22.escape.adapters;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.objects.Category;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * An adapter for use in a popup where user can choose what categories to
 * display in task or event list.
 * 
 * @author Carl
 */
public class CategoryAdapter implements ListAdapter {

	// The context this adapter is used in
	private Context context;
	// The tasks in the list
	private static List<Category> theCategories;
	// Array keeping track of changes in the list
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

	/**
	 * Create a new CategoryAdapter
	 * 
	 * @param context
	 *            the context this adapter is used in
	 */
	public CategoryAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Set the categories to display in the list
	 * 
	 * @param categories
	 *            list with categories to display
	 */
	public void setCategories(List<Category> categories) {
		this.theCategories = categories;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int i) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver) {
		observers.add(dataSetObserver);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
		observers.remove(dataSetObserver);
	}

	/**
	 * Call this to notify that something has changed. Makes the view update!
	 */
	public void notifyDataSetChanged() {
		for (DataSetObserver observer : observers) {
			observer.onChanged();
		}
	}

	@Override
	public int getCount() {
		return theCategories.size();
	}

	@Override
	public Object getItem(int i) {
		return theCategories.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		Category theCategory = (Category) getItem(position);

		// Create the view if it does not exist
		if (view == null) {
			CheckBox tmpBox = new CheckBox(context);
			// The buttons initial state
			tmpBox.setChecked(theCategory.getShouldBeDisplayed());
			// Set what to do when checkbox changes state
			tmpBox.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
					position));
			view = tmpBox;
		}
		CheckBox myBox = (CheckBox) view;
		myBox.setChecked(theCategory.getShouldBeDisplayed());
		myBox.setText(theCategory.getName() + "  "
				+ theCategory.getShouldBeDisplayed());
		view = myBox;

		return view;
	}

	@Override
	public int getItemViewType(int i) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return theCategories.isEmpty();
	}

	/**
	 * a listener for handling checkboxes with categories.
	 */
	private class CustomOnCheckedChangeListener
			implements
				CompoundButton.OnCheckedChangeListener {
		private int pos;

		/**
		 * Create a new custom on checked listener
		 * 
		 * @param i
		 *            the position of the category in the list
		 */
		public CustomOnCheckedChangeListener(int i) {
			this.pos = i;
		}
		@Override
		public void onCheckedChanged(CompoundButton compoundButton,
				boolean newButtonValue) {
			((Category) getItem(pos)).setShouldBeDisplayed(newButtonValue);

            //TODO why is pos 0 so weird?
			Toast.makeText(
					context,
					pos + ((Category) getItem(pos)).getName() + " "
							+ ((Category) getItem(pos)).getShouldBeDisplayed(),
					Toast.LENGTH_SHORT).show();

			compoundButton.setText(((Category) getItem(pos)).getName() + "  "
					+ ((Category) getItem(pos)).getShouldBeDisplayed());
		}
	}
}
