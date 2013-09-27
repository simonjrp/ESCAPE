package se.chalmers.dat255.group22.escape;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import se.chalmers.dat255.group22.escape.objects.ListObject;
import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * An ExpandableListAdapter that makes use of a
 * {@link se.chalmers.dat255.group22.escape.objects.ListObject}.<br>
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
	// child data in format of header title, data container (TaskModel)
	private HashMap<String, List<ListObject>> taskDataMap;

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
		this.taskDataMap = listChildData; // task

	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.taskDataMap.get(this.headerList.get(groupPosition)).get(
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
		final Date childTime = ((ListObject) getChild(groupPosition,
				childPosition)).getTime().getStartDate();
		final String childTimeText = DateFormat.format("HH:mm", childTime).toString();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		final ListObject listObject = (ListObject) getChild(groupPosition,
				childPosition);

		final TextView childLabel = (TextView) convertView
				.findViewById(R.id.listTask);
		
		final TextView childTimeView = (TextView) convertView.findViewById(R.id.startTimeTask);

		final ImageButton editButton = (ImageButton) convertView
				.findViewById(R.id.editButton);
		
		final ImageButton deleteButton = (ImageButton) convertView
				.findViewById(R.id.deleteButton);

		editButton.setVisibility(View.INVISIBLE);
		deleteButton.setVisibility(View.INVISIBLE);
		
		
		editButton.setX(editButton.getX() + 300);
		deleteButton.setX(deleteButton.getX() + 300);
		
		childLabel.setText(childText);
		childTimeView.setText(childTimeText);
		childLabel.setOnClickListener(new OnClickListener() {
			boolean alreadyExpanded;

			@Override
			public void onClick(View v) {
				alreadyExpanded = !alreadyExpanded;
				if (alreadyExpanded) {
					// TODO temporary ugly fix for fist release
					if (listObject.getComment() == null) {
						childLabel.setText(listObject.getName() + "\n"
								+ listObject.toString());
					} else {
						childLabel.setText(listObject.getName() + "\n"
								+ listObject.getComment());

					}
				} else {
					childLabel.setText(listObject.getName());
				}
			}
		});
		// TODO We add two listeners since it wont work on one if the other is
		// added to
		// Adding touchlisteners
		childLabel.setOnTouchListener(new OptionTouchListener(context,
				convertView));
		// convertView.setOnTouchListener(new OptionTouchListener(context,
		// convertView));

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.taskDataMap.get(this.headerList.get(groupPosition)).size();

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
