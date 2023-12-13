package gr.hua.dit.oop2_ex;

import gr.hua.dit.oop2_ex.model.Meeting;
import gr.hua.dit.oop2_ex.model.Task;
import gr.hua.dit.oop2_ex.parser.MeetingsParserImpl;
import gr.hua.dit.oop2_ex.parser.tasks.TasksParserImpl;
import gr.hua.dit.oop2_ex.usecase.MeetingsUseCase;
import gr.hua.dit.oop2_ex.usecase.MeetingsUseCaseImpl;
import gr.hua.dit.oop2_ex.usecase.TasksUseCaseImpl;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
//import net.fortuna.ical4j.util.UidGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

//import static gr.hua.dit.oop2_ex.createEvents.createMeeting;
//import static gr.hua.dit.oop2_ex.createEvents.createTask;


public class Main {

    public static void main(String[] args) throws IOException
    {
        try {
            Scanner scanner = new Scanner(System.in);

            //chooses the file name
            System.out.print("What is the name of the ICS file you would like to add a calendar?: ");
            String icsFileName = scanner.nextLine();
//            String icsFileName = args[0];

            File file = new File(icsFileName);
            Calendar calendar;

            //checks if file exists or creates it
            if (file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                CalendarBuilder builder = new CalendarBuilder();
                calendar = builder.build(fin);
            } else {
                calendar = new Calendar();
            }

            //gets the info from the user
            System.out.println("What is the name of the event");
            String eventName = scanner.nextLine();

            System.out.println("What kind of event do you want to add, a Task or a Meeting?");
            String kindOfEvent = scanner.nextLine().toLowerCase();
            while (!"task".equals(kindOfEvent) && !"meeting".equals(kindOfEvent)) {
                System.out.print("Events can only be Task or Meeting. Try again.");
                kindOfEvent = scanner.nextLine();
            }

            System.out.println("Enter the date of the event (\"yyyy-MM-dd HH:mm\")");
            String dateTimeString = scanner.nextLine();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDateTime = dateFormat.parse(dateTimeString);

            System.out.println("Give a description to the event");
            String eventDescription = scanner.nextLine();

            // Extracting date and time components
            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(dateOnlyFormat.format(startDateTime));

            SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm");
            LocalTime startTime = LocalTime.parse(timeOnlyFormat.format(startDateTime));


//            // Creating a unique identifier for the event
//            UidGenerator uidGenerator = new UidGenerator("1");
//            String uidValue = uidGenerator.generateUid().getValue();

            if (kindOfEvent.equals("task")) {
                boolean completed = false;
                Task task = new Task(eventName, eventDescription, startDate, startTime, completed);
                TasksUseCaseImpl taskusedcase = new TasksUseCaseImpl(LocalDateTime.now(), calendar, new TasksParserImpl());
                taskusedcase.saveTask(task);
            } else {
                System.out.println("How long will the meeting be?");
                long duration = Long.parseLong(scanner.nextLine());
                Meeting meeting = new Meeting(eventName, eventDescription, startDate, startTime, duration);
                MeetingsUseCaseImpl meetingsUseCase = new MeetingsUseCaseImpl(LocalDateTime.now(), calendar, new MeetingsParserImpl());
                meetingsUseCase.saveMeeting(meeting);
            }


            //saves the event
            System.out.println("Event added successfully.");



            scanner.close();

        } catch (IOException | ParserException | ParseException e) {
            e.printStackTrace();
        }
    }

}
