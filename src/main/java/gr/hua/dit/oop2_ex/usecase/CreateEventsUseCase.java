package gr.hua.dit.oop2_ex.usecase;

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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class CreateEventsUseCase {

	private final String iCalPath;
	private final MeetingsRepository meetingsRepository;
	private final TasksRepository tasksRepository;

	public CreateEventsUseCase(String iCalPath) {
		this(iCalPath, new MeetingsParserImpl(), new TasksParserImpl());
	}

	public CreateEventsUseCase(
		String iCalPath,
		MeetingsParser meetingsParser,
		TasksParser tasksParser
	) {
		Calendar calendar = ICSCalendarUtils.initCalendar(iCalPath);

		this.iCalPath = iCalPath;
		this.meetingsRepository = new MeetingsRepositoryImpl(calendar, meetingsParser);
		this.tasksRepository = new TasksRepositoryImpl(calendar, tasksParser);
	}

	private String readTitle(
		Scanner scanner,
		String promptMessage,
		String errorMessage
	) {
		System.out.print(promptMessage);
		while (!scanner.hasNextLine()) {
			System.out.print(errorMessage);
			scanner.nextLine();
		}
		return scanner.nextLine();
	}

	@Nullable
	private String readDescription(
		Scanner scanner,
		String promptMessage
	) {
		System.out.print(promptMessage);
		String description = scanner.nextLine();
		if (description.isEmpty() || description.isBlank()) {
			return null;
		}
		return description;
	}

	private LocalDateTime readDateTime(
		Scanner scanner,
		String promptMessage,
		String errorMessage,
		@Nullable LocalDateTime previousDateTime
	) {
		LocalDateTime result = null;
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
		do {
			System.out.print(promptMessage);
			while (!scanner.hasNextLine()) {
				System.out.print(errorMessage);
				scanner.nextLine();
			}
			String dateString = scanner.nextLine();
			try {
				LocalDateTime dateTime = LocalDateTime.parse(dateString, dateTimeFormatter);
				if (previousDateTime != null && dateTime.isBefore(previousDateTime)) {
					System.out.println("Date and time cannot be before " + previousDateTime);
					continue;
				}
				result = dateTime;
			} catch (DateTimeParseException ignored) {
			}
		} while (result == null);
		return result;
	}

	public void createEvent() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Choose the type of event you want to create (1 for meeting, 2 for task): ");
		while (!scanner.hasNext("^[" + EVENT_TYPE_MEETING + EVENT_TYPE_TASK + "]$")) {
			System.out.println("Invalid input");
			System.out.print("Choose the type of event you want to create (1 for meeting, 2 for task): ");
			scanner.next();
		}
		int eventType = scanner.nextInt();
		scanner.nextLine();

		String title = readTitle(
			scanner,
			"Enter a title for the new meeting: ",
			"Enter a non-empty title for this new event: "
		);

		String description = readDescription(
			scanner,
			"Enter a description for the new meeting: "
		);

		if (eventType == EVENT_TYPE_MEETING) {
			LocalDateTime startDateTime = readDateTime(
				scanner,
				"Enter the start date and time for the new meeting (" + DATE_TIME_FORMAT_PATTERN + "): ",
				"Enter a valid date and time in the format \"" + DATE_TIME_FORMAT_PATTERN + "\"",
				null
			);
			LocalDateTime endDateTime = readDateTime(
				scanner,
				"Enter the end date and time for the new meeting (" + DATE_TIME_FORMAT_PATTERN + "): ",
				"Enter a valid date and time in the format \"" + DATE_TIME_FORMAT_PATTERN + "\"",
				startDateTime
			);

			long duration = ChronoUnit.MILLIS.between(startDateTime, endDateTime);

			Meeting meeting = new Meeting(
				title,
				description,
				LocalDate.from(startDateTime),
				LocalTime.from(startDateTime),
				duration
			);

			// save meeting using meetingsRepository
			meetingsRepository.saveMeeting(meeting, new File(iCalPath));
		} else if (eventType == EVENT_TYPE_TASK) {
			LocalDateTime dueDateTime = readDateTime(
				scanner,
				"Enter the due date and time (" + DATE_TIME_FORMAT_PATTERN + "): ",
				"Enter a valid date and time in the format \"" + DATE_TIME_FORMAT_PATTERN + "\"",
				null
			);

			Task task = new Task(
				title,
				description,
				LocalDate.from(dueDateTime),
				LocalTime.from(dueDateTime),
				false
			);

			// save task using tasksRepository
			tasksRepository.saveTask(task, new File(iCalPath));
		}

		scanner.close();
	}

	private static final int EVENT_TYPE_MEETING = 1;
	private static final int EVENT_TYPE_TASK = 2;
	private static final String DATE_TIME_FORMAT_PATTERN = "dd/MM/yyyy H:mm";
}
