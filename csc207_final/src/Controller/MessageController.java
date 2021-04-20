package Controller;

import Presenter.MessagePresenter;
import UseCase.EventManager;
import UseCase.MessageManager;
import UseCase.UserManager;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * Controller that deals with viewing and sending messages for Attendees.
 *
 * @author Wenying Wu, Yu Jin Kim
 */
public class MessageController {
    final MessageManager myMessageManager;
    UserManager myUserManager;
    EventManager myEventManager;
    final String username;
    MessagePresenter messagePresenter;

    /**
     * Constructor for MessageController
     * Creates a new instance of messagePresenter.
     *
     * @param username String username of current user
     * @param myUserManager instance of UserManager
     * @param messageManager instance of MessageManager
     */
    public MessageController(String username, UserManager myUserManager, MessageManager messageManager, EventManager eventManager){
        myMessageManager = messageManager;
        this.myUserManager = myUserManager;
        this.myEventManager = eventManager;
        this.username = username;
        messagePresenter = new MessagePresenter(username);
    }

    /**
     * Creates single message.
     *
     * @param receiverID String username of the recipient of the message
     * @param messageContent String content of the message
     * @return true if message created
     */
    public boolean sendSingleMessage(String receiverID, String messageContent){
        if (myUserManager.canSend(username, receiverID)){
            if (myUserManager.getUserType(receiverID).equals("Attendee") || myUserManager.getUserType(receiverID).equals("VIP")) {
                if (myUserManager.isFriend(username, receiverID)) {
                    return myMessageManager.createMessage(receiverID, messageContent);
                }
                else {
                    messagePresenter.printNotFriend(receiverID);
                    return false;
                }
            }
            return myMessageManager.createMessage(receiverID, messageContent);
        } else {return false;}
    }

    /**
     * Creates single message for reply.
     *
     * @param receiverID String username of the recipient of the message
     * @param messageContent String content of the message
     * @return true if message created
     */
    public boolean sendSingleReply(String receiverID, String messageContent){
        if (myMessageManager.getSenderConversations().contains(receiverID)){
            return myMessageManager.createMessage(receiverID, messageContent);
            }
        else {
            messagePresenter.printReplyCannotSend();
            return false;}
    }

    /**
     * Displays all conversations of user. Prompts the user to view a specific message history.
     */
    public void viewConversations(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.viewConversations(myMessageManager);
        String input = scanner.nextLine();
        if (input.equals("1")){
            markAsRead();
        } else if (input.equals("2")){
            markAsUnread();
        } else if (myMessageManager.getSenderConversations().contains(input)){
            viewSingleConversation(input);
        }
    }

    /**
     * Prompts for username, marks conversation as read.
     */
    public void markAsRead(){
        messagePresenter.printConversationPrompt();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (myMessageManager.getSenderConversations().contains(input)){
            if (myMessageManager.getSenderUnreadConversations().contains(input)) {
                myMessageManager.markConversationAsRead(input);
                messagePresenter.printMarkedAsRead();
                return;
            } messagePresenter.printConversationAlreadyRead();
        } else {
            messagePresenter.printConversationDoesNotExist();
        }
    }

    /**
     * Prompts for username, marks conversation as unread.
     */
    public void markAsUnread(){
        messagePresenter.printConversationPrompt();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (myMessageManager.getSenderConversations().contains(input)){
            if (!myMessageManager.getSenderUnreadConversations().contains(input)) {
                if (myMessageManager.markConversationAsUnread(input)) {
                    messagePresenter.printMarkedAsUnread();
                } else {
                    messagePresenter.printCannotMarkUnread();
                }
            }
        } else {
            messagePresenter.printConversationDoesNotExist();
        }
    }

    /**
     * Displays all archived conversations of user. Prompts the user to view a specific message history.
     * Or delete all conversations in the archive.
     */

    public void viewArchivedConversations(){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.viewArchivedConversations(myMessageManager);
        String input = scanner.nextLine();
        if (input.equals("1")){
            deleteAllArchivedConversations();
        } else if (myMessageManager.getSenderArchivedConversations().contains(input)){
            viewSingleArchiveConversation(input);
        }
    }

    /**
     * Displays single message history. Prompts the user to either reply to the conversation,
     * continue browsing conversations, or return to Message menu.
     *
     * @param conversationPartner String username of the other participant in this conversation history.
     */
    public void viewSingleConversation(String conversationPartner){
        Scanner scanner = new Scanner(System.in);
        messagePresenter.viewSingleConversation(myMessageManager, myUserManager, conversationPartner);
        String input = scanner.nextLine();
        switch (input) {
            case "0":
                replyToConversation(conversationPartner);
                break;
            case "1":
                archiveSingleMessage(conversationPartner);
                break;
            case "2":
                archiveConversation(conversationPartner);
                break;
            case "3":
                deleteSingleMessage(conversationPartner);
                break;
            case "4":
                deleteConversation(conversationPartner);
                break;
            case "5":
                viewConversations();
                break;
        }
    }

