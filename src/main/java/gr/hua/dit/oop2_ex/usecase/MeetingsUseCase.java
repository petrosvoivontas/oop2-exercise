package gr.hua.dit.oop2_ex.usecase;

import gr.hua.dit.oop2_ex.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingsUseCase {

	List<Meeting> getMeetings(LocalDateTime now, MeetingsFilter filter);

	void saveMeeting(Meeting meeting);
}
