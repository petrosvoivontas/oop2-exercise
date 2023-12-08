package gr.hua.dit.oop2_ex.parser;

import gr.hua.dit.oop2_ex.model.Meeting;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MeetingsParserImpl implements MeetingsParser {

	@Override
	public Meeting transformToMeeting(VEvent vEvent) {
		final String title = vEvent.getSummary().getValue();

		Description description = vEvent.getDescription();
		String descriptionText = null;
		if (description != null && !description.getValue().isEmpty()) {
			descriptionText = description.getValue();
		}

		final DtStart startDate = vEvent.getStartDate();
		final LocalDateTime startDateTime = startDate.getDate()
			.toInstant()
			.atZone(startDate.getTimeZone().toZoneId())
			.toLocalDateTime();

		final DtEnd endDate = vEvent.getEndDate();
		final LocalDateTime endDateTime = endDate.getDate()
			.toInstant()
			.atZone(endDate.getTimeZone().toZoneId())
			.toLocalDateTime();
		long duration = Duration.between(startDateTime, endDateTime).toMillis();

		return new Meeting(
			title,
			descriptionText,
			LocalDate.from(startDateTime),
			LocalTime.from(startDateTime),
			duration
		);
	}

	private Calendar toCalendar(LocalDateTime localDateTime) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, localDateTime.getYear());
		calendar.set(Calendar.MONTH, localDateTime.getMonthValue() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, localDateTime.getDayOfMonth());
		calendar.set(Calendar.HOUR_OF_DAY, localDateTime.getHour());
		calendar.set(Calendar.MINUTE, localDateTime.getMinute());
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	@Override
	public VEvent transformToVEvent(Meeting meeting) {
		// list that will hold the properties of the new event
		PropertyList<Property> propertyList = new PropertyList<>();

		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timeZone = registry.getTimeZone("Europe/Athens");

		LocalDateTime startDateTime = meeting.getStartDateTime();
		LocalDateTime endDateTime = meeting.getEndDateTime();

		// create the DtStart property
		Date startDate = toCalendar(startDateTime).getTime();
		DtStart dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(startDate));
		propertyList.add(dtStart);

		// create the DtEnd property
		Date endDate = toCalendar(endDateTime).getTime();
		DtEnd dtEnd = new DtEnd(new net.fortuna.ical4j.model.DateTime(endDate));
		propertyList.add(dtEnd);

		// add the Summary (title) property
		propertyList.add(new Summary(meeting.getTitle()));

		// add the Description property (if there is a description)
		String description = meeting.getDescription();
		if (description != null) {
			propertyList.add(new Description(description));
		}

		VTimeZone vTimeZone = timeZone.getVTimeZone();
		propertyList.add(vTimeZone.getTimeZoneId());

		// create and return a new VEvent object
		return new VEvent.Factory().createComponent(propertyList);
	}
}
