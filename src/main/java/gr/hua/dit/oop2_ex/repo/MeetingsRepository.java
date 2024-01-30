package gr.hua.dit.oop2_ex.repo;

import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.usecase.MeetingsFilter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface MeetingsRepository {

	List<Meeting> getMeetings(LocalDateTime now, MeetingsFilter filter);

	void saveMeeting(Meeting meeting, File calendarFile);

	void deleteMeeting(Meeting meeting, File calendarFile);
}
