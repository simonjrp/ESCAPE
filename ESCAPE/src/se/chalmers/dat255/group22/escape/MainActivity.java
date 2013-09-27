package se.chalmers.dat255.group22.escape;

import java.sql.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The main activity, to be launched when app is started.
 */
public class MainActivity extends FragmentActivity {

	private TabsPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(pagerAdapter);

		// switch to the events list directly after startup
		viewPager.setCurrentItem(TabsPagerAdapter.EVENTS_FRAGMENT);

		NotificationHandler nf = new NotificationHandler(this);
		DBHandler dbh = new DBHandler(this);

		ListObject lo = new ListObject(0, "Köp mjölk");
		lo.setComment("Mjölk är väldigt gott men du måste ändå köpa lättmjölk, annars blir du tjock");

		TimeAlarm alarm = new TimeAlarm(0, new Date(
				System.currentTimeMillis() + 1000));

		long idAlarm = dbh.addTimeAlarm(alarm);

		long idLo = dbh.addListObject(lo);

		long idTime = dbh.addTime(new Time(0, new Date(System
				.currentTimeMillis() + 100000), new Date(System
				.currentTimeMillis() + 1000000)));

		dbh.addListObjectWithTimeAlarm(dbh.getListObject(idLo),
				dbh.getTimeAlarm(idAlarm));
		
		dbh.addListObjectsWithTime(dbh.getListObject(idLo), dbh.getTime(idTime));

		nf.addReminderNotification(dbh.getListObject(idLo));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.add_task:
			Intent intent = new Intent(this, NewTaskActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}
