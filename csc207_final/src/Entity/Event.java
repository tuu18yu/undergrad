package Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A class that represents an event.
 * @author An Yen
 */
public class Event implements Serializable {
    private String name; // name of event
    private ArrayList<String> speaker; // an arraylist of name(s) of speaker(s)
    private LocalDateTime time; //time of the event
    private final ArrayList<String> attendees; // names of the attendees
    private final int room; // room number
    private final int duration; // the duration of this event
    private int capacity; // the capacity of this event

    /**
     * The name, ID, occurring time, occurring place, and speaker required to create an event.
     * @param name- the name of this event
     * @param speaker- the name(s) of the speaker(s)
     * @param time- the occurring time of this event
     * @param room- the occurring room number of this event
     * @param duration - the duration of this event
     * @param capacity - the capacity of this event
     */
    public Event (String name, ArrayList<String> speaker, LocalDateTime time, int room,
                  int duration, int capacity) {
        this.name = name;
        this.speaker = speaker;
        this.time = time;
        this.room = room;
        this.attendees = new ArrayList<>();
        this.duration = duration;
        this.capacity = capacity;
    }

    /**
     * Return the name of this event.
     * @return the name of this Event
     */
    public String getName() {
        return name;
    }

    /**
     Return the name(s) of the speaker(s) of this event.
     * @return the name(s) of the Speaker(s) of this Event
     */
    public ArrayList<String> getSpeaker() {
        return speaker;
    }

    /**
     * Returns the occurring time of this event.
     * @return the occurring time of this Event
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Returns the occurring room number of this event.
     * @return the occurring room number of this Event
     */
    public Integer getRoomNum() {
        return room;
    }

    /**
     * Returns the duration in hours of this event
     * @return the duration of this event
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the maximum capacity of people for this event.
     */
    public int getCapacity() { return capacity; }

    /**
     * This method sets the attendee list of this event.
     * @param attendeeName- the attendee's name
     */
    public void addAttendee(String attendeeName) {
        this.attendees.add(attendeeName);
    }

    /**
     * Return the list of attendees
     * @return the list of attendees
     */
    public ArrayList<String> getAttendees() {return attendees;}

    /**
     * This method sets the name of this event.
     * @param name- the name of this event
     */
    public void setName(String name) {this.name = name; }

    /**
     * This method assign a speaker to this event by the speaker's name.
     * @param speaker- the arraylist of the name(s) of the speaker(s)
     */
    public void setSpeaker(ArrayList<String> speaker) {
        this.speaker = speaker;
    }

    /**
     * This method sets the occurring time of this event.
     * @param time- the time of this event
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * This method sets the capacity of this event.
     *
     * @param capacity the capacity of the event.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * This method returns the current number of attendees of the event.
     * This number includes the speakers at the event.
     *
     * @return number of attendees and speakers attending the event.
     */
    public int getNumberOfAttendees() {
        return attendees.size() + speaker.size();
    }
}