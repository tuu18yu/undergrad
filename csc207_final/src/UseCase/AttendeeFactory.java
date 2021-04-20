package UseCase;

import Entity.Attendee;
import Entity.UserAccount;

/**
 * A Class that manages Attendee accounts
 */
public class AttendeeFactory implements AccountFactory {
    /**
     * Creates a new Attendee
     *
     * @param userName The new user's username, *cannot be a duplicate within the system
     * @param password The new user's password
     * @return The newly created Attendee
     * @throws IllegalArgumentException If there is a duplicate userName
     */
    @Override
    public UserAccount createAccount(String userName, String password) {
        return new Attendee(userName, password);
    }
}
