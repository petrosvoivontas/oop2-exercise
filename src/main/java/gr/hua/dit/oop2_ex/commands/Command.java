package gr.hua.dit.oop2_ex.commands;

import gr.hua.dit.oop2_ex.usecase.EventsFilter;

public sealed class Command permits Command.ListEvents, Command.CreateEvents, Command.InvalidCommand {

	private Command() {
	}

	public final static class ListEvents extends Command {

		private final String filePath;
		private final EventsFilter filter;

		public ListEvents(String filePath, EventsFilter filter) {
			this.filePath = filePath;
			this.filter = filter;
		}

		public String getFilePath() {
			return filePath;
		}

		public EventsFilter getFilter() {
			return filter;
		}
	}

	public final static class CreateEvents extends Command {

		private final String filePath;

		public CreateEvents(String filePath) {
			this.filePath = filePath;
		}

		public String getFilePath() {
			return filePath;
		}
	}

	public final static class InvalidCommand extends Command {

		InvalidCommand() {
		}
	}
}
