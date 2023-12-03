package gr.hua.dit.oop2_ex.parser.tasks;

import gr.hua.dit.oop2_ex.model.Task;
import net.fortuna.ical4j.model.component.VToDo;

public interface TasksParser {

	Task transformToTask(VToDo todoComponent);
}
