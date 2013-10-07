package se.chalmers.dat255.group22.escape.listeners;

import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import se.chalmers.dat255.group22.escape.R;

public class OptionTouchListener extends LongTouchActionListener {
	private View ve;
	private boolean animationStarted;
	private TranslateAnimation slide;
	private TextView timeText;
	private ImageButton editButton;
	private ImageButton deleteButton;
	public OptionTouchListener(Context context, View ve) {
		super(context);
		this.ve = ve;
		animationStarted = false;

		timeText = (TextView) ve.findViewById(R.id.startTimeTask);
		editButton = (ImageButton) ve.findViewById(R.id.editButton);
		deleteButton = (ImageButton) ve.findViewById(R.id.deleteButton);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onLongTouchAction(View v) {
		if (!v.findViewById(R.id.taskData).isShown()) {
			timeText.setVisibility(View.INVISIBLE);
			editButton.setVisibility(View.VISIBLE);
			deleteButton.setVisibility(View.VISIBLE);

			slide = new TranslateAnimation(-300, 0, 0, 0);
			slide.setDuration(250);
			slide.setFillAfter(true);
			slide.setFillEnabled(true);

			// Should in fact check this for BOTH buttons, but since one is only
			// showed with the other it would just be unnecessary code
			if (editButton.getAnimation() == null) {
				// Same logic applies here as we SET and CLEAR the animation for
				// the button
				editButton.setAnimation(slide);

				editButton.startAnimation(slide);
				deleteButton.startAnimation(slide);
			}
		}
	}

}
