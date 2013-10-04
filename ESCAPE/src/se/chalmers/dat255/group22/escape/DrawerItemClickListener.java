package se.chalmers.dat255.group22.escape;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements ListView.OnItemClickListener {

	private List<Fragment> fragmentList;
	private FragmentActivity activity;
	private String[] drawerTitles;
	private DrawerLayout drawerLayout;
	private ListView drawerList;

	public DrawerItemClickListener(FragmentActivity activity,String[] drawerTitles, DrawerLayout drawerLayout, ListView drawerList) {
		this.activity = activity;
		this.drawerTitles = drawerTitles;
		this.drawerLayout = drawerLayout;
		this.drawerList = drawerList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem(position);
	}

	private void selectItem(int position) {
		if (fragmentList == null) {
			fragmentList = new ArrayList<Fragment>();

			// Add all wanted fragments here
			fragmentList.add(new TasksEventsFragment());
			fragmentList.add(new PomodoroFragment());
		}

		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragmentList.get(position))
				.commit();
		
		drawerList.setItemChecked(position, true);
		setTitle(drawerTitles[position]);
		drawerLayout.closeDrawer(drawerList);

	}

	private void setTitle(String string) {
		activity.getActionBar().setTitle(string);
		
	}

}
