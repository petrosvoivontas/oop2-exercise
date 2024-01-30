package gr.hua.dit.oop2_ex.repo;

import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.parser.MeetingsParser;
import gr.hua.dit.oop2_ex.usecase.MeetingsFilter;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MeetingsRepositoryImpl implements MeetingsRepository {

	private final Calendar calendar;
	private final MeetingsParser meetingsParser;

	public MeetingsRepositoryImpl(Calendar calendar, MeetingsParser meetingsParser) {
		this.calendar = calendar;
		this.meetingsParser = meetingsParser;
	}

	private Predicate<Meeting> getFilterPredicate(LocalDateTime now, MeetingsFilter filter) {
		LocalDateTime rangeStart = switch (filter) {
			case DAY, WEEK, MONTH -> now;
			case PAST_DAY -> now
				.withHour(0)
				.withMinute(0)
				.withSecond(0);
			case PAST_WEEK -> now
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
			case PAST_MONTH -> now
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.with(TemporalAdjusters.firstDayOfMonth());
		};
		LocalDateTime rangeEnd = switch (filter) {
			case DAY -> now
				.withHour(23)
				.withMinute(59)
				.withSecond(59);
			case WEEK -> now
				.withHour(23)
				.withMinute(59)
				.withSecond(59)
				.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
			case MONTH -> now
				.withHour(23)
				.withMinute(59)
				.withSecond(59)
				.with(TemporalAdjusters.lastDayOfMonth());
			case PAST_DAY, PAST_WEEK, PAST_MONTH -> now;
		};
		return meeting -> {
			LocalDateTime meetingStartDateTime = meeting.getStartDateTime();
			return meetingStartDateTime.isAfter(rangeStart) && meetingStartDateTime.isBefore(rangeEnd);
		};
	}

	@Override
	public List<Meeting> getMeetings(LocalDateTime now, MeetingsFilter filter) {
		if (calendar == null) {
			return null;
		}
		List<CalendarComponent> eventComponents = calendar.getComponents(Component.VEVENT);
		Predicate<Meeting> filterPredicate = getFilterPredicate(now, filter);
		return eventComponents.stream()
			.map(component -> (VEvent) component)
			.map(meetingsParser::transformToMeeting)
			.filter(filterPredicate)
			.toList();
	}

	@Override
	public void saveMeeting(Meeting meeting, File calendarFile) {
		VEvent newEvent = meetingsParser.transformToVEvent(meeting);

		calendar.getComponents().add(newEvent);
		storeCalendarToFile(calendarFile);
	}

	@Override
	public void deleteMeeting(Meeting meeting, File calendarFile) {
		calendar.getComponents(VEvent.VEVENT)
			.stream().filter(vEvent -> Objects.equals(((VEvent) vEvent).getSummary().getValue(), meeting.getTitle()))
			.findFirst().ifPresent(vEventToDelete -> calendar.getComponents().remove(vEventToDelete));
		storeCalendarToFile(calendarFile);
	}

	private void storeCalendarToFile(File calendarFile) {
		CalendarOutputter outputter = new CalendarOutputter();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(calendarFile);
			outputter.output(calendar, fileOutputStream);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
