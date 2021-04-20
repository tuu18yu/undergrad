package Controller;

import Presenter.LoginPresenter;
import UseCase.UserManager;

import java.util.Scanner;

/**
 * A controller that deals with account creation and login information
 *
 * @author Chaolin Wang
 */
public class LoginController {
    private String loggedInUser;
    private final UserManager um;
    private final LoginPresenter lp;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * A constructor for Login controller.
     * Takes in a MangerFacade, call the getter for userManager
     * @param userManager The ManagerFacade object where we get the managers from
     */
    public LoginController(UserManager userManager) {
        this.loggedInUser = null;
        this.um = userManager;
        lp = new LoginPresenter();
    }

    /**
     * Check if the password is in the wanted format.
     *
     * @param password - the input password
     * @return true if the password is in the correct format, otherwise return false
     */
    private boolean validPassword(String password){
        //Check the length of the password, at least 4 characters, at most 8 characters
        if (password.length() < 4){
            lp.printPasswordTooShort();
            return false;
        } else if (password.length() > 8) {
            lp.printPasswordTooLong();
            return false;
        }
        //Checks if the password has same characters in a role more than 2 (case sensitive)
        int sameChar = 1;
        for (int i = 0; i < password.length() - 1; i++){
            if(password.charAt(i) == password.charAt(i + 1)){
                sameChar++;
                if(sameChar == 3){
                    lp.printPasswordTooManyDuplicate();
                    return false;
                }
            }else{
                sameChar = 1;
            }
        }
        return true;
    }

    /**
     * Creates a new user, given the type of user and account information.
     *
     * ***This can create any type of user
     * * also, since this is private, this acts as a helper for the other overloaded createAccount(String Name)
     *
     * @param userName The new user's username, *cannot be a duplicate within the system
     * @param password The new user's password
     * @param type The type of user added, can only be 2 strings: "Attendee" or "Organizer"
     */
    private void createAccount(String userName, String password, String type) {
        //null edge case
        if (type == null) {
            lp.displayAccountCreateInfo(false);
            return;
        }
        // Two additional constraints to username and password: no spaces and case-sensitivity to duplicates
        if (um.isDuplicate(userName)) {
            lp.printDuplicateName();
            lp.displayAccountCreateInfo(false);
            return;
        }
        if (userName.contains(" ") || password.contains(" ")) {
            lp.printSpaceError();
            lp.displayAccountCreateInfo(false);
            return;
        }

        // below deals with any input other than the ones listed above
        try {
            this.um.createUser(userName, password, type);
            lp.displayAccountCreateInfo(true); // successfully created account
        }
        catch (IllegalArgumentException ex) {
            lp.printInputException(ex);
            lp.displayAccountCreateInfo(false); // unrecognized input or duplicate user
        }
    }

    /**
     * Overload createAccount that takes in user input
     * @param type- string representing the user type that he/she want
     */
    public void createAccount(String type){
        lp.inputNameOfType(type);
        boolean nameIsZero;
        String userName;
        do {
            lp.inputName();
            userName = scanner.nextLine();
            if (userName.equals("0")) {
                lp.printInvalidUserName();
                nameIsZero = true;
            } else {
                nameIsZero = false;
            }
        }while(nameIsZero);
        boolean validPW;
        String password;
        do {
            lp.inputPasswordOfType(type);
            lp.passwordInstruction();
            password = scanner.nextLine();
            // check if the password matches the requested format
            validPW = this.validPassword(password);
        }while(!validPW);

        createAccount(userName, password, type);
    }

    /**
     * For general users check the code to allow them to create VIP account
     * @return whether he/she can create a VIP or not
     */
    public boolean codeCheck(){
        lp.promptInvCode();
        if(um.checkInvitationCode(scanner.nextLine())) {
            lp.printValidInvCode();
            return true;
        }
        else {
            lp.printInvalidInvCode();
            return false; // make sure the account does not get created
        }
    }

    /**
     * Logs in a user if the account information provided is correct
     * @param userName The user's username
     * @param password The user's password
     */
    public void login(String userName, String password) {
        boolean okay = um.canLogin(userName, password);
        lp.displayLoginInfo(okay);
        if(okay) {
            this.loggedInUser = userName;
            lp.displayLoginUser(loggedInUser);
        }
        else {
            this.loggedInUser = null;
        }
    }

    /**
     * Overloaded login that takes in user input from scanner, to be used in ConferenceSystem
     */
    public void login() {
        lp.inputName();
        String userName = scanner.nextLine();
        lp.inputPassword();
        String password = scanner.nextLine();
        this.login(userName, password);
    }

    /**
     * Prints a list of options for the start menu and takes in an option using a scanner.
     * @return The option entered
     */
    public String getStartMenu() {
        lp.printStartMenu();
        return scanner.nextLine();
    }

    /**
     * Prints a list of options for the Organizer's main menu and takes in an option using a scanner.
     * @return The option entered
     */
    public String getOrganizerMenu(){
        lp.printOrganizerMenu();
        return scanner.nextLine();
    }

    /**
     * Prints a list of options for the Speaker's main menu and takes in an option using a scanner.
     * @return The option entered
     */
    public String getSpeakerMenu(){
        lp.printSpeakerMenu();
        return scanner.nextLine();
    }

    /**
     * Prints a list of options for the Attendee's main menu and takes in an option using a scanner.
     * @return The option entered
     */
    public String getAttendeeMenu(){
        lp.printAttendeeMenu();
        return scanner.nextLine();
    }

    /**
     * Prints a list of options for the VIP's main menu and takes in an option using a scanner.
     * @return The option entered
     */
    public String getVIPMenu(){
        lp.printVIPMenu();
        return scanner.nextLine();
    }

    /**
     * Logs out any existing user
     */
    public void logout() {
        this.loggedInUser = null;
        lp.displayLogoutUser();
    }

    /**
     * Getter for the currently logged in user
     * @return a string of the user id if there is a user logged in, or null if there is not a logged in in user
     */
    public String getLoggedInUser() {
        return this.loggedInUser;
    }

    /**
     * Prints a message indicating an invalid option
     */
    public void invalidOption() {
        lp.printInvalidInput();
    }


    /**
     * Getter of user type by name
     * @param username - User name
     * @return String of user type
     */
    public String getUserType(String username){
        return um.getUserType(username);
    }

    /**
     * Prints a list of options to create the user account they want available to the initial menu
     * @return the option entered
     */
    public String getInitialAccountCreation() {
        lp.printInitialAccountCreationMenu();
        return scanner.nextLine();
    }
    /**
     * Prints a list of options for organizers to create the user account they want
     * @return the option entered
     */
    public String getOrganizerCreateUserMenu(){
        lp.printOrganizerCreateUserMenu();
        return scanner.nextLine();
    }
}
