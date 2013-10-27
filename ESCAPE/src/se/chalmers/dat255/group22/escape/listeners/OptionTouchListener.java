package se.chalmers.dat255.group22.escape.listeners;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
	private RelativeLayout timeLayout;
	private ImageButton editButton;
	private ImageButton deleteButton;
	private ListObject listObject;
	private Context context;

	/**
	 * Create a new OptionTouchListener.
	 * 
	 * @param context
	 *            the context to be used.
	 * @param ve
	 *            the view that makes use of the listener.
	 */
	public OptionTouchListener(Context context, View ve, ListObject listObject) {
		super(context);

		timeLayout = (RelativeLayout) ve
				.findViewById(R.id.list_object_collapsed_time);
		editButton = (ImageButton) ve.findViewById(R.id.edit_button);
		deleteButton = (ImageButton) ve.findViewById(R.id.delete_button);
		this.context = context;
		this.listObject = listObject;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onLongTouchAction(View v) {

		// If the view is not expanded...
		if (!v.findViewById(R.id.list_object_expanded_layout).isShown()) {
			// ...remove the time text and make the buttons visible...
			timeLayout.setVisibility(View.INVISIBLE);
			// If this is an autogenerated object, do show edit-button!!
			if (listObject.getCategories().get(0).getName()
					.equals(context.getString(R.string.autogenerated)))
				editButton.setVisibility(View.INVISIBLE);
			else
				editButton.setVisibility(View.VISIBLE);
			deleteButton.setVisibility(View.VISIBLE);

			// ...instantiate the animation...
			slide = new TranslateAnimation(300, 0, 0, 0);
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
			if (deleteButton.getAnimation() == null) {
				/*
				 * Same logic applies here as we SET and CLEAR the animation for
				 * the button.
				 */
				// If this is an autogenerated object, do show edit-button!!
				if (listObject.getCategories().get(0).getName()
						.equals(context.getString(R.string.autogenerated))) {
					deleteButton.setAnimation(slide);
					deleteButton.startAnimation(slide);
				} else {
					editButton.setAnimation(slide);
					editButton.startAnimation(slide);
					deleteButton.setAnimation(slide);
					deleteButton.startAnimation(slide);
				}
			}
		}
	}

}
