package gr.hua.dit.oop2_ex.layout;

import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.parser.MeetingsParserImpl;
import gr.hua.dit.oop2_ex.parser.tasks.TasksParserImpl;
import gr.hua.dit.oop2_ex.repo.MeetingsRepository;
import gr.hua.dit.oop2_ex.repo.MeetingsRepositoryImpl;
import gr.hua.dit.oop2_ex.repo.TasksRepository;
import gr.hua.dit.oop2_ex.repo.TasksRepositoryImpl;
import gr.hua.dit.oop2_ex.usecase.EventsFilter;
import gr.hua.dit.oop2_ex.usecase.MeetingsFilter;
import gr.hua.dit.oop2_ex.usecase.TasksFilter;
import gr.hua.dit.oop2_ex.utils.ICSCalendarUtils;
import net.fortuna.ical4j.model.Calendar;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarFrame extends JFrame {

	@Nullable
	private JScrollPane scrollPane;

	private File calendarFile;

	public CalendarFrame() throws HeadlessException {
		super("Calendar");
	}

	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".ics files", "ics");
		fileChooser.setFileFilter(filter);

		int state = fileChooser.showOpenDialog(null);

		if (state == JFileChooser.APPROVE_OPTION) {
			calendarFile = fileChooser.getSelectedFile();
			getMenuBar().add(createViewMenu());
			displayButtonsForNewEvents();
		}
	}

	private String getMeetingPreview(Meeting meeting) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		return meeting.getTitle() +
			" | " +
			formatter.format(meeting.getStartDateTime()) +
			" - " +
			formatter.format(meeting.getEndDateTime());
	}

	private String getTaskPreview(Task task) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		return task.getTitle() +
			" | Due " +
			formatter.format(task.getDueDate()) +
			" | Status: " +
			(task.isCompleted() ? "Done" : "To-do");
	}

	private void showMeetings(MeetingsFilter filter) {
		if (scrollPane != null) {
			getContentPane().remove(scrollPane);
		}
		Calendar calendar = ICSCalendarUtils.getOrCreateCalendar(calendarFile.getAbsolutePath());
		MeetingsRepository meetingsRepository = new MeetingsRepositoryImpl(calendar, new MeetingsParserImpl());
		List<Meeting> meetings = meetingsRepository.getMeetings(LocalDateTime.now(), filter);
		List<String> uiModels = meetings.stream().map(this::getMeetingPreview).toList();

		JList<String> eventsList = new JList<>();
		eventsList.setListData(uiModels.toArray(new String[0]));
		scrollPane = new JScrollPane(eventsList);
		add(scrollPane);

		eventsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int listIndex = eventsList.locationToIndex(e.getPoint());
					Meeting meeting = meetings.get(listIndex);
					MeetingDetailsDialog meetingDetailsFrame = new MeetingDetailsDialog(
						meetingsRepository,
						calendarFile,
						meeting
					);
					meetingDetailsFrame.pack();
					meetingDetailsFrame.setVisible(true);
				}
			}
		});

		validate();
	}

	private void showTasks(TasksFilter filter) {
		if (scrollPane != null) {
			getContentPane().remove(scrollPane);
		}
		Calendar calendar = ICSCalendarUtils.getOrCreateCalendar(calendarFile.getAbsolutePath());
		TasksRepository tasksRepository = new TasksRepositoryImpl(calendar, new TasksParserImpl());
		List<Task> tasks = tasksRepository.getTasks(LocalDateTime.now(), filter);
		List<String> uiModels = tasks.stream().map(this::getTaskPreview).toList();

		JList<String> eventsList = new JList<>();
		eventsList.setListData(uiModels.toArray(new String[0]));
		scrollPane = new JScrollPane(eventsList);
		add(scrollPane);

		eventsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int listIndex = eventsList.locationToIndex(e.getPoint());
					Task task = tasks.get(listIndex);
					TaskDetailsDialog meetingDetailsFrame = new TaskDetailsDialog(
						tasksRepository,
						calendarFile,
						task
					);
					meetingDetailsFrame.pack();
					meetingDetailsFrame.setVisible(true);
				}
			}
		});

		validate();
	}

	private Menu createFileMenu() {
		Menu fileMenu = new Menu("File");
		MenuItem openMenuItem = new MenuItem("Open...");
		openMenuItem.addActionListener(event -> {
			openFileChooser();
		});
		fileMenu.add(openMenuItem);
		return fileMenu;
	}

	private CheckboxMenuItem createCheckboxMenuItemWithLister(
		String label,
		EventsFilter eventsFilter,
		Menu menu,
		boolean selected
	) {
		CheckboxMenuItem menuItem = new CheckboxMenuItem(label, selected);
		menuItem.addItemListener(new ViewMenuItemListener(menu, () -> {
			if (eventsFilter instanceof MeetingsFilter meetingsFilter) {
				showMeetings(meetingsFilter);
			} else if (eventsFilter instanceof TasksFilter tasksFilter) {
				showTasks(tasksFilter);
			}
		}));
		return menuItem;
	}

	private CheckboxMenuItem createCheckboxMenuItemWithLister(
		String label,
		EventsFilter eventsFilter,
		Menu menu
	) {
		return createCheckboxMenuItemWithLister(label, eventsFilter, menu, false);
	}

	private Menu createViewMenu() {
		Menu viewMenu = new Menu("View");

		MenuItem meetingsSectionMenuItem = new MenuItem("Meetings lists");
		meetingsSectionMenuItem.setEnabled(false);
		viewMenu.add(meetingsSectionMenuItem);

		viewMenu.add(createCheckboxMenuItemWithLister(
			"Day",
			MeetingsFilter.DAY,
			viewMenu,
			true
		));
		viewMenu.add(createCheckboxMenuItemWithLister(
			"Week",
			MeetingsFilter.WEEK,
			viewMenu
		));
		viewMenu.add(createCheckboxMenuItemWithLister(
			"Month",
			MeetingsFilter.MONTH,
			viewMenu
		));
		viewMenu.add(createCheckboxMenuItemWithLister(
			"Past day",
			MeetingsFilter.PAST_DAY,
			viewMenu
		));
		viewMenu.add(createCheckboxMenuItemWithLister(
			"Past week",
			MeetingsFilter.PAST_WEEK,
			viewMenu
		));
		viewMenu.add(createCheckboxMenuItemWithLister(
			"Past month",
			MeetingsFilter.PAST_MONTH,
			viewMenu
		));

		viewMenu.addSeparator();

		MenuItem tasksSectionMenuItem = new MenuItem("Tasks lists");
		tasksSectionMenuItem.setEnabled(false);
		viewMenu.add(tasksSectionMenuItem);

		viewMenu.add(createCheckboxMenuItemWithLister(
			"To-do",
			TasksFilter.TODO,
			viewMenu
		));
		viewMenu.add(createCheckboxMenuItemWithLister(
			"Due",
			TasksFilter.DUE,
			viewMenu
		));

		return viewMenu;
	}

	private void setupMenuBar() {
		MenuBar menuBar = new MenuBar();

		menuBar.add(createFileMenu());

		setMenuBar(menuBar);
	}

	private void displayButtonsForNewEvents() {
		JButton newMeetingButton = new JButton("New meeting");
		JButton newTaskButton = new JButton("New task");

		newMeetingButton.addActionListener(e -> {
			Calendar calendar = ICSCalendarUtils.getOrCreateCalendar(calendarFile.getAbsolutePath());
			MeetingsRepository meetingsRepository = new MeetingsRepositoryImpl(calendar, new MeetingsParserImpl());
			MeetingDetailsDialog dialog = new MeetingDetailsDialog(
				meetingsRepository,
				calendarFile,
				null
			);
			dialog.pack();
			dialog.setVisible(true);
		});

		newTaskButton.addActionListener(e -> {
			Calendar calendar = ICSCalendarUtils.getOrCreateCalendar(calendarFile.getAbsolutePath());
			TasksRepository tasksRepository = new TasksRepositoryImpl(calendar, new TasksParserImpl());
			TaskDetailsDialog dialog = new TaskDetailsDialog(
				tasksRepository,
				calendarFile,
				null
			);
			dialog.pack();
			dialog.setVisible(true);
		});

		add(newMeetingButton);
		add(newTaskButton);
		validate();
	}

	@Override
	protected void frameInit() {
		super.frameInit();

		setupMenuBar();

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	}
}
