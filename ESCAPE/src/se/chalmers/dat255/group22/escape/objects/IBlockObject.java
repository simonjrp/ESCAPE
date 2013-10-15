package se.chalmers.dat255.group22.escape.objects;

public interface IBlockObject {
	
	public int getId();
	
	public String getName();
	
	public TimeWindow getTimeWindow();
	
	public int getHours();
	
	public int getSessionMinutes();
	
	public int getSplitAmount();
	
	public int getLastSplitMinutes();

}
