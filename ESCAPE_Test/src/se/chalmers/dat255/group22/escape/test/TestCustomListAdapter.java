package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;

import se.chalmers.dat255.group22.escape.ListFragment.CustomListAdapter;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * A test for some methods in CustomListAdapter. The test requires
 * ListObject.java and DBHandler.java to be functional!
 * 
 * @author Carl Jansson
 */
public class TestCustomListAdapter extends AndroidTestCase {
	private CustomListAdapter adapter;
	private DBHandler db;
	long taskID;
	long eventID;

	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		// Create a new empty DBHandler so previously saved events wont
		// interfere with the test
		db = new DBHandler(context);
		adapter = new CustomListAdapter(context);

		ListObject tmpTask = new ListObject(1, "tmpTask");
		ListObject tmpEvent = new ListObject(2, "tmpEvent");
		Time tmpTime = new Time(0, new Date(System.currentTimeMillis()), null);
		taskID = db.addListObject(tmpTask);
		eventID = db.addListObjectsWithTime(tmpEvent, tmpTime);
	}

	public void testReInit() {
		assertEquals(true, adapter.getCount() == 0);
		assertEquals(true, adapter.isEmpty());
		adapter.reInit();
		assertEquals(true, adapter.getCount() == 1);
		assertEquals(false, adapter.isEmpty());
		assertEquals(true,
				adapter.getListObject(0).equals(db.getListObject(taskID)));
		assertEquals(true, db.getListObject(taskID).equals(adapter.getItem(0)));
		assertEquals(true, (adapter.getListObject(0) instanceof ListObject));
		assertEquals(true, db.getAllListObjects().size() == 1);
	}

	public void testAddListObject() {
		assertEquals(true, adapter.getCount() == 0);
		assertEquals(true, adapter.isEmpty());
		adapter.addListObject(db.getListObject(taskID));
		assertEquals(true, adapter.getCount() == 1);
		assertEquals(false, adapter.isEmpty());
		// test so the task can't be added 2 times!
		adapter.addListObject(db.getListObject(taskID));
		assertEquals(true, adapter.getCount() == 1);
		assertEquals(false, adapter.isEmpty());
		assertEquals(true,
				adapter.getListObject(0).equals(db.getListObject(taskID)));
		assertEquals(true, db.getListObject(taskID).equals(adapter.getItem(0)));
		// test adding another task
		adapter.addListObject(db.getListObject(eventID));
		assertEquals(true, adapter.getCount() == 2);
		assertEquals(false, adapter.isEmpty());
	}

	public void testRemoveListObject() {
		adapter.addListObject(db.getListObject(taskID));
		assertEquals(true, adapter.getCount() == 1);
		assertEquals(false, adapter.isEmpty());
		// remove 1 list item
		adapter.removeListObject(db.getListObject(taskID));
		assertEquals(true, adapter.getCount() == 0);
		assertEquals(true, adapter.isEmpty());
		adapter.addListObject(db.getListObject(taskID));
		adapter.addListObject(db.getListObject(eventID));
		assertEquals(true, adapter.getCount() == 2);
		assertEquals(false, adapter.isEmpty());
		// remove 1 of 2 list items
		adapter.removeListObject(db.getListObject(eventID));
		assertEquals(true, adapter.getCount() == 1);
		assertEquals(false, adapter.isEmpty());
		assertEquals(true,
				adapter.getListObject(0).equals(db.getListObject(taskID)));
		assertEquals(true, db.getListObject(taskID).equals(adapter.getItem(0)));
	}

	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}
}