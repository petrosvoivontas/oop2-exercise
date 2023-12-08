package gr.hua.dit.oop2_ex.utils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LocalDateTimeUtils {

	public static Calendar toCalendar(LocalDateTime localDateTime) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, localDateTime.getYear());
		calendar.set(Calendar.MONTH, localDateTime.getMonthValue() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, localDateTime.getDayOfMonth());
		calendar.set(Calendar.HOUR_OF_DAY, localDateTime.getHour());
		calendar.set(Calendar.MINUTE, localDateTime.getMinute());
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
}
