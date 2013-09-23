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
 * A fragment displaying an expandable list with tasks.<br>
 * A task is different from an event such that a task does not have a set time
 * while an event does.
 * 
 * @author tholene
 */
public class ExpandableTaskListFragment extends Fragment {

	CustomExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<ListObject> taskList;
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
				R.id.expTaskList);
		// setting list adapter
		expListView.setAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.expandable_task_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		DBHandler dbHandler = new DBHandler(getActivity());
		List<ListObject> listObjects = dbHandler.getAllListObjects();
		for (ListObject lo : listObjects) {
			addListObject(lo);
		}

		// TODO Ugly code for 'updating' the expandable list view. Without this,
		// if a category list is expanded before adding a new activity, you
		// have to collapse and expand that category list to be able to see the
		// newly added item.
		ExpandableListView expLv = (ExpandableListView) getActivity()
				.findViewById(R.id.expTaskList);
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
		taskList = new ArrayList<ListObject>();
		headerList = new ArrayList<String>();

		headerList.add(getResources().getString(R.string.taskHeader));

		taskDataMap
				.put(getResources().getString(R.string.taskHeader), taskList);

	}

	/**
	 * Add a new task for today.
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObject(ListObject listObject) {
		boolean alreadyExists = false;

		// TODO ugly, temporary code so that the same listobject doesn't get
		// added miltiple times
		for (ListObject lo : taskList) {
			if (listObject.getName().equals(lo.getName())) {
				alreadyExists = true;
			}
		}

		if (!alreadyExists) {
			taskList.add(listObject);
		}

	}
}
