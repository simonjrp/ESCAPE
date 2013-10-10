package se.chalmers.dat255.group22.escape.adapters;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.objects.Category;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;

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
	private List<Category> theCategories;
	// Array keeping track of changes in the list
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

    /**
     * Create a new CategoryAdapter
     * @param context the context this adapter is used in
     */
	public CategoryAdapter(Context context) {
		this.context = context;
	}

    /**
     * Set the categories to display in the list
     * @param categories list with categories to display
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
		return false;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

        final Category theCategory = (Category) getItem(position);

        // Create the view if it does not exist
        if (view == null) {
            CheckBox tmpBox = new CheckBox(context);
            // The buttons initial state
            tmpBox.setChecked(theCategory.getBaseColor() != null);
            // Set what to do when checkbox changes state
            tmpBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if ( b == true ) {
                        theCategory.setBaseColor("Hey");
                        compoundButton.setText(theCategory.getName() + "  " + theCategory.getBaseColor());
                    } else {
                        theCategory.setBaseColor(null);
                        compoundButton.setText(theCategory.getName() + "  " + theCategory.getBaseColor());
                    }
                }
            });
            view = tmpBox;
        }
        CheckBox myBox = (CheckBox)view;
        myBox.setChecked(theCategory.getBaseColor()!=null);
        myBox.setText(theCategory.getName() + "  " + theCategory.getBaseColor());
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
}
