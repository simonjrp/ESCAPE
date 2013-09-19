package se.chalmers.dat255.group22.escape;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * A fragment displaying an expandable list with events
 * 
 * @author Tholene, Carl
 */
public class ExpandableListFragment extends Fragment {

	MyListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	List<TaskModel> taskList;
	HashMap<String, List<TaskModel>> listDataChild;

	/**
	 * Called when the activity containing the fragment is created.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// get the list data
		getListData();

		listAdapter = new MyListAdapter(getActivity(), listDataHeader, listDataChild);

		// getting the view
		expListView = (ExpandableListView) getActivity().findViewById(
				R.id.lvExp);
		// setting list adapter
		expListView.setAdapter(listAdapter);
	}

	/**
	 * Called when view is created
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.expandable_list, container, false);
	}

	/*
	 * Preparing the list data
	 */
	private void getListData() {
		listDataHeader = new ArrayList<String>();
		listDataHeader.add("Today");
		listDataHeader.add("Tomorrow");
		listDataHeader.add("Someday");
		listDataChild = new HashMap<String, List<TaskModel>>();
		taskList = new ArrayList<TaskModel>();

		// testTask
		addTask(new TaskModel("Handla kläder", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Nordstan"),
				"Glöm inte plånboken"));
		addTask(new TaskModel("Handla skor", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Skoaffären"),
				"Rabattkuponger!!!"));
		addTask(new TaskModel("Laga mat",
				new Time(System.currentTimeMillis()), new Date(
						System.currentTimeMillis()), new Location("Hemma"),
				"Köttfärssås och spaghetti"));

		listDataChild.put("Today", taskList);
		listDataChild.put("Tomorrow", taskList);
		addTask(new TaskModel("Handla mat", new Time(18, 00, 00), new Date(
				2013, 9, 18), new Location("Nordstan"), "Glöm inte plånboken"));
		listDataChild.put("Someday", taskList);

	}

	/**
	 * Add a task model as a task
	 * 
	 * @param Task
	 *            the task to add
	 */
	public void addTask(TaskModel Task) {
		taskList.add(Task);
	}

}
