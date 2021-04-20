package UseCase;

import Entity.Event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class stores a list of existing events, legal starting
 * time, legal ending time and manages every event related command.
 *
 * @author Kelly Le, Filip Jovanovic, An Yen
 */
public class EventManager implements Serializable {

    private ArrayList<Event> eventList = new ArrayList<>();

    /**
     * Returns the starting time of this conference
     *
     * @return the startTime of this conference
     */
    public int getStartTime() {
        // the opening time of this conference is 9 am.
        return 9;
    }

    /**
     * Returns the ending time of this conference.
     *
     * @return the endTime of this conference
     */
    public int getEndTime() {
        // the ending time of this conference is 5 pm.
        return 17;
    }

    /**
     * Return a list of all possible start times.
     *
     * @return an ArrayList of strings of start times.
     */
    public ArrayList<String> getStartTimes () {
        ArrayList<String> availableTimes = new ArrayList<>();
        int startTime = this.getStartTime();
        while (startTime < this.getEndTime()){
            if (startTime < 10){
                availableTimes.add("0" + startTime);
            } else {
                availableTimes.add(Integer.toString(startTime));
            }
            startTime += 1;
        }
        return availableTimes;
    }

    /**
     * Returns the list of events.
     *
     * @return event list
     */
    public ArrayList<Event> getEventList() {
        return eventList;
    }

    /**
     * Returns an event that is created.
     *
     * @param name - the name of this event
     * @param speaker - the name(s) of the speaker(s)
     * @param time - the occurring time of this event
     * @param room - the occurring room of this event
     * @param duration - the duration of this event
     * @param capacity - the capacity of this event
     * @return the Event that is created
     */
    private Event createNewEvent(String name, ArrayList<String> speaker,
                                 LocalDateTime time, int room, int duration, int capacity) {
        return new Event(name, speaker, time, room, duration, capacity);
    }

