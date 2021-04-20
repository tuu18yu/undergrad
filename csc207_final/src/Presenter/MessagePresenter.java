package Presenter;

import UseCase.MessageManager;
import UseCase.UserManager;
import UseCase.EventManager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A presenter class that prints values that will be displayed in the UI
 * for Messaging System.
 *
 * @author Wenying Wu, Yu Jin Kim
 */
public class MessagePresenter {
    private final String username;

    /**
     * Create MessagePresenter with given username.
     * @param username - username of the user
     */
    public MessagePresenter(String username){
        this.username = username;
    }

    /**
     * Generate string representation of a single message from given MessageManager and
     * message ID.
     *
     * @param messageManager - current messageManager
     * @param messageID - key value of the wanted message in the hashmap
     * @return a basic String representation of this message
     */
    public String getMessageText(MessageManager messageManager, UUID messageID){
        String sender;
        String receiver;
        if (messageManager.isSender(messageID)){
            sender = "you";
            receiver = messageManager.getMessageReceiver(messageID);
        } else {
            sender = messageManager.getMessageSender(messageID);
            receiver = "you";
        }
        return sender + " sent to " + receiver + ":\n" + messageManager.getMessageContent(messageID) +
                "\n           at " + messageManager.getMessageTime(messageID);
    }

    /**
     * Generate string representation of single message that includes which message number it is out of all
     * the messages in its conversation.
     *
     *
     * @param messageManager - current messageManager
     * @param messageID - key value of the wanted message in the hashmap
     * @param messageNumber - numerical location of message in its conversation
     * @param conversationLength - size of its conversation
     * @return - a String representation of this message, contextualized by its numerical place in its
     * conversation.
     */
    public String getMessageTextWithMessageNumber(MessageManager messageManager, UUID messageID, int messageNumber,
                                                  int conversationLength){
        String messageText = getMessageText(messageManager, messageID);
        return messageText + "\n           (" + messageNumber + " out of " + conversationLength + " messages)";
    }

    /**
     * Print Message Menu for Attendees.
     */
    public void printMessageMenu(){
        System.out.println("Welcome to the Attendee Message Menu\n" +
                "Options:\n" +
                "0. Exit Message Menu\n" +
                "1. View conversations and reply to messages\n" +
                "2. Send a message\n"+
                "3. View archived conversations:\n" +
                "4. Manage Friend List:\n" +
                "Enter 0, 1, 2, 3, or 4: ");
    }

    /**
     * Print Message Menu for Organizers.
     */
    public void printOrganizerMessageMenu(){
        System.out.println("Welcome to the Organizer Message Menu.\n" +
                "Options:\n" +
                "0. Exit Message Menu\n" +
                "1. View conversations\n" +
                "2. Send a message to a single user\n" +
                "3. Send a message to all Speakers\n" +
                "4. Send a message to all Attendees and VIPs\n" +
                "5. View archived conversations\n" +
                "6. View system message trash bin\n" +
                "Enter 0, 1, 2, 3, 4, 5, or 6: ");
    }


    /**
     * Print Message Menu for Speakers.
     */
    public void printSpeakerMessageMenu(){
        System.out.println("Welcome to the Speaker Message Menu.\n" +
                "Options:\n" +
                "0. Exit Message Menu\n" +
                "1. View conversations and reply to messages\n" +
                "2. Send a message to Attendees of your Talks:\n" +
                "3. View archived conversations:\n" +
                "Enter 0, 1, 2, or 3: ");
    }

    /**
     * Print Message Menu for VIP's.
     */
    public void printVIPMessageMenu(){
        System.out.println("Welcome to the VIP Message Menu\n" +
                "Options:\n" +
                "0. Exit Message Menu\n" +
                "1. View conversations and reply to messages\n" +
                "2. Send a message\n"+
                "3. View archived conversations:\n" +
                "4. Manage Friend List:\n" +
                "Enter 0, 1, 2, 3, or 4: ");
    }

