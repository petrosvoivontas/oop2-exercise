package gr.hua.dit.oop2_ex.parser.tasks;

import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.utils.LocalDateTimeUtils;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Due;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Summary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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

	@Override
	public VToDo transformToVTodo(Task task) {
		PropertyList<Property> properties = new PropertyList<>();

		Summary title = new Summary(task.getTitle());
		properties.add(title);

		String description = task.getDescription();
		if (description != null) {
			Description descriptionProperty = new Description(description);
			properties.add(descriptionProperty);
		}

		Status status = new Status(Status.VALUE_IN_PROCESS);
		properties.add(status);

		Date dueDate = LocalDateTimeUtils.toCalendar(task.getDueDate()).getTime();
		Due due = new Due(new DateTime(dueDate));
		properties.add(due);

		return new VToDo.Factory().createComponent(properties);
	}
}
