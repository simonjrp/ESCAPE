package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.graphics.Paint;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

/**
 * An {@link android.view.View.OnClickListener} that will show additional
 * information about the object when clicked.
 * 
 * @author tholene
 */
public class CustomOnClickListener implements View.OnClickListener {
	private final static String NEW_ROW = "\n";
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
			if (!v.findViewById(R.id.editButton).isShown()) {
				Time start = new Time();
				start.set((dbHandler.getTime(listObject) != null ? (dbHandler
						.getTime(listObject).getStartDate().getTime()) : 0));
				Time end = new Time();
				end.set((dbHandler.getTime(listObject) != null ? (dbHandler
						.getTime(listObject).getEndDate().getTime()) : 0));
				StringBuilder builder = new StringBuilder();

				taskData.setText(

				(listObject.getComment() != null ? listObject.getComment()
						+ NEW_ROW : "")
						+ (dbHandler.getPlace(listObject) != null ? dbHandler
								.getPlace(listObject).getName() + NEW_ROW : "")
						+ (start != null ? start.format("%H:%M") + "-" : "")
						+ (end != null ? end.format("%H:%M") + NEW_ROW : "")
						+ (dbHandler.getTimeAlarm(listObject) != null
								? "Remind me at "
										+ dbHandler.getTimeAlarm(listObject)
												.getDate().toString()
								: "")

				);
				if (taskData.getText() != null)
					taskData.setVisibility(View.VISIBLE);

				taskData.setHeight(taskData.getLineCount()
						* taskData.getLineHeight() + 5);

				taskData.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
				childLabel.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
			}
		} else {
			taskData.setVisibility(View.INVISIBLE);
			taskData.setHeight(0);
			childLabel.setPaintFlags(1);
		}

	}

}
