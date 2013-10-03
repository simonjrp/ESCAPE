package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        initialize();
	}

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_action, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pick_category :
                Toast.makeText(getActivity(), "task category pick", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		ourListAdapter.reInit();
	}

	/**
	 * Initialize the database, lists and adapter
	 */
	private void initialize() {
		// Initiate the lists and set the adapter to use
		ourListAdapter = new CustomListAdapter(getActivity());
		ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
		ourTaskList.setAdapter(ourListAdapter);
	}
}
