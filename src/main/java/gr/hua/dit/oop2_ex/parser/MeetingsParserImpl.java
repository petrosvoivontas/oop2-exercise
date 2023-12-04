package gr.hua.dit.oop2_ex.parser;

import gr.hua.dit.oop2_ex.model.Meeting;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
}
