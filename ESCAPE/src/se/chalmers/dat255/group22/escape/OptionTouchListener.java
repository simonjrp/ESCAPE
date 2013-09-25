package se.chalmers.dat255.group22.escape;

import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class OptionTouchListener extends LongTouchActionListener {
	private boolean alreadyExpanded;
	private ListObject listObject;
	private TextView childLabel;
	
	public OptionTouchListener(Context context, ListObject listObject, TextView childLabel) {
		super(context);
		this.listObject = listObject;
		this.childLabel = childLabel;
	}

	@Override
	public void onClick(View v) {
		alreadyExpanded = !alreadyExpanded;
		if (alreadyExpanded) {
			// TODO temporary ugly fix for fist release
			if (listObject.getComment() == null) {
				childLabel.setText(listObject.getName() + "\n"
						+ listObject.toString());
			} else {
				childLabel.setText(listObject.getName() + "\n"
						+ listObject.getComment());

			}
		} else {
			childLabel.setText(listObject.getName());
		}	}

	@Override
	public void onLongTouchAction(View v) {
		//TODO add show edit buttons
	}

}
