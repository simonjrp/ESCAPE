package se.chalmers.dat255.group22.escape.listeners;

import se.chalmers.dat255.group22.escape.R;
import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * This listener decides what should happen whenever a LongTouchAction is
 * performed on its view.<br>
 * <br>
 * 
 * Whenever a LongTouchAction is performed and the view is not expanded, two
 * buttons will slide in and replace the textView placed to the right in the
 * current view.<br>
 * This will act as the "edit-mode" of the view and these buttons can later on
 * be dismissed by performing a simple touch once again.
 * 
 * @author Anno, Johanna, tholene
 */
public class OptionTouchListener extends LongTouchActionListener {

	private TranslateAnimation slide;
	private TextView timeText;
	private ImageButton editButton;
	private ImageButton deleteButton;

	/**
	 * Create a new OptionTouchListener.
	 * 
	 * @param context
	 *            the context to be used.
	 * @param ve
	 *            the view that makes use of the listener.
	 */
	public OptionTouchListener(Context context, View ve) {
		super(context);

		timeText = (TextView) ve.findViewById(R.id.startTimeTask);
		editButton = (ImageButton) ve.findViewById(R.id.editButton);
		deleteButton = (ImageButton) ve.findViewById(R.id.deleteButton);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onLongTouchAction(View v) {

		// If the view is not expanded...
		if (!v.findViewById(R.id.taskData).isShown()) {
			// ...remove the time text and make the buttons visible...
			timeText.setVisibility(View.INVISIBLE);
			editButton.setVisibility(View.VISIBLE);
			deleteButton.setVisibility(View.VISIBLE);

			// ...instantiate the animation...
			slide = new TranslateAnimation(-300, 0, 0, 0);
			slide.setDuration(250);
			slide.setFillAfter(true);
			slide.setFillEnabled(true);

			// ...and if the animation for this button is null (AKA previously
			// cleared or first time initiated), we set the animation once again
			// and start it.

			/*
			 * Should in fact check this for BOTH buttons, but since one is only
			 * showed with the other it would just be unnecessary code. It will
			 * suffice with just checking this for one of the buttons and by
			 * that just clearing one of them as well
			 */
			if (editButton.getAnimation() == null) {
				/*
				 * Same logic applies here as we SET and CLEAR the animation for
				 * the button.
				 */
				editButton.setAnimation(slide);

				editButton.startAnimation(slide);
				deleteButton.startAnimation(slide);
			}
		}
	}

}
