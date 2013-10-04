package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
		ourListAdapter.reInit();
        updateEditButtons();
	}

	/**
	 * Initialize the database, lists and adapter
	 */
	private void initialize() {
		// Initiate the lists and set the adapter to use
		ourListAdapter = new CustomListAdapter(getActivity(), this);
		ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
		ourTaskList.setAdapter(ourListAdapter);
	}

    protected void updateEditButtons() {
        TextView timeText = (TextView) getActivity().findViewById(
                R.id.startTimeTask);
        if (timeText != null)
            timeText.setVisibility(View.VISIBLE);

        ImageButton editButton = (ImageButton) getActivity().findViewById(
                R.id.editButton);
        if (editButton != null) {
            editButton.setVisibility(View.INVISIBLE);
            editButton.clearAnimation();
        }

        ImageButton deleteButton = (ImageButton) getActivity().findViewById(
                R.id.deleteButton);
        if (deleteButton != null) {
            deleteButton.setVisibility(View.INVISIBLE);
            deleteButton.clearAnimation();
        }
    }
}
