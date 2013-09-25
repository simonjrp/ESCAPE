package se.chalmers.dat255.group22.escape.ListFragment;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A fragment displaying a list with tasks.
 * A task is different from an event such that a task does not have a set time
 * while an event does.
 * 
 * @author tholene, Carl
 */
public class TaskListFragment extends Fragment {

    // The listView to display data in
	ListView ourTaskList;
    // The adapter used to handle data
	CustomListAdapter ourListAdapter;
    // List containing the data displayed
	List<ListObject> taskList;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // Initiate the lists and set the adapter to use
		taskList = new ArrayList<ListObject>();
		ourListAdapter = new CustomListAdapter(getActivity(), taskList);
		ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
		ourTaskList.setAdapter(ourListAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();

        // Fetch tasks from database
		DBHandler dbHandler = new DBHandler(getActivity());
		List<ListObject> listObjects = dbHandler.getAllListObjects();
		for (ListObject lo : listObjects) {
            // we only want tasks in this fragment (objects without a specific time)
			if (lo.getTime() == null) {
				addListObject(lo);
			}
		}
	}

	/**
	 * Add a new task for the list
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
            // Notify the adapter that data has changed
            ourListAdapter.notifyDataSetChanged();
		}
	}
}
