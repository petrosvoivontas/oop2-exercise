package gr.hua.dit.oop2_ex.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Meeting extends Event {

	private final long duration;

	public Meeting(
		@NotNull String title,
		@Nullable String description,
		@NotNull LocalDate date,
		@NotNull LocalTime time,
		long duration
	) {
		super(title, description, date, time);

		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	@NotNull
	public LocalDateTime getStartDateTime() {
		LocalTime time = getTime();
		if (time == null) {
			throw new IllegalStateException("time for a Meeting cannot be null");
		}
		return LocalDateTime.of(getDate(), getTime());
	}

	@NotNull
	public LocalDateTime getEndDateTime() {
		LocalDateTime startDateTime = getStartDateTime();
		return startDateTime.plus(duration, ChronoUnit.MILLIS);
	}
}
