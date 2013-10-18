package se.chalmers.dat255.group22.escape.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import se.chalmers.dat255.group22.escape.objects.TimeWindow;

/**
 * A utility for autogenerating splittable blocks into a schedule
 * 
 * @author Mike
 * 
 */
public class AutoGenerator {

	public static final int NIGHT_START = 22;
	public static final int NIGHT_END = 8;

	private List<ListObject> schedule;
	private List<IBlockObject> blocks;

	/**
	 * Lists are not mutated inside this class.
	 * 
	 * @param currentSchedule
	 * 			The schedule to generate "around"
	 * @param blocks
	 * 			The blocks to generate
	 */
	public AutoGenerator(List<ListObject> currentSchedule,
			List<IBlockObject> blocks) {
		this.schedule = currentSchedule == null ? new ArrayList<ListObject>()
				: new ArrayList<ListObject>(currentSchedule);
		this.blocks = blocks == null ? new ArrayList<IBlockObject>()
				: new LinkedList<IBlockObject>(blocks);
	}

	/**
	 * @return the schedule
	 */
	public List<ListObject> getSchedule() {
		return schedule;
	}

	/**
	 * @return the blocks
	 */
	public List<IBlockObject> getBlocks() {
		return blocks;
	}

	public List<ListObject> generate() {
		// ** When is the user free?
		// Find where in the week we are
		Calendar now = new GregorianCalendar();
		now.setTimeInMillis(System.currentTimeMillis());
		// Find the next Sunday 22.00
		Calendar end = new GregorianCalendar(now.get(Calendar.YEAR),
				now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

		while (end.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			end.add(Calendar.DAY_OF_WEEK, 1);
		}
		end.add(Calendar.HOUR_OF_DAY, 24);

		// Get a list of all the nights for the rest of the week
		LinkedList<TimeBox> totalList = removeNights(now, end);
		
		// Add a small timebox so that it starts to add right after "now"
		totalList.add(new TimeBox(now.getTimeInMillis(), now.getTimeInMillis()-1));

		// Place the nights sorted together with the original schedule
		Iterator<ListObject> iterator = schedule.iterator();
		while (iterator.hasNext()) {
			TimeBox timeBox = convertListObject(iterator.next());
			if (timeBox != null) {
				totalList.add(timeBox);
			}
		}

		
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
				// Larger sessions get priority over smaller
				Collections.sort(list, Collections
						.reverseOrder(new Comparator<IBlockObject>() {

							/*
							 * Sorts on session size
							 */
							@Override
							public int compare(IBlockObject first,
									IBlockObject second) {
								int a = first.getSessionMinutes(), b = second
										.getSessionMinutes();
								if (a == b) {
									return 0;
								}
								return a - b;
							}

						}));
				// Data[][] is used to remember information about the different
				// blocks, and to be able to modify it
				int[][] data = new int[list.size()][3];
				int i = 0;
				for (IBlockObject block : list) {
					data[i][0] = block.getSplitAmount();
					data[i][1] = block.getSplitAmount();
					data[i][2] = block.getTimeWindow().getNumVal();
					i++;
				}
				if (data[0][2] == TimeWindow.ALL.getNumVal()) {
					// If we entered the last priority (the blocks that can be
					// placed anywhere) we remove all the supporting timeboxes
					// for working hour begin/end we added in previous steps,
					// since they can span these "borders"
					removeAllNoDurationTimeBoxes(totalList);
				}
				ListIterator<TimeBox> it = totalList.listIterator(0);

				if (it.hasNext()) {
					TimeBox current;
					TimeBox next = it.next();
					while (it.hasNext()) {
						current = next;
						if (it.hasNext()) {
							next = it.next();

							if (data[0][2] != TimeWindow.ALL.getNumVal()) {
								// If we are trying to insert
								// something that isn't an
								// "all" timeWindow we need
								// to check if there is a
								// split within the time
								// duration we are checking
								Calendar workingDayStart = Calendar
										.getInstance();
								workingDayStart.setTimeInMillis(current.end);
								workingDayStart.set(Calendar.HOUR_OF_DAY, 8);
								workingDayStart.set(Calendar.MINUTE, 0);
								workingDayStart.set(Calendar.SECOND, 0);
								workingDayStart.set(Calendar.MILLISECOND, 0);
								Long workStart = workingDayStart
										.getTimeInMillis();

								Calendar workingDayEnd = (Calendar) workingDayStart
										.clone();
								workingDayEnd.setTimeInMillis(current.end);
								workingDayEnd.set(Calendar.HOUR_OF_DAY, 17);
								workingDayEnd.set(Calendar.MINUTE, 0);
								workingDayEnd.set(Calendar.SECOND, 0);
								workingDayEnd.set(Calendar.MILLISECOND, 0);
								Long workEnd = workingDayEnd.getTimeInMillis();

								if (current.end < workStart
										&& next.start > workStart) {
									// Here the split is 8
									// We add a timebox that starts and ends at
									// the same time and sets next to point to
									// it so that it is considered during the
									// rest of the algorithm
									it.previous();
									it.add(new TimeBox(workStart, workStart));
									next = it.next();
								} else if (current.end < workEnd
										&& next.start > workEnd) {
									// Here the split is 17
									it.previous();
									it.add(new TimeBox(workEnd, workEnd));

									next = it.previous();
									it.next();
								}
							}

							Long timeSlotSize = next.start - current.end;

							// We order the blocks to use by the percentage they
							// have been used so that we don't end up with a
							// scenario where one "block" has been used a lot
							// more than the others in the end, if there isn't
							// enough time to add all the blocks sessions
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

							// Check all the blocks we have if they fit in the
							// duration
							for (i = 0; i < list.size(); i++) {
								int nextOnTurn = 0;
								for (int j = 0; j < order.length; j++) {
									if (order[j] == i) {
										nextOnTurn = j;
										break;
									}
								}
								IBlockObject currentBlock = list
										.get(nextOnTurn);
								if (data[nextOnTurn][1] > 0) {

									if (data[nextOnTurn][2] != TimeWindow.ALL
											.getNumVal()) {
										// If the block is not of the same
										// timeWindow as the current period we
										// continue with the loop without doing
										// anything
										TimeWindow timeWindow = calculateTimeWindow(
												current.end, next.start);
										if (data[nextOnTurn][2] != timeWindow
												.getNumVal()) {
											continue;
										}
									}

									// We make sure to check if it is the last
									// session and get the proper session time
									blockTime = (long) 60000
											* (data[nextOnTurn][1] == 1 ? currentBlock
													.getLastSplitMinutes()
													: currentBlock
															.getSessionMinutes());
									if (timeSlotSize >= blockTime) {
										fittingBlock = currentBlock;
										// Reduce the remaining block amount
										// with 1
										data[nextOnTurn][1]--;
										break;
									}
								}
							}
							if (fittingBlock != null) {
								// Inserting a block that fits into the
								// resulting list object list.
								// Also adding a timebox to the schedule so that
								// we don't place something at the same spot as
								// it, worth noting, we place the iterator
								// "one step behind" to recalculate with the new
								// obstruction in mind
								ListObject insert = new ListObject(-1,
										fittingBlock.getName());
								insert.setComment("[Autogenerated]");
								lolist.add(insert);
								insert.setTime(new Time(-1, new Date(
										current.end), new Date(current.end
										+ blockTime)));
								it.previous();
								it.add(new TimeBox(current.end, current.end
										+ blockTime));
								it.previous();
							}
						}
						// Check if we are done; that we don't have any more
						// IBlockObjects to place
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

		return lolist;
	}

	/*
	 * This removes all the time boxes that have no real duration
	 */
	private void removeAllNoDurationTimeBoxes(LinkedList<TimeBox> totalList) {

		ListIterator<TimeBox> li = totalList.listIterator(0);
		while (li.hasNext()) {
			TimeBox tb = li.next();
			long duration = tb.end - tb.start;
			if (duration == 0) {
				li.remove();
			}
		}
	}

	/*
	 * Returns the right TimeWindow that spans between the two time parameters
	 * I.e. checks if it is working hours, leisure time or in between
	 */
	private TimeWindow calculateTimeWindow(Long start, Long end) {

		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = (Calendar) calStart.clone();

		calStart.setTimeInMillis(start);
		calEnd.setTimeInMillis(end);

		Calendar workStart = Calendar.getInstance();
		Calendar workEnd = (Calendar) calStart.clone();

		workStart.set(Calendar.HOUR_OF_DAY, 8);
		workStart.set(Calendar.MINUTE, 0);
		workStart.set(Calendar.SECOND, 0);
		workStart.set(Calendar.MILLISECOND, 0);
		workEnd.set(Calendar.HOUR_OF_DAY, 17);
		workEnd.set(Calendar.MINUTE, 0);
		workEnd.set(Calendar.SECOND, 0);
		workEnd.set(Calendar.MILLISECOND, 0);

		if (calStart.compareTo(workStart) < 0) {
			if (calEnd.compareTo(workStart) <= 0) {
				return TimeWindow.LEISURE;
			} else {
				return TimeWindow.ALL;
			}
		} else if (calStart.compareTo(workEnd) >= 0) {
			return TimeWindow.LEISURE;
		} else {
			if (calEnd.compareTo(workEnd) > 0) {
				return TimeWindow.ALL;
			} else {
				return TimeWindow.WORKING;
			}
		}
	}

	/*
	 * Fixes overlap between TimeBoxes in a list.
	 */
	private void fixOverlap(LinkedList<TimeBox> fullList) {
		Collections.sort(fullList);
		
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
						if (iterator.hasNext()) {
							next = iterator.next();
						} else {
							break;
						}
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
				s.set(Calendar.MINUTE, 0);
				s.set(Calendar.SECOND, 0);
				s.set(Calendar.MILLISECOND, 0);
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

		protected TimeBox(Long start, Long end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int compareTo(TimeBox another) {
			if (this.start < another.start) {
				return -1;
			} else if (this.start > another.start) {
				return 1;
			}
			return 0;
		}

	}

	/*
	 * Converts a list object with time into a TimeBox
	 */
	private TimeBox convertListObject(ListObject lo) {
		Time time = lo.getTime();
		if (time != null) {
			TimeBox tb = new TimeBox(time.getStartDate().getTime(), time
					.getEndDate().getTime());
			return tb;
		}
		return null;
	}

	public boolean validate(List<ListObject> newSchedule,
			List<ListObject> generatedSchedule) {
		List<ListObject> list = new ArrayList<ListObject>(newSchedule);
		list.addAll(generatedSchedule);
		Collections.sort(list, new Comparator<ListObject>() {
			/*
			 * Sorts on start time
			 */
			@Override
			public int compare(ListObject first, ListObject second) {
				long a = first.getTime().getStartDate().getTime();
				long b = second.getTime().getStartDate().getTime();
				if (a > b) {
					return 1;
				}

				if (a == b) {
					return 0;
				}
				return -1;
			}
		});

		Iterator<ListObject> iterator = list.iterator();
		while (iterator.hasNext()) {
			ListObject current = iterator.next();
			ListObject next;

			if (iterator.hasNext()) {
				next = iterator.next();
			} else {
				break;
			}

			if (next.getTime().getStartDate().getTime() < current.getTime()
					.getEndDate().getTime()) {
				return false;
			}
		}

		return true;
	}
}
