package se.chalmers.dat255.group22.escape.adapters;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.group22.escape.MainActivity;
import se.chalmers.dat255.group22.escape.R;
import se.chalmers.dat255.group22.escape.database.DBHandler;
import se.chalmers.dat255.group22.escape.objects.IBlockObject;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * A list adapter for the BlockListFragment. More or less a simpler variant of
 * CustomListAdapter.
 * 
 * @author anno
 */
public class BlockListAdapter implements ListAdapter {

	// The context this adapter is used in
	private Context context;
	// The blockobjects in the list
	private List<IBlockObject> taskList;
	// Array keeping track of changes in the list
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

	/**
	 * @param context
	 *            The context (activity) this adapter is used in
	 */
	public BlockListAdapter(Context context) {
		this.context = context;
		initialize();
	}

	/**
	 * Initialize list
	 */
	private void initialize() {
		taskList = new ArrayList<IBlockObject>();
	}

	/**
	 * Read from database and add block objects to the list
	 */
	public void reInit() {
		// Fetch tasks from database
		DBHandler dbHandler = new DBHandler(context);
		List<IBlockObject> databaseList = dbHandler.getAllBlocks();

		for (IBlockObject block : databaseList) {
			addBlockObject(block);
		}
		updateButtons();
		dbHandler.close();
	}

	/**
	 * Update the edit/remove button
	 */
	protected void updateButtons() {
		try {
			ImageButton deleteButton = (ImageButton) ((MainActivity) context)
					.findViewById(R.id.deleteButton);
			if (deleteButton != null) {
				deleteButton.setVisibility(View.INVISIBLE);
				deleteButton.clearAnimation();
			}
		} catch (RuntimeException e) {
			// Do nothing
		}
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int i) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver) {
		observers.add(dataSetObserver);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
		observers.remove(dataSetObserver);
	}

	/**
	 * Call this to notify that something has changed. Makes the view update!
	 */
	public void notifyDataSetChanged() {
		for (DataSetObserver observer : observers)
			observer.onChanged();
	}

	@Override
	public int getCount() {
		return taskList.size();
	}

	@Override
	public Object getItem(int i) {
		return taskList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int childPosition, View convertView, ViewGroup parent) {
		updateButtons();
		final IBlockObject blockObject = (IBlockObject) getItem(childPosition);

		final String childText = blockObject.getName();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_task, null);
		}

		final TextView childLabel = (TextView) convertView
				.findViewById(R.id.listTask);

		final ImageButton editButton = (ImageButton) convertView
				.findViewById(R.id.editButton);

		final ImageButton deleteButton = (ImageButton) convertView
				.findViewById(R.id.deleteButton);

		editButton.setVisibility(View.GONE);
		deleteButton.setVisibility(View.INVISIBLE);

		// Removes the block from the database and the list
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DBHandler dbh = new DBHandler(context);
				dbh.deleteBlock(blockObject);
				removeBlockObject(blockObject);
				dbh.close();
			}

		});

		childLabel.setText(childText);

		TextView childData = (TextView) convertView.findViewById(R.id.taskData);
		childData.setText("Hours: \t\t\t\t\t" + blockObject.getHours()
				+ " h\nMinutes/session: \t" + blockObject.getSessionMinutes()
				+ " min");

		// We don't want the data to show yet...
		childData.setVisibility(View.INVISIBLE);
		childData.setHeight(0);

		// These listeners handle the clicks.
		// The normal click removes the delete button or shows the data for the
		// block and the long click shows the delete button
		convertView.setOnClickListener(new OnClickListener() {

			Boolean isExpanded = false;

			@Override
			public void onClick(View v) {
				if (deleteButton.isShown()) {
					deleteButton.setVisibility(View.INVISIBLE);
					deleteButton.clearAnimation();
				} else {
					isExpanded = !isExpanded;
				}

				TextView childData = (TextView) v.findViewById(R.id.taskData);
				if (isExpanded) {
					if (!deleteButton.isShown()) {

						childData.setVisibility(View.VISIBLE);

						childData.setHeight(childData.getLineCount()
								* childData.getLineHeight() + 5);
					}

				} else {
					// If the view is only expanded, hide it again
					childData.setVisibility(View.INVISIBLE);
					childData.setHeight(0);
					childLabel.setPaintFlags(1);
				}
			}

		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			private TranslateAnimation slide;

			public boolean onLongClick(View v) {
				if (!deleteButton.isShown()) {
					deleteButton.setVisibility(View.VISIBLE);

					// ...instantiate the animation...
					slide = new TranslateAnimation(300, 0, 0, 0);
					slide.setDuration(250);
					slide.setFillAfter(true);
					slide.setFillEnabled(true);

					if (editButton.getAnimation() == null) {
						deleteButton.startAnimation(slide);
					}
				}
				// Send true so that the click event is consumed
				return true;
			}
		});

		convertView.setVisibility(View.VISIBLE);
		return convertView;
	}

	@Override
	public int getItemViewType(int i) {
		return i;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return taskList.isEmpty();
	}

	/**
	 * Add a new task for the displayed list if the task is not already in the
	 * list.
	 * 
	 * @param blockObject
	 *            the IBlockObject to add
	 */
	public void addBlockObject(IBlockObject blockObject) {
		if (!taskList.contains(blockObject)) {
			taskList.add(blockObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Remove a task from the displayed list.
	 * 
	 * @param blockObject
	 *            the IBlockOBject to remove
	 */
	public void removeBlockObject(IBlockObject blockObject) {
		if (taskList.contains(blockObject)) {
			taskList.remove(blockObject);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * Get a block object from the displayed list
	 * 
	 * @param position
	 *            position of object to return
	 * @return the IBlockObject at the position in the list
	 */
	public IBlockObject getBlockObject(int position) {
		if (0 <= position && position < taskList.size()) {
			return taskList.get(position);
		}
		return null;
	}
}