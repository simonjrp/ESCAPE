package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import se.chalmers.dat255.group22.escape.objects.BlockObject;
import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeWindow;
import se.chalmers.dat255.group22.escape.utilities.AutoGenerator;
import android.test.AndroidTestCase;
import android.util.Log;

public class TestAutoGenerator extends AndroidTestCase {

	public void testWithSimpleSchedule() {
		
		List<ListObject> schedule = new LinkedList<ListObject>();
		ListObject l1 = new ListObject(1, "School");
		Calendar d1 = Calendar.getInstance();
		d1.add(Calendar.HOUR_OF_DAY, 1);
		d1.set(Calendar.MINUTE, 0);
		d1.set(Calendar.SECOND, 0);
		d1.set(Calendar.MILLISECOND, 0);
		d1.add(Calendar.HOUR_OF_DAY, 1);
		Calendar d2 = (Calendar) d1.clone();
		d2.add(Calendar.HOUR_OF_DAY, 1);

		ListObject l2 = new ListObject(2, "Math");
		Calendar d21 = (Calendar) d2.clone();
		d21.add(Calendar.HOUR_OF_DAY, 5);
		Calendar d22 = (Calendar) d21.clone();
		d22.add(Calendar.HOUR_OF_DAY, 1);

		ListObject l3 = new ListObject(2, "ASD");
		Calendar d31 = (Calendar) d22.clone();
		d31.add(Calendar.HOUR_OF_DAY, 5);
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
		IBlockObject block1 = new BlockObject("working", TimeWindow.WORKING, 10,
				44);
		IBlockObject block2 = new BlockObject("working2", TimeWindow.WORKING, 10, 60);
		IBlockObject block3 = new BlockObject("all", TimeWindow.ALL, 30, 60);
		blocks.add(block1);
		blocks.add(block2);
		blocks.add(block3);

		AutoGenerator auto = new AutoGenerator(schedule, blocks);
		List<ListObject> test = auto.generate();

		assertTrue(test != null);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");
		StringBuilder sb = new StringBuilder();
		sb.append("Count: " + test.size() + "\n");
		for (ListObject lo : test) {
			Time time = lo.getTime();
			sb.append("" + lo.getName() + ": "
					+ sdf.format(time.getStartDate()) + " to "
					+ sdf.format(time.getEndDate()) + "\n");
		}
		Log.d("ListObjects", sb.toString());
	}
	
public void testWithoutSchedule() {
		
		List<ListObject> schedule = new LinkedList<ListObject>();
			

		List<IBlockObject> blocks = new LinkedList<IBlockObject>();
		IBlockObject block1 = new BlockObject("working", TimeWindow.WORKING, 10,
				44);
		IBlockObject block2 = new BlockObject("working2", TimeWindow.WORKING, 10, 60);
		IBlockObject block3 = new BlockObject("all", TimeWindow.ALL, 30, 60);
		blocks.add(block1);
		blocks.add(block2);
		blocks.add(block3);

		AutoGenerator auto = new AutoGenerator(schedule, blocks);
		List<ListObject> test = auto.generate();

		assertTrue(test != null);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");
		StringBuilder sb = new StringBuilder();
		sb.append("Count: " + test.size() + "\n");
		for (ListObject lo : test) {
			Time time = lo.getTime();
			sb.append("" + lo.getName() + ": "
					+ sdf.format(time.getStartDate()) + " to "
					+ sdf.format(time.getEndDate()) + "\n");
		}
		Log.d("ListObjects", sb.toString());
	}

//	public void testAddNights() {
//		AutoGenerator auto = new AutoGenerator(null, null);
//		GregorianCalendar cal = new GregorianCalendar();
//		cal.set(2013, 9, 13, 22, 0);
//		GregorianCalendar calz = new GregorianCalendar();
//		Date date = new Date(System.currentTimeMillis());
//		calz.setTime(date);
//
//		List<TimeBox> lol = auto.removeNights(calz, cal);
//
//		assertTrue(!lol.isEmpty());
//	}

//	public void testFixOverlap() {
//		AutoGenerator auto = new AutoGenerator(null, null);
//		LinkedList<TimeBox> testPass = new LinkedList<TimeBox>();
//		TimeBox one = auto.new TimeBox(1000l, 2000l), two = auto.new TimeBox(
//				1500l, 3000l);
//		testPass.add(one);
//		testPass.add(two);
//
//		auto.fixOverlap(testPass);
//		assertEquals(1, testPass.size());
//
//		TimeBox three = auto.new TimeBox(3500l, 9000l), four = auto.new TimeBox(
//				4000l, 5000l);
//		TimeBox five = auto.new TimeBox(6000l, 10000l), six = auto.new TimeBox(
//				9500l, 12000l);
//
//		testPass.add(three);
//		testPass.add(four);
//		testPass.add(five);
//		testPass.add(six);
//
//		auto.fixOverlap(testPass);
//		assertEquals(2, testPass.size());
//	}

}
