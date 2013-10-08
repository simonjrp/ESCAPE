package se.chalmers.dat255.group22.escape.fragments.listfragments;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.CustomListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
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

	ListPopupWindow listPopupWindow;

	// TODO delete this tmp crap
	String[] products = {"A list", "containing", "meaningless", "nonsense",
			"Just like Television!"};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Make fragment aware it has an action bar object!
		setHasOptionsMenu(true);
		initialize();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_action, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.pick_category :
				getPopup();
				return true;
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
		// TODO temp fix, maybe better way to read from DB again?
		// initialize();
		ourListAdapter.reInit();
	}

	/**
	 * Initialize the list, adapter and popup
	 */
	private void initialize() {
		ourListAdapter = new CustomListAdapter(getActivity());
		ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
		ourTaskList.setAdapter(ourListAdapter);
		initPopup();
	}

	/**
	 * Create the popup used to pick what categories to display
	 */
	private void initPopup() {
		View menuItemView = getActivity().findViewById(R.id.pick_category);
		listPopupWindow = new ListPopupWindow(getActivity());
		listPopupWindow.setAdapter(new ArrayAdapter(getActivity(),
				R.layout.popup_categories, products));
		listPopupWindow.setAnchorView(menuItemView);
		listPopupWindow.setWidth(300);
		listPopupWindow.setHeight(400);
		listPopupWindow.setModal(true);
		// listPopupWindow.setOnItemClickListener();
	}

    /**
     * Called when popup should be displayed
     */
	private void getPopup() {
		listPopupWindow.show();

	}
}
