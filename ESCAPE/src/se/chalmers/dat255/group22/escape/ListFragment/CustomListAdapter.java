package se.chalmers.dat255.group22.escape.ListFragment;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Adapter for displaying ListObjects in an ordinary list
 * 
 * @author Carl
 */
public class CustomListAdapter implements ListAdapter {

	// The context this adapter is used in
	private Context context;
	// The tasks in the list
	private List<ListObject> ourTaskList;
    // Array keeping track of changes in the list
    private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

	/**
	 * Creates a new CustomListAdapter
	 * 
	 * @param context
	 *            The context (activity) this adapter is used in
	 * @param taskList
	 *            list with the tasks to display
	 */
	public CustomListAdapter(Context context, List<ListObject> taskList) {
		this.context = context;
		this.ourTaskList = taskList;
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
     * Call this method after changing data in the list
     */
    public void notifyDataSetChanged(){
        for (DataSetObserver observer: observers) {
            observer.onChanged();
        }
    }

	@Override
	public int getCount() {
		return ourTaskList.size();
	}

	@Override
	public Object getItem(int i) {
		return ourTaskList.get(i);
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
	public View getView(int i, View view, ViewGroup viewGroup) {

		final ListObject listObject = (ListObject) getItem(i);

		final String childText = listObject.getName();

		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_task, null);
		}

		// Get a textview for the object
		TextView childLabel = (TextView) view.findViewById(R.id.listTask);

		// Get a textview for the object's data
		TextView childData = (TextView) view.findViewById(R.id.taskData);

		// We don't want the data to show yet...
		childData.setVisibility(View.INVISIBLE);
		childData.setHeight(0);

		childLabel.setText(childText);

		CustomOnClickListener clickListener = new CustomOnClickListener(
				listObject, childLabel, childData);
		childLabel.setOnClickListener(clickListener);

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
		return ourTaskList.isEmpty();
	}
}
