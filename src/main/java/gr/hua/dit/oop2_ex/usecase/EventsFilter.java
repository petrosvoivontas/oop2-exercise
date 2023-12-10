package gr.hua.dit.oop2_ex.usecase;

public sealed interface EventsFilter permits MeetingsFilter, TasksFilter {

	String getCommand();
}
