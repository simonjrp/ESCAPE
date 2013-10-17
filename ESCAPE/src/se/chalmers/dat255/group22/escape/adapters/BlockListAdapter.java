package se.chalmers.dat255.group22.escape.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.widget.ArrayAdapter;

public class BlockListAdapter extends ArrayAdapter<String> {

	
	private final DataSetObservable dataSetObservable = new DataSetObservable();
	HashMap<String, Integer> idMap = new HashMap<String, Integer>();
	

	public BlockListAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.size(); ++i) {
			idMap.put(objects.get(i), i);
		}
	}

	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return idMap.get(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		this.dataSetObservable.notifyInvalidated();
	}

	/**
	 * Call this to notify that something has changed. Makes the view update!
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged() {
		this.dataSetObservable.notifyChanged();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		this.dataSetObservable.registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		this.dataSetObservable.unregisterObserver(observer);
	}

}