package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The main activity, to be launched when app is started.
 */
public class MainActivity extends FragmentActivity {

	// Variable to store application name
	private CharSequence mTitle;

	private List<Fragment> fragments;
	private String[] fragmentTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fragments = new ArrayList<Fragment>();

		// Saving title of application for later use
		mTitle = getTitle();

		fragmentTitles = getResources().getStringArray(R.array.fragments_array);

		initializeFragments();

		
		
	}

	private void initializeFragments() {

		for (int i = 0; i < fragmentTitles.length; i++) {
			if (i == 0) {
				Fragment fragment = new ExpandableListFragment();
				Bundle args = new Bundle();
				args.putString("TITLE", fragmentTitles[i]);
				fragment.setArguments(args);
				fragments.add(fragment);
			} else {
				Fragment fragment = new TestFragment();
				Bundle args = new Bundle();
				args.putString("TITLE", fragmentTitles[i]);
				fragment.setArguments(args);
				fragments.add(fragment);
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.add_task :
				Intent intent = new Intent(this, NewTaskActivity.class);
				startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}
