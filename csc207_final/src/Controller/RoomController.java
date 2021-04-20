package Controller;

import Presenter.RoomPresenter;
import UseCase.RoomBuilder;
import UseCase.RoomManager;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A controller class that deals with displaying a list of rooms, adding rooms, and deleting rooms.
 *
 * @author Filip Jovanovic
 */
public class RoomController {
    private final RoomManager roomManager;
    private RoomBuilder roomBuilder = new RoomBuilder();
    private final InputValidator inputValidator = new InputValidator();
    private final RoomPresenter roomPresenter = new RoomPresenter();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for the RoomController taking a RoomManager.
     * @param roomManager the RoomManager
     */
    public RoomController(RoomManager roomManager)
    {
        this.roomManager = roomManager;
    }

    /**
     * Displays the room menu and gets input from the user about which option they would like to select.
     * @return the selected option
     */
    public String getRoomMenu(String type) {

        if (type.toLowerCase().equals("organizer"))
            roomPresenter.printOrganizerRoomMenu();
        else
            roomPresenter.printRoomMenu();
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Gets an int input from the user for a new room's number.
     * If the user enters a number of a room that already exists,
     * will prompt the user to enter another number.
     *
     * @return an integer greater than or equal to 0
     */
    private int getNewRoomNumber() {
        int roomNumber = 1;
        boolean validNumber = false;

        roomPresenter.printEnterRoomNumber();

        while (!validNumber) {
            roomNumber = inputValidator.getIntGreaterOrEqual(1);

            if (roomManager.doesRoomExist(roomNumber))
                roomPresenter.printRoomExists();
            else
                validNumber = true;
        }

        return roomNumber;
    }

    /**
     * Gets an int input from the user for a new room's capacity.
     *
     * @return an integer greater than or equal to 2.
     */
    private int getNewRoomCapacity() {
        roomPresenter.printEnterCapacity();

        return inputValidator.getIntGreaterOrEqual(3);
    }

    /**
     * Gets an int input from the user for a new room's square footage.
     *
     * @return an integer greater than or equal to 1.
     */
    private int getNewRoomSquareFeet() {
        roomPresenter.printEnterSquareFeet();

        return inputValidator.getIntGreaterOrEqual(1);
    }

    /**
     * Gets an int input from the user for a new room's number of screens.
     *
     * @return an integer greater than or equal to 2.
     */
    private int getNewRoomScreens() {
        roomPresenter.printEnterScreens();

        return inputValidator.getIntGreaterOrEqual(0);
    }

    /**
     * Gets a string input from the user indicating if the new room has a sound system.
     *
     * @return true if user entered "yes", false if user entered "no".
     */
    private boolean getNewRoomSoundSystem() {
        roomPresenter.printEnterSoundSystem();

        return inputValidator.getStringForBoolean();
    }

    /**
     * Gets a string input from the user indicating if the new room has a stage.
     *
     * @return true if user entered "yes", false if user entered "no".
     */
    private boolean getNewRoomStage() {
        roomPresenter.printEnterStage();

        return inputValidator.getStringForBoolean();
    }

    /**
     * Gets a string input from the user indicating if the new room has accessibility features.
     *
     * @return true if user entered "yes", false if user entered "no".
     */
    private boolean getNewRoomAccessible() {
        roomPresenter.printEnterAccessible();

        return inputValidator.getStringForBoolean();
    }

    /**
     * Gets a string input from the user indicating if the new room has WiFi.
     *
     * @return true if user entered "yes", false if user entered "no".
     */
    private boolean getNewRoomWifi() {
        roomPresenter.printEnterWifi();

        return inputValidator.getStringForBoolean();
    }

    /**
     * Gets a string input from the user for the new room's special features.
     *
     * @return String containing the special features.
     */
    private String getNewRoomSpecialFeatures() {
        roomPresenter.printEnterSpecialFeatures();

        return scanner.nextLine();
    }

    /**
     * Gets a string input from the user for the new room's description.
     *
     * @return String containing the description.
     */
    private String getNewRoomDescription() {
        roomPresenter.printEnterDescription();

        return inputValidator.getNonEmptyString();
    }

    /**
     * Prompts the user to input details about the new room, creates the room,
     * and adds it to the list of rooms.
     */
    public void createNewRoom() {
        int roomNumber = getNewRoomNumber();
        int capacity = getNewRoomCapacity();
        int squareFeet = getNewRoomSquareFeet();
        int screens = getNewRoomScreens();
        boolean soundSystem = getNewRoomSoundSystem();
        boolean stage = getNewRoomStage();
        boolean accessible = getNewRoomAccessible();
        boolean wifi = getNewRoomWifi();
        String specialFeatures = getNewRoomSpecialFeatures();
        String description = getNewRoomDescription();

        roomBuilder = roomBuilder.roomNumber(roomNumber)
                .roomNumber(roomNumber)
                .capacity(capacity)
                .squareFootage(squareFeet)
                .screens(screens)
                .soundSystem(soundSystem)
                .stage(stage)
                .accessible(accessible)
                .wifi(wifi)
                .specialFeatures(specialFeatures)
                .description(description);

        roomManager.addRoom(roomBuilder);
        roomPresenter.printRoomAddSuccessful();
    }

    /**
     * Gets an int input from the user for a room that already exists.
     * If the user enters a number of a room that does not exist,
     * will prompt the user to enter another number.
     *
     * @return the int that the user entered.
     */
    private int getRoomNumber() {
        int roomNumber = 0;
        boolean validNumber = false;

        while (!validNumber) {
            roomNumber = inputValidator.getIntGreaterOrEqual(0);

            if (!roomManager.doesRoomExist(roomNumber) && roomNumber != 0)
                roomPresenter.printRoomNonExistent();
            else
                validNumber = true;
        }

        return roomNumber;
    }

    /**
     * Displays a list of all rooms, showing basic information about each room,
     * and then prompts the user to enter an existing room number for full information.
     * If the room list is empty, will return to the menu of room options.
     */
    public void displayRooms() {
        if (roomManager.isRoomMapEmpty()) {
            roomPresenter.printNoRooms();
            return;
        }

        ArrayList<Integer> rooms = roomManager.getRoomNumbers();

        for (int room : rooms) {
            roomPresenter.printRoomShort(room, roomManager);
        }

        roomPresenter.printSelectRoomForFullInfo();

        int displayRoomNumber = getRoomNumber();

        if (displayRoomNumber == 0)
            return;

        roomPresenter.printRoomFull(displayRoomNumber, roomManager);
    }

    /**
     * Deletes an existing room if there are no scheduled times in the room after the current date and time.
     */
    public void deleteRoom() {
        if (roomManager.isRoomMapEmpty()) {
            roomPresenter.printNoRooms();
            return;
        }

        roomPresenter.printSelectRoomForDeletion();

        int roomNumber = getRoomNumber();

        if (roomNumber == 0)
            return;

        roomManager.removePastTimes(roomNumber);

        if (roomManager.canDeleteRoom(roomNumber)) {
            roomManager.deleteRoom(roomNumber);
            roomPresenter.printRoomDeletionSuccessful();
        } else {
            roomPresenter.printRoomDeletionFailure();
        }
    }
}
