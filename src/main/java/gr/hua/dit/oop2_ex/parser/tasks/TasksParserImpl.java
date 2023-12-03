package gr.hua.dit.oop2_ex.parser.tasks;

import gr.hua.dit.oop2_ex.model.Task;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Due;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TasksParserImpl implements TasksParser {

	@Override
	public Task transformToTask(VToDo todoComponent) {
		final String title = todoComponent.getSummary().getValue();

		Description description = todoComponent.getDescription();
		String descriptionText = null;
		if (description != null && !description.getValue().isEmpty()) {
			descriptionText = description.getValue();
		}

		final Due dueDate = todoComponent.getDue();
		final LocalDateTime dueDateTime = dueDate.getDate()
			.toInstant()
			.atZone(dueDate.getTimeZone().toZoneId())
			.toLocalDateTime();

		final boolean completed = todoComponent.getDateCompleted() != null;

		return new Task(
			title,
			descriptionText,
			LocalDate.from(dueDateTime),
			LocalTime.from(dueDateTime),
			completed
		);
	}
}
