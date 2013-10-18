package se.chalmers.dat255.group22.escape.adapters;

import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_ID;
import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_MSG;
import static se.chalmers.dat255.group22.escape.utils.Constants.INTENT_GET_ID;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
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
	 * Creates a new CustomListAdapter used to display tasks
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
		List<ListObject> databaseList = dbHandler.getAllListObjects();

		for (ListObject lo : databaseList) {
			// we only want ListObjects without a specific time in this list!
			if (dbHandler.getTime(lo) == null) {

				// These variables must be set on the object since they are used
				// when sorting the list objects and choosing what to display.
				for (Category cat : dbHandler.getCategories(lo))
					lo.addToCategory(cat);

				addListObject(lo);
			}
		}

		resetEditButtons();
	}

	/**
	 * If edit and delete buttons initialized this method will make them
	 * invisible
	 */
	protected void resetEditButtons() {
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
		resetEditButtons();
		final ListObject listObject = (ListObject) getItem(childPosition);
		final int nextChild = childPosition;
		final String childText = listObject.getName();
		final View thisView = convertView;
		final ViewGroup thisViewGroup = parent;

		if (getLOShouldBeVisible(listObject)) {

			String childTimeText = "";
			if (dbHandler.getTime(listObject) != null) {
				final Date childStartDate = dbHandler.getTime(listObject)
						.getStartDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm",
						Locale.getDefault());
				childTimeText = dateFormat.format(childStartDate);
			}

			if (convertView == null || !convertView.isShown()) {
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

			// editButton.setX(convertView.getRight() + deleteButton.getWidth()
			// +
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
					dbh.purgeListObject(listObject);
					removeListObject(listObject);
					LinearLayout nextObject = null;
					try {
						nextObject = (LinearLayout) getView(nextChild,
								thisView, thisViewGroup);
						nextObject.refreshDrawableState();
						nextObject.postInvalidate();

					} catch (NullPointerException e) {
						// Do nothing
					} catch (IndexOutOfBoundsException e) {
						// Do nothing
					}

					// v.refreshDrawableState();
				}

			});

			// TODO tasks don't have time!
			childLabel.setText(childText);
			childTimeView
					.setText(childTimeText.equals("") ? "" : childTimeText);
			// Get the layout for the object's data
			RelativeLayout childData = (RelativeLayout) convertView
					.findViewById(R.id.taskDataLayout);

			// We don't want the data to show yet...
			childData.setVisibility(View.GONE);

			childLabel.setText(childText);

			CustomOnClickListener clickListener = new CustomOnClickListener(
					context, listObject, childLabel, childData);
			convertView.setOnClickListener(clickListener);

			// TODO We add two listeners since it wont work on one if the other
			// is
			// added to
			// Adding touchlisteners
			convertView.setOnTouchListener(new OptionTouchListener(context,
					convertView));
			// convertView.setOnTouchListener(new OptionTouchListener(context,
			// convertView));
			// Set the state colors of the view
			ColorDrawable ribbonColor = new ColorDrawable();

			if (listObject.isImportant())
				ribbonColor.setColor(Color.parseColor("#"
						+ dbHandler.getCategories(listObject).get(0)
								.getImportantColor()));
			else
				ribbonColor.setColor(Color.parseColor("#"
						+ dbHandler.getCategories(listObject).get(0)
								.getBaseColor()));

			LinearLayout ribbonView = (LinearLayout) convertView
					.findViewById(R.id.task_ribbons);
			View ribbon = new View(convertView.getContext());
			ribbon.setLayoutParams(new LinearLayout.LayoutParams((int) context
					.getResources().getDimension(R.dimen.ribbon_width),
					LinearLayout.LayoutParams.MATCH_PARENT));
			ribbon.setBackgroundDrawable(ribbonColor);
			ribbonView.addView(ribbon);

			ColorDrawable baseColor = new ColorDrawable();
			baseColor.setColor(context.getResources().getColor(R.color.white));
			StateListDrawable states = new StateListDrawable();
			states.addState(
					new int[] { android.R.attr.state_pressed },
					context.getResources().getDrawable(
							R.drawable.list_pressed_holo_dark));
			states.addState(StateSet.WILD_CARD, baseColor);
			convertView.setBackgroundDrawable(states);

			if (!getLOShouldBeVisible(listObject))
				convertView.setVisibility(View.VISIBLE);
		} else {
			// TODO is there a way to fix visibility without this?
			convertView = new View(context);
			convertView.setVisibility(View.INVISIBLE);
		}
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
