package se.chalmers.dat255.group22.escape;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The handler for the SQLite Database
 * 
 * @author Johanna and Mike
 * 
 */
public class DBHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

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
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_GPS_ALARMS_LATITUDE + " NUMERIC NOT NULL,"
			+ COLUMN_GPS_ALARMS_LONGITUDE + " NUMERIC NOT NULL" + ")";
	private static final String CREATE_CATEGORIES_WITH_LISTOBJECTS_TABLE = "CREATE TABLE "
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
			+ "),"
			+ "FOREIGN KEY ("
			+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT
			+ ") REFERENCES "
			+ TABLE_LIST_OBJECTS + "(" + COLUMN_LIST_OBJECTS_ID + ")" + ")";
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
			+ "),"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM
			+ ") REFERENCES "
			+ TABLE_TIME_ALARMS
			+ "("
			+ COLUMN_TIME_ALARMS_ID
			+ ")" + ")";
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
			+ "),"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM
			+ ") REFERENCES "
			+ TABLE_GPS_ALARMS
			+ "("
			+ COLUMN_GPS_ALARMS_ID
			+ ")" + ")";
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
			+ "),"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_TIME_TIME
			+ ") REFERENCES " + TABLE_TIMES + "(" + COLUMN_TIMES_ID + ")" + ")";
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
			+ "),"
			+ "FOREIGN KEY ("
			+ COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE
			+ ") REFERENCES "
			+ TABLE_PLACES
			+ "("
			+ COLUMN_PLACES_ID
			+ ")"
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
		db.execSQL(CREATE_CATEGORIES_WITH_LISTOBJECTS_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_TIME_ALARM_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_GPS_ALARM_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_TIME_TABLE);
		db.execSQL(CREATE_LIST_OBJECTS_WITH_PLACE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Write onUpgrade such that it reuses old data.

		// Drop older tables if existed
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_LIST_OBJECTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_CATEGORIES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_PLACES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TIMES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TIME_ALARMS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_GPS_ALARMS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "
				+ CREATE_CATEGORIES_WITH_LISTOBJECTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "
				+ CREATE_LIST_OBJECTS_WITH_TIME_ALARM_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "
				+ CREATE_LIST_OBJECTS_WITH_GPS_ALARM_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "
				+ CREATE_LIST_OBJECTS_WITH_TIME_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "
				+ CREATE_LIST_OBJECTS_WITH_PLACE_TABLE);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Adds a ListObject to the database
	 * 
	 * @param listObject
	 */
	public void addListObject(ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_NAME, listObject.getName());
		values.put(COLUMN_LIST_OBJECTS_COMMENT, listObject.getComment());
		values.put(COLUMN_LIST_OBJECTS_IMPORTANT,
				(listObject.isImportant()) ? 1 : 0);

		db.insert(TABLE_LIST_OBJECTS, null, values);
		db.close();
	}

	/**
	 * Adds a Category to the database
	 * 
	 * @param category
	 */
	public void addCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CATEGORIES_NAME, category.getName());
		values.put(COLUMN_CATEGORIES_BASE_COLOR, category.getBaseColor());
		values.put(COLUMN_CATEGORIES_IMPORTANT_COLOR,
				category.getImportantColor());

		db.insert(TABLE_CATEGORIES, null, values);
		db.close();
	}

	/**
	 * Adds a Place to the database
	 * 
	 * @param place
	 */
	public void addPlace(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_PLACES_NAME, place.getName());

		db.insert(TABLE_PLACES, null, values);
		db.close();
	}

	/**
	 * Adds a Time to the database
	 * 
	 * @param time
	 */
	public void addTime(Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TIMES_START_DATE, time.getStartDate().getTime());
		values.put(COLUMN_TIMES_END_DATE, time.getEndDate().getTime());

		db.insert(TABLE_TIMES, null, values);
		db.close();
	}

	/**
	 * 
	 * Adds a TimeAlarm to the database
	 * 
	 * @param timeAlarm
	 */
	public void addTimeAlarm(TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TIME_ALARMS_DATE, timeAlarm.getDate().getTime());

		db.insert(TABLE_TIME_ALARMS, null, values);
		db.close();
	}

	/**
	 * Adds a GPSAlarm to the database
	 * 
	 * @param gpsAlarm
	 */
	public void addGPSAlarm(GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_GPS_ALARMS_LATITUDE, gpsAlarm.getLatitude());
		values.put(COLUMN_GPS_ALARMS_LONGITUDE, gpsAlarm.getLongitude());

		db.insert(TABLE_GPS_ALARMS, null, values);
		db.close();
	}

	/**
	 * Adds a Category to a ListObject in the database
	 * 
	 * @param category
	 * @param listObject
	 */
	public void addCategoryWithListObject(Category category,
			ListObject listObject) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY,
				category.getName());
		values.put(COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT,
				listObject.getId());

		db.insert(TABLE_CATEGORIES_WITH_LISTOBJECTS, null, values);
		db.close();
	}

	/**
	 * Adds a TimeAlarm to a ListObject in the database
	 * 
	 * @param listObject
	 * @param timeAlarm
	 */
	public void addListObjectsWithTimeAlarm(ListObject listObject,
			TimeAlarm timeAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_LIST_OBJECT,
				listObject.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_ALARM_TIME_ALARM,
				timeAlarm.getId());

		db.insert(TABLE_LIST_OBJECTS_WITH_TIME_ALARM, null, values);
		db.close();
	}

	/**
	 * Adds a GPSAlarm to the ListObject in the database
	 * 
	 * @param listObject
	 * @param gpsAlarm
	 */
	public void addListObjectsWithGPSAlarm(ListObject listObject,
			GPSAlarm gpsAlarm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_LIST_OBJECT,
				listObject.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_GPS_ALARM_GPS_ALARM,
				gpsAlarm.getId());

		db.insert(TABLE_LIST_OBJECTS_WITH_GPS_ALARM, null, values);
		db.close();
	}

	/**
	 * Adds a Time to a ListObject in the database
	 * 
	 * @param listObject
	 * @param time
	 */
	public void addListObjectsWithTime(ListObject listObject, Time time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_TIME, time.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_TIME_LIST_OBJECT,
				listObject.getId());

		db.insert(TABLE_LIST_OBJECTS_WITH_TIME, null, values);
		db.close();
	}

	/**
	 * Adds a Place to a ListObject in the database
	 * 
	 * @param listObject
	 * @param place
	 */
	public void addListObjectsWithPlace(ListObject listObject, Place place) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_OBJECTS_WITH_PLACE_PLACE, place.getId());
		values.put(COLUMN_LIST_OBJECTS_WITH_PLACE_LIST_OBJECT,
				listObject.getId());

		db.insert(TABLE_LIST_OBJECTS_WITH_PLACE, null, values);
		db.close();
	}

	/**
	 * Returns a list with all the ListObjects in the database
	 * 
	 * @return
	 */
	public List<ListObject> getAllListObjects() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<ListObject> list = new LinkedList<ListObject>();
		Cursor cursor = db.query(TABLE_LIST_OBJECTS, new String[] {
				COLUMN_LIST_OBJECTS_ID, COLUMN_LIST_OBJECTS_NAME,
				COLUMN_LIST_OBJECTS_COMMENT, COLUMN_LIST_OBJECTS_IMPORTANT },
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
						.getColumnIndex(COLUMN_LIST_OBJECTS_IMPORTANT)) == 1 ? true
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
		Cursor cursor = db.query(TABLE_CATEGORIES, new String[] {
				COLUMN_CATEGORIES_NAME, COLUMN_CATEGORIES_BASE_COLOR,
				COLUMN_CATEGORIES_IMPORTANT_COLOR }, null, null, null, null,
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
		Cursor cursor = db.query(TABLE_PLACES, new String[] { COLUMN_PLACES_ID,
				COLUMN_PLACES_NAME }, null, null, null, null, null);
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
		Cursor cursor = db.query(TABLE_TIMES, new String[] { COLUMN_TIMES_ID,
				COLUMN_TIMES_START_DATE, COLUMN_TIMES_END_DATE }, null, null,
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
		Cursor cursor = db.query(TABLE_TIME_ALARMS, new String[] {
				COLUMN_TIME_ALARMS_ID, COLUMN_TIME_ALARMS_DATE }, null, null,
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
		Cursor cursor = db.query(TABLE_GPS_ALARMS, new String[] {
				COLUMN_GPS_ALARMS_ID, COLUMN_GPS_ALARMS_LONGITUDE,
				COLUMN_GPS_ALARMS_LATITUDE }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				GPSAlarm object = new GPSAlarm(cursor.getInt(cursor
						.getColumnIndex(COLUMN_GPS_ALARMS_ID)),
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
				+ COLUMN_LIST_OBJECTS_ID + " WHERE "
				+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_CATEGORY + " = "
				+ category.getName();

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
						.getColumnIndex(COLUMN_LIST_OBJECTS_IMPORTANT)) == 1 ? true
						: false));
				list.add(object);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * Returns a list of the Categories to a ListObject
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
				+ COLUMN_CATEGORIES_NAME + " WHERE "
				+ COLUMN_CATEGORIES_WITH_LIST_OBJECTS_LIST_OBJECT + " = "
				+ listObject.getName();

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

}
