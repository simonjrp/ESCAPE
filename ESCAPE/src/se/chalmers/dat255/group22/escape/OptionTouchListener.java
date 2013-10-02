package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

public class OptionTouchListener extends LongTouchActionListener {
	// private boolean alreadyExpanded;
	// private ListObject listObject;
	// private TextView childLabel;
	// private CustomExpandableListAdapter adapter;
	private View ve;
	private boolean animationStarted;
	private TranslateAnimation slide;

	public OptionTouchListener(Context context, View ve) {
		super(context);
		this.ve = ve;
		// this.listObject = listObject;
		// this.childLabel = childLabel;
	}

	@Override
	public void onClick(View v) {
		// TODO add the old?
	}

	@Override
	public void onLongTouchAction(View v) {
		TextView timeText = (TextView) ve.findViewById(R.id.startTimeTask);
		timeText.setVisibility(View.INVISIBLE);
		ImageButton editButton = (ImageButton) ve.findViewById(R.id.editButton);
		editButton.setVisibility(View.VISIBLE);

		ImageButton deleteButton = (ImageButton) ve
				.findViewById(R.id.deleteButton);
		deleteButton.setVisibility(View.VISIBLE);

		if (!animationStarted) {
			slide = new TranslateAnimation(-300, 0, 0, 0);
			slide.setDuration(250);
			slide.setFillAfter(true);
			slide.setFillEnabled(true);
			editButton.startAnimation(slide);
			deleteButton.startAnimation(slide);
			animationStarted = true;
		}
	}

}
