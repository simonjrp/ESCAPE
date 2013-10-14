package se.chalmers.dat255.group22.escape.adapters;

import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_ID;
import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_MSG;
import static se.chalmers.dat255.group22.escape.utils.Constants.INTENT_GET_ID;

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
 * to determine what ListObjects should be displayed
 * 
 * @author Carl Jansson
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
	 * Initialize the database and lists
	 */
	private void initialize() {
		dbHandler = new DBHandler(context);
		taskList = new ArrayList<ListObject>();
		theCategories = new ArrayList<Category>();
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
				for (Category cat : dbHandler.getCategories(lo))
					lo.addToCategory(cat);

				// TODO All categories should be fetched from database!
				// This category with the same name as the listobject is only
				// intended to test functionality!
				lo.addToCategory(new Category(lo.getName(), null, null));

				addListObject(lo);
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
				intent.putExtra(EDIT_TASK_MSG, bundle);

				bundle.putInt(INTENT_GET_ID, listObject.getId());
				intent.setFlags(EDIT_TASK_ID);
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
		if (!getLOShouldBeVisible(listObject))
			convertView.setVisibility(View.INVISIBLE);
		else
			convertView.setVisibility(View.VISIBLE);
		return convertView;
	}
	@Override
	public int getItemViewType(int i) {
		return i;
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
	 * list. All categories associated the the ListObject are added to the
	 * active categories
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObject(ListObject listObject) {
		if (!taskList.contains(listObject)) {
			taskList.add(listObject);
			addCategoryList(listObject.getCategories());
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a task from the displayed list. Also removes all associated
	 * Categories not associated with another listObject
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObject(ListObject listObject) {
		if (taskList.contains(listObject)) {
			taskList.remove(listObject);
			removeLoAssociatedCats(listObject);
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
	 * Add a category to the list with categories displayed by this adapter.
	 * 
	 * @param cat
	 *            the category to add
	 */
	public void addCategory(Category cat) {
		if (!theCategories.contains(cat))
			theCategories.add(cat);
	}

	/**
	 * add a list with categories to the category list. If a category from input
	 * list is already in the list it will not be added again.
	 * 
	 * @param catList
	 *            a list with new categories to add into the list
	 */
	public void addCategoryList(List<Category> catList) {
		for (Category cat : catList)
			addCategory(cat);
	}

	/**
	 * remove a category from the list with categories displayed by this
	 * adapter.
	 * 
	 * @param cat
	 *            the category to remove
	 */
	public void removeCategory(Category cat) {
		if (theCategories.contains(cat))
			theCategories.remove(cat);
	}

	/**
	 * Remove all categories associated with a ListObject if they are not part
	 * of another ListObject displayed in the list
	 * 
	 * @param listObject
	 *            the ListObject to check for categories to remove
	 */
	public void removeLoAssociatedCats(ListObject listObject) {
		for (Category cat : listObject.getCategories()) {
			boolean catIsInList = false;
			for (ListObject lo : this.taskList) {
				if (lo.getCategories().contains(cat)) {
					catIsInList = true;
					break;
				}
			}
			if (!catIsInList)
				removeCategory(cat);
		}
	}

	/**
	 * removes a list with categories from the list with categories displayed in
	 * by this adapter.
	 * 
	 * @param catList
	 *            List with categories that will be removed
	 */
	public void removeCategoryList(List<Category> catList) {
		for (Category cat : catList)
			removeCategory(cat);
	}

	/**
	 * Get a category list with all categories possible to display in this
	 * adapter
	 * 
	 * @return a list with all categories
	 */
	public List<Category> getTheCategories() {
		if (theCategories == null)
			theCategories = new ArrayList<Category>();
		return theCategories;
	}

	/**
	 * Get if a ListObject should be displayed.
	 * 
	 * @param lo
	 *            the list object to check if it should be displayed
	 * @return true if it should be displayed
	 */
	public boolean getLOShouldBeVisible(ListObject lo) {
		if (theCategories != null) {
			for (Category cat : lo.getCategories()) {
				if (!getCatShouldBeVisible(cat))
					return false;
			}
		}
		return true;
	}

	/**
	 * Check if a specific category should be displayed.
	 * 
	 * @param cat
	 *            the category to find out if it should be displayed
	 * @return true if it should be displayed
	 */
	public boolean getCatShouldBeVisible(Category cat) {
		for (Category cat2 : theCategories) {
			if (cat2.equals(cat))
				return cat2.getShouldBeDisplayed();
		}
		return true;
	}

	/**
	 * Replace existing category objects with equals from this list and add new
	 * ones
	 * 
	 * @param newCatList
	 *            a Category list containing new to display values
	 */
	public void addReplaceCategoryList(List<Category> newCatList) {
		// TODO is this method crap?
		for (Category cat : newCatList) {
			// This relies on categories being equal by having the same name!
			removeCategory(cat);
			addCategory(cat);
		}
	}
}
