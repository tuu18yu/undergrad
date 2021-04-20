package Presenter;

import Entity.Event;
import UseCase.EventManager;
import UseCase.UserManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A presenter class that prints information on UI that directs the user
 * and shows result to the user during all sign up event ans delete event
 * processes.
 *
 * @author An Yen
 */
public class SignUpPresenter {

    /**
     * Prints the menu of Sign Up System.
     */
    public void printMenu(){
        System.out.println("Welcome to the Sign Up Menu! \n" +
                "Options:\n" +
                "1. Sign up for an event\n" +
                "2. Delete a registered event\n" +
                "3. See events\n" +
                "4. Exit Sign Up Menu\n" +
                "Enter 1, 2, 3, or 4: ");
    }

    /**
     * Prints all events. Each line represents one event and is in the
     * sequence: "Name, Time, Speaker, Room Number, Capacity, Available/Full".
     *
     * @param eventList - an arraylist of events
     */
    public void displayEventList(ArrayList<Event> eventList, EventManager em){
        String events = em.eventListGenerator(eventList);
        if(events.equals("")){
            System.out.println("There are no available events yet...");
        }else {
            System.out.println("All Events: ");
            System.out.println(events);
        }
    }

    /**
     * Prints message that asks the user to enter an event name that
     * he/she wants to sign up for or 0 to exit
     */
    public void printSignUpEventPrompt(){
        System.out.println("Enter the event name you want to sign up for " +
                "(or enter 0 to return to the Sign Up menu): ");
    }

    /**
     * Prints the events whose name are in the given arraylist
     * Each line represents one event and is in the sequence:
     * "Name, Time, Speaker, Room Number, Capacity, Available/Full".
     *
     * @param registeredEventNames - an arraylist of event names
     * @param em - an instance of EventManager
     */
    public void displayRegisteredEvents(ArrayList<String> registeredEventNames, EventManager em) {
        ArrayList<Event> registeredEvents;
        if (registeredEventNames.isEmpty()){
            System.out.println("You didn't sign up for any events.");
        }else {
            registeredEvents = em.getRegisteredEvents(registeredEventNames);
            String events = em.eventListGenerator(registeredEvents);
            System.out.println("Registered Events: ");
            System.out.println(events);
        }
    }

    /**
     * Prints the events that occurs on the given date.
     *
     * @param em - an EventManager instance
     * @param date - given date
     */
    public void displayEventsByDate(EventManager em, LocalDateTime date){
        String events = em.eventListGenerator(em.getEventByDate(date));
        if (events.isEmpty()){
            System.out.println("There are no events on "
                    + date.getYear() + "-"
                    + date.getMonthValue() + "-"
                    + date.getDayOfMonth() + ".");
        } else {
            System.out.println("Events on "
                    + date.getYear() + "-"
                    + date.getMonthValue() + "-"
                    + date.getDayOfMonth() + ":");
            System.out.println(events);
        }
    }

    /**
     * Prints the events of the given speaker.
     *
     * @param em - an EventManager instance
     * @param speakerName - the given speaker's name
     */
    public void displayEventsBySpeakers(EventManager em, String speakerName, String isMyself) {
        String events = em.eventListGenerator(em.getEventBySpeaker(speakerName));
        if (events.isEmpty()){
            switch (isMyself) {
                case "no":
                    System.out.println("Speaker " + speakerName + " has no events yet.");
                    break;
                case "yes":
                    System.out.println("You have no events yet.");
                    break;
            }
        } else {
            switch (isMyself){
                case "no":
                    System.out.println("Events of speaker " + speakerName + ":");
                    break;
                case "yes":
                    System.out.println("Your events: ");
            }
            System.out.println(events);
        }
    }

    /**
     * Prints the events that starts on the given time.
     *
     * @param em - an EventManager instance
     * @param time - the given time in hour
     */
    public void displayEventsByTime(EventManager em, String time){
        String events = em.eventListGenerator(em.getEventByTime(time));
        if (events.isEmpty()){
            System.out.println("There are no events on time " + time);
        } else {
            System.out.println("Events on time " + time + ":");
            System.out.println(events);
        }
    }

