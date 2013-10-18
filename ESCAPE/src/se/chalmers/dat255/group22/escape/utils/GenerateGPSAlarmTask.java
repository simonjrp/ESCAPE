package se.chalmers.dat255.group22.escape.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import se.chalmers.dat255.group22.escape.NotificationHandler;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.GPSAlarm;

/**
 * This AsyncTask class is used to generate a GPSAlarm object from a textstring
 * representing an address. The GPSAlarm is stored to the database and a
 * notification is created. <b>NOTE: The ListObject has to be added to the
 * database before using this task. </b>
 * 
 * @author Simon Persson
 * 
 */
public class GenerateGPSAlarmTask extends AsyncTask<String, Void, GPSAlarm> {
	private Context context;
	private long listObjectId;
	private DBHandler dbHandler;

	/**
	 * Creates a new GenerateGpsAlarmTask.
	 * 
	 * @param context
	 *            The context to use when getting a DBHandler object.
	 * @param idOfListObject
	 *            The db ID of the ListObject that the created GPSAlarm should
	 *            be attached to.
	 */
	public GenerateGPSAlarmTask(Context context, long idOfListObject) {
		this.context = context;
		this.listObjectId = idOfListObject;
		dbHandler = new DBHandler(this.context);
	}

	@Override
	protected GPSAlarm doInBackground(String... input) {
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		double longitude = 0;
		double latitude = 0;

		try {
			List<Address> addressList = geocoder.getFromLocationName(input[0],
					1);
			try {

			} catch (IndexOutOfBoundsException e) {
				Log.d(Constants.APPTAG,
						"Couldn't find address from location text");
				e.printStackTrace();
			}
			Address address = addressList.get(0);
			longitude = address.getLongitude();
			latitude = address.getLatitude();
		} catch (IOException e) {
			Log.d(Constants.APPTAG, "Geocoder produced IOException");
			e.printStackTrace();
		}

		Log.d(Constants.APPTAG, "Coordinates for " + input[0] + " found.");

		return new GPSAlarm(0, input[0], longitude, latitude);
	}

	@Override
	protected void onPostExecute(GPSAlarm newGPSAlarm) {

		// When all operations are done, add GPS alarm and associate
		// it with the ListObject
		long tmpId = dbHandler.addGPSAlarm(newGPSAlarm);

		dbHandler.addListObjectWithGPSAlarm(
				dbHandler.getListObject(listObjectId),
				dbHandler.getGPSAlarm(tmpId));

		Log.d(Constants.APPTAG, "GPSAlarm added to db with coordinates "
				+ newGPSAlarm.getLatitude() + "," + newGPSAlarm.getLongitude()
				+ ".");

		NotificationHandler.getInstance().addPlaceReminder(listObjectId);
	}
}
