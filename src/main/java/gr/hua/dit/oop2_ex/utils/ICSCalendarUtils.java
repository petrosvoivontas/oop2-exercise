package gr.hua.dit.oop2_ex.utils;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class ICSCalendarUtils {

	public static Calendar createCalendar() {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//DIT HUA Event maker"));
		calendar.getProperties().add(new Version(Version.VALUE_2_0, Version.VALUE_2_0));
		calendar.getProperties().add(new CalScale(CalScale.VALUE_GREGORIAN));
		return calendar;
	}

	public static Calendar initCalendar(String iCalPath) {
		File iCalFile = Paths.get(iCalPath).toFile();
		try {
			FileInputStream inputStream = new FileInputStream(iCalFile);
			CalendarBuilder calendarBuilder = new CalendarBuilder();
			return calendarBuilder.build(inputStream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
