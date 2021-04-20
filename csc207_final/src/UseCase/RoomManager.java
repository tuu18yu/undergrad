package UseCase;

import Entity.Room;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Use Case that deals with how Room entities can be used, and stores a TreeMap of all the currently
 * registered rooms.
 *
 * @author Filip Jovanovic
 */
public class RoomManager implements Serializable {
    private final TreeMap<Integer, Room> roomMap = new TreeMap<>();

    /**
     * Checks if a room exists in roomMap.
     * @param roomNumber the room number
     * @return true if the room exists in roomMap, false otherwise
     */
    public boolean doesRoomExist(int roomNumber) {
        return roomMap.containsKey(roomNumber);
    }

    /**
     * Determines if roomMap is empty.
     * @return true if roomMap is empty, false otherwise
     */
    public boolean isRoomMapEmpty() {
        return roomMap.isEmpty();
    }

    /**
     * Returns the list of all room numbers in the roomMap.
     * @return ArrayList of Integers of the room numbers in roomMap
     */
    public ArrayList<Integer> getRoomNumbers() {
        return new ArrayList<>(roomMap.keySet());
    }

    /**
     * Returns the schedule of the specified room.
     * @param roomNumber the room number
     * @return TreeMap with start times as keys and end times as values
     */
    public TreeMap<LocalDateTime, LocalDateTime> getRoomSchedule(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.getSchedule();
    }

    /**
     * Gets the specified room's capacity.
     * @param roomNumber the room number
     * @return the room capacity
     */
    public int getRoomCapacity(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.getCapacity();
    }

    /**
     * Gets the specified room's square footage.
     * @param roomNumber the room number
     * @return the room square footage
     */
    public int getSquareFootage(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.getSquareFootage();
    }

    /**
     * Gets the specified room's number of screens.
     * @param roomNumber the room number
     * @return the room number of screens
     */
    public int getScreens(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.getScreens();
    }

    /**
     * Gets if the specified room has a sound system.
     * @param roomNumber the room number
     * @return true if the room has a sound system, false otherwise
     */
    public boolean roomHasSoundSystem(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.hasSoundSystem();
    }

    /**
     * Gets if the specified room has a stage.
     * @param roomNumber the room number
     * @return true if the room has a stage, false otherwise
     */
    public boolean roomHasStage(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.hasStage();
    }

    /**
     * Gets if the specified room is accessible to people with disabilities.
     * @param roomNumber the room number
     * @return true if the room is accessible, false otherwise
     */
    public boolean roomIsAccessible(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.isAccessible();
    }

    /**
     * Gets if the specified room has WiFi.
     * @param roomNumber the room number
     * @return true if the room has WiFi, false otherwise
     */
    public boolean roomHasWifi(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.hasWifi();
    }

    /**
     * Gets the specified room's special features.
     * @param roomNumber the room number
     * @return the room special features
     */
    public String getSpecialFeatures(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.getSpecialFeatures();
    }

    /**
     * Gets the specified room's description.
     * @param roomNumber the room number
     * @return the room description
     */
    public String getDescription(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        return room.getDescription();
    }

    /**
     * Creates a new Room object and adds it to the roomMap.
     * @param roomBuilder The RoomBuilder that has the new room's information stored in it
     */
    public void addRoom(RoomBuilder roomBuilder)
    {
        Room room = roomBuilder.build();

        int roomNumber = room.getRoomNumber();
        roomMap.put(roomNumber, room);
    }

    /**
     * Determines if the specified room can be deleted safely.
     * A room can be deleted safely if it has no times in its schedule.
     * @param roomNumber the room number
     * @return true if room can be safely deleted, false otherwise
     */
    public boolean canDeleteRoom(int roomNumber) {
        Room room = roomMap.get(roomNumber);

        return room.getSchedule().isEmpty();
    }

    /**
     * Deletes a room from the roomMap.
     * @param roomNumber the room number
     */
    public void deleteRoom(int roomNumber) {
        roomMap.remove(roomNumber);
    }

    /**
     * Checks if a certain period of time conflicts with times in the specified room's schedule.
     * @param roomNumber the room number
     * @param checkStartTime the start time to be checked
     * @param checkDuration the duration of the period of time
     * @return true if there is a conflict, false otherwise
     */
    public boolean hasTimeConflict(int roomNumber, LocalDateTime checkStartTime, int checkDuration) {
        TreeMap<LocalDateTime, LocalDateTime> schedule = getRoomSchedule(roomNumber);
        LocalDateTime checkEndTime = checkStartTime.plusHours(checkDuration);

        for (Map.Entry<LocalDateTime, LocalDateTime> otherTimeInterval : schedule.entrySet()) {
            LocalDateTime otherStartTime = otherTimeInterval.getKey();
            LocalDateTime otherEndTime = otherTimeInterval.getValue();

            if (checkStartTime.isBefore(otherEndTime) && checkEndTime.isAfter(otherStartTime))
                return true;
        }

        return false;
    }

    /**
     * Gets the room numbers of all rooms that do not have a time conflict with the specified period of time.
     * @param newStartTime the start time of the period of time
     * @param duration the duration of the period of time
     * @return ArrayList of room numbers that are available for the specified period of time
     */
    public ArrayList<Integer> getAvailableRooms(LocalDateTime newStartTime, int duration) {
        ArrayList<Integer> availableRooms = new ArrayList<>();

        for (Map.Entry<Integer, Room> rooms : roomMap.entrySet()) {
            int roomNumber = rooms.getKey();
            boolean conflictExist = this.hasTimeConflict(roomNumber, newStartTime, duration);

            if (!conflictExist) {
                availableRooms.add(roomNumber);
            }
        }

        return availableRooms;
    }

    /**
     * Add a period of time to the specified room's schedule.
     * @param roomNumber the room number
     * @param startTime the start time of the period of time
     * @param duration the duration of the period of time
     */
    public void addScheduleTime(int roomNumber, LocalDateTime startTime, int duration) {
        Room room = roomMap.get(roomNumber);
        room.addToSchedule(startTime, duration);
    }

    /**
     * Remove a period of time to the specified room's schedule.
     * @param roomNumber the room number
     * @param startTime the start time of the period of time
     */
    public void removeScheduleTime(int roomNumber, LocalDateTime startTime) {
        Room room = roomMap.get(roomNumber);
        room.removeFromSchedule(startTime);
    }

    /**
     * Determines if an event's capacity conflicts with the room's capacity.
     * This occurs whenever the event's capacity is larger than the room's capacity.
     * @param roomNumber the room number
     * @param eventCapacity the event's capacity
     * @return true if there is a capacity conflict, false otherwise
     */
    public boolean hasCapacityConflict(int roomNumber, int eventCapacity) {
        return eventCapacity > getRoomCapacity(roomNumber);
    }

    /**
     * Removes all time in the specified room's schedule that are before the current date and time.
     * @param roomNumber the room number
     */
    public void removePastTimes(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        TreeMap<LocalDateTime, LocalDateTime> schedule = room.getSchedule();

        if (schedule.isEmpty())
            return;

        schedule.entrySet().removeIf(entry -> entry.getKey().isBefore(LocalDateTime.now())); // Using predicates
    }
}

