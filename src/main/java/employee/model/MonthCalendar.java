package employee.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthCalendar {
	public static List<String> daysOfMonth(int month) {

		LocalDate min = firstDay(month);

		LocalDate max = lastDay(month);

		List<String> days = new ArrayList<>();
		while (!min.isAfter(max)) {
			days.add(min.toString());
			min = min.plusDays(1);

		}
		return days;
	}

	public static LocalDate firstDay(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.WEEK_OF_MONTH, 1);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		Date minDay = cal.getTime();
		LocalDate min = minDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return min;
	}

	public static LocalDate lastDay(int month) {
		Calendar cal2 = Calendar.getInstance();
		int maxWeek = cal2.getActualMaximum(Calendar.WEEK_OF_MONTH);
		cal2.set(Calendar.MONTH, month);
		cal2.set(Calendar.WEEK_OF_MONTH, maxWeek);
		cal2.set(Calendar.DAY_OF_WEEK, 7);
		Date maxDay = cal2.getTime();
		LocalDate max = maxDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return max;
	}

	
	public static int dateIndexLimit() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		LocalDate min = firstDay(month);
		LocalDate max = lastDay(month);
		Date d =  Calendar.getInstance().getTime();
		LocalDate currDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int curr = -1;
		while (!min.isAfter(max)) {
			if (!min.isAfter(currDate)) {
				curr++;
			}
				
			min = min.plusDays(1);
		}
		
		return curr;
	}
	
	public static int currMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int currDate() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);
	}
}
