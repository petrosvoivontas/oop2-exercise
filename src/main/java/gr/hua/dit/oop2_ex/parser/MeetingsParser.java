package gr.hua.dit.oop2_ex.parser;

import gr.hua.dit.oop2_ex.model.Meeting;
import net.fortuna.ical4j.model.component.VEvent;

public interface MeetingsParser {

	Meeting transformToMeeting(VEvent vEvent);
}
