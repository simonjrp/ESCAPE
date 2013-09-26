package se.chalmers.dat255.group22.escape.ListFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.Time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * A fragment displaying an expandable list with events.<br>
 * An event is different from a task such that an event has a set time while a
 * task does not.
 * 
 * @author tholene, Carl
 */
public class ExpandableEventListFragment extends Fragment {

	CustomExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<ListObject> todayEventList;
	List<ListObject> tomorrowEventList;
	List<ListObject> thisWeekEventList;
	List<String> headerList;
	HashMap<String, List<ListObject>> eventDataMap;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// get the list data
		getListData();

		listAdapter = new CustomExpandableListAdapter(getActivity(),
				headerList, eventDataMap);

		// getting the view
		expListView = (ExpandableListView) getActivity().findViewById(
				R.id.expEventList);
		// setting list adapter
		expListView.setAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.expandable_event_list, container,
				false);
	}

	@Override
	public void onResume() {
		super.onResume();

        DBHandler dbHandler = new DBHandler(getActivity());
		List<ListObject> listObjects = dbHandler.getAllListObjects();

		for (ListObject lo : listObjects) {
			// we only want evens in this fragment (objects with a set time)
            try {
                if (dbHandler.getTime(lo).getStartDate() != null) {
                    //addListObject(lo);
                    addListObjectToday(lo);
                } else {
                    //dbHandler.addListObjectsWithTime(lo, new Time(1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+1)));
                    //addListObject(lo);
                    addListObjectTomorrow(lo);
                }
            } catch (Exception e){
                System.out.println("Exception caught!");
                e.printStackTrace();
            }
		}

		// TODO Ugly code for 'updating' the expandable list view. Without this,
		// if a category list is expanded before adding a new activity, you
		// have to collapse and expand that category list to be able to see the
		// newly added item.
		/*
		 * Look into implementing the following methods in CustomExpandableListAdapter
		 * public void notifyDataSetChanged () public void
		 * registerDataSetObserver (DataSetObserver observer) public void
		 * unregisterDataSetObserver (DataSetObserver observer)
		 */

		ExpandableListView expLv = (ExpandableListView) getActivity()
				.findViewById(R.id.expEventList);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			if (expLv.isGroupExpanded(i)) {
				expLv.collapseGroup(i);
				expLv.expandGroup(i);
			}
		}
		expLv.expandGroup(0, true);

	}

    /**
     * Prepare the dummy list data
     */
	private void getListData() {

		eventDataMap = new HashMap<String, List<ListObject>>();
		todayEventList = new ArrayList<ListObject>();
		tomorrowEventList = new ArrayList<ListObject>();
		thisWeekEventList = new ArrayList<ListObject>();
		headerList = new ArrayList<String>();

		headerList.add(getResources().getString(R.string.todayLabel));
		headerList.add(getResources().getString(R.string.tomorrowLabel));
		headerList.add(getResources().getString(R.string.thisWeekLabel));

		eventDataMap.put(getResources().getString(R.string.todayLabel),
				todayEventList);
		eventDataMap.put(getResources().getString(R.string.tomorrowLabel),
				tomorrowEventList);
		eventDataMap.put(getResources().getString(R.string.thisWeekLabel),
				thisWeekEventList);

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
		/*Date theDate = dbHandler.getTime(listObject).getStartDate();

		//TODO Check if this works and maybe add additional functionality!
		if (isToday(theDate)) {
			addListObjectToday(listObject);
		} else if (isTomorrow(theDate)) {
			addListObjectTomorrow(listObject);
		} else {
			addListObjectThisWeek(listObject);
		}*/
	}

	/**
	 * Add a new event for today.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectToday(ListObject listObject) {

		/*
		 * boolean alreadyExists = false;
		 * 
		 * // TODO ugly, temporary code so that the same listobject doesn't get
		 * // added multiple times
		 * for (ListObject lo : todayEventList) {
		 * if
		 * (listObject.getId() == lo.getId() ){
		 * alreadyExists = true;
		 * } }
		 */
		//if (!todayEventList.contains(listObject)) {
			todayEventList.add(listObject);
		//}
	}

	/**
	 * Add a new event for tomorrow.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectTomorrow(ListObject listObject) {
		//if (!tomorrowEventList.contains(listObject)) {
			tomorrowEventList.add(listObject);
		//}
	}

	/**
	 * Add a new event for someday.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectThisWeek(ListObject listObject) {
		if (!thisWeekEventList.contains(listObject)) {
			thisWeekEventList.add(listObject);
		}
	}

	/**
	 * Method to check if a date is today
	 * 
	 * @param theDate
	 *            the date to see if it is today
	 * @return true if it is today
	 */
	private boolean isToday(Date theDate) {
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
	private boolean isTomorrow(Date theDate) {
		// Get a calendar with the events start time
		GregorianCalendar theCalendar = new GregorianCalendar();
		theCalendar.setTime(theDate);
		// Get a calendar with current system time and set it to day after tomorrow
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
