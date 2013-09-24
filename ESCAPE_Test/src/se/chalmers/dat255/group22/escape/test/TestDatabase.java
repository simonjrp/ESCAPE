package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
import java.util.List;

import se.chalmers.dat255.group22.escape.Category;
import se.chalmers.dat255.group22.escape.DBHandler;
import se.chalmers.dat255.group22.escape.GPSAlarm;
import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.Place;
import se.chalmers.dat255.group22.escape.Time;
import se.chalmers.dat255.group22.escape.TimeAlarm;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
	
	// GPS alarm
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

	// Time Alarm
	public void testAddTimeAlarm() {
		Date date = new Date(1000l);
		TimeAlarm ta = new TimeAlarm(1, date);
		db.addTimeAlarm(ta);
		
		List<TimeAlarm> list = db.getAllTimeAlarms();
		assertEquals(date, list.get(0).getDate());
	}
	
	public void testEditTimeAlarm() {
		Date date = new Date(1000l);
		TimeAlarm ta = new TimeAlarm(1, date);
		db.addTimeAlarm(ta);
		
		ta.getDate().setTime(2000l);
		db.updateTimeAlarm(ta);
		
		List<TimeAlarm> list = db.getAllTimeAlarms();
		assertEquals(date, list.get(0).getDate());
	}
	
	public void testRemoveTimeAlarm() {
		Date date = new Date(1000l);
		TimeAlarm ta = new TimeAlarm(1, date);
		db.addTimeAlarm(ta);
		
		db.deleteTimeAlarm(ta);
		
		List<TimeAlarm> list = db.getAllTimeAlarms();
		assertEquals(true, list.isEmpty());
	}
	
	// Testing CategoriesWithListObject
	public void testAddCategoryWithListObject() {
		Category category = new Category("test", "color", "color2");
		ListObject lo = new ListObject(1, "listobject");
		db.addCategory(category);
		db.addListObject(lo);
		
		db.addCategoryWithListObject(category, lo);
		
		List<Category> list = db.getCategories(lo);
		List<ListObject> list2 = db.getListObjects(category);
		assertEquals(category, list.get(0));
		assertEquals(lo, list2.get(0));
	}
	
	public void testRemoveCategoryWithListObject() {
		Category category = new Category("test", "color", "color2");
		ListObject lo = new ListObject(1, "listobject");
		db.addCategory(category);
		db.addListObject(lo);
		
		db.addCategoryWithListObject(category, lo);
		db.deleteCategoryWithListObject(category, lo);
		
		List<Category> list = db.getCategories(lo);
		List<ListObject> list2 = db.getListObjects(category);
		assertEquals(true, list.isEmpty());
		assertEquals(true, list2.isEmpty());
	}
	
	// Testing ListObjectWithTimeAlarm
	
	// Testing ListObjectsWithGPSAlarm
	public void testAddListObjectsWithGPSAlarm() {
		double longitude = 1d;
		double latitude = 2d;
		GPSAlarm gpsAlarm = new GPSAlarm(1, longitude, latitude);
		ListObject lo = new ListObject(1, "listobject");
		db.addGPSAlarm(gpsAlarm);
		db.addListObject(lo);
		
		db.addListObjectsWithGPSAlarm(lo, gpsAlarm);
		//TODO saknas getmetoder
		List<GPSAlarm> list = db.getGPSAlarm(lo);
		assertEquals(gpsAlarm, list.get(0));
	}
	
	public void testEditListObjectsWithGPSAlarm() {
		double longitude1 = 1d;
		double longitude2 = 2d;
		double latitude1 = 3d;
		double latitude2 = 4d;
		GPSAlarm gpsAlarm1 = new GPSAlarm(1, longitude1, latitude1);
		GPSAlarm gpsAlarm2 = new GPSAlarm(1, longitude2, latitude2);
		ListObject lo = new ListObject(1, "listobject");
		
		db.addGPSAlarm(gpsAlarm1);
		db.addGPSAlarm(gpsAlarm2);
		db.addListObject(lo);
		
		db.addListObjectsWithGPSAlarm(lo, gpsAlarm1);
		
		db.updateListObjectWithGPSAlarm(lo, gpsAlarm2);
		assertEquals(gpsAlarm2, getGPSAlarm(lo));
		
	}
	
	// Testing ListObjectsWithTime
	public void testAddListObjectsWithTime() {
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);
		ListObject lo = new ListObject(1, "listobject");
		db.addTime(time);
		db.addListObject(lo);
		
		db.addListObjectsWithTime(lo, time);
		//TODO saknas getmetoder
		List<Time> list = db.getTime(lo);
		assertEquals(time, list.get(0));
		
		
		
	}
	
	public void testEditListObjectsWithTime() {
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Date date3 = new Date(3l);
		Date date4 = new Date(4l);
		Time time1 = new Time(1, date1, date2);	
		Time time2 = new Time(1, date3, date4);
		ListObject lo = new ListObject(1, "listobject");
		
        db.addTime(time1);
        db.addTime(time2);
        db.addListObject(lo);
        
        db.addListObjectsWithTime(lo, time1);
        
        db.updateListObjectWithTime(lo, time2);
		
        assertEquals(time2, getTime(lo));
		
	}
	
	// Testing ListObjectsWithPlace
	public void testAddListObjectsWithPlace() {
		Place place = new Place(1, "Test");
		db.addPlace(place);
		ListObject lo = new ListObject(1, "listobject");
		
		db.addListObjectsWithPlace(lo, place);
		//TODO saknas getmetoder
		List<Place> list = db.getPlace(lo);
		assertEquals(place, list.get(0));
	}

	public void testEditListObjectsWithPlace() {
		Place place1 = new Place(1, "Test1");
		Place place2 = new Place(1, "Test2");
		ListObject lo = new ListObject(1, "listobject");
		
		db.addPlace(place1);
		db.addPlace(place2);
		db.addListObject(lo);
        
        db.addListObjectsWithPlace(lo, place1);        
        db.updateListObjectWithPlace(lo, place2);
		
        assertEquals(place2, getPlace(lo));
		
	}
	
		
	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

}
