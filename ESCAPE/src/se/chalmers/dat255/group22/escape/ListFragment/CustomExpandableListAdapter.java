package se.chalmers.dat255.group22.escape.ListFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.OptionTouchListener;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * An ExpandableListAdapter that makes use of a
 * {@link se.chalmers.dat255.group22.escape.objects.ListObject}.<br>
 * It also simulates a three level expandable listview by giving each child its
 * own {@link android.view.View.OnClickListener}.
 * 
 * 
 * @author tholene, Carl
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

	private static final String EMPTY_LIST = "EMPTY";

	// The context this is used in
	private Context context;
	// header titles
	private List<String> headerList;
	// The lists
	List<ListObject> todayEventList;
	List<ListObject> tomorrowEventList;
	List<ListObject> thisWeekEventList;
	// child data in format of header title, data container (ListObject)
	private HashMap<String, List<ListObject>> objectDataMap;

	// The database
	private DBHandler dbHandler;

	// Keeps track of changes
	private final DataSetObservable dataSetObservable = new DataSetObservable();

	/**
	 * Create a new custom list adapter.
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
		headerList = new ArrayList<String>();

		headerList.add(context.getResources().getString(R.string.todayLabel));
		headerList
				.add(context.getResources().getString(R.string.tomorrowLabel));
		headerList
				.add(context.getResources().getString(R.string.thisWeekLabel));

		objectDataMap.put(
				context.getResources().getString(R.string.todayLabel),
				todayEventList);
		objectDataMap.put(
				context.getResources().getString(R.string.tomorrowLabel),
				tomorrowEventList);
		objectDataMap.put(
				context.getResources().getString(R.string.thisWeekLabel),
				thisWeekEventList);

	}

	public void reInit() {
		List<ListObject> listObjects = dbHandler.getAllListObjects();

		for (ListObject lo : listObjects) {
			// we only want evens in this fragment (objects with a set time)
			if (dbHandler.getTime(lo) != null) {
				addListObject(lo);
			}
		}

		// TODO default empty list view
		// Adds a textview to all empty childlists
		MainActivity mActivity = (MainActivity) context;
		ExpandableListView expLv = (ExpandableListView) mActivity
				.findViewById(R.id.expEventList);

		this.notifyDataSetChanged();
		expLv.expandGroup(0, true);
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

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		if (listObject.getName().equals(EMPTY_LIST)) {
			LayoutInflater inflaInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflaInflater.inflate(R.layout.empty_list, null);
		} else {

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
			// + 300);
			// deleteButton.setX(convertView.getRight() + 300);

			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DBHandler dbh = new DBHandler(context);
					dbh.deleteListObject(listObject);
					removeListObjectToday(listObject);
					removeListObjectTomorrow(listObject);
					removeListObjectThisWeek(listObject);
				}

			});

			childLabel.setText(childText);
			childTimeView.setText(childTimeText.equals("") ? "no start time"
					: childTimeText);
			// Get a textview for the object's data
			TextView childData = (TextView) convertView
					.findViewById(R.id.taskData);

			// We don't want the data to show yet...
			childData.setVisibility(View.INVISIBLE);
			childData.setHeight(0);

			childLabel.setText(childText);

			CustomOnClickListener clickListener = new CustomOnClickListener(
					listObject, childLabel, childData);
			childLabel.setOnClickListener(clickListener);
			// TODO We add two listeners since it wont work on one if the other
			// is
			// added to
			// Adding touchlisteners
			childLabel.setOnTouchListener(new OptionTouchListener(context,
					convertView));
			// convertView.setOnTouchListener(new OptionTouchListener(context,
			// convertView));

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

		// Get the name of the header to display for each entry
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_header, null);
		}

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
				&& thisWeekEventList.isEmpty();
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
	 * placed where it should be.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObject(ListObject listObject) {
		// Get a calendar with current system time
		Time theTime = dbHandler.getTime(listObject);
		if (theTime != null) {
			// Get start date
			Date theDate = theTime.getStartDate();
			// Add into the relevant list
			if (isToday(theDate)) {
				addListObjectToday(listObject);
			} else if (isTomorrow(theDate)) {
				addListObjectTomorrow(listObject);
			} else {
				addListObjectThisWeek(listObject);
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
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Add a new event for someday if the task is not already in the list
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectThisWeek(ListObject listObject) {
		if (!thisWeekEventList.contains(listObject)) {
			thisWeekEventList.add(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a event for today.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectToday(ListObject listObject) {
		if (todayEventList.contains(listObject)) {
			todayEventList.remove(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a event for tomorrow.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectTomorrow(ListObject listObject) {
		if (tomorrowEventList.contains(listObject)) {
			tomorrowEventList.remove(listObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a event for someday.
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObjectThisWeek(ListObject listObject) {
		if (thisWeekEventList.contains(listObject)) {
			thisWeekEventList.remove(listObject);
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
	 * Get a list object from the someday list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObjectSomeday(int i) {
		if (0 <= i && i < thisWeekEventList.size()) {
			return thisWeekEventList.get(i);
		}
		return null;
	}

	// TODO Look into better way to check if it is today or tomorrow
	/**
	 * Method to check if a date is today
	 * 
	 * @param theDate
	 *            the date to see if it is today
	 * @return true if it is today
	 */
	public boolean isToday(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time and set it to day after today
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.roll(Calendar.DAY_OF_YEAR, true);
		tomorrow.set(Calendar.HOUR_OF_DAY, 0);

		return theCalendar.before(tomorrow);
	}

	/**
	 * Method to check if a date is tomorrow
	 * 
	 * @param theDate
	 *            the date to see if it is tomorrow
	 * @return true of it is tomorrow
	 */
	public boolean isTomorrow(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time and set it to day after
		// tomorrow
		Calendar dayAfterTomorrow = Calendar.getInstance();
		dayAfterTomorrow.set(Calendar.HOUR_OF_DAY, 0);
		dayAfterTomorrow.roll(Calendar.DAY_OF_YEAR, true);
		if (theCalendar.before(dayAfterTomorrow)) {
			return false;
		}
		dayAfterTomorrow.roll(Calendar.DAY_OF_YEAR, true);

		return theCalendar.before(dayAfterTomorrow);
	}
}
