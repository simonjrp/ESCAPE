package se.chalmers.dat255.group22.escape;

import java.util.LinkedList;
import java.util.List;

// TODO Add the possibility for all object classes to be constructed without
// any id (or other primary key that is autoincremented)
/**
 * Object class that represents a ListObject.
 * 
 * @author Johanna & Mike
 */
public class ListObject {

	private final int id;
	private String name;
	private String comment;
	private boolean important;
	
//	private Category category;
	private Time time;
	private Place place;
	private TimeAlarm timeAlarm;
	private GPSAlarm gpsAlarm;
	
	private List<Category> categories;
	
	
	public ListObject(int id, String name) {
		this.id = id;
		this.name = name;
		this.important = false;
		this.categories = new LinkedList<Category>();
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return the important
	 */
	public boolean isImportant() {
		return important;
	}

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * @return the place
	 */
	public Place getPlace() {
		return place;
	}



	/**
	 * @return the timeAlarm
	 */
	public TimeAlarm getTimeAlarm() {
		return timeAlarm;
	}


	/**
	 * @return the gpsAlarm
	 */
	public GPSAlarm getGpsAlarm() {
		return gpsAlarm;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param important the important to set
	 */
	public void setImportant(boolean important) {
		this.important = important;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Time time) {
		this.time = time;
	}
	
	/**
	 * @param place the place to set
	 */
	public void setPlace(Place place) {
		this.place = place;
	}

	/**
	 * @param timeAlarm the timeAlarm to set
	 */
	public void setTimeAlarm(TimeAlarm timeAlarm) {
		this.timeAlarm = timeAlarm;
	}

	/**
	 * @param gpsAlarm the gpsAlarm to set
	 */
	public void setGpsAlarm(GPSAlarm gpsAlarm) {
		this.gpsAlarm = gpsAlarm;
	}
	
	/**
	 * @param category the category to set
	 */
	public void addToCategory(Category category) {
		categories.add(category);
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListObject [id=" + id + ", name=" + name + ", important="
				+ important + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ListObject other = (ListObject) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
}
