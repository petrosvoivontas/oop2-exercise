package gr.hua.dit.oop2_ex.utils;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

public class ICSCalendarUtils {

	public static Calendar createCalendar() {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//DIT HUA Event maker"));
		calendar.getProperties().add(new Version(Version.VALUE_2_0, Version.VALUE_2_0));
		calendar.getProperties().add(new CalScale(CalScale.VALUE_GREGORIAN));
		return calendar;
	}
}
