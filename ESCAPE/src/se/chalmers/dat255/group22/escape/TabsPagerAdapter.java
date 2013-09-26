package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import se.chalmers.dat255.group22.escape.ListFragment.ExpandableEventListFragment;
import se.chalmers.dat255.group22.escape.ListFragment.TaskListFragment;

/**
 * A pager adapter to use with a viewpager.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	// List of all fragments in the swipeable viewpager
	private String[] fragmentTitles;
	private List<Fragment> fragmentList;

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

		Fragment taskListFragment = new TaskListFragment();
		fragmentList.add(taskListFragment);

		Fragment eventListFragment = new ExpandableEventListFragment();
		fragmentList.add(eventListFragment);

		// TODO will be replaced by a pomodoro clock fragment
		TestFragment testFragment = new TestFragment();
		Bundle args = new Bundle();
		args.putString("TITLE", fragmentTitles[2]);
		testFragment.setArguments(args);
		fragmentList.add(testFragment);

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		// TODO make non-static
		return fragmentList.size();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public CharSequence getPageTitle(int position) {

		return fragmentTitles[position];
	}

}
