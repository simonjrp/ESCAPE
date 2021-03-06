package se.chalmers.dat255.group22.escape.adapters;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.fragments.listfragments.ExpandableEventListFragment;
import se.chalmers.dat255.group22.escape.fragments.listfragments.TaskListFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A pager adapter to use with a viewpager (swipeable tabs).
 * 
 * @author Simon Persson
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	private String[] fragmentTitles;
	private List<Fragment> fragmentList;

	public static final int TASKS_FRAGMENT = 0;
	public static final int EVENTS_FRAGMENT = 1;

	/**
	 * Creates a new TabsPagerAdapter.
	 * 
	 * @param fragmentManager
	 *            The fragment manager to be used by the adapter.
	 * @param context
	 *            The context to be used for reading predefined string arrays
	 *            from the /res folder.
	 */
	public TabsPagerAdapter(FragmentManager fragmentManager, Context context) {
		super(fragmentManager);

		fragmentTitles = context.getResources().getStringArray(
				R.array.fragments_array);
		fragmentList = new ArrayList<Fragment>();

		// Create all wanted fragments and add them to fragmentList here

		Fragment eventListFragment = new ExpandableEventListFragment();
		fragmentList.add(eventListFragment);

		Fragment taskListFragment = new TaskListFragment();
		fragmentList.add(taskListFragment);

	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {

		return fragmentTitles[position];
	}

}
