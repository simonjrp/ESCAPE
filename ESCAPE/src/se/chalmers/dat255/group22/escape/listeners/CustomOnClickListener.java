package se.chalmers.dat255.group22.escape.listeners;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;

import static se.chalmers.dat255.group22.escape.utils.Constants.DEFAULT_PAINT_FLAG;
import static se.chalmers.dat255.group22.escape.utils.Constants.NEW_ROW;

/**
 * An {@link android.view.View.OnClickListener} that will show additional
 * information about the object when it is clicked. Ideal for a
 * {@link se.chalmers.dat255.group22.escape.objects.ListObject}.
 * 
 * @author tholene
 */
public class CustomOnClickListener implements View.OnClickListener {

	private static String REMIND_ME_AT;
	private Context context;
	private TextView parent;
	private RelativeLayout taskDataLayout;
	private ListObject listObject;
	private boolean isExpanded;
	private DBHandler dbHandler;
	private TextView taskComment;
	private TextView taskPlace;
	private RelativeLayout taskTimeLayout;
	private TextView taskReminder;
	private ImageView taskReminderType;
	private SimpleDateFormat dateFormatSingleLine;
	private SimpleDateFormat dateFormatMultiLine;
	private SimpleDateFormat yearFormat;
	private SimpleDateFormat yearWithDateFormat;
	/**
	 * Create a new CustomOnClickListener.
	 * 
	 * @param listObject
	 *            the
	 *            {@link se.chalmers.dat255.group22.escape.objects.ListObject}
	 *            that contains the data to be viewed.
	 * @param parent
	 *            the TextView that the listener will be added to.
	 * @param taskDataLayout
	 *            the TextView containing the data that is associated with this
	 *            particular listObject
	 */
	public CustomOnClickListener(Context context, ListObject listObject,
			TextView parent, RelativeLayout taskDataLayout) {
		this.context = context;
		this.listObject = listObject;
		this.parent = parent;

		this.taskDataLayout = taskDataLayout;

		taskComment = (TextView) taskDataLayout
				.findViewById(R.id.taskDataComment);
		taskPlace = (TextView) taskDataLayout.findViewById(R.id.taskDataPlace);
		taskTimeLayout = (RelativeLayout) taskDataLayout
				.findViewById(R.id.taskDataTimeLayout);
		taskReminder = (TextView) taskDataLayout
				.findViewById(R.id.taskDataReminder);
		taskReminderType = (ImageView) taskDataLayout
				.findViewById(R.id.taskDataReminderType);
		isExpanded = false;
		dbHandler = new DBHandler(parent.getContext());
		REMIND_ME_AT = parent.getContext().getString(R.string.remind_me) + ":"
				+ NEW_ROW;
		dateFormatSingleLine = new SimpleDateFormat("EEE, dd MMM HH:mm",
				Locale.getDefault());
		dateFormatMultiLine = new SimpleDateFormat("EEE, dd MMM" + NEW_ROW
				+ "HH:mm", Locale.getDefault());
		yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
		yearWithDateFormat = new SimpleDateFormat("yyyy" + NEW_ROW
				+ "EEE, dd MMM" + NEW_ROW + "HH:mm", Locale.getDefault());
	}

