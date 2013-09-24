package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;

import android.graphics.Color;
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
    private TextView taskData;
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
	public CustomOnClickListener(ListObject listObject, TextView childLabel, TextView taskData) {
		this.listObject = listObject;
		this.childLabel = childLabel;
        this.taskData = taskData;
		isExpanded = false;
	}

	@Override
	public void onClick(View v) {
		isExpanded = !isExpanded;
		if (isExpanded) {
            taskData.setVisibility(View.VISIBLE);
            taskData.setHeight(6*childLabel.getHeight()/8);
            taskData.setText(

                    (listObject.getComment() != null? listObject.getComment(): "") + "\n" +
                    (listObject.getPlace() != null? listObject.getPlace().getName() : "" ) + "\n"

            );
            taskData.setBackgroundColor(listObject.isImportant()? Color.CYAN : Color.BLUE);
		} else {
            taskData.setVisibility(View.INVISIBLE);
            taskData.setHeight(0);
		}
	}

}
