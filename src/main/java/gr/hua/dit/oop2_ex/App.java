package gr.hua.dit.oop2_ex;

import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;
import gr.hua.dit.oop2_ex.commands.Command;
import gr.hua.dit.oop2_ex.commands.CommandPicker;
import gr.hua.dit.oop2_ex.usecase.CreateEventsUseCase;
import gr.hua.dit.oop2_ex.usecase.ListEventsUseCase;

public class App {

	public static void main(String[] args) {
		/*
		1. validate args
		2. choose scenario based on number of args
		3. execute commands
		3.a. list events - get events and print them to the console
		3.b. create events - ask for user input and then use save methods
		 */

		TimeTeller timeTeller = TimeService.getTeller();
		TimeService.stop();
		CommandPicker commandPicker = new CommandPicker();
		Command command = commandPicker.parseCommand(args);

		if (command instanceof Command.ListEvents listEventsCommand) {
			/*
			1. attempt to open file. If it doesn't exist, exit
			2. create Calendar from file
			3. use MeetingsUseCase or TasksUseCase
			4. print the resulting list to the console
			 */

			ListEventsUseCase useCase = new ListEventsUseCase(listEventsCommand.getFilePath());
			useCase.printEventsWithFilter(timeTeller.now(), listEventsCommand.getFilter());
		} else if (command instanceof Command.CreateEvents createEventsCommand) {
			CreateEventsUseCase useCase = new CreateEventsUseCase(createEventsCommand.getFilePath());
			useCase.createEvent();
		}
	}
}
