package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
import java.util.List;

import se.chalmers.dat255.group22.escape.Category;
import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.Place;
import se.chalmers.dat255.group22.escape.Time;
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
	
	// ListObject tests
	public void testAddEntry(){
		ListObject lo = new ListObject(1, "Test");
		
        db.addListObject(lo);
        List<ListObject> list = db.getAllListObjects();
        assertEquals(lo, list.get(0));
    }
	
	public void testEditEntry(){ 
		List<ListObject> list = db.getAllListObjects();
        ListObject lo = list.get(0);
        lo.setName("Test2");
        
        db.updateListObject(lo);
        list = db.getAllListObjects();
        assertEquals(lo.getName(), list.get(0).getName());
    }

	
	public void testRemoveEntry(){
		ListObject lo = db.getListObject(1l);
		db.deleteListObject(lo);
		List<ListObject> list = db.getAllListObjects();
		assertEquals(true, list.isEmpty());
		
	}
	
	// Categories tests
	public void testAddCategory(){
		Category category = new Category("Test1", "Color1", "Color");
		
        db.addCategory(category);
        List<Category> list = db.getAllCategories();
        assertEquals(category, list.get(0));
    }
	
	public void testEditCategory(){ 
		Category category = new Category("Test1", "Color1", "Color");
        db.addCategory(category);
		List<Category> list = db.getAllCategories();
        category.setName("Test2");
        db.updateCategory(category, "Test1");
        list = db.getAllCategories();
        assertEquals(category.getName(), list.get(0).getName());
    }

	public void testRemoveCategory(){
		List<Category> list = db.getAllCategories();
		Category category = list.get(0);
		db.deleteCategory(category);
		assertEquals(true, list.isEmpty());
	}
	
	//Places tests
	public void testAddPlace(){
		Place place = new Place(1, "Test");
		
        db.addPlace(place);
        List<Place> list = db.getAllPlaces();
        assertEquals(place, list.get(0));
    }
	
	public void testEditPlace(){ 
		List<Place> list = db.getAllPlaces();
        Place place = list.get(0);
        place.setName("Test2");
        
        db.updatePlaces(place);
        list = db.getAllPlaces();
        assertEquals(place.getName(), list.get(0).getName());
    }

	
	public void testRemovePlace(){
		List<Place> list = db.getAllPlaces();
		Place place = list.get(0);
		db.deletePlace(place);
		assertEquals(true, list.isEmpty());
	}
	
	//Time tests
	public void testAddTime(){
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);
		
        db.addTime(time);
        List<Time> list = db.getAllTimes();
        assertEquals(time, list.get(0));
    }
	
	public void testEditTime(){ 
		Date date3 = new Date(3l);
		List<Time> list = db.getAllTimes();
        Time time = list.get(0);
        time.setEndDate(date3);
        db.updateTimes(time);
        list = db.getAllTimes();
        assertEquals(time.getEndDate(), list.get(0).getEndDate());
    }

	
	public void testRemoveTime(){
		List<Time> list = db.getAllTimes();
		Time time = list.get(0);
		db.deleteTime(time);
		assertEquals(true, list.isEmpty());
	}
		
	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

}
