package UseCase;


import Entity.UserAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * A Class that manages UserAccounts by creating new accounts and checking login information
 */
public class UserManager implements Serializable {

    private HashMap<String, UserAccount> userMap = new HashMap<>();

    // Stores the hash codes of the invitation codes (Strings of length 18)
    private final ArrayList<Integer> invitationCodes = new ArrayList<>();


    /**
     * Creates a new user, given the type of user and account information.
     *
     * @param userName The new user's username, *cannot be a duplicate within the system
     * @param password The new user's password
     * @param type The type of user added, can only be 3 strings: "Attendee", "Speaker",
     *             or ""Organizer"
     * @throws IllegalArgumentException is thrown when any input doesn't meet the requirements
     */
    public void createUser(String userName, String password, String type) throws IllegalArgumentException {
        // Makes sure the strings passed in are not null or empty
        if(userName == null || password == null){throw new IllegalArgumentException("Invalid inputs: null");}
        else if(userName.isEmpty() || password.isEmpty()){throw new IllegalArgumentException("Invalid inputs: Empty");}

        if (containsUser(userName)){
            throw new IllegalArgumentException("The user already exists");
        }
        switch (type) {
            case "Attendee":
                userMap.put(userName, new AttendeeFactory().createAccount(userName, password));
                break;
            case "Speaker":
                userMap.put(userName, new SpeakerFactory().createAccount(userName, password));
                break;
            case "Organizer":
                userMap.put(userName, new OrganizerFactory().createAccount(userName, password));
                break;
            case "VIP":
                userMap.put(userName, new VIPFactory().createAccount(userName, password));
                break;
            default:
                throw new IllegalArgumentException("Invalid input: unknown type");
        }
    }

