package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;

import se.chalmers.dat255.group22.escape.adapters.CustomExpandableListAdapter;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * A test for some methods in CustomExpandableListAdapter. The test requires
 * ListObject.java, DBHandler.java and related data classes to be functional!
 * 
 * DataBase is used since some methods in adapter assume that every ListObject
 * originate in database and tries to get additional data from that source!
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
		ListObject tmpEventTomorrow = new ListObject(3, "tmpTomorrow");
		ListObject tmpEventSomeday = new ListObject(4, "tmpSomeday");
		taskID = db.addListObject(tmpTask);
		todayEventID = db.addListObject(tmpEventToday);
		tomorrowEventID = db.addListObject(tmpEventTomorrow);
		somedayEventID = db.addListObject(tmpEventSomeday);
		Time timeToday = new Time(0, new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()));
		Time timeTomorrow = new Time(1, new Date(System.currentTimeMillis()
				+ DAY_IN_MILLIS), new Date(System.currentTimeMillis()));
		Time timeSomeday = new Time(2, new Date(System.currentTimeMillis() + 2
				* DAY_IN_MILLIS), new Date(System.currentTimeMillis()));
		long tmpTimeToday = db.addTime(timeToday);
		long tmpTimeTomorrow = db.addTime(timeTomorrow);
		long tmpTimesomeday = db.addTime(timeSomeday);
		taskID = db.addListObject(tmpTask);
		db.addListObjectsWithTime(db.getListObject(todayEventID),
				db.getTime(tmpTimeToday));
		db.addListObjectsWithTime(db.getListObject(tomorrowEventID),
				db.getTime(tmpTimeTomorrow));
		db.addListObjectsWithTime(db.getListObject(somedayEventID),
				db.getTime(tmpTimesomeday));
	}

	// TODO method reInit() seems to be dependent on MainActivity actually being
	// MainActivity
	// public void testReInit() {adapter.reInit()}

	public void testaddListObject() {
		assertEquals(true, adapter.getGroupCount() == 3);
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(true, adapter.isEmpty());

		adapter.addListObject(db.getListObject(todayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(
				true,
				db.getListObject(todayEventID).equals(
						adapter.getListObjectToday(0)));
		assertEquals(false, adapter.isEmpty());

		adapter.addListObject(db.getListObject(tomorrowEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(
				true,
				db.getListObject(tomorrowEventID).equals(
						adapter.getListObjectTomorrow(0)));

		adapter.addListObject(db.getListObject(somedayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 1);
		assertEquals(
				true,
				db.getListObject(somedayEventID).equals(
						adapter.getListObjectThisWeek(0)));
	}

	public void testAddListObjectToday() {
		assertEquals(true, adapter.getGroupCount() == 3);
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(true, adapter.isEmpty());

		adapter.addListObjectToday(db.getListObject(todayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(
				true,
				db.getListObject(todayEventID).equals(
						adapter.getListObjectToday(0)));
		assertEquals(false, adapter.isEmpty());
	}

	public void testAddListObjectTomorrow() {
		assertEquals(true, adapter.getGroupCount() == 3);
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(true, adapter.isEmpty());

		adapter.addListObjectTomorrow(db.getListObject(tomorrowEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(
				true,
				db.getListObject(tomorrowEventID).equals(
						adapter.getListObjectTomorrow(0)));
		assertEquals(false, adapter.isEmpty());
	}

	public void testAddListObjectThisWeek() {
		assertEquals(true, adapter.getGroupCount() == 3);
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
		assertEquals(true, adapter.isEmpty());

		adapter.addListObjectThisWeek(db.getListObject(somedayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 1);
		assertEquals(
				true,
				db.getListObject(somedayEventID).equals(
						adapter.getListObjectThisWeek(0)));
		assertEquals(false, adapter.isEmpty());
	}

	public void testRemoveListObjectToday() {
		adapter.addListObjectToday(db.getListObject(todayEventID));
		adapter.addListObjectTomorrow(db.getListObject(tomorrowEventID));
		adapter.addListObjectThisWeek(db.getListObject(somedayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 1);
		adapter.removeListObjectToday(db.getListObject(todayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 0);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 1);

	}

	public void testRemoveListObjectTomorrow() {
		adapter.addListObjectToday(db.getListObject(todayEventID));
		adapter.addListObjectTomorrow(db.getListObject(tomorrowEventID));
		adapter.addListObjectThisWeek(db.getListObject(somedayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 1);
		adapter.removeListObjectTomorrow(db.getListObject(tomorrowEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 0);
		assertEquals(true, adapter.getChildrenCount(2) == 1);
	}

	public void testRemoveListObjectThisWeek() {
		adapter.addListObjectToday(db.getListObject(todayEventID));
		adapter.addListObjectTomorrow(db.getListObject(tomorrowEventID));
		adapter.addListObjectThisWeek(db.getListObject(somedayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 1);
		adapter.removeListObjectThisWeek(db.getListObject(somedayEventID));
		assertEquals(true, adapter.getChildrenCount(0) == 1);
		assertEquals(true, adapter.getChildrenCount(1) == 1);
		assertEquals(true, adapter.getChildrenCount(2) == 0);
	}

	public void testIsToday() {
		// time before today is supposed to be true 2013-09-02
		assertEquals(
				true,
				adapter.isToday(new Date(System.currentTimeMillis()
						- DAY_IN_MILLIS)));
		assertEquals(true,
				adapter.isToday(new Date(System.currentTimeMillis())));
		assertEquals(
				false,
				adapter.isToday(new Date(System.currentTimeMillis()
						+ DAY_IN_MILLIS)));
		assertEquals(
				false,
				adapter.isToday(new Date(System.currentTimeMillis() + 2
						* DAY_IN_MILLIS)));
	}

	public void testIsTomorrow() {
		assertEquals(
				false,
				adapter.isTomorrow(new Date(System.currentTimeMillis()
						- DAY_IN_MILLIS)));
		assertEquals(false,
				adapter.isTomorrow(new Date(System.currentTimeMillis())));
		assertEquals(
				true,
				adapter.isTomorrow(new Date(System.currentTimeMillis()
						+ DAY_IN_MILLIS)));
		assertEquals(
				false,
				adapter.isTomorrow(new Date(System.currentTimeMillis() + 2
						* DAY_IN_MILLIS)));
	}

	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}
}
