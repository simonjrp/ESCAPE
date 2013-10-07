package se.chalmers.dat255.group22.escape.listeners;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.graphics.Paint;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * An {@link android.view.View.OnClickListener} that will show additional
 * information about the object when it is clicked. Ideal for a
 * {@link se.chalmers.dat255.group22.escape.objects.ListObject}.
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
	 * @param taskData
	 *            the TextView containing the data that is associated with this
	 *            particular listObject
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

		// This happens when the view is clicked...
		if (isExpanded) {
			// ... and the edit/remove buttons are not showing.
			if (!v.findViewById(R.id.editButton).isShown()) {
				Time start = new Time();
				Time end = new Time();

				// If the listObject has a time (AKA is an event)...
				if (dbHandler.getTime(listObject) != null) {
					// ... get the start and end time...
					long startTime = dbHandler.getTime(listObject)
							.getStartDate().getTime();
					long endTime = dbHandler.getTime(listObject).getEndDate()
							.getTime();
					// ... and set them to the local variables.
					start.set(startTime);
					end.set(endTime);
				} else {
					start = null;
					end = null;
				}

				// Start building the string that will contain the various data
				// associated with this particular listObject.
				StringBuilder builder = new StringBuilder();

				if (listObject.getComment() != null)
					builder.append(listObject.getComment());
				if (dbHandler.getPlace(listObject) != null)
					// No "    " or places as such are allowed.
					if (dbHandler.getPlace(listObject).getName().trim()
							.length() != 0)
						builder.append(NEW_ROW
								+ dbHandler.getPlace(listObject).getName());
				if (start != null)
					// Format the start time to HH:MM...
					builder.append(NEW_ROW + start.format("%H:%M"));
				if (end != null)
					// ...and format the end time to HH:MM
					builder.append("-" + end.format("%H:%M"));
				if (dbHandler.getTimeAlarm(listObject) != null)
					// If this listObject has a reminder, present is aswell
					builder.append(NEW_ROW
							+ "Remind me at "
							+ dbHandler.getTimeAlarm(listObject).getDate()
									.toString());

				// Set the string to textView
				taskData.setText(builder.toString());

				// If the string was built and contained something...
				if (taskData.getText() != null) {
					// Make the textView visible and set the height relative to
					// the amount of lines
					taskData.setVisibility(View.VISIBLE);

					taskData.setHeight(taskData.getLineCount()
							* taskData.getLineHeight() + 5);

					// Make the header label underlined to indicate that
					// "this is the selected item"
					// Also "style" the text of the details somewhat
					taskData.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
					childLabel.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
				}
			}
			// If the edit/remove buttons are shown and you click the
			// listObject...
		} else if (v.findViewById(R.id.editButton).isShown()) {
			// ...we basically hide the buttons and reset the view to it's
			// "Original State"
			// We also clear the animations to make sure they can run again if
			// invoked
			TextView timeText = (TextView) v.findViewById(R.id.startTimeTask);
			timeText.setVisibility(View.VISIBLE);

			ImageButton editButton = (ImageButton) v
					.findViewById(R.id.editButton);
			editButton.setVisibility(View.INVISIBLE);
			editButton.clearAnimation();

			ImageButton deleteButton = (ImageButton) v
					.findViewById(R.id.deleteButton);
			deleteButton.setVisibility(View.INVISIBLE);
			deleteButton.clearAnimation();
		} else {
			// Should be unreachable, so do nothing
		}

	}

}
