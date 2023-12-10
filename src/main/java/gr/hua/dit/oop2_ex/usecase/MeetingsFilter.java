package gr.hua.dit.oop2_ex.usecase;

public enum MeetingsFilter implements EventsFilter {
	DAY("day"),
	WEEK("week"),
	MONTH("month"),
	PAST_DAY("pastday"),
	PAST_WEEK("pastweek"),
	PAST_MONTH("pastmonth");

	private final String command;

	MeetingsFilter(String command) {
		this.command = command;
	}

	@Override
	public String getCommand() {
		return command;
	}
}
