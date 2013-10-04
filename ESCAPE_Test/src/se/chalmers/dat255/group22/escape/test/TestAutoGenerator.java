package se.chalmers.dat255.group22.escape.test;

import java.sql.Date;
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
import android.test.AndroidTestCase;
import android.util.Log;

public class TestAutoGenerator extends AndroidTestCase {

	public void testWithSimpleSchedule() {
		List<ListObject> schedule = new LinkedList<ListObject>();
		ListObject l1 = new ListObject(1, "School");
		Calendar d1 = new GregorianCalendar(2013, 10, 4, 15, 30);
		d1.add(Calendar.HOUR_OF_DAY, 1);
		Calendar d2 = (Calendar) d1.clone();
		d2.add(Calendar.HOUR_OF_DAY, 1);
		
		ListObject l2 = new ListObject(2, "Math");
		Calendar d21 = new GregorianCalendar(2013, 10, 4, 15, 30);
		d21.add(Calendar.HOUR_OF_DAY, 5);
		Calendar d22 = (Calendar) d21.clone();
		d22.add(Calendar.HOUR_OF_DAY, 1);
		
		ListObject l3 = new ListObject(2, "ASD");
		Calendar d31 = new GregorianCalendar(2013, 10, 5, 8, 5);
		d21.add(Calendar.HOUR_OF_DAY, 0);
		Calendar d32 = (Calendar) d31.clone();
		d22.add(Calendar.HOUR_OF_DAY, 1);

		l1.setTime(new Time(1, new Date(d1.getTimeInMillis()), new Date(d2
				.getTimeInMillis())));
		l2.setTime(new Time(2, new Date(d21.getTimeInMillis()), new Date(d22
				.getTimeInMillis())));
		schedule.add(l1);
		schedule.add(l2);
		schedule.add(l3);

		List<IBlockObject> blocks = new LinkedList<IBlockObject>();
		IBlockObject block1 = new BlockObject("test", TimeWindow.WORKING, 1, 30);
		blocks.add(block1);

		AutoGenerator auto = new AutoGenerator(schedule, blocks);
		List<ListObject> test = auto.generate();

		
		Log.d("testCount", "testCount : " + test.size() + " with :");
		StringBuilder sb = new StringBuilder();
		sb.append("LO={" 
				+ d1.get(Calendar.DAY_OF_MONTH) + "~"
				+ d1.get(Calendar.HOUR_OF_DAY) + ":"
				+ d1.get(Calendar.MINUTE) + " - "
				+ d2.get(Calendar.DAY_OF_MONTH) + "~"
				+ d2.get(Calendar.HOUR_OF_DAY) + ":"
				+ d2.get(Calendar.MINUTE) + "; " +
				+ d21.get(Calendar.DAY_OF_MONTH) + "~"
				+ d21.get(Calendar.HOUR_OF_DAY) + ":"
				+ d21.get(Calendar.MINUTE) + " - "
				+ d22.get(Calendar.DAY_OF_MONTH) + "~"
				+ d22.get(Calendar.HOUR_OF_DAY) + ":"
				+ d22.get(Calendar.MINUTE)
				+ d31.get(Calendar.DAY_OF_MONTH) + "~"
				+ d31.get(Calendar.HOUR_OF_DAY) + ":"
				+ d31.get(Calendar.MINUTE) + " - "
				+ d32.get(Calendar.DAY_OF_MONTH) + "~"
				+ d32.get(Calendar.HOUR_OF_DAY) + ":"
				+ d32.get(Calendar.MINUTE)
				+ "}");

		Log.d("Schedule:", sb.toString());
		for (ListObject lo : test) {
			String name = lo.getName();
			Calendar parser = new GregorianCalendar();
			parser.setTime(lo.getTime().getStartDate());
			Calendar parser2 = new GregorianCalendar();
			parser2.setTime(lo.getTime().getEndDate());
			Log.d("testWithSimpleSchedule",
					"" + name + " at " 
							+ parser.get(Calendar.DAY_OF_MONTH) + "~"
							+ parser.get(Calendar.HOUR_OF_DAY) + ":"
							+ parser.get(Calendar.MINUTE) + " - "
							+ parser2.get(Calendar.DAY_OF_MONTH) + "~"
							+ parser2.get(Calendar.HOUR_OF_DAY) + ":"
							+ parser2.get(Calendar.MINUTE));
		}

		assertTrue(test != null);
		assertTrue(test.size() > 0);
	}

}
