package se.chalmers.dat255.group22.escape;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.test_fragment, container, false);

		TextView textView = (TextView) v.findViewById(R.id.fragment_title);

		// Get the title of the argument and show it in the textview
		textView.setText(getArguments().getString("TITLE"));

		return v;

	}
}
