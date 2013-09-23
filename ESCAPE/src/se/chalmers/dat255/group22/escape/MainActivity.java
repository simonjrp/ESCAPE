package se.chalmers.dat255.group22.escape;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The main activity, to be launched when app is started.
 */
public class MainActivity extends Activity {

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

		// begin to show todo fragment
		if (savedInstanceState == null) {
			selectFragment(0);
		}
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

	/**
	 * Method for selecting which fragment to be shown
	 * 
	 * @param position
	 *            The position in the listview of the wanted fragment
	 */
	public void selectFragment(int position) {

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragments.get(position)).commit();
		setTitle(fragmentTitles[position]);
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
