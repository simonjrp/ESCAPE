package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The main activity, to be launched when app is started.
 */
public class MainActivity extends FragmentActivity {

	private String[] drawerTitles;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence title;
	private CharSequence drawerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Configure the navigation drawer
		drawerTitles = getResources().getStringArray(R.array.drawer_titles);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, drawerTitles));
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		title = drawerTitle = getTitle();

		// Creates a new listener for drawer opened and closed events (to set
		// title and hide icons in actionbar etc)
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(title);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View view) {
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Send item selected event to the drawer toggle (to handle click on the
		// back/up button in the actionbar). If back/up button was pressed, the
		// drawer toggle will return true, and false otherwised (i.e some other
		// button in action bar was pressed)
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle all action bar items except for the back/up button here.
		switch (item.getItemId()) {
		case R.id.add_task:
			Intent intent = new Intent(this, NewTaskActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	/*
	 * Called when invalidateOptionsMenu() is called.
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Hide all buttons in actionbar if navigation drawer is open
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		menu.findItem(R.id.add_task).setVisible(!drawerOpen);
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		private List<Fragment> fragmentList;
		
		public DrawerItemClickListener() {
			selectItem(0);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}

		private void selectItem(int position) {
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();

				// Add all wanted fragments here
				fragmentList.add(new TasksEventsFragment());
				fragmentList.add(new BlocksFragment());
				fragmentList.add(new PomodoroFragment());
			}

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragmentList.get(position))
					.commit();

			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position]);
			drawerLayout.closeDrawer(drawerList);

		}

		private void setTitle(String string) {
			title = string;

		}
	}
}
