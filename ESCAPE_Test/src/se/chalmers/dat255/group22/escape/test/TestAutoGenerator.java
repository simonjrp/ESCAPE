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

public class TestAutoGenerator extends AndroidTestCase {
	
	public void testWithSimpleSchedule() {
		List<ListObject> schedule = new LinkedList<ListObject>();
		ListObject l1 = new ListObject(1, "School");
		Calendar d1 = new GregorianCalendar();
		d1.add(Calendar.HOUR_OF_DAY, 1);
		Calendar d2 = (Calendar) d1.clone();
		d2.add(Calendar.HOUR_OF_DAY, 2);
		ListObject l2 = new ListObject(2, "Math");
		Calendar d21 = new GregorianCalendar();
		d1.add(Calendar.HOUR_OF_DAY, 5);
		Calendar d22 = (Calendar) d1.clone();
		d2.add(Calendar.HOUR_OF_DAY, 7);
		
		l1.setTime(new Time(1, new Date(d1.getTimeInMillis()), new Date(d2.getTimeInMillis())));
		l2.setTime(new Time(2, new Date(d21.getTimeInMillis()), new Date(d22.getTimeInMillis())));
		schedule.add(l1);
		schedule.add(l2);
		
		List<IBlockObject> blocks = new LinkedList<IBlockObject>();
		IBlockObject block1 = new BlockObject("test", TimeWindow.WORKING, 3, 30);
		blocks.add(block1);
		
		AutoGenerator auto = new AutoGenerator(schedule, blocks);
		List<ListObject> test = auto.generate();
		
		assertTrue(test != null);
		assertTrue(test.size() > 0);
	}
	
	
}
