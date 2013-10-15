package se.chalmers.dat255.group22.escape;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.BlockObject;
import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.TimeWindow;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Class representing the activity started when user adds new time blocks
 * (autogen).
 * 
 * @author Simon Persson
 */
public class NewBlockActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_block);
		// Show the Up button in the action bar.
		setupActionBar();

		// TODO Assigns test data (temporary) to all spinners.
		Spinner totalHoursSpinner = (Spinner) findViewById(R.id.block_hours_spinner);
		Spinner splitTimeSpinner = (Spinner) findViewById(R.id.split_time_spinner);
		Spinner whenSpinner = (Spinner) findViewById(R.id.when_to_gen);

		ArrayAdapter<CharSequence> totalHoursAdapter = ArrayAdapter
				.createFromResource(this, R.array.temp_amount_hours_values,
						android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> splitTimeAdapter = ArrayAdapter
				.createFromResource(this, R.array.temp_duration_values,
						android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> whenAdapter = ArrayAdapter
				.createFromResource(this, R.array.when_to_gen,
						android.R.layout.simple_spinner_item);

		totalHoursAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		splitTimeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		whenAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		totalHoursSpinner.setAdapter(totalHoursAdapter);
		splitTimeSpinner.setAdapter(splitTimeAdapter);
		whenSpinner.setAdapter(whenAdapter);

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_block, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			onBackPressed();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		/* Set data of the object */
		Log.d("ADDING BLOCK", "TEST");

		// Title
		EditText taskTitle = (EditText) findViewById(R.id.block_title);
		String name = taskTitle.getText().toString();

		// // Description
		// EditText taskDesc = (EditText) findViewById(R.id.block_description);
		// String comment = taskDesc.getText().toString();
		//
		// // Important
		// CheckBox important = (CheckBox) findViewById(R.id.block_important);
		// boolean importantTask = important.isChecked();

		// Hours
		Spinner hourSpinner = (Spinner) findViewById(R.id.block_hours_spinner);
		ArrayAdapter<CharSequence> hoursAdapter = (ArrayAdapter<CharSequence>) hourSpinner
				.getAdapter();
		String hoursString = (String) hoursAdapter.getItem(hourSpinner
				.getSelectedItemPosition());
		int hours;
		if (!hoursString.equals("Custom")) {
			hours = Integer.parseInt(hoursString);
		} else {
			hours = 50;
		}

		// Session minutes
		Spinner sessionSpinner = (Spinner) findViewById(R.id.split_time_spinner);
		ArrayAdapter<CharSequence> sessionSpinnerAdapter = (ArrayAdapter<CharSequence>) sessionSpinner
				.getAdapter();
		String sessionString = (String) sessionSpinnerAdapter
				.getItem(hourSpinner.getSelectedItemPosition());
		int sessionMinutes;
		if (!sessionString.equals("Custom")) {
			sessionMinutes = Integer.parseInt(hoursString);
		} else {
			sessionMinutes = 200;
		}

		// Session minutes
		Spinner timeOfDaySpinner = (Spinner) findViewById(R.id.when_to_gen);
		// ArrayAdapter<CharSequence> timeOfDaySpinnerAdapter =
		// (ArrayAdapter<CharSequence>) timeOfDaySpinner.getAdapter();
		int timeOfDayPosition = timeOfDaySpinner.getSelectedItemPosition();
		TimeWindow timeWindow;
		switch (timeOfDayPosition) {
		case 0:
			timeWindow = TimeWindow.WORKING;
			break;
		case 1:
			timeWindow = TimeWindow.LEISURE;
			break;
		default:
			timeWindow = TimeWindow.ALL;
			break;
		}

		// If a name is set, create BlockObject
		if (name.trim().length() != 0) {
			IBlockObject newBlock = new BlockObject(name, timeWindow, hours,
					sessionMinutes);
			DBHandler db = new DBHandler(this);
			db.addBlock(newBlock);
			db.close();
			Log.d("ADDING BLOCK", "Block={ " + "name=" + name + ", timeWindow="
					+ timeWindow + ", hours=" + hours + ", sessionMinutes="
					+ sessionMinutes + " }");
		}

		super.onBackPressed();
	}
}
