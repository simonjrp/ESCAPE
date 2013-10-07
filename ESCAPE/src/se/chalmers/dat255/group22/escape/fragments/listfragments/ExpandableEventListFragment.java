package se.chalmers.dat255.group22.escape.fragments.listfragments;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.CustomExpandableListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * A fragment displaying an expandable list with events.<br>
 * An event is different from a task such that an event has a set time while a
 * task does not.
 * 
 * @author tholene, Carl
 */
public class ExpandableEventListFragment extends Fragment {

    // The adapter used to display out ListObjects
	CustomExpandableListAdapter listAdapter;
    // the list objects are displayed in
	ExpandableListView expListView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // Make fragment aware it has an action bar object!
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
                Toast.makeText(getActivity(), "event category pick", Toast.LENGTH_SHORT).show();
                //TODO implement a way to pick what categories to display
                break;
        }
        return super.onOptionsItemSelected(item);
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
	 * Initialize the lists and adapter
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
