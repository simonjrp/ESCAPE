package se.chalmers.dat255.group22.escape;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class NewTaskActivity extends Activity {

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

        // Time Alarm

        // GPS Alarm

        Category newCategory = new Category(category, "Random Color", "Another random Color");


		ListObject lo = new ListObject(1, name);
        if(comment.trim().length() != 0)
		    lo.setComment(comment);
		lo.setImportant(importantTask);
        lo.addToCategory(newCategory);
        lo.setPlace(new Place(1, location));


		DBHandler dbHandler = new DBHandler(this);
		dbHandler.addListObject(lo);

		super.onBackPressed();
	}

	public void onAddReminder(View v) {

		v.setVisibility(View.INVISIBLE);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.task_details_layout);

		Spinner dateSpinner = new Spinner(this);
		dateSpinner.setId(1);

		// create adapter for date spinner
		ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter
				.createFromResource(this, R.array.test_dates,
                        android.R.layout.simple_spinner_item);
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dateSpinner.setAdapter(dateAdapter);

		Spinner timeSpinner = new Spinner(this);
		timeSpinner.setId(2);

		// create adapter for time spinner
		ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter
				.createFromResource(this, R.array.test_times,
                        android.R.layout.simple_spinner_item);
		timeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(timeAdapter);

		// set layout parameters for date and time spinners
		RelativeLayout.LayoutParams paramsDate = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsDate.addRule(RelativeLayout.BELOW, R.id.task_reminders_list);
		paramsDate.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		RelativeLayout.LayoutParams paramsTime = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsTime.addRule(RelativeLayout.BELOW, 1);
		paramsTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		// add spinners with corresponding parameters
		layout.addView(dateSpinner, paramsDate);
		layout.addView(timeSpinner, paramsTime);

	}
}
