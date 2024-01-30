package gr.hua.dit.oop2_ex.repo;

import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.usecase.TasksFilter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface TasksRepository {

	List<Task> getTasks(LocalDateTime now, TasksFilter filter);

	void saveTask(Task task, File calendarFile);

	void deleteMeeting(Task task, File calendarFile);
}
