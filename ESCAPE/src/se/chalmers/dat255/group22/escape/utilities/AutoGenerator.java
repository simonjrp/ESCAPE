package se.chalmers.dat255.group22.escape.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import android.animation.TimeInterpolator;

import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeWindow;

public abstract class AutoGenerator {

	public static final int NIGHT_START = 22;
	public static final int NIGHT_END = 8;

	private List<ListObject> schedule;
	private List<IBlockObject> blocks;

	public AutoGenerator(List<ListObject> currentSchedule,
			List<IBlockObject> blocks) {
		this.schedule = currentSchedule;
		this.blocks = blocks;
	}

	public List<ListObject> generate() {
		// ** When is the user free?
		// Find where in the week we are
		Calendar now = new GregorianCalendar();
		// Find the next Sunday 22.00
		Calendar end = new GregorianCalendar(now.get(Calendar.YEAR),
				now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

		while (end.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			end.add(Calendar.DAY_OF_WEEK, 1);
		}
		end.add(Calendar.HOUR_OF_DAY, NIGHT_START);

		// Get a list of all the nights for the rest of the week
		List<TimeBox> totalList = removeNights(now, end);
		
		// Place the nights sorted together with the original schedule
		

		// Prioritize the blocks depending on if they are constrained to working
		// hours or leisure (or all)
		List<IBlockObject> first = new LinkedList<IBlockObject>();
		List<IBlockObject> second = new LinkedList<IBlockObject>();

		for (IBlockObject block : blocks) {
			if (block.getTimeWindow() != TimeWindow.ALL) {
				first.add(block);
			} else {
				second.add(block);
			}
		}

		// Place the splits into the time slots
		// -- Prioritize the splits that can only be
		// placed within certain time windows

		// If not all splits fit, overflow somewhere

		// Return the list with the newly created listObjects
		return null;
	}

	private List<TimeBox> removeNights(Calendar start, Calendar end) {
		// If start is after or the same time as end, return null
		if (start.compareTo(end) > -1) {
			return null;
		}

		List<TimeBox> nightList = new LinkedList<TimeBox>();
		// Get new objects we can change
		Calendar s = (Calendar) start.clone();
		Calendar e = (Calendar) end.clone();

		int hour = s.get(Calendar.HOUR_OF_DAY);
		// Check that we are not within a night right now
		if (hour >= NIGHT_START || hour < NIGHT_END) {
			// If we are within a night, we create a time box from now to when
			// it ends
			Long startTimeBox = s.getTimeInMillis();
			s.set(Calendar.HOUR_OF_DAY, hour + 1);
			s.set(Calendar.MINUTE, 0);
			s.set(Calendar.SECOND, 0);
			s.set(Calendar.MILLISECOND, 0);
			while (s.get(Calendar.HOUR_OF_DAY) != NIGHT_END) {
				s.add(Calendar.HOUR_OF_DAY, 1);
			}
			Long endTimeBox = s.getTimeInMillis();
			nightList.add(new TimeBox(startTimeBox, endTimeBox));
		}

		// Iterate from start to end and add nights as they come by
		// TODO Might change this to be more efficient. Not iterate through
		// every non-night hour of the week and instead jump the distance to the
		// next night
		while (s.compareTo(e) < 0) {
			if (s.get(Calendar.HOUR_OF_DAY) == NIGHT_START) {
				Long timeBoxStart = s.getTimeInMillis();
				// Calculate the night span
				int nightSpan = NIGHT_END;
				// Dead because night start and night end is set statically
				nightSpan += (NIGHT_START > NIGHT_END) ? (24 - NIGHT_START)
						: -NIGHT_START;
				// Move forward time through the night
				s.add(Calendar.HOUR_OF_DAY, nightSpan);
				// Add the night to the list
				nightList.add(new TimeBox(timeBoxStart, s.getTimeInMillis()));
			}

			s.add(Calendar.HOUR_OF_DAY, 1);
		}
		return nightList;
	}

	// This inner class represents a "timebox", i.e. a start- and an endtime
	// that belongs together.
	// The start and end time is represented by milliseconds (since 1970 Jan 1)
	protected static class TimeBox {
		public static Long start;
		public static Long end;

		TimeBox(Long start, Long end) {
			this.start = start;
			this.end = end;
		}

		static TimeBox convertListObject(ListObject lo) {
			Time time = lo.getTime();
			if (time != null) {
				TimeBox tb = new TimeBox(time.getStartDate().getTime(), time
						.getEndDate().getTime());
				return tb;
			}

			return null;
		}

	}

	public abstract boolean validate();

}
