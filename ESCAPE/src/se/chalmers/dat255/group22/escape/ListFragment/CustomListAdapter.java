package se.chalmers.dat255.group22.escape.ListFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.OptionTouchListener;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import android.content.Context;
import android.database.DataSetObserver;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
	private List<ListObject> taskList;
	// Array keeping track of changes in the list
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	// The database
	private DBHandler dbHandler;

	/**
	 * Creates a new CustomListAdapter
	 * 
	 * @param context
	 *            The context (activity) this adapter is used in
	 */
	public CustomListAdapter(Context context) {
		this.context = context;
		initialize();
	}

	/**
	 * Initialize the database and list
	 */
	private void initialize() {
		dbHandler = new DBHandler(context);
		taskList = new ArrayList<ListObject>();
	}

	/**
	 * Read from database and add all saved ListObjects that doesn't have a
	 * specific time
	 */
	public void reInit() {
		// Fetch tasks from database
		List<ListObject> listObjects = dbHandler.getAllListObjects();
		for (ListObject lo : listObjects) {
			// we only want ListObjects without a specific time in this list!
			if (dbHandler.getTime(lo) == null) {
				addListObject(lo);
			}
		}
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
		return taskList.size();
	}

	@Override
	public Object getItem(int i) {
		return taskList.get(i);
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
	public View getView(int childPosition, View convertView, ViewGroup parent) {
		// Get the name of the task to display for each task entry

		final ListObject listObject = (ListObject) getItem(childPosition);

		final String childText = listObject.getName();

		final Time childTime = listObject.getTime();
		String childTimeText = "";
		if (childTime != null) {
			final Date childStartDate = childTime.getStartDate();
			childTimeText = DateFormat.format("HH:mm", childStartDate)
					.toString();
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		// Get a textview for the object
		final TextView childLabel = (TextView) convertView
				.findViewById(R.id.listTask);

		final TextView childTimeView = (TextView) convertView
				.findViewById(R.id.startTimeTask);

		final ImageButton editButton = (ImageButton) convertView
				.findViewById(R.id.editButton);

		final ImageButton deleteButton = (ImageButton) convertView
				.findViewById(R.id.deleteButton);

		editButton.setVisibility(View.INVISIBLE);
		deleteButton.setVisibility(View.INVISIBLE);

		// editButton.setX(convertView.getRight() + deleteButton.getWidth() +
		// 300);
		// deleteButton.setX(convertView.getRight() + 300);

		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DBHandler dbh = new DBHandler(context);
				dbh.deleteListObject(listObject);
				removeListObject(listObject);

				// v.refreshDrawableState();
			}

		});

		childLabel.setText(childText);
		childTimeView.setText(childTimeText.equals("") ? "10:00"
				: childTimeText);
		// Get a textview for the object's data
		TextView childData = (TextView) convertView.findViewById(R.id.taskData);

		// We don't want the data to show yet...
		childData.setVisibility(View.INVISIBLE);
		childData.setHeight(0);

		childLabel.setText(childText);

		CustomOnClickListener clickListener = new CustomOnClickListener(
				listObject, childLabel, childData);
		childLabel.setOnClickListener(clickListener);
		// TODO We add two listeners since it wont work on one if the other is
		// added to
		// Adding touchlisteners
		childLabel.setOnTouchListener(new OptionTouchListener(context,
				convertView));
		// convertView.setOnTouchListener(new OptionTouchListener(context,
		// convertView));

		return convertView;
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
		return taskList.isEmpty();
	}

	/**
	 * Add a new task for the displayed list if the task is not already in the
	 * list
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObject(ListObject listObject) {
		if (!taskList.contains(listObject)) {
			taskList.add(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a task from the displayed list
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObject(ListObject listObject) {
		if (taskList.contains(listObject)) {
			taskList.remove(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Get a list object from the displayed list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObject(int i) {
		if (0 <= i && i < taskList.size()) {
			return taskList.get(i);
		}
		return null;
	}
}
