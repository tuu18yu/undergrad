package Presenter;

import UseCase.RoomManager;

import java.util.ArrayList;

/**
 * A presenter class for printing messages related to room actions.
 *
 * @author Filip Jovanovic
 */
public class RoomPresenter {

    /**
     * Prints the room menu.
     */
    public void printRoomMenu() {
        System.out.print("Welcome to the room menu! Please select one of the following options:\n" +
                "1. View list of rooms.\n" +
                "Please choose an option, or enter 0 to exit the room menu: \n");
    }

    /**
     * Prints the organizer room menu.
     */
    public void printOrganizerRoomMenu() {
        System.out.print("Welcome to the room menu! Please select one of the following options:\n" +
                "1. View list of rooms.\n" +
                "2. Add a new room.\n" +
                "3. Delete a room.\n" +
                "Please choose an option, or enter 0 to exit the room menu: \n");
    }

    /**
     * Prints a message prompting the user to enter a room number.
     */
    public void printEnterRoomNumber() {
        System.out.print("Please enter the room number: ");
    }

    /**
     * Prints a message indicating the selected room already exists.
     */
    public void printRoomExists() {
        System.out.print("That room number already exists. Please enter another number: ");
    }

    /**
     * Prints a message prompting the user to enter a capacity.
     */
    public void printEnterCapacity() {
        System.out.print("Please enter the room's capacity (minimum capacity is 3): ");
    }

    /**
     * Prints a message prompting the user to enter a square footage.
     */
    public void printEnterSquareFeet() {
        System.out.print("Please enter the room's size in square feet: ");
    }

    /**
     * Prints a message prompting the user to enter a number of screens.
     */
    public void printEnterScreens() {
        System.out.print("Please enter the number of screens in the room: ");
    }

    /**
     * Prints a message prompting the user to enter if the room has a sound system or not.
     */
    public void printEnterSoundSystem() {
        System.out.print("Does the room have a sound system? (Yes/No): ");
    }

    /**
     * Prints a message prompting the user to enter if the room has a stage or not.
     */
    public void printEnterStage() {
        System.out.print("Does the room have a stage in it? (Yes/No): ");
    }

    /**
     * Prints a message prompting the user to enter if the room has a sound system or not.
     */
    public void printEnterAccessible() {
        System.out.print("Does this room provide accessibility features? (Yes/No): ");
    }

    /**
     * Prints a message prompting the user to enter if the room has WiFi or not.
     */
    public void printEnterWifi() {
        System.out.print("Does the room have WiFi? (Yes/No): ");
    }

    /**
     * Prints a message prompting the user to enter special features.
     */
    public void printEnterSpecialFeatures() {
        System.out.print("Please enter the room's special features.\n" +
                "If the room has no special features, please leave this blank: ");
    }

    /**
     * Prints a message prompting the user to enter a description.
     */
    public void printEnterDescription() {
        System.out.println("Please enter a description of the room: ");
    }

    /**
     * Prints a message indicating if the room was added successfully.
     */
    public void printRoomAddSuccessful() {
        System.out.println("Room added successfully!");
    }

    /**
     * Prints a message prompting the user to enter if the room has a sound system or not.
     * @param roomNumber the room number
     */
    private void printRoomNumberHeader(int roomNumber) {
        System.out.println("========== Room " + roomNumber + " ===========\n");
    }

    /**
     * Prints the specified room's capacity.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printRoomCapacity(int roomNumber, RoomManager roomManager) {
        System.out.println("Capacity: " + roomManager.getRoomCapacity(roomNumber) + "\n");
    }

    /**
     * Prints the specified room's square footage.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printSquareFeet(int roomNumber, RoomManager roomManager) {
        System.out.println("Size: " + roomManager.getScreens(roomNumber) + " sq ft\n");
    }

    /**
     * Prints the specified room's number of screens.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printScreens(int roomNumber, RoomManager roomManager) {
        System.out.println("Screens: " + roomManager.getScreens(roomNumber) + "\n");
    }

    /**
     * Prints Yes or No depending on the value of the boolean passed to the method.
     * Prints Yes if the boolean is true, and prints No otherwise.
     * @param bool the boolean to print
     */
    private void booleanToYesNo(boolean bool) {
        if (bool)
            System.out.println("Yes");
        else
            System.out.println("No");
    }

