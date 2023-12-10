package gr.hua.dit.oop2_ex.logger;

import gr.hua.dit.oop2_ex.model.Task;

import java.io.PrintStream;

public class TasksLoggerImpl implements TasksLogger {

	private final PrintStream printStream;

	public TasksLoggerImpl() {
		this(System.out);
	}

	public TasksLoggerImpl(PrintStream printStream) {
		this.printStream = printStream;
	}

	private String taskToString(Task task) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("TITLE: ");
		stringBuilder.append(task.getTitle());
		stringBuilder.append('\n');
		if (task.getDescription() != null) {
			stringBuilder.append("DESCRIPTION: ");
			stringBuilder.append(task.getDescription());
			stringBuilder.append('\n');
		}
		stringBuilder.append("DUE DATE: ");
		stringBuilder.append(task.getDueDate());
		stringBuilder.append('\n');
		stringBuilder.append("COMPLETED: ");
		if (task.isCompleted()) {
			stringBuilder.append("YES");
//			stringBuilder.append('\n');
//			stringBuilder.append("COMPLETED AT: ");
		} else {
			stringBuilder.append("NO");
		}
		return stringBuilder.toString();
	}

	@Override
	public void logTask(Task task) {
		printStream.println(task);
	}
}
