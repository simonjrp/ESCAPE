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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.chalmers.dat255.group22.escape.adapters.SpinnerCategoryAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerDayAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTimeAdapter;
import se.chalmers.dat255.group22.escape.adapters.SpinnerTypeAdapter;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.fragments.TaskDetailsFragment;
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
public class NewTaskActivity extends Activity {

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

		DBHandler dbHandler = new DBHandler(this);
		// Set up the spinner for different categories
		Spinner categorySpinner = (Spinner) this
				.findViewById(R.id.task_categories);

		ArrayList<String> categories = new ArrayList<String>();
		// Grab all the categories from the DB...
		List<Category> categoriesFromDB = dbHandler.getAllCategories();

		// ...and add them to the array used in the spinner
		// TODO Catch eventual NullPointerException?
		for (Category c : categoriesFromDB) {
			if (!c.getName().equals(getString(R.string.custom_category)))
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
		locationReminderAutoComplete = (AutoCompleteTextView) findViewById(R.id.reminderLocationEditText);
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
		String location = "";
		AutoCompleteTextView taskLocation = (AutoCompleteTextView) findViewById(R.id.task_location);
		if (taskLocation.getText() != null)
			location = taskLocation.getText().toString();
		// Time
		RelativeLayout eventLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
		String dateFromString = "";
		String dateToString = "";
		if (eventLayout.isShown()) {

			Spinner dateFrom = (Spinner) findViewById(R.id.date_from);
			Spinner timeFrom = (Spinner) findViewById(R.id.time_from);

			Spinner dateTo = (Spinner) findViewById(R.id.date_to);
			Spinner timeTo = (Spinner) findViewById(R.id.time_to);

		}

		// Time Alarm

		TimeAlarm timeAlarm = null;

		if (hasReminder) {
			// Reminder type
			Spinner reminderTypeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
			if (reminderTypeSpinner.getSelectedItemPosition() == 0) {
				reminderType = Constants.ReminderType.TIME;
			} else {
				reminderType = Constants.ReminderType.GPS;
			}

			if (reminderType == Constants.ReminderType.TIME) {
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
		}

		// GPS Alarm
		/*
		 * NOTE: GPS Alarm is added in an AsyncTask, because the process of
		 * getting coordinates from a textfields would freeze the UI otherwise
		 */

		// TODO Fix Category from spinner!
		// Category newCategory = new Category(category, "Random Color",
		// "Another random Color");
		Category newCategory = new Category("Tmp default ", "Random Color",
				"Another random Color");
		Place place = new Place(1, location);

		// If a name is set, create ListObject...
		if (name.trim().length() != 0) {
			DBHandler db = new DBHandler(this);
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
			Time time = null;
			if (isEvent) {
				time = getTimeFromSpinners(dateFromString, dateToString);
				newListObject.setTime(time);
			}

			/*
			 * If we are editing an already created task, update the database
			 * with the new values
			 */
			if (editing) {
				Bundle bundle = getIntent().getBundleExtra(EDIT_TASK_MSG);
				DBHandler dbHandler = new DBHandler(this);
				long id = bundle.getInt(INTENT_GET_ID);
				ListObject editedListObject = dbHandler.getListObject(id);
				editedListObject.setName(newListObject.getName());
				editedListObject.setComment(newListObject.getComment());
				editedListObject.setImportant(newListObject.isImportant());
				long tmpId;

				// Update/add new place
				Place originalPlace = dbHandler.getPlace(editedListObject);
				if (originalPlace != null) {
					originalPlace.setName(place.getName());
					db.updatePlaces(originalPlace);
				} else {
					tmpId = dbHandler.addPlace(place);
					dbHandler.addListObjectWithPlace(editedListObject,
							dbHandler.getPlace(tmpId));
				}

				// No matter what¸ remove all old time/place reminders.
				TimeAlarm originalTimeAlarm = dbHandler
						.getTimeAlarm(editedListObject);
				if (originalTimeAlarm != null) {
					NotificationHandler.getInstance().init(this);
					NotificationHandler.getInstance().removeTimeReminder(
							editedListObject);
					dbHandler.deleteTimeAlarm(originalTimeAlarm);
				}

				GPSAlarm originalGPSAlarm = dbHandler
						.getGPSAlarm(editedListObject);
				if (originalGPSAlarm != null) {
					NotificationHandler.getInstance().removeLocationReminder(
							editedListObject);
					dbHandler.deleteGPSAlarm(originalGPSAlarm);
				}

				// Update/add new reminder
				if (hasReminder) {
					if (reminderType == Constants.ReminderType.TIME) {

						if (originalTimeAlarm != null) {
							originalTimeAlarm.setDate(timeAlarm.getDate());
							db.updateTimeAlarm(originalTimeAlarm);
							NotificationHandler
									.getInstance()
									.addTimeReminder(
											dbHandler
													.getListObject((long) editedListObject
															.getId()));
						} else {
							tmpId = dbHandler.addTimeAlarm(timeAlarm);
							dbHandler.addListObjectWithTimeAlarm(
									editedListObject,
									dbHandler.getTimeAlarm(tmpId));
							NotificationHandler
									.getInstance()
									.addTimeReminder(
											dbHandler
													.getListObject((long) editedListObject
															.getId()));
						}
					} else {
						EditText reminderLocationEditText = (EditText) findViewById(R.id.reminderLocationEditText);
						new GenerateGPSAlarmTask(this, id)
								.execute(reminderLocationEditText.getText()
										.toString());
					}
				}

				// Update/add time
				if (isEvent) {
					Time originalTime = dbHandler.getTime(editedListObject);
					if (originalTime != null) {
						originalTime.setStartDate(time.getStartDate());
						originalTime.setEndDate(time.getEndDate());
						db.updateTimes(originalTime);
					} else {
						tmpId = dbHandler.addTime(time);
						dbHandler.addListObjectsWithTime(editedListObject,
								dbHandler.getTime(tmpId));
					}
				} else {
					dbHandler.deleteListObjectWithTime(editedListObject);
				}
				dbHandler.updateListObject(editedListObject);
			} else {
				saveToDatabase(newListObject);

			}

		} else {
			// ... do nothing
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

			// creates a time notification
			NotificationHandler.getInstance().addTimeReminder(
					dbHandler.getListObject(objId));

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
			EditText reminderLocationEditText = (EditText) findViewById(R.id.reminderLocationEditText);
			new GenerateGPSAlarmTask(this, objId)
					.execute(reminderLocationEditText.getText().toString());
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

		//
		/* From: DateSpinner */
		//
		Spinner dateFromSpinner = (Spinner) findViewById(R.id.date_from);

		dateFromSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						dateFromSpinner.getId()));

		// Array containing different days for an event
		ArrayList<String> strDayList = new ArrayList<String>();
		strDayList.add(getString(R.string.today_label));
		strDayList.add(getString(R.string.tomorrow_label));
		strDayList.add(nextWeekSameDay);
		strDayList.add(getString(R.string.pick_day_label));

		SpinnerDayAdapter dayFromAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, dateFromSpinner);

