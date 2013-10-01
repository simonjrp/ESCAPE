package se.chalmers.dat255.group22.escape.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.ListObject;

public abstract class AutoGenerator {
	
	private List<ListObject> schedule;
	private List<IBlockObject> blocks;
	
	public AutoGenerator(List<ListObject> currentSchedule, List<IBlockObject> blocks) {
		this.schedule = currentSchedule;
		this.blocks = blocks;
	}
	
	public List<ListObject> generate() {
		// When is the user free?
		Calendar now = new GregorianCalendar();
		Calendar sunday = new GregorianCalendar(now.get(Calendar.YEAR),
				now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

		// Split the blocks into the parts they want

		// Place the splits into the time slots
		// 			-- Prioritize the splits that can only be 
		// 				placed within certain time windows

		// If not all splits fit, overflow somewhere

		// Return the list with the newly created listObjects
		return null;
	}
	
	public abstract boolean validate();
	
}
