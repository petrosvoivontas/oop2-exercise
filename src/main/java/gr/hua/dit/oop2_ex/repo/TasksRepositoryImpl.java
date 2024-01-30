package gr.hua.dit.oop2_ex.repo;

import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.parser.tasks.TasksParser;
import gr.hua.dit.oop2_ex.usecase.TasksFilter;
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
import java.util.Objects;
import java.util.function.Predicate;

public class TasksRepositoryImpl implements TasksRepository {

	private final Calendar calendar;
	private final TasksParser tasksParser;

	public TasksRepositoryImpl(Calendar calendar, TasksParser tasksParser) {
		this.calendar = calendar;
		this.tasksParser = tasksParser;
	}

	private Predicate<Task> getFilterPredicate(LocalDateTime now, TasksFilter filter) {
		return task -> switch (filter) {
			case TODO -> !task.isCompleted() && now.isBefore(task.getDueDate());
			case DUE -> !task.isCompleted() && now.isAfter(task.getDueDate());
		};
	}

	@Override
	public List<Task> getTasks(LocalDateTime now, TasksFilter filter) {
		List<VToDo> todoComponents = calendar.getComponents(Component.VTODO);
		Predicate<Task> filterPredicate = getFilterPredicate(now, filter);
		return todoComponents.stream()
			.map(tasksParser::transformToTask)
			.filter(filterPredicate)
			.toList();
	}

	@Override
	public void saveTask(Task task, File calendarFile) {
		VToDo todo = tasksParser.transformToVTodo(task);

		calendar.getComponents().add(todo);
		storeCalendarToFile(calendarFile);
	}

	@Override
	public void deleteMeeting(Task task, File calendarFile) {
		calendar.getComponents(VToDo.VTODO)
			.stream().filter(vTodo -> Objects.equals(((VToDo) vTodo).getSummary().getValue(), task.getTitle()))
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