	@Override
	public void onClick(View v) {
		isExpanded = !isExpanded;
		// This happens when the view is clicked...
		if (isExpanded) {
			// ... and the edit/remove buttons are not showing.
			if (!v.findViewById(R.id.deleteButton).isShown()) {
				Date start = null;
				Date end = null;

				// Set the state colors of the view
				ColorDrawable baseColor = new ColorDrawable();
				baseColor.setColor(context.getResources().getColor(
						R.color.white));

				StateListDrawable states = new StateListDrawable();
				states.addState(
						new int[]{android.R.attr.state_pressed},
						context.getResources().getDrawable(
								R.drawable.list_pressed_holo_dark));
				states.addState(StateSet.WILD_CARD, baseColor);
				v.setBackgroundDrawable(states);

				if (listObject.getComment() != null) {
					taskComment.setText(listObject.getComment());
					taskComment.setVisibility(View.VISIBLE);
					if (start != null)
						taskComment.setMinLines(4);
					else
						taskComment.setMinLines(0);
				} else {
					taskComment.setVisibility(View.GONE);
				}

				if (dbHandler.getPlace(listObject) != null) {
					taskPlace.setText(dbHandler.getPlace(listObject).getName());
					taskPlace.setVisibility(View.VISIBLE);
				} else {
					taskPlace.setVisibility(View.GONE);
				}

				// If the listObject has a time (AKA is an event)...
				if (listObject.getTime() != null) {
					// ... get the start and end dates...
					start = listObject.getTime().getStartDate();
					end = listObject.getTime().getEndDate();

				} else {
					start = null;
					end = null;
				}

				// If we have a time...
				if (start != null) {
					TextView startTime = (TextView) v
							.findViewById(R.id.taskDataStartTime2);
					TextView endTime = (TextView) v
							.findViewById(R.id.taskDataEndTime2);
					// Is the date this year?
					int year = Calendar.getInstance().get(Calendar.YEAR);
					if (Integer.parseInt(yearFormat.format(start)) == year) {
						startTime.setText(dateFormatMultiLine.format(start));
					} else {
						// If not, add the year in the string
						startTime.setText(yearWithDateFormat.format(start));
					}
					if (Integer.parseInt(yearFormat.format(end)) == year) {
						endTime.setText(dateFormatMultiLine.format(end));
					} else {
						endTime.setText(yearWithDateFormat.format(end));
					}
					taskTimeLayout.setVisibility(View.VISIBLE);
				} else {
					View separator = v.findViewById(R.id.taskTimeSeparator);
					separator.setVisibility(View.GONE);
					taskTimeLayout.setVisibility(View.GONE);
				}

				// Does the listObject have a reminder?
				if (dbHandler.getTimeAlarm(listObject) != null
						|| dbHandler.getGPSAlarm(listObject) != null) {
					View separator = v.findViewById(R.id.taskDataSeparator);
					separator.setVisibility(View.VISIBLE);
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(REMIND_ME_AT);
					// when it is removable
					// If it is a GPSAlarm...
					if (dbHandler.getTimeAlarm(listObject) == null
							&& dbHandler.getGPSAlarm(listObject) != null) {
						taskReminderType
								.setImageResource(R.drawable.location_place);
						stringBuilder.append(dbHandler.getGPSAlarm(listObject)
								.getAdress());
						// If it is a TimeAlarm...
					} else if (dbHandler.getTimeAlarm(listObject) != null
							&& dbHandler.getGPSAlarm(listObject) == null) {
						taskReminderType
								.setImageResource(R.drawable.device_access_alarms);
						taskReminderType.setVisibility(View.VISIBLE);
						stringBuilder.append(dateFormatSingleLine
								.format(dbHandler.getTimeAlarm(listObject)
										.getDate()));
					}
					taskReminder.setVisibility(View.VISIBLE);
					taskReminder.setText(stringBuilder.toString());
				} else {
					taskReminderType.setVisibility(View.GONE);
					taskReminder.setVisibility(View.GONE);
					View separator = v.findViewById(R.id.taskDataSeparator);
					separator.setVisibility(View.GONE);
				}

				taskDataLayout.setVisibility(View.VISIBLE);
				// Make the header label underlined to indicate that
				// "this is the selected item"
				parent.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

				TranslateAnimation slide = new TranslateAnimation(0, 0, -50, 0);
				slide.setDuration(150);
				slide.setFillAfter(true);
				slide.setFillEnabled(true);
				taskDataLayout.setAnimation(slide);
				slide.start();
				RelativeLayout timeView = (RelativeLayout) v
						.findViewById(R.id.start_time_task);
				timeView.setVisibility(View.INVISIBLE);

			}

			// If the edit/remove buttons are shown and you click the
			// listObject...
		} else if (v.findViewById(R.id.deleteButton).isShown()) {
			dismissButtons(v);
		} else {
			// If the view is only expanded, hide it again
			dismissDetails(v);

			// Set the state colors of the view
			ColorDrawable baseColor = new ColorDrawable();
			baseColor.setColor(context.getResources().getColor(R.color.white));

			StateListDrawable states = new StateListDrawable();
			states.addState(
					new int[]{android.R.attr.state_pressed},
					context.getResources().getDrawable(
							R.drawable.list_pressed_holo_dark));
			states.addState(StateSet.WILD_CARD, baseColor);
			v.setBackgroundDrawable(states);

		}

	}

	private void dismissButtons(View v) {
		// ...we basically hide the buttons and reset the view to it's
		// "Original State"
		// We also clear the animations to make sure they can run again if
		// invoked
        RelativeLayout timeView = (RelativeLayout) v
                .findViewById(R.id.start_time_task);
        timeView.setVisibility(View.VISIBLE);

		ImageButton editButton = (ImageButton) v.findViewById(R.id.editButton);
		editButton.setVisibility(View.INVISIBLE);
		editButton.clearAnimation();

		ImageButton deleteButton = (ImageButton) v
				.findViewById(R.id.deleteButton);
		deleteButton.setVisibility(View.INVISIBLE);
		deleteButton.clearAnimation();
	}

	private void dismissDetails(View v) {
		taskDataLayout.clearAnimation();
		taskDataLayout.setVisibility(View.GONE);
		parent.setPaintFlags(DEFAULT_PAINT_FLAG);
        RelativeLayout timeView = (RelativeLayout) v
                .findViewById(R.id.start_time_task);
        timeView.setVisibility(View.VISIBLE);
	}
}
