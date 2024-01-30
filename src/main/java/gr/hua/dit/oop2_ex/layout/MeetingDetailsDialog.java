package gr.hua.dit.oop2_ex.layout;

import com.github.lgooddatepicker.components.DateTimePicker;
import gr.hua.dit.oop2_ex.model.Meeting;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class MeetingDetailsDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonSave;
	private JButton buttonCancel;
	private JTextField titleTextField;
	private JTextArea descriptionTextArea;
	private DateTimePicker startDateTimePicker;
	private DateTimePicker endDateTimePicker;

	@Nullable
	private final Meeting meeting;

	public MeetingDetailsDialog(@Nullable Meeting meeting) {
		this.meeting = meeting;
		if (meeting != null) {
			setTitle("Edit \"" + meeting.getTitle() + "\"");
		} else {
			setTitle("New Meeting");
		}

		setContentPane(contentPane);
		setModal(true);
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
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		MeetingDetailsDialog dialog = new MeetingDetailsDialog(null);
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
