package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.ListObject;
import android.view.View;
import android.widget.TextView;

/**
 * An {@link android.view.View.OnClickListener} that will show additional
 * information about the object when clicked.
 * 
 * @author tholene
 */
public class CustomOnClickListener implements View.OnClickListener {

	private TextView childLabel;
	private ListObject listObject;
	private boolean isExpanded;

	/**
	 * Create a new CustomOnClickListener.
	 * 
	 * @param listObject
	 *            the {@link se.chalmers.dat255.group22.escape.ListObject} that
	 *            contains the data to be viewed.
	 * @param childLabel
	 *            the TextView that the listener will be added to.
	 */
	public CustomOnClickListener(ListObject listObject, TextView childLabel) {
		this.listObject = listObject;
		this.childLabel = childLabel;
		isExpanded = false;
	}

	@Override
	public void onClick(View v) {
		isExpanded = !isExpanded;
		if (isExpanded) {
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
		}
	}

}
