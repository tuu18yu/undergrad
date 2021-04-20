package Controller;

import Gateway.ReadWriteGateway;
import Presenter.InputPresenter;
import Presenter.ReadWritePresenter;
import UseCase.EventManager;
import UseCase.MessageManager;
import UseCase.RoomManager;
import UseCase.UserManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * This is the main controller class that contains all the instances of the Managers and Controllers
 *
 * This class should only be initiated once in the main/UI method.
 *
 * Example for main() {
 *     Convention con = new Convention();
 *     con.run();
 * }
 */
public class ConferenceSystem {
    // Use case class instances
    private EventManager eventManager = new EventManager();
    private RoomManager roomManager = new RoomManager();
    private UserManager userManager = new UserManager();
    private MessageManager messageManager = new MessageManager();

    // Instance of ReadWriteGateway to allow for reading from and writing to files.
    private final ReadWriteGateway readWriteGateway = new ReadWriteGateway();

    // Instance of ReadWritePresenter to print messages related to file reading and writing.
    private final ReadWritePresenter readWritePresenter = new ReadWritePresenter();

    // Instance of ReadWriteScanner to read input related to file reading and writing.
    private final Scanner readWriteScanner = new Scanner(System.in);

    private final InputPresenter inputPresenter = new InputPresenter();

    // Controller class instances
    private LoginController loginController;
    private SignUpController signUpController;
    private ScheduleController scheduleController;
    private RoomController roomController;
    private MessageController messageController;
    private OrganizerMessageController organizerMessageController;
    private SpeakerMessageController speakerMessageController;
    private VIPMessageController myVIPMessageController;
    private GameController gameController;

    // File paths
    private final String usersPath = "C:\\Users\\tuu18\\IdeaProjects\\group_0210\\phase2\\src\\users.ser";
    private final String eventsPath = "C:\\Users\\tuu18\\IdeaProjects\\group_0210\\phase2\\src\\events.ser";
    private final String messagesPath = "C:\\Users\\tuu18\\IdeaProjects\\group_0210\\phase2\\src\\messages.ser";
    private final String roomsPath = "C:\\Users\\tuu18\\IdeaProjects\\group_0210\\phase2\\src\\rooms.ser";

    // Variable for keeping track with user, should be initialized after login.
    String username;

    public ConferenceSystem() {username = null;}

    /**
     * Runs the Conference system. Reads data files at the start, then prompts user to log in or
     * sign up with a new account. Writes data files once the user decides to log out.
     */
    public void run() {
        boolean readSuccessful = readData();

        if (readSuccessful)
            readWritePresenter.printReadSuccess();
        else
            readExceptionChooseOption();

        // After reading in Use Cases from file, instantiate controllers that do not require
        // the username of the logged in person to be instantiated.
        loginController = new LoginController(userManager);
        signUpController = new SignUpController(eventManager, userManager);
        scheduleController = new ScheduleController(eventManager, roomManager, userManager);
        roomController = new RoomController(roomManager);
        gameController = new GameController(userManager);

        //Menu for sign in or register
        initialLoginMenu();
        signUpController.callRemovePastEvents();
        messageManager.setSenderID(username);

        //initialize all classes that need the username
        messageController = new MessageController(username, userManager, messageManager, eventManager);
        organizerMessageController = new OrganizerMessageController(username, messageManager, userManager, eventManager);
        speakerMessageController = new SpeakerMessageController(username, messageManager, userManager, eventManager);
        myVIPMessageController = new VIPMessageController(username, userManager, messageManager, eventManager);
        //2. shows Main menu for each user
        switch (loginController.getUserType(username)){
            case "Organizer":
                organizerMenu();
                break;
            case "Speaker":
                speakerMenu();
                break;
            case "Attendee":
                attendeeMenu();
                break;
            case "VIP":
                VIPMenu();
                break;
        }
        //3. return if the user logout. Write out data to files.
        boolean writeSuccessful = writeData();

        if (writeSuccessful)
            readWritePresenter.printWriteSuccess();
        else
            writeExceptionChooseOption();
    }

