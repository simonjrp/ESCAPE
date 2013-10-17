package se.chalmers.dat255.group22.escape.fragments.listfragments;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.CategoryAdapter;
import se.chalmers.dat255.group22.escape.adapters.CustomListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * A fragment displaying a list with tasks. A task is different from an event
 * such that a task does not have a set time while an event does.
 * 
 * @author tholene, Carl
 */
public class TaskListFragment extends Fragment {

	// The listView to display data in
	private ListView ourTaskList;
	// The adapter used to handle data
	private CustomListAdapter ourListAdapter;
	// popup where use can pick what categories to display
	private ListPopupWindow listPopupWindow;
	// The adapter used to display the category popup
	private CategoryAdapter categoryAdapter;
	// the width used for the category popup
	private int categoryListWidth;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Make fragment aware it has an action bar object!
		setHasOptionsMenu(true);
		initialize();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.pick_category :
				if (this.isVisible()) {
					getPopup();
					return true;
				}
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
		initialize();
		ourListAdapter.reInit();
	}

	/**
	 * Initialize the list, adapter and popup
	 */
	private void initialize() {
		ourListAdapter = new CustomListAdapter(getActivity());
		ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
		ourTaskList.setAdapter(ourListAdapter);
		categoryListWidth = getActivity().getResources().getDimensionPixelSize(
				R.dimen.category_list_width);
		initPopup();
	}

	/**
	 * Create the popup used to pick what categories to display
	 */
	private void initPopup() {
		View menuItemView = getActivity().findViewById(R.id.pick_category);
		listPopupWindow = new ListPopupWindow(getActivity());
		listPopupWindow.setAnchorView(menuItemView);
		listPopupWindow.setModal(true);
		listPopupWindow.setWidth(categoryListWidth);
		listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
		listPopupWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						if (ourListAdapter != null)
							ourListAdapter.notifyDataSetChanged();
					}
				});
	}

	/**
	 * Called when popup should be displayed
	 */
	private void getPopup() {
		// TODO Find way to use popup without creating it every time!
		if (!ourListAdapter.getTheCategories().isEmpty()) {
			// if (listPopupWindow == null)
			initPopup();
			categoryAdapter = new CategoryAdapter(getActivity());
			categoryAdapter.setCategories(ourListAdapter.getTheCategories());
			listPopupWindow.setAdapter(categoryAdapter);
			categoryAdapter.notifyDataSetChanged();
			if (!categoryAdapter.isEmpty())
				listPopupWindow.show();
		}
	}
}
