package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;

import se.chalmers.dat255.group22.escape.ListFragment.CustomExpandableListAdapter;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * A test for some methods in CustomExpandableListAdapter. The test requires
 * ListObject.java and DBHandler.java to be functional!
 * 
 * @author Carl Jansson
 */
public class TestCustomExpandableListAdapter extends AndroidTestCase {

	private CustomExpandableListAdapter adapter;
	private DBHandler db;
	long taskID;
	long todayEventID;
	long tomorrowEventID;
	long somedayEventID;
	static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		// Create a new empty DBHandler so previously saved events wont
		// interfere with the test
		db = new DBHandler(context);
		adapter = new CustomExpandableListAdapter(context);

		ListObject tmpTask = new ListObject(1, "tmpTask");
		ListObject tmpEventToday = new ListObject(2, "tmpToday");
		ListObject tmpEventTomorrow = new ListObject(2, "tmpTomorrow");
		ListObject tmpEventSomeday = new ListObject(2, "tmpSomeday");
		Time timeToday = new Time(0, new Date(System.currentTimeMillis()), null);
		Time timeTomorrow = new Time(0, new Date(System.currentTimeMillis()
				+ DAY_IN_MILLIS), null);
		Time timeSomeday = new Time(0, new Date(System.currentTimeMillis() + 2
				* DAY_IN_MILLIS), null);
		taskID = db.addListObject(tmpTask);
		todayEventID = db.addListObjectsWithTime(tmpEventToday, timeToday);
		tomorrowEventID = db.addListObjectsWithTime(tmpEventTomorrow,
				timeTomorrow);
		somedayEventID = db
				.addListObjectsWithTime(tmpEventSomeday, timeSomeday);
	}

	public void testReInit() {
		// Reads from database and places events in correct category
	}

	public void testaddListObject() {
		// sorts into correct header
	}

	public void testAddListObjectToday() {

	}

	public void testAddListObjectTomorrow() {

	}

	public void testAddListObjectThisWeek() {

	}

	public void testRemoveListObjectToday() {

	}

	public void testRemoveListObjectTomorrow() {

	}

	public void testRemoveListObjectThisWeek() {

	}

	public void tesIsToday() {

	}

	public void testIsTomorrow() {

	}

	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}
}
