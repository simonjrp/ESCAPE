package se.chalmers.dat255.group22.escape.ListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;
import android.app.Fragment;
import android.os.Bundle;
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

        for(ListObject lo:listObjects) {
            // we only want evens in this fragment (objects with a set time)
            if(lo.getTime() != null) {

            }
        }

		// TODO Ugly code for 'updating' the expandable list view. Without this,
		// if a category list is expanded before adding a new activity, you
		// have to collapse and expand that category list to be able to see the
		// newly added item.

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

	/*
	 * Preparing the dummy list data
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
	 * Add a new event for today.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectToday(ListObject listObject) {

        boolean alreadyExists = false;

        // TODO ugly, temporary code so that the same listobject doesn't get
        // added multiple times
        for (ListObject lo : todayEventList) {
            if (listObject.getId() == lo.getId() ){
                alreadyExists = true;
            }
        }

        if (!alreadyExists) {
            todayEventList.add(listObject);
        }
	}

	/**
	 * Add a new event for tomorrow.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectTomorrow(ListObject listObject) {
		tomorrowEventList.add(listObject);
	}

	/**
	 * Add a new event for someday.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */

	public void addListObjectThisWeek(ListObject listObject) {
		thisWeekEventList.add(listObject);
	}

}
