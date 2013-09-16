package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Created by tholene on 9/16/13.
 * 
 * @author Tholene, Carl
 */
public class ExpandableListFragment extends Fragment {

	MyListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// get the list data
		getListData();

		listAdapter = new MyListAdapter(getActivity(), listDataHeader,
				listDataChild);

		// setting list adapter
		expListView = (ExpandableListView) getActivity().findViewById(R.id.lvExp);
		expListView.setAdapter(listAdapter);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.expandable_list, container, false);
	}

	/*
	 * Preparing the list data
	 */
	private void getListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding header data
		listDataHeader.add("Meeting with projectgroup");
		listDataHeader.add("Make use of coupon at ICA");

		// Adding child data
		List<String> task1 = new ArrayList<String>();
		// parse database? <date> <time> <description>?
		task1.add("16/9" + " " + "08:00" + " " + "EDIT-huset 3213");

		List<String> task2 = new ArrayList<String>();
		task2.add("ICA Olskroken" + " | " + "Remind me at" + " 15:00");

		listDataChild.put(listDataHeader.get(0), task1); // Header, Child data
		listDataChild.put(listDataHeader.get(1), task2);

	}
}
