package gr.hua.dit.oop2_ex.layout;

import com.github.lgooddatepicker.components.DateTimePicker;
import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.repo.MeetingsRepository;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MeetingDetailsDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonSave;
	private JButton buttonCancel;
	private JTextField titleTextField;
	private JTextArea descriptionTextArea;
	private DateTimePicker startDateTimePicker;
	private DateTimePicker endDateTimePicker;

	private final MeetingsRepository meetingsRepository;
	private final File calendarFile;

	@Nullable
	private final Meeting meeting;

	public MeetingDetailsDialog(
		MeetingsRepository meetingsRepository,
		File calendarFile,
		@Nullable Meeting meeting
	) {
		this.meetingsRepository = meetingsRepository;
		this.calendarFile = calendarFile;
		this.meeting = meeting;

		if (meeting != null) {
			setTitle("Editing \"" + meeting.getTitle() + "\"");
		} else {
			setTitle("New meeting");
		}

		setContentPane(contentPane);
		setModalityType(ModalityType.APPLICATION_MODAL);
		getRootPane().setDefaultButton(buttonSave);

		buttonSave.addActionListener(e -> onSave());

		buttonCancel.addActionListener(e -> onCancel());

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(
			e -> onCancel(),
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);

		initFieldsForExistingMeeting();
	}

	private void initFieldsForExistingMeeting() {
		if (meeting == null) return;

		titleTextField.setText(meeting.getTitle());
		descriptionTextArea.setText(meeting.getDescription());
		startDateTimePicker.setDateTimePermissive(meeting.getStartDateTime());
		endDateTimePicker.setDateTimePermissive(meeting.getEndDateTime());
	}

	private void onSave() {
		// add your code here
		saveMeeting();
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	private Meeting constructNewMeeting() {
		String title = titleTextField.getText();
		String description = descriptionTextArea.getText();
		LocalDateTime startDatetime = startDateTimePicker.getDateTimeStrict();
		LocalDateTime endDateTime = endDateTimePicker.getDateTimeStrict();
		long duration = ChronoUnit.MILLIS.between(startDatetime, endDateTime);
		return new Meeting(
			title,
			description,
			LocalDate.from(startDatetime),
			LocalTime.from(endDateTime),
			duration
		);
	}

	private void saveMeeting() {
		Meeting newMeeting = constructNewMeeting();
		if (meeting != null) {
			meetingsRepository.deleteMeeting(meeting, calendarFile);
		}
		meetingsRepository.saveMeeting(newMeeting, calendarFile);
	}
}
