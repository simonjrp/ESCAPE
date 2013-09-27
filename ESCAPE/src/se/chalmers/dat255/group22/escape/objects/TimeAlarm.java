package se.chalmers.dat255.group22.escape.objects;

import java.sql.Date;

/**
 * Simple object class to represent a time based alarm
 * 
 * @author Johanna and Mike
 * 
 */
public class TimeAlarm {

	private final int id;
	private Date date;

	public TimeAlarm(int id, Date date) {
		this.id = id;
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimeAlarm [id=" + id + ", date=" + date + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeAlarm other = (TimeAlarm) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