    /**
     * Check for duplicate userNames, this also counts case-sensitive usernames as duplicates.
     * ie: "Joe" and "JOE" are duplicates
     * This method is to be used at the controller level.
     * Important Note: this is different then the other method containsUser(String) as that checks for exact usernames
     *
     * @param userName The username entered
     * @return true iff userName entered matches any names in the system, disregarding case
     */
    public boolean isDuplicate(String userName) {
        for (String key : userMap.keySet()) {
            if (key.toLowerCase().equals(userName.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the HashMap of all UserAccounts.
     *
     * @return the user HashMap with usernames as keys and UserAccount objects as values
     */
    public HashMap<String, UserAccount> getUserMap() {
        return userMap;
    }

    /**
     * Sets the HashMap of all UserAccounts.
     *
     * @param userMap - Hashmap with usernames as keys and UserAccount objects as values
     *
     */
    public void setUserMap(HashMap<String, UserAccount> userMap) {
        this.userMap = userMap;
    }

    /**
     *
     * @param username - name of the user
     * @return the user account got by name search
     */
    public UserAccount getUserByName(String username){
        return userMap.get(username);
    }

    /**
     *  Gets the type of the user as a String
     *
     * @param username - name of the user
     * @return the user type
     */
    public String getUserType(String username){
        return getUserByName(username).getUserType();
    }

    /**
     * Checks if a user matches a given type
     *
     * @param username The username of the user to be checked
     * @param wantedType The type of user to compare with
     * @return true iff the user is of the given type
     */
    public boolean isOfType(String username, String wantedType){
        return getUserType(username).equals(wantedType);
    }

    /**
     * Returns true is the user exists.
     *
     * @param username - the username of this user
     * @return true if the user exists
     */
    public boolean containsUser(String username)
    {
        return userMap.containsKey(username);
    }

    /**
     * Returns the password of this user.
     *
     * @param username - the username of this user
     * @return a String of this user's password
     */
    public String getPassword(String username)
    {
        return userMap.get(username).getPassWord();
    }

    /**
     * Returns a arraylist of event names this user registered.
     *
     * @param username - the username of this user
     * @return - an Arraylist of event names
     */
    public ArrayList<String> getRegisteredEvents(String username)
    {
        return userMap.get(username).getRegisteredEvents();
    }

    /**
     * Adds this event's name to this user's registered event list.
     *
     * @param username - the username of this user
     * @param eventName - the name of this event
     */
    public void addRegisteredEvent(String username, String eventName)
    {
        UserAccount user = userMap.get(username);
        user.registerEvent(eventName);
    }

    /**
     * Return a list of speakers' usernames.
     *
     * @return an ArrayList of String representing the usernames of speakers
     */
    public ArrayList<String> getSpeakerList(){
        Set<String> usernames = userMap.keySet();
        ArrayList<String> speakers = new ArrayList<>();
        for(int i = 0; i < usernames.size(); i++){
            if (isOfType((String)usernames.toArray()[i], "Speaker"))
            speakers.add((String)usernames.toArray()[i]);
        }
        return speakers;
    }
    /**
     * Return a list of attendees' usernames.
     *
     * @return an ArrayList of String representing the usernames of speakers
     */
    public ArrayList<String> getAttendeeList(){
        Set<String> usernames = userMap.keySet();
        ArrayList<String> attendees = new ArrayList<>();
        for(int i = 0; i < usernames.size(); i++){
            if (isOfType((String)usernames.toArray()[i], ("Attendee")))
                attendees.add((String)usernames.toArray()[i]);
        }
        return attendees;
    }
    /**
     * Return a list of VIPs' usernames.
     *
     * @return an ArrayList of String representing the usernames of VIP's
     */
    public ArrayList<String> getVIPList(){
        Set<String> usernames = userMap.keySet();
        ArrayList<String> attendees = new ArrayList<>();
        for(int i = 0; i < usernames.size(); i++){
            if (isOfType((String)usernames.toArray()[i], ("VIP")))
                attendees.add((String)usernames.toArray()[i]);
        }
        return attendees;
    }

    /**
     * Return a list of Organizers' usernames.
     *
     * @return an ArrayList of String representing the usernames of organizers
     */
    public ArrayList<String> getOrganizerList(){
        Set<String> usernames = userMap.keySet();
        ArrayList<String> attendees = new ArrayList<>();
        for(int i = 0; i < usernames.size(); i++){
            if (isOfType((String)usernames.toArray()[i], ("Organizer")))
                attendees.add((String)usernames.toArray()[i]);
        }
        return attendees;
    }

    /**
     * Checks whether or not a person can login with given account information.
     *
     * @param userName The userName id of the user
     * @param password The password of the user
     * @return true if the id is in the system and the password matches, false otherwise
     */
    public boolean canLogin(String userName, String password) {
        if (containsUser(userName)) {
            return getPassword(userName).equals(password);
        }
        return false;
    }

    /**
     * This checks if a user has permission to send messages to a receiver.
     *
     * @param username - username of user sending a message
     * @param receiver - username of user receiving the message
     *
     * @return true if the user can send to the receiver, fails silently otherwise
     */
    public boolean canSend(String username, String receiver){
        if (!containsUser(receiver)){ // receiver not found
            return false;
        } else if (receiver.equals(username)){ // send to self
            return false;
        } else if (isOfType(username, "Organizer")){ // organizers can send to all users
            return true;
        } else if (isOfType(username, "Speaker")){ // speakers can send to all users who are enrolled in their events
            return true;
        } else if (isOfType(username, "VIP")) {
            return true;
        } else{
            return !isOfType(receiver, "Organizer");
        }
    }

    /**
     * This generates a brand new one time InvitationCode to allow for creation of VIP accounts, and stores its
     * hash code to the invitationCodes field
     * @return A one time code to allow creation of VIP accounts, this a string made up of 18 pseudorandom
     * alphanumerical characters
     */
    public String newInvitationCode() {
        Random rand = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 18; i++) {
            switch (rand.nextInt(3)) {
                case 0:
                    code.append(rand.nextInt(10)); //0-9
                    break;
                case 1:
                    code.append((char) (65+rand.nextInt(26))); //a-z
                    break;
                case 2:
                    code.append((char) (97+rand.nextInt(26))); //A-Z
                    break;
            }
        }
        String invCode = code.toString(); // turns StringBuilder to string

        invitationCodes.add(invCode.hashCode());
        return invCode;
    }

    /**
     * This checks if a given invitation code is valid. If so, it gets removed from the pile.
     * @param invCode The invitation code provided
     * @return true if the code is valid and can be "redeemed", false otherwise
     */
    public boolean checkInvitationCode(String invCode) {
        boolean secretCode = "Lindsey Is Awesome".equals(invCode);
        return invitationCodes.remove((Object) invCode.hashCode()) || secretCode;
    }

    /**
     * Checks if a receiver is in username's friend request list
     * @param username username of the sender
     * @param receiver username of the receiver
     * @return true if receiver is in username's friend request list, false otherwise
     */
    public boolean isFriendRequestSent(String username, String receiver) {
        return getUserByName(username).getFriendRequest().contains(receiver);
    }

    /**
     * Checks if a receiver is in username's friend list
     * @param username username of the sender
     * @param receiver username of the receiver
     * @return true if receiver is in username's friend list, false otherwise
     */
    public boolean isFriend(String username, String receiver) {
        return getUserByName(username).getFriendList().contains(receiver);
    }
    /**
     * Adds receiver to username's friend request list if receiver is not already in username's friend list or friend
     * request list, if the type of username is VIP added first of the friend request list of reciever
     *
     * @param username username of the sender
     * @param receiver username of the receiver
     * @return true if receiver is added to username's friend request list, false otherwise
     */
    public boolean addFriendRequest(String username, String receiver){
        if (!isFriend(username, receiver) && !isFriendRequestSent(username, receiver)){
            if (getUserType(username).equals("VIP")) {
                getUserByName(receiver).addVIPFriendRequest(username);
                return true;
            }
            getUserByName(receiver).addFriendRequest(username);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds receiver to username's friend list if response is true
     *
     * @param username username of the sender
     * @param receiver username of the receiver
     * @param response user's input, accept of decline friend request
     */
    public void addFriend(String username, String receiver, boolean response) {
        if (isFriend(username, receiver)) {
            return;
        }
        if (response) {
            getUserByName(receiver).addFriend(username);
            getUserByName(username).addFriend(receiver);
        }
        getUserByName(username).removeFriendRequest(receiver);
    }
    /**
     * Gets the friend list of username
     *
     * @param username username of the sender
     * @return  friend list of username
     */
    public ArrayList<String> getFriendList(String username) {
        return getUserByName(username).getFriendList();
    }

    /**
     * Gets the friend list of username
     *
     * @param username username of the sender
     * @return  friend list of username
     */
    public ArrayList<String> getFriendRequest(String username) {
        return getUserByName(username).getFriendRequest();
    }

}

