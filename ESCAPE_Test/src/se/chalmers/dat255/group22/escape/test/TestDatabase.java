package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
import java.util.List;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.GPSAlarm;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * This test is supposed to test all the methods in
 * se.chalmers.dat255.group22.escape.DBHandler
 * 
 * @author Mike & Johanna
 */
public class TestDatabase extends AndroidTestCase {

	private DBHandler db;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		db = new DBHandler(context);
	}

	// ListObject tests
	public void testAddEntry() {
		ListObject lo = new ListObject(1, "Test");
		db.addListObject(lo);
		List<ListObject> list = db.getAllListObjects();
		assertEquals(lo, list.get(0));
	}

	public void testEditEntry() {
		ListObject lo = new ListObject(1, "Test");
		db.addListObject(lo);
		lo.setName("Test2");
		db.updateListObject(lo);
		List<ListObject> list = db.getAllListObjects();
		assertEquals(lo.getName(), list.get(0).getName());
	}

	public void testRemoveEntry() {
		ListObject lo = new ListObject(1, "Test");
		db.addListObject(lo);
		db.deleteListObject(lo);
		List<ListObject> list = db.getAllListObjects();
		assertEquals(true, list.isEmpty());

	}

	// Categories tests
	public void testAddCategory() {
		Category category = new Category("Test1", "Color1", "Color");
		db.addCategory(category);
		List<Category> list = db.getAllCategories();
		assertEquals(category, list.get(0));
	}

	public void testEditCategory() {
		Category category = new Category("Test1", "Color1", "Color");
		db.addCategory(category);
		category.setName("Test2");
		db.updateCategory(category, "Test1");
		List<Category> list = db.getAllCategories();
		assertEquals(category.getName(), list.get(0).getName());
	}

	public void testRemoveCategory() {
		Category category = new Category("Test1", "Color1", "Color");
		db.addCategory(category);
		db.deleteCategory(category);
		List<Category> list = db.getAllCategories();
		assertEquals(true, list.isEmpty());
	}

	// Places tests
	public void testAddPlace() {
		Place place = new Place(1, "Test");
		db.addPlace(place);
		List<Place> list = db.getAllPlaces();
		assertEquals(place, list.get(0));
	}

	public void testEditPlace() {
		Place place = new Place(1, "Test");
		db.addPlace(place);
		place.setName("Test2");
		db.updatePlaces(place);
		List<Place> list = db.getAllPlaces();
		assertEquals(place.getName(), list.get(0).getName());
	}

	public void testRemovePlace() {
		Place place = new Place(1, "Test");
		db.addPlace(place);
		db.deletePlace(place);
		List<Place> list = db.getAllPlaces();
		assertEquals(true, list.isEmpty());
	}

	// Time tests
	public void testAddTime() {
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);
		db.addTime(time);
		List<Time> list = db.getAllTimes();
		assertEquals(time, list.get(0));
	}

	public void testEditTime() {
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

	public void testRemoveTime() {
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
		GPSAlarm gps = new GPSAlarm(1, "Johogatan, Göteborg", 1223.2222, 1333.333);
		db.addGPSAlarm(gps);

		List<GPSAlarm> list = db.getAllGPSAlarms();
		assertEquals(1223.2222, list.get(0).getLongitude());
		assertEquals(1333.333, list.get(0).getLatitude());
	}

	public void testEditGPSAlarm() {
		GPSAlarm gps = new GPSAlarm(1, "Johogatan, Göteborg", 1223.2222, 1333.333);
		db.addGPSAlarm(gps);

		gps.setLatitude(113);
		db.updateGPSAlarm(gps);

		List<GPSAlarm> list = db.getAllGPSAlarms();
		assertEquals(113.0, list.get(0).getLatitude());
	}

	public void testRemoveGPSAlarm() {
		GPSAlarm gps = new GPSAlarm(1, "Johogatan, Göteborg", 1223.2222, 1333.333);
		GPSAlarm gps2 = new GPSAlarm(2, "Johogatan, Göteborg", 33, 22);
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

	public void testDeleteCategoryWithListObject() {
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
	public void testAddListObjectWithTimeAlarm() {
		ListObject listObject = new ListObject(1, "listobject");
		Date date = new Date(1000l);
		TimeAlarm ta = new TimeAlarm(1, date);
		db.addListObject(listObject);
		db.addTimeAlarm(ta);

		db.addListObjectWithTimeAlarm(listObject, ta);

		TimeAlarm ta2 = db.getTimeAlarm(listObject);
		assertEquals(ta, ta2);
	}

	public void testEditListObjectWithTimeAlarm() {
		ListObject listObject = new ListObject(1, "listobject");
		Date date = new Date(1000l);
		TimeAlarm ta = new TimeAlarm(1, date);
		TimeAlarm ta2 = new TimeAlarm(2, date);
		db.addListObject(listObject);
		db.addTimeAlarm(ta);
		db.addTimeAlarm(ta2);

		db.addListObjectWithTimeAlarm(listObject, ta);

		db.updateListObjectWithTimeAlarm(listObject, ta2);

		assertEquals(ta2, db.getTimeAlarm(listObject));
	}

	public void testDeleteListObjectWithTimeAlarm() {
		ListObject listObject = new ListObject(1, "listobject");
		Date date = new Date(1000l);
		TimeAlarm ta = new TimeAlarm(1, date);
		db.addListObject(listObject);
		db.addTimeAlarm(ta);

		db.addListObjectWithTimeAlarm(listObject, ta);

		db.deleteListObjectWithTimeAlarm(listObject);

		assertEquals(null, db.getTimeAlarm(listObject));
	}

	// Testing ListObjectsWithGPSAlarm
	public void testAddListObjectWithGPSAlarm() {
		double longitude = 1d;
		double latitude = 2d;
		GPSAlarm gpsAlarm = new GPSAlarm(1, "Johogatan, Göteborg", longitude, latitude);
		ListObject lo = new ListObject(1, "listobject");
		db.addGPSAlarm(gpsAlarm);
		db.addListObject(lo);

		db.addListObjectWithGPSAlarm(lo, gpsAlarm);
		GPSAlarm test = db.getGPSAlarm(lo);
		assertEquals(gpsAlarm, test);
	}

	public void testEditListObjectWithGPSAlarm() {
		double longitude1 = 1d;
		double longitude2 = 2d;
		double latitude1 = 3d;
		double latitude2 = 4d;
		GPSAlarm gpsAlarm1 = new GPSAlarm(1, "Johogatan, Göteborg", longitude1, latitude1);
		GPSAlarm gpsAlarm2 = new GPSAlarm(1, "Johogatan, Göteborg", longitude2, latitude2);
		ListObject lo = new ListObject(1, "listobject");

		db.addGPSAlarm(gpsAlarm1);
		db.addGPSAlarm(gpsAlarm2);
		db.addListObject(lo);

		db.addListObjectWithGPSAlarm(lo, gpsAlarm1);

		db.updateListObjectWithGPSAlarm(lo, gpsAlarm2);
		assertEquals(gpsAlarm2, db.getGPSAlarm(lo));

	}

	public void testDeleteListObjectWithGPSAlarm() {
		double longitude = 1d;
		double latitude = 2d;
		GPSAlarm gpsAlarm = new GPSAlarm(1, "Johogatan, Göteborg", longitude, latitude);
		ListObject lo = new ListObject(1, "listobject");
		db.addGPSAlarm(gpsAlarm);
		db.addListObject(lo);

		db.addListObjectWithGPSAlarm(lo, gpsAlarm);
		assertEquals(gpsAlarm, db.getGPSAlarm(lo));

		db.deleteListObjectWithGPSAlarm(lo);
		assertEquals(null, db.getGPSAlarm(lo));
	}

	// Testing ListObjectsWithTime
	public void testAddListObjectWithTime() {
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);
		ListObject lo = new ListObject(1, "listobject");
		db.addTime(time);
		db.addListObject(lo);

		db.addListObjectsWithTime(lo, time);
		Time test = db.getTime(lo);
		assertEquals(time, test);

	}

	public void testEditListObjectWithTime() {
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

		assertEquals(time2, db.getTime(lo));

	}

	public void testRemoveListObjectWithTime() {
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);
		ListObject lo = new ListObject(1, "listobject");
		db.addTime(time);
		db.addListObject(lo);

		db.addListObjectsWithTime(lo, time);
		assertEquals(time, db.getTime(lo));

		db.deleteListObjectWithTime(lo);
		assertEquals(null, db.getTime(lo));

	}

	// Testing ListObjectsWithPlace
	public void testEditListObjectWithPlace() {
		Place place1 = new Place(1, "Test1");
		Place place2 = new Place(1, "Test2");
		ListObject lo = new ListObject(1, "listobject");

		db.addPlace(place1);
		db.addPlace(place2);
		db.addListObject(lo);

		db.addListObjectWithPlace(lo, place1);
		db.updateListObjectWithPlace(lo, place2);

		assertEquals(place2, db.getPlace(lo));
	}

	public void testAddListObjectWithPlace() {
		Place place = new Place(1, "Test");
		db.addPlace(place);
		ListObject lo = new ListObject(1, "listobject");
		db.addListObject(lo);

		db.addListObjectWithPlace(lo, place);
		Place test = db.getPlace(lo);
		assertEquals(place, test);
	}

	public void testRemoveListObjectWithPlace() {
		Place place = new Place(1, "Test");
		db.addPlace(place);
		ListObject lo = new ListObject(1, "listobject");
		db.addListObject(lo);

		db.addListObjectWithPlace(lo, place);
		assertEquals(place, db.getPlace(lo));

		db.deleteListObjectWithPlace(lo);
		assertEquals(null, db.getPlace(lo));
	}
	
	public void testGetListObjectsThatHaveTime() {
		Date date1 = new Date(1l);
		Date date2 = new Date(2l);
		Time time = new Time(1, date1, date2);
		ListObject lo = new ListObject(1, "listobject");
		db.addTime(time);
		Long id = db.addListObject(lo);
		lo = db.getListObject(id);

		db.addListObjectsWithTime(lo, time);
		assertEquals(time, db.getTime(lo));

		List<ListObject> lotimes = db.getAllListObjectsThatHaveTimeAndCategories();
		assertTrue(lotimes.contains(lo));
	}

	@Override
	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

}
