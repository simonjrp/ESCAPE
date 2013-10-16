package se.chalmers.dat255.group22.escape.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import se.chalmers.dat255.group22.escape.objects.Category;
import se.chalmers.dat255.group22.escape.objects.GPSAlarm;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Place;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeAlarm;

/**
 * The handler for the SQLite Database
 * 
 * @author Johanna and Mike
 * 
 */
public class DBHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "escapeDatabase";

	// Table names
	private static final String TABLE_LIST_OBJECTS = "listObjects";
	private static final String TABLE_CATEGORIES = "categories";
	private static final String TABLE_PLACES = "places";
	private static final String TABLE_TIMES = "times";
	private static final String TABLE_TIME_ALARMS = "timeAlarms";
	private static final String TABLE_GPS_ALARMS = "gpsAlarms";
	private static final String TABLE_CATEGORIES_WITH_LISTOBJECTS = "categoriesWithListObjects";
	private static final String TABLE_LIST_OBJECTS_WITH_TIME_ALARM = "listObjectsWithTimeAlarm";
	private static final String TABLE_LIST_OBJECTS_WITH_GPS_ALARM = "listObjectsWithGPSAlarm";
	private static final String TABLE_LIST_OBJECTS_WITH_TIME = "listObjectsWithTime";
	private static final String TABLE_LIST_OBJECTS_WITH_PLACE = "listObjectsWithPlace";

	// ListObject table column names
	private static final String COLUMN_LIST_OBJECTS_ID = "_id";
	private static final String COLUMN_LIST_OBJECTS_NAME = "name";
	private static final String COLUMN_LIST_OBJECTS_COMMENT = "comment";
	private static final String COLUMN_LIST_OBJECTS_IMPORTANT = "important";

	// Categories table column names
	private static final String COLUMN_CATEGORIES_NAME = "_name";
	private static final String COLUMN_CATEGORIES_BASE_COLOR = "baseColor";
	private static final String COLUMN_CATEGORIES_IMPORTANT_COLOR = "importantColor";

	// Places table column names
	private static final String COLUMN_PLACES_ID = "_id";
	private static final String COLUMN_PLACES_NAME = "name";

	// Times table column names
	private static final String COLUMN_TIMES_ID = "_id";
	private static final String COLUMN_TIMES_START_DATE = "startDate";
	private static final String COLUMN_TIMES_END_DATE = "endDate";

	// TimeAlarms table column names
	private static final String COLUMN_TIME_ALARMS_ID = "_id";
	private static final String COLUMN_TIME_ALARMS_DATE = "date";

	// GPSAlarms table column names
	private static final String COLUMN_GPS_ALARMS_ID = "_id";
	private static final String COLUMN_GPS_ALARMS_ADRESS = "adress";
	private static final String COLUMN_GPS_ALARMS_LONGITUDE = "longitude";
	private static final String COLUMN_GPS_ALARMS_LATITUDE = "latitude";

	// "Categories with list objects" table column names
	private static final String COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT = "_listObject";
	private static final String COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY = "_category";

	// "List objects with time alarm" table column names
	private static final String COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT = "_listObject";
	private static final String COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM = "timeAlarm";

	// "List objects with gps alarm" table column names
	private static final String COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT = "_listObject";
	private static final String COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM = "gpsAlarm";

	// "List objects with time" table column names
	private static final String COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT = "_listObject";
	private static final String COLUMN_LIST_OBJECTS_WITH_TIME_TIME = "time";

	// "List objects with place" table column names
	private static final String COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT = "_listObject";
	private static final String COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE = "place";

	// "Create table"-strings
	private static final String CREATE_LIST_OBJECTS_TABLE = "CREATE TABLE "
			+ TABLE_LIST_OBJECTS + "(" + COLUMN_LIST_OBJECTS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_LIST_OBJECTS_NAME
			+ " TEXT NOT NULL," + COLUMN_LIST_OBJECTS_COMMENT + " TEXT,"
			+ COLUMN_LIST_OBJECTS_IMPORTANT + " INTEGER NOT NULL" + ")";
	private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE "
			+ TABLE_CATEGORIES + "(" + COLUMN_CATEGORIES_NAME
			+ " TEXT PRIMARY KEY," + COLUMN_CATEGORIES_BASE_COLOR
			+ " TEXT NOT NULL," + COLUMN_CATEGORIES_IMPORTANT_COLOR + " TEXT"
			+ ")";
	private static final String CREATE_PLACES_TABLE = "CREATE TABLE "
			+ TABLE_PLACES + "(" + COLUMN_PLACES_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PLACES_NAME
			+ " TEXT NOT NULL" + ")";
	private static final String CREATE_TIMES_TABLE = "CREATE TABLE "
			+ TABLE_TIMES + "(" + COLUMN_TIMES_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TIMES_START_DATE
			+ " INTEGER NOT NULL," + COLUMN_TIMES_END_DATE
			+ " INTEGER NOT NULL" + ")";
	private static final String CREATE_TIME_ALARMS_TABLE = "CREATE TABLE "
			+ TABLE_TIME_ALARMS + "(" + COLUMN_TIME_ALARMS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TIME_ALARMS_DATE
			+ " INTEGER NOT NULL" + ")";
	private static final String CREATE_GPS_ALARMS_TABLE = "CREATE TABLE "
			+ TABLE_GPS_ALARMS + "(" + COLUMN_GPS_ALARMS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_GPS_ALARMS_ADRESS
			+ " TEXT NOT NULL," + COLUMN_GPS_ALARMS_LATITUDE
			+ " NUMERIC NOT NULL," + COLUMN_GPS_ALARMS_LONGITUDE
			+ " NUMERIC NOT NULL" + ")";
	private static final String CREATE_CATEGORIES_WITH_LIST_OBJECTS_TABLE = "CREATE TABLE "
			+ TABLE_CATEGORIES_WITH_LISTOBJECTS
			+ "("
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT
			+ " INTEGER NOT NULL,"
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY
			+ " TEXT NOT NULL,"
			+ "PRIMARY KEY ("
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT
			+ ", "
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY
			+ "),"
			+ "FOREIGN KEY ("
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY
			+ ") REFERENCES "
			+ TABLE_CATEGORIES
			+ "("
			+ COLUMN_CATEGORIES_NAME
			+ ") ON DELETE CASCADE,"
			+ "FOREIGN KEY ("
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT
			+ ") REFERENCES "
			+ TABLE_LIST_OBJECTS
			+ "("
			+ COLUMN_LIST_OBJECTS_ID
			+ ") ON DELETE CASCADE" + ")";
	private static final String CREATE_LIST_OBJECTS_WITH_TIME_ALARM_TABLE = "CREATE TABLE "
			+ TABLE_LIST_OBJECTS_WITH_TIME_ALARM
			+ "("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT
			+ " INTEGER PRIMARY KEY,"
			+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM
			+ " INTEGER NOT NULL,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT
			+ ") REFERENCES "
			+ TABLE_LIST_OBJECTS
			+ "("
			+ COLUMN_LIST_OBJECTS_ID
			+ ") ON DELETE CASCADE,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM
			+ ") REFERENCES "
			+ TABLE_TIME_ALARMS
			+ "("
			+ COLUMN_TIME_ALARMS_ID
			+ ") ON DELETE CASCADE" + ")";
	private static final String CREATE_LIST_OBJECTS_WITH_GPS_ALARM_TABLE = "CREATE TABLE "
			+ TABLE_LIST_OBJECTS_WITH_GPS_ALARM
			+ "("
			+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT
			+ " INTEGER PRIMARY KEY,"
			+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM
			+ " INTEGER NOT NULL,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT
			+ ") REFERENCES "
			+ TABLE_LIST_OBJECTS
			+ "("
			+ COLUMN_LIST_OBJECTS_ID
			+ ") ON DELETE CASCADE,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM
			+ ") REFERENCES "
			+ TABLE_GPS_ALARMS
			+ "("
			+ COLUMN_GPS_ALARMS_ID
			+ ") ON DELETE CASCADE" + ")";
	private static final String CREATE_LIST_OBJECTS_WITH_TIME_TABLE = "CREATE TABLE "
			+ TABLE_LIST_OBJECTS_WITH_TIME
			+ "("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT
			+ " INTEGER PRIMARY KEY,"
			+ COLUMN_LIST_OBJECTS_WITH_TIME_TIME
			+ " INTEGER NOT NULL,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT
			+ ") REFERENCES "
			+ TABLE_LIST_OBJECTS
			+ "("
			+ COLUMN_LIST_OBJECTS_ID
			+ ") ON DELETE CASCADE,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_TIME
			+ ") REFERENCES "
			+ TABLE_TIMES + "(" + COLUMN_TIMES_ID + ") ON DELETE CASCADE" + ")";
	private static final String CREATE_LIST_OBJECTS_WITH_PLACE_TABLE = "CREATE TABLE "
			+ TABLE_LIST_OBJECTS_WITH_PLACE
			+ "("
			+ COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT
			+ " INTEGER PRIMARY KEY,"
			+ COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE
			+ " INTEGER NOT NULL,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT
			+ ") REFERENCES "
			+ TABLE_LIST_OBJECTS
			+ "("
			+ COLUMN_LIST_OBJECTS_ID
			+ ") ON DELETE CASCADE,"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE
			+ ") REFERENCES "
			+ TABLE_PLACES
			+ "("
			+ COLUMN_PLACES_ID
			+ ") ON DELETE CASCADE"
			+ ")";

	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating the tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LIST_OBJECTS_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_PLACES_TABLE);
		db.execSQL(CREATE_TIMES_TABLE);
		db.execSQL(CREATE_TIME_ALARMS_TABLE);
		db.execSQL(CREATE_GPS_ALARMS_TABLE);
		db.execSQL(CREATE_CATEGORIES_WITH_LIST_OBJECTS_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_TIME_ALARM_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_GPS_ALARM_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_TIME_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_PLACE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Write onUpgrade such that it reuses old data.

		if (oldVersion < 2) {
			db.execSQL("DROP TABLE " + TABLE_GPS_ALARMS);
			db.execSQL(CREATE_GPS_ALARMS_TABLE);
		}

		// // Drop older tables if existed
		// db.execSQL("DROP TABLE IF EXISTS " + CREATE_LIST_OBJECTS_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS " + CREATE_CATEGORIES_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS " + CREATE_PLACES_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS " + CREATE_TIMES_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS " + CREATE_TIME_ALARMS_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS " + CREATE_GPS_ALARMS_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS "
		// + CREATE_CATEGORIES_WITH_LIST_OBJECTS_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS "
		// + CREATE_LIST_OBJECTS_WITH_TIME_ALARM_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS "
		// + CREATE_LIST_OBJECTS_WITH_GPS_ALARM_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS "
		// + CREATE_LIST_OBJECTS_WITH_TIME_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS "
		// + CREATE_LIST_OBJECTS_WITH_PLACE_TABLE);
		// // Create tables again
		// onCreate(db);
	}

	// This enables foreign_keys such that "ON DELETE CASCADE" works.
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	/**
	 * Saves a listObject to the database
	 * 
	 * @param listObject
	 */
	public Long addListObject(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_NAME, listObject.getName());
		values.put(COLUMN_LIST_OBJECTS_COMMENT, listObject.getComment());
		values.put(COLUMN_LIST_OBJECTS_IMPORTANT, (listObject.isImportant())
				? 1
				: 0);

		Long id = db.insert(TABLE_LIST_OBJECTS, null, values);
		db.close();

		return id;
	}

	/**
	 * Saves a category to the database
	 * 
	 * @param category
	 * @return -1 if some error happened, otherwise just a number
	 */
	public Long addCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CATEGORIES_NAME, category.getName());
		values.put(COLUMN_CATEGORIES_BASE_COLOR, category.getBaseColor());
		values.put(COLUMN_CATEGORIES_IMPORTANT_COLOR,
				category.getImportantColor());

		Long id = db.insert(TABLE_CATEGORIES, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a place to the database
	 * 
	 * @param place
	 */
	public Long addPlace(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_PLACES_NAME, place.getName());

		Long id = db.insert(TABLE_PLACES, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a Time object to the database
	 * 
	 * @param time
	 */
	public Long addTime(Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TIMES_START_DATE, time.getStartDate().getTime());
		values.put(COLUMN_TIMES_END_DATE, time.getEndDate().getTime());

		Long id = db.insert(TABLE_TIMES, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a time alarm to the database
	 * 
	 * @param timeAlarm
	 */
	public Long addTimeAlarm(TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TIME_ALARMS_DATE, timeAlarm.getDate().getTime());

		Long id = db.insert(TABLE_TIME_ALARMS, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a GPS alarm to the database
	 * 
	 * @param gpsAlarm
	 */
	public Long addGPSAlarm(GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_GPS_ALARMS_ADRESS, gpsAlarm.getAdress());
		values.put(COLUMN_GPS_ALARMS_LATITUDE, gpsAlarm.getLatitude());
		values.put(COLUMN_GPS_ALARMS_LONGITUDE, gpsAlarm.getLongitude());

		Long id = db.insert(TABLE_GPS_ALARMS, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a category and listobject "pair" to the database I.e. the relation
	 * between them is saved *
	 * 
	 * @param category
	 * @param listObject
	 */
	public Long addCategoryWithListObject(Category category,
			ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY,
				category.getName());
		values.put(COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT,
				listObject.getId());

		Long id = db.insert(TABLE_CATEGORIES_WITH_LISTOBJECTS, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a time alarm to a list object to the database
	 * 
	 * @param listObject
	 * @param timeAlarm
	 */
	public Long addListObjectWithTimeAlarm(ListObject listObject,
			TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT,
				listObject.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM,
				timeAlarm.getId());

		Long id = db.insert(TABLE_LIST_OBJECTS_WITH_TIME_ALARM, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a GPS alarm to a list object to the database
	 * 
	 * @param listObject
	 * @param gpsAlarm
	 */
	public Long addListObjectWithGPSAlarm(ListObject listObject,
			GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT,
				listObject.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM,
				gpsAlarm.getId());

		Long id = db.insert(TABLE_LIST_OBJECTS_WITH_GPS_ALARM, null, values);
		db.close();
		return id;
	}

	/**
	 * Saves a time to a list object to the database
	 * 
	 * @param listObject
	 * @param time
	 */
	public Long addListObjectsWithTime(ListObject listObject, Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_TIME, time.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT,
				listObject.getId());

		Long id = db.insert(TABLE_LIST_OBJECTS_WITH_TIME, null, values);
		db.close();
		return id;
	}

	/**
	 * 
	 * Saves a place to a list object to the database
	 * 
	 * @param listObject
	 * @param place
	 */
	public Long addListObjectWithPlace(ListObject listObject, Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE, place.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT,
				listObject.getId());

		Long id = db.insert(TABLE_LIST_OBJECTS_WITH_PLACE, null, values);
		db.close();
		return id;
	}

	/**
	 * Returns a list with all the ListObjects in the database
	 * 
	 * @return
	 */
	public List<ListObject> getAllListObjects() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<ListObject> list = new LinkedList<ListObject>();
		Cursor cursor = db.query(TABLE_LIST_OBJECTS, new String[]{
				COLUMN_LIST_OBJECTS_ID, COLUMN_LIST_OBJECTS_NAME,
				COLUMN_LIST_OBJECTS_COMMENT, COLUMN_LIST_OBJECTS_IMPORTANT},
				null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				ListObject object = new ListObject(cursor.getInt(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_LIST_OBJECTS_NAME)));
				object.setComment(cursor.getString(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_COMMENT)));
				object.setImportant((cursor.getInt(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_IMPORTANT)) == 1
						? true
						: false));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * Returns a list with all the Categories in the database
	 * 
	 * @return
	 */
	public List<Category> getAllCategories() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<Category> list = new LinkedList<Category>();
		Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{
				COLUMN_CATEGORIES_NAME, COLUMN_CATEGORIES_BASE_COLOR,
				COLUMN_CATEGORIES_IMPORTANT_COLOR}, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				Category object = new Category(
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_NAME)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_BASE_COLOR)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_IMPORTANT_COLOR)));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * Returns a list with all the Places in the database
	 * 
	 * @return
	 */
	public List<Place> getAllPlaces() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<Place> list = new LinkedList<Place>();
		Cursor cursor = db.query(TABLE_PLACES, new String[]{COLUMN_PLACES_ID,
				COLUMN_PLACES_NAME}, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Place object = new Place(cursor.getInt(cursor
						.getColumnIndex(COLUMN_PLACES_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_PLACES_NAME)));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * Returns a list with all the Times in the database
	 * 
	 * @return
	 */
	public List<Time> getAllTimes() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<Time> list = new LinkedList<Time>();
		Cursor cursor = db.query(TABLE_TIMES, new String[]{COLUMN_TIMES_ID,
				COLUMN_TIMES_START_DATE, COLUMN_TIMES_END_DATE}, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Time object = new Time(cursor.getInt(cursor
						.getColumnIndex(COLUMN_TIMES_ID)), new Date(
						cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIMES_START_DATE))),
						new Date(cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIMES_END_DATE))));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * Returns a list with all the TimeAlarms in the database
	 * 
	 * @return
	 */
	public List<TimeAlarm> getAllTimeAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<TimeAlarm> list = new LinkedList<TimeAlarm>();
		Cursor cursor = db.query(TABLE_TIME_ALARMS, new String[]{
				COLUMN_TIME_ALARMS_ID, COLUMN_TIME_ALARMS_DATE}, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				TimeAlarm object = new TimeAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_TIMES_ID)), new Date(
						cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIME_ALARMS_DATE))));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * Returns a list with all the GPSAlarms in the database
	 * 
	 * @return
	 */
	public List<GPSAlarm> getAllGPSAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<GPSAlarm> list = new LinkedList<GPSAlarm>();
		Cursor cursor = db.query(TABLE_GPS_ALARMS, new String[]{
				COLUMN_GPS_ALARMS_ID, COLUMN_GPS_ALARMS_ADRESS,
				COLUMN_GPS_ALARMS_LONGITUDE, COLUMN_GPS_ALARMS_LATITUDE}, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				GPSAlarm object = new GPSAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_GPS_ALARMS_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_ADRESS)),
						cursor.getDouble(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_LONGITUDE)),
						cursor.getDouble(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_LATITUDE)));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * Returns a list of the ListObjects of a Category
	 * 
	 * @param category
	 * @return
	 */

	public List<ListObject> getListObjects(Category category) {
		SQLiteDatabase db = this.getReadableDatabase();

		String raw = "SELECT b." + COLUMN_LIST_OBJECTS_ID + ", b."
				+ COLUMN_LIST_OBJECTS_NAME + ", b."
				+ COLUMN_LIST_OBJECTS_COMMENT + ", b."
				+ COLUMN_LIST_OBJECTS_IMPORTANT + " FROM "
				+ TABLE_CATEGORIES_WITH_LISTOBJECTS + " a" + " INNER JOIN "
				+ TABLE_LIST_OBJECTS + " b" + " ON a."
				+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT + " = b."
				+ COLUMN_LIST_OBJECTS_ID + " WHERE a."
				+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY + " = '"
				+ category.getName() + "'";

		List<ListObject> list = new LinkedList<ListObject>();
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToFirst()) {
			do {
				ListObject object = new ListObject(cursor.getInt(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_LIST_OBJECTS_NAME)));
				object.setComment(cursor.getString(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_COMMENT)));
				object.setImportant((cursor.getInt(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_IMPORTANT)) == 1
						? true
						: false));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * Returns a list of the Categories to a ListObject
	 * 
	 * @param listObject
	 * @return
	 */
	public List<Category> getCategories(ListObject listObject) {
		SQLiteDatabase db = this.getReadableDatabase();

		String raw = "SELECT b." + COLUMN_CATEGORIES_NAME + ", b."
				+ COLUMN_CATEGORIES_BASE_COLOR + ", b."
				+ COLUMN_CATEGORIES_IMPORTANT_COLOR + " FROM "
				+ TABLE_CATEGORIES_WITH_LISTOBJECTS + " a" + " INNER JOIN "
				+ TABLE_CATEGORIES + " b" + " ON a."
				+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY + " = b."
				+ COLUMN_CATEGORIES_NAME + " WHERE a."
				+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT + " = '"
				+ listObject.getId() + "'";

		List<Category> list = new LinkedList<Category>();
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToFirst()) {
			do {
				Category object = new Category(
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_NAME)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_BASE_COLOR)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_IMPORTANT_COLOR)));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * Returns the Category with the name specified.
	 * 
	 * @param name
	 * @return Category with the name
	 */
	public Category getCategory(String name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{
				COLUMN_CATEGORIES_NAME, COLUMN_CATEGORIES_BASE_COLOR,
				COLUMN_CATEGORIES_IMPORTANT_COLOR}, COLUMN_CATEGORIES_NAME
				+ "=?", new String[]{name}, null, null, null);

		List<Category> list = new LinkedList<Category>();
		if (cursor.moveToFirst()) {
			do {

				Category object = new Category(
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_NAME)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_BASE_COLOR)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_CATEGORIES_IMPORTANT_COLOR)));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	// TODO _Maybe_ make a method to return EVERYTHING from dBase. Is doable,
	// but is it usable?
	// public List<ListObject> getAllListObjectsWithAllRelatedIds() {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor cursor = db.rawQuery(
	// "SELECT a.?, b.? AS ?.name, c.? AS ?.id, d.? AS ?.id, e.? AS ?.id, f.? AS ?.id "
	// + "FROM ? a LEFT JOIN ? b "
	// + "ON a.? = b.? "
	// + "LEFT JOIN ? c "
	// + "ON a.? = c.? "
	// + "LEFT JOIN ? d "
	// + "ON a.? = d.? "
	// + "LEFT JOIN ? e "
	// + "ON a.? = e.? "
	// + "LEFT JOIN ? f "
	// + "ON a.? = f.?",
	// new String[] { TABLE_LIST_OBJECTS,
	// TABLE_CATEGORIES_WITH_LISTOBJECTS,
	// TABLE_CATEGORIES,
	// TABLE_LIST_OBJECTS_WITH_TIME_ALARM,
	// TABLE_TIME_ALARMS,
	// TABLE_LIST_OBJECTS_WITH_TIME,
	// TABLE_TIMES,
	// TABLE_LIST_OBJECTS_WITH_GPS_ALARM,
	// TABLE_GPS_ALARMS,
	// TABLE_LIST_OBJECTS_WITH_PLACE,
	// TABLE_PLACES,
	//
	//
	// TABLE_LIST_OBJECTS,
	// TABLE_CATEGORIES_WITH_LISTOBJECTS,
	// COLUMN_LIST_OBJECTS_ID,
	// COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT,
	// TABLE_LIST_OBJECTS_WITH_TIME_ALARM,
	// COLUMN_LIST_OBJECTS_ID,
	// COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT,
	// TABLE_LIST_OBJECTS_WITH_TIME,
	// COLUMN_LIST_OBJECTS_ID,
	// COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT,
	// TABLE_LIST_OBJECTS_WITH_GPS_ALARM,
	// COLUMN_LIST_OBJECTS_ID,
	// COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT,
	// TABLE_LIST_OBJECTS_WITH_PLACE,
	// COLUMN_LIST_OBJECTS_ID,
	// COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT});
	// if (cursor.moveToFirst()) {
	// do {
	//
	// } while (cursor.moveToNext());
	// }
	// return null;
	// }

	/**
	 * Returns the listObject with the id specified.
	 * 
	 * @param id
	 * @return ListObject with the id
	 */
	public ListObject getListObject(Long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_LIST_OBJECTS, new String[]{
				COLUMN_LIST_OBJECTS_ID, COLUMN_LIST_OBJECTS_NAME,
				COLUMN_LIST_OBJECTS_COMMENT, COLUMN_LIST_OBJECTS_IMPORTANT},
				COLUMN_LIST_OBJECTS_ID + "=?", new String[]{id.toString()},
				null, null, null);

		List<ListObject> list = new LinkedList<ListObject>();
		if (cursor.moveToFirst()) {
			do {

				ListObject object = new ListObject(cursor.getInt(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_LIST_OBJECTS_NAME)));
				object.setComment(cursor.getString(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_COMMENT)));
				object.setImportant(cursor.getInt(cursor
						.getColumnIndex(COLUMN_LIST_OBJECTS_IMPORTANT)) == 1
						? true
						: false);

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the listObject with the id specified.
	 * 
	 * @param id
	 * @return ListObject with the id
	 */
	public TimeAlarm getTimeAlarm(Long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TIME_ALARMS, new String[]{
				COLUMN_TIME_ALARMS_ID, COLUMN_TIME_ALARMS_DATE},
				COLUMN_TIME_ALARMS_ID + "=?", new String[]{id.toString()},
				null, null, null);

		List<TimeAlarm> list = new LinkedList<TimeAlarm>();
		if (cursor.moveToFirst()) {
			do {

				TimeAlarm object = new TimeAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_TIME_ALARMS_ID)), new Date(
						cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIME_ALARMS_DATE))));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the TimeAlarm for a ListObject
	 * 
	 * @param listObject
	 * @return TimeAlarm for the listObject
	 */
	public TimeAlarm getTimeAlarm(ListObject listObject) {
		SQLiteDatabase db = this.getReadableDatabase();

		String raw = "SELECT b." + COLUMN_TIME_ALARMS_ID + ", b."
				+ COLUMN_TIME_ALARMS_DATE + " FROM "
				+ TABLE_LIST_OBJECTS_WITH_TIME_ALARM + " a" + " INNER JOIN "
				+ TABLE_TIME_ALARMS + " b" + " ON a."
				+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM + " = b."
				+ COLUMN_TIME_ALARMS_ID + " WHERE a."
				+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT + " = '"
				+ listObject.getId() + "'";

		List<TimeAlarm> list = new LinkedList<TimeAlarm>();
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToFirst()) {
			do {
				TimeAlarm timeAlarm = new TimeAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_TIME_ALARMS_ID)), new Date(
						cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIME_ALARMS_DATE))));

				list.add(timeAlarm);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the GPSAlarm with the id specified.
	 * 
	 * @param id
	 * @return GPSAlarm with the id
	 */
	public GPSAlarm getGPSAlarm(Long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_GPS_ALARMS, new String[]{
				COLUMN_GPS_ALARMS_ID, COLUMN_GPS_ALARMS_ADRESS,
				COLUMN_GPS_ALARMS_LATITUDE, COLUMN_GPS_ALARMS_LONGITUDE},
				COLUMN_GPS_ALARMS_ID + "=?", new String[]{id.toString()}, null,
				null, null);

		List<GPSAlarm> list = new LinkedList<GPSAlarm>();
		if (cursor.moveToFirst()) {
			do {

				GPSAlarm object = new GPSAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_GPS_ALARMS_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_ADRESS)),
						cursor.getDouble(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_LONGITUDE)),
						cursor.getDouble(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_LATITUDE)));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the TimeAlarm for a ListObject
	 * 
	 * @param listObject
	 * @return TimeAlarm for the listObject
	 */
	public GPSAlarm getGPSAlarm(ListObject listObject) {
		SQLiteDatabase db = this.getReadableDatabase();

		String raw = "SELECT b." + COLUMN_GPS_ALARMS_ID + ", b."
				+ COLUMN_GPS_ALARMS_ADRESS + ", b."
				+ COLUMN_GPS_ALARMS_LATITUDE + ", b."
				+ COLUMN_GPS_ALARMS_LONGITUDE + " FROM "
				+ TABLE_LIST_OBJECTS_WITH_GPS_ALARM + " a" + " INNER JOIN "
				+ TABLE_GPS_ALARMS + " b" + " ON a."
				+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM + " = b."
				+ COLUMN_GPS_ALARMS_ID + " WHERE a."
				+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT + " = '"
				+ listObject.getId() + "'";

		List<GPSAlarm> list = new LinkedList<GPSAlarm>();
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToFirst()) {
			do {
				GPSAlarm object = new GPSAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_GPS_ALARMS_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_ADRESS)),
						cursor.getDouble(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_LONGITUDE)),
						cursor.getDouble(cursor
								.getColumnIndex(COLUMN_GPS_ALARMS_LATITUDE)));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Method for getting a Time object from an ID
	 * 
	 * @param id
	 *            The ID of the Time object
	 * @return The Time object with the given id
	 */
	public Time getTime(Long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TIMES, new String[]{COLUMN_TIMES_ID,
				COLUMN_TIMES_START_DATE, COLUMN_TIMES_END_DATE},
				COLUMN_TIMES_ID + "=?", new String[]{id.toString()}, null,
				null, null);

		List<Time> list = new LinkedList<Time>();
		if (cursor.moveToFirst()) {
			do {

				Time object = new Time(cursor.getInt(cursor
						.getColumnIndex(COLUMN_TIMES_ID)), new Date(
						cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIMES_START_DATE))),
						new Date(cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIMES_START_DATE))));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the Time for a list object
	 * 
	 * @param listObject
	 * @return Time for the listObject
	 */
	public Time getTime(ListObject listObject) {
		SQLiteDatabase db = this.getReadableDatabase();

		String raw = "SELECT b." + COLUMN_TIMES_ID + ", b."
				+ COLUMN_TIMES_START_DATE + ", b." + COLUMN_TIMES_END_DATE
				+ " FROM " + TABLE_LIST_OBJECTS_WITH_TIME + " a"
				+ " INNER JOIN " + TABLE_TIMES + " b" + " ON a."
				+ COLUMN_LIST_OBJECTS_WITH_TIME_TIME + " = b."
				+ COLUMN_TIMES_ID + " WHERE a."
				+ COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT + " = '"
				+ listObject.getId() + "'";

		List<Time> list = new LinkedList<Time>();
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToFirst()) {
			do {
				Time object = new Time(cursor.getInt(cursor
						.getColumnIndex(COLUMN_TIMES_ID)), new Date(
						cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIMES_START_DATE))),
						new Date(cursor.getLong(cursor
								.getColumnIndex(COLUMN_TIMES_END_DATE))));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the Place for a list object
	 * 
	 * @param listObject
	 * @return Place for the listObject
	 */
	public Place getPlace(ListObject listObject) {
		SQLiteDatabase db = this.getReadableDatabase();

		String raw = "SELECT b." + COLUMN_PLACES_ID + ", b."
				+ COLUMN_PLACES_NAME + " FROM " + TABLE_LIST_OBJECTS_WITH_PLACE
				+ " a" + " INNER JOIN " + TABLE_PLACES + " b" + " ON a."
				+ COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE + " = b."
				+ COLUMN_PLACES_ID + " WHERE a."
				+ COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT + " = '"
				+ listObject.getId() + "'";

		List<Place> list = new LinkedList<Place>();
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToFirst()) {
			do {
				Place object = new Place(cursor.getInt(cursor
						.getColumnIndex(COLUMN_PLACES_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_PLACES_NAME)));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the Place with the id specified.
	 * 
	 * @param id
	 * @return Place with the id
	 */
	public Place getPlace(Long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PLACES, new String[]{COLUMN_PLACES_ID,
				COLUMN_PLACES_NAME}, COLUMN_PLACES_ID + "=?",
				new String[]{id.toString()}, null, null, null);

		List<Place> list = new LinkedList<Place>();
		if (cursor.moveToFirst()) {
			do {

				Place object = new Place(cursor.getInt(cursor
						.getColumnIndex(COLUMN_PLACES_ID)),
						cursor.getString(cursor
								.getColumnIndex(COLUMN_PLACES_NAME)));

				list.add(object);
			} while (cursor.moveToNext());
		}

		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Updates a listObject in the database
	 * 
	 * @param listObject
	 *            to update
	 * @return the number of updated rows
	 */
	public int updateListObject(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_NAME, listObject.getName());
		values.put(COLUMN_LIST_OBJECTS_COMMENT, listObject.getComment());
		values.put(COLUMN_LIST_OBJECTS_IMPORTANT, (listObject.isImportant())
				? 1
				: 0);

		int rv = db.update(TABLE_LIST_OBJECTS, values, COLUMN_LIST_OBJECTS_ID
				+ "=?", new String[]{"" + listObject.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a category in the database
	 * 
	 * @param category
	 *            to update
	 * @param oldName
	 *            , if null it will update the the name that category has; if
	 *            not null it will update the specified named category
	 * @return number of affected rows
	 */
	public int updateCategory(Category category, String oldName) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CATEGORIES_NAME, category.getName());
		values.put(COLUMN_CATEGORIES_BASE_COLOR, category.getBaseColor());
		values.put(COLUMN_CATEGORIES_IMPORTANT_COLOR,
				category.getImportantColor());

		int rv = db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORIES_NAME
                + "=?",
                new String[]{(oldName != null) ? oldName : category.getName()});
		db.close();

		return rv;
	}

	/**
	 * Updates a place in the database
	 * 
	 * @param place
	 *            to update
	 * @return number of affected rows
	 */
	public int updatePlaces(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_PLACES_NAME, place.getName());

		int rv = db.update(TABLE_PLACES, values, COLUMN_PLACES_ID + "=?",
				new String[]{"" + place.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a Time in the database
	 * 
	 * @param time
	 *            to update
	 * @return number of affected rows
	 */
	public int updateTimes(Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TIMES_START_DATE, time.getStartDate().getTime());
		values.put(COLUMN_TIMES_END_DATE, time.getEndDate().getTime());

		int rv = db.update(TABLE_TIMES, values, COLUMN_TIMES_ID + "=?",
				new String[]{"" + time.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a timed alarm in the database
	 * 
	 * @param timeAlarm
	 *            to update
	 * @return number of affected rows
	 */
	public int updateTimeAlarm(TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TIME_ALARMS_DATE, timeAlarm.getDate().getTime());

		int rv = db.update(TABLE_TIME_ALARMS, values, COLUMN_TIME_ALARMS_ID
				+ "=?", new String[]{"" + timeAlarm.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a GPS alarm in the database
	 * 
	 * @param gpsAlarm
	 *            to update
	 * @return number of affected rows
	 */
	public int updateGPSAlarm(GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_GPS_ALARMS_ADRESS, gpsAlarm.getAdress());
		values.put(COLUMN_GPS_ALARMS_LATITUDE, gpsAlarm.getLatitude());
		values.put(COLUMN_GPS_ALARMS_LONGITUDE, gpsAlarm.getLongitude());

		int rv = db.update(TABLE_GPS_ALARMS, values, COLUMN_GPS_ALARMS_ID
				+ "=?", new String[]{"" + gpsAlarm.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a list object's time alarm, if it has any.
	 * 
	 * @param listObject
	 *            to update
	 * @param timeAlarm
	 *            to change to
	 * @return number of affected rows
	 */
	public int updateListObjectWithTimeAlarm(ListObject listObject,
			TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM,
				timeAlarm.getId());

		int rv = db.update(TABLE_LIST_OBJECTS_WITH_TIME_ALARM, values,
				COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a list object's GPS alarm, if it has any.
	 * 
	 * @param listObject
	 *            to update
	 * @param gpsAlarm
	 *            to change to
	 * @return number of affected rows
	 */
	public int updateListObjectWithGPSAlarm(ListObject listObject,
			GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM,
				gpsAlarm.getId());

		int rv = db.update(TABLE_LIST_OBJECTS_WITH_GPS_ALARM, values,
				COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a list object's Time, if it has any.
	 * 
	 * @param listObject
	 *            to update
	 * @param time
	 *            to change to
	 * @return number of affected rows
	 */
	public int updateListObjectWithTime(ListObject listObject, Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_TIME, time.getId());

		int rv = db.update(TABLE_LIST_OBJECTS_WITH_TIME, values,
				COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();

		return rv;
	}

	/**
	 * Updates a list object's place, if it has any.
	 * 
	 * @param listObject
	 *            to update
	 * @param place
	 *            to change to
	 * @return number of affected rows
	 */
	public int updateListObjectWithPlace(ListObject listObject, Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE, place.getId());

		int rv = db.update(TABLE_LIST_OBJECTS_WITH_PLACE, values,
				COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();

		return rv;
	}

	/**
	 * Deletes a ListObject from the Database
	 * 
	 * @param listObject
	 *            to delete
	 * @return true if anything was deleted, false otherwise
	 * @deprecated Use purgeListObject() instead. Using this method leaves
	 *             unwanted stuff in database
	 */
	public boolean deleteListObject(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_LIST_OBJECTS, COLUMN_LIST_OBJECTS_ID + "=?",
				new String[]{"" + listObject.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Purges a ListObject from the Database which includes deleting every
	 * place, time, timeAlarm and GPSAlarm that was connected to it from the
	 * database
	 * 
	 * @param listObject
	 *            to purge
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean purgeListObject(ListObject listObject) {
		Time time = getTime(listObject);
		Place place = getPlace(listObject);
		TimeAlarm timeAlarm = getTimeAlarm(listObject);
		GPSAlarm gpsAlarm = getGPSAlarm(listObject);

		if (time != null)
			deleteTime(time);
		if (place != null)
			deletePlace(place);
		if (timeAlarm != null)
			deleteTimeAlarm(timeAlarm);
		if (gpsAlarm != null)
			deleteGPSAlarm(gpsAlarm);

		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_LIST_OBJECTS, COLUMN_LIST_OBJECTS_ID + "=?",
				new String[]{"" + listObject.getId()});
		db.close();

		return rv > 0;
	}

	/**
	 * Deletes a Category from the Database
	 * 
	 * @param category
	 *            to delete
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_CATEGORIES, COLUMN_CATEGORIES_NAME + "=?",
				new String[]{"" + category.getName()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes a place from the Database
	 * 
	 * @param place
	 *            to delete
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deletePlace(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_PLACES, COLUMN_PLACES_ID + "=?",
				new String[]{"" + place.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes a time from the Database
	 * 
	 * @param time
	 *            to delete
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteTime(Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_TIMES, COLUMN_TIMES_ID + "=?", new String[]{""
				+ time.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes a time alarm from the Database
	 * 
	 * @param timeAlarm
	 *            to delete
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteTimeAlarm(TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_TIME_ALARMS, COLUMN_TIME_ALARMS_ID + "=?",
				new String[]{"" + timeAlarm.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes a GPS alarm from the Database
	 * 
	 * @param gpsAlarm
	 *            to delete
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteGPSAlarm(GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_GPS_ALARMS, COLUMN_GPS_ALARMS_ID + "=?",
				new String[]{"" + gpsAlarm.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes a category from a listobject (Note: Does not delete the list
	 * object nor the category, only the relation)
	 * 
	 * @param category
	 * @param listObject
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteCategoryWithListObject(Category category,
			ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_CATEGORIES_WITH_LISTOBJECTS,
				COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY + "=? AND "
						+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT
						+ "=?", new String[]{"" + category.getName(),
						"" + listObject.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes the time alarm from a list object.
	 * 
	 * @param listObject
	 *            to delete time alarm from
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteListObjectWithTimeAlarm(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_LIST_OBJECTS_WITH_TIME_ALARM,
				COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes the GPS alarm from a list object.
	 * 
	 * @param listObject
	 *            to delete GPS alarm from
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteListObjectWithGPSAlarm(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_LIST_OBJECTS_WITH_GPS_ALARM,
				COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes the Time from a list object.
	 * 
	 * @param listObject
	 *            to delete Time from
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteListObjectWithTime(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_LIST_OBJECTS_WITH_TIME,
				COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();
		return rv > 0;
	}

	/**
	 * Deletes the place from a list object.
	 * 
	 * @param listObject
	 *            to delete place from
	 * @return true if anything was deleted, false otherwise
	 */
	public boolean deleteListObjectWithPlace(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		int rv = db.delete(TABLE_LIST_OBJECTS_WITH_PLACE,
				COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT + "=?",
				new String[]{"" + listObject.getId()});
		db.close();
		return rv > 0;
	}
}
