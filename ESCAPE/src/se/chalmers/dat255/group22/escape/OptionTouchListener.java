package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public class OptionTouchListener extends LongTouchActionListener {
//	private boolean alreadyExpanded;
//	private ListObject listObject;
//	private TextView childLabel;
//	private CustomExpandableListAdapter adapter;
	private View ve;
	
	public OptionTouchListener(Context context, View ve) {
		super(context);
		this.ve = ve;
//		this.listObject = listObject;
//		this.childLabel = childLabel;
	}

	@Override
	public void onClick(View v) {
		// TODO add the old?
	}

	@Override
	public void onLongTouchAction(View v) {
		Button editButton = (Button) ve.findViewById(R.id.editButton);
		editButton.setVisibility(View.VISIBLE);
		Button deleteButton = (Button) ve.findViewById(R.id.deleteButton);
		deleteButton.setVisibility(View.VISIBLE);
	}

}
