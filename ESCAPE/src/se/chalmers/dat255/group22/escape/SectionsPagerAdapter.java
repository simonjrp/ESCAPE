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
    private String[] fragmentTitles;
	private List<Fragment> fragmentList;

	/**
	 * Constructor for creating a new SectionsPagerAdapter.
	 * 
	 * @param fragmentManager
	 *            The fragment manager that's used for fragment transactions
	 */
	public SectionsPagerAdapter(FragmentManager fragmentManager, String[] fragmentTitles) {
		super(fragmentManager);

        this.fragmentTitles = fragmentTitles;

        for(int i = 0; i < fragmentTitles.length; i++) {
            if(i == 0) {
                ExpandableListFragment listFragment = new ExpandableListFragment();
                fragmentList.add(listFragment);
            }else {
                TestFragment testFragment = new TestFragment();
                Bundle args = new Bundle();
                args.putString("TITLE", fragmentTitles[i]);
                testFragment.setArguments(args);
                fragmentList.add(testFragment);
            }
        }
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
