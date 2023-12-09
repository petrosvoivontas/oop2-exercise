package gr.hua.dit.oop2_ex.usecase;

import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.parser.tasks.TasksParser;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VToDo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class TasksUseCaseImpl implements TasksUseCase {

	private final LocalDateTime now;
	private final Calendar calendar;
	private final TasksParser tasksParser;

	public TasksUseCaseImpl(LocalDateTime now, Calendar calendar, TasksParser tasksParser) {
		this.now = now;
		this.calendar = calendar;
		this.tasksParser = tasksParser;
	}

	private Predicate<Task> getFilterPredicate(TasksFilter filter) {
		return task -> switch (filter) {
			case TODO -> !task.isCompleted() && now.isBefore(task.getDueDate());
			case DUE -> !task.isCompleted() && now.isAfter(task.getDueDate());
		};
	}

	@Override
	public List<Task> getTasks(LocalDateTime now, TasksFilter filter) {
		List<VToDo> todoComponents = calendar.getComponents(Component.VTODO);
		Predicate<Task> filterPredicate = getFilterPredicate(filter);
		return todoComponents.stream()
			.map(tasksParser::transformToTask)
			.filter(filterPredicate)
			.toList();
	}

	@Override
	public void saveTask(Task task, File calendarFile) {
		VToDo todo = tasksParser.transformToVTodo(task);

		calendar.getComponents().add(todo);
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
