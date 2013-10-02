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
import android.content.Intent;
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
	private boolean editing;

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
		// TODO Fix this ugly check
		if (intent.getStringExtra("EDIT") != null) {
			if (intent.getStringExtra("EDIT").equals("TRUE")) {
				// TODO Should keep track of wether this is a new event or an
				// TODO event in editing, maybe a better way for this?
				editing = true;

				// References to all the input fields
				EditText title = (EditText)findViewById(R.id.task_title);
				Spinner category = (Spinner)findViewById(R.id.task_categories);
				EditText description = (EditText)findViewById(R.id.task_description);
				EditText location = (EditText)findViewById(R.id.task_location);
				CheckBox important = (CheckBox)findViewById(R.id.task_important);

				Spinner remindType = (Spinner)findViewById(R.id.reminderTypeSpinner);
				Spinner remindDate = (Spinner)findViewById(R.id.reminderDateSpinner);
				Spinner remindTime = (Spinner)findViewById(R.id.reminderTimeSpinner);

				Spinner dateFrom = (Spinner)findViewById(R.id.date_from);
				Spinner dateTo = (Spinner)findViewById(R.id.date_to);
				Spinner timeFrom = (Spinner)findViewById(R.id.time_from);
				Spinner timeTo = (Spinner)findViewById(R.id.time_to);

                CheckBox mondayBox = (CheckBox)findViewById(R.id.mondayBox);
                CheckBox tuesdayBox = (CheckBox)findViewById(R.id.tuesdayBox);
                CheckBox wednesdayBox = (CheckBox)findViewById(R.id.wednesdayBox);
                CheckBox thursdayBox = (CheckBox)findViewById(R.id.thursdayBox);
                CheckBox fridayBox = (CheckBox)findViewById(R.id.fridayBox);
                CheckBox saturdayBox = (CheckBox)findViewById(R.id.saturdayBox);
                CheckBox sundayBox = (CheckBox)findViewById(R.id.sundayBox);
                Spinner interval = (Spinner)findViewById(R.id.repeatIntervalSpinner);

				// Set up the fields from the ListObject that "called" the
				// activity
				// TODO This should also be saved into the object when
				// TODO pausing/destroying the activity!
				title.setText(intent.getStringExtra("Name"));
                description.setText(intent.getStringExtra("Description"));

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
		// TODO It's possible to select a time for the reminder that might've
		// passed already
		TimeAlarm timeAlarm = null;
		if (hasReminder) {
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

            if(editing) {
                // TODO Update? Remove+Add? Cookies?
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
		DBHandler dbHandler = new DBHandler(this);
		dbHandler.addListObject(lo);

		if (lo.getTimeAlarm() != null) {
			dbHandler.addTimeAlarm(lo.getTimeAlarm());
			dbHandler.addListObjectWithTimeAlarm(lo, lo.getTimeAlarm());
			// creates a notification!
			if (hasReminder) {
				// TODO fix notifications!
				// NotificationHandler nf = new NotificationHandler(this);
				// nf.addReminderNotification(lo);
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

		//
		/* From: DateSpinner */
		//
		// Array containing different days for an event
		String[] strDayArr = {getString(R.string.todayLabel),
				getString(R.string.tomorrowLabel),
				getString(R.string.thisWeekLabel),
				getString(R.string.pickDayLabel)};

		SpinnerDayAdapter dayAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, strDayArr);
		dateFromSpinner.setAdapter(dayAdapter);

		//
		/* From: TimeSpinner */
		//
		Spinner timeFromSpinner = (Spinner) findViewById(R.id.time_from);

		// Array containing different times for an event
		String[] strTimeArr = {getString(R.string.morning),
				getString(R.string.afternoon), getString(R.string.evening),
				getString(R.string.night), getString(R.string.pickTimeLabel)};

		SpinnerTimeAdapter timeAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, strTimeArr);
		timeFromSpinner.setAdapter(timeAdapter);

		//
		/* To: TimeSpinner */
		//
		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);

		dateToSpinner.setAdapter(dayAdapter);

		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);

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
		int imgArr[] = {R.drawable.device_access_alarms,
				R.drawable.location_place};

		// An array containing strings to be associated with each image
		String[] strTypeArr = {getString(R.string.time_reminder),
				getString(R.string.location_reminder)};

		SpinnerTypeAdapter typeAdapter = new SpinnerTypeAdapter(this,
				R.layout.type_spinner_item, strTypeArr, imgArr);

		Spinner typeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
		typeSpinner.setAdapter(typeAdapter);

		/* Next up is the date of the reminder, simple enough */
		// Array containing different days for an event
		String[] strDayArr = {getString(R.string.todayLabel),
				getString(R.string.tomorrowLabel),
				getString(R.string.thisWeekLabel),
				getString(R.string.pickDayLabel)};

		SpinnerDayAdapter dayAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, strDayArr);

		Spinner dateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);
		dateSpinner.setAdapter(dayAdapter);

		/*
		 * Last but not least, time of the reminder with a clarification of the
		 * time
		 */
		// Array containing different times for an event
		String[] strTimeArr = {getString(R.string.morning),
				getString(R.string.afternoon), getString(R.string.evening),
				getString(R.string.night), getString(R.string.pickTimeLabel)};

		SpinnerTimeAdapter timeAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, strTimeArr);

		Spinner timeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);
		timeSpinner.setAdapter(timeAdapter);
		hasReminder = true;
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
}
