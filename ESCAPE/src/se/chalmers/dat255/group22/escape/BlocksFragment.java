package se.chalmers.dat255.group22.escape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BlocksFragment extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.blocks_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Button newBlockButton = (Button) getActivity().findViewById(
				R.id.new_block_button);
		newBlockButton.setOnClickListener((OnClickListener) this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), NewBlockActivity.class);
		startActivity(intent);
	}
}
