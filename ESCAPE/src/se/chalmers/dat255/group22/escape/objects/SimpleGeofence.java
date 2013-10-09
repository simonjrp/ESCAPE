package se.chalmers.dat255.group22.escape.objects;

/**
 * A class representing a GeofenceObject, defined with coordinates and radius.
 * 
 * @author Simon Persson
 * 
 */
public class SimpleGeofence {

	private final int id;
	private final double latitude;
	private final double longitude;
	private final float radius;
	private long expirationDuration;
	private int transitionType;

	/**
	 * Constructor for creating a new SimpleGeofence object.
	 * 
	 * @param id
	 *            The id of the Geofence (will be used as request id).
	 * @param latitude
	 *            The latitude of the Geofence's center.
	 * @param longitude
	 *            The longitude of the Geofence's center.
	 * @param radius
	 *            The radius of the Geofence circle.
	 * @param expirationDuration
	 *            The Geofence expiration duration.
	 * @param transitionType
	 *            The type of Geofence transition.
	 */
	public SimpleGeofence(int id, double latitude, double longitude,
			float radius, long expirationDuration, int transitionType) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.expirationDuration = expirationDuration;
		this.transitionType = transitionType;
	}

	/**
	 * Getter for the id of the Geofence.
	 * 
	 * @return Geofence's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter for the latitude of the Geofence.
	 * 
	 * @return Geofence's latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Getter for the longitude of the Geofence.
	 * 
	 * @return Geofence's longitude.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Getter for the radius of the Geofence.
	 * 
	 * @return Geofence's radius.
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Getter for the expiration duration of the Geofence.
	 * 
	 * @return Geofence's expiration duration.
	 */
	public long getExpirationDuration() {
		return expirationDuration;
	}

	/**
	 * Getter for the type of transition of the Geofence.
	 * 
	 * @return Geofence's transition type.
	 */
	public int getTransitionType() {
		return transitionType;
	}
}