    /**
     * Print list of users that logged in user has messages with.
     *
     * @param messageManager - a MessageManager instance
     */
    public void viewConversations(MessageManager messageManager){
        System.out.println("You have conversations with these users: ");
        System.out.println(messageManager.getSenderConversations());
        ArrayList<String> unread = messageManager.getSenderUnreadConversations();
        System.out.println("You have unread messages from " + unread.size() + " users: ");
        System.out.println(unread);
        System.out.println("Options:\n" +
                "Enter a username to see your message history \n" +
                "1. Mark a conversation as read \n" +
                "2. Mark a conversation as unread\n" +
                "(Note: you can only mark a conversation as unread if you have received messages in it)\n" +
                "Enter a username, 1, 2, or anything else to return to Message Menu:");
    }

    /**
     * Prints list of users that logged in user has archived messages with.
     *
     * @param messageManager - a MessageManager instance
     */
    public void viewArchivedConversations(MessageManager messageManager){
        System.out.println("You have archived conversations with these users: ");
        System.out.println(messageManager.getSenderArchivedConversations());
        System.out.println("Enter a username to see your message history \n" +
                "1. Delete all archived conversations \n" +
                "Enter a username, 1, or anything else to return to Message Menu: ");
    }


    /**
     * Prints conversation with single user, marks the conversation as read.
     *
     * @param messageManager - a MessageManager instance
     * @param userManager - an UserManager instance
     * @param recipientID - username of the user receiving the message
     */
    public void printSingleConversation(MessageManager messageManager, UserManager userManager, String recipientID){
        String identity = userManager.getUserType(username);
        System.out.println("Your conversation with " + identity + " " + recipientID + ": ");
        ArrayList<UUID> singleConversation = messageManager.getSingleConversationByReceiver(recipientID);
        int conversationLength = singleConversation.size();
        System.out.println("(This conversation has " + conversationLength + " messages.)");
        for (int i = 0; i < conversationLength; i++){
            String messageText;
            messageText = getMessageTextWithMessageNumber(messageManager, singleConversation.get(i),
                    i+1, conversationLength);
            System.out.println(messageText);
        }
        messageManager.markConversationAsRead(recipientID);
    }

    public void printArchivedConversation(MessageManager messageManager, UserManager userManager, String recipientID){
        String identity = userManager.getUserType(username);
        System.out.println("Your archived conversation with " + identity + " " + recipientID + ": " );
        ArrayList<UUID> singleConversation = messageManager.getArchivedConversationByReceiver(recipientID);
        int conversationLength = singleConversation.size();
        System.out.println("(This conversation has " + conversationLength + " messages.)");
        for (int i = 0; i < conversationLength; i++){
            String messageText;
            messageText = getMessageTextWithMessageNumber(messageManager, singleConversation.get(i),
                    i+1, conversationLength);
            System.out.println(messageText);
        }
    }


    /**
     * Prints options for Attendees and Speakers to continue browsing, reply to conversation, delete or archive
     * the conversation or individual messages, or return to Message Menu.
     */
    public void printViewSingleConversationPrompt(){
        System.out.println("Options:\n" +
                "0: Reply to this conversation\n" +
                "1. Archive a single message\n" +
                "2. Archive entire conversation\n" +
                "3. Delete a single message (only from your inbox, unless you have VIP or Organizer status)\n" +
                "4. Delete conversation (only from your inbox, unless you have VIP or Organizer status)\n" +
                "5. Continue browsing conversations\n" +
                "Enter 0, 1, 2, 3, 4, 5, or anything else to return to the Message Menu: ");
    }

    /**
     * Calls method to print single conversation between Attendee or Speaker user and a given recipient.
     * Calls method to print subsequent options of Attendees and Speakers.
     *
     * @param messageManager - a MessageManager instance
     * @param userManager - an UserManager instance
     * @param recipientID - username of the user receiving the message
     */
    public void viewSingleConversation(MessageManager messageManager, UserManager userManager, String recipientID) {
        printSingleConversation(messageManager, userManager, recipientID);
        printViewSingleConversationPrompt();
    }

    public void printArchiveOptions(){
        System.out.println("Options: \n" +
                "0: Unarchive this conversation\n" +
                "1. Delete this conversation\n" +
                "2. Continue browsing archived conversations\n" +
                "Enter 0, 1, 2, or anything else to return to Message Menu:");
    }

