# Members
* Petros Voivontas - 219136
* Zacharias Kamitsos - 21832
* Tomor Gkisto - 2022013

> GitHub repo: https://github.com/petrosvoivontas/oop2-exercise

# Getting started

## How to compile the project

In order to compile the calendar.jar file we use the provided Maven wrapper. Use the commands below to package the application
into the final `calendar.jar` file.

> **Java version**
> 
> Java version 17+ is required to compile and run the application

### Packaging on Windows
```
mvnw.cmd clean package
```

### Packaging on Linux / MacOS

```shell
./mvnw clean package
```

The `calendar.jar` file will be placed in the `target` folder

## Using the application

The application supports 2 operations:
1. Listing meetings / tasks of a `.ics` file
2. Creating meetings / tasks in a `.ics` file (creates the file if it doesn't exist)

### List meetings / events

The application can list the meeting / events of a `.ics` file given a specific filter. The supported filters are:
* **day** - meetings till the end of the current day
* **week** - meetings till the end of the current week
* **month** - meetings till the end of the current month
* **pastday** - meetings since the start of the current day
* **pastweek** - meetings since the start of the current week
* **pastmonth** - meetings since the start of the current month
* **todo** - tasks that aren't completed and aren't yet due
* **due** - tasks that aren't completed and are overdue

Example:

```shell
java -jar calendar.jar day mycal.ics
```

### Create meetings / events

The application can create new meetings / events in an already existing `.ics` file, or create a new file if it doesn't already exist.

Example:

```shell
java -jar calendar.jar mycal.ics
```

# Handling `.ics` files
Management of `.ics` files (parsing and creating) is done using the [iCal4j](https://www.ical4j.org) library

# Other dependencies
The [org.jetbrains.annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) library was also used, to better define nullable and not-null parameters.

# Code walkthrough

## App.java
This file contains the `main` method of the application.

## CommandPicker.java
The job of the `CommandPicker` class is to take in the arguments passed from the terminal, validate them, and decide which command we should run (list, create).

## CreateEventsUseCase.java / ListEventsUseCase.java
The `CreateEventsUseCase` and `ListEventsUseCase` classes execute the basic operations of the app from start to finish. They receive the required arguments,
ask the user for input from the terminal (if necessary), communicate with the appropriate `Repository` classes for retrieval or storage, and print their output
to the console.

## Repository classes (MeetingsRepository / TasksRepository)
The repository classes are responsible for implementing the retrieval and save operations. Whenever the application wants to retrieve a meeting, or save a new task,
it uses the appropriate method of a Repository (e.g. `saveTask(Task task, File calendarFile)`).

## Logger classes (MeetingsLogger / TasksLogger)
These classes are mainly used in the list operation, taking a `Meeting` or `Task` object and printing it to the console.

## MeetingsParser / TasksParser
These classes are responsible for transforming an object of a class we have defined to an object of an iCal4j class, and vice versa. Instead of having that transformation code
in repositories or use cases, we have extracted it into separate classes.