package se.chalmers.dat255.group22.escape.adapters;

import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_ID;
import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_MSG;
import static se.chalmers.dat255.group22.escape.utils.Constants.INTENT_GET_ID;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.NewTaskActivity;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.listeners.CustomOnClickListener;
import se.chalmers.dat255.group22.escape.listeners.OptionTouchListener;
import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.utils.CheckDateUtils;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An ExpandableListAdapter that makes use of a
 * {@link se.chalmers.dat255.group22.escape.objects.ListObject}.<br>
 * It also simulates a three level expandable listview by giving each child its
 * own {@link android.view.View.OnClickListener}.
 * {@link se.chalmers.dat255.group22.escape.objects.Category} can be used to
 * determine what ListObjects should be displayed
 * 
 * @author tholene, Carl
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

	// Keeps track of changes
	private final DataSetObservable dataSetObservable = new DataSetObservable();
	// The lists
	List<ListObject> todayEventList;
	List<ListObject> tomorrowEventList;
	List<ListObject> thisWeekEventList;
	List<ListObject> somedayEventList;
	// The context this is used in
	private Context context;
	// header titles
	private List<String> headerList;
	// child data in format of header title, data container (ListObject)
	private HashMap<String, List<ListObject>> objectDataMap;
	// the categories, including bool for if they should be displayed
	private List<Category> theCategories;
	// The database
	private DBHandler dbHandler;
	// A temporary task that is displayed if list is empty
	ListObject emptyListDefaultTask;

	/**
	 * Create a new custom expandable list adapter used to display events
	 * 
	 * @param context
	 *            The context to make use of
	 */
	public CustomExpandableListAdapter(Context context) {
		this.context = context;
		initialize();
	}

	/**
	 * Initialize database, lists, headers and hashmap
	 */
	private void initialize() {

		// Initiate the database
		dbHandler = new DBHandler(context);

		objectDataMap = new HashMap<String, List<ListObject>>();
		todayEventList = new ArrayList<ListObject>();
		tomorrowEventList = new ArrayList<ListObject>();
		thisWeekEventList = new ArrayList<ListObject>();
		somedayEventList = new ArrayList<ListObject>();
		headerList = new ArrayList<String>();

		headerList.add(context.getResources().getString(R.string.today_label));
		headerList.add(context.getResources()
				.getString(R.string.tomorrow_label));
		headerList.add(context.getResources()
				.getString(R.string.thisweek_label));

		headerList
				.add(context.getResources().getString(R.string.someday_label));

		objectDataMap.put(context.getResources()
				.getString(R.string.today_label), todayEventList);
		objectDataMap.put(
				context.getResources().getString(R.string.tomorrow_label),
				tomorrowEventList);
		objectDataMap.put(
				context.getResources().getString(R.string.thisweek_label),
				thisWeekEventList);
		objectDataMap.put(
				context.getResources().getString(R.string.someday_label),
				somedayEventList);

		theCategories = new ArrayList<Category>();

		emptyListDefaultTask = new ListObject(97569754,
				"Anything you need to do?");
		emptyListDefaultTask
				.setComment("In this list you can add events, tasks with a set time!");
		emptyListDefaultTask.setTime(new Time(1, new Date(System
				.currentTimeMillis()), new Date(
				System.currentTimeMillis() + 1000 * 60 * 60)));
	}

	/**
	 * Fetches all objects from database and adds all events to what is
	 * displayed.
	 */
	public void reInit() {
		List<ListObject> listObjects = dbHandler.getAllListObjects();
		boolean noEvents = true;

		for (ListObject lo : listObjects) {
			// we only want evens in this fragment (objects with a set time)
			if (dbHandler.getTime(lo) != null) {

				// These variables must be set on the object since they are used
				// when sorting the list objects and choosing what to display.
				lo.setTime(dbHandler.getTime(lo));
				for (Category cat : dbHandler.getCategories(lo))
					lo.addToCategory(cat);

				addListObject(lo);
				noEvents = false;
			}
		}
		// If list is empty add a default event
		if (noEvents)
			addListObjectToday(emptyListDefaultTask);
		else
			removeListObjectToday(emptyListDefaultTask);

		MainActivity mActivity = (MainActivity) context;
		ExpandableListView expLv = (ExpandableListView) mActivity
				.findViewById(R.id.expEventList);

		// First expandable group is always expanded when adapter reinits
		expLv.expandGroup(0, true);
	}

	/**
	 * If edit and delete buttons initialized this method will make them
	 * invisible
	 */
	protected void resetEditButtons() {
		try {
			RelativeLayout layout = (RelativeLayout) ((MainActivity) context)
					.findViewById(R.id.listTask);
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

		} catch (Exception e) {
			// Do nothing. Only used in order to make testing possible
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.objectDataMap.get(this.headerList.get(groupPosition)).get(
				childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final ListObject listObject = ((ListObject) getChild(groupPosition,
				childPosition));

		if (getLOShouldBeVisible(listObject)) {

			// Get the name of the task to display for each task entry
			final String childText = listObject.getName();

			// Get the time if it exists
			String childTimeText = "";
			if (dbHandler.getTime(listObject) != null) {
				final Date childStartDate = dbHandler.getTime(listObject)
						.getStartDate();
				childTimeText = DateFormat.format("hh:mm", childStartDate)
						.toString();
			}

			if (convertView == null || !convertView.isShown()) {
				LayoutInflater infalInflater = (LayoutInflater) this.context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_task, null);
			}
			// Get a textview for the object
			TextView childLabel = (TextView) convertView
					.findViewById(R.id.listTask);

			TextView childTimeView = (TextView) convertView
					.findViewById(R.id.startTimeTask);

			ImageButton editButton = (ImageButton) convertView
					.findViewById(R.id.editButton);

			ImageButton deleteButton = (ImageButton) convertView
					.findViewById(R.id.deleteButton);

			resetEditButtons();

			// OnClickListener for sending an intent with the ID of the
			// listObject
			// that was clicked
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
				}

			});

			childLabel.setText(childText);
			childTimeView.setText(childTimeText.equals("")
					? "no start time"
					: childTimeText);
			// Get a textview for the object's data
			TextView childData = (TextView) convertView
					.findViewById(R.id.taskData);

			// We don't want the data to show yet...
			childData.setVisibility(View.INVISIBLE);
			childData.setHeight(0);

			childLabel.setText(childText);

			// Custom listener for showing/hiding data relevant to the
			// listObject
			CustomOnClickListener clickListener = new CustomOnClickListener(
					listObject, childLabel, childData);
			convertView.setOnClickListener(clickListener);

			// We add two listeners since it wont work on one if the other is
			// added
			// too

			// Adding touchlisteners
			convertView.setOnTouchListener(new OptionTouchListener(context,
					convertView));
			convertView.setVisibility(View.VISIBLE);
		} else {
			// TODO is there a way to fix visibility without this?
			convertView = new View(context);
			convertView.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.objectDataMap.get(this.headerList.get(groupPosition))
				.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.headerList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.headerList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		resetEditButtons();
		// Get the name of the header to display for each entry
		String headerTitle = (String) getGroup(groupPosition);

		LayoutInflater infalInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = infalInflater.inflate(R.layout.list_header, null);

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.listHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return todayEventList.isEmpty() && tomorrowEventList.isEmpty()
				&& thisWeekEventList.isEmpty() && somedayEventList.isEmpty();
	}

	@Override
	public void notifyDataSetInvalidated() {
		this.dataSetObservable.notifyInvalidated();
	}

	/**
	 * Call this to notify that something has changed. Makes the view update!
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged() {
		this.dataSetObservable.notifyChanged();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		this.dataSetObservable.registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		this.dataSetObservable.unregisterObserver(observer);
	}

	/**
	 * Add a list object to the expandable list. Object should automatically be
	 * placed where it should be. If the list object does not contain a start
	 * date it will not be added into the list.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObject(ListObject listObject) {
		// Get a calendar with current system time
		Time theTime = listObject.getTime();
		if (theTime != null) {
			// Get start date
			Date theDate = theTime.getStartDate();
			// Add into the relevant list
			if (CheckDateUtils.isToday(theDate)) {
				addListObjectToday(listObject);
			} else if (CheckDateUtils.isTomorrow(theDate)) {
				addListObjectTomorrow(listObject);
			} else if (CheckDateUtils.isThisWeek(theDate)) {
				addListObjectThisWeek(listObject);
			} else {
				addListObjectSomeday(listObject);
			}
		}
	}

	/**
	 * Add a new event for today if the task is not already in the list
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectToday(ListObject listObject) {
		if (!todayEventList.contains(listObject)) {
			todayEventList.add(listObject);
			addCategoryList(listObject.getCategories());
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Add a new event for tomorrow if the task is not already in the list
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectTomorrow(ListObject listObject) {
		if (!tomorrowEventList.contains(listObject)) {
			tomorrowEventList.add(listObject);
			addCategoryList(listObject.getCategories());
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Add a new event for this week. Not that this is the current week and not
	 * 7 days ahead!
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectThisWeek(ListObject listObject) {
		if (!thisWeekEventList.contains(listObject)) {
			thisWeekEventList.add(listObject);
			addCategoryList(listObject.getCategories());
			this.notifyDataSetChanged();
		}

	}

	/**
	 * Add a new event for someday if the task is not already in the list
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectSomeday(ListObject listObject) {
		if (!somedayEventList.contains(listObject)) {
			somedayEventList.add(listObject);
			addCategoryList(listObject.getCategories());
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Removes a listobject from the event list
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObject(ListObject listObject) {
		removeListObjectToday(listObject);
		removeListObjectTomorrow(listObject);
		removeListObjectThisWeek(listObject);
		removeListObjectSomeday(listObject);
	}

	/**
	 * Remove a event from today.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectToday(ListObject listObject) {
		if (todayEventList.contains(listObject)) {
			todayEventList.remove(listObject);
			removeLoAssociatedCats(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a event from tomorrow.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectTomorrow(ListObject listObject) {
		if (tomorrowEventList.contains(listObject)) {
			tomorrowEventList.remove(listObject);
			removeLoAssociatedCats(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a event from this week.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectThisWeek(ListObject listObject) {
		if (thisWeekEventList.contains(listObject)) {
			thisWeekEventList.remove(listObject);
			removeLoAssociatedCats(listObject);
			this.notifyDataSetChanged();
		}

	}

	/**
	 * Remove a event from someday.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectSomeday(ListObject listObject) {
		if (somedayEventList.contains(listObject)) {
			somedayEventList.remove(listObject);
			removeLoAssociatedCats(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Get a list object from the today list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObjectToday(int i) {
		if (0 <= i && i < todayEventList.size()) {
			return todayEventList.get(i);
		}
		return null;
	}

	/**
	 * Get a list object from the tomorrow list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObjectTomorrow(int i) {
		if (0 <= i && i < tomorrowEventList.size()) {
			return tomorrowEventList.get(i);
		}
		return null;
	}

	/**
	 * Get a list object from the this week list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObjectThisWeek(int i) {
		if (0 <= i && i < thisWeekEventList.size()) {
			return thisWeekEventList.get(i);
		}
		return null;

	}

	/**
	 * Get a list object from the this week list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObjectSomeday(int i) {
		if (0 <= i && i < somedayEventList.size()) {
			return somedayEventList.get(i);
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
	 * Remove all categories associated with a ListObject from the list with
	 * categories displayed by this adapter if they are not part of another
	 * ListObject displayed in the list
	 * 
	 * @param listObject
	 *            the ListObject to check for categories to remove
	 */
	public void removeLoAssociatedCats(ListObject listObject) {
		for (Category cat : listObject.getCategories()) {

			boolean catIsInList = false;

			for (ListObject lo : this.todayEventList) {
				if (lo.getCategories().contains(cat)) {
					catIsInList = true;
					break;
				}
			}
			if (!catIsInList) {
				for (ListObject lo : this.tomorrowEventList) {
					if (lo.getCategories().contains(cat)) {
						catIsInList = true;
						break;
					}
				}
			}
			if (!catIsInList) {
				for (ListObject lo : this.thisWeekEventList) {
					if (lo.getCategories().contains(cat)) {
						catIsInList = true;
						break;
					}
				}
			}
			if (!catIsInList) {
				for (ListObject lo : this.somedayEventList) {
					if (lo.getCategories().contains(cat)) {
						catIsInList = true;
						break;
					}
				}
			}
			if (!catIsInList)
				removeCategory(cat);
		}
	}

	/**
	 * removes a list with categories from the list with categories displayed by
	 * this adapter.
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
