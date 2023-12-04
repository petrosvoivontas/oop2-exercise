package gr.hua.dit.oop2_ex.usecase;

import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.parser.MeetingsParser;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.function.Predicate;

public class MeetingsUseCaseImpl implements MeetingsUseCase {

	private final LocalDateTime now;
	private final Calendar calendar;
	private final MeetingsParser meetingsParser;

	public MeetingsUseCaseImpl(LocalDateTime now, Calendar calendar, MeetingsParser meetingsParser) {
		this.now = now;
		this.calendar = calendar;
		this.meetingsParser = meetingsParser;
	}

	private Predicate<Meeting> getFilterPredicate(MeetingsFilter filter) {
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
		Predicate<Meeting> filterPredicate = getFilterPredicate(filter);
		return eventComponents.stream()
			.map(component -> (VEvent) component)
			.map(meetingsParser::transformToMeeting)
			.filter(filterPredicate)
			.toList();
	}

	@Override
	public void saveMeeting(Meeting meeting) {

	}
}
