package Presenter;

/**
 * A presenter that prints different prompts and messages to the UI so that the login system can function as needed
 */
public class LoginPresenter {
    /**
     * This prints the start menu, before anyone has signed in.
     */
    public void printStartMenu(){
        System.out.println("Welcome to the Start Menu! \n" +
                "Options:\n" +
                "1. Sign in\n" +
                "2. Register\n" +
                "Enter 1 or 2: ");
    }

    /**
     * Prompts the client to enter a username.
     */
    public void inputName(){
        System.out.println("Enter your unique username: \n" +
                "(Please do not use 0 as your username)");
    }

    /**
     * Prompts the client to enter a password.
     */
    public void inputPassword(){
        System.out.println("Enter your password: ");
    }

    /**
     * Prints the error message: "Sorry this username is already taken."
     */
    public void printDuplicateName() {
        System.out.println("Sorry this username is already taken.");
    }

    /**
     * Prints the error message: "Sorry, your username and password cannot contain spaces."
     */
    public void printSpaceError() {
        System.out.println("Sorry, your username and password cannot contain spaces.");
    }
    /**
     * Displays a error message by printing "Invalid input, please try again."
     */
    public void printInvalidInput() {
        System.out.println("Invalid input, please try again.");
    }

    /**
     * Displays an error in the account creation process, depending on the error message of the thrown exception
     * @param exception The exception that was thrown and caught
     */
    public void printInputException(IllegalArgumentException exception) {
        System.out.println(exception.getMessage());
    }

    /**
     * display information when an account is created
     * @param value- boolean value got from login controller
     */
    public void displayAccountCreateInfo(boolean value){
        if (value){
            System.out.println("The user account was created successfully!");
        }
        else
            System.out.println("Failed to create account, please try again.");
    }

    /**
     * Display information when a user is logging in
     * @param value- boolean value got from login controller
     */
    public void displayLoginInfo(boolean value){
        if (value){
            System.out.println("Correct Password.");
        }
        else {
            System.out.println("Incorrect username or password.");
        }
    }

    /**
     * Display information about current login user
     * @param info- user name got from login controller
     */
    public void displayLoginUser(String info){
        System.out.println("Current user is " + info + ".");
    }

    /**
     * Display a successful log gout message.
     */
    public void displayLogoutUser(){
        System.out.println("Logged out successfully!");
    }

    /**
     * Prints out the main menu for the Organizer. This contains all the actions that an Organizer can take.
     */
    public void printOrganizerMenu(){
        System.out.println("Welcome to the Organizer Menu! \n" +
                "Options:\n" +
                "1. Create account\n"+
                "2. Sign up\n" +
                "3. Manage Events\n" +
                "4. Manage Rooms\n" +
                "5. Send a message\n" +
                "6. Play Math Game\n" +
                "7. Log out\n"+
                "Enter 1, 2, 3, 4, 5, 6 or 7: ");
    }

    /**
     * Prints out the main menu for the Speaker. This contains all the actions that a Speaker can take.
     */
    public void printSpeakerMenu(){
        System.out.println("Welcome to the Speaker Menu! \n" +
                "Options:\n" +
                "1. Sign up\n" +
                "2. Send a message\n" +
                "3. View room menu\n" +
                "4. Play Math Game\n" +
                "5. Log out\n"+
                "Enter 1, 2, 3, 4, or 5: ");
    }

    /**
     * Prints out the main menu for the Attendee. This contains all the actions that an Attendee can take.
     */
    public void printAttendeeMenu(){
        System.out.println("Welcome to the Attendee Menu! \n" +
                "Options:\n" +
                "1. Sign up\n" +
                "2. Send a message\n" +
                "3. View room menu\n" +
                "4. Play Math Game\n" +
                "5. Log out\n"+
                "Enter 1, 2, 3, 4, or 5: ");
    }

    /**
     * Prints out the main menu for the VIP. This contains all the actions that a VIP can take.
     */
    public void printVIPMenu(){
        System.out.println("\nWelcome to the premium VIP Menu! \n\n" +
                "Here are the options you can choose:\n" +
                "1. Sign up\n" +
                "2. Send a message\n" +
                "3. View room menu\n" +
                "4. Play Math Game\n" +
                "5. Log out\n"+
                "Please enter 1, 2, 3, 4, or 5: ");
    }

    /**
     * Prints the initial Account Creation menu
     * ***** can only have attendee, organizer, or VIP if they have an invitation code
     */
    public void printInitialAccountCreationMenu() {
        System.out.println("What type of user do you want to create:\n"+
                "1. Attendee\n"+
                "2. Organizer\n"+
                "3. VIP (only if you have a special invitation code)\n"+
                "4. Exit this menu\n"+
                "Enter 1, 2, 3, or 4: ");
    }

    /**
     * Print the menu where organizer can create other types of user.
     */
    public void printOrganizerCreateUserMenu(){
        System.out.println("What type of user do you want to create:\n"+
                "Options:\n" +
                "1. Speaker\n"+
                "2. Attendee\n"+
                "3. VIP\n"+
                "4. Go back to Organizer menu\n"+
                "Enter 1, 2, 3, or 4: ");
    }

    /**
     * Prints message about password being too short.
     */
    public void printPasswordTooShort() {
        System.out.println("Password too short. Password should contain at least 4 characters.");
    }

    /**
     * Prints message about password being too long.
     */
    public void printPasswordTooLong() {
        System.out.println("Password too long. Password should contain at most 8 characters.");
    }

    /**
     * Prints message about password contains more than 2 same characters in a row.
     */
    public void printPasswordTooManyDuplicate() {
        System.out.println("Password cannot contain more than 2 same characters in a row.");
    }

    /**
     * Prints the password instructions.
     */
    public void passwordInstruction() {
        System.out.println("*** Password Instructions ***\n" +
                "(a) At least 4 characters, at most 8 characters\n" +
                "(b) Cannot contain more than 2 same character in a row (Ex. 10003, helloooo, are invalid)");
    }

    /**
     * Prints message that tells the organizer to enter
     * the name of the type of user wanted to be create
     *
     * @param type - the type of the user wanted to be create
     */
    public void inputNameOfType(String type) {
        System.out.println("Please enter the username of who you would like to make a(n) "+ type);
    }

    /**
     * Prints message that tells the organizer to enter
     * the password of the type of user wanted to be create
     *
     * @param type - the type of the user wanted to be create
     */
    public void inputPasswordOfType(String type) {
        System.out.println("Please enter the password you want this "+ type +" to have: ");
    }

    /**
     * Prints message about invalid username.
     */
    public void printInvalidUserName() {
        System.out.println("0 is not a valid username.");
    }

    /**
     * Asks the client for an invitation code to make VIP accounts
     */
    public void promptInvCode() {
        System.out.println("Please enter a valid invitation code below:");
    }
    /**
     * Prints that the invitation code is correct
     */
    public void printValidInvCode() {
        System.out.println("Fantastic, your invitation code has been redeemed!");
    }

    /**
     * Prints that the invitation code given is not valid
     */
    public void printInvalidInvCode() {
        System.out.println("Sorry, the code is invalid or has expired");
    }
}

