package se.chalmers.dat255.group22.escape.fragments;

import java.lang.reflect.Field;

import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.adapters.TabsPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment containing a viewpager (swipeable tabs) holding
 * ExpandableEventListFragment and TaskListFragment.
 * 
 * @author Simon Persson
 * @see se.chalmers.dat255.group22.escape.fragments.listfragments.ExpandableEventListFragment
 * @see se.chalmers.dat255.group22.escape.fragments.listfragments.TaskListFragment
 * 
 */
public class TasksEventsFragment extends Fragment {
	private TabsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private FragmentActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tasks_events_fragment, container,
				false);
		activity = (FragmentActivity) getActivity();
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		pagerAdapter = new TabsPagerAdapter(this.getChildFragmentManager(),
				activity);

		viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
		viewPager.setAdapter(pagerAdapter);

		viewPager.setCurrentItem(TabsPagerAdapter.TASKS_FRAGMENT);

	}

	/*
	 * This is a little hack necessary to reset the MainActivity's child
	 * fragment manager after this fragment is detached and get a new one every
	 * time it's attached again. Otherwise the child fragment manager tries to
	 * reuse old fragments (Task and Event fragment) that's already been
	 * destroyed and throws an exception.
	 * 
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
