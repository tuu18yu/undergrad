package Controller;

import Presenter.SignUpPresenter;
import UseCase.EventManager;
import UseCase.UserManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * A controller class that calls EventManager and UserManager
 * to manage any command related to signing up events.
 * And calls SignUpPresenter's printing methods to show information
 * to the user.
 *
 * @author An Yen, Kelly Le, Filip Jovanovic
 */
public class SignUpController {
    private final EventManager eventManager;
    private final UserManager userManager;
    private final SignUpPresenter sp = new SignUpPresenter();

    /**
     * Creates a instance of ScheduleController with EventManager and UserManager as parameters.
     * @param em - EventManager instance
     * @param um - UserManager instance
     */
    public SignUpController(EventManager em, UserManager um){
        this.eventManager = em;
        this.userManager = um;
    }

    /**
     * Calls SignUpPresenter to print out the menu of Sign Up System.
     * Gets the input of the user and returns a string of the option
     * the user entered.
     *
     * @return - String, option the user entered.
     */
    public String getMenu(){
        Scanner scan = new Scanner(System.in);
        sp.printMenu();
        return scan.nextLine();
    }

    /**
     * Allows user to see events in different ways. Including:
     * see all events, see registered events, see events of a date,
     * see events of a speaker, and see events that start on a time.
     *
     * @param username - the username of the user
     */
    public void seeEventMenu(String username){
        String option;
        do {
            Scanner scan = new Scanner(System.in);
            sp.printSeeEventMenu();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    this.getEventList();
                    break;
                case "2":
                    this.getRegisteredEventList(username);
                    break;
                case "3":
                    this.getEventListByDate();
                    break;
                case "4":
                    this.getEventListBySpeaker();
                    break;
                case "5":
                    this.getEventByStartTime();
                    break;
                case "6":
                    this.getMyEvents(username);
                case "7":
                    break;
                default:
                    sp.printInvalidInput();
            }
        }while (!option.equals("7"));
    }

    /**
     * Calls SignUpPresenter to print out all events.
     */
    public void getEventList(){
        sp.displayEventList(eventManager.getEventList(), eventManager);
    }

    /**
     * Calls SignUpPresenter to print out the registered events
     * of this user.
     * @param username - the user's username
     */
    public void getRegisteredEventList(String username){
        sp.displayRegisteredEvents(userManager.getRegisteredEvents(username), eventManager);
    }

    /**
     * Receive input date from the user, check if the date is valid, and
     * calls SignUpPresenter to print out the events of the given date.
     */
    public void getEventListByDate(){
        boolean validDate = false;
        String inputDate;
        do {
            Scanner scan1 = new Scanner(System.in);
            sp.printEnterDatePrompt();
            inputDate = scan1.nextLine();
            if (inputDate.equals("0")){
                return;
            }
            LocalDate localDate;
            try{
                localDate = LocalDate.parse(inputDate);
                if (localDate.compareTo(LocalDate.now()) < 0) {
                    sp.printTimePastPrompt();
                    validDate = false;
                }else {
                    validDate = true;
                }
            }catch (Exception e){
                sp.printInvalidDateFormat();
            }
        }while (!validDate);
        LocalDateTime date = LocalDateTime.parse(inputDate + "T" +"00:00:00");
        sp.displayEventsByDate(eventManager, date);
    }


    /**
     * Receive input speaker name from the user, check if the speaker is valid, and
     * calls SignUpPresenter to print out the events of the given speaker.
     */
    public void getEventListBySpeaker(){
        boolean validSpeakerName;
        String name;
        do{
            Scanner scan = new Scanner(System.in);
            sp.printAvailableSpeakers(userManager);
            sp.printEnterSpeakerPrompt();
            name = scan.nextLine();
            if(name.equals("0")){
                return;
            }
            if (!userManager.getSpeakerList().contains(name)){
                sp.printInvalidSpeakerName();
                validSpeakerName = false;
            } else{
                sp.displayEventsBySpeakers(eventManager, name, "no");
                validSpeakerName = true;
            }
        } while (!validSpeakerName);
    }

    /**
     * Calls SignUpPresenter to print out the events of the user if the
     * user is a speaker. Otherwise calls SignUpPresenter to show invalid
     * messages.
     *
     * @param userName - the name of the user
     */
    public void getMyEvents(String userName){
        if (userManager.getSpeakerList().contains(userName)) {
            sp.displayEventsBySpeakers(eventManager, userName, "yes");
        }else{
            sp.youAreNotASpeaker();
        }
    }

    /**
     * Receive input time from the user, check if the time is valid, and
     * calls SignUpPresenter to print out the events of the given speaker.
     */
    private void getEventByStartTime(){
        boolean validTime;
        String inputTime;
        do{
            Scanner scan = new Scanner(System.in);
            sp.printAvailableTimes(eventManager);
            sp.printEnterTimePrompt();
            inputTime = scan.nextLine();
            if(inputTime.equals("0")){
                return;
            }
            if(!eventManager.getStartTimes().contains(inputTime)){
                sp.printInvalidInput();
                validTime = false;
            }else{
                sp.displayEventsByTime(eventManager, inputTime);
                validTime = true;
            }
        } while(!validTime);
    }

    /**
     * Calls EventManager to remove the past events.
     */
    public void callRemovePastEvents(){
        eventManager.removePastEvents();
    }

    /**
     * Calls SignUpPresenter to print out all events, then asks the user
     * to enter the name of the event he/she wanted to sign up for,
     * or enter 0 if don't want to sign up any event.
     * After receiving the input from user, SignUpPresenter will be called
     * to print the sign up result on the UI.
     *
     * @param username - the user's username
     */
    public void signUpEvent(String username){
        boolean validEventName = false;
        String eventName;
        boolean isSpeaker = userManager.getSpeakerList().contains(username);
        do {
            do {
                Scanner scan1 = new Scanner(System.in);
                //prints the events
                this.getEventList();
                sp.printSignUpEventPrompt();
                eventName = scan1.nextLine();
                //back to Sign Up System Menu
                if (eventName.equals("0"))
                    return; //back to Sign Up System Menu
                if (!eventManager.getEvent(eventName)) {
                    sp.printInvalidEventName(); //invalid event name
                } else if (isSpeaker && eventManager.getEventListBySpeaker(username).contains(eventName)){
                    sp.printYouAreTheSpeaker();
                } else {
                    validEventName = true;
                }
            } while (!validEventName);
            if(eventManager.getEvent(eventName)){
                if(eventManager.addUserToEvent(username, eventName)){
                    userManager.addRegisteredEvent(username,eventName);
                    sp.printSignUpSuccess();
                } else{
                    sp.printSignUpFail();
                    validEventName = false;
                }
            }
        }while(!validEventName);
        //back to Sign Up System Menu
    }

    /**
     * Calls SignUpPresenter to print out registered events of this user,
     * then asks the user to enter the name of the event he/she wanted to delete
     * from the registered events, or enter 0 if don't want to delete any event.
     * After receiving the input from user, SignUpPresenter will be called to print
     * the deletion result on the UI.
     *
     * @param username - the user's username
     */
    public void deleteEvent(String username) {
        boolean registered = false;
        String eventName;
        do {
            Scanner scan1 = new Scanner(System.in);
            //prints registered events
            this.getRegisteredEventList(username);
            sp.printDeleteEventPrompt();
            eventName = scan1.nextLine();
            //back to Sign Up System Menu
            if (eventName.equals("0"))
                return;
            //check if this user has registered for this event
            if (!userManager.getRegisteredEvents(username).contains(eventName)) {
                sp.printNotInRegisteredEvent();
            }else{
                registered = true;
            }
        }while (!registered);
        //check if this user can delete this event
        if (eventManager.deleteUserFromEvent(username, eventName)){
            userManager.getRegisteredEvents(username).remove(eventName);
            sp.printDeleteEventSuccess();
            return; //back to Sign Up System Menu
        }
        sp.printDeleteEventFail();
        //back to Sign Up System Menu
    }

    /**
     * Shows the message about ending the Sign Up System
     */
    public void signUpSystemEnd(){
        sp.printEndSignUpSystem();
    }

    /**
     * Shows the message about getting invalid input
     */
    public void InvalidInput(){
        sp.printInvalidInput();
    }
}