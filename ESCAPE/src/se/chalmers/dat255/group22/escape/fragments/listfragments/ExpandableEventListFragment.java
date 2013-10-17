package se.chalmers.dat255.group22.escape.fragments.listfragments;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.CategoryAdapter;
import se.chalmers.dat255.group22.escape.adapters.CustomExpandableListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

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
		return inflater.inflate(R.layout.expandable_event_list, container,
				false);
	}

	@Override
	public void onResume() {
		super.onResume();
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
						if (listAdapter != null)
							listAdapter.notifyDataSetChanged();
					}
				});
	}

	/**
	 * Display the category popup if the category list is not empty
	 */
	private void getPopup() {
		// TODO Find way to use popup without creating it every time!
		if (!listAdapter.getTheCategories().isEmpty()) {
			// if (listPopupWindow == null)
			initPopup();
			categoryAdapter = new CategoryAdapter(getActivity());
			categoryAdapter.setCategories(listAdapter.getTheCategories());
			listPopupWindow.setAdapter(categoryAdapter);
			categoryAdapter.notifyDataSetChanged();
			if (!categoryAdapter.isEmpty())
				listPopupWindow.show();
		}
	}
}
