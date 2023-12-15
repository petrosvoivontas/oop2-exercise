package gr.hua.dit.oop2_ex.utils;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class ICSCalendarUtils {

	@NotNull
	public static Calendar createCalendar() {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//DIT HUA Event maker"));
		calendar.getProperties().add(new Version(new ParameterList(), Version.VALUE_2_0));
		calendar.getProperties().add(new CalScale(CalScale.VALUE_GREGORIAN));
		return calendar;
	}

	@NotNull
	public static Calendar initCalendar(String iCalPath) {
		File iCalFile = Paths.get(iCalPath).toFile();
		try {
			FileInputStream inputStream = new FileInputStream(iCalFile);
			CalendarBuilder calendarBuilder = new CalendarBuilder();
			return calendarBuilder.build(inputStream);
		} catch (FileNotFoundException e) {
			System.out.println("File in path " + iCalPath + " not found");
			System.exit(1);
			throw new RuntimeException(e);
		} catch (ParserException e) {
			System.out.println("File in path " + iCalPath + " does not contain a valid iCal calendar");
			System.exit(1);
			throw new RuntimeException(e);
		} catch (IOException e) {
			System.out.println("There was a problem with opening the file in path " + iCalPath);
			System.exit(1);
			throw new RuntimeException(e);
		}
	}

	@NotNull
	public static Calendar getOrCreateCalendar(String iCalPath) {
		File iCalFile = Paths.get(iCalPath).toFile();
		if (iCalFile.exists()) {
			return initCalendar(iCalPath);
		}
		return createCalendar();
	}
}
