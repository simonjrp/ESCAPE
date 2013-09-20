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
 * A fragment displaying an expandable list with tasks.
 * 
 * @author tholene, Carl
 */
public class ExpandableListFragment extends Fragment {

	CustomExpandableListAdapter listAdapter;
	ExpandableListView expListView;

	List<TaskModel> todayTaskList;
	List<TaskModel> tomorrowTaskList;
	List<TaskModel> somedayTaskList;

	List<String> headerList;
	HashMap<String, List<TaskModel>> taskDataMap;

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

	/*
	 * Preparing the dummy list data
	 */
	private void getListData() {
		headerList = new ArrayList<String>();

		headerList.add(getResources().getString(R.string.todayLabel));
		headerList.add(getResources().getString(R.string.tomorrowLabel));
		headerList.add(getResources().getString(R.string.somedayLabel));

		taskDataMap = new HashMap<String, List<TaskModel>>();
		todayTaskList = new ArrayList<TaskModel>();
		tomorrowTaskList = new ArrayList<TaskModel>();
		somedayTaskList = new ArrayList<TaskModel>();

        taskDataMap.put(getResources().getString(R.string.todayLabel), todayTaskList);
        taskDataMap.put(getResources().getString(R.string.tomorrowLabel), tomorrowTaskList);
        taskDataMap.put(getResources().getString(R.string.somedayLabel), somedayTaskList);

		// testTask
		addTaskToday(new TaskModel("Handla kläder", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Nordstan"),
				"Glöm inte plånboken"));
		addTaskTomorrow(new TaskModel("Handla skor", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Skoaffären"),
				"Rabattkuponger!!!"));
		addTaskTomorrow(new TaskModel("Laga mat", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Hemma"),
				"Köttfärssås och spaghetti"));
		addTaskSomeday(new TaskModel("Gå på Liseberg", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Liseberg"),
				"Köp åkband"));



	}

	/**
	 * Add a new task for today.
	 * 
	 * @param Task
	 *            the task to add
	 */
	public void addTaskToday(TaskModel Task) {
		todayTaskList.add(Task);
	}

	/**
	 * Add a new task for tomorrow.
	 * 
	 * @param Task
	 *            the task to add
	 */
	public void addTaskTomorrow(TaskModel Task) {
		tomorrowTaskList.add(Task);
	}

	/**
	 * Add a new task for someday.
	 * 
	 * @param Task
	 *            the task to add
	 */

	public void addTaskSomeday(TaskModel Task) {
		somedayTaskList.add(Task);
	}

}
