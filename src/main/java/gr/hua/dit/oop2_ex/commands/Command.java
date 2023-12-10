package gr.hua.dit.oop2_ex.commands;

public sealed class Command permits Command.ListEvents, Command.CreateEvents, Command.InvalidCommand {

	private Command() {
	}

	public final static class ListEvents extends Command {

		private final String filePath;
		private final String subcommand;

		public ListEvents(String filePath, String subcommand) {
			this.filePath = filePath;
			this.subcommand = subcommand;
		}

		public String getFilePath() {
			return filePath;
		}

		public String getSubcommand() {
			return subcommand;
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
