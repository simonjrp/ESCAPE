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
	List<String> headerList;
	List<TaskModel> todayTaskList;
    List<TaskModel> tomorrowTaskList;
    List<TaskModel> somedayTaskList;
	HashMap<String, List<TaskModel>> taskDataMap;

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

		listAdapter = new MyListAdapter(getActivity(), headerList, taskDataMap);

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
		headerList = new ArrayList<String>();
		headerList.add("Today");
		headerList.add("Tomorrow");
		headerList.add("Someday");
		taskDataMap = new HashMap<String, List<TaskModel>>();
		todayTaskList = new ArrayList<TaskModel>();
        tomorrowTaskList = new ArrayList<TaskModel>();
        somedayTaskList = new ArrayList<TaskModel>();

		// testTask
		addTaskToday(new TaskModel("Handla kläder", new Time(
				System.currentTimeMillis()), new Date(
				System.currentTimeMillis()), new Location("Nordstan"),
				"Glöm inte plånboken"));
		addTaskTomorrow(new TaskModel("Handla skor", new Time(
                System.currentTimeMillis()), new Date(
                System.currentTimeMillis()), new Location("Skoaffären"),
                "Rabattkuponger!!!"));
		addTaskTomorrow(new TaskModel("Laga mat",
                new Time(System.currentTimeMillis()), new Date(
                System.currentTimeMillis()), new Location("Hemma"),
                "Köttfärssås och spaghetti"));

		taskDataMap.put("Today", todayTaskList);
		taskDataMap.put("Tomorrow", tomorrowTaskList);

		addTaskSomeday(new TaskModel("Handla mat", new Time(System.currentTimeMillis()), new Date(
                System.currentTimeMillis()), new Location("Nordstan"), "Glöm inte plånboken"));

		taskDataMap.put("Someday", somedayTaskList);

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
