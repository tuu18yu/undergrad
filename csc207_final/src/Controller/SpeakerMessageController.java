package Controller;

import Entity.Event;
import UseCase.EventManager;
import UseCase.MessageManager;
import UseCase.UserManager;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A controller class that calls MessageManager to manage any
 * command related to Speaker's Messaging System
 *
 * @author Yu Jin Kim
 */
public class SpeakerMessageController extends MessageController {
    /**
     * Constructor for SpeakerMessageController
     * Creates a new instance of messagePresenter.
     *
     * @param username String username of current user
     * @param myUserManager instance of UserManager
     * @param myMessageManager instance of MessageManager
     * @param myEventManager instance of EventManager
     */
    public SpeakerMessageController(String username, MessageManager myMessageManager, UserManager myUserManager, EventManager myEventManager){
        super(username, myUserManager, myMessageManager, myEventManager);
    }

    public boolean sendSingleMessage(String receiverID, String messageContent){
        if (myUserManager.canSend(username, receiverID)){
            return myMessageManager.createMessage(receiverID, messageContent);
        } else {return false;}
    }


    /**
     * Sends messages to all attendees enrolled in any Speaker's events.
     *
     * @param messageContent - message of the content
     * @return true if message created
     */
    public boolean sendAllMessageAllEvent(String messageContent) {
        ArrayList<String> usernames = new ArrayList<>();
        for (Event event : myEventManager.getEventList()) {
            if (event.getSpeaker().contains(username)) {
                if (event.getAttendees().isEmpty()) {
                    messagePresenter.printNoAttendees(event.getName());
                }
                for (String userName : event.getAttendees()) {
                    if (!usernames.contains(userName)) {
                        myMessageManager.createMessage(userName, messageContent);
                        usernames.add(userName);
                    }
                }
            }
        }
        return !myEventManager.getEventList().isEmpty();
    }

    /**
     * Prompts user for receiver ID and message content. Sends single message.
     */
    public void sendMessagesToAttendeesOfAllTalks(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.printEvent(myEventManager, username);
        messagePresenter.printContentPrompt();
        String content = scanner.nextLine();
        if (sendAllMessageAllEvent(content)){
            messagePresenter.printMessageSuccessSpeaker();
        } else {
            messagePresenter.printMessageFailed();
        }
    }

    /**
     * Sends messages to all attendees enrolled in chosen event.
     *
     * @param messageContent - message of the content
     * @return true if message created
     */
    public boolean sendAllMessageAnEvent(String messageContent, String eventName) {
        if (myEventManager.getEventList().isEmpty()) {
            return false;
        }
        for (Event event : myEventManager.getEventList()) {
            if (event.getName().equals(eventName)) {
                if (event.getAttendees().isEmpty()){
                    messagePresenter.printNoAttendees(eventName);
                    return false;
                }
                if (!event.getSpeaker().contains(username)) {
                    return false;
                }
                for (String userName : event.getAttendees()) {
                    myMessageManager.createMessage(userName, messageContent);
                }
            }
        }
        return myEventManager.getEvent(eventName);
    }

    /**
     * Prompts user for receiver ID and message content. Sends single message.
     */
    public void sendMessagesToAttendeesOfATalk(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.printEvent(myEventManager, username);
        messagePresenter.printEventIDPrompt();
        String eventName = scanner.nextLine();
        messagePresenter.printAttendeesEvent(myEventManager, eventName);
        messagePresenter.printContentPrompt();
        String content = scanner.nextLine();
        if (sendAllMessageAnEvent(content, eventName)) {
            messagePresenter.printMessageSuccess();
        } else {
            messagePresenter.printMessageFailed();
        }
    }

    /**
     * Prompts user for sending message to all of the events or a single event.
     */
    public void sendMessage(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.printSpeakerMessagePrompt();
        String input = scanner.nextLine();
        boolean loop = false;
        do {
            switch (input) {
                case "0":
                    loop = true;
                    break;
                case "1":
                    loop = true;
                    sendMessagesToAttendeesOfATalk();
                    break;
                case "2":
                    loop = true;
                    sendMessagesToAttendeesOfAllTalks();
                    break;
            }
        } while (!loop);
    }

    /**
     * Calls MessagePresenter to print out the Message Menu.
     */
    public String getMessageMenu(){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printSpeakerMessageMenu();
        return scan.nextLine();
    }

}


