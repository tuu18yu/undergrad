package Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A class that represents a Message.
 */
public class Message implements Serializable, Comparable<Message> {
    private final UUID id; // id of message
    private final String sender;// Stores the name of the sender
    private String receiver;// Stores the name of the receiver
    private String content;// Stores the content of the message
    private final LocalDateTime time = LocalDateTime.now();// time when the message create
    private boolean readStatus = false; // Stores if the message has been read by the receiver
    private boolean archivedBySender = false; // Stores if the message has been archived by the sender
    private boolean archivedByReceiver = false; // Stores if the message has been archived by the receiver
    private boolean deletedBySender = false; // Stores if the message has been deleted by the sender
    private boolean deletedByReceiver = false; // Stores if the message has been deleted by the receiver


    /**
     * Create new message
     * @param sender- sender of this message
     * @param receiver- receiver of this message
     * @param content- content of this message
     */
    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.id = UUID.randomUUID();
    }

    /**
     * Getter of sender
     * @return sender name
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter of receiver
     * @return receiver name
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Setter of receiver
     * @param receiver- receiver name
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Getter of content
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter of content
     * @param content- content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter of id
     * @return message id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Getter of time
     * @return time when the message sends
     */
    public LocalDateTime getTime() {
        return time;
    }

    public int compareTo(Message msg) {
        return this.time.compareTo(msg.time);
    }

    /**
     * Getter for read status of the message.
     *
     * @return true if message has been read, false if not.
     */
    public boolean isReadStatus() { return readStatus;}

    /**
     * Getter for sender archive status of message.
     *
     * @return true if message archived by sender, false if not.
     */
    public boolean isArchivedBySender() { return archivedBySender;}

    /**
     * Getter for receiver archive status of message.
     *
     * @return true if message archived by receiver, false if not.
     */
    public boolean isArchivedByReceiver() {return archivedByReceiver;}

    /**
     * Getter for sender delete status of message.
     *
     * @return true if message deleted by sender, false if not.
     */
    public boolean isDeletedBySender() {return deletedBySender;}

    /**
     * Getter for receiver delete status of message.
     *
     * @return true if message deleted by receiver, false if not.
     */
    public boolean isDeletedByReceiver() {return deletedByReceiver;}

    /**
     * Marks message as read (by receiver). Sets value of readStatus to true.
     */
    public void markAsRead() { readStatus = true;}

    /**
     * Marks message as unread (by receiver). Sets value of readStatus to false.
     */
    public void markAsUnread() {readStatus = false;}

    /**
     * Sets value of sender archive status to true.
     */
    public void markAsSenderArchive() { archivedBySender = true;}

    /**
     * Sets value of sender archive status to false.
     */
    public void unMarkAsSenderArchive() { archivedBySender = false;}

    /**
     * Sets value of receiver archive status to true.
     */
    public void markAsReceiverArchive() { archivedByReceiver = true;}

    /**
     * Sets value of receiver archive status to false.
     */
    public void unMarkAsReceiverArchive() { archivedByReceiver = false;}

    /**
     * Sets value of sender delete status to true. Irreversible action.
     * Sender archive status will be set as false, because message cannot be deleted
     * and archived by the sender at the same time.
     */
    public void markAsSenderDeleted() {
        deletedBySender = true;
        archivedBySender = false;
    }

    /**
     * Sets value of receiver delete status to true. Irreversible action.
     * Sender archive status will be set as false, because message cannot be deleted
     * and archived by the receiver at the same time.
     */
    public void markAsReceiverDeleted() {
        deletedByReceiver = true;
        archivedByReceiver = false;
    }
}