		//
		/* From: TimeSpinner */
		//
		Spinner timeFromSpinner = (Spinner) findViewById(R.id.time_from);
		timeFromSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.TIME_SPINNER,
						timeFromSpinner.getId()));

		SpinnerTimeAdapter timeFromAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, timeFromSpinner);

		//
		/* To: DateSpinner */
		//
		Spinner dateToSpinner = (Spinner) findViewById(R.id.date_to);
		dateToSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						dateToSpinner.getId()));

		SpinnerDayAdapter dayToAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, dateToSpinner);

		//
		/* To: TimeSpinner */
		//
		Spinner timeToSpinner = (Spinner) findViewById(R.id.time_to);
		timeToSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.TIME_SPINNER,
						timeToSpinner.getId()));

		SpinnerTimeAdapter timeToAdapter = new SpinnerTimeAdapter(this,
				R.layout.time_spinner_item, timeToSpinner);

		// Set all adapters
		dateFromSpinner.setAdapter(dayFromAdapter);
		timeFromSpinner.setAdapter(timeFromAdapter);

		dateToSpinner.setAdapter(dayToAdapter);
		timeToSpinner.setAdapter(timeToAdapter);

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

		Spinner reminderDateSpinner = (Spinner) findViewById(R.id.reminderDateSpinner);

		SpinnerDayAdapter dayAdapter = new SpinnerDayAdapter(this,
				R.layout.simple_spinner_item, reminderDateSpinner);

		reminderDateSpinner.setAdapter(dayAdapter);
		reminderDateSpinner
				.setOnItemSelectedListener(new OnItemSelectedSpinnerListener(
						this, OnItemSelectedSpinnerListener.DATE_SPINNER,
						reminderDateSpinner.getId()));

		/*
		 * Last but not least, time of the reminder with a clarification of the
		 * time
		 */

		Spinner reminderTimeSpinner = (Spinner) findViewById(R.id.reminderTimeSpinner);

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
	 * 
	 * @param v
	 *            the view that calls this method.
	 */
	public void onCancelEvent(View v) {
		RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.eventSpinners);
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
		Spinner day = (Spinner) findViewById(R.id.reminderDateSpinner);
		Spinner time = (Spinner) findViewById(R.id.reminderTimeSpinner);

		day.setVisibility(View.VISIBLE);
		time.setVisibility(View.VISIBLE);

		// Also hiding previous input

		AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.reminderLocationEditText);
		location.setVisibility(View.INVISIBLE);
		isLocationReminder = false;
	}

	/*
	 * Switches the input forms to something more suited for a Location Reminder
	 */
	private void showLocationReminderInput() {
		isLocationReminder = true;
		AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.reminderLocationEditText);
		location.setVisibility(View.VISIBLE);

		// Also hiding previous input

		Spinner day = (Spinner) findViewById(R.id.reminderDateSpinner);
		Spinner time = (Spinner) findViewById(R.id.reminderTimeSpinner);

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

			// TODO This needs to be fixed
			/*
			 * if (listObject.getCategories() != null) categoryString =
			 * listObject.getCategories().get(0) .getName();
			 */

			if (listObject.getComment() != null)
				descriptionString = listObject.getComment();

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

			Spinner remindType = (Spinner) findViewById(R.id.reminderTypeSpinner);
			Spinner remindDate = (Spinner) findViewById(R.id.reminderDateSpinner);
			Spinner remindTime = (Spinner) findViewById(R.id.reminderTimeSpinner);

			AutoCompleteTextView remindLocation = (AutoCompleteTextView) findViewById(R.id.reminderLocationEditText);

			Spinner dateFrom = (Spinner) findViewById(R.id.date_from);
			Spinner dateTo = (Spinner) findViewById(R.id.date_to);
			Spinner timeFrom = (Spinner) findViewById(R.id.time_from);
			Spinner timeTo = (Spinner) findViewById(R.id.time_to);

			// ...and set their default values from the listObject!
			title.setText(nameString);

			// TODO This needs to be worked out
			/*
			 * if(categoryString != null &&
			 * !categoryString.equals(getString(R.string.custom_category)))
			 * category.setSelection(0);
			 */

			if (descriptionString != null) {
				if (descriptionString.trim().length() != 0)
					description.setText(descriptionString);
			}
			if (locationString != null) {
				if (locationString.trim().length() != 0)
					location.setText(locationString);
			}

			important.setChecked(isImportant);

			// Open up the reminder field if it has a reminder...
			if (timeAlarm != null) {
				RelativeLayout remindMe = (RelativeLayout) findViewById(R.id.remindMeField);
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
			if (gpsAlarm != null) {
				RelativeLayout remindMe = (RelativeLayout) findViewById(R.id.remindMeField);
				onRemindMe(remindMe);
				remindType.setSelection(1);
				showLocationReminderInput();

				// Set the text of the location field
				// TODO Need latest DB to get Location as string from
				// TODO GPSAlarm
				remindLocation.setText(gpsAlarm.getAdress());

				isLocationReminder = true;
				isTimeReminder = false;
			}

			// ...and the event field if it is an event...
			if (timeStart != null) {
				Button remindMe = (Button) findViewById(R.id.task_convert_event);
				onConvertEvent(remindMe);

				// Set the spinners according to the current time...
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
				GetPlaces task = new GetPlaces(autoCompleteTextView, adapter,
						getBaseContext());
				// now pass the argument in the textview to the task
				task.execute(autoCompleteTextView.getText().toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}

}
