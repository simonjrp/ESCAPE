package se.chalmers.dat255.group22.escape;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import se.chalmers.dat255.group22.escape.adapters.SpinnerCategoryAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerDayAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerIntervalAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTimeAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTypeAdapter;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * The activity for adding a new task or event.
 * 
 * @author tholene
 */
public class NewTaskActivity extends Activity {

	private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private boolean hasReminder;
	private boolean isRepeating;
	private boolean isEvent;
	private TimeAlarm customTimeAlarmDate;

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
	protected void onResume() {
		super.onResume();

		// Set up the spinner for different categories
		Spinner categorySpinner = (Spinner) this
				.findViewById(R.id.task_categories);

		// TODO This array should be grabbed from the database
		String[] categories = { "Life", "Work", "School", "Custom category..." };

		// The DayAdapter only makes use of simple strings and presents its
		// items the way we want the categories to be presented. It would be
		// unnecessary to create an identical adapter just for the categories,
		// so we just use this one instead

		SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(
				this, R.layout.simple_spinner_item, categories);

		categorySpinner.setAdapter(categoryAdapter);
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
		TimeAlarm timeAlarm = null;
		if (hasReminder) {
			// Gets the spinners and their adapters.

			Spinner reminderDateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);
			SpinnerDayAdapter reminderDateAdapter = (SpinnerDayAdapter) reminderDateSpinner
					.getAdapter();
			Spinner reminderTimeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);
			SpinnerTimeAdapter reminderTimeAdapter = (SpinnerTimeAdapter) reminderTimeSpinner
					.getAdapter();

			// Retrieves date.
			Calendar dateCalendar = Calendar.getInstance();
			Date date = reminderDateAdapter.getData(reminderDateSpinner
					.getSelectedItemPosition());
			dateCalendar.setTime(date);

			// Retrieves time.
			Calendar timeCalendar = Calendar.getInstance();
			Date time = reminderTimeAdapter.getData(reminderTimeSpinner
					.getSelectedItemPosition());
			timeCalendar.setTime(time);

			// Merges date and time calendar
			dateCalendar.set(Calendar.HOUR_OF_DAY,
					timeCalendar.get(Calendar.HOUR_OF_DAY));
			dateCalendar
					.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

			// Generates final Date object from merged date and time Calendar
			// object.
			Date finalDate = new Date(dateCalendar.getTimeInMillis());
			Toast.makeText(this, finalDate.toString(), Toast.LENGTH_LONG)
					.show();

			timeAlarm = new TimeAlarm(0, finalDate);
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

			ListObject lo = new ListObject(1, name);
			if (comment.trim().length() != 0)
				lo.setComment(comment);
			lo.setImportant(importantTask);
			lo.addToCategory(newCategory);
			lo.setPlace(place);
			lo.setTime(time);
			lo.setTimeAlarm(timeAlarm);

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
		dbHandler.addListObject(lo);

