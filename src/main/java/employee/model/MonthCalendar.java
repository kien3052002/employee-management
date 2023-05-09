package employee.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthCalendar {
	public static List<String> daysOfMonth(int month) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.WEEK_OF_MONTH, 1);
	    cal.set(Calendar.DAY_OF_WEEK, 1);
	    Date minDay = cal.getTime();
	    LocalDate min = minDay.toInstant().atZone(ZoneId.systemDefault())
	        .toLocalDate();
	    
	    // ----------------------------------

	    Calendar cal2 = Calendar.getInstance();
	    int maxWeek = cal2.getActualMaximum(Calendar.WEEK_OF_MONTH);
	    cal2.set(Calendar.MONTH, month);
	    cal2.set(Calendar.WEEK_OF_MONTH, maxWeek);
	    cal2.set(Calendar.DAY_OF_WEEK, 7);
	    Date maxDay = cal2.getTime();
	    LocalDate max = maxDay.toInstant().atZone(ZoneId.systemDefault())
	        .toLocalDate();

	    //-----------------------------------
	    
	    List<String> days = new ArrayList<>(); 
	    while (!min.isAfter(max)) {
	      days.add(min.toString());
	      min = min.plusDays(1);
	    }
	    return days;
	}
	
	public static int currMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH)+1;
	}
}
