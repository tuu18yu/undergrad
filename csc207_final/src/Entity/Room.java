package Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 * An entity class that represents a room.
 *
 * @author Filip Jovanovic
 */
public class Room implements Serializable {
    private TreeMap<LocalDateTime, LocalDateTime> schedule = new TreeMap<>();
    private int roomNumber;
    private int capacity;
    private int squareFootage;
    private int screens;
    private boolean soundSystem;
    private boolean stage;
    private boolean accessible;
    private boolean wifi;
    private String specialFeatures;
    private String description;

    /**
     * Gets the Room's schedule.
     * @return TreeMap with start times as keys and end times as values
     */
    public TreeMap<LocalDateTime, LocalDateTime> getSchedule() {
        return schedule;
    }

    /**
     * Sets the Room's schedule.
     * @param schedule the schedule as a TreeMap\<LocalDateTime, LocalDateTime\>
     *                with start times as keys and end times as values
     */
    public void setSchedule(TreeMap<LocalDateTime, LocalDateTime> schedule) {
        this.schedule = schedule;
    }

    /**
     * Adds a start time and end time to the dictionary corresponding to a time range for an event, given the event
     * start time and the event duration.
     * @param startTime the start time of the event
     * @param duration the duration of the event
     */
    public void addToSchedule(LocalDateTime startTime, int duration) {
        LocalDateTime endTime = startTime.plusHours(duration);
        schedule.put(startTime, endTime);
    }

    /**
     * Removes a start time and end time from the dictionary corresponding to a time range for an event, given the event
     * start time.
     * @param startTime the start time of the event
     */
    public void removeFromSchedule(LocalDateTime startTime) {
        schedule.remove(startTime);
    }

    /**
     * Gets the room's number.
     * @return the room number.
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the room's number.
     * @param roomNumber the room number
     */
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Gets the room's capacity.
     * @return the room capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the room's capacity.
     * @param capacity the room capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the room's square footage.
     * @return the room square footage
     */
    public int getSquareFootage() {
        return squareFootage;
    }

    /**
     * Sets the room's square footage.
     * @param squareFootage the room square footage
     */
    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    /**
     * Gets the room's number of screens.
     * @return the room number of screens
     */
    public int getScreens() {
        return screens;
    }

    /**
     * Sets the room's number of screens.
     * @param screens the room number of screens
     */
    public void setScreens(int screens) {
        this.screens = screens;
    }

    /**
     * Gets if the room has a sound system or not.
     * @return true if the room has a sound system, false otherwise
     */
    public boolean hasSoundSystem() {
        return soundSystem;
    }

    /**
     * Sets if the room has a sound system or not.
     * @param soundSystem boolean that is true if the room has a sound system and false otherwise.
     */
    public void setSoundSystem(boolean soundSystem) {
        this.soundSystem = soundSystem;
    }

    /**
     * Gets if the room has a stage or not.
     * @return true if the room has a stage, false otherwise
     */
    public boolean hasStage() {
        return stage;
    }

    /**
     * Sets if the room has a stage or not.
     * @param stage boolean that is true if the room has a stage and false otherwise.
     */
    public void setStage(boolean stage) {
        this.stage = stage;
    }

    /**
     * Gets if the room is accessible for people with disabilities.
     * @return true if the room is accessible, false otherwise
     */
    public boolean isAccessible() {
        return accessible;
    }

    /**
     * Sets if the room is accessible for people with disabilities or not.
     * @param accessible boolean that is true if the room is accessible and false otherwise.
     */
    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    /**
     * Gets if the room has WiFi or not.
     * @return true if the room has Wifi, false otherwise
     */
    public boolean hasWifi() {
        return wifi;
    }

    /**
     * Sets if the room has WiFi or not.
     * @param wifi boolean that is true if the room has WiFi and false otherwise.
     */
    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    /**
     * Gets the special features of the room.
     * @return the room special features
     */
    public String getSpecialFeatures() {
        return specialFeatures;
    }

    /**
     * Sets the special features of the room.
     * @param specialFeatures the room special features
     */
    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    /**
     * Gets the description of the room.
     * @return the room description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the room.
     * @param description the room description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
