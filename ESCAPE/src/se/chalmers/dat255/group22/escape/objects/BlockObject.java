package se.chalmers.dat255.group22.escape.objects;

public class BlockObject implements IBlockObject {
	
	private String name;
	private TimeWindow timeWindow;
	private int hours;
	private int sessionMinutes;
	private int splitAmount;
	private int lastSplitMinutes;
	
	public BlockObject(String name, TimeWindow timeWindow, int hours,
			int sessionMinutes) {
		this.name = name;
		this.timeWindow = timeWindow;
		this.hours = hours;
		this.sessionMinutes = sessionMinutes;
		this.splitAmount = (hours * 60 % sessionMinutes > 0 ? (hours * 60 / sessionMinutes) + 1
				: hours * 60 / sessionMinutes);
		this.lastSplitMinutes = (hours * 60 % sessionMinutes > 0 ? hours * 60
				% sessionMinutes : sessionMinutes);

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
		return splitAmount;
	}

	@Override
	public int getLastSplitMinutes() {
		return lastSplitMinutes;
	}
	
}
