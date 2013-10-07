package se.chalmers.dat255.group22.escape.listFragments;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.CustomExpandableListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * A fragment displaying an expandable list with events.<br>
 * An event is different from a task such that an event has a set time while a
 * task does not.
 * 
 * @author tholene, Carl
 */
public class ExpandableEventListFragment extends Fragment {

	private static final String EMPTY_LIST = "EMPTY";
	CustomExpandableListAdapter listAdapter;
	ExpandableListView expListView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialize();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.expandable_event_list, container,
				false);
	}

	@Override
	public void onResume() {
		super.onResume();
		// TODO temp fix, maybe better way to read from DB again?
		initialize();
		listAdapter.reInit();
	}

	/**
	 * Initialize the database, lists and adapter
	 */
	private void initialize() {
		// Create the adapter used to display the list
		listAdapter = new CustomExpandableListAdapter(getActivity());
		// getting the list view
		expListView = (ExpandableListView) getActivity().findViewById(
				R.id.expEventList);
		// setting list adapter
		expListView.setAdapter(listAdapter);
	}
}
