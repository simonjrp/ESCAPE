package se.chalmers.dat255.group22.escape.test;

import se.chalmers.dat255.group22.escape.DBHandler;
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
        // Here i have my new database wich is not connected to the standard database of the App
    }

	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

}
