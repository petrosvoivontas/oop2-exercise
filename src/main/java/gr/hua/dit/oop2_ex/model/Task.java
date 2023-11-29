package gr.hua.dit.oop2_ex.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Task extends Event {

	private final boolean completed;

	public Task(
		@NotNull String title,
		@Nullable String description,
		@NotNull LocalDate date,
		@Nullable LocalTime time,
		boolean completed
	) {
		super(title, description, date, time);
		this.completed = completed;
	}

	public boolean isCompleted() {
		return completed;
	}

	@NotNull
	public LocalDateTime getDueDate() {
		LocalTime time = getTime();
		if (time == null) {
			throw new IllegalStateException("time for a Meeting cannot be null");
		}
		return LocalDateTime.of(getDate(), getTime());
	}
}
