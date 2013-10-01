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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class NewTaskActivity extends Activity {

	static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private boolean remindMeClicked;
	private boolean repeatClicked;
	private boolean isEvent;

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
			case android.R.id.home :
				onBackPressed();
				return true;
			default :
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
				case 0 :
					// date already correct
					break;
				case 1 :
					calendar.set(Calendar.DAY_OF_MONTH,
							calendar.get(Calendar.DAY_OF_MONTH) + 1);
					break;
				case 2 :
					calendar.set(Calendar.DAY_OF_MONTH,
							calendar.get(Calendar.DAY_OF_MONTH) + 2);
					break;
				default :
					// TODO do nothing
					break;
			}

			switch (reminderTimeSpinner.getSelectedItemPosition()) {
				case 0 :
					calendar.set(Calendar.HOUR_OF_DAY, 9);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					break;
				case 1 :
					calendar.set(Calendar.HOUR_OF_DAY, 13);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					break;
				case 2 :
					calendar.set(Calendar.HOUR_OF_DAY, 17);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					break;
				case 3 :
					calendar.set(Calendar.HOUR_OF_DAY, 20);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					break;
				default :
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

			ListObject lo = new ListObject(1, name);
			if (comment.trim().length() != 0)
				lo.setComment(comment);
            else
                lo.setComment(null);
			lo.setImportant(importantTask);
			lo.addToCategory(newCategory);
			lo.setPlace(place);
			lo.setTimeAlarm(timeAlarm);
            //lo.setGpsAlarm(...);
			if (isEvent) {
				Time time = getTimeFromSpinners(dateFromString, dateToString);
				lo.setTime(time);
			}

			saveToDatabase(lo);

		} else {

		}
		super.onBackPressed();
	}

	/**
	 * Save a complete ListObject into the database
	 * 
	 * @param lo
	 *            ListObject to save
	 */
	public void saveToDatabase(ListObject lo) {
		DBHandler dbHandler = new DBHandler(this);
		long objId = dbHandler.addListObject(lo);
		long tmpId;

		if (lo.getTimeAlarm() != null) {
			tmpId = dbHandler.addTimeAlarm(lo.getTimeAlarm());
			dbHandler.addListObjectWithTimeAlarm(
					dbHandler.getListObject(objId),
					dbHandler.getTimeAlarm(tmpId));
			// creates a notification!
			if (remindMeClicked) {
				// TODO fix notifications!
				// NotificationHandler nf = new NotificationHandler(this);
				// nf.addReminderNotification(dbHandler.getListObject(objId));
			}
		}
		if (lo.getTime() != null) {
			tmpId = dbHandler.addTime(lo.getTime());
			dbHandler.addListObjectsWithTime(dbHandler.getListObject(objId),
					dbHandler.getTime(tmpId));
		}
		if (lo.getCategories() != null) {
			for (Category cat : lo.getCategories()) {
				tmpId = dbHandler.addCategory(cat);
				// TODO do this work?
				dbHandler.addCategoryWithListObject(cat,
						dbHandler.getListObject(objId));
			}
		}
		// TODO fix gps alarm setter
		if (lo.getGpsAlarm() != null) {
			tmpId = dbHandler.addGPSAlarm(lo.getGpsAlarm());
			// dbHandler.addListObjectWithGPSAlarm(dbHandler.getListObject(objId),
			// dbHandler.getGPSAlarm(tmpId));
		}
		// TODO fix place setter
		if (lo.getPlace() != null) {
			tmpId = dbHandler.addPlace(lo.getPlace());
			// dbHandler.addListObjectWithPlace(dbHandler.getListObject(objId),
			// dbHandler.getPlace(tmpId));
		}
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
		String[] strTimeArr = {"Morning", "Afternoon", "Evening", "Night",
				"..."};
		ReminderTimeAdapter timeAdapter = new ReminderTimeAdapter(this,
				R.layout.time_spinner_item, strTimeArr);
		timeFromSpinner.setAdapter(timeAdapter);

		//
		/* To: TimeSpinner */
		//
		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);

		dateToSpinner.setAdapter(dateAdapter);

		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);

		timeToSpinner.setAdapter(timeAdapter);

		isEvent = true;

	}

	public void onRemindMe(View v) {
		v.setVisibility(View.INVISIBLE);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.setReminderField);
		relativeLayout.setVisibility(View.VISIBLE);

		/* Add items to the spinners */
		/* Begin with the "TYPE" of reminder */
		int imgArr[] = {R.drawable.device_access_alarms,
				R.drawable.location_place};
		String[] strTypeArr = {getString(R.string.time_reminder),
				getString(R.string.location_reminder)};

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
		String[] strTimeArr = {"Morning", "Afternoon", "Evening", "Night",
				"..."};
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

	public void onCancelRepeat(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.repeatActiveLayout);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.repeatInactiveLayout);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);

		repeatClicked = false;
	}

	public void onCancelEvent(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
		Button toBeShownButton = (Button) findViewById(R.id.task_add_reminder);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownButton.setVisibility(View.VISIBLE);

		isEvent = false;
	}

	public void onRepeat(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.repeatInactiveLayout);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.repeatActiveLayout);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);

		ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter
				.createFromResource(this, R.array.test_intervals,
						android.R.layout.simple_spinner_item);
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner repeatIntervalSpinner = (Spinner) findViewById(R.id.repeatIntervalSpinner);
		repeatIntervalSpinner.setAdapter(dateAdapter);

		repeatClicked = true;
	}

	private Time getTimeFromSpinners(String dateFromString, String dateToString) {
		Date startDate;
		Date endDate;
		if (dateFromString.equals(getString(R.string.todayLabel))) {
			startDate = new Date(System.currentTimeMillis());
		} else if (dateFromString.equals(getString(R.string.tomorrowLabel))) {
			startDate = new Date(System.currentTimeMillis() + DAY_IN_MILLIS);
		} else if (dateFromString.equals(getString(R.string.thisWeekLabel))) {
			startDate = new Date(System.currentTimeMillis()
					+ (2 * DAY_IN_MILLIS));
		} else if (dateFromString.equals(getString(R.string.pickDateLabel))) {
			startDate = null;
			Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG).show();
		} else {
            startDate = null;
		}

		if (dateToString.equals(getString(R.string.todayLabel))) {
			endDate = new Date(System.currentTimeMillis());
		} else if (dateToString.equals(getString(R.string.tomorrowLabel))) {
			endDate = new Date(System.currentTimeMillis() + DAY_IN_MILLIS);
		} else if (dateToString.equals(getString(R.string.thisWeekLabel))) {
			endDate = new Date(System.currentTimeMillis() + (2 * DAY_IN_MILLIS));
		} else if (dateToString.equals(getString(R.string.pickDateLabel))) {
			endDate = null;
			Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG).show();
		} else {
            endDate = null;
		}
        if (startDate == null && endDate == null)
            return null;

		return new Time(1, startDate, endDate);
	}
}