    /**
     * Calls method to print single archived conversation. Prints options for user to continue browsing, delete or
     * unarchive the conversation, or return to Message Menu.
     *
     * @param messageManager - this session's instance of MessageManager
     * @param userManager - this session's instance of UserManager
     * @param recipientID - the username of the conversation partner
     */
    public void viewArchivedSingleConversation(MessageManager messageManager, UserManager userManager,
                                               String recipientID) {
        printArchivedConversation(messageManager, userManager, recipientID);
        printArchiveOptions();
    }

    /**
     * Prints prompt for ID of message recipient.
     */
    public void printReceiverIDPrompt(){
        System.out.println("Enter the username of the user you wish to message: ");
    }

    /**
     * Prints prompt for ID of Event.
     */
    public void printEventIDPrompt(){
        System.out.println("Enter the name of the event whose attendees you wish to message: ");
    }

    /**
     * Print prompt for ID of conversation partner
     */
    public void printConversationPrompt(){
        System.out.println("Enter the username of the conversation: ");
    }

    /**
     * Prints options for a Speaker to send messages.
     */
    public void printSpeakerMessagePrompt(){
        System.out.println("Options:\n" +
                "0. Return to previous menu\n" +
                "1. Choose a Talk\n" +
                "2. Send to all of your Talks\n" +
                "Enter 0, 1, or 2: ");
    }

    /**
     * Print prompt for content of message.
     */
    public void printContentPrompt(){
        System.out.println("Enter the message text: ");
    }

    /**
     * Print notification of message success.
     */
    public void printMessageSuccess(){
        System.out.println("Message successfully sent!");
    }

    /**
     * Print notification of message success.
     */
    public void printMessageSuccessSpeaker(){
        System.out.println("Message successfully sent excluding events without attendees.");
    }


    /**
     * Print notification of message failure.
     */
    public void printMessageFailed(){
        System.out.println("Message failed.");
    }

    /**
     * Print notification of invalid input.
     */
    public void printInvalidInput(){
        System.out.println("Input invalid, please try again.");
    }

    /**
     * Print notification that no attendees are signed up
     */
    public void printNoAttendees(String eventName){
        System.out.println("There are no attendees signed up in " + eventName);
    }

    /**
     * Print notification that current user does not have permission to message recipient.
     */
    public void printCannotSend(){
        System.out.println("You do not have permission to send messages to this user.");
    }

    /**
     * Print notification that current user does not have conversation with the wanted user.
     */
    public void printReplyCannotSend(){
        System.out.println("You do not have existing conversation with this user.");
    }

    /**
     * Print list of Speakers in the conference.
     * @param userManager - an UserManager instance
     */
    public void printSpeakers(UserManager userManager){
        System.out.println("You can message the following Speakers:");
        System.out.println(userManager.getSpeakerList());
    }

    /**
     * Print list of Speakers of the events user has enrolled given user
     * @param userManager - an UserManager instance
     * @param eventManager - an EventManager instance
     * @param username - username of the user
     */
    public void printEnrolledSpeakers(UserManager userManager, EventManager eventManager, String username){
        ArrayList<String> speakers = new ArrayList<>();
        for (String event: userManager.getRegisteredEvents(username)) {
            for (String speaker: eventManager.getEventSpeakers(event)){
                if (!speakers.contains(speaker)) {
                    speakers.add(speaker);
                }
            }
        }
        System.out.println("You can message the following Speakers:");
        System.out.println(speakers);
    }

    /**
     * Print list of Attendees in the conference.
     * @param userManager - an UserManager instance
     */
    public void printAttendees(UserManager userManager){
        System.out.println("You can message the following Attendees:");
        ArrayList<String> attendees = userManager.getAttendeeList();
        attendees.remove(username);
        System.out.println(attendees);
    }

    /**
     * Print list of VIP's in the conference.
     * @param userManager - an UserManager instance
     */
    public void printVIPs(UserManager userManager){
        System.out.println("You can message the following VIP's:");
        ArrayList<String> vips = userManager.getVIPList();
        vips.remove(username);
        System.out.println(vips);
    }

    /**
     * Print list of VIP's in the conference.
     * @param userManager - an UserManager instance
     */
    public void printAttendeesAndVIPs(UserManager userManager){
        System.out.println("You can send friend request the following Attendees:");
        ArrayList<String> attendees = userManager.getAttendeeList();
        attendees.remove(username);
        System.out.println(attendees);
        System.out.println("You can send friend request to the following VIP's:");
        ArrayList<String> vips = userManager.getVIPList();
        vips.remove(username);
        System.out.println(vips);
    }


