package se.chalmers.dat255.group22.escape.test;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.TabsPagerAdapter;
import se.chalmers.dat255.group22.escape.TestFragment;
import se.chalmers.dat255.group22.escape.ListFragment.ExpandableEventListFragment;
import se.chalmers.dat255.group22.escape.ListFragment.TaskListFragment;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;

public class TestTabsPagerAdapter extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity mainActivity;
	private TabsPagerAdapter tPA;
	private String[] fragmentTitles;

	public static final int TASKS_PAGER_POS = 0;
	public static final int EVENTS_PAGER_POS = 1;
	public static final int POMODORO_PAGER_POS = 2;

	public static final int NBR_OF_PAGER_FRAGMENTS = 3;

	public TestTabsPagerAdapter() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
		ViewPager pager = (ViewPager) mainActivity
				.findViewById(se.chalmers.dat255.group22.escape.R.id.view_pager);
		tPA = (TabsPagerAdapter) pager.getAdapter();
		fragmentTitles = mainActivity.getResources().getStringArray(
				se.chalmers.dat255.group22.escape.R.array.fragments_array);
	}

	public void testPreCondition() {
		assertTrue(tPA != null);
		assertTrue(fragmentTitles != null);
		assertEquals(fragmentTitles.length, NBR_OF_PAGER_FRAGMENTS);
	}

	public void testGetItem() {
		Fragment tasks = tPA.getItem(TASKS_PAGER_POS);
		Fragment events = tPA.getItem(EVENTS_PAGER_POS);
		Fragment pomodoro = tPA.getItem(POMODORO_PAGER_POS);
		assertEquals(tasks.getClass(), TaskListFragment.class);
		assertEquals(events.getClass(), ExpandableEventListFragment.class);
		assertEquals(pomodoro.getClass(), TestFragment.class);
	}

	public void testGetCount() {
		assertEquals(tPA.getCount(), NBR_OF_PAGER_FRAGMENTS);
	}

	public void testGetPageTitle() {
		CharSequence tasksTitle = tPA.getPageTitle(TASKS_PAGER_POS);
		CharSequence eventsTitle = tPA.getPageTitle(EVENTS_PAGER_POS);
		CharSequence pomodoroTitle = tPA.getPageTitle(POMODORO_PAGER_POS);
		assertEquals(tasksTitle, fragmentTitles[TASKS_PAGER_POS]);
		assertEquals(eventsTitle, fragmentTitles[EVENTS_PAGER_POS]);
		assertEquals(pomodoroTitle, fragmentTitles[POMODORO_PAGER_POS]);
	}

}
