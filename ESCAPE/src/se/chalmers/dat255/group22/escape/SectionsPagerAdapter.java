package se.chalmers.dat255.group22.escape;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 9/23/13.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;
    public SectionsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ExpandableListFragment());

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
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        //TODO make non-static
        return 3;
    }
}
