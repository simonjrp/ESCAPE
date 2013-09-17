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
	List<String> listHeader;
	HashMap<String, List<String>> listTask;
	HashMap<String, List<String>> listTaskData;

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

		listAdapter = new MyListAdapter(getActivity(), listHeader, listTask,
				listTaskData);

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
		listHeader = new ArrayList<String>();
		listTask = new HashMap<String, List<String>>();

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

		/*
		 * // Adding header data listHeader.add("Meeting with projectgroup");
		 * listHeader.add("Make use of coupon at ICA");
		 * 
		 * // Adding child data List<String> task1 = new ArrayList<String>(); //
		 * parse database? <date> <time> <description>? task1.add("16/9" + " " +
		 * "08:00" + " " + "EDIT-huset 3213");
		 * 
		 * List<String> task2 = new ArrayList<String>();
		 * task2.add("ICA Olskroken" + " | " + "Remind me at" + " 15:00");
		 * 
		 * listTask.put(listHeader.get(0), task1); // Header, Child data
		 * listTask.put(listHeader.get(1), task2);
		 */

	}

	/**
	 * Add a task model as a task
	 * 
	 * @param Task
	 *            the task to add
	 */
	public void addTask(TaskModel Task) {
		addTask(Task.getName(), Task.getTime(), Task.getDate(),
				Task.getLocation(), Task.getDescription());
	}

	/**
	 * Add a new task to the list.
	 * 
	 * @param name
	 *            the name of the task. This will be displayed even when the
	 *            task is not expanded.
	 * @param time
	 *            the time of the task.
	 * @param date
	 *            the date of the task.
	 * @param location
	 *            the location of the task.
	 * @param description
	 *            the description of the task.
	 */
	public void addTask(String name, Time time, Date date, Location location,
			String description) {

		listHeader.add(name);
		List<String> task1 = new ArrayList<String>();
		task1.add(time.toString() + "\n" + date.toString() + "\n"
				+ location.getProvider() + "\n" + description);
		listTask.put(listHeader.get(listHeader.indexOf(name)), task1);

	}

}
