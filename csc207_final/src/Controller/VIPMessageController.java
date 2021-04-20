package Controller;

import UseCase.EventManager;
import UseCase.MessageManager;
import UseCase.UserManager;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class VIPMessageController extends MessageController{

    /**
     * Constructor for VIPMessageController
     * Creates a new instance of messagePresenter.
     *
     * @param username       String username of current user
     * @param myUserManager  instance of UserManager
     * @param messageManager instance of MessageManager
     */
    public VIPMessageController(String username, UserManager myUserManager, MessageManager messageManager, EventManager eventManager) {
        super(username, myUserManager, messageManager, eventManager);
    }

    /**
     * Calls MessagePresenter to print VIP message menu. Prompts for a response to a list of menu options.
     *
     * @return the String input from the User.
     */
    public String getVIPMessageMenu() {
        Scanner scan = new Scanner(System.in);
        messagePresenter.printVIPMessageMenu();
        return scan.nextLine();
    }

    /**
     * Prompts user for receiver ID and message content. Sends single message.
     * Can message any speaker/organizer/VIP, or attendee in your friend list
     */
    @Override
    public void sendMessage(){
        messagePresenter.printFriendList(myUserManager, username);
        messagePresenter.printEnrolledSpeakers(myUserManager, myEventManager, username);
        messagePresenter.printOrganizers(myUserManager);
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
     * Deletes a single message of a conversation, for VIP and conversation partner both.
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
     * Deletes entire conversation, for VIP and conversation partner both.
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
     * Deletes all archived messages, for VIP and conversation partner both.
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
     * Deletes archived messages with a given conversation partner, for VIP and conversation partner both.
     *
     * @param conversationPartner - username of conversation partner
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
}
