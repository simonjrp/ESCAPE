package se.chalmers.dat255.group22.escape.ListFragment;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import se.chalmers.dat255.group22.escape.OptionTouchListener;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.ListObject;
import se.chalmers.dat255.group22.escape.objects.Time;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
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

	private static final String EMPTY_LIST = "EMPTY";
	private Context context;
	// header titles
	private List<String> headerList;
	// child data in format of header title, data container (ListObject)
	private HashMap<String, List<ListObject>> objectDataMap;

	// Keeps track of changes
	private final DataSetObservable dataSetObservable = new DataSetObservable();

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
		final Time childTime = ((ListObject) getChild(groupPosition,
				childPosition)).getTime();
		String childTimeText = "";
		if (childTime != null) {
			final Date childStartDate = childTime.getStartDate();
			childTimeText = DateFormat.format("HH:mm", childStartDate)
					.toString();
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		// Get the listObject
		final ListObject listObject = (ListObject) getChild(groupPosition,
				childPosition);
		if (listObject.getName().equals(EMPTY_LIST)) {
			LayoutInflater inflaInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflaInflater.inflate(R.layout.empty_list, null);
		} else {

			// Get a textview for the object
			final TextView childLabel = (TextView) convertView
					.findViewById(R.id.listTask);

			final TextView childTimeView = (TextView) convertView
					.findViewById(R.id.startTimeTask);

			final ImageButton editButton = (ImageButton) convertView
					.findViewById(R.id.editButton);

			final ImageButton deleteButton = (ImageButton) convertView
					.findViewById(R.id.deleteButton);

			editButton.setVisibility(View.INVISIBLE);
			deleteButton.setVisibility(View.INVISIBLE);

			// editButton.setX(convertView.getRight() + deleteButton.getWidth()
			// + 300);
			// deleteButton.setX(convertView.getRight() + 300);

			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DBHandler dbh = new DBHandler(context);
					dbh.deleteListObject(listObject);
				}

			});

			childLabel.setText(childText);
			childTimeView.setText(childTimeText.equals("") ? "10:00"
					: childTimeText);
			// Get a textview for the object's data
			TextView childData = (TextView) convertView
					.findViewById(R.id.taskData);

			// We don't want the data to show yet...
			childData.setVisibility(View.INVISIBLE);
			childData.setHeight(0);

			childLabel.setText(childText);

			CustomOnClickListener clickListener = new CustomOnClickListener(
					listObject, childLabel, childData);
			childLabel.setOnClickListener(clickListener);
			// TODO We add two listeners since it wont work on one if the other
			// is
			// added to
			// Adding touchlisteners
			childLabel.setOnTouchListener(new OptionTouchListener(context,
					convertView));
			// convertView.setOnTouchListener(new OptionTouchListener(context,
			// convertView));

		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.objectDataMap.get(this.headerList.get(groupPosition))
				.size();

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

	/**
	 * Call this to notify that something has changed. Makes the view update!
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetInvalidated() {
		this.getDataSetObservable().notifyInvalidated();
	}

	@Override
	public void notifyDataSetChanged() {
		this.getDataSetObservable().notifyChanged();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		this.getDataSetObservable().registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		this.getDataSetObservable().unregisterObserver(observer);
	}

	protected DataSetObservable getDataSetObservable() {
		return dataSetObservable;
	}
}