    /**
     * Reads in all user, event, and message data from the files.
     *
     * @return True if file was read from successful, false if file could not be opened or
     * file contents were not valid.
     */
    private boolean readData() {
        try {
            userManager = (UserManager)readWriteGateway.readFromFile(usersPath);
            eventManager = (EventManager)readWriteGateway.readFromFile(eventsPath);
            messageManager = (MessageManager)readWriteGateway.readFromFile(messagesPath);
            roomManager = (RoomManager)readWriteGateway.readFromFile(roomsPath);

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * Writes all data to the respective files.
     *
     * @return Boolean indicating if reading was successful.
     */
    private boolean writeData()
    {
        try {
            readWriteGateway.saveToFile(usersPath, userManager);
            readWriteGateway.saveToFile(eventsPath, eventManager);
            readWriteGateway.saveToFile(messagesPath, messageManager);
            readWriteGateway.saveToFile(roomsPath, roomManager);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Based on user's input, performs appropriate action related to exceptions when reading from files.
     * User can choose to attempt to read the files again, continue without reading the files, or exit the program.
     */
    private void readExceptionChooseOption() {
        String option;
        boolean managersInitialized;

        do {
            readWritePresenter.printReadError();
            readWritePresenter.printReadErrorMenu();
            option = readWriteScanner.nextLine();
            managersInitialized = false;
            switch (option) {
                case "1": // Retry reading
                    managersInitialized = readData();
                    if (managersInitialized)
                        readWritePresenter.printReadSuccess();
                    break;
                case "2": // Continue without reading
                    userManager = new UserManager();
                    eventManager = new EventManager();
                    messageManager = new MessageManager();
                    managersInitialized = true;
                    break;
                case "3": // Exit program
                    System.exit(1);
                default: // Invalid input
                    readWritePresenter.printInvalidOption();
            }
        } while (!managersInitialized);
    }

    /**
     * Based on user's input, performs appropriate action related to exceptions when writing to files.
     * User can choose to attempt to write to the files again or exit the program.
     */
    private void writeExceptionChooseOption() {
        String option;
        boolean writeSuccessful;

        do {
            readWritePresenter.printWriteError();
            readWritePresenter.printWriteErrorMenu();
            option = readWriteScanner.nextLine();
            writeSuccessful = false;
            switch (option) {
                case "1": // Retry writing
                    writeSuccessful = writeData();
                    if (writeSuccessful)
                        readWritePresenter.printWriteSuccess();
                    break;
                case "2": // Exit program
                    System.exit(1);
                default: // Invalid input
                    readWritePresenter.printInvalidOption();
            }
        } while (!writeSuccessful);
    }

    /**
     * Helper method at the start that deals with login and account creation
     */
    private void initialLoginMenu() {
        String loginMenuOption;
        do {
            loginMenuOption = loginController.getStartMenu();
            switch (loginMenuOption) {
                case "1":
                    loginController.login();
                    this.username = loginController.getLoggedInUser();
                    break;
                case "2":
                    this.initialCreateAccount();
                    this.username = loginController.getLoggedInUser();
                    break;
                default:
                    loginController.invalidOption();
            }
        } while (loginController.getLoggedInUser() == null);
    }

    private void initialCreateAccount() {
        String menuOption;
        do{
            //Organizer create other types of user
            menuOption = loginController.getInitialAccountCreation();
            switch (menuOption){
                case "1":
                    loginController.createAccount("Attendee");
                    break;
                case "2":
                    loginController.createAccount("Organizer");
                    break;
                case "3":
                    if(loginController.codeCheck()) {
                        loginController.createAccount("VIP");
                    }
                    break;
                case "4":
                    break;
                default:
                    loginController.invalidOption();
            }
        }while (!menuOption.equals("4"));
    }

    /**
     * organizer menu helper class
     */
    private void organizerMenu(){
        String menuOption;
        do{
            // Organizer start menu
            menuOption = loginController.getOrganizerMenu();
            switch (menuOption) {
                case "1":
                    createOtherUserMenu();
                    break;
                case "2":
                    signUpMenu();
                    break;
                case "3":
                    scheduleMenu();
                    break;
                case "4":
                    organizerRoomMenu();
                    break;
                case "5":
                    organizerMessageMenu();
                    break;
                case "6":
                    gameMenu();
                    break;
                case "7":
                    loginController.logout();
                    break;
                default:
                    loginController.invalidOption();
            }
        } while (!menuOption.equals("7"));
    }

    /**
     * Organizer can create other types of user
     */
    private void createOtherUserMenu(){
        String menuOption;
        do{
            //Organizer create other types of user
            menuOption = loginController.getOrganizerCreateUserMenu();
            switch (menuOption){
                case "1":
                    loginController.createAccount("Speaker");
                    break;
                case "2":
                    loginController.createAccount("Attendee");
                    break;
                case "3":
                    loginController.createAccount("VIP");
                    break;
                case "4":
                    break;
                default:
                    loginController.invalidOption();
            }
        }while (!menuOption.equals("4"));
    }

    /**
     * Speaker menu helper class
     */
    private void speakerMenu(){
        String menuOption;
        do{
            //Speaker start menu
            menuOption = loginController.getSpeakerMenu();
            switch (menuOption) {
                case "1":
                    signUpMenu();
                    break;
                case "2":
                    speakerMessageMenu();
                    break;
                case "3":
                    roomMenu();
                    break;
                case "4":
                    gameMenu();
                    break;
                case "5":
                    loginController.logout();
                    break;
                default:
                    loginController.invalidOption();
            }
        } while (!menuOption.equals("5"));
    }

    /**
     * Attendee menu helper class
     */
    private void attendeeMenu(){
        String menuOption;
        do{
            // Attendee start menu
            menuOption = loginController.getAttendeeMenu();
            switch (menuOption) {
                case "1":
                    signUpMenu();
                    break;
                case "2":
                    attendeeMessageMenu();
                    break;
                case "3":
                    roomMenu();
                    break;
                case "4":
                    gameMenu();
                    break;
                case "5":
                    loginController.logout();
                    break;
                default:
                    loginController.invalidOption();
            }
        } while (!menuOption.equals("5"));
    }

    /**
     * VIP menu helper class
     */
    private void VIPMenu(){
        String menuOption;
        do{
            // Attendee start menu
            menuOption = loginController.getVIPMenu();
            switch (menuOption) {
                case "1":
                    signUpMenu();
                    break;
                case "2":
                    VIPMessageMenu();
                    break;
                case "3":
                    roomMenu();
                    break;
                case "4":
                    gameMenu();
                    break;
                case "5":
                    loginController.logout();
                    break;
                default:
                    loginController.invalidOption();
            }
        } while (!menuOption.equals("5"));
    }

    /**
     * Helper method that deals with signing up events,showing events
     * and deleting registered events.
     * First, it shows a menu of Sign Up System.
     * User can input a number to do either signing up for event,
     * deleting registered event, see events, see registered events,
     * and exist Sign Up System to main menu.
     */
    private void signUpMenu(){
        String menuOption;
        do{// Sign Up System Menu
            menuOption = signUpController.getMenu();
            switch (menuOption) {
                case "1":
                    signUpController.signUpEvent(username);
                    break;
                case "2":
                    signUpController.deleteEvent(username);
                    break;
                case "3":
                    signUpController.seeEventMenu(username);
                    break;
                case "4":
                    signUpController.signUpSystemEnd();
                    break;
                default:
                    signUpController.InvalidInput();
            }
        } while (!menuOption.equals("4"));
    }

    /**
     * Helper method that allows Organizers to either create events, create speakers, or exit this menu by inputting
     * a specific number.
     */
    private void scheduleMenu(){
        String scheduleMenuOption;
        do{
            scheduleMenuOption = scheduleController.getScheduleMenu();
            switch (scheduleMenuOption) {
                case "1":
                    scheduleController.getEventTypeMenu();
                    break;
                case "2":
                    scheduleController.deleteEventFromConference();
                    break;
                case "3":
                    scheduleController.changeEventCapacity();
                    break;
                case "4":
                    scheduleController.endScheduling();
                    break;
                default:
                    scheduleController.failScheduleMenu();
            }
        } while (!scheduleMenuOption.equals("4"));
    }

    /**
     * Helper method that allows Organizers to either send message to a single attendee or speaker, or all speakers,
     * or all attendees, or exit this menu by inputting  a specific number.
     */
    private void organizerMessageMenu() {
        String organizerMessageMenuOption;
        do {
            organizerMessageMenuOption = organizerMessageController.getMessageMenu();
            switch (organizerMessageMenuOption) {
                case "0":
                    break;
                case "1":
                    organizerMessageController.viewConversations();
                    break;
                case "2":
                    organizerMessageController.sendMessage();
                    break;
                case "3":
                    organizerMessageController.sendMessagesToSpeakers();
                    break;
                case "4":
                    organizerMessageController.sendMessagesToAttendeesVIPs();
                    break;
                case "5":
                    organizerMessageController.viewArchivedConversations();
                    break;
                case "6":
                    organizerMessageController.viewDeletedMessagesBin();
                    break;
                default:
                    organizerMessageController.invalidInput();
            }
        } while (!organizerMessageMenuOption.equals("0"));
    }

    /**
     * Helper method that allows Speakers to either send message to all attendees of their one event or all events,
     * or reply to an attendee, or exit this menu by inputting  a specific number.
     */
    private void speakerMessageMenu(){
        String speakerMessageMenuOption;
        do{
            speakerMessageMenuOption = speakerMessageController.getMessageMenu();
            switch (speakerMessageMenuOption){
                case "0":
                    break;
                case "1":
                    speakerMessageController.viewConversations();
                    break;
                case "2":
                    speakerMessageController.sendMessage();
                    break;
                case "3":
                    speakerMessageController.viewArchivedConversations();
                    break;
                default:
                    speakerMessageController.invalidInput();
            }
        } while (!speakerMessageMenuOption.equals("0"));
    }

    /**
     * Helper method that allows Attendees to either send a message to another Attendee or Speaker,
     * or view conversation, or exit this menu by inputting  a specific number.
     */
    private void attendeeMessageMenu(){
        String attendeeMessageMenuOption;
        do{
            attendeeMessageMenuOption = messageController.getMessageMenu();
            switch (attendeeMessageMenuOption){
                case "0":
                    break;
                case "1":
                    messageController.viewConversations();
                    break;
                case "2":
                    messageController.sendMessage();
                    break;
                case "3":
                    messageController.viewArchivedConversations();
                    break;
                case "4":
                    messageController.manageFriendList();
                    break;
                default:
                    messageController.invalidInput();
            }
        } while (!attendeeMessageMenuOption.equals("0"));
    }

    /**
     * Helper method that allows VIP to either send a message to other users,
     * or view conversation, or exit this menu by inputting a specific number.
     */
    private void VIPMessageMenu(){
        String attendeeMessageMenuOption;
        do{
            attendeeMessageMenuOption = myVIPMessageController.getVIPMessageMenu();
            switch (attendeeMessageMenuOption){
                case "0":
                    break;
                case "1":
                    myVIPMessageController.viewConversations();
                    break;
                case "2":
                    myVIPMessageController.sendMessage();
                    break;
                case "4":
                    myVIPMessageController.manageFriendList();
                    break;
                default:
                    myVIPMessageController.invalidInput();
            }
        } while (!attendeeMessageMenuOption.equals("0"));
    }

    /**
     * Helper methods that allow users to play math games. They can select
     * a level and a type (+, -, *, /) by entering corresponding input.
     */
    private void gameMenu(){
        int level = gameController.setLevel();
        if (level == 0){
            return;
        }
        String choice;
        do {
            Scanner scan2 = new Scanner(System.in);
            gameController.getGameMenu();
            choice = scan2.nextLine();
            switch (choice) {
                case "1":
                    gameController.gameMode(1, level);
                    break;
                case "2":
                    gameController.gameMode(2, level);
                    break;
                case "3":
                    gameController.gameMode(3, level);
                    break;
                case "4":
                    gameController.gameMode(4, level);
                    break;
                case "5":
                    break;
                default:
                    gameController.InvalidInput();
            }
        }while(!choice.equals("5"));
    }

    private void roomMenu() {
        String option;
        do {
            option = roomController.getRoomMenu("non-organizer");
            switch (option){
                case "0":
                    break;
                case "1":
                    roomController.displayRooms();
                    break;
                default:
                    inputPresenter.printInvalidInput();
            }
        } while (!option.equals("0"));
    }

    private void organizerRoomMenu() {
        String option;
        do {
            option = roomController.getRoomMenu("organizer");
            switch (option){
                case "0":
                    break;
                case "1":
                    roomController.displayRooms();
                    break;
                case "2":
                    roomController.createNewRoom();
                    break;
                case "3":
                    roomController.deleteRoom();
                    break;
                default:
                    inputPresenter.printInvalidInput();
            }
        } while (!option.equals("0"));
    }
}
