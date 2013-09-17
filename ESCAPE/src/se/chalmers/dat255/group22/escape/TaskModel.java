package se.chalmers.dat255.group22.escape;

import android.location.Location;

import java.sql.Date;
import java.sql.Time;

/**
 * A model containing the data needed for a specific task.
 * @author tholene, Carl Jansson
 */
public class TaskModel {

    private String name;
    private Time time;
    private Date date;
    private Location location;
    private String description;

    /**
     * Create a new task.
     * @param name name of the task.
     * @param date date of the task.
     */
    public TaskModel(String name, Date date) {
        new TaskModel(name, null, date, null, null);
    }

    /**
     * Create a new task.
     * @param name name of the task.
     * @param time time of the task.
     * @param date date of the task.
     * @param location location of the task.
     * @param description a more detailed description of the task.
     */
    public TaskModel(String name, Time time, Date date, Location location, String description) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    /**
     * Create a new task.
     * @param name name of the task.
     * @param time time of the task.
     * @param date date of the task.
     * @param description a more detailed description of the task.
     */
    public TaskModel(String name, Time time, Date date, String description) {
        new TaskModel(name, time, date, null, description);
    }


    /**
     * Get the name of the task.
     * @return the name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the time of the task.
     * @return the time of the task.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Get the date of the task.
     * @return the date of the task.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the location of the task.
     * @return the location of the task.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get a more detailed description of the task.
     * @return the description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the name of the task.
     * @param name the new name of the task.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the time of the task.
     * @param time the new time of the task.
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Sets the date of the task.
     * @param date the new date of the task.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the location of the task.
     * @param location the new location of the task.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Sets the description of the task.
     * @param description the new description of the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
