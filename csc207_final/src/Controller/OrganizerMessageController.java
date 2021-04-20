package Controller;

import UseCase.EventManager;
import UseCase.MessageManager;
import UseCase.UserManager;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * A controller class that calls MessageManager to manage any
 * command related to Organizer's Messaging System
 *
 * @author Yu Jin Kim
 */

public class OrganizerMessageController extends MessageController {

    /**
     * Constructor for OrganizerMessageController
     * Creates a new instance of messagePresenter.
     *
     * @param username String username of current user
     * @param myUserManager instance of UserManager
     * @param myMessageManager instance of MessageManager
     */
    public OrganizerMessageController(String username, MessageManager myMessageManager, UserManager myUserManager, EventManager eventManager){
        super(username, myUserManager, myMessageManager, eventManager);
    }

    /**
     * Sends single message to given user.
     *
     * @param receiverID String username of the recipient of the message
     * @param messageContent String content of the message
     * @return true if message created
     */
    @Override
    public boolean sendSingleMessage(String receiverID, String messageContent){
        if (myUserManager.canSend(username, receiverID)){
            return myMessageManager.createMessage(receiverID, messageContent);
        } else {
            messagePresenter.printCannotSend();
            return false;}
    }

    /**
     * Prints list of attendee and list of speakers.
     */
    public void displayPossibleContacts(){
        messagePresenter.printAttendees(myUserManager);
        messagePresenter.printSpeakers(myUserManager);
        messagePresenter.printVIPs(myUserManager);
        messagePresenter.printOrganizers(myUserManager);
    }

    /**
     * Sends messages to all Speakers in the system.
     *
     * @param messageContent - content of the message
     * @return true if message created
     */
    public boolean sendAllSpeakersMessage(String messageContent){
        for (String userName: myUserManager.getSpeakerList()) {
            if (!myUserManager.canSend(this.username, userName)) {
                return false;
            }
            else {
               myMessageManager.createMessage(userName, messageContent);
            }
        }
        return !myUserManager.getSpeakerList().isEmpty();
    }

    /**
     * Prompts user for message content. Sends messages to all Speakers.
     */
    public void sendMessagesToSpeakers(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.printContentPrompt();
        String content = scanner.nextLine();
        if (sendAllSpeakersMessage(content)){
            messagePresenter.printMessageSuccess();
        } else {
            messagePresenter.printMessageFailed();
        }
    }


    /**
     * Sends messages to all Attendees in the system.
     *
     * @param messageContent - content of the message
     * @return true if message created
     */
    public boolean sendAllAttendeesVIPsMessage(String messageContent){
        for (String userName: myUserManager.getAttendeeList()) {
            myMessageManager.createMessage(userName, messageContent);
        }

        for (String userName: myUserManager.getVIPList()) {
            myMessageManager.createMessage(userName, messageContent);
        }
        return !myUserManager.getAttendeeList().isEmpty() || !myUserManager.getVIPList().isEmpty();
    }

    /**
     * Prompts user for receiver ID and message content. Sends messages to all Attendees.
     */
    public void sendMessagesToAttendeesVIPs(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.printContentPrompt();
        String content = scanner.nextLine();
        if (sendAllAttendeesVIPsMessage(content)){
            messagePresenter.printMessageSuccess();
        } else {
            messagePresenter.printMessageFailed();
        }
    }

    /**
     * Calls MessagePresenter to print out the Message Menu.
     */
    public String getMessageMenu(){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printOrganizerMessageMenu();
        return scan.nextLine();
    }

    /**
     * Deletes a single message of a conversation, for organizer and conversation partner both.
     *
     * @param conversationPartner - username of the conversation to delete a single message from.
     */
    @Override
    public void deleteSingleMessage(String conversationPartner){
        ArrayList<UUID> conversation = myMessageManager.getSingleConversationByReceiver(conversationPartner);
        int toBeDeleted = checkDeleteSingleMessage(conversation);
        if (toBeDeleted > -1){
            myMessageManager.deleteSingleMessageBothSides(conversation.get(toBeDeleted));
            messagePresenter.printMessageUserReceiverDelete();
        }
    }

    /**
     * Deletes entire conversation, for organizer and conversation partner both.
     *
     * @param conversationPartner - username partner of the conversation to be deleted.
     */
    @Override
    public void deleteConversation(String conversationPartner){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String confirm = scan.nextLine();
        if (confirm.equals("Y")){
            myMessageManager.deleteConversationBothSides(conversationPartner);
            messagePresenter.printConversationUserReceiverDelete();
        }
    }

    /**
     * Deletes all archived messages, for organizer and conversation partner both.
     */
    @Override
    public void deleteAllArchivedConversations() {
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String confirm = scan.nextLine();
        if (confirm.equals("Y")) {
            myMessageManager.deleteAllArchivedConversationsBothSides();
            messagePresenter.printArchiveUserReceiverDeletion();
        }
    }

    /**
     * Deletes archived messages with a given conversation partner, for organizer and conversation partner both.
     *
     * @param conversationPartner - username of the conversation partner
     */
    @Override
    public void deleteArchivedConversation(String conversationPartner){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String confirm = scan.nextLine();
        if (confirm.equals("Y")){
            myMessageManager.deleteArchivedConversationBothSides(conversationPartner);
            messagePresenter.printConversationUserReceiverDelete();
        }
    }

    /**
     * Displays messages deleted from the inbox of both sender and receiver.
     */
    public void viewDeletedMessagesBin(){
        messagePresenter.viewFullyDeletedMessages(myMessageManager);
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        if (input.equals("0")){
            clearDeletedMessagesBin();
        }
        else if (myMessageManager.getDeletedMessagesSenderReceivers().contains(input)){
            viewUserDeletedMessagesBin(input);
        }
    }

    /**
     * Clears the system storage of all fully deleted messages.
     */
    public void clearDeletedMessagesBin(){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String input = scan.nextLine();
        if (input.equals("Y")){
            myMessageManager.emptyDeletedMessages();
            messagePresenter.printMessageBinCleared();
        }
    }

    /**
     * Displays messages fully deleted that belong to a specific user, given a username.
     *
     * @param userID - username of the user whose deleted messages are displayed.
     */
    private void viewUserDeletedMessagesBin(String userID){
        messagePresenter.viewUserFullyDeletedMessage(myMessageManager, userID);
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        if (input.equals("0")){
            viewDeletedMessagesBin();
        }
    }
}
