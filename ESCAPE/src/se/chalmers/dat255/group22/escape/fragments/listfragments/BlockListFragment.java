package se.chalmers.dat255.group22.escape.fragments.listfragments;

import se.chalmers.dat255.group22.escape.NewBlockActivity;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.BlockListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A fragment displaying the IBlockObjects that are saved to the database. An
 * IBlockObject is something that is automatically generated around the normal
 * event schedule.
 * 
 * @author anno
 */
public class BlockListFragment extends Fragment {
	// The listView to display data in
	private ListView ourTaskList;
	// The adapter used to handle data
	private BlockListAdapter ourListAdapter;

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
		case R.id.add_task:
			Intent intent = new Intent(getActivity(), NewBlockActivity.class);
			startActivity(intent);
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

	/*
	 * Initializes the list and adapter
	 */
	private void initialize() {
		ourListAdapter = new BlockListAdapter(getActivity());
		ourTaskList = (ListView) getActivity().findViewById(R.id.task_list);
		ourTaskList.setAdapter(ourListAdapter);
	}
}
