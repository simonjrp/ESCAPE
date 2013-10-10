package se.chalmers.dat255.group22.escape;

import java.sql.Date;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class NewTaskActivity extends Activity {

	static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private boolean remindMeClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		if (savedInstanceState == null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.add(R.id.container_new_task, new TaskDetailsFragment())
					.commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Make home button in actionbar work like pressing on backbutton
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {

		/* Set data of the object */

		// Title
		EditText taskTitle = (EditText) findViewById(R.id.task_title);
		String name = taskTitle.getText().toString();

		// Description
		EditText taskDesc = (EditText) findViewById(R.id.task_description);
		String comment = taskDesc.getText().toString();

		// Important
		CheckBox important = (CheckBox) findViewById(R.id.task_important);
		boolean importantTask = important.isChecked();

		// Category
		Spinner categories = (Spinner) findViewById(R.id.task_categories);
		String category = categories.getSelectedItem().toString();

		// Location
		EditText taskLocation = (EditText) findViewById(R.id.task_location);
		String location = taskLocation.getText().toString();

		// Time
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
		String dateFromString = "";
		String dateToString = "";
		if (relativeLayout.isShown()) {
			Spinner dateFrom = (Spinner) findViewById(R.id.date_from);
			dateFromString = dateFrom.getSelectedItem().toString();

			Spinner timeFrom = (Spinner) findViewById(R.id.time_from);
			// TODO DON'T FORGET THIS DECLARATION
			String timeFromString = timeFrom.getSelectedItem().toString();

			Spinner dateTo = (Spinner) findViewById(R.id.date_to);
			dateToString = dateTo.getSelectedItem().toString();

			Spinner timeTo = (Spinner) findViewById(R.id.time_to);
			// TODO DON'T FORGET THIS DECLARATION
			String timeToString = timeTo.getSelectedItem().toString();
		}

		// Time Alarm
		// TODO It's possible to select a time for the reminder that might've
		// passed already
		TimeAlarm timeAlarm = null;
		if (remindMeClicked) {
			Spinner reminderDateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);
			Spinner reminderTimeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			switch (reminderDateSpinner.getSelectedItemPosition()) {
			case 0:
				// date already correct
				break;
			case 1:
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.get(Calendar.DAY_OF_MONTH) + 1);
				break;
			case 2:
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.get(Calendar.DAY_OF_MONTH) + 2);
				break;
			default:
				// TODO do nothing
				break;
			}

			switch (reminderTimeSpinner.getSelectedItemPosition()) {
			case 0:
				calendar.set(Calendar.HOUR_OF_DAY, 9);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				break;
			case 1:
				calendar.set(Calendar.HOUR_OF_DAY, 13);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				break;
			case 2:
				calendar.set(Calendar.HOUR_OF_DAY, 17);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				break;
			case 3:
				calendar.set(Calendar.HOUR_OF_DAY, 20);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				break;
			default:
				// TODO do nothing
				break;
			}

			Date date = new Date(calendar.getTimeInMillis());
			timeAlarm = new TimeAlarm(0, date);
		}

		// GPS Alarm

		Category newCategory = new Category(category, "Random Color",
				"Another random Color");
		Place place = new Place(1, location);

		if (name.trim().length() != 0) {

			Time time;
			Date startDate = new Date(DAY_IN_MILLIS);
			Date endDate = new Date(DAY_IN_MILLIS);

			getTimeFromSpinners(dateFromString, startDate, dateToString,
					endDate);

			time = new Time(1, startDate, endDate);

			DBHandler dbHandler = new DBHandler(this);

			ListObject lo = new ListObject(1, name);
			if (comment.trim().length() != 0)
				lo.setComment(comment);
			lo.setImportant(importantTask);
			lo.addToCategory(newCategory);
			lo.setPlace(place);
			lo.setTime(time);
			long idLo = dbHandler.addListObject(lo);

			// add timealarm and create notification
			if (remindMeClicked) {
				NotificationHandler nf = new NotificationHandler(this);

				long idAlarm = dbHandler.addTimeAlarm(timeAlarm);

				dbHandler.addListObjectWithTimeAlarm(
						dbHandler.getListObject(idLo),
						dbHandler.getTimeAlarm(idAlarm));

				nf.addTimeReminder(dbHandler.getListObject(idLo));
			}

		} else {

		}
		super.onBackPressed();
	}

	public void onConvertEvent(View v) {

		v.setVisibility(View.INVISIBLE);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
		relativeLayout.setVisibility(View.VISIBLE);

		Spinner dateFromSpinner = (Spinner) findViewById(R.id.date_from);

		//
		/* From: DateSpinner */
		//
		ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter
				.createFromResource(this, R.array.test_dates,
						android.R.layout.simple_spinner_item);
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dateFromSpinner.setAdapter(dateAdapter);

		//
		/* From: TimeSpinner */
		//
		Spinner timeFromSpinner = (Spinner) findViewById(R.id.time_from);

		// create adapter for time spinner
		ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter
				.createFromResource(this, R.array.test_times,
						android.R.layout.simple_spinner_item);
		timeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeFromSpinner.setAdapter(timeAdapter);

		//
		/* To: TimeSpinner */
		//
		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);

		dateToSpinner.setAdapter(dateAdapter);

		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);

		timeToSpinner.setAdapter(timeAdapter);

	}

	public void onRemindMe(View v) {
		v.setVisibility(View.INVISIBLE);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.setReminderField);
		relativeLayout.setVisibility(View.VISIBLE);

		/* Add items to the spinners */
		/* Begin with the "TYPE" of reminder */
		int imgArr[] = { R.drawable.device_access_alarms,
				R.drawable.location_place };
		String[] strTypeArr = { getString(R.string.time_reminder),
				getString(R.string.location_reminder) };

		ReminderTypeAdapter typeAdapter = new ReminderTypeAdapter(this,
				R.layout.type_spinner_item, strTypeArr, imgArr);

		Spinner typeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
		typeSpinner.setAdapter(typeAdapter);

		/* Next up is the date of the reminder, simple enough */
		ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter
				.createFromResource(this, R.array.test_dates,
						android.R.layout.simple_spinner_item);
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner dateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);
		dateSpinner.setAdapter(dateAdapter);

		/*
		 * Last but not least, time of the reminder with a clarification of the
		 * time
		 */
		String[] strTimeArr = { "Morning", "Afternoon", "Evening", "Night" };
		ReminderTimeAdapter timeAdapter = new ReminderTimeAdapter(this,
				R.layout.time_spinner_item, strTimeArr);
		Spinner timeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);
		timeSpinner.setAdapter(timeAdapter);
		remindMeClicked = true;
	}

	public void onCancelReminder(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.setReminderField);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.remindMeField);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);
		remindMeClicked = false;
	}

	private void getTimeFromSpinners(String dateFromString, Date startDate,
			String dateToString, Date endDate) {
		if (dateFromString.equals("Today")) {
			startDate = new Date(System.currentTimeMillis());
		} else if (dateFromString.equals("Tomorrow")) {
			startDate = new Date(System.currentTimeMillis() + DAY_IN_MILLIS);
		} else if (dateFromString.equals("This week")) {
			startDate = new Date(System.currentTimeMillis()
					+ (2 * DAY_IN_MILLIS));
		} else if (dateFromString.equals("Pick a date...")) {
			startDate = null;
			Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG);
		} else {

		}

		if (dateToString.equals("Today")) {
			endDate = new Date(System.currentTimeMillis());
		} else if (dateToString.equals("Tomorrow")) {
			endDate = new Date(System.currentTimeMillis() + DAY_IN_MILLIS);
		} else if (dateToString.equals("This week")) {
			endDate = new Date(System.currentTimeMillis() + (2 * DAY_IN_MILLIS));
		} else if (dateToString.equals("Pick a date...")) {
			endDate = null;
			Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG);
		} else {

		}
	}
}
