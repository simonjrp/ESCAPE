package se.chalmers.dat255.group22.escape;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NewTaskActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().add(R.id.container_new_task, new TaskDetailsFragment()).commit();
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //Make home button in actionbar work like pressing on backbutton
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAddReminder(View v) {
    }
}
