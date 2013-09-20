package se.chalmers.dat255.group22.escape;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment displaying an expandable list with tasks.
 * 
 * @author tholene, Carl
 */
public class ExpandableListFragment extends Fragment {

	CustomExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<ListObject> todayTaskList;
	List<ListObject> tomorrowTaskList;
	List<ListObject> somedayTaskList;
	List<String> headerList;
	HashMap<String, List<ListObject>> taskDataMap;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// get the list data
		getListData();

		listAdapter = new CustomExpandableListAdapter(getActivity(),
				headerList, taskDataMap);

		// getting the view
		expListView = (ExpandableListView) getActivity().findViewById(
				R.id.lvExp);
		// setting list adapter
		expListView.setAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.expandable_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		DBHandler dbHandler = new DBHandler(getActivity());
		List<ListObject> listObjects = dbHandler.getAllListObjects();
		for (ListObject lo : listObjects) {
			addListObjectToday(lo);
		}

		// TODO Ugly code for 'updating' the expandable list view. Without this,
		// if a category list is expanded before adding a new activity, you
		// have to collapse and expand that category list to be able to see the
		// newly added item.
		ExpandableListView expLv = (ExpandableListView) getActivity()
				.findViewById(R.id.lvExp);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			if (expLv.isGroupExpanded(i)) {
				expLv.collapseGroup(i);
				expLv.expandGroup(i);
			}
		}

	}

	/*
	 * Preparing the dummy list data
	 */
	private void getListData() {

		taskDataMap = new HashMap<String, List<ListObject>>();
		todayTaskList = new ArrayList<ListObject>();
		tomorrowTaskList = new ArrayList<ListObject>();
		somedayTaskList = new ArrayList<ListObject>();
		headerList = new ArrayList<String>();

		headerList.add(getResources().getString(R.string.todayLabel));
		headerList.add(getResources().getString(R.string.tomorrowLabel));
		headerList.add(getResources().getString(R.string.somedayLabel));

		taskDataMap.put(getResources().getString(R.string.todayLabel),
				todayTaskList);
		taskDataMap.put(getResources().getString(R.string.tomorrowLabel),
				tomorrowTaskList);
		taskDataMap.put(getResources().getString(R.string.somedayLabel),
				somedayTaskList);

	}

	/**
	 * Add a new task for today.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectToday(ListObject listObject) {
		boolean alreadyExists = false;

		// TODO ugly, temporary code so that the same listobject doesn't get
		// added miltiple times
		for (ListObject lo : todayTaskList) {
			if (listObject.getName().equals(lo.getName())) {
				alreadyExists = true;
			}
		}

		if (!alreadyExists) {
			todayTaskList.add(listObject);
		}

	}

	/**
	 * Add a new task for tomorrow.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObjectTomorrow(ListObject listObject) {
		tomorrowTaskList.add(listObject);
	}

	/**
	 * Add a new task for someday.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */

	public void addListObjectSomeday(ListObject listObject) {
		somedayTaskList.add(listObject);
	}

}
