package Entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that represents abstract class user account.
 * @author Qianwen Wu
 */
public abstract class UserAccount implements Serializable {
    private String userName;
    private String passWord;
    private ArrayList<String> registeredEvents; // Stores the list of events that the user have registered
    private ArrayList<String> friendList; //Stores the list of usernames that user can message
    private ArrayList<String> friendRequest; //Stores the list of usernames that wants to be friends with user
    protected String userType; // The type of user created, should match exactly with the subclasses filename (no .java)

    public UserAccount() {}

    /**
     * Create user account by given user name and password
     *
     * @param userName- name of user
     * @param passWord- identify user by password
     */
    public UserAccount(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.registeredEvents = new ArrayList<>();
        this.friendList = new ArrayList<>();
        this.friendRequest = new ArrayList<>();
    }

    /**
     * Getter of user name
     *
     * @return the user name
     */
    public String getUserName() {return userName;}

    /**
     * Modify the user name
     *
     */
    public void setUserName(String name) {this.userName = name;}

    /**
     * Return the password
     *
     * @return password
     */
    public String getPassWord() {return passWord;}

    /**
     * Modify the password
     *
     * @param passWord- password
     */
    public void setPassWord(String passWord) {this.passWord = passWord;}

    /**
     * Getter of userType
     *
     * @return the type of this current user
     */
    public String getUserType() {
        return this.userType;
    }

    /**
     * Return the list of registered events
     * @return the list of registered events
     */
    public ArrayList<String> getRegisteredEvents() {return registeredEvents;}

    /**
     * Sign up new event
     * @param eventName- registered event name
     */
    public void registerEvent(String eventName) {this.registeredEvents.add(eventName);}
    /**
     * Getter of friendList
     *
     * @return friendList
     */
    public ArrayList<String> getFriendList() {
        return friendList;
    }
    /**
     * Add friendID to friendList
     *
     * @param friendID - username of the wanted user
     */
    public void addFriend(String friendID) {
        friendList.add(friendID);
    }

    /**
     * Getter of friendRequest
     *
     * @return friendRequest
     */
    public ArrayList<String> getFriendRequest() {
        return friendRequest;
    }

    /**
     * Add friendID to friendRequest
     *
     * @param friendID - username of the wanted user
     */
    public void addFriendRequest(String friendID) {
        friendRequest.add(friendID);
    }
    /**
     * Add friendID to friendRequest as first of the friendRequest
     *
     * @param friendID - username of the wanted user
     */
    public void addVIPFriendRequest(String friendID) {
        friendRequest.add(0, friendID);
    }
    /**
     * Remover friendID from friendRequest
     *
     * @param friendID - username of the wanted user
     */
    public void removeFriendRequest(String friendID) {
        friendRequest.remove(friendID);
    }
}