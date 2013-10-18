package se.chalmers.dat255.group22.escape.test;
import static org.junit.Assert.*;
import se.chalmers.dat255.group22.escape.utils.CheckDateUtils;
import java.sql.Date;
import java.util.Calendar;
import org.junit.Test;

/**
 * test for utils class with methods related to dates
 * 
 * @author Carl
 */
public class TestCheckDateUtils {
	
	private long DAYINMILLIS = 100*60*60*24;
	private Date aWeekAgo;
	private Date yesterday;
	private Date today;
	private Date tomorrow;
	private Date inTwoDays;
	private Date firstDayInWeek;
	private Date lastDayInWeek;
	private Date inAWeek;
	
	protected void setUp() throws Exception {
		
		Calendar tmpDate = Calendar.getInstance();
		
		today = new Date(tmpDate.getTimeInMillis());
		aWeekAgo = new Date(tmpDate.getTimeInMillis()-7*DAYINMILLIS);
		yesterday = new Date(tmpDate.getTimeInMillis()-DAYINMILLIS);
		tomorrow = new Date(tmpDate.getTimeInMillis()+DAYINMILLIS);
		inTwoDays = new Date(tmpDate.getTimeInMillis()+2*DAYINMILLIS);
		inAWeek = new Date(tmpDate.getTimeInMillis()+7*DAYINMILLIS);
		
		int dayInWeek = tmpDate.get(Calendar.DAY_OF_WEEK);
		switch (dayInWeek) {
		case Calendar.MONDAY:
			firstDayInWeek = today;
			lastDayInWeek = new Date(tmpDate.getTimeInMillis()+6*DAYINMILLIS);
			break;
		case Calendar.TUESDAY:
			firstDayInWeek = yesterday;
			lastDayInWeek = new Date(tmpDate.getTimeInMillis()+5*DAYINMILLIS);
		break;
		case Calendar.WEDNESDAY:
			firstDayInWeek = new Date(tmpDate.getTimeInMillis()-2*DAYINMILLIS);
			lastDayInWeek = new Date(tmpDate.getTimeInMillis()+4*DAYINMILLIS);
			break;
		case Calendar.THURSDAY:
			firstDayInWeek = new Date(tmpDate.getTimeInMillis()-3*DAYINMILLIS);
			lastDayInWeek = new Date(tmpDate.getTimeInMillis()+3*DAYINMILLIS);
			break;
		case Calendar.FRIDAY:
			firstDayInWeek = new Date(tmpDate.getTimeInMillis()-4*DAYINMILLIS);
			lastDayInWeek = new Date(tmpDate.getTimeInMillis()+2*DAYINMILLIS);
			break;
		case Calendar.SATURDAY:
			firstDayInWeek = new Date(tmpDate.getTimeInMillis()-5*DAYINMILLIS);
			lastDayInWeek = tomorrow;
			break;
		case Calendar.SUNDAY:
			firstDayInWeek = new Date(tmpDate.getTimeInMillis()-6*DAYINMILLIS);
			lastDayInWeek = today;
			break;
		}
	}
	
	@Test
	public void testIsToday() {
		// yesterday should be true!
		assertEquals(true, CheckDateUtils.isToday(yesterday));
		assertEquals(true, CheckDateUtils.isToday(today));
		assertEquals(false, CheckDateUtils.isToday(tomorrow));
		assertEquals(false, CheckDateUtils.isToday(inTwoDays));
		assertEquals(false, CheckDateUtils.isToday(inAWeek));
	}
	
	@Test
	public void testIsTomorrow() {
		assertEquals(false, CheckDateUtils.isTomorrow(yesterday));
		assertEquals(false, CheckDateUtils.isTomorrow(today));
		assertEquals(true, CheckDateUtils.isTomorrow(tomorrow));
		assertEquals(false, CheckDateUtils.isTomorrow(inTwoDays));
		assertEquals(false, CheckDateUtils.isTomorrow(inAWeek));
	}
	
	@Test
	public void testIsThisWeek() {
		assertEquals(false, CheckDateUtils.isThisWeek(aWeekAgo));
		assertEquals(true, CheckDateUtils.isThisWeek(firstDayInWeek));
		assertEquals(true, CheckDateUtils.isThisWeek(today));
		assertEquals(true, CheckDateUtils.isThisWeek(lastDayInWeek));
		assertEquals(false, CheckDateUtils.isThisWeek(inAWeek));
	}
	
	@Test
	public void testDateHasPassed() {
		assertEquals(true, CheckDateUtils.dateHasPassed(aWeekAgo));
		assertEquals(true, CheckDateUtils.dateHasPassed(yesterday));
		assertEquals(false, CheckDateUtils.dateHasPassed(today));
		assertEquals(false, CheckDateUtils.dateHasPassed(lastDayInWeek));
		assertEquals(false, CheckDateUtils.dateHasPassed(inAWeek));
	}

}
