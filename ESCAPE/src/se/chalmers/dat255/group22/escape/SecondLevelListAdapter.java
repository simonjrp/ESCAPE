package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tholene on 9/17/13.
 */
public class SecondLevelListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    private HashMap<String, List<String>> _listTask; // header titles
    // child data in format of header title, child title
    private  HashMap<String, List<String>> _listTaskData;

    public SecondLevelListAdapter(Context context, List<String> listHeader,
                               HashMap<String, List<String>> listTask, HashMap<String, List<String>> listTaskData) {
        this._context = context;
        this._listTask= listTask; // task
        this._listTaskData = listTaskData; // taskdata

    }

    @Override
    public int getGroupCount() {
        return this._listTask.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listTaskData.get(this._listTask.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listTask.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listTaskData.get(this._listTask.get(groupPosition)).get(
                childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

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
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
