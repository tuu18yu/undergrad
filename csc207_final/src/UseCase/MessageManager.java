package UseCase;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Entity.Message;

/**
 * A class that stores, manages, creates messages
 * UseCase.MessageManager stores a map of message IDs to messages
 * stores ID of user sending messages
 * Getters for message sender, receiver, time, and content from messageID
 * Getter for message history of current user
 * Send messages or reply to messages
 *
 * @author Wenying Wu
 */

public class MessageManager implements Serializable {

    private HashMap<UUID, Message> systemMessages = new HashMap<>(); // Stores undeleted messages
    private HashMap<UUID, Message> deletedMessages = new HashMap<>(); // Stores messages deleted by receiver and sender
    private transient String senderID;

    /**
     * Sets sender ID.
     * @param senderID - message sender's ID
     */
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    /**
     * Creates a message. Other methods call this method
     * to 'send' a message. This does not check whether or not
     * the recipientID is valid.
     * @param receiverID - Username of the message receiver.
     * @param messageContent - The message text.
     */
    public boolean createMessage(String receiverID, String messageContent){
        Message newMessage = new Message(senderID, receiverID, messageContent);
        systemMessages.put(newMessage.getId(), newMessage);
        return true;
    }

    /**
     * @param messageID - ID of the message.
     * @return the message object that corresponds to the messageID
     */
    private Message getMessage(UUID messageID){
        if (systemMessages.get(messageID)==null){
            return deletedMessages.get(messageID);
        } return systemMessages.get(messageID);
    }

    /**
     * @param messageID - ID of the message
     * @return true if message is marked as read, false if not.
     */
    private boolean getMessageReadStatus(UUID messageID) { return getMessage(messageID).isReadStatus();}

    /**
     * @param messageID - ID of the message.
     * @return sender of message
     */
    public String getMessageSender(UUID messageID){
        return getMessage(messageID).getSender();
    }

    /**
     * @param messageID - ID of the message.
     * @return receiver of message
     */
    public String getMessageReceiver(UUID messageID){
        return getMessage(messageID).getReceiver();
    }

    /**
     * @param messageID - ID of the message.
     * @return content of message
     */
    public String getMessageContent(UUID messageID){
        return getMessage(messageID).getContent();
    }

    /**
     * @param messageID - ID of the message.
     * @return time of message
     */
    public String getMessageTime(UUID messageID){
        LocalDateTime messageTime = getMessage(messageID).getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        return messageTime.format(formatter);
    }

    /**
     * Helper method, returns message history of single account
     *
     * @param userID - Username of the user.
     * @return Arraylist containing all messageIDs in account's message history
     *          as both sender and receiver, sorted by time.
     */
    private ArrayList<UUID> getMessages(String userID) {
        ArrayList<Message> messageHistory = new ArrayList<>();
        for (Message m : systemMessages.values()) {
            if (m.getSender().equals(userID) || m.getReceiver().equals(userID)){
                messageHistory.add(m);}
        }
        Collections.sort(messageHistory);
        ArrayList<UUID> messageIDs = new ArrayList<>();
        for (Message m: messageHistory){
            messageIDs.add(m.getId());
        }
        return messageIDs;
    }

    /**
     * Get message history of current user
     *
     * @return ArrayList containing all messageIDs of all sent or received
     * messages of current user. Does not include archived or deleted messages.
     */
    private ArrayList<UUID> getVisibleSenderMessages(){
        ArrayList<UUID> allSenderMessages = new ArrayList<>();
        for (UUID m: getMessages(senderID)){
            if (isMessageArchived(m)||isMessageDeleted(m)) {
                continue;
            } allSenderMessages.add(m);
        }
        return allSenderMessages;
    }

    /**
     * Get archived messages of current user
     *
     * @return ArrayList containing all messageIDs of all ARCHIVED sent or received
     * messages of current user.
     */
    private ArrayList<UUID> getArchivedSenderMessages(){
        ArrayList<UUID> archivedMessages = new ArrayList<>();
        for (UUID m: getMessages(senderID)){
            if (isMessageArchived(m)){
                archivedMessages.add(m);
            }
        }
        return archivedMessages;
    }