    /**
     * Prints message that asks the user to enter an event name that
     * he/she wants to delete or 0 to exists
     */
    public void printDeleteEventPrompt(){
        System.out.println("Enter the event name you want to delete (or enter 0 to return to the Sign Up menu): ");
    }

    /**
     * Prints "Sign up success!".
     */
    public void printSignUpSuccess(){
        System.out.println("Sign up success!");
    }

    /**
     * Prints "You cannot sign up for this event. Sign up failed...".
     */
    public void printSignUpFail(){
        System.out.println("You've already signed up for this event or this event is full. Sign up failed...");
    }

    /**
     * Prints "Event successfully deleted!".
     */
    public void printDeleteEventSuccess() {
        System.out.println("Event successfully deleted!");
    }

    /**
     * Prints "Deletion failed.".
     */
    public void printDeleteEventFail(){
        System.out.println("Deletion failed.");
    }

    /**
     * Prints "You didn't sign up for this event."
     */
    public void printNotInRegisteredEvent(){
        System.out.println("You didn't sign up for this event.");
    }

    /**
     * Prints "End of Sign Up System."
     */
    public void printEndSignUpSystem(){
        System.out.println("You have exited the Sign Up Menu.");
    }

    /**
     * Prints "This event doesn't exist."
     */
    public void printInvalidEventName(){
        System.out.println("This event doesn't exist.");
    }

    /**
     * Prints "Invalid input, please try again."
     */
    public void printInvalidInput(){
        System.out.println("Invalid input, please try again.");
    }

    /**
     * Prints message that tells the user to enter a date.
     */
    public void printEnterDatePrompt(){
        System.out.println("Enter a date in the format yyyy-mm-dd or enter 0 to exist\n" +
                "ex. 2020-05-30");
    }

    /**
     * Prints "The date you entered has past. Please try again."
     */
    public void printTimePastPrompt() {
        System.out.println("The date you entered has past. Please try again.");
    }

    /**
     * Prints "Invalid date format.Please try again."
     */
    public void printInvalidDateFormat() {
        System.out.println("Invalid date format.Please try again.");
    }

    /**
     * Prints the options of ways to see events.
     */
    public void printSeeEventMenu() {
        System.out.println("Options:\n" +
                "1. See all events\n" +
                "2. See registered events\n" +
                "3. See events of one day\n" +
                "4. See events of a Speaker\n" +
                "5. See events of a starting time on all days\n" +
                "6. See my events (only for Speakers)\n" +
                "7. Back to Sign Up Menu\n" +
                "Enter 1, 2, 3, 4, 5, 6 or 7: ");
    }

    /**
     * Prints all existing speakers.
     *
     * @param um - an UserManager instance
     */
    public void printAvailableSpeakers(UserManager um) {
        if (um.getSpeakerList().isEmpty()){
            System.out.println("There are no speakers yet.");
        } else{
            System.out.println("All speakers:");
            System.out.println(um.getSpeakerList());
        }

    }

    /**
     * Prints message that tells the user to enter a speaker name.
     */
    public void printEnterSpeakerPrompt() {
        System.out.println("Which speaker's events do you want to see?\n" +
                " (Enter a speaker's name or 0 to exist)");
    }

    /**
     * Prints "The speaker you entered doesn't exist. Please try again."
     */
    public void printInvalidSpeakerName() {
        System.out.println("The speaker you entered doesn't exist. Please try again.");
    }

    /**
     * Prints all times that is available for events to start.
     *
     * @param em - an EventManager instance
     */
    public void printAvailableTimes(EventManager em) {
        System.out.println("Available times: ");
        int i = 0;
        for (String s: em.getStartTimes()){
            System.out.print(s);
            i++;
            if (em.getStartTimes().size() != i){
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Prints message that tells the user to enter a time.
     */
    public void printEnterTimePrompt() {
        System.out.println("Enter an available time or enter 0 to exist\n" +
                "ex. 09 for 9AM, 12 for noon, 16 for 4PM");
    }

    /**
     * Prints message that shows the speaker user not to sign up
     * for its own event.
     */
    public void printYouAreTheSpeaker() {
        System.out.println("You cannot sign up for your own event. Please select other events.");
    }

    /**
     * Prints "You are not a speaker."
     */
    public void youAreNotASpeaker() {
        System.out.println("You are not a speaker.");
    }
}