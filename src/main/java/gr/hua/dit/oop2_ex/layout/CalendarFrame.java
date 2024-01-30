package gr.hua.dit.oop2_ex.layout;

import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.parser.MeetingsParserImpl;
import gr.hua.dit.oop2_ex.repo.MeetingsRepository;
import gr.hua.dit.oop2_ex.repo.MeetingsRepositoryImpl;
import gr.hua.dit.oop2_ex.usecase.MeetingsFilter;
import gr.hua.dit.oop2_ex.utils.ICSCalendarUtils;
import net.fortuna.ical4j.model.Calendar;

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

	public CalendarFrame() throws HeadlessException {
		super("Calendar");
	}

	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".ics files", "ics");
		fileChooser.setFileFilter(filter);

		int state = fileChooser.showOpenDialog(null);

		if (state == JFileChooser.APPROVE_OPTION) {
			File calendarFile = fileChooser.getSelectedFile();
			getEvents(calendarFile);
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

	private void getEvents(File calendarFile) {
		Calendar calendar = ICSCalendarUtils.getOrCreateCalendar(calendarFile.getAbsolutePath());
		MeetingsRepository meetingsRepository = new MeetingsRepositoryImpl(calendar, new MeetingsParserImpl());
		List<Meeting> meetings = meetingsRepository.getMeetings(LocalDateTime.now(), MeetingsFilter.PAST_WEEK);
		List<String> meetingsModels = meetings.stream().map(this::getMeetingPreview).toList();

		JList<String> jList = new JList<>();
		jList.setListData(meetingsModels.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(jList);
		add(scrollPane);

		jList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					System.out.println("double click on " + e.getComponent());
					int listIndex = jList.locationToIndex(e.getPoint());
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
	}

	private void setupMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem openMenuItem = new MenuItem("Open...");
		openMenuItem.addActionListener(event -> {
			openFileChooser();
		});
		fileMenu.add(openMenuItem);
		menuBar.add(fileMenu);
		setMenuBar(menuBar);
	}

	@Override
	protected void frameInit() {
		super.frameInit();

		setupMenuBar();
	}
}