    /**
     * Returns true if the event name exists in the event list.
     *
     * @param name - the name of the event
     *
     * @return true if the event name exists
     */
    public boolean getEvent(String name){
        if (this.getEventList() == null || this.getEventList().isEmpty()){
            return false;
        }

        for (Event e: this.getEventList()) {
            if (e.getName().toLowerCase().trim().equals(name.toLowerCase().trim())){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the event instance if the event name exists in the event list.
     * Else returns null.
     * @param name - the event name
     * @return the Event instance with the corresponding event name
     */
    private Event findEventByName(String name){
        for (Event e: eventList){
            if(e.getName().equals(name)){
                return e;
            }
        }
        return null;
    }

    /**
     * Creates an event and is added to the conference.
     * Passes silently if event was not able to be made.
     * Checks if the speaker gives another talk at the same time and if the event name already exists.
     *
     * @param name - the name of this event
     * @param speaker - the name(s) of the speaker(s)
     * @param time - the occurring time of this event
     * @param room - the occurring room of this event
     * @param duration - the duration of this event
     * @param capacity - the capacity of this event
     */
    public void addEvent(String name, ArrayList<String> speaker,
                         LocalDateTime time, int room, int duration, int capacity) {
        Event newEvent = createNewEvent(name, speaker, time, room, duration, capacity);
        if(eventList == null || eventList.isEmpty()) {
            this.eventList = new ArrayList<>();
            this.eventList.add(newEvent);
        }
        for (Event e : eventList) {
            if ((e.getTime() == time && e.getSpeaker().equals(speaker)) || e.getName().equals(name)) {
                return;
            }
        }
        ArrayList<Event> sortedEvents = new ArrayList<>();
        boolean Added = false;
        for (Event e : eventList) {
            if (e.getTime().isAfter(newEvent.getTime()) && !Added) {
                sortedEvents.add(newEvent);
                sortedEvents.add(e);
                Added = true;
            } else {
                sortedEvents.add(e);
            }
        }
        if (!Added){
            sortedEvents.add(newEvent);
        }
        this.eventList = sortedEvents;

    }

    /**
     * Decide whether this user can sign up for this event or not.
     * If the user is already in this event, or this user had signed up
     * another event that occurs at the same time as this event, or this
     * event is already fulled, or this user is the speaker of this event
     * this user cannot sign up for this event and this method returns false.
     * Otherwise the user can sign up for this event and this method
     * returns true.
     *
     * @param username - the username of this user.
     * @param eventName - the event's name the user wanted to sign up for.
     * @return true is the user can sign up for this event. Otherwise false.
     */
    private boolean canAddUserToEvent(String username, String eventName){
        Event event = findEventByName(eventName);
        assert event != null;
        if (event.getSpeaker().contains(username)){
            return false;
        }
        if (event.getCapacity() == (event.getAttendees().size())) {
            return false;
        }
        for (Event e: eventList){
            if(event.getTime().equals(e.getTime())) {
                if(e.getAttendees().contains(username)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if the user is added successfully to
     * this event, and returns false if fails to add this
     * user to this event.
     *
     * @param username - the name of the user
     * @param eventName - the event's name the user wanted to sign up for.
     * @return true if this user is added successfully to the event, otherwise false.
     */
    public boolean addUserToEvent(String username, String eventName){
        Event event = findEventByName(eventName);
        if (canAddUserToEvent(username, eventName)){
            assert event != null;
            event.addAttendee(username);
            return true;
        }
        return false;
    }

    /**
     * Returns true if the user is successfully deleted from
     * this event, and returns false if fails to delete this
     * user from this event (not in event list).
     *
     * @param username - the name of the user
     * @param eventName - the event's name the user wanted to sign up for.
     * @return true if this user is successfully deleted from the event, otherwise false.
     */
    public boolean deleteUserFromEvent(String username, String eventName){
        for (Event e: this.eventList){
            if (e.getName().equals(eventName)) {
                if (e.getAttendees().contains(username)) {
                    return e.getAttendees().remove(username);
                }
            }
        }
        return false;
    }

    /**
     * Returns true if an event has a time conflict with the given starting time and
     * the given duration. Returns false if there are no time conflict.
     * Two events will have time conflict when occurs on the same day and
     * (1) They have the same starting time.
     * or
     * (2) One starts while another hasn't ended yet.
     *
     * @param e - an Event instance
     * @param time - a starting time of an event that we want to compare
     * @param duration - the duration of an event in hours
     *
     * @return - true if there is a time conflict, and false when there's no time conflict
     */
    private boolean hasTimeConflict(Event e, LocalDateTime time, int duration){
        if (e.getTime().getYear() == time.getYear() &&
                e.getTime().getMonthValue() == time.getMonthValue() &&
                e.getTime().getDayOfMonth() == time.getDayOfMonth()){
            int startHour = time.getHour();
            int endHour = time.getHour() + duration;
            int eStartHour = e.getTime().getHour();
            int eEndHour = e.getTime().getHour() + e.getDuration();
            if (eStartHour == startHour){
                return true;
            }
            else if((eStartHour < startHour && startHour < eEndHour)){
                return true;
            }
            else return startHour < eStartHour && eStartHour < endHour;
        }
        return false;
    }

    /**
     * Return a list of Speakers available at the given time.
     *
     * @param speakerList - the list of existing speakers
     * @param time - the chosen time
     * @param duration - the duration time in hours of an event
     *
     * @return a list of available Speakers
     */
    public ArrayList<String> getAvailableSpeakers(ArrayList<String> speakerList, LocalDateTime time, int duration) {
        ArrayList<String> availableList = new ArrayList<>();
        ArrayList<String> unavailableList = new ArrayList<>();
        if (this.eventList != null) {
            for (Event e : this.eventList) {
                if(hasTimeConflict(e, time, duration)){
                    unavailableList.addAll(e.getSpeaker());
                }
            }
        }

        for (String s : speakerList) {
            if (!unavailableList.contains(s)) {
                availableList.add(s);
            }
        }
        return availableList;
    }

    /**
     * Remove the past events from the event list.
     */
    public void removePastEvents(){
        ArrayList<Event> updatedEventList = new ArrayList<>();
        for (Event e: this.eventList){
            if (e.getTime().compareTo(LocalDateTime.now()) > 0){
                updatedEventList.add(e);
            }
        }
        this.eventList = updatedEventList;
    }

    /**
     * Generates a display format of events.
     *
     * @param eventList - an arraylist of events
     * @return a String object that is the display format of events.
     */
    public String eventListGenerator(ArrayList<Event> eventList){
        StringBuilder events = new StringBuilder();
        if (eventList == null){
            return events.toString();
        }
        for (Event e: eventList){
            int space = e.getCapacity() - e.getAttendees().size();
            String available = "Full";
            if (space > 0){
                available = "Available";
            }
            String speakerString;
            if (e.getSpeaker().isEmpty()){
                speakerString = "No Speakers";
            }else{
                speakerString = e.getSpeaker().toString();
            }
            events.append("Name: ").append(e.getName())
                    .append(", Time: ").append(e.getTime().toString())
                    .append(", Duration: ").append(e.getDuration()).append(" hours")
                    .append(", Speaker(s): ").append(speakerString)
                    .append(", Room Number: ").append(e.getRoomNum().toString())
                    .append(", Capacity: ").append(e.getCapacity()).append(", ").append(available).append("\n");
        }
        return events.toString();
    }

    /**
     * Generates a list of registered events
     *
     * @param registeredEventNames - a list of the registered event names
     *
     * @return a list of Events that a User has registered to
     */
    public ArrayList<Event> getRegisteredEvents(ArrayList<String> registeredEventNames){
        ArrayList<Event> registeredEvents = new ArrayList<>();
        for (Event e : eventList) {
            if (registeredEventNames.contains(e.getName())) {
                registeredEvents.add(e);
            }
        }
        return registeredEvents;
    }

    /**
     * Returns list of event names of given Speaker
     *
     * @param username - username of the Speaker
     * @return an ArrayList of String that contains names of events that Speaker gives
     */
    public ArrayList<String> getEventListBySpeaker(String username) {
        ArrayList<String> events = new ArrayList<>();
        for (Event e: eventList) {
            if (e.getSpeaker().contains(username)) {
                events.add(e.getName());
            }
        }
        return events;
    }

    /**
     * Returns list of attendee usernames of Event, given Event name.
     *
     * @param eventName- name of the event
     * @return an ArrayList of String that contains usernames of users attending the Event
     */
    public ArrayList<String> getEventAttendees(String eventName) {
        ArrayList<String> users = new ArrayList<>();
        for (Event e : eventList) {
            if (e.getName().equals(eventName)) {
                users = e.getAttendees();
            }
        }
        return users;
    }

    /**
     * Returns an arraylist of all events that occurs on the given date.
     *
     * @param date - given date
     *
     * @return - ArrayList<Event> that occurs on the given date
     */
    public ArrayList<Event> getEventByDate (LocalDateTime date){
        ArrayList<Event> sameDayEvents = new ArrayList<>();
        for (Event e: eventList){
            if(e.getTime().getYear() == date.getYear()
                    && e.getTime().getMonthValue() == date.getMonthValue()
                    && e.getTime().getDayOfMonth() == date.getDayOfMonth()){
                sameDayEvents.add(e);
            }
        }
        return sameDayEvents;
    }

    /**
     * Returns an arraylist of all events of a given speaker.
     *
     * @param speakerName - the name of the speaker
     * @return - ArrayList<Event> of an speaker
     */
    public ArrayList<Event> getEventBySpeaker(String speakerName){
        ArrayList<Event> sameSpeakerEvents = new ArrayList<>();
        for (Event e: eventList){
            if (e.getSpeaker().contains(speakerName)){
                sameSpeakerEvents.add(e);
            }
        }
        return sameSpeakerEvents;
    }

    /**
     * Returns an arraylist of all events that occurs on the given time of all days.
     *
     * @param time - given time in hour
     *
     * @return - ArrayList<Event> that occurs on the given time
     */
    public ArrayList<Event> getEventByTime(String time){
        ArrayList<Event> eventsAtTime = new ArrayList<>();
        for(Event e: eventList){
            String eventTime;
            int hour = e.getTime().getHour();
            if(hour < 10){
                eventTime = "0" + hour;
            }else{
                eventTime = "" + hour;
            }
            if(eventTime.equals(time)){
                eventsAtTime.add(e);
            }
        }
        return eventsAtTime;
    }

    /**
     * Returns true if the Event was successfully deleted and deletes all attendees from the event.
     *
     * @param eventName - name of event that wants to be deleted
     *
     * @return true if the Event was removed from the event list, false otherwise
     */
    public boolean deleteConferenceEvent(String eventName){
        for (Event e: this.eventList){
            if (e.getName().equals(eventName)) {
                return this.eventList.remove(e);
            }
        }
        return false;
    }

    /**
     * Returns the capacity of an event given the event's name.
     *
     * @param event - the name of the event
     *
     * @return the event's capacity
     */
    public int getCapacityByEvent(String event){
        return Objects.requireNonNull(findEventByName(event)).getCapacity();
    }

    /**
     * Returns the room's number given the event's name.
     *
     * @param event - the name of the event
     *
     * @return the event's room number
     */
    public Integer getRoomByEvent(String event){
        return Objects.requireNonNull(findEventByName(event)).getRoomNum();
    }

    /**
     * Changes the capacity of an Event.
     * If event does not exist, fails silently.
     *
     * @param event - event name
     * @param capacity - the new capacity
     */
    public void changeCapacity(String event, int capacity){
        Event e = findEventByName(event);
        if (e != null)
            e.setCapacity(capacity);
    }

    /**
     * Returns number of attendees of an event given the event name.
     * This number includes the speakers at the event.
     *
     * @param event the name of the event.
     * @return number of attendees and speakers attending the event.
     */
    public int getNumberOfAttendeesByEvent(String event) {
        int numAttendees = -1;

        Event e = findEventByName(event);

        if (e != null) {
            numAttendees = e.getNumberOfAttendees();
        }
        return numAttendees;
    }

    /**
     * Gets an event's starting date and time given the event name.
     *
     * @param event the name of the event.
     * @return starting date and time of the event.
     */
    public LocalDateTime getStartDateTimeByEvent(String event) {
        LocalDateTime dateTime = LocalDateTime.now();

        Event e = findEventByName(event);

        if (e != null)
            dateTime = e.getTime();
        return dateTime;
    }

    /**
     * Returns an arraylist of all speakers of given event namr.
     *
     * @param event - given username of event
     *
     * @return - ArrayList<Event> that contains username of speakers
     */
    public ArrayList<String> getEventSpeakers(String event){
        return findEventByName(event).getSpeaker();
    }
}
