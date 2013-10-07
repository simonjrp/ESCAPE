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
import se.chalmers.dat255.group22.escape.fragments.TaskDetailsFragment;
import se.chalmers.dat255.group22.escape.listeners.OnItemSelectedSpinnerListener;
import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

/**
 * An activity used for creating a new task
 * 
 * @author Erik, Carl, Simon Persson
 */
public class NewTaskActivity extends Activity {

	private boolean hasReminder;
	private boolean isTimeReminder;
	private boolean isLocationReminder;
	private boolean isRepeating;
	private boolean isEvent;
	private boolean editing;
	private String nextWeekSameDay;

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
		Calendar tempCalendar = Calendar.getInstance();
		int weekday = tempCalendar.get(Calendar.DAY_OF_WEEK);
		String[] weekDays = getResources().getStringArray(R.array.weekdays);
		nextWeekSameDay = getResources().getString(R.string.nextweeksameday)
				+ " " + weekDays[weekday];
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Set up the spinner for different categories
		Spinner categorySpinner = (Spinner) this
				.findViewById(R.id.task_categories);

		// TODO This array should be grabbed from the database
		ArrayList<String> categories = new ArrayList<String>();

		categories.add(getString(R.string.custom_category));

		// The DayAdapter only makes use of simple strings and presents its
		// items the way we want the categories to be presented. It would be
		// unnecessary to create an identical adapter just for the categories,
		// so we just use this one instead

		SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(
				this, R.layout.simple_spinner_item, categories);

		categorySpinner.setAdapter(categoryAdapter);

		Intent intent = getIntent();

		// TODO Fix this ugly check?
		if (intent.getFlags() == 1) {
			Bundle bundle = intent.getBundleExtra("Edit Task");
			if (bundle != null) {
				// TODO Should keep track of wether this is a new event or an
				// TODO event in editing, maybe a better way for this?
				DBHandler dbHandler = new DBHandler(this);
				long id = bundle.getInt("ID");
				ListObject listObject = dbHandler.getListObject(id);

				editing = true;

				// References to all the input fields
				EditText title = (EditText) findViewById(R.id.task_title);
				Spinner category = (Spinner) findViewById(R.id.task_categories);
				EditText description = (EditText) findViewById(R.id.task_description);
				EditText location = (EditText) findViewById(R.id.task_location);
				CheckBox important = (CheckBox) findViewById(R.id.task_important);

				Spinner remindType = (Spinner) findViewById(R.id.reminderTypeSpinner);
				Spinner remindDate = (Spinner) findViewById(R.id.reminderDateSpinner);
				Spinner remindTime = (Spinner) findViewById(R.id.reminderTimeSpinner);

				Spinner dateFrom = (Spinner) findViewById(R.id.date_from);
				Spinner dateTo = (Spinner) findViewById(R.id.date_to);
				Spinner timeFrom = (Spinner) findViewById(R.id.time_from);
				Spinner timeTo = (Spinner) findViewById(R.id.time_to);

				CheckBox mondayBox = (CheckBox) findViewById(R.id.mondayBox);
				CheckBox tuesdayBox = (CheckBox) findViewById(R.id.tuesdayBox);
				CheckBox wednesdayBox = (CheckBox) findViewById(R.id.wednesdayBox);
				CheckBox thursdayBox = (CheckBox) findViewById(R.id.thursdayBox);
				CheckBox fridayBox = (CheckBox) findViewById(R.id.fridayBox);
				CheckBox saturdayBox = (CheckBox) findViewById(R.id.saturdayBox);
				CheckBox sundayBox = (CheckBox) findViewById(R.id.sundayBox);
				Spinner interval = (Spinner) findViewById(R.id.repeatIntervalSpinner);

				// Get data from the ListObject that "called" the activity

				String nameString = listObject.getName().toString();
				String descriptionString = "";
				String locationString = "";
				String timeAlarmString = "";
				String timeStartString = "";
				String timeEndString = "";
				if (listObject.getComment() != null)
					descriptionString = listObject.getComment();
				if (dbHandler.getPlace(listObject) != null)
					locationString = dbHandler.getPlace(listObject).getName();
				Boolean isImportant = listObject.isImportant();
				if (dbHandler.getTimeAlarm(listObject) != null)
					timeAlarmString = dbHandler.getTimeAlarm(listObject)
							.getDate().toString();
				if (dbHandler.getTime(listObject) != null)
					timeStartString = dbHandler.getTime(listObject)
							.getStartDate().toString();
				if (dbHandler.getTime(listObject) != null)
					timeEndString = dbHandler.getTime(listObject).getEndDate()
							.toString();

				// TODO This should also be saved into the object/database when
				// TODO pausing/destroying the activity!
				title.setText(nameString);
				if (descriptionString != null) {
					if (descriptionString.trim().length() != 0)
						description.setText(descriptionString);
				}
				if (locationString != null) {
					if (locationString.trim().length() != 0)
						location.setText(locationString);
				}
				important.setChecked(isImportant);

				// TODO FIX TIMES AND STUFF
			}
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
		TimeAlarm timeAlarm = null;
		if (hasReminder) {
			// Gets the spinners and their adapters.

			Spinner reminderDateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);
			SpinnerDayAdapter reminderDateAdapter = (SpinnerDayAdapter) reminderDateSpinner
					.getAdapter();
			Spinner reminderTimeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);
			SpinnerTimeAdapter reminderTimeAdapter = (SpinnerTimeAdapter) reminderTimeSpinner
					.getAdapter();

