package se.chalmers.dat255.group22.escape.ListFragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import se.chalmers.dat255.group22.escape.ListObject;
import se.chalmers.dat255.group22.escape.R;

/**
 * Adapter for displaying ListObjects in an ordinary list
 *
 * @author Carl
 */
public class CustomListAdapter implements ListAdapter {

    // The context this adapter is used in
    private Context context;
    // header titles
    private List<ListObject> ourTaskList;

    public CustomListAdapter(Context context, List<ListObject> taskList) {
        this.context = context;
        this.ourTaskList = taskList;
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

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return ourTaskList.size();
    }

    @Override
    public Object getItem(int i) {
        return ourTaskList.get(i);
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ListObject listObject = (ListObject) getItem(i);

        final String childText = listObject.getName();

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_task, null);
        }

        final TextView childLabel = (TextView) view
                .findViewById(R.id.listTask);

        childLabel.setText(childText);

        CustomOnClickListener clickListener = new CustomOnClickListener(listObject, childLabel);
        childLabel.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return ourTaskList.isEmpty();
    }
}
