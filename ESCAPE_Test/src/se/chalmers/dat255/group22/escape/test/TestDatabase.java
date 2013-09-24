package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
import java.util.List;

import se.chalmers.dat255.group22.escape.Category;
import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.GPSAlarm;
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
		ListObject lo = new ListObject(1, "Test");
        db.addListObject(lo);
		lo.setName("Test2");        		
		db.updateListObject(lo);
        List<ListObject> list = db.getAllListObjects();
        assertEquals(lo.getName(), list.get(0).getName());
    }

	
	public void testRemoveEntry(){
		ListObject lo = new ListObject(1, "Test");
        db.addListObject(lo);
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
        category.setName("Test2");
        db.updateCategory(category, "Test1");
        List<Category> list = db.getAllCategories();
        assertEquals(category.getName(), list.get(0).getName());
    }

	public void testRemoveCategory(){
		Category category = new Category("Test1", "Color1", "Color");
        db.addCategory(category);
		db.deleteCategory(category);
		List<Category> list = db.getAllCategories();
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
		Place place = new Place(1, "Test");
		db.addPlace(place);
        place.setName("Test2");
        db.updatePlaces(place);
        List<Place> list = db.getAllPlaces();
        assertEquals(place.getName(), list.get(0).getName());
    }

	
	public void testRemovePlace(){
		Place place = new Place(1, "Test");
		db.addPlace(place);
		db.deletePlace(place);
		List<Place> list = db.getAllPlaces();
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
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Date date3 = new Date(3l);
		Time time = new Time(1, date1, date2);	
        db.addTime(time);
        time.setEndDate(date3);
        db.updateTimes(time);
        List<Time> list = db.getAllTimes();
        assertEquals(time.getEndDate(), list.get(0).getEndDate());
    }

	
	public void testRemoveTime(){
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);	
        db.addTime(time);
		db.deleteTime(time);
		List<Time> list = db.getAllTimes();
		assertEquals(true, list.isEmpty());
	}
	
	public void testAddGPSAlarm() {
		GPSAlarm gps = new GPSAlarm(1, 1223.2222, 1333.333);
		db.addGPSAlarm(gps);
		
		List<GPSAlarm> list = db.getAllGPSAlarms();
		assertEquals(1223.2222, list.get(0).getLongitude());
		assertEquals(1333.333, list.get(0).getLatitude());
	}
	
	public void testEditGPSAlarm() {
		GPSAlarm gps = new GPSAlarm(1, 1223.2222, 1333.333);
		db.addGPSAlarm(gps);
		
		gps.setLatitude(113);
		db.updateGPSAlarm(gps);
		
		List<GPSAlarm> list = db.getAllGPSAlarms();
		assertEquals(113.0, list.get(0).getLatitude());
	}
	
	public void testRemoveGPSAlarm() {
		GPSAlarm gps = new GPSAlarm(1, 1223.2222, 1333.333);
		GPSAlarm gps2 = new GPSAlarm(2, 33, 22);
		db.addGPSAlarm(gps);
		db.addGPSAlarm(gps2);
		
		db.deleteGPSAlarm(gps);
		
		List<GPSAlarm> list = db.getAllGPSAlarms();
		assertEquals(1, list.size());
	}
		
	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

}
