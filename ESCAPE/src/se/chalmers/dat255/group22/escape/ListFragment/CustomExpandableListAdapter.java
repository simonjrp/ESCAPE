package se.chalmers.dat255.group22.escape.ListFragment;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;



import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;

/**
 * An ExpandableListAdapter that makes use of a
 * {@link se.chalmers.dat255.group22.escape.ListObject}.<br>
 * It also simulates a three level expandable listview by giving each child its
 * own {@link android.view.View.OnClickListener}.
 * 
 * 
 * @author tholene, Carl
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	// header titles
	private List<String> headerList;
	// child data in format of header title, data container (ListObject)
	private HashMap<String, List<ListObject>> objectDataMap;

	/**
	 * Create a new custom list adapter.
	 * 
	 * @param context
	 *            The context to make use of
	 * @param listDataHeader
	 *            A list of header titles
	 * @param listChildData
	 *            A list of ListObjects to be associated with a header
	 */
	public CustomExpandableListAdapter(Context context,
			List<String> listDataHeader,
			HashMap<String, List<ListObject>> listChildData) {
		this.context = context;
		this.headerList = listDataHeader; // today, tomorrow etc
		this.objectDataMap = listChildData; // task/event

	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.objectDataMap.get(this.headerList.get(groupPosition)).get(
				childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// Get the name of the task to display for each task entry
		final String childText = ((ListObject) getChild(groupPosition,
				childPosition)).getName();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

        // Get the listObject
		ListObject listObject = (ListObject) getChild(groupPosition,
				childPosition);

        // Get a textview for the object
		TextView childLabel = (TextView) convertView
				.findViewById(R.id.listTask);

        // Get a textview for the object's data
        TextView childData = (TextView) convertView.findViewById(R.id.taskData);

        // We don't want the data to show yet...
        childData.setVisibility(View.INVISIBLE);
        childData.setHeight(0);

		childLabel.setText(childText);

        CustomOnClickListener clickListener = new CustomOnClickListener(listObject, childLabel, childData);
        childLabel.setOnClickListener(clickListener);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.objectDataMap.get(this.headerList.get(groupPosition)).size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.headerList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.headerList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		// Get the name of the header to display for each entry
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_header, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.listHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
