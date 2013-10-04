package se.chalmers.dat255.group22.escape.ListFragment;

import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.graphics.Paint;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author
 */
public class CheckBoxOnClickListener implements View.OnClickListener {

	private ListObject listObject;
	private View ve;
	private CustomExpandableListAdapter customExpandableListAdapter;

	/**
	 * Create a new CustomOnClickListener.
	 * 
	 * @param listObject
	 *            the
	 *            {@link se.chalmers.dat255.group22.escape.objects.ListObject}
	 *            that contains the data to be viewed.
	 * @param childLabel
	 *            the TextView that the listener will be added to.
	 */
	public CheckBoxOnClickListener(ListObject listObject, View ve,
			CustomExpandableListAdapter customExpandableListAdapter) {
		this.listObject = listObject;
		this.ve = ve;
		this.customExpandableListAdapter = customExpandableListAdapter;
	}

	@Override
	public void onClick(View v) {
		Toast toast = Toast.makeText(ve.getContext(), "hej hoj", Toast.LENGTH_SHORT);
		toast.show();
		
		DBHandler dbh = new DBHandler(ve.getContext());
		dbh.deleteListObject(listObject);
		customExpandableListAdapter.removeListObjectToday(listObject);
		customExpandableListAdapter.removeListObjectTomorrow(listObject);
		customExpandableListAdapter.removeListObjectThisWeek(listObject);
	}

}
