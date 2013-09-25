package se.chalmers.dat255.group22.escape;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

public class CustomExpandableListView extends ExpandableListView {

	float historicX = Float.NaN;
	float historicY = Float.NaN;
	
	
	
	public CustomExpandableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			historicX = e.getX();
			historicY = e.getY();
			break;
		}
		
		return true;
	}
	
	
}
