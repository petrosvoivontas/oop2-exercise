package gr.hua.dit.oop2_ex.usecase;

public enum TasksFilter implements EventsFilter {
	TODO("todo"),
	DUE("due");

	private final String command;

	TasksFilter(String command) {
		this.command = command;
	}

	@Override
	public String getCommand() {
		return command;
	}
}
