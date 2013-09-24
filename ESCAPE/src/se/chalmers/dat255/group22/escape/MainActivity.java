package se.chalmers.dat255.group22.escape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The main activity, to be launched when app is started.
 */
public class MainActivity extends FragmentActivity {

	private SectionsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
    private String[] fragmentTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        fragmentTitles = getResources().getStringArray(R.id.fragment_title);
		pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentTitles);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(pagerAdapter);

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
			case R.id.add_task :
				Intent intent = new Intent(this, NewTaskActivity.class);
				startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}