    /**
     * Print list of Organizers in the conference.
     * @param userManager - an UserManager instance
     */
    public void printOrganizers(UserManager userManager){
        System.out.println("You can message the following Organizers:");
        ArrayList<String> vips = userManager.getOrganizerList();
        vips.remove(username);
        System.out.println(vips);
    }

    /**
     * Print list of users attending the event
     * @param eventManager - an EventManager instance
     */
    public void printAttendeesEvent(EventManager eventManager, String eventName){
        System.out.println("The users enrolled in " + eventName + " are:");
        ArrayList<String> attendees = eventManager.getEventAttendees(eventName);
        System.out.println(attendees);
    }

    /**
     * Print list of all the events that Speaker is giving
     * @param eventManager - an EventManager instance
     * @param username - username of the user
     */
    public void printEvent(EventManager eventManager, String username){
        System.out.println("You are hosting following events:");
        ArrayList<String> events = eventManager.getEventListBySpeaker(username);
        System.out.println(events);
    }

    /**
     * Print notification of message archival.
     */
    public void printMessageArchived(){
        System.out.println("Message successfully archived.");
    }

    /**
     * Print conversation archival notification.
     */
    public void printConversationArchived(){
        System.out.println("Conversation successfully archived.");
    }

    /**
     * Print notification of conversation unarchived.
     */
    public void printMessageUnarchived(){
        System.out.println("Conversation successfully unarchived.");
    }

    /**
     * Print message deletion notification.
     */
    public void printMessageDeleted(){
        System.out.println("Message successfully deleted.");
    }

    /**
     * Print conversation deletion notification.
     */
    public void printConversationDeleted(){
        System.out.println("Conversation successfully deleted.");
    }

    /**
     * Print all archived conversations deletion notification
     */
    public void printArchiveDeletion(){
        System.out.println("All archived conversations successfully deleted.");
    }

    /**
     * Print all archived conversations deletion from user and receiver inboxes notification
     */
    public void printArchiveUserReceiverDeletion(){
        System.out.println("All archived conversations successfully deleted from both inboxes.");
    }

    /**
     * Print message deletion from user and receiver inboxes notification.
     */
    public void printMessageUserReceiverDelete(){
        System.out.println("Message deleted from both inboxes.");
    }

    /**
     * Print conversation deletion from user and receiver inboxes notification.
     */
    public void printConversationUserReceiverDelete(){
        System.out.println("Conversation deleted from both inboxes.");
    }

    /**
     * Print prompt for confirmation of user decision.
     */
    public void printConfirmationPrompt(){
        System.out.println("Enter Y to confirm this decision. It cannot be reversed. ");
    }

    /**
     * Print prompt for the number of a message in a conversation.
     */
    public void printMessageNumberPrompt(){
        System.out.println("Enter the number of this message in the conversation:");
    }

    /**
     * Print notification that the message the user chose does not exist.
     */
    public void printMessageDoesNotExist(){
        System.out.println("This message does not exist.");
    }

    /**
     * Prints conversation successfully marked as read.
     */
    public void printMarkedAsRead(){
        System.out.println("Conversation marked as read.");
    }

    /**
     * Prints conversation successfully marked as unread.
     */
    public void printMarkedAsUnread(){
        System.out.println("Conversation marked as unread.");
    }

    /**
     * Prints conversation does not exist.
     */
    public void printConversationDoesNotExist(){
        System.out.println("This conversation does not exist.");
    }

    /**
     * Print conversation cannot be marked as unread.
     */
    public void printCannotMarkUnread(){ System.out.println("This conversation cannot be marked as unread");}

    /**
     * Prints notification that conversation is already marked as read.
     */
    public void printConversationAlreadyRead() {System.out.println("This message is already marked as read.");}



    /**
     * Print message garbage bin successfully cleared.
     */
    public void printMessageBinCleared() { System.out.println("The message garbage bin has been successfully cleared.");}

    /**
     * Print list of friends
     * @param userManager - an UserManager
     * @param username - username of the user
     */
    public void printFriendList(UserManager userManager, String username){
        System.out.println("Friend list of " + username + ":");
        ArrayList<String> friends = userManager.getFriendList(username);
        System.out.println(friends);
    }