    /**
     * Prints if the specified room has a sound system.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printSoundSystem(int roomNumber, RoomManager roomManager) {
        boolean hasSoundSystem = roomManager.roomHasSoundSystem(roomNumber);
        System.out.print("Sound System: ");
        booleanToYesNo(hasSoundSystem);
        System.out.print("\n");
    }

    /**
     * Prints if the specified room has a stage.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printStage(int roomNumber, RoomManager roomManager) {
        boolean hasStage = roomManager.roomHasStage(roomNumber);
        System.out.print("Stage: ");
        booleanToYesNo(hasStage);
        System.out.print("\n");
    }

    /**
     * Prints if the specified room is accessible for people with disabilities.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printAccessible(int roomNumber, RoomManager roomManager) {
        boolean isAccessible = roomManager.roomIsAccessible(roomNumber);
        System.out.print("Accessibility Features: ");
        booleanToYesNo(isAccessible);
        System.out.print("\n");
    }

    /**
     * Prints if the specified room has WiFi.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printWifi(int roomNumber, RoomManager roomManager) {
        boolean hasWifi = roomManager.roomHasWifi(roomNumber);
        System.out.print("Wifi: ");
        booleanToYesNo(hasWifi);
        System.out.print("\n");
    }

    /**
     * Prints the specified room's special features.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printSpecialFeatures(int roomNumber, RoomManager roomManager) {
        String specialFeatures = roomManager.getSpecialFeatures(roomNumber);

        if (specialFeatures.isEmpty())
            System.out.println("Special Features: None\n");
        else
            System.out.println("Special Features: " + specialFeatures + "\n");
    }

    /**
     * Prints the specified room's description.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    private void printDescription(int roomNumber, RoomManager roomManager) {
        String description = roomManager.getDescription(roomNumber);
        System.out.println("Description: " + description + "\n");
    }

    /**
     * Prints a short overview of the room, including the room number, capacity, and description.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    public void printRoomShort(int roomNumber, RoomManager roomManager) {
        printRoomNumberHeader(roomNumber);
        printRoomCapacity(roomNumber, roomManager);
        printDescription(roomNumber, roomManager);
        System.out.println("==========");
    }

    /**
     * Prints a complete overview of the room, including all of the room's features.
     * @param roomNumber the room number
     * @param roomManager the roomManager containing the map of rooms
     */
    public void printRoomFull(int roomNumber, RoomManager roomManager) {
        printRoomNumberHeader(roomNumber);
        printRoomCapacity(roomNumber, roomManager);
        printSquareFeet(roomNumber, roomManager);
        printScreens(roomNumber, roomManager);
        printSoundSystem(roomNumber, roomManager);
        printStage(roomNumber, roomManager);
        printAccessible(roomNumber, roomManager);
        printWifi(roomNumber, roomManager);
        printSpecialFeatures(roomNumber, roomManager);
        printDescription(roomNumber, roomManager);
    }

    /**
     * Prints a message indicating that there are no rooms currently registered.
     */
    public void printNoRooms() {
        System.out.println("No rooms have currently been registered.");
    }

    /**
     * Prints a message prompting the user to enter a room number to display the full overview for.
     */
    public void printSelectRoomForFullInfo() {
        System.out.println("Enter a room number to view all of the room's details, or enter 0 to exit: ");
    }

    /**
     * Prints a message indicating the selected room does not exist.
     */
    public void printRoomNonExistent() {
        System.out.println("That room does not exist, please try again: ");
    }

    /**
     * Prints a message indicating the user should enter the number of the room they want to delete.
     */
    public void printSelectRoomForDeletion() {
        System.out.println("Enter the number of the room to delete, or enter 0 to cancel: ");
    }

    /**
     * Prints a message indicating that a room has successfully been delete.
     */
    public void printRoomDeletionSuccessful() {
        System.out.println("Room has been deleted successfully.");
    }

    /**
     * Prints a message indicating that a room was unable to be deleted.
     */
    public void printRoomDeletionFailure() {
        System.out.println("Room could not be deleted as it has events scheduled for future times.");
    }

    /**
     * Prints a list of the available room numbers.
     * @param availableRooms the list of available room numbers
     */
    public void printAvailableRoomNumbers(ArrayList<Integer> availableRooms) {
        System.out.print("Available Rooms: ");
        if (availableRooms.isEmpty()) {
            System.out.println("None");
        } else {
            for (int roomNumber : availableRooms)
                System.out.print(roomNumber + " ");
            System.out.print("\n");
        }
    }

    /**
     * Prints a message prompting the user to select an available room.
     */
    public void printChooseAvailableRoom() {
        System.out.println("Select an available room number for the event, or enter 0 to cancel event creation: ");
    }

    /**
     * Prints a message indicating the room is not an available room.
     */
    public void printRoomNotAvailable() {
        System.out.println("That room is not an available room.");
    }

    /**
     * Prints a message indicating that the capacity entered is greater than the room's capacity.
     */
    public void printAboveRoomCapacity() {
        System.out.println("Capacity entered is greater than the room capacity.");
    }
}