			Date finalDate = getOneDate(
					reminderDateAdapter.getData(reminderDateSpinner
							.getSelectedItemPosition()),
					reminderTimeAdapter.getData(reminderTimeSpinner
							.getSelectedItemPosition()));
			Calendar tempCalendar = Calendar.getInstance();
			tempCalendar.setTime(finalDate);

			timeAlarm = new TimeAlarm(0, finalDate);
		}

		// GPS Alarm

		Category newCategory = new Category(category, "Random Color",
				"Another random Color");
		Place place = new Place(1, location);

		// If a name is set create ListObject
		if (name.trim().length() != 0) {
			ListObject lo = new ListObject(1, name);

			if (comment.trim().length() != 0)
				lo.setComment(comment);
			else
				// A comment MUST be set even if only null!
				lo.setComment(null);

			lo.setImportant(importantTask);
			lo.addToCategory(newCategory);
			lo.setPlace(place);
			lo.setTimeAlarm(timeAlarm);
			// lo.setGpsAlarm(...);
			if (isEvent) {
				Time time = getTimeFromSpinners(dateFromString, dateToString);
				lo.setTime(time);
			}

			if (editing) {
				// TODO Update? Remove+Add? Cookies?
				Bundle bundle = getIntent().getBundleExtra("Edit Task");
				DBHandler dbHandler = new DBHandler(this);
				long id = bundle.getInt("ID");
				ListObject listObject = dbHandler.getListObject(id);
				DBHandler db = new DBHandler(this);

				listObject.setName(lo.getName());
				listObject.setComment(lo.getComment());
				listObject.setPlace(lo.getPlace());
				listObject.setImportant(lo.isImportant());
				db.updatePlaces(listObject.getPlace());
				db.updateListObject(listObject);
			} else {
				saveToDatabase(lo);
			}

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
		// instantiate the database
		DBHandler dbHandler = new DBHandler(this);
		// Save the ListObject framework and save the database id.
		long objId = dbHandler.addListObject(lo);
		// A temporary id to use when saving associated data to connect them
		long tmpId;
		/*
		 * If an associated data is not null it will be saved and the tmp id
		 * will be connected to the list object
		 */
		if (lo.getTimeAlarm() != null) {
			tmpId = dbHandler.addTimeAlarm(lo.getTimeAlarm());
			dbHandler.addListObjectWithTimeAlarm(
					dbHandler.getListObject(objId),
					dbHandler.getTimeAlarm(tmpId));
			// creates a notification!
			if (hasReminder) {
				NotificationHandler nf = new NotificationHandler(this);
				nf.addTimeReminder(dbHandler.getListObject(objId));
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
				// TODO implement methods for setting category!
				// dbHandler.addCategoryWithListObject(cat,
				// dbHandler.getListObject(objId));
			}
		}
		if (lo.getGpsAlarm() != null) {
			tmpId = dbHandler.addGPSAlarm(lo.getGpsAlarm());
			dbHandler.addListObjectWithGPSAlarm(dbHandler.getListObject(objId),
					dbHandler.getGPSAlarm(tmpId));
		}
		if (lo.getPlace() != null) {
			tmpId = dbHandler.addPlace(lo.getPlace());
			dbHandler.addListObjectWithPlace(dbHandler.getListObject(objId),
					dbHandler.getPlace(tmpId));
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
		strDayList.add(nextWeekSameDay);
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
		hasReminder = true;
		// Hide the "Button" that called this method
		v.setVisibility(View.INVISIBLE);

		// View the reminder layout
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.setReminderField);
		relativeLayout.setVisibility(View.VISIBLE);

		/*
		 * Add items to the spinners
		 * 
		 * Begin with the "TYPE" of reminder
		 */
		// An array containing the images for a time and location reminder
		int imgArr[] = {R.drawable.device_access_alarms,
				R.drawable.location_place};

		// An array containing strings to be associated with each image
		String[] strTypeArr = {getString(R.string.time_reminder),
				getString(R.string.location_reminder)};

		SpinnerTypeAdapter typeAdapter = new SpinnerTypeAdapter(this,
				R.layout.type_spinner_item, strTypeArr, imgArr);

		Spinner typeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
		typeSpinner.setAdapter(typeAdapter);

		typeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 0) {
							showTimeReminderInput();
						} else if (position == 1) {
							showLocationReminderInput();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		/* Next up is the date of the reminder, simple enough */
		// ArrayList containing different days for an event

		ArrayList<String> strDayList = new ArrayList<String>();
		strDayList.add(getString(R.string.todayLabel));
		strDayList.add(getString(R.string.tomorrowLabel));
		strDayList.add(nextWeekSameDay);
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
		String[] strIntervalArr = {getString(R.string.oneWeekLabel),
				getString(R.string.twoWeeksLabel),
				getString(R.string.threeWeeksLabel),
				getString(R.string.oneMonthLabel)};

		SpinnerIntervalAdapter intervalAdapter = new SpinnerIntervalAdapter(
				this, R.layout.simple_spinner_item, strIntervalArr);

		Spinner repeatIntervalSpinner = (Spinner) findViewById(R.id.repeatIntervalSpinner);

		repeatIntervalSpinner.setAdapter(intervalAdapter);

		isRepeating = true;
	}

	/**
	 * When the "X" button next to the reminder field is pressed, this method is
	 * called and the layout is restored to its previous state.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelReminder(View v) {
		RelativeLayout toBeHidden = (RelativeLayout) findViewById(R.id.setReminderField);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.remindMeField);

		toBeHidden.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);
		hasReminder = false;
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

	/*
	 * Switches the input forms to something more suited for a Time Reminder
	 */
	private void showTimeReminderInput() {
		isTimeReminder = true;
		Spinner day = (Spinner) findViewById(R.id.reminderDateSpinner);
		Spinner time = (Spinner) findViewById(R.id.reminderTimeSpinner);

		day.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);

		// Also hiding previous input

		EditText location = (EditText) findViewById(R.id.reminderLocationEditText);
		location.setVisibility(View.INVISIBLE);
		isLocationReminder = false;
	}

	/*
	 * Switches the input forms to something more suited for a Location Reminder
	 */
	private void showLocationReminderInput() {
		isLocationReminder = true;
		EditText location = (EditText) findViewById(R.id.reminderLocationEditText);
		location.setVisibility(View.VISIBLE);

		// Also hiding previous input

		Spinner day = (Spinner) findViewById(R.id.reminderDateSpinner);
		Spinner time = (Spinner) findViewById(R.id.reminderTimeSpinner);

		day.setVisibility(View.INVISIBLE);
		time.setVisibility(View.INVISIBLE);
		isTimeReminder = false;
	}

	private Time getTimeFromSpinners(String dateFromString, String dateToString) {

		// Gets all date and time spinners in convert event view
		Spinner dateFromSpinner = (Spinner) findViewById(R.id.date_from);
		SpinnerDayAdapter dateFromAdapter = (SpinnerDayAdapter) dateFromSpinner
				.getAdapter();

		Spinner timeFromSpinner = (Spinner) findViewById(R.id.time_from);
		SpinnerTimeAdapter timeFromAdapter = (SpinnerTimeAdapter) timeFromSpinner
				.getAdapter();

		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);
		SpinnerDayAdapter dateToAdapter = (SpinnerDayAdapter) dateToSpinner
				.getAdapter();

		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);
		SpinnerTimeAdapter timeToAdapter = (SpinnerTimeAdapter) timeToSpinner
				.getAdapter();

		// creates the final start and end dates
		Date finalStartDate = getOneDate(
				dateFromAdapter.getData(dateFromSpinner
						.getSelectedItemPosition()),
				timeFromAdapter.getData(timeFromSpinner
						.getSelectedItemPosition()));

		Date finalEndDate = getOneDate(
				dateToAdapter.getData(dateToSpinner.getSelectedItemPosition()),
				timeToAdapter.getData(timeToSpinner.getSelectedItemPosition()));

		return new Time(0, finalStartDate, finalEndDate);
	}

	// TODO temporary method
	public Date getOneDate(Date date, Date time) {
		// Retrieves date.
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);

		// Retrieves time.
		Calendar timeCalendar = Calendar.getInstance();
		timeCalendar.setTime(time);

		// Merges date and time calendar
		dateCalendar.set(Calendar.HOUR_OF_DAY,
				timeCalendar.get(Calendar.HOUR_OF_DAY));

		dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

		return new Date(dateCalendar.getTimeInMillis());
	}
}
