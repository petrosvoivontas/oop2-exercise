package gr.hua.dit.oop2_ex.layout;

import javax.swing.*;

public class CalendarUI {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CalendarFrame calendarFrame = new CalendarFrame();

			calendarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			calendarFrame.setSize(300,300);
			calendarFrame.setLocationRelativeTo(null);
			calendarFrame.setVisible(true);
		});
	}
}
