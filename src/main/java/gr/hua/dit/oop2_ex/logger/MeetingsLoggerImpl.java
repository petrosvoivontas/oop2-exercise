package gr.hua.dit.oop2_ex.logger;

import gr.hua.dit.oop2_ex.model.Meeting;

import java.io.PrintStream;

public class MeetingsLoggerImpl implements MeetingsLogger {

	private final PrintStream printStream;

	public MeetingsLoggerImpl() {
		this(System.out);
	}

	public MeetingsLoggerImpl(PrintStream printStream) {
		this.printStream = printStream;
	}

	private String meetingToString(Meeting meeting) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("TITLE: ");
		stringBuilder.append(meeting.getTitle());
		stringBuilder.append('\n');
		if (meeting.getDescription() != null) {
			stringBuilder.append("DESCRIPTION: ");
			stringBuilder.append(meeting.getDescription());
			stringBuilder.append('\n');
		}
		stringBuilder.append("STARTS AT: ");
		stringBuilder.append(meeting.getStartDateTime());
		stringBuilder.append('\n');
		stringBuilder.append("ENDS AT: ");
		stringBuilder.append(meeting.getEndDateTime());
		stringBuilder.append('\n');
		return stringBuilder.toString();
	}

	@Override
	public void logMeeting(Meeting meeting) {
		printStream.println(meetingToString(meeting));
	}
}
