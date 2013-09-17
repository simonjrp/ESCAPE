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
public class TopLevelListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    private List<String> _listHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listTask;
    private HashMap<String, List<String>> _listTaskData;


    public TopLevelListAdapter(Context context, List<String> listHeader,
                         HashMap<String, List<String>> listTask, HashMap<String, List<String>> listTaskData) {
        this._context = context;
        this._listHeader = listHeader; // today, tomorrow etc
        this._listTask = listTask; // task
        this._listTaskData = listTaskData; // taskdata

    }

    @Override
    public int getGroupCount() {
        return this._listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listTask.get(this._listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listTask.get(this._listHeader.get(groupPosition)).get(
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
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_task, null);
        }

        ExpandableListView expListChild = (ExpandableListView) convertView
                .findViewById(R.id.lblListTask);
        expListChild.setAdapter(new SecondLevelListAdapter(_context, _listHeader, _listTask, _listTaskData));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
