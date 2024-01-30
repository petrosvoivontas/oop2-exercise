package gr.hua.dit.oop2_ex.layout;

import com.github.lgooddatepicker.components.DateTimePicker;
import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.repo.TasksRepository;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TaskDetailsDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonSave;
	private JButton buttonCancel;
	private JTextField titleTextField;
	private JTextArea descriptionTextArea;
	private DateTimePicker dueDateTimePicker;
	private JCheckBox statusCheckbox;

	private final TasksRepository tasksRepository;
	private final File calendarFile;

	@Nullable
	private final Task task;

	public TaskDetailsDialog(
		TasksRepository tasksRepository,
		File calendarFile,
		@Nullable Task task
	) {
		this.tasksRepository = tasksRepository;
		this.calendarFile = calendarFile;
		this.task = task;

		if (task != null) {
			setTitle("Editing \"" + task.getTitle() + "\"");
		}
		{
			setTitle("New task");
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

		initFieldsForExistingTask();
	}

	private void initFieldsForExistingTask() {
		if (task == null) return;

		titleTextField.setText(task.getTitle());
		descriptionTextArea.setText(task.getDescription());
		dueDateTimePicker.setDateTimePermissive(task.getDueDate());
		statusCheckbox.setSelected(task.isCompleted());
	}

	private void onSave() {
		saveTask();
		dispose();
	}

	private void onCancel() {
		dispose();
	}

	private Task constructNewTask() {
		String title = titleTextField.getText();
		String description = descriptionTextArea.getText();
		boolean completed = statusCheckbox.isSelected();
		LocalDateTime dueDateTime = dueDateTimePicker.getDateTimeStrict();
		return new Task(
			title,
			description,
			LocalDate.from(dueDateTime),
			LocalTime.from(dueDateTime),
			completed
		);
	}

	private void saveTask() {
		Task newTask = constructNewTask();
		if (task != null) {
			tasksRepository.deleteMeeting(task, calendarFile);
		}
		tasksRepository.saveTask(newTask, calendarFile);
	}
}
