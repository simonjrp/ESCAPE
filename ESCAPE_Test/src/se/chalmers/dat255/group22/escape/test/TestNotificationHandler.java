//package se.chalmers.dat255.group22.escape.test;
//
//import java.sql.Date;
//
//import se.chalmers.dat255.group22.escape.MainActivity;
//import se.chalmers.dat255.group22.escape.NotificationHandler;
//import se.chalmers.dat255.group22.escape.database.DBHandler;
//import se.chalmers.dat255.group22.escape.objects.ListObject;
//import se.chalmers.dat255.group22.escape.objects.TimeAlarm;
//import android.app.AlarmManager;
//import android.content.Context;
//import android.test.ActivityInstrumentationTestCase2;
//
//public class TestNotificationHandler extends
//		ActivityInstrumentationTestCase2<MainActivity> {
//	private MainActivity mActivity;
//	private NotificationHandler notifHandler;
//	private DBHandler dbHandler;
//	private AlarmManager alarmManager;
//
//	public TestNotificationHandler() {
//		super(MainActivity.class);
//	}
//
//	@Override
//	public void setUp() {
//
//		// disable touch mode on the device/vm
//		setActivityInitialTouchMode(false);
//
//		// get basic objects needed in the test
//		mActivity = getActivity();
//		notifHandler = new NotificationHandler(mActivity);
//		dbHandler = new DBHandler(mActivity);
//		alarmManager = (AlarmManager) mActivity
//				.getSystemService(Context.ALARM_SERVICE);
//	}
//
//	public void testAddReminderNotificationException() {
//		ListObject lo = new ListObject(0, "Title");
//
//		lo.setComment("Description");
//		long idLo = dbHandler.addListObject(lo);
//
//		TimeAlarm timeAlarm = new TimeAlarm(0, new Date(
//				System.currentTimeMillis()));
//		long idTimeAlarm = dbHandler.addTimeAlarm(timeAlarm);
//
//		// dbHandler.addListObjectWithTimeAlarm(dbHandler.getListObject(idLo),
//		// dbHandler.getTimeAlarm(idTimeAlarm));
//
//		try {
//			notifHandler.addTimeReminder(dbHandler.getListObject(idLo));
//			fail("Expected NullPointerException to be thrown");
//		} catch (IllegalArgumentException e) {
//			assertTrue(true);
//		}
//
//	}
//
//}
