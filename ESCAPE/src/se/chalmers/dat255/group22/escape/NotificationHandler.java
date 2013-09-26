package se.chalmers.dat255.group22.escape;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class NotificationHandler {
	
	public void createNotification(TimeAlarm alarm) {
		Date date = alarm.getDate();
		long timeInMillis = date.getTime();
		
		String timeStamp = "00 00 00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss");
		
		
	}

}