		if (lo.getTimeAlarm() != null) {
			dbHandler.addTimeAlarm(lo.getTimeAlarm());
			dbHandler.addListObjectWithTimeAlarm(lo, lo.getTimeAlarm());
			// creates a notification!
			if (hasReminder) {
				// TODO fix notifications!
				NotificationHandler nf = new NotificationHandler(this);
				nf.addReminderNotification(lo);
			}
		}
		if (lo.getTime() != null) {
			dbHandler.addTime(lo.getTime());
			dbHandler.addListObjectsWithTime(lo, lo.getTime());
		}
		if (lo.getCategories() != null) {
			for (Category cat : lo.getCategories()) {
				dbHandler.addCategory(cat);
				dbHandler.addCategoryWithListObject(cat, lo);
			}
		}
		if (lo.getGpsAlarm() != null) {
			dbHandler.addGPSAlarm(lo.getGpsAlarm());
			dbHandler.addListObjectWithGPSAlarm(lo, lo.getGpsAlarm());
		}
		if (lo.getPlace() != null) {
			dbHandler.addPlace(lo.getPlace());
			dbHandler.addListObjectWithPlace(lo, lo.getPlace());
		}
	}

	/**
	 * When the "Convert to Event" button is pressed, this method is called and
	 * the button is switched to a view containing the required forms for an
	 * event.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onConvertEvent(View v) {

		v.setVisibility(View.INVISIBLE);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
		relativeLayout.setVisibility(View.VISIBLE);

		Spinner dateFromSpinner = (Spinner) findViewById(R.id.date_from);
		dateFromSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						dateFromSpinner.getId()));

		//
		/* From: DateSpinner */
		//
		// Array containing different days for an event
		ArrayList<String> strDayList = new ArrayList<String>();
		strDayList.add(getString(R.string.todayLabel));
		strDayList.add(getString(R.string.tomorrowLabel));
		strDayList.add(getString(R.string.thisWeekLabel));
		strDayList.add(getString(R.string.pickDayLabel));

		SpinnerDayAdapter dayAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, strDayList);
		dateFromSpinner.setAdapter(dayAdapter);

		//
		/* From: TimeSpinner */
		//
		Spinner timeFromSpinner = (Spinner) findViewById(R.id.time_from);
		timeFromSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.TIME_SPINNER,
						timeFromSpinner.getId()));

		// Array containing different times for an event
		ArrayList<String> strTimeArr = new ArrayList<String>();
		strTimeArr.add(getString(R.string.morning));
		strTimeArr.add(getString(R.string.afternoon));
		strTimeArr.add(getString(R.string.evening));
		strTimeArr.add(getString(R.string.night));
		strTimeArr.add(getString(R.string.pickTimeLabel));

		SpinnerTimeAdapter timeAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, strTimeArr);
		timeFromSpinner.setAdapter(timeAdapter);

		//
		/* To: TimeSpinner */
		//
		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);

		dateToSpinner.setAdapter(dayAdapter);
		dateToSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						dateToSpinner.getId()));

		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);
		timeToSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.TIME_SPINNER,
						timeToSpinner.getId()));

		timeToSpinner.setAdapter(timeAdapter);

		isEvent = true;

	}

	/**
	 * When the "Remind me" button is pressed, this method is called and the
	 * button is switched to a view containing the required forms for adding a
	 * reminder.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onRemindMe(View v) {
		v.setVisibility(View.INVISIBLE);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.setReminderField);
		relativeLayout.setVisibility(View.VISIBLE);

		/*
		 * Add items to the spinners
		 * 
		 * 
		 * 
		 * 
		 * Begin with the "TYPE" of reminder
		 */
		// An array containing the images for a time and location reminder
		int imgArr[] = { R.drawable.device_access_alarms,
				R.drawable.location_place };

		// An array containing strings to be associated with each image
		String[] strTypeArr = { getString(R.string.time_reminder),
				getString(R.string.location_reminder) };

		SpinnerTypeAdapter typeAdapter = new SpinnerTypeAdapter(this,
				R.layout.type_spinner_item, strTypeArr, imgArr);

		Spinner typeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
		typeSpinner.setAdapter(typeAdapter);

		/* Next up is the date of the reminder, simple enough */
		// ArrayList containing different days for an event
		ArrayList<String> strDayList = new ArrayList<String>();
		strDayList.add(getString(R.string.todayLabel));
		strDayList.add(getString(R.string.tomorrowLabel));
		strDayList.add(getString(R.string.thisWeekLabel));
		strDayList.add(getString(R.string.pickDayLabel));

		SpinnerDayAdapter dayAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, strDayList);

		Spinner dateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);
		dateSpinner.setAdapter(dayAdapter);
		dateSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						dateSpinner.getId()));

		/*
		 * Last but not least, time of the reminder with a clarification of the
		 * time
		 */
		// Array containing different times for an event
		ArrayList<String> strTimeArr = new ArrayList<String>();
		strTimeArr.add(getString(R.string.morning));
		strTimeArr.add(getString(R.string.afternoon));
		strTimeArr.add(getString(R.string.evening));
		strTimeArr.add(getString(R.string.night));
		strTimeArr.add(getString(R.string.pickTimeLabel));

		SpinnerTimeAdapter timeAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, strTimeArr);

		Spinner timeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);
		timeSpinner.setAdapter(timeAdapter);
		timeSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.TIME_SPINNER,
						timeSpinner.getId()));
		hasReminder = true;
	}

	/**
	 * When the "Repeat" button is pressed, this method is called and the button
	 * is switched to a view containing the required forms for a repeating
	 * event.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onRepeat(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.repeatInactiveLayout);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.repeatActiveLayout);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);

		// Array of strings for different intervals
		String[] strIntervalArr = { getString(R.string.oneWeekLabel),
				getString(R.string.twoWeeksLabel),
				getString(R.string.threeWeeksLabel),
				getString(R.string.oneMonthLabel) };

		SpinnerIntervalAdapter intervalAdapter = new SpinnerIntervalAdapter(
				this, R.layout.simple_spinner_item, strIntervalArr);

		Spinner repeatIntervalSpinner = (Spinner) findViewById(R.id.repeatIntervalSpinner);

		repeatIntervalSpinner.setAdapter(intervalAdapter);

		isRepeating = true;
	}

	/**
	 * When the "X" button next to the event field is pressed, this method is
	 * called and the layout is restored to its previous state.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelEvent(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
		Button toBeShownButton = (Button) findViewById(R.id.task_add_reminder);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownButton.setVisibility(View.VISIBLE);

		isEvent = false;
	}

	/**
	 * When the "X" button next to the reminder field is pressed, this method is
	 * called and the layout is restored to its previous state.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelReminder(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.setReminderField);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.remindMeField);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);
		hasReminder = false;
	}

	/**
	 * When the "X" button next to the repeat field is pressed, this method is
	 * called and the layout is restored to its previous state.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelRepeat(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.repeatActiveLayout);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.repeatInactiveLayout);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);

		isRepeating = false;
	}

	private void getTimeFromSpinners(String dateFromString, Date startDate,
			String dateToString, Date endDate) {
		if (dateFromString.equals(getString(R.string.todayLabel))) {
			startDate = new Date(System.currentTimeMillis());
		} else if (dateFromString.equals(getString(R.string.tomorrowLabel))) {
			startDate = new Date(System.currentTimeMillis() + DAY_IN_MILLIS);
		} else if (dateFromString.equals(getString(R.string.thisWeekLabel))) {
			startDate = new Date(System.currentTimeMillis()
					+ (2 * DAY_IN_MILLIS));
		} else if (dateFromString.equals(getString(R.string.pickDayLabel))) {
			startDate = null;
			Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG).show();
		} else {
		}

		if (dateToString.equals(getString(R.string.todayLabel))) {
			endDate = new Date(System.currentTimeMillis());
		} else if (dateToString.equals(getString(R.string.tomorrowLabel))) {
			endDate = new Date(System.currentTimeMillis() + DAY_IN_MILLIS);
		} else if (dateToString.equals(getString(R.string.thisWeekLabel))) {
			endDate = new Date(System.currentTimeMillis() + (2 * DAY_IN_MILLIS));
		} else if (dateToString.equals(getString(R.string.pickDayLabel))) {
			endDate = null;
			Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG).show();
		} else {

		}
	}

	public void setDate(int year, int month, int day) {
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.YEAR, year);
		tempCalendar.set(Calendar.MONTH, month);
		tempCalendar.set(Calendar.DAY_OF_MONTH, day);
		customTimeAlarmDate = new TimeAlarm(0, new Date(
				tempCalendar.getTimeInMillis()));
	}

	public void setTime(int hour, int minute) {

	}
}
