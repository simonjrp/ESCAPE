package se.chalmers.dat255.group22.escape;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
		viewPager.setCurrentItem(1);

		// switch to the events list directly after startup
		viewPager.setCurrentItem(TabsPagerAdapter.EVENTS_FRAGMENT);
		// saves the state between the 3 different fragments
		viewPager.setOffscreenPageLimit(3);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
//	  public void createNotification(View view) {
//		    // Prepare intent which is triggered if the
//		    // notification is selected
//		    Intent intent = new Intent(this, MainActivity.class);
//		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//		    // Build notification
//		    // Actions are just fake
//		    Notification noti = new Notification.Builder(this)
//		        .setContentTitle("New mail from " + "test@gmail.com")
//		        .setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
//		        .setContentIntent(pIntent)
//		        .addAction(R.drawable.ic_launcher, "Call", pIntent)
//		        .addAction(R.drawable.ic_launcher, "More", pIntent)
//		        .addAction(R.drawable.ic_launcher, "And more", pIntent).build();
//		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		    // Hide the notification after its selected
//		    noti.flags |= Notification.FLAG_AUTO_CANCEL;
//
//		    notificationManager.notify(0, noti);
//
//		  }

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
