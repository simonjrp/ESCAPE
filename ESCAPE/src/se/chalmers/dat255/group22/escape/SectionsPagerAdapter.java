package se.chalmers.dat255.group22.escape;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A pager adapter to use with a viewpager.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	// List of all fragments in the swipeable viewpager
	private List<Fragment> fragmentList;

	/**
	 * Constructor for creating a new SectionsPagerAdapter.
	 * 
	 * @param fragmentManager
	 *            The fragment manager that's used for fragment transactions
	 */
	public SectionsPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);

		// Create all wanted fragments and add them to fragmentList here
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new ExpandableListFragment());

		// TODO temporary test fragments
		TestFragment testFragment1 = new TestFragment();
		Bundle bundle1 = new Bundle();
		bundle1.putString("TITLE", "Test 1");
		testFragment1.setArguments(bundle1);

		TestFragment testFragment2 = new TestFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putString("TITLE", "Test 2");
		testFragment2.setArguments(bundle2);

		fragmentList.add(testFragment1);
		fragmentList.add(testFragment2);
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
}
