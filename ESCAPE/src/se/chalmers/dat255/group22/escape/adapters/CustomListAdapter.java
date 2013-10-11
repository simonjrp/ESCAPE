package se.chalmers.dat255.group22.escape.adapters;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.NewTaskActivity;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.listeners.CustomOnClickListener;
import se.chalmers.dat255.group22.escape.listeners.OptionTouchListener;
import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Adapter for displaying
 * {@link se.chalmers.dat255.group22.escape.objects.ListObject} in an ordinary
 * list. {@link se.chalmers.dat255.group22.escape.objects.Category} can be used
 * to determine what ListObjects to be displayed
 * 
 * @author Carl
 */
public class CustomListAdapter implements ListAdapter {

	// The context this adapter is used in
	private Context context;
	// The tasks in the list
	private List<ListObject> taskList;
	// the categories, including bool for if they should be displayed
	private List<Category> theCategories;
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
		// theCategories = new ArrayList<Category>();
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
				for (Category cat : dbHandler.getCategories(lo)) {
					addCategory(cat);
				}
			}
		}
		updateEditButtons();
	}

	/**
	 * update the edit/remove button
	 */
	protected void updateEditButtons() {
		try {
			TextView timeText = (TextView) ((MainActivity) context)
					.findViewById(R.id.startTimeTask);
			if (timeText != null)
				timeText.setVisibility(View.VISIBLE);

			ImageButton editButton = (ImageButton) ((MainActivity) context)
					.findViewById(R.id.editButton);
			if (editButton != null) {
				editButton.setVisibility(View.INVISIBLE);
				editButton.clearAnimation();
			}

			ImageButton deleteButton = (ImageButton) ((MainActivity) context)
					.findViewById(R.id.deleteButton);
			if (deleteButton != null) {
				deleteButton.setVisibility(View.INVISIBLE);
				deleteButton.clearAnimation();
			}
		} catch (RuntimeException e) {
			// Do nothing
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
		for (DataSetObserver observer : observers)
			observer.onChanged();
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
		updateEditButtons();
		final ListObject listObject = (ListObject) getItem(childPosition);

		final String childText = listObject.getName();

		// final Time childTime = listObject.getTime();
		String childTimeText = "";
		if (dbHandler.getTime(listObject) != null) {
			final Date childStartDate = dbHandler.getTime(listObject)
					.getStartDate();
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
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, NewTaskActivity.class);

				Bundle bundle = new Bundle();
				intent.putExtra("Edit Task", bundle);

				bundle.putInt("ID", listObject.getId());
				intent.setFlags(1);
				context.startActivity(intent);
			}
		});
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DBHandler dbh = new DBHandler(context);
				dbh.deleteListObject(listObject);
				removeListObject(listObject);

				// v.refreshDrawableState();
			}

		});

		// TODO tasks don't have time!
		childLabel.setText(childText);
		childTimeView.setText(childTimeText.equals("") ? "" : childTimeText);
		// Get a textview for the object's data
		TextView childData = (TextView) convertView.findViewById(R.id.taskData);

		// We don't want the data to show yet...
		childData.setVisibility(View.INVISIBLE);
		childData.setHeight(0);

		childLabel.setText(childText);

		CustomOnClickListener clickListener = new CustomOnClickListener(
				listObject, childLabel, childData);
		convertView.setOnClickListener(clickListener);
		// TODO We add two listeners since it wont work on one if the other is
		// added to
		// Adding touchlisteners
		convertView.setOnTouchListener(new OptionTouchListener(context,
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

	/**
	 * Add a category to the category list of categories to be displyed
	 * 
	 * @param cat
	 *            the category to add
	 */
	public void addCategory(Category cat) {
		if (!theCategories.contains(cat))
			theCategories.add(cat);
	}

	/**
	 * remove a category from the category list of categories to be displayed
	 * 
	 * @param cat
	 *            the category to remove
	 */
	public void removeCategory(Category cat) {
		if (theCategories.contains(cat))
			theCategories.remove(cat);
	}

	/**
	 * Get a category list with all categories possible to display in this
	 * adapter
	 * 
	 * @return a list with all categories
	 */
	public List<Category> getTheCategories() {
		if (theCategories == null) {
			List<Category> tmpList = new ArrayList<Category>();
			tmpList.add(new Category("tmp0", null, null));
			tmpList.add(new Category("tmp1", null, null));
			tmpList.add(new Category("tmp2", null, null));
			tmpList.add(new Category("tmp3", null, null));
			tmpList.add(new Category("tmp4", null, null));
			tmpList.add(new Category("tmp5", null, null));
			tmpList.add(new Category("tmp6", null, null));
			tmpList.add(new Category("tmp7", null, null));
			tmpList.add(new Category("tmp8", null, null));
			tmpList.add(new Category("tmp9", null, null));
			tmpList.add(new Category("tmp10", null, null));
			tmpList.add(new Category("tmp11", null, null));
			tmpList.add(new Category("tmp12", null, null));
			tmpList.add(new Category("tmp13", null, null));
			tmpList.add(new Category("tmp14", null, null));
			tmpList.add(new Category("tmp15", null, null));
			tmpList.add(new Category("tmp16", null, null));
			tmpList.add(new Category("tmp17", null, null));
			tmpList.add(new Category("tmp18", null, null));
			tmpList.add(new Category("tmp19", null, null));
			theCategories = tmpList;
		}
		addCategory(new Category("Hey", null, null));
		// theCategories.add(new Category("Hey", null, null));
		return theCategories;
	}

	/**
	 * Replace existing category objects with equals from this list and add new
	 * ones
	 * 
	 * @param newCatList
	 *            a Category list containing new to display values
	 */
	public void addReplaceCategoryList(List<Category> newCatList) {
		for (Category cat : newCatList) {
			// This relies on categories being equal by having the same name!
			removeCategory(cat);
			addCategory(cat);
		}
	}
}
