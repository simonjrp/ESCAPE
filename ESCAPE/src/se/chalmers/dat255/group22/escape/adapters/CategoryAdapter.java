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

/**
 * An adapter for displaying checkboxes with
 * {@link se.chalmers.dat255.group22.escape.objects.Category}. Checkboxes are
 * used to set the categories shouldBeDisplayed value.
 * 
 * @author Carl Jansson
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
		notifyDataSetChanged();
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
		for (DataSetObserver observer : observers)
			observer.onChanged();
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
			tmpBox.setText(theCategory.getName() + "  "
					+ theCategory.getShouldBeDisplayed());
			// Set what to do when checkbox changes state
			tmpBox.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(
					position));
			view = tmpBox;
		}
		return view;
	}

	@Override
	public int getItemViewType(int i) {
		return i;
	}

	@Override
	public int getViewTypeCount() {
		return theCategories.size();
	}

	@Override
	public boolean isEmpty() {
		return theCategories.isEmpty();
	}

	/**
	 * Get the list with categories with current values for if they should be
	 * displayed or not.
	 * 
	 * @return list with categories containing booleans for if they should be
	 *         displayed
	 */
	public List<Category> getTheCategories() {
		return theCategories;
	}

	/**
	 * a listener for handling checkboxes with categories.
	 */
	private class CustomOnCheckedChangeListener
			implements
				CompoundButton.OnCheckedChangeListener {

		// the listeners associated objects position
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
			compoundButton.setText(((Category) getItem(pos)).getName() + "  "
					+ ((Category) getItem(pos)).getShouldBeDisplayed());
		}
	}
}
