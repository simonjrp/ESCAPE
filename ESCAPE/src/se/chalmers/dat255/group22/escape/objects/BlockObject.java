package se.chalmers.dat255.group22.escape.objects;

public class BlockObject implements IBlockObject {

	private String name;
	private TimeWindow timeWindow;
	private int hours;
	private int sessionMinutes;
	private int id;

	public BlockObject(String name, TimeWindow timeWindow, int hours,
			int sessionMinutes) {
		this.id = -1;
		this.name = name;
		this.timeWindow = timeWindow;
		this.hours = hours;
		this.sessionMinutes = sessionMinutes;

	}
	public BlockObject(int id, String name, TimeWindow timeWindow, int hours,
			int sessionMinutes) {
		this(name, timeWindow, hours, sessionMinutes);
		this.id = id;

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public int getHours() {
		return hours;
	}

	@Override
	public int getSessionMinutes() {
		return sessionMinutes;
	}

	@Override
	public int getSplitAmount() {
		return (hours * 60 % sessionMinutes > 0
				? (hours * 60 / sessionMinutes) + 1
				: hours * 60 / sessionMinutes);
	}

	@Override
	public int getLastSplitMinutes() {
		return (hours * 60 % sessionMinutes > 0
				? hours * 60 % sessionMinutes
				: sessionMinutes);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param timeWindow
	 *            the timeWindow to set
	 */
	public void setTimeWindow(TimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	/**
	 * @param hours
	 *            the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @param sessionMinutes
	 *            the sessionMinutes to set
	 */
	public void setSessionMinutes(int sessionMinutes) {
		this.sessionMinutes = sessionMinutes;
	}

}
