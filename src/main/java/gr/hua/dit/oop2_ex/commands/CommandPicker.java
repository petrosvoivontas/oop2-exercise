package gr.hua.dit.oop2_ex.commands;

import gr.hua.dit.oop2_ex.usecase.MeetingsFilter;
import gr.hua.dit.oop2_ex.usecase.TasksFilter;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CommandPicker {

	private boolean isValidFilePath(String path) {
		try {
			Paths.get(path);
			return true;
		} catch (InvalidPathException e) {
			return false;
		}
	}

	private boolean argsAreValid(String[] args) {
		// a valid command contains 1 or 2 arguments
		if (args.length == 0 || args.length > 2) {
			return false;
		}

		// if there is only 1 argument, check if it is a valid path
		if (args.length == 1) {
			return isValidFilePath(args[0]);
		}

		// there are 2 arguments

		// is the 1st argument a meetings filter?
		boolean isMeetingsFilter = Arrays.stream(MeetingsFilter.values())
			.anyMatch(filter -> Objects.equals(filter.getCommand(), args[0]));

		// is the 2nd argument a tasks filter?
		boolean isTasksFilter = Arrays.stream(TasksFilter.values())
			.anyMatch(filter -> Objects.equals(filter.getCommand(), args[0]));

		return (isMeetingsFilter || isTasksFilter) && isValidFilePath(args[1]);
	}

	public Command parseCommand(String[] args) {
		if (!argsAreValid(args)) {
			return Commands.INVALID_COMMAND;
		}

		if (args.length == 1) {
			return new Command.CreateEvents(args[0]);
		}

		final String filterArg = args[0];
		final String filePathArg = args[1];

		Optional<MeetingsFilter> meetingsFilter = Arrays.stream(MeetingsFilter.values())
			.filter(filter -> filter.getCommand().equals(filterArg))
			.findFirst();
		if (meetingsFilter.isPresent()) {
			return new Command.ListEvents(filePathArg, meetingsFilter.get());
		}

		Optional<TasksFilter> tasksFilter = Arrays.stream(TasksFilter.values())
			.filter(filter -> filter.getCommand().equals(filterArg))
			.findFirst();
		if (tasksFilter.isPresent()) {
			return new Command.ListEvents(filePathArg, tasksFilter.get());
		}

		return Commands.INVALID_COMMAND;
	}
}
