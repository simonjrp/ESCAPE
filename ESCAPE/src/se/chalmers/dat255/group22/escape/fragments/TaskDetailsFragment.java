package se.chalmers.dat255.group22.escape.fragments;

import se.chalmers.dat255.group22.escape.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class representing a fragment showing the data of a task/event (with
 * textfields, spinners etc.)
 * 
 * @author Simon Persson
 */
public class TaskDetailsFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.task_details, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
