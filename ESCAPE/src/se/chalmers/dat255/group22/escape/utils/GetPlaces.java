package se.chalmers.dat255.group22.escape.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.dat255.group22.escape.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * @author tholene
 */
public class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {
	private AutoCompleteTextView autoCompleteTextView;
	private ArrayAdapter<String> adapter;
	private Context context;

	public GetPlaces(AutoCompleteTextView autoCompleteTextView,
			ArrayAdapter<String> adapter, Context context) {
		this.autoCompleteTextView = autoCompleteTextView;
		this.adapter = adapter;
		this.context = context;
	}

	@Override
	// three dots is java for an array of strings
	protected ArrayList<String> doInBackground(String... args) {

		Log.d("gottaGo", "doInBackground");

		ArrayList<String> predictionsArr = new ArrayList<String>();

		String fullArgs = "";
		for (int i = 0; i < args.length; i++) {
			fullArgs = args[i] + " ";
		}

		try {
			// Browser key for Google API

			String key = "key=" + context.getString(R.string.API_key);
			String input = "";

			try {
				input = "input=" + URLEncoder.encode(fullArgs, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=true";

			// Language
			String language = "language=" + context.getString(R.string.country_short);

            // Country
			String country = "components=country:" + context.getString(R.string.country_short);

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&"
					+ language + "&" + country + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			URL googlePlaces = new URL(url);
			URLConnection tc = googlePlaces.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					tc.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();
			// take Google's legible JSON and turn it into one big string.
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			// turn that string into a JSON object
			JSONObject predictions = new JSONObject(sb.toString());

			// now get the JSON array that's inside that object
			JSONArray allPredictions = new JSONArray(
					predictions.getString("predictions"));

			for (int i = 0; i < allPredictions.length(); i++) {

				// Parse the JSONArray to exlude the country
				JSONObject thisPrediction = (JSONObject) allPredictions.get(i);

				JSONArray allTerms = thisPrediction.getJSONArray("terms");

				String result = "";

				for (int j = 0; j < allTerms.length(); j++) {
					String thisValue = allTerms.getJSONObject(j).getString(
							"value");
					if (!thisValue.equals(context.getString(R.string.country))) {
						if (j == 0)
							result = thisValue;
						else
							result = result + ", " + thisValue;
					}
				}

				// add each entry to our array
				predictionsArr.add(result);
			}
		} catch (IOException e) {

			Log.e("ESCAPE", "GetPlaces : doInBackground", e);

		} catch (JSONException e) {

			Log.e("ESCAPE", "GetPlaces : doInBackground", e);

		}

		return predictionsArr;

	}

	// then our post

	@Override
	protected void onPostExecute(ArrayList<String> result) {

		Log.d("ESCAPE", "onPostExecute : " + result.size());
		// update the adapter
		adapter = new ArrayAdapter<String>(context, R.layout.location_item);
		adapter.setNotifyOnChange(true);
		// attach the adapter to textview
		autoCompleteTextView.setAdapter(adapter);

		for (String string : result) {

			Log.d("ESCAPE", "onPostExecute : result = " + string);

			adapter.add(string);
			adapter.notifyDataSetChanged();

		}

		Log.d("ESCAPE",
				"onPostExecute : autoCompleteAdapter" + adapter.getCount());

	}

}
