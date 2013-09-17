package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * @author tholene
 */
public class MyListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> __listTask;
    private HashMap<String, List<String>> _listTaskData;

	public MyListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData, HashMap<String, List<String>> listGrandChildData) {
		this._context = context;
		this._listHeader = listDataHeader; //today, tomorrow etc
		this.__listTask = listChildData; //task
        this._listTaskData = listGrandChildData; //taskdata

	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.__listTask.get(this._listHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_taskdata, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListTaskData);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.__listTask.get(this._listHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListTask);
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