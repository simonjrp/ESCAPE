package se.chalmers.dat255.group22.escape.test;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.adapters.TabsPagerAdapter;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

public class TestMainActivity extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private ViewPager viewPager;
	private TabsPagerAdapter pagerAdapter;
	public static final int ADAPTER_COUNT = 3;

	public TestMainActivity() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// disable touch mode on the device/vm
		setActivityInitialTouchMode(false);

		// get basic objects needed in the test
		mActivity = getActivity();
		viewPager = (ViewPager) mActivity
				.findViewById(se.chalmers.dat255.group22.escape.R.id.view_pager);
		pagerAdapter = (TabsPagerAdapter) viewPager.getAdapter();
	}

	public void testPreConditions() {
		assertTrue(viewPager != null);
		assertTrue(pagerAdapter != null);
		assertEquals(pagerAdapter.getCount(), ADAPTER_COUNT);
	}

}
