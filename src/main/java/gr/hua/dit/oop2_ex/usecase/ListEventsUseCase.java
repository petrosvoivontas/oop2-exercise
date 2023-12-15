package gr.hua.dit.oop2_ex.usecase;

import gr.hua.dit.oop2_ex.logger.MeetingsLogger;
import gr.hua.dit.oop2_ex.logger.MeetingsLoggerImpl;
import gr.hua.dit.oop2_ex.logger.TasksLogger;
import gr.hua.dit.oop2_ex.logger.TasksLoggerImpl;
import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.parser.MeetingsParser;
import gr.hua.dit.oop2_ex.parser.MeetingsParserImpl;
import gr.hua.dit.oop2_ex.parser.tasks.TasksParser;
import gr.hua.dit.oop2_ex.parser.tasks.TasksParserImpl;
import gr.hua.dit.oop2_ex.repo.MeetingsRepository;
import gr.hua.dit.oop2_ex.repo.MeetingsRepositoryImpl;
import gr.hua.dit.oop2_ex.repo.TasksRepository;
import gr.hua.dit.oop2_ex.repo.TasksRepositoryImpl;
import gr.hua.dit.oop2_ex.utils.ICSCalendarUtils;
import net.fortuna.ical4j.model.Calendar;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class ListEventsUseCase {

	private final String iCalPath;
	private final MeetingsRepository meetingsRepository;
	private final MeetingsLogger meetingsLogger;

	private final TasksRepository tasksRepository;
	private final TasksLogger tasksLogger;

	public ListEventsUseCase(String iCalPath) {
		this(
			iCalPath,
			new MeetingsParserImpl(),
			new MeetingsLoggerImpl(),
			new TasksParserImpl(),
			new TasksLoggerImpl()
		);
	}

	public ListEventsUseCase(
		String iCalPath,
		MeetingsParser meetingsParser,
		MeetingsLogger meetingsLogger,
		TasksParser tasksParser,
		TasksLogger tasksLogger
	) {
		Calendar calendar = ICSCalendarUtils.initCalendar(iCalPath);

		this.iCalPath = iCalPath;
		this.meetingsRepository = new MeetingsRepositoryImpl(calendar, meetingsParser);
		this.meetingsLogger = meetingsLogger;
		this.tasksRepository = new TasksRepositoryImpl(calendar, tasksParser);
		this.tasksLogger = tasksLogger;
	}

	public void printEventsWithFilter(LocalDateTime now, EventsFilter filter) {
		String fileName = Paths.get(iCalPath).getFileName().toString();

		if (filter instanceof MeetingsFilter meetingsFilter) {
			System.out.println("Looking for meetings in " + fileName + " with filter \"" + filter.getCommand() + "\"");

			List<Meeting> meetings = meetingsRepository.getMeetings(now, meetingsFilter);

			System.out.println("Found " + meetings.size() + " meetings");

			for (Meeting meeting : meetings) {
				// print meeting to the console
				meetingsLogger.logMeeting(meeting);
			}
		} else if (filter instanceof TasksFilter tasksFilter) {
			System.out.println("Looking for tasks in " + fileName + " with filter \"" + filter.getCommand() + "\"");

			List<Task> tasks = tasksRepository.getTasks(now, tasksFilter);

			System.out.println("Found " + tasks.size() + " tasks");

			for (Task task : tasks) {
				// print task to the console
				tasksLogger.logTask(task);
			}
		}
	}
}
