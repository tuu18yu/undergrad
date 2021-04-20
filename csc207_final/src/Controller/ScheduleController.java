package Controller;

import Presenter.RoomPresenter;
import Presenter.SchedulePresenter;
import Presenter.SignUpPresenter;
import UseCase.EventManager;
import UseCase.RoomManager;
import UseCase.UserManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A controller class that reads in user input related to adding events by
 * calling EventManager. User input is prompted by calling SchedulePresenter
 * which has printing methods that show and ask for information.
 *
 * @author An Yen, Kelly Le
 */
public class ScheduleController {
    private final EventManager eventManager;
    private final RoomManager roomManager;
    private final UserManager userManager;
    private final SchedulePresenter scheduleP;
    private final RoomPresenter roomP;
    private final SignUpPresenter signUpP;

    /**
     * Creates a instance of ScheduleController with a ManagerFacade instance.
     * @param em - an EventManager instance
     * @param um - an UserManager instance
     */
    public ScheduleController(EventManager em, RoomManager rm, UserManager um){
        eventManager = em;
        roomManager = rm;
        userManager = um;
        scheduleP  = new SchedulePresenter(eventManager);
        roomP = new RoomPresenter();
        signUpP = new SignUpPresenter();
    }

    /**
     * Calls SchedulePresenter to print out the Scheduling Menu.
     */
    public String getScheduleMenu(){
        Scanner scan = new Scanner(System.in);
        scheduleP.printScheduleMenu();
        return scan.nextLine();
    }

