package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
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
public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
	// Variable to store application name
	private CharSequence mTitle;
	// Variable to store current drawer title
	private CharSequence mDrawerTitle;

    private List<Fragment> fragments;
	private String[] fragmentTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        fragments = new ArrayList<Fragment>();


		// Saving title of application for later use
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

		};

        mDrawerLayout.setDrawerListener(mDrawerToggle);

		fragmentTitles = getResources().getStringArray(R.array.fragments_array);

		// Sets which items the ListView should show
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, fragmentTitles));

		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectFragment(position);

			}

		});
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

        initializeFragments();

        // begin to show todo fragment
        if(savedInstanceState == null) {
            selectFragment(0);
        }
	}

    private void initializeFragments() {

        for(int i = 0; i < fragmentTitles.length; i++) {
            if(i == 0) {
                Fragment fragment = new ExpandableListFragment();
                Bundle args = new Bundle();
                args.putString("TITLE", fragmentTitles[i]);
                fragment.setArguments(args);
                fragments.add(fragment);
            }else {
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
		mDrawerList.setItemChecked(position, true);
		setTitle(fragmentTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mDrawerTitle = title;
		super.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
     // Handle your other action bar items...
        
        switch (item.getItemId()){
        case R.id.add_task:
        	Intent intent = new Intent(this, NewTaskActivity.class);
        	startActivity(intent);
        }
        
      
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        boolean todoFragmentActive = mDrawerList.getCheckedItemPosition() == 0;
        return super.onPrepareOptionsMenu(menu);
    }
}
