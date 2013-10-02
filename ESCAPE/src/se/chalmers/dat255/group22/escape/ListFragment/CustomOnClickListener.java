package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.graphics.Paint;
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
	private DBHandler dbHandler;

	/**
	 * Create a new CustomOnClickListener.
	 * 
	 * @param listObject
	 *            the
	 *            {@link se.chalmers.dat255.group22.escape.objects.ListObject}
	 *            that contains the data to be viewed.
	 * @param childLabel
	 *            the TextView that the listener will be added to.
	 */
	public CustomOnClickListener(ListObject listObject, TextView childLabel,
			TextView taskData) {
		this.listObject = listObject;
		this.childLabel = childLabel;
		this.taskData = taskData;
		isExpanded = false;
		dbHandler = new DBHandler(childLabel.getContext());
	}

	@Override
	public void onClick(View v) {
        isExpanded = !isExpanded;
        if (isExpanded) {
            taskData.setText(

                    (listObject.getComment() != null ? "* " + listObject.getComment() + "\n" : "No Comment\n") +
                            (listObject.getPlace() != null ? "* " + listObject.getPlace().getName() + "\n" : "No place")

            );

            if (taskData.getText() != null)
                taskData.setVisibility(View.VISIBLE);

            taskData.setHeight(taskData.getLineCount()
                    * taskData.getLineHeight() + 5);

            taskData.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            childLabel.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        } else {
            taskData.setVisibility(View.INVISIBLE);
            taskData.setHeight(0);
            childLabel.setPaintFlags(1);
        }
	}

}
