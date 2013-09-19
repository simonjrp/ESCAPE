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

	private Context context;
	// header titles
	private List<String> headerList;
	// child data in format of header title, data container (TaskModel)
	private HashMap<String, List<TaskModel>> taskDataMap;

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
		this.context = context;
		this.headerList = listDataHeader; // today, tomorrow etc
		this.taskDataMap = listChildData; // task

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
		return this.taskDataMap.get(this.headerList.get(groupPosition)).get(
				childPosititon);
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
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		final TaskModel taskModel = (TaskModel) getChild(groupPosition,
				childPosition);

		final TextView childLabel = (TextView) convertView
				.findViewById(R.id.listTask);

		childLabel.setText(childText);
		childLabel.setOnClickListener(new View.OnClickListener() {
            boolean alreadyExpanded = false;
			@Override
			public void onClick(View v) {
                alreadyExpanded = !alreadyExpanded;
                if(alreadyExpanded) {
                    childLabel.setText(taskModel.getName() + "\n\n"
                            + taskModel.getTime() + "\n"
                            + taskModel.getDate() + "\n"
                            + taskModel.getLocation().getProvider() + "\n"
                            + taskModel.getDescription());
                } else {
                    childLabel.setText(taskModel.getName());
                }
			}
		});
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
		return this.taskDataMap.get(this.headerList.get(groupPosition)).size();

	}

	/**
	 * Returns an entire group
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return this.headerList.get(groupPosition);
	}

	/**
	 * Returns the number of groups
	 * 
	 * @return number of groups
	 */
	@Override
	public int getGroupCount() {
		return this.headerList.size();
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
