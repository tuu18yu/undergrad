package UseCase;

import Entity.Room;

/**
 * A Builder class for creating new Room objects.
 *
 * @author Filip Jovanovic
 */
public class RoomBuilder {
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
     * Sets this RoomBuilder's roomNumber and returns the RoomBuilder.
     * @param roomNumber the room number
     * @return the RoomBuilder
     */
    public RoomBuilder roomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    /**
     * Sets this RoomBuilder's capacity and returns the RoomBuilder.
     * @param capacity the room capacity
     * @return the RoomBuilder
     */
    public RoomBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    /**
     * Sets this RoomBuilder's square footage and returns the RoomBuilder.
     * @param squareFootage the room square footage
     * @return the RoomBuilder
     */
    public RoomBuilder squareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
        return this;
    }

    /**
     * Sets this RoomBuilder's number of screens and returns the RoomBuilder.
     * @param screens the room number of screens
     * @return the RoomBuilder
     */
    public RoomBuilder screens(int screens) {
        this.screens = screens;
        return this;
    }

    /**
     * Sets if this RoomBuilder has a sound system and returns the RoomBuilder.
     * @param soundSystem if the room has a sound system or not
     * @return the RoomBuilder
     */
    public RoomBuilder soundSystem(boolean soundSystem) {
        this.soundSystem = soundSystem;
        return this;
    }

    /**
     * Sets if this RoomBuilder has a stage and returns the RoomBuilder.
     * @param stage if the room has a stage or not
     * @return the RoomBuilder
     */
    public RoomBuilder stage(boolean stage) {
        this.stage = stage;
        return this;
    }

    /**
     * Sets if this RoomBuilder is accessible for people with disabilities and returns the RoomBuilder.
     * @param accessible if the room is accessible
     * @return the RoomBuilder
     */
    public RoomBuilder accessible(boolean accessible) {
        this.accessible = accessible;
        return this;
    }

    /**
     * Sets if this RoomBuilder has WiFi and returns the RoomBuilder.
     * @param wifi if the room has Wifi or not
     * @return the RoomBuilder
     */
    public RoomBuilder wifi(boolean wifi) {
        this.wifi = wifi;
        return this;
    }

    /**
     * Sets this RoomBuilder's special features and returns the RoomBuilder.
     * @param specialFeatures the room special features
     * @return the RoomBuilder
     */
    public RoomBuilder specialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
        return this;
    }

    /**
     * Sets this RoomBuilder's description and returns the RoomBuilder.
     * @param description the room description
     * @return the RoomBuilder
     */
    public RoomBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Creates the new Room based on the RoomBuilder's variable values and returns the Room.
     * @return the new Room
     */
    public Room build() {
        Room room = new Room();

        room.setRoomNumber(roomNumber);
        room.setCapacity(capacity);
        room.setSquareFootage(squareFootage);
        room.setScreens(screens);
        room.setSoundSystem(soundSystem);
        room.setStage(stage);
        room.setAccessible(accessible);
        room.setWifi(wifi);
        room.setSpecialFeatures(specialFeatures);
        room.setDescription(description);

        return room;
    }
}
