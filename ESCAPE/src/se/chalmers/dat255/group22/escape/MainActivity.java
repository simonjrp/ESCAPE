package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.fragments.PomodoroFragment;
import se.chalmers.dat255.group22.escape.fragments.TasksEventsFragment;
import se.chalmers.dat255.group22.escape.fragments.listfragments.BlockListFragment;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main activity, to be launched when app is started.
 * 
 * @author Carl, Erik, Mike, Johanna, Simon Persson
 */
public class MainActivity extends FragmentActivity {

	private String[] drawerTitles;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private RelativeLayout drawerListLayout;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence title;
	private CharSequence drawerTitle;
	private int fragmentPosition;
	private boolean backPressedOnce;
	private AlertDialog aboutDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializes the notification handler with this context
		NotificationHandler.getInstance().init(this);

		// Configure the navigation drawer
		drawerTitles = getResources().getStringArray(R.array.drawer_titles);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer_list);
		drawerListLayout = (RelativeLayout) findViewById(R.id.left_drawer_layout);

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
		// drawer toggle will return true, and false otherwise (i.e some other
		// button in action bar was pressed)
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle all action bar items except for the back/up button here.
		Intent intent;
		switch (item.getItemId()) {
			case R.id.add_task :
				intent = new Intent(this, NewTaskActivity.class);
				startActivity(intent);
				break;
			case R.id.add_blocks :
				intent = new Intent(this, NewBlockActivity.class);
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
		// Hides the "New task" button in actionbar if navigation drawer is open
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListLayout);
		menu.findItem(R.id.add_task).setVisible(
				!drawerOpen && fragmentPosition == 0);

		menu.findItem(R.id.pick_category).setVisible(
				(!drawerOpen && fragmentPosition == 0));

		menu.findItem(R.id.add_blocks).setVisible(
				!drawerOpen && fragmentPosition == 1);

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

	/*
	 * Class for handling clicks in the navigation drawer.
	 */
	private class DrawerItemClickListener
			implements
				ListView.OnItemClickListener {

		private List<Fragment> fragmentList;

		/*
		 * Constructor for creating a new DrawerItemClickListener. Automatically
		 * selects and shows the first fragment in the fragments list
		 */
		public DrawerItemClickListener() {
			selectItem(0);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}

		/*
		 * Method for selecting/showing fragments when user clicks on an item in
		 * the navigation drawer.
		 * 
		 * @param position The position (in the fragments list) of the fragment
		 * to be shown.
		 */
		private void selectItem(int position) {
			fragmentPosition = position;
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();

				// Add all wanted fragments here
				fragmentList.add(new TasksEventsFragment());
				fragmentList.add(new BlockListFragment());
				fragmentList.add(new PomodoroFragment());
			}

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragmentList.get(position))
					.commit();

			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position]);
			drawerLayout.closeDrawer(drawerListLayout);

		}

		/*
		 * Method for setting the title of the app properly. Used to be able to
		 * have a dynamic app title.
		 * 
		 * @param string The new title of the app.
		 */
		private void setTitle(String string) {
			title = string;
		}
	}

	@Override
	public void onBackPressed() {
		/*
		 * Displays a toast about exiting the app if the user clicks on the back
		 * button one time. If the user clicks one more time in the next 2
		 * seconds, the application exits.
		 */
		if (backPressedOnce) {
			super.onBackPressed();
		} else {
			backPressedOnce = true;
			Toast.makeText(this, getString(R.string.back_button_hint),
					Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					backPressedOnce = false;
				}
			}, 2000);
		}

	}

	/**
	 * OnClick method for the about menu item in the navigation drawer.
	 * 
	 * @param view
	 *            The view that was clicked.
	 */
	public void onClickAbout(View view) {
		showAboutDialog();
	}

	/*
	 * Brings up an about dialog showing short description of the app.
	 */
	private void showAboutDialog() {
		if (aboutDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.about_dialog_title));
			builder.setIcon(R.drawable.ic_launcher);

			View dialogView = getLayoutInflater().inflate(
					R.layout.about_dialog, null, false);

			// Interprets html link tags correctly and makes links clickable
			TextView repoLink = (TextView) dialogView
					.findViewById(R.id.about_dialog_repo);
			repoLink.setMovementMethod(LinkMovementMethod.getInstance());

			TextView licenseLink = (TextView) dialogView
					.findViewById(R.id.about_dialog_license);
			licenseLink.setMovementMethod(LinkMovementMethod.getInstance());

			builder.setView(dialogView);

			aboutDialog = builder.create();
		}

		// Brings up the dialog
		aboutDialog.show();

	}

}
