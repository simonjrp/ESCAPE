package se.chalmers.dat255.group22.escape;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import se.chalmers.dat255.group22.escape.adapters.EventSpinnerSyncer;
import se.chalmers.dat255.group22.escape.adapters.SpinnerCategoryAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerDayAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTimeAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTypeAdapter;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.fragments.TaskDetailsFragment;
import se.chalmers.dat255.group22.escape.fragments.dialogfragments.CategoryCreatorFragment;
import se.chalmers.dat255.group22.escape.listeners.OnItemSelectedSpinnerListener;
import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.GPSAlarm;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import se.chalmers.dat255.group22.escape.utils.Constants;
import se.chalmers.dat255.group22.escape.utils.Constants.ReminderType;
import se.chalmers.dat255.group22.escape.utils.GenerateGPSAlarmTask;
import se.chalmers.dat255.group22.escape.utils.GetPlaces;

import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_ID;
import static se.chalmers.dat255.group22.escape.utils.Constants.EDIT_TASK_MSG;
import static se.chalmers.dat255.group22.escape.utils.Constants.INTENT_GET_ID;

/**
 * An activity used for creating a new task
 * 
 * @author Erik, Carl, Simon Persson
 */
public class NewTaskActivity extends Activity
		implements
			CategoryCreatorFragment.EditCategoryDialogListener {

	private AutoCompleteTextView locationAutoComplete;
	private AutoCompleteTextView locationReminderAutoComplete;
	private ArrayAdapter<String> adapter;
	private boolean hasReminder;
	private boolean isTimeReminder;
	private boolean isLocationReminder;
	private boolean isEvent;
	private boolean editing;
	private String nextWeekSameDay;
	private ReminderType reminderType;
	private boolean userIsSure;

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
		userIsSure = false;

		// Set up the spinner for different categories
		initCategoryAdapter();

		/*
		 * Check if the activity was called from an already created listObject
		 * by checking the flag of the intent.
		 * 
		 * In other words, check if we are editing a current listObject or
		 * creating a new one.
		 */
		Intent intent = getIntent();
		if (intent.getFlags() == EDIT_TASK_ID) {
			loadListObjectFromIntent(intent);
		}

		// Initiate the AutoCompleteTextViews
		locationReminderAutoComplete = (AutoCompleteTextView) findViewById(R.id.reminder_location_edittext);
		locationAutoComplete = (AutoCompleteTextView) findViewById(R.id.task_location);

		// Initiate the ArrayAdapter
		adapter = new ArrayAdapter<String>(this, R.layout.location_item);
		adapter.setNotifyOnChange(true);

		// Set up the AutoCompleteTextViews
		setupAutoCompleteTextView(locationReminderAutoComplete);
		setupAutoCompleteTextView(locationAutoComplete);

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

		// Get references to all input fields
		// Title
		EditText taskTitle = (EditText) findViewById(R.id.task_title);
		String name = taskTitle.getText().toString();

		// Category
		Spinner categories = (Spinner) findViewById(R.id.task_categories);
		String category = categories.getSelectedItem().toString();

		// Description
		EditText taskDesc = (EditText) findViewById(R.id.task_description);
		String comment = taskDesc.getText().toString();

		// Important
		CheckBox important = (CheckBox) findViewById(R.id.task_important);
		boolean importantTask = important.isChecked();

		// Location
		String location = "";
		AutoCompleteTextView taskLocation = (AutoCompleteTextView) findViewById(R.id.task_location);
		if (taskLocation.getText() != null)
			location = taskLocation.getText().toString();

		// Time
		String dateFromString = "";
		String dateToString = "";
		if (isEvent) {

			Spinner dateFrom = (Spinner) findViewById(R.id.date_from);
			Spinner timeFrom = (Spinner) findViewById(R.id.time_from);

			Spinner dateTo = (Spinner) findViewById(R.id.date_to);
			Spinner timeTo = (Spinner) findViewById(R.id.time_to);

		}

		// Time Alarm

		TimeAlarm timeAlarm = null;

		if (hasReminder) {
			if (isTimeReminder) {
				// Gets the spinners and their adapters.

				Spinner reminderDateSpinner = (Spinner) findViewById(R.id.reminder_date_spinner);
				SpinnerDayAdapter reminderDateAdapter = (SpinnerDayAdapter) reminderDateSpinner
						.getAdapter();

				Spinner reminderTimeSpinner = (Spinner) findViewById(R.id.reminder_time_spinner);
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
		}

		// GPS Alarm
		/*
		 * NOTE: GPS Alarm is added in an AsyncTask, because the process of
		 * getting coordinates from a textfields would freeze the UI otherwise
		 */
		DBHandler dbHandler = new DBHandler(this);
		Category newCategory = new Category(category, dbHandler.getCategory(
				category).getBaseColor(), dbHandler.getCategory(category)
				.getImportantColor());
		dbHandler.close();
		Place place = new Place(1, location);

		// If a name is set, create ListObject...
		if (name.trim().length() != 0) {
			ListObject newListObject = new ListObject(1, name);

			if (comment.trim().length() != 0)
				newListObject.setComment(comment);
			else
				// A comment MUST be set even if only null!
				newListObject.setComment(null);

			newListObject.setImportant(importantTask);
			newListObject.addToCategory(newCategory);
			newListObject.setPlace(place);
			if (isTimeReminder) {
				newListObject.setTimeAlarm(timeAlarm);
			} else if (isLocationReminder) {
				// lo.setGpsAlarm(...);
			}
			Time newTime = null;
			if (isEvent) {
				newTime = getTimeFromSpinners(dateFromString, dateToString);
				newListObject.setTime(newTime);
			}

			/*
			 * If we are editing an already created task, update the database
			 * with the new values
			 */
			if (editing) {
				updateDataBase(newListObject);
			} else {
				saveToDatabase(newListObject);
			}
			super.onBackPressed();
		} else {
			// If there is no title, remind the user that a listObject needs a
			// title to prevent mistakes
			if (userIsSure) {
				if (isEvent)
					Toast.makeText(this, getText(R.string.event_not_saved),
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, getText(R.string.task_not_saved),
							Toast.LENGTH_LONG).show();
				super.onBackPressed();
			} else {
				Toast.makeText(this, getText(R.string.no_save),
						Toast.LENGTH_SHORT).show();
				userIsSure = true;
			}
		}

	}

	/*
	 * Grab all values from the fields and update/add the values to the database
	 */
	private void updateDataBase(ListObject newListObject) {
		// References
		DBHandler dbHandler = new DBHandler(this);
		Place place = newListObject.getPlace();
		Category newCategory = newListObject.getCategories().get(0);
		Time newTime = newListObject.getTime();
		TimeAlarm timeAlarm = newListObject.getTimeAlarm();
		// Get the ListObject that wants to be edited
		Bundle bundle = getIntent().getBundleExtra(EDIT_TASK_MSG);
		long id = bundle.getInt(INTENT_GET_ID);
		long tmpId;
		ListObject editedListObject = dbHandler.getListObject(id);

		editedListObject.setName(newListObject.getName());
		editedListObject.setComment(newListObject.getComment());
		editedListObject.setImportant(newListObject.isImportant());

		// Update/add new place
		Place originalPlace = dbHandler.getPlace(editedListObject);
		if (originalPlace != null) {
			originalPlace.setName(place.getName());
			dbHandler.updatePlaces(originalPlace);
		} else {
			tmpId = dbHandler.addPlace(place);
			dbHandler.addListObjectWithPlace(editedListObject,
					dbHandler.getPlace(tmpId));
		}

		Category originalCategory = dbHandler.getCategories(editedListObject)
				.get(0);
		if (originalCategory != null) {
			dbHandler.deleteCategoryWithListObject(originalCategory,
					editedListObject);
			originalCategory.setName(newCategory.getName());
			dbHandler.addCategoryWithListObject(originalCategory,
					editedListObject);
		}

		// No matter what¸ remove all old time/place reminders.
		TimeAlarm originalTimeAlarm = dbHandler.getTimeAlarm(editedListObject);
		if (originalTimeAlarm != null) {
			dbHandler.deleteTimeAlarm(originalTimeAlarm);
			dbHandler.deleteListObjectWithTimeAlarm(editedListObject);
			NotificationHandler.getInstance().removeTimeReminder(
					editedListObject);
		}

		GPSAlarm originalGPSAlarm = dbHandler.getGPSAlarm(editedListObject);
		if (originalGPSAlarm != null) {
			dbHandler.deleteListObjectWithGPSAlarm(editedListObject);
			dbHandler.deleteGPSAlarm(originalGPSAlarm);
			NotificationHandler.getInstance().removeLocationReminder(
					editedListObject);
		}

		Time originalTime = dbHandler.getTime(editedListObject);
		if (originalTime != null) {
			dbHandler.deleteListObjectWithTime(editedListObject);
			dbHandler.deleteTime(originalTime);

		}

		// Update/add time
		if (isEvent) {
			tmpId = dbHandler.addTime(newTime);
			dbHandler.addListObjectsWithTime(editedListObject,
					dbHandler.getTime(tmpId));

		}

		// Update/add new reminder
		if (hasReminder) {
			if (reminderType == Constants.ReminderType.TIME && isTimeReminder) {

				if (originalTimeAlarm != null) {
					originalTimeAlarm.setDate(timeAlarm.getDate());
					dbHandler.updateTimeAlarm(originalTimeAlarm);
					NotificationHandler.getInstance().addTimeReminder(
							dbHandler.getListObject(
									(long) editedListObject.getId()).getId());
				} else {
					tmpId = dbHandler.addTimeAlarm(timeAlarm);
					dbHandler.addListObjectWithTimeAlarm(editedListObject,
							dbHandler.getTimeAlarm(tmpId));
					NotificationHandler.getInstance().addTimeReminder(
							dbHandler.getListObject(
									(long) editedListObject.getId()).getId());
				}
			} else if (isLocationReminder) {
				EditText reminderLocationEditText = (EditText) findViewById(R.id.reminder_location_edittext);
				new GenerateGPSAlarmTask(this, id)
						.execute(reminderLocationEditText.getText().toString());
			}
		}

		dbHandler.updateListObject(editedListObject);
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

			// creates a time notification
			NotificationHandler.getInstance().addTimeReminder(objId);

		}

		if (lo.getTime() != null) {
			tmpId = dbHandler.addTime(lo.getTime());
			dbHandler.addListObjectsWithTime(dbHandler.getListObject(objId),
					dbHandler.getTime(tmpId));
		}

		if (lo.getCategories() != null) {
			for (Category cat : lo.getCategories()) {
				if (!cat.getName().equals(getString(R.string.custom_category)))
					dbHandler.addCategory(cat);
				dbHandler.addCategoryWithListObject(
						dbHandler.getCategory(cat.getName()),
						dbHandler.getListObject(objId));
			}
		}

		/*
		 * Adds a GPS reminder. This is done in another thread to avoid that UI
		 * freezes when trying to find coordinates matching the text string in
		 * the reminder location text field.
		 */
		if (reminderType == Constants.ReminderType.GPS) {
			EditText reminderLocationEditText = (EditText) findViewById(R.id.reminder_location_edittext);
			new GenerateGPSAlarmTask(this, objId)
					.execute(reminderLocationEditText.getText().toString());
		}

		if (lo.getPlace() != null) {
			tmpId = dbHandler.addPlace(lo.getPlace());
			dbHandler.addListObjectWithPlace(dbHandler.getListObject(objId),
					dbHandler.getPlace(tmpId));
		}
		dbHandler.close();
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
		isEvent = true;
		v.setVisibility(View.INVISIBLE);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.event_spinners);
		relativeLayout.setVisibility(View.VISIBLE);

		//
		/* From: DateSpinner */
		//
		Spinner dateFromSpinner = (Spinner) findViewById(R.id.date_from);

		// Array containing different days for an event
		ArrayList<String> strDayList = new ArrayList<String>();
		strDayList.add(getString(R.string.todayLabel));
		strDayList.add(getString(R.string.tomorrowLabel));
		strDayList.add(nextWeekSameDay);
		strDayList.add(getString(R.string.pick_day_label));

		SpinnerDayAdapter dayFromAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, dateFromSpinner);

		//
		/* From: TimeSpinner */
		//
		Spinner timeFromSpinner = (Spinner) findViewById(R.id.time_from);

		SpinnerTimeAdapter timeFromAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, timeFromSpinner);

		//
		/* To: DateSpinner */
		//
		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);

		SpinnerDayAdapter dayToAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, dateToSpinner);

		//
		/* To: TimeSpinner */
		//
		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);

		SpinnerTimeAdapter timeToAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, timeToSpinner);

		// Set all adapters
		dateFromSpinner.setAdapter(dayFromAdapter);
        dateToSpinner.setAdapter(dayToAdapter);
        timeFromSpinner.setAdapter(timeFromAdapter);
        timeToSpinner.setAdapter(timeToAdapter);

        EventSpinnerSyncer eventSpinnerSyncer = new EventSpinnerSyncer(this, dateFromSpinner, dateToSpinner, timeFromSpinner, timeToSpinner);

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
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.set_reminder_field);
		relativeLayout.setVisibility(View.VISIBLE);

		// Add items to the spinners
		// Begin with the "TYPE" of reminder

		// An array containing the images for a time and location reminder
		int imgArr[] = {R.drawable.device_access_alarms,
				R.drawable.location_place};

		// An array containing strings to be associated with each image
		String[] strTypeArr = {getString(R.string.time_reminder),
				getString(R.string.location_reminder)};

		SpinnerTypeAdapter typeAdapter = new SpinnerTypeAdapter(this,
				R.layout.type_spinner_item, strTypeArr, imgArr);

		Spinner typeSpinner = (Spinner) findViewById(R.id.reminder_type_spinner);
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

		// Next up is the date of the reminder

		Spinner reminderDateSpinner = (Spinner) findViewById(R.id.reminder_date_spinner);

		SpinnerDayAdapter dayAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, reminderDateSpinner);

		reminderDateSpinner.setAdapter(dayAdapter);
		reminderDateSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						reminderDateSpinner.getId()));

		// Last but not least, time of the reminder with a clarification of the
		// time

		Spinner reminderTimeSpinner = (Spinner) findViewById(R.id.reminder_time_spinner);

		SpinnerTimeAdapter timeAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, reminderTimeSpinner);

		reminderTimeSpinner.setAdapter(timeAdapter);
		reminderTimeSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.TIME_SPINNER,
						reminderTimeSpinner.getId()));
		hasReminder = true;

	}

	/**
	 * When the "X" button next to the reminder field is pressed, this method is
	 * called and the layout is restored to its previous state.
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelReminder(View v) {
		RelativeLayout toBeHidden = (RelativeLayout) findViewById(R.id.set_reminder_field);
		RelativeLayout toBeShownLayout = (RelativeLayout) findViewById(R.id.remind_me_field);

		toBeHidden.setVisibility(View.INVISIBLE);
		toBeShownLayout.setVisibility(View.VISIBLE);
		hasReminder = false;
	}

	/**
	 * When the "X" button next to the event field is pressed, this method is
	 * called and the layout is restored to its previous state.
	 * 
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelEvent(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.event_spinners);
		Button toBeShownButton = (Button) findViewById(R.id.task_convert_event);

		currentLayout.setVisibility(View.INVISIBLE);
		toBeShownButton.setVisibility(View.VISIBLE);
		isEvent = false;
	}

	/*
	 * Switches the input forms to something more suited for a Time Reminder
	 */
	private void showTimeReminderInput() {
		isTimeReminder = true;
		Spinner day = (Spinner) findViewById(R.id.reminder_date_spinner);
		Spinner time = (Spinner) findViewById(R.id.reminder_time_spinner);

		day.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);

		// Also hiding previous input

		AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.reminder_location_edittext);
		location.setVisibility(View.INVISIBLE);
		isLocationReminder = false;
	}

	/*
	 * Switches the input forms to something more suited for a Location Reminder
	 */
	private void showLocationReminderInput() {
		isLocationReminder = true;
		AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.reminder_location_edittext);
		location.setVisibility(View.VISIBLE);

		// Also hiding previous input

		Spinner day = (Spinner) findViewById(R.id.reminder_date_spinner);
		Spinner time = (Spinner) findViewById(R.id.reminder_time_spinner);

		day.setVisibility(View.INVISIBLE);
		time.setVisibility(View.INVISIBLE);
		isTimeReminder = false;
	}

	/* Get the time from Spinners */
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

	/*
	 * Add data from a listObject in the database to the input fields! The
	 * intent should contain the ID of the listObject bundled with a
	 * EDIT_TASK_MSG to make sure we want to edit an object!
	 */
	private void loadListObjectFromIntent(Intent intent) {
		DBHandler dbHandler = new DBHandler(this);
		// Get the bundle and the ID of the listObject...
		Bundle bundle = intent.getBundleExtra(EDIT_TASK_MSG);
		if (bundle != null) {
			long id = bundle.getInt(INTENT_GET_ID);
			ListObject listObject = dbHandler.getListObject(id);

			editing = true;

			// ...and get data from the listObject that "called" the
			// activity

			// Avoid NullPointerException
			String nameString = "";
			String categoryString = "";
			String descriptionString = "";
			String locationString = "";
			TimeAlarm timeAlarm = null;
			GPSAlarm gpsAlarm = null;
			Date timeStart = null;
			Date timeEnd = null;

			if (listObject.getName() != null)
				nameString = listObject.getName();

			if (listObject.getComment() != null)
				descriptionString = listObject.getComment();

			if (listObject.getCategories() != null)
				categoryString = dbHandler.getCategories(listObject).get(0)
						.getName();

			if (dbHandler.getPlace(listObject) != null)
				locationString = dbHandler.getPlace(listObject).getName();

			Boolean isImportant = listObject.isImportant();

			if (dbHandler.getTimeAlarm(listObject) != null) {
				timeAlarm = dbHandler.getTimeAlarm(listObject);
			}

			if (dbHandler.getGPSAlarm(listObject) != null) {
				gpsAlarm = dbHandler.getGPSAlarm(listObject);
			}

			if (dbHandler.getTime(listObject) != null)
				timeStart = dbHandler.getTime(listObject).getStartDate();

			if (dbHandler.getTime(listObject) != null)
				timeEnd = dbHandler.getTime(listObject).getEndDate();

			// Grab references to all the input fields...

			EditText title = (EditText) findViewById(R.id.task_title);
			Spinner category = (Spinner) findViewById(R.id.task_categories);
			EditText description = (EditText) findViewById(R.id.task_description);
			AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.task_location);
			CheckBox important = (CheckBox) findViewById(R.id.task_important);

			Spinner remindType = (Spinner) findViewById(R.id.reminder_type_spinner);
			Spinner remindDate = (Spinner) findViewById(R.id.reminder_date_spinner);
			Spinner remindTime = (Spinner) findViewById(R.id.reminder_time_spinner);

			AutoCompleteTextView remindLocation = (AutoCompleteTextView) findViewById(R.id.reminder_location_edittext);

			Spinner dateFrom = (Spinner) findViewById(R.id.date_from);
			Spinner dateTo = (Spinner) findViewById(R.id.date_to);
			Spinner timeFrom = (Spinner) findViewById(R.id.time_from);
			Spinner timeTo = (Spinner) findViewById(R.id.time_to);

			// ...and set their default values from the listObject!
			title.setText(nameString);

			if (categoryString != null
					&& !categoryString
							.equals(getString(R.string.custom_category))) {
				for (int i = 0; i < category.getAdapter().getCount(); i++) {
					String c = (String) category.getAdapter().getItem(i);
					if (c.equals(categoryString)) {
						category.setSelection(i);
						break;
					}
				}

			}

			if (descriptionString != null) {
				if (descriptionString.trim().length() != 0)
					description.setText(descriptionString);
			}
			if (locationString != null) {
				if (locationString.trim().length() != 0)
					location.setText(locationString);
			}

			important.setChecked(isImportant);

			// Open up the reminder field if it has a reminder.
			// Also check what if is is a...
			// ...time reminder...
			if (timeAlarm != null) {
				RelativeLayout remindMe = (RelativeLayout) findViewById(R.id.remind_me_field);
				onRemindMe(remindMe);
				remindType.setSelection(0);
				showTimeReminderInput();

				// Set the spinners according to the current time alarm...
				SpinnerDayAdapter dayAdapter = (SpinnerDayAdapter) remindDate
						.getAdapter();
				dayAdapter.addDate(timeAlarm.getDate());

				SpinnerTimeAdapter timeAdapter = (SpinnerTimeAdapter) remindTime
						.getAdapter();
				timeAdapter.addTime(timeAlarm.getDate());

				isTimeReminder = true;
				isLocationReminder = false;

			}
			// ... or a location reminder...
			if (gpsAlarm != null) {
				RelativeLayout remindMe = (RelativeLayout) findViewById(R.id.remind_me_field);
				onRemindMe(remindMe);
				remindType.setSelection(1);
				showLocationReminderInput();

				// Set the text of the location field
				remindLocation.setText(gpsAlarm.getAdress());

				isLocationReminder = true;
				isTimeReminder = false;
			}

			// ...and show the event field if it is an event...
			if (timeStart != null) {
				Button remindMe = (Button) findViewById(R.id.task_convert_event);
				onConvertEvent(remindMe);

				// Set the spinners according to the current time
				// Day from:
				SpinnerDayAdapter fromDayAdapter = (SpinnerDayAdapter) dateFrom
						.getAdapter();
				fromDayAdapter.addDate(timeStart);

				// Time from:
				SpinnerTimeAdapter fromTimeAdapter = (SpinnerTimeAdapter) timeFrom
						.getAdapter();
				fromTimeAdapter.addTime(timeStart);

				// Day to:
				SpinnerDayAdapter toDayAdapter = (SpinnerDayAdapter) dateTo
						.getAdapter();
				toDayAdapter.addDate(timeEnd);

				// Time to:
				SpinnerTimeAdapter toTimeAdapter = (SpinnerTimeAdapter) timeTo
						.getAdapter();
				toTimeAdapter.addTime(timeEnd);

				isEvent = true;
			}
		}
	}

	/*
	 * Set up the AutoCompleteTextViews. Basically add a listener that executes
	 * the AsyncTask for predictions.
	 */
	private void setupAutoCompleteTextView(
			final AutoCompleteTextView autoCompleteTextView) {
		autoCompleteTextView.setThreshold(3);
		autoCompleteTextView.setAdapter(adapter);
		autoCompleteTextView
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus)
							autoCompleteTextView.setSelection(0);
					}
				});
		autoCompleteTextView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 0) {
					GetPlaces task = new GetPlaces(autoCompleteTextView,
							adapter, getBaseContext());
					// now pass the argument in the textview to the task
					task.execute(autoCompleteTextView.getText().toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}

	/*
	 * Add the default categories to the database and add them to the
	 * categoryAdapter. This is necessary because if the user creates a new
	 * category, that category should be displayed immediately.
	 */
	private void initCategoryAdapter() {
		DBHandler dbHandler = new DBHandler(this);
		Spinner categorySpinner = (Spinner) this
				.findViewById(R.id.task_categories);

		ArrayList<String> categories = new ArrayList<String>();
		// Grab all the categories from the DB...

		dbHandler.addCategory(new Category(
				getString(R.string.default_category_school), Integer
						.toHexString(getResources().getColor(R.color.magenta)),
				Integer.toHexString(getResources().getColor(
                        R.color.magenta_dark))));

		dbHandler
				.addCategory(new Category(
						getString(R.string.default_category_work), Integer
								.toHexString(getResources().getColor(
										R.color.red)), Integer
								.toHexString(getResources().getColor(
										R.color.red_dark))));
		dbHandler
				.addCategory(new Category(
						getString(R.string.default_category_spare_time),
						Integer.toHexString(getResources().getColor(
								R.color.green)), Integer
								.toHexString(getResources().getColor(
										R.color.green_dark))));

		dbHandler.addCategory(new Category(getString(R.string.autogenerated),
				Integer.toHexString(getResources().getColor(R.color.orange)),
				Integer.toHexString(getResources()
						.getColor(R.color.orange_dark))));

		List<Category> categoriesFromDB = dbHandler.getAllCategories();

		// ...and add them to the array used in the spinner
		for (Category c : categoriesFromDB) {
			if (!c.getName().equals(getString(R.string.custom_category))
					&& !c.getName().equals(getString(R.string.autogenerated)))
				categories.add(c.getName());
		}
		// Add the last item that will act as a button for adding a new category
		categories.add(getString(R.string.custom_category));

		// The DayAdapter only makes use of simple strings and presents its
		// items the way we want the categories to be presented. It would be
		// unnecessary to create an identical adapter just for the categories,
		// so we just use this one instead

		SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(
				this, R.layout.simple_spinner_item, categories);

		categorySpinner.setAdapter(categoryAdapter);
		categorySpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.CATEGORY_SPINNER,
						categorySpinner.getId()));
	}

	@Override
	public void onFinishEditDialog(String inputText) {
		DBHandler dbHandler = new DBHandler(this);
		if (!inputText.equals(getString(R.string.custom_category))) {
			// TODO The colors SHOULD be something the user has defined...
			// Its more fun if the new categories at least have SOME different
			// color...
			Random random = new Random();
			String baseColor = "";
			String importantColor = "";
			switch (random.nextInt(3)) {
				case 0 :
					baseColor = Integer.toHexString(getResources().getColor(
							R.color.lime_green));
					importantColor = Integer.toHexString(getResources()
							.getColor(R.color.lime_green_dark));
					break;
				case 1 :
					baseColor = Integer.toHexString(getResources().getColor(
							R.color.light_blue));
					importantColor = Integer.toHexString(getResources()
							.getColor(R.color.light_blue_dark));
					break;
				case 2 :
					baseColor = Integer.toHexString(getResources().getColor(
							R.color.chocolate_brown));
					importantColor = Integer.toHexString(getResources()
							.getColor(R.color.chocolate_brown_dark));
					break;
				default :
					baseColor = Integer.toHexString(getResources().getColor(
							R.color.white));
					importantColor = Integer.toHexString(getResources()
							.getColor(R.color.white));
					break;
			}
			Category newCategory = new Category(inputText, baseColor,
					importantColor);
			dbHandler.addCategory(newCategory);
			initCategoryAdapter();
		}
	}
}
