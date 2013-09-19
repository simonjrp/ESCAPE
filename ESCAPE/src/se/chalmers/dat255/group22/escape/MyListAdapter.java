package se.chalmers.dat255.group22.escape;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * A ListAdapter that works with TaskModel data
 * 
 * @author tholene, Carl
 */
public class MyListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	// header titles
	private List<String> _listDataHeader;
	// child data in format of header title, data container (TaskModel)
	private HashMap<String, List<TaskModel>> _listDataChild;

	/**
	 * Constructor for MyListAdapter
	 * 
	 * @param context
	 *            The activity
	 * @param listDataHeader
	 *            header data
	 * @param listChildData
	 *            the taskmodels to add
	 */
	public MyListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<TaskModel>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	/**
	 * Returns the child object
	 * 
	 * @param groupPosition
	 * @param childPosititon
	 * @return
	 */
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * returns the child view
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @param isLastChild
	 * @param convertView
	 * @param parent
	 * @return the child view to display
	 */
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = ((TaskModel) getChild(groupPosition,
				childPosition)).getName();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);
		return convertView;
	}

	/**
	 * Returns the number of children
	 * 
	 * @param groupPosition
	 * @return the number of children
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	/**
	 * Returns an entire group
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	/**
	 * Returns the number of groups
	 * 
	 * @return number of groups
	 */
	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Returns the view to use for heads
	 * 
	 * @param groupPosition
	 * @param isExpanded
	 * @param convertView
	 * @param parent
	 * @return head view
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
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