    /**
     * Checks if current user is sender of message
     *
     * @return true if current user is sender of the message
     */

    public boolean isSender(UUID messageID) {
        return senderID.equals(getMessageSender(messageID));
    }

    /**
     * Checks if current user is receiver of message
     *
     * @return true if current user is receiver of message
     */
    public boolean isReceiver(UUID messageID){
        return senderID.equals(getMessageReceiver(messageID));
    }

    /**
     * Generates list of current user's messages that are marked as unread.
     *
     * @return list of UUIDs of user's unread messages.
     */
    public ArrayList<UUID> getUnreadSenderMessages(){
        ArrayList<UUID> unarchivedMessages = getVisibleSenderMessages();
        ArrayList<UUID> unreadMessages = new ArrayList<>();
        for (UUID i : unarchivedMessages){
            if ((!getMessageReadStatus(i))&&isReceiver(i)){
                unreadMessages.add(i);
            }
        }
        return unreadMessages;
    }

    /**
     * Helper method, generates list of string usernames of conversation partners that current user has
     * based of a list of messages.
     *
     * @param messages - list of UUID message IDs that the current user is sender or receiver of.
     * @return a list of usernames of conversation partners
     */
    private ArrayList<String> getChatHeadsList(ArrayList<UUID> messages){
        ArrayList<String> conversations = new ArrayList<>();
        for (UUID i: messages){
            if (isSender(i)){
                conversations.add(getMessage(i).getReceiver());
            } else {conversations.add(getMessage(i).getSender());}
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(conversations);
        return new ArrayList<>(hashSet);
    }

    /**
     * Generates list of current user's unarchived chat partners
     *
     * @return list of String usernames that current user has unarchived messages with.
     */
    public ArrayList<String> getSenderConversations(){
        ArrayList<UUID> messages = getVisibleSenderMessages();
        return getChatHeadsList(messages);
    }

    /**
     * Generates a list of current user's archived chat partners.
     *
     * @return list of String usernames that current user has archived messages with.
     */
    public ArrayList<String> getSenderArchivedConversations(){
        ArrayList<UUID> messages = getArchivedSenderMessages();
        return getChatHeadsList(messages);
    }

    public ArrayList<String> getSenderUnreadConversations(){
        ArrayList<UUID> messages = getUnreadSenderMessages();
        return getChatHeadsList(messages);
    }

    /**
     * Helper method, generates a list of message IDs that a given user has participated in,
     *
     * @param otherID - username of chat partner
     * @param conversation - list of messages that current user has participated in
     * @return list of message IDs from given list that the given chat partner has participated in.
     */
    private ArrayList<UUID> getSingleConversationHelper(String otherID, ArrayList<UUID> conversation){
        ArrayList<UUID> singleConversation = new ArrayList<>();
        for (UUID i: conversation){
            if (otherID.equals(getMessage(i).getReceiver()) || otherID.equals(getMessage(i).getSender())) {
                singleConversation.add(i);
            }
        } return singleConversation;
    }

    /**
     * Return list of message IDs that comprise the message history between
     * the current user of the program and a given user.
     *
     * Does not include archived messages.
     *
     * @param otherID - Username of the other user.
     * @return list of UUID message IDs.
     */
    public ArrayList<UUID> getSingleConversationByReceiver(String otherID){
        ArrayList<UUID> conversation = getVisibleSenderMessages();
        return getSingleConversationHelper(otherID, conversation);
    }

    /**
     * Return list of message IDs that comprise the archived message history between
     * the current user of the program and a giver user.
     *
     * @param otherID - username of other user
     * @return list of UUID message IDs
     */
    public ArrayList<UUID> getArchivedConversationByReceiver(String otherID){
        ArrayList<UUID> conversation = getArchivedSenderMessages();
        return getSingleConversationHelper(otherID, conversation);
    }

    /**
     * Mark a single message as archived.
     *
     * @param messageID - UUID of the message to be archived.
     */
    public void archiveSingleMessage(UUID messageID){
        if (isSender(messageID)) {
            getMessage(messageID).markAsSenderArchive();
        }
        else if (isReceiver(messageID)){
            getMessage(messageID).markAsReceiverArchive();
        }
    }

    /**
     * Archive an entire conversation.
     *
     * @param otherID - the username of the conversation partner.
     */
    public void archiveConversation(String otherID){
        ArrayList<UUID> conversation = getSingleConversationByReceiver(otherID);
        for (UUID messageID: conversation){
            archiveSingleMessage(messageID);
        }

    }

    /**
     * Un-archive a single message.
     *
     * @param messageID - UUID of the message to be archived.
     */
    public void unArchiveSingleMessage(UUID messageID){
        if (isSender(messageID)) {
            getMessage(messageID).unMarkAsSenderArchive();
        }
        else if (isReceiver(messageID)){
            getMessage(messageID).unMarkAsReceiverArchive();
        }
    }

    /**
     * Un-archive an entire conversation.
     *
     * @param otherID - the username of the conversation partner.
     */
    public void unArchiveConversation(String otherID){
        ArrayList<UUID> conversation = getArchivedConversationByReceiver(otherID);
        for (UUID messageID: conversation){
            unArchiveSingleMessage(messageID);
        }

    }

    /**
     * Mark single message as deleted.
     *
     * @param messageID - UUID of the message to be deleted.
     */
    public void deleteSingleMessage(UUID messageID){
        Message toBeDeleted = getMessage(messageID);
        if (isSender(messageID)) {
            toBeDeleted.markAsSenderDeleted();
        } else if (isReceiver(messageID)) {
            toBeDeleted.markAsReceiverDeleted();
        }
        sendToDeletedBin(toBeDeleted);

    }

    /**
     * Checks if both users have deleted a message. If so, move message from
     * system messages (active messages) to deleted messages.
     *
     * @param toBeDeleted - Message to be checked for full deletion.
     */
    public void sendToDeletedBin(Message toBeDeleted){
        if (toBeDeleted.isDeletedBySender()&&toBeDeleted.isDeletedByReceiver()) {
            UUID fullyDeletedID = toBeDeleted.getId();
            Message fullyDeleted = systemMessages.remove(fullyDeletedID);
            deletedMessages.put(fullyDeletedID, fullyDeleted);
        }
    }

    /**
     * Marks message as deleted for both users.
     *
     * @param messageID - ID of the message to be deleted
     */
    public void deleteSingleMessageBothSides(UUID messageID){
        Message toBeDeleted = getMessage(messageID);
        toBeDeleted.markAsSenderDeleted();
        toBeDeleted.markAsReceiverDeleted();
        sendToDeletedBin(toBeDeleted);
    }

    /**
     * Mass mark messages from given list of UUIDs as deleted by current user
     *
     * @param conversation - list of UUID of messages
     */
    public void massDeleteMessages(ArrayList<UUID> conversation){
        for (UUID messageID: conversation){
            deleteSingleMessage(messageID);
        }
    }

    /**
     * Mass mark messages from given list of UUIDs as deleted by both users.
     *
     * @param conversation - list of UUID of messages
     */
    public void massDeleteMessagesBothSides(ArrayList<UUID> conversation){
        for (UUID messageID: conversation){
            deleteSingleMessageBothSides(messageID);
        }
    }

    /**
     * Mark an entire unarchived conversation as deleted.
     *
     * @param otherID - the username of the conversation partner.
     */
    public void deleteConversation(String otherID){
        ArrayList<UUID> conversation = getSingleConversationByReceiver(otherID);
        massDeleteMessages(conversation);
    }

    /**
     * Mark an entire unarchived conversation as deleted by both users.
     *
     * @param otherID - the username of the conversation partner
     */
    public void deleteConversationBothSides(String otherID){
        ArrayList<UUID> conversation = getSingleConversationByReceiver(otherID);
        massDeleteMessagesBothSides(conversation);
    }

    /**
     * Mark an entire archived conversation as deleted.
     *
     * @param otherID - the username of the conversation partner.
     */
    public void deleteArchivedConversation(String otherID){
        ArrayList<UUID> conversation = getArchivedConversationByReceiver(otherID);
        massDeleteMessages(conversation);
    }

    /**
     * Mark an entire archived conversation as deleted by both sides.
     *
     * @param otherID - username of the conversation partner.
     */
    public void deleteArchivedConversationBothSides(String otherID){
        ArrayList<UUID> conversation = getArchivedConversationByReceiver(otherID);
        massDeleteMessagesBothSides(conversation);
    }

    /**
     * Mark all archived conversations as deleted.
     */
    public void deleteAllArchivedConversations(){
        ArrayList<UUID> history = getArchivedSenderMessages();
        massDeleteMessages(history);
    }

    /**
     * Mark all archived conversations as deleted.
     */
    public void deleteAllArchivedConversationsBothSides(){
        ArrayList<UUID> history = getArchivedSenderMessages();
        massDeleteMessagesBothSides(history);
    }

    /**
     * Permanently deletes all messages deleted by both receiver and sender.
     */
    public void emptyDeletedMessages(){
        deletedMessages.clear();
    }

    /**
     * Checks if user has archived given message.
     *
     * @param messageID - UUID of the message to be checked.
     * @return true if message archived by user, false if not.
     */
    public boolean isMessageArchived(UUID messageID){
        if (isSender(messageID)) {
            return getMessage(messageID).isArchivedBySender();
        } else {
            return getMessage(messageID).isArchivedByReceiver();
        }
    }

    /**
     * Checks if user has deleted given message.
     *
     * @param messageID - UUID of message to be checked.
     * @return true if message deleted by user, false if not.
     */
    public boolean isMessageDeleted(UUID messageID){
        if (isSender(messageID)){
            return getMessage(messageID).isDeletedBySender();
        } else{
            return getMessage(messageID).isDeletedByReceiver();
        }
    }

    /**
     * Marks all unread messages in message history with given chat partner as read.
     *
     * @param otherID - username of chat partner
     */
    public void markConversationAsRead(String otherID){
        ArrayList<UUID> conversation = getSingleConversationHelper(otherID, getUnreadSenderMessages());
        for (UUID i: conversation){
            getMessage(i).markAsRead();
        }
    }

    /**
     * Marks conversation with given chat partner as unread.
     *
     * @param otherID - username of chat partner
     * @return true if conversation successfully marked as unread, false if given chat partner does not have an
     * unarchived conversation with current user or user is not a recipient in the conversation.
     */
    public boolean markConversationAsUnread(String otherID){
        if (!getSenderConversations().contains(otherID)){
            return false;
        }
        ArrayList<UUID> conversation = getSingleConversationByReceiver(otherID);
        UUID someMessage = conversation.get(0);
        getMessage(someMessage).markAsUnread();
        return getSenderUnreadConversations().contains(otherID);
    }

    /**
     * Given a list of messages, generates a list of all of the senders and receivers of the messages in that list.
     *
     * @param messages - list of message IDs
     * @return unrepeated list of string usernames of the senders of the messages in the list
     */
    private ArrayList<String> getListOfMessageSenderReceivers(ArrayList<UUID> messages){
        ArrayList<String> conversations = new ArrayList<>();
        for (UUID i: messages){
            conversations.add(getMessage(i).getSender());
            conversations.add(getMessage(i).getReceiver());
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(conversations);
        return new ArrayList<>(hashSet);
    }

    /**
     * Gets a list of usernames of users whose messages have been fully deleted from both
     * the sender and the receiver inboxes.
     *
     * @return list of string usernames
     */
    public ArrayList<String> getDeletedMessagesSenderReceivers(){
        ArrayList<UUID> messageList = new ArrayList<>(deletedMessages.keySet());
        return getListOfMessageSenderReceivers(messageList);
    }

    /**
     * Given a username, gets list of messages from fully deleted messages that have said user as receiver or sender.
     *
     * @param messenger username of User
     * @return UUID list of fully deleted messages that involve given messenger, unsorted.
     */
    public ArrayList<UUID> getFullyDeletedMessagesByUser(String messenger){
        ArrayList<UUID> messageList = new ArrayList<>();
        for (UUID i: deletedMessages.keySet()){
            String one = getMessageSender(i);
            String two = getMessageReceiver(i);
            if (one.equals(messenger)||two.equals(messenger)){
                messageList.add(i);
            }
        }
        return messageList;
    }

}
