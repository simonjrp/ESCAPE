package se.chalmers.dat255.group22.escape.utilities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeWindow;

/**
 * A utility for autogenerating splittable blocks into a schedule
 * 
 * @author anno
 * 
 */
public class AutoGenerator {

	public static final int NIGHT_START = 22;
	public static final int NIGHT_END = 8;

	private List<ListObject> schedule;
	private List<IBlockObject> blocks;

	/**
	 * Lists are mutated inside this class.
	 * 
	 * @param currentSchedule
	 * @param blocks
	 */
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
		end.add(Calendar.HOUR_OF_DAY, 24);

		// Get a list of all the nights for the rest of the week
		LinkedList<TimeBox> totalList = removeNights(now, end);

		// Place the nights sorted together with the original schedule
		Iterator<ListObject> iterator = schedule.iterator();
		while (iterator.hasNext()) {
			totalList.add(convertListObject(iterator.next()));
		}

		Collections.sort(totalList);
		fixOverlap(totalList);

		// Prioritize the blocks depending on if they are constrained to working
		// hours or leisure (or all)
		ArrayList<IBlockObject> first = new ArrayList<IBlockObject>();
		ArrayList<IBlockObject> second = new ArrayList<IBlockObject>();

		for (IBlockObject block : blocks) {
			if (block.getTimeWindow() != TimeWindow.ALL) {
				first.add(block);
			} else {
				second.add(block);
			}
		}
		List<ArrayList<IBlockObject>> priolists = new LinkedList<ArrayList<IBlockObject>>();
		priolists.add(first);
		priolists.add(second);

		// Place the splits into the time slots
		// -- Prioritize the splits that can only be
		// placed within certain time windows

		List<ListObject> lolist = new LinkedList<ListObject>();

		for (ArrayList<IBlockObject> list : priolists) {
			if (!list.isEmpty()) {
				// First priority
				Collections.sort(first, new Comparator<IBlockObject>() {

					/*
					 * Sorts on session size
					 */
					@Override
					public int compare(IBlockObject first, IBlockObject second) {
						int a = first.getSessionMinutes(), b = second
								.getSessionMinutes();
						if (a == b) {
							return 0;
						}
						return a - b;
					}

				});
				int[][] data = new int[first.size()][3];
				int i = 0;
				for (IBlockObject block : first) {
					data[i][0] = block.getSplitAmount();
					data[i][1] = block.getSplitAmount();
					data[i][2] = block.getTimeWindow().getNumVal();
				}
				ListIterator<TimeBox> it = totalList.listIterator(0);
				if (it.hasNext()) {
					TimeBox current;
					TimeBox next = it.next();
					while (it.hasNext()) {
						current = next;
						if (it.hasNext()) {
							next = it.next();

							Long diffLong = next.start - current.end;
							IBlockObject fittingBlock = null;
							long blockTime = 0;
							double[] percentageLeft = new double[data.length];
							int[] order = new int[data.length];
							for (i = 0; i < percentageLeft.length; i++) {
								percentageLeft[i] = (double) data[i][1]
										/ data[i][0];
							}

							for (i = 0; i < percentageLeft.length; i++) {
								int max = 0;
								for (int j = 0; j < percentageLeft.length; j++) {
									if (percentageLeft[j] > percentageLeft[max]) {
										max = j;
									}
								}
								percentageLeft[max] = 0;
								// Set the new order
								order[max] = i;
							}

							for (i = 0; i < first.size(); i++) {
								int nextOnTurn = 0;
								for (int j = 0; j < order.length; j++) {
									if (order[j] == i) {
										nextOnTurn = j;
										break;
									}
								}
								IBlockObject currentBlock = first
										.get(nextOnTurn);
								if (data[nextOnTurn][1] > 0) {
									blockTime = (long) 60000
											* (data[nextOnTurn][1] == 1 ? currentBlock
													.getLastSplitMinutes()
													: currentBlock
															.getSessionMinutes());
									if (diffLong >= blockTime) {
										fittingBlock = currentBlock;
										// Reduce the remaining block amount with 1
										data[nextOnTurn][1]--;
										break;
									}
								}
							}
							if (fittingBlock != null) {
								// INSERT
								ListObject insert = new ListObject(-1,
										fittingBlock.getName());
								lolist.add(insert);
								insert.setTime(new Time(-1, new Date(
										current.end), new Date(current.end
										+ blockTime)));
								it.previous();
								it.add(new TimeBox(current.end, current.end
										+ blockTime));
								it.previous();
							}
						} // else {
							// Here we can do if there is no more timeBoxes (last
							// space
							// until list end
							// }
						// Check if we have any more blocks to place
						boolean canPlace = false;
						for (i = 0; i < data.length; i++) {
							if (data[i][1] > 0) {
								canPlace = true;
								break;
							}
						}
						if (!canPlace) {
							break;
						}
					}
				} 
			}
		}

		// Splits not used is stored.

		// Return the list with the newly created listObjects
		return lolist;
	}

	/*
	 * Fixes overlap between TimeBoxes in a list.
	 */
	private void fixOverlap(LinkedList<TimeBox> fullList) {
		Iterator<TimeBox> iterator = fullList.iterator();
		if (iterator.hasNext()) {
			TimeBox current, next = iterator.next();
			while (iterator.hasNext()) {
				current = next;

				if (iterator.hasNext()) {
					next = iterator.next();
					Long end = current.end;
					while (end >= next.start) {
						if (next.end >= end) {
							end = next.end;
						}
						iterator.remove();
						next = iterator.next();
					}
					current.end = end;
				}
			}
		}
	}

	private LinkedList<TimeBox> removeNights(Calendar start, Calendar end) {
		// If start is after or the same time as end, return null
		if (start.compareTo(end) > -1) {
			return null;
		}

		LinkedList<TimeBox> nightList = new LinkedList<TimeBox>();
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
	protected class TimeBox implements Comparable<TimeBox> {
		public Long start;
		public Long end;

		TimeBox(Long start, Long end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int compareTo(TimeBox another) {
			if (this.start >= another.end) {
				return 1;
			} else if (this.end <= another.start) {
				return -1;
			} else if ((this.start < another.end && this.end > another.end)
					|| (this.end > another.start && this.start < another.start)) {
				return 1;
			} else if ((another.start > this.end && another.end > this.end)
					|| (another.end > this.start && another.start < this.start)) {
				return -1;
			}
			return 0;
		}

	}

	private TimeBox convertListObject(ListObject lo) {
		Time time = lo.getTime();
		if (time != null) {
			TimeBox tb = new TimeBox(time.getStartDate().getTime(), time
					.getEndDate().getTime());
			return tb;
		}
		return null;
	}

	public boolean validate() { 
		return false;
	}
}
