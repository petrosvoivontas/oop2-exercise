package gr.hua.dit.oop2_ex.usecase;

import gr.hua.dit.oop2_ex.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TasksUseCase {

	List<Task> getTasks(LocalDateTime now, TasksFilter filter);

	void saveTask(Task task);
}
