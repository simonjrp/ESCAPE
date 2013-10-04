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
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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

    PopupWindow popUp;

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
	 * Initialize the database, lists and adapter
	 */
	private void initialize() {
		// Initiate the lists and set the adapter to use
		ourListAdapter = new CustomListAdapter(getActivity());
		ourTaskList = (ListView) getActivity().findViewById(R.id.listView);
		ourTaskList.setAdapter(ourListAdapter);
	}

    private void getPopup(){
        //TODO implement a way to select categories to display
        View menuItemView = getActivity().findViewById(R.id.pick_category);
        PopupMenu popup = new PopupMenu(getActivity(), menuItemView);
        popup.setOnMenuItemClickListener(new tmpListener());
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.popup_categories, popup.getMenu());
        popup.show();
    }
    private class tmpListener implements PopupMenu.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.cat1:
                    //TODO checkboxes are not set atm!
                    if (item.isChecked()) item.setChecked(false);
                    else item.setChecked(true);
                    Toast.makeText(getActivity(), "shit", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.cat2:
                    if (item.isChecked()) item.setChecked(false);
                    else item.setChecked(true);
                    return true;
                default:
                    return false;
            }
        }
    }
}

