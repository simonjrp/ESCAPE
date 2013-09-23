package se.chalmers.dat255.group22.escape.test;

import java.util.List;

import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.ListObject;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class TestDatabase extends AndroidTestCase {

	private DBHandler db;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context 
        = new RenamingDelegatingContext(getContext(), "test_");
        db = new DBHandler(context);
	}
	
	public void testAddEntry(){
		ListObject lo = new ListObject(1, "Test");
		
        db.addListObject(lo);
        List<ListObject> list = db.getAllListObjects();
        assertEquals(lo, list.get(0));
    }
	
//	public void testEditEntry(){
//		ListObject lo = new ListObject(1, "Test");
//		
//        db.addListObject(lo);
//        List<ListObject> list = db.getAllListObjects();
//        assertEquals(lo, list.get(0));
//    }

	
	public void testRemoveEntry(){
		ListObject lo = db.getListObject(1l);
		db.deleteListObject(lo);
		List<ListObject> list = db.getAllListObjects();
		assertEquals(true, list.isEmpty());
		
	}
	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

}
