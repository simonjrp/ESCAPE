package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import se.chalmers.dat255.group22.escape.objects.BlockObject;
import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeWindow;
import se.chalmers.dat255.group22.escape.utilities.AutoGenerator;
import se.chalmers.dat255.group22.escape.utilities.AutoGenerator.TimeBox;
import android.test.AndroidTestCase;
import android.util.Log;

public class TestAutoGenerator extends AndroidTestCase {

	public void testWithSimpleSchedule() {
		List<ListObject> schedule = new LinkedList<ListObject>();
		ListObject l1 = new ListObject(1, "School");
		Calendar d1 = new GregorianCalendar(2013, 9, 7, 15, 30);
		d1.add(Calendar.HOUR_OF_DAY, 1);
		Calendar d2 = (Calendar) d1.clone();
		d2.add(Calendar.HOUR_OF_DAY, 1);

		ListObject l2 = new ListObject(2, "Math");
		Calendar d21 = new GregorianCalendar(2013, 9, 7, 15, 30);
		d21.add(Calendar.HOUR_OF_DAY, 5);
		Calendar d22 = (Calendar) d21.clone();
		d22.add(Calendar.HOUR_OF_DAY, 1);

		ListObject l3 = new ListObject(2, "ASD");
		Calendar d31 = new GregorianCalendar(2013, 9, 8, 8, 5);
		d31.add(Calendar.HOUR_OF_DAY, 0);
		Calendar d32 = (Calendar) d31.clone();
		d32.add(Calendar.HOUR_OF_DAY, 1);

		l1.setTime(new Time(1, new Date(d1.getTimeInMillis()), new Date(d2
				.getTimeInMillis())));
		l2.setTime(new Time(2, new Date(d21.getTimeInMillis()), new Date(d22
				.getTimeInMillis())));
		l3.setTime(new Time(3, new Date(d31.getTimeInMillis()), new Date(d32
				.getTimeInMillis())));
		schedule.add(l1);
		schedule.add(l2);
		schedule.add(l3);

		List<IBlockObject> blocks = new LinkedList<IBlockObject>();
		IBlockObject block1 = new BlockObject("test", TimeWindow.WORKING, 400, 30);
		blocks.add(block1);

		AutoGenerator auto = new AutoGenerator(schedule, blocks);
		List<ListObject> test = auto.generate();

//		Log.d("testCount", "testCount : " + test.size() + " with :");
//		StringBuilder sb = new StringBuilder();
//		sb.append("LO={" + d1.get(Calendar.DAY_OF_MONTH) + "~"
//				+ d1.get(Calendar.HOUR_OF_DAY) + ":" + d1.get(Calendar.MINUTE)
//				+ " - " + d2.get(Calendar.DAY_OF_MONTH) + "~"
//				+ d2.get(Calendar.HOUR_OF_DAY) + ":" + d2.get(Calendar.MINUTE)
//				+ "; " + +d21.get(Calendar.DAY_OF_MONTH) + "~"
//				+ d21.get(Calendar.HOUR_OF_DAY) + ":"
//				+ d21.get(Calendar.MINUTE) + " - "
//				+ d22.get(Calendar.DAY_OF_MONTH) + "~"
//				+ d22.get(Calendar.HOUR_OF_DAY) + ":"
//				+ d22.get(Calendar.MINUTE) + d31.get(Calendar.DAY_OF_MONTH)
//				+ "~" + d31.get(Calendar.HOUR_OF_DAY) + ":"
//				+ d31.get(Calendar.MINUTE) + " - "
//				+ d32.get(Calendar.DAY_OF_MONTH) + "~"
//				+ d32.get(Calendar.HOUR_OF_DAY) + ":"
//				+ d32.get(Calendar.MINUTE) + "}");

		assertTrue(test != null);
		assertTrue(test.size() > 0);
	}

	public void testAddNights() {
		AutoGenerator auto = new AutoGenerator(null, null);
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(2013, 9, 13, 22, 0);
		GregorianCalendar calz = new GregorianCalendar();
		Date date = new Date(System.currentTimeMillis());
		calz.setTime(date);
		// calz.set(2013, 10, 4, 18, 0);
		// SimpleDateFormat sdf = new SimpleDateFormat();
		// Log.d("COME..",
		// "" + sdf.format(calz.getTime()) + " --- "
		// + sdf.format(cal.getTime()));
		List<TimeBox> lol = auto.removeNights(calz, cal);

		// StringBuilder sb = new StringBuilder();
		// sb.append("These are the nights: (Amount = " + lol.size() + ") \n");
		// Calendar cal2 = new GregorianCalendar();
		// SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm:ss");
		// for (TimeBox tb : lol) {
		// // cal.setTimeInMillis(tb.start);
		// // cal2.setTimeInMillis(tb.end);
		// Date d1 = new Date(tb.start);
		// Date d2 = new Date(tb.end);
		//
		// sb.append("TIMEBOX={" + formatter.format(d1) + " - "
		// + formatter.format(d2) + "}, \n");
		// }

		assertTrue(!lol.isEmpty());
	}

	public void testFixOverlap() {
		AutoGenerator auto = new AutoGenerator(null, null);
		LinkedList<TimeBox> testPass = new LinkedList<TimeBox>();
		TimeBox one = auto.new TimeBox(1000l, 2000l), two = auto.new TimeBox(
				1500l, 3000l);
		testPass.add(one);
		testPass.add(two);

		auto.fixOverlap(testPass);
		assertEquals(1, testPass.size());

		TimeBox three = auto.new TimeBox(3500l, 9000l), four = auto.new TimeBox(
				4000l, 5000l);
		TimeBox five = auto.new TimeBox(6000l, 10000l), six = auto.new TimeBox(
				9500l, 12000l);

		testPass.add(three);
		testPass.add(four);
		testPass.add(five);
		testPass.add(six);

		auto.fixOverlap(testPass);
		assertEquals(2, testPass.size());
	}

}
