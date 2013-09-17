package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The handler for the SQLite Database
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
		// TODO Auto-generated method stub
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
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_CATEGORIES_WITH_LISTOBJECTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_LIST_OBJECTS_WITH_TIME_ALARM_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_LIST_OBJECTS_WITH_GPS_ALARM_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_LIST_OBJECTS_WITH_TIME_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_LIST_OBJECTS_WITH_PLACE_TABLE);
        // Create tables again
        onCreate(db);
	}
	
	

}
