package gr.hua.dit.oop2_ex.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Event {

	@NotNull
	private final String title;

	@Nullable
	private final String description;

	@NotNull
	private final LocalDate date;

	@Nullable
	private final LocalTime time;

	public Event(
		@NotNull String title,
		@Nullable String description,
		@NotNull LocalDate date,
		@Nullable LocalTime time
	) {
		this.title = title;
		this.description = description;
		this.date = date;
		this.time = time;
	}

	@NotNull
	public String getTitle() {
		return title;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	@NotNull
	public LocalDate getDate() {
		return date;
	}

	@Nullable
	public LocalTime getTime() {
		return time;
	}
}
