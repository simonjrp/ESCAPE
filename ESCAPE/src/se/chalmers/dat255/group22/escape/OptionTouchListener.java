package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.view.animation.TranslateAnimation;


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
		ImageButton editButton = (ImageButton) ve.findViewById(R.id.editButton);
		editButton.setVisibility(View.VISIBLE);
		
		TranslateAnimation slide = new TranslateAnimation(0, -100, 0, 0);
		slide.setDuration(100);
		slide.setFillAfter(true);
		editButton.startAnimation(slide);
		
		ImageButton deleteButton = (ImageButton) ve.findViewById(R.id.deleteButton);
		deleteButton.setVisibility(View.VISIBLE);
	}

}