    /**
     * Allows Organizers to create different kinds of events or exit this menu by inputting
     * a specific number.
     */
    public void getEventTypeMenu(){
        String option;
        do {
            Scanner scan = new Scanner(System.in);
            scheduleP.printEventTypeMenu();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    this.createEvent(1);
                    break;
                case "2":
                    this.createEvent(2);
                    break;
                case "3":
                    this.createEvent(0);
                    break;
                case "4":
                    break;
                default:
                    signUpP.printInvalidInput();
            }
        }while (!option.equals("4"));
    }

    /**
     * Calls SchedulePresenter to prompt the user to input
     * the name, time, room, and date and prints out the
     * event details if successful.
     *
     * @param numberOfSpeakers - the number of speakers required for this event
     */
    public void createEvent(int numberOfSpeakers){
        String name;
        boolean ValidName;
        do {
            Scanner scan = new Scanner(System.in);
            scheduleP.printName();
            name = scan.nextLine();
            if (name.equals("0")) {
                return;
            }
            else if (name.trim().isEmpty() || eventManager.getEvent(name)){
                scheduleP.printFailedName();
                ValidName = false;
            } else {
                ValidName = true;
            }
        } while (!ValidName);

        //enter date, check date
        String inputDate;
        boolean ValidDate = false;
        do {
            Scanner scanD = new Scanner(System.in);
            scheduleP.printEnterDate();
            inputDate = scanD.nextLine();
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(inputDate);
                if (localDate.compareTo(LocalDate.now()) < 0) {
                    scheduleP.printInvalidDate();
                    ValidDate = false;
                } else{
                    ValidDate = true;
                }
            }catch (Exception e){
                scheduleP.printInvalidDateFormat();
            }

        } while (!ValidDate);

        //enter time, check time
        String inputTime;
        boolean ValidTime;
        do {
            Scanner scanT = new Scanner(System.in);
            scheduleP.displayStartTimes();
            scheduleP.printEnterTime();
            inputTime = scanT.nextLine();
            if (!eventManager.getStartTimes().contains(inputTime)) {
                scheduleP.printFailStartTimes();
                ValidTime = false;
            } else {
                ValidTime = true;
            }
        } while (!ValidTime);

        //combine the date and time into one LocalDateTime instance
        LocalDateTime eventTime = LocalDateTime.parse(inputDate + "T" + inputTime + ":00:00");

        //enter duration
        String inputDuration;
        int intDuration;
        boolean ValidDuration;
        do{
            Scanner scanD = new Scanner(System.in);
            scheduleP.printEnterDurationPrompt();
            inputDuration = scanD.nextLine();
            intDuration = Integer.parseInt(inputDuration);
            if (intDuration < 1 || intDuration > 3) {
                scheduleP.printInvalidDuration();
                ValidDuration = false;
            }else {
                ValidDuration = true;
            }
        }while(!ValidDuration);

        String roomInput;
        int room = 0;
        boolean ValidRoom = false;
        do {
            Scanner scan2 = new Scanner(System.in);
            ArrayList<Integer> availableRooms = roomManager.getAvailableRooms(eventTime, intDuration);

            roomP.printAvailableRoomNumbers(availableRooms);
            roomP.printChooseAvailableRoom();

            roomInput = scan2.nextLine();
            try {
                room = Integer.parseInt(roomInput);
                if (room == 0) {
                    return;
                } else if (!availableRooms.contains(room)){
                    roomP.printRoomNotAvailable();
                    ValidRoom = false;
                } else {
                    ValidRoom = true;
                }
            } catch (Exception e){
                roomP.printRoomNotAvailable();
            }
        } while (!ValidRoom);

        String capacity;
        boolean ValidCapacity = false;
        do {
            Scanner scanC = new Scanner(System.in);
            int roomCapacity = roomManager.getRoomCapacity(room);
            scheduleP.printCapacityPrompt(roomCapacity);
            capacity = scanC.nextLine();
            try {
                int intCapacity = Integer.parseInt(capacity);
                if (intCapacity <= 0 || roomManager.hasCapacityConflict(room, intCapacity)){ //eventManager.hasCapacityConflict(intCapacity, eventManager.getRoom(Integer.parseInt(room)))
                    scheduleP.printInvalidCapacity();
                    ValidCapacity = false;
                } else {
                    ValidCapacity = true;
                }
            } catch (Exception e){
                scheduleP.printInvalidCapacity();
            }

        } while (!ValidCapacity);


        int i = 1;
        ArrayList<String> speakers = new ArrayList<>();
        while(i <= numberOfSpeakers) {
            String speaker;
            boolean ValidSpeaker;
            do {
                Scanner scanS = new Scanner(System.in);
                scheduleP.displaySpeakerList(userManager.getSpeakerList(), eventTime, intDuration, speakers);
                if(numberOfSpeakers == 2){
                    switch (i){
                        case 1:
                            scheduleP.printFirstSpeakerPrompt();
                            break;
                        case 2:
                            scheduleP.printSecondSpeakerPrompt();
                            break;
                    }
                }
                speaker = scanS.nextLine();
                if (speaker.equals("0")) {
                    return;
                } else if (!eventManager.getAvailableSpeakers(userManager.getSpeakerList(), eventTime,
                        intDuration).contains(speaker)
                        || speakers.contains(speaker)) {
                    scheduleP.printFailSpeaker();
                    ValidSpeaker = false;
                } else {
                    ValidSpeaker = true;
                    speakers.add(speaker);
                    i++;
                }
            } while (!ValidSpeaker);
        }

        this.callAddEvent(name.trim(), speakers, eventTime, room, intDuration,
                Integer.parseInt(capacity));
    }

    /**
     * Calls SchedulePresenter to prompt the Organizer to change the Event's capacity.
     */
    public void changeEventCapacity() {
        String inputEvent;
        boolean ValidEvent;
        if (eventManager.getEventList().isEmpty()) {
            signUpP.displayEventList(eventManager.getEventList(), eventManager);
            return;
        }
        do {
            Scanner scan1 = new Scanner(System.in);
            signUpP.displayEventList(eventManager.getEventList(), eventManager);
            scheduleP.printChangeCapacityEvent();
            inputEvent = scan1.nextLine();
            if (inputEvent.equals("0")){
                return;
            } else if (!eventManager.getEvent(inputEvent)) {
                scheduleP.printNoEvent();
                ValidEvent = false;
            } else {
                ValidEvent = true;
            }
        } while (!ValidEvent);

        int roomNumber = eventManager.getRoomByEvent(inputEvent);
        int roomCapacity = roomManager.getRoomCapacity(roomNumber);

        String inputCapacity;
        boolean ValidCapacity = false;
        do {
            Scanner scan2 = new Scanner(System.in);
            scheduleP.printChangeCapacity(inputEvent, roomCapacity);
            inputCapacity = scan2.nextLine();
            try {
                int intCapacity = Integer.parseInt(inputCapacity);
                if (inputCapacity.equals("0")) {
                    return;
                } else if (intCapacity == eventManager.getCapacityByEvent(inputEvent)){
                    scheduleP.printCapacityMatch();
                } else if (roomManager.hasCapacityConflict(roomNumber, intCapacity)) {
                    roomP.printAboveRoomCapacity();
                    ValidCapacity = false;
                } else if (intCapacity < eventManager.getNumberOfAttendeesByEvent(inputEvent)) {
                    scheduleP.printNewCapacityBelowCurrentAttendees();
                } else {
                    eventManager.changeCapacity(inputEvent, intCapacity);
                    ValidCapacity = true;
                }
            } catch (Exception e) {
                scheduleP.printInvalidCapacity();
            }
        } while (!ValidCapacity);
        scheduleP.printChangeSuccess();
    }

    /**
     * Calls SchedulePresenter to receive a list of events and
     * prompt the user to input the desired event they wish to
     * delete from this conference.
     */
    public void deleteEventFromConference() {
        String inputEvent;
        boolean validEvent;
        do {
            Scanner scan1 = new Scanner(System.in);
            signUpP.displayEventList(eventManager.getEventList(), eventManager);
            scheduleP.printDeletePrompt();
            inputEvent = scan1.nextLine();
            if (inputEvent.equals("0")){
                return;
            } else if (!eventManager.getEvent(inputEvent)) {
                scheduleP.printNoEvent();
                validEvent = false;
            } else {
                validEvent = true;
            }
        } while (!validEvent);

        boolean hasBeenDeleted;

        if (eventManager.getEvent(inputEvent) && !eventManager.getEventAttendees(inputEvent).isEmpty()) {
            Scanner scan2 = new Scanner(System.in);
            scheduleP.printAttendeesExist();
            if (!scan2.nextLine().equals("1"))
                return;
        }

        int room = eventManager.getRoomByEvent(inputEvent);
        LocalDateTime dateTime = eventManager.getStartDateTimeByEvent(inputEvent);

        hasBeenDeleted = eventManager.deleteConferenceEvent(inputEvent);

        if (hasBeenDeleted) {
            roomManager.removeScheduleTime(room, dateTime);
            scheduleP.printDeletionSuccess();
        }
        else {
            scheduleP.printDeletionFailure();
        }
    }

    /**
     * Calls the addEvent() method of EventManager and calls SchedulePresenter to print
     * if a event is successfully created.
     *
     * @param name - name of the event wanted to be created (receive from UI)
     * @param speakers - name(s) of the speaker(s) of the event wanted to be created (receive from UI)
     * @param time - the occurring time of the event wanted to be created (receive from UI)
     * @param room - the occurring room of the event wanted to be created (receive from UI)
     * @param duration - the duration of the event wanted to be created (receive from UI)
     * @param capacity - the capacity of the event wanted to be created (receive from UI)
     */
    public void callAddEvent(String name, ArrayList<String> speakers,
                             LocalDateTime time, int room, int duration, int capacity){
        // create a speaker account if this speaker haven't have an account yet.
        for (String s: speakers) {
            if (!userManager.isOfType(s, "Speaker")) {
                scheduleP.createEventResult(false, name, speakers, time, room, duration, capacity);
                return;
            }
        }

        eventManager.addEvent(name, speakers, time, room, duration, capacity);
        roomManager.addScheduleTime(room, time, duration);
        scheduleP.createEventResult(true, name, speakers, time, room, duration, capacity);
    }

    /**
     * Prints that the scheduling system has been exited.
     */
    public void endScheduling(){
        scheduleP.printEndScheduling();
    }

    /**
     * Prints that the user has put an invalid option in for the Menu.
     */
    public void failScheduleMenu(){
        scheduleP.printFailScheduleMenu();
    }
}