    /**
     * Print list of friend requests
     * @param userManager - an UserManager
     * @param username - username of the user
     */
    public void printFriendRequestList(UserManager userManager, String username){
        System.out.println("You have received friend requests from following users:");
        ArrayList<String> friends = userManager.getFriendRequest(username);
        System.out.println(friends);
    }

    /**
     * Print prompt for username of user to send friend request to
     */
    public void printSendFriendRequest() {
        System.out.println("Enter the username of the user you want to send friend request:");
    }

    /**
     * Print prompt for username of user to accept friend request
     */
    public void printAcceptFriendRequest() {
        System.out.println("Enter the username of the user you want to accept friend request:");
    }

    /**
     * Print notification that wanted user has been successfully added to friend list
     */
    public void printAcceptFriendRequestSuccess(String receiver) {
        System.out.println("You and " + receiver + " are now friends!");
    }
    /**
     * Print notification that wanted user has been failed added to friend list
     */
    public void printNotInFriendRequest(String receiver) {
        System.out.println(receiver + "is not in your friend request.");
    }
    /**
     * Print prompt for username of user to decline friend request
     */
    public void printDeclineFriendRequest() {
        System.out.println("Enter the username of the user you want to decline friend request:");
    }

    /**
     * Print notification that wanted user has been successfully declined
     */
    public void printDeclineFriendRequestSuccess(String receiver) {
        System.out.println("You declined friend request from " + receiver + ":(");
    }

    /**
     * Print notification that wanted user is not in friend list
     */
    public void printNotFriend(String receiverID) {
        System.out.println(receiverID + " is not in your friend list:(");
    }

    /**
     * Print notification that Speaker and Organizer cannot be friends
     */
    public void printCannotBeFriend(){
        System.out.println("You cannot add Speaker and Organizer as friend");
    }

    /**
     * Print Friend List Menu
     */
    public void printFriendListMenu(){
        System.out.println("Options:\n" +
                "0. Return to menu\n" +
                "1. View/manage received friend requests\n" +
                "2. Send friend requests\n" +
                "3. View your friend list\n" +
                "Enter 0, 1, 2, or 3: ");
    }

    /**
     * Print Friend Request Menu
     */
    public void printFriendRequestMenu(){
        System.out.println("Options:\n" +
                "0. Return to menu\n" +
                "1. Accept friend request\n" +
                "2. Decline friend requests\n" +
                "Enter 0, 1, or 2: ");
    }

    /**
     * Print that no username is in the system
     */
    public void printWrongUsername(String receiver){
        System.out.println("There is no user with username " + receiver);
    }
    /**
     * Print notification that wanted user is already in friend list or friend request list
     */
    public void printAlreadySent(){
        System.out.println("You already sent this user a request or you are already friends!");
    }

    /**
     * Displays menu for viewing messages fully deleted from the system (by both sender and receiver)
     * Displays a list of users with fully deleted messages.
     *
     * @param messageManager -
     */
    public void viewFullyDeletedMessages(MessageManager messageManager){
        System.out.println("Welcome to the garbage bin, of unloved messages unwanted by all...");
        System.out.println("Here are all messages deleted from the inboxes of both sender and receiver.");
        System.out.println("There are fully deleted messages involving these users: ");
        System.out.println(messageManager.getDeletedMessagesSenderReceivers());
        System.out.println("Enter a username to view the associated messages, 0 to clear the message bin, " +
                "or anything else to return to Message Menu");
    }

    /**
     * Display the fully deleted messages by a given user.
     *
     * @param messageManager - an instance of MessageManager
     * @param userID - the username of the user whose deleted messages you wish to view
     */
    public void viewUserFullyDeletedMessage(MessageManager messageManager, String userID){
        ArrayList<UUID> messages = messageManager.getFullyDeletedMessagesByUser(userID);
        for (UUID i: messages){
            String messageContent = messageManager.getMessageSender(i) + " sent to "
                    + messageManager.getMessageReceiver(i) + ":\n" + messageManager.getMessageContent(i) +
                    "\n           at " + messageManager.getMessageTime(i);
            System.out.println(messageContent);
        }
        System.out.println("Enter 0 to continue browsing the Message Bin, or anything " +
                "else to return to Message Menu.");
    }
}
