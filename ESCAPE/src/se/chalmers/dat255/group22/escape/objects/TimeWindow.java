package se.chalmers.dat255.group22.escape.objects;

public enum TimeWindow {
	WORKING(1), LEISURE(2), ALL(0);
	
	private final int numVal;
	
	TimeWindow(int numVal) {
		this.numVal = numVal;
	}

	public int getNumVal() {
		return numVal;
	}
	
	public static TimeWindow parseNumVal(int value) {
		switch (value) {
		case 1: return WORKING;
		case 2: return LEISURE;
		default: return ALL;
		}
	}
}
