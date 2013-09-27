package se.chalmers.dat255.group22.escape.ListFragment;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A fragment displaying a list with tasks. A task is different from an event
 * such that a task does not have a set time while an event does.
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
    // The database
    private DBHandler dbHandler;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        initialize();
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
		List<ListObject> listObjects = dbHandler.getAllListObjects();
		for (ListObject lo : listObjects) {
			// we only want tasks in this fragment (objects without a specific
			// time)
			if (lo.getTime() == null) {
				addListObject(lo);
			}
		}
	}

    /**
     * Initialize the database, lists and adapter
     */
    private void initialize() {
        dbHandler = new DBHandler(getActivity());
        // Initiate the lists and set the adapter to use
        taskList = new ArrayList<ListObject>();
        ourListAdapter = new CustomListAdapter(getActivity(), taskList);
        ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
        ourTaskList.setAdapter(ourListAdapter);
    }

	/**
	 * Add a new task for the list
	 * 
	 * @param listObject
	 *            the listObject to add
	 */
	public void addListObject(ListObject listObject) {
		if (!taskList.contains(listObject)) {
			taskList.add(listObject);
			ourListAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a task from the list
	 * 
	 * @param listObject
	 *            the listObject to remove
	 */
	public void removeListObject(ListObject listObject) {
		if (taskList.contains(listObject)) {
			taskList.remove(listObject);
			ourListAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Get a list object from the list
	 * 
	 * @param i
	 *            number of object to return
	 * @return the specified list object
	 */
	public ListObject getListObject(int i) {
		if ( 0 <= i && i < taskList.size() ) {
			return taskList.get(i);
		}
		return null;
	}
}