    /**
     * Displays single archived message history.
     *
     * @param conversationPartner - username of other participant in conversation history.
     */
    public void viewSingleArchiveConversation(String conversationPartner){
        messagePresenter.viewArchivedSingleConversation(myMessageManager, myUserManager, conversationPartner);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("0")){
            unarchiveConversation(conversationPartner);
        } else if (input.equals("1")){
            deleteArchivedConversation(conversationPartner);
        }
    }

    /**
     * Prompts user for reply content and replies to conversation.
     *
     * @param conversationPartner String username that the user is replying to.
     */
    public void replyToConversation(String conversationPartner){
        messagePresenter.printContentPrompt();
        Scanner scanner = new Scanner(System.in);
        String content = scanner.nextLine();
        if (sendSingleReply(conversationPartner, content)) {
            messagePresenter.printMessageSuccess();
        } else {
            messagePresenter.printMessageFailed();
        }
    }

    /**
     * Prints friend list of user and list of speakers.
     */
    public void displayPossibleContacts(){
        messagePresenter.printFriendList(myUserManager, username);
        messagePresenter.printEnrolledSpeakers(myUserManager, myEventManager, username);
    }

    /**
     * Prompts user for receiver ID and message content. Sends single message.
     */
    public void sendMessage(){
        displayPossibleContacts();
        Scanner scanner = new Scanner(System.in);
        messagePresenter.printReceiverIDPrompt();
        String receiver = scanner.nextLine();
        messagePresenter.printContentPrompt();
        String content = scanner.nextLine();
        if (sendSingleMessage(receiver, content)){
            messagePresenter.printMessageSuccess();
        } else {
            messagePresenter.printMessageFailed();
        }
    }

    /**
     * Calls MessagePresenter to print Attendee message menu. Prompts for a response to a list of menu options.
     *
     * @return the String input from the User.
     */
    public String getMessageMenu(){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printMessageMenu();
        return scan.nextLine();
    }

    /**
     * Calls MessagePresenter to print a notification of invalid input.
     */
    public void invalidInput(){
        messagePresenter.printInvalidInput();
    }

    /**
     * Prompts user for information to delete a single message (message number and confirmation of deletion).
     *

     * @param conversation - list of message ids in the conversation the user wants to delete from
     * @return int position of the message to be deleted, or -1 if there exists no such message.
     */
    public int checkDeleteSingleMessage(ArrayList<UUID> conversation) {
        messagePresenter.printMessageNumberPrompt();
        Scanner scan = new Scanner(System.in);
        int next = scan.nextInt();
        if (next > conversation.size()){
            messagePresenter.printMessageDoesNotExist();
            return -1;
        } else {
            messagePresenter.printConfirmationPrompt();
            scan = new Scanner(System.in);
            String confirm = scan.nextLine();
            if (confirm.equals("Y")) {
                return (next-1);
            } else {
                return -1;
            }
        }
    }

    /**
     * Deletes single message from a conversation given the conversation partner. Prompts for the message number
     * of the message the user wishes to delete.
     *
     * @param conversationPartner - the username of the conversation partner the message is with.
     */
    public void deleteSingleMessage(String conversationPartner){
        ArrayList<UUID> conversation = myMessageManager.getSingleConversationByReceiver(conversationPartner);
        int toBeDeleted = checkDeleteSingleMessage(conversation);
        if (toBeDeleted > -1){
            myMessageManager.deleteSingleMessage(conversation.get(toBeDeleted));
            messagePresenter.printMessageDeleted();
        }
    }

    /**
     * Deletes an entire conversation with a given chat partner.
     *
     * @param conversationPartner - username of the conversation partner
     */
    public void deleteConversation(String conversationPartner){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String confirm = scan.nextLine();
        if (confirm.equals("Y")){
            myMessageManager.deleteConversation(conversationPartner);
            messagePresenter.printConversationDeleted();
        }
    }

    /**
     * Archives a single message from a given conversation. Prompts the user for the number of the message
     * to be archived.
     *
     * @param conversationPartner - username of the conversation partner that this message is with.
     */
    public void archiveSingleMessage(String conversationPartner){
        messagePresenter.printMessageNumberPrompt();
        ArrayList<UUID> conversation = myMessageManager.getSingleConversationByReceiver(conversationPartner);
        Scanner scan = new Scanner(System.in);
        int next = scan.nextInt();
        if (next > conversation.size()){
            messagePresenter.printMessageDoesNotExist();
            return;
        }
        myMessageManager.archiveSingleMessage(conversation.get(next-1));
        messagePresenter.printMessageArchived();
    }

    /**
     * Archives entire conversation with given conversation partner.
     *
     * @param conversationPartner - username of the conversation partner
     */
    public void archiveConversation(String conversationPartner){
        myMessageManager.archiveConversation(conversationPartner);
        messagePresenter.printConversationArchived();
    }

    /**
     * Un-archives archived messages with a given conversation partner.
     *
     * @param conversationPartner - username of the conversation partner.
     */
    public void unarchiveConversation(String conversationPartner){
        myMessageManager.unArchiveConversation(conversationPartner);
        messagePresenter.printMessageUnarchived();
    }

    /**
     * Deletes archived messages with a given conversation partner.
     *
     * @param conversationPartner - username of conversation partner
     */
    public void deleteArchivedConversation(String conversationPartner){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String confirm = scan.nextLine();
        if (confirm.equals("Y")){
            myMessageManager.deleteArchivedConversation(conversationPartner);
            messagePresenter.printConversationDeleted();
        }
    }

    /**
     * Deletes all archived messages.
     */
    public void deleteAllArchivedConversations(){
        Scanner scan = new Scanner(System.in);
        messagePresenter.printConfirmationPrompt();
        String confirm = scan.nextLine();
        if (confirm.equals("Y")){
            myMessageManager.deleteAllArchivedConversations();
            messagePresenter.printArchiveDeletion();
        }
    }
    /**
     * Sends friends request to receiver
     *
     * @param receiver - username of user receiving the friend request
     */
    public void sendingFriendRequest(String receiver) {
        if (myUserManager.containsUser(receiver)) {
            if (myUserManager.getUserType(receiver).equals("Attendee") || myUserManager.getUserType(receiver).equals("VIP")) {
                if (!myUserManager.isFriend(username, receiver) && !myUserManager.isFriendRequestSent(receiver, username)) {
                    myUserManager.addFriendRequest(username, receiver);
                }
                else {
                    messagePresenter.printAlreadySent();
                }
            } else {
                messagePresenter.printCannotBeFriend();
            }
        }
        else {
            messagePresenter.printWrongUsername(receiver);
        }
    }

    /**
     * Prompts user to send friend request, and sends friend request to given input
     */
    public void sendFriendRequest() {
        Scanner scan = new Scanner(System.in);
        messagePresenter.printAttendeesAndVIPs(myUserManager);
        messagePresenter.printSendFriendRequest();
        String input = scan.nextLine();
        sendingFriendRequest(input);
    }

    /**
     * Adds given username in friend request to friend list if user accepts and deletes the username in friend request
     */
    public void acceptFriendRequest() {
        Scanner scan = new Scanner(System.in);
        messagePresenter.printAcceptFriendRequest();
        String input = scan.nextLine();
        if (myUserManager.getFriendRequest(username).contains(input)) {
            myUserManager.addFriend(username, input, true);
            messagePresenter.printAcceptFriendRequestSuccess(input);
        }
        else {
            messagePresenter.printNotInFriendRequest(input);
        }
    }

    /**
     * Deletes given username in friend request
     */
    public void declineFriendRequest() {
        Scanner scan = new Scanner(System.in);
        messagePresenter.printDeclineFriendRequest();
        String input = scan.nextLine();
        if (myUserManager.getFriendRequest(username).contains(input)) {
            myUserManager.addFriend(username, input, false);
            messagePresenter.printDeclineFriendRequestSuccess(input);
        }
        else {
            messagePresenter.printNotInFriendRequest(input);
        }
    }

    /**
     * Displays the user friend request menu
     */
    public void viewFriendRequest() {
        String input;
        do {
            Scanner scan = new Scanner(System.in);
            messagePresenter.printFriendRequestList(myUserManager, username);
            messagePresenter.printFriendRequestMenu();
            input = scan.nextLine();
            switch (input) {
                case "0":
                    break;
                case "1":
                    acceptFriendRequest();
                    break;
                case "2":
                    declineFriendRequest();
                    break;
                default:
                    messagePresenter.printInvalidInput();
            }
        } while (!input.equals("0"));
    }

    /**
     * Displays the user friend list
     */
    public void viewFriendList() {
        messagePresenter.printFriendList(myUserManager, username);
    }

    /**
     * Displays menu for methods related friend list system
     */
    public void manageFriendList() {
        String input;
        do {
            Scanner scan = new Scanner(System.in);
            messagePresenter.printFriendRequestList(myUserManager, username);
            messagePresenter.printFriendListMenu();
            input = scan.nextLine();
            switch (input) {
                case "0":
                    break;
                case "1":
                    viewFriendRequest();
                    break;
                case "2":
                    sendFriendRequest();
                    break;
                case "3":
                    viewFriendList();
                    break;
                default:
                    messagePresenter.printInvalidInput();
            }
        } while (!input.equals("0"));
    }


}
