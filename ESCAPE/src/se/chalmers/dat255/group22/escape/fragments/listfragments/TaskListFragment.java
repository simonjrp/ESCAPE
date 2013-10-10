package se.chalmers.dat255.group22.escape.fragments.listfragments;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.CategoryAdapter;
import se.chalmers.dat255.group22.escape.adapters.CustomListAdapter;
import se.chalmers.dat255.group22.escape.objects.Category;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	// popup where use can pick what categories to display
	ListPopupWindow listPopupWindow;

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
		CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity());
		List<Category> tmpList = new ArrayList<Category>();
		tmpList.add(new Category("tmp0", null, null));
		tmpList.add(new Category("tmp1", null, null));
		tmpList.add(new Category("tmp2", null, null));
		tmpList.add(new Category("tmp3", null, null));
		tmpList.add(new Category("tmp4", null, null));
		tmpList.add(new Category("tmp5", null, null));
		tmpList.add(new Category("tmp6", null, null));
		tmpList.add(new Category("tmp7", null, null));
		tmpList.add(new Category("tmp8", null, null));
		tmpList.add(new Category("tmp9", null, null));
		tmpList.add(new Category("tmp10", null, null));
		tmpList.add(new Category("tmp11", null, null));
		tmpList.add(new Category("tmp12", null, null));
		tmpList.add(new Category("tmp13", null, null));
		tmpList.add(new Category("tmp14", null, null));
		tmpList.add(new Category("tmp15", null, null));
		tmpList.add(new Category("tmp16", null, null));
		tmpList.add(new Category("tmp17", null, null));
		tmpList.add(new Category("tmp18", null, null));
		tmpList.add(new Category("tmp19", null, null));
		// TODO Fix getter for real categories and make values matter
		categoryAdapter.setCategories(tmpList);
		listPopupWindow.setAdapter(categoryAdapter);
		listPopupWindow.setAnchorView(menuItemView);
		listPopupWindow.setModal(true);
		listPopupWindow.setWidth(300);
		listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
	}

	/**
	 * Called when popup should be displayed
	 */
	private void getPopup() {
		listPopupWindow.show();
	}
}
