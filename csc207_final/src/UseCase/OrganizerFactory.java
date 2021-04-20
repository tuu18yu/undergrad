package UseCase;

import Entity.Organizer;
import Entity.UserAccount;


/**
 * A helper class that manages Organizer accounts
 */
public class OrganizerFactory implements AccountFactory {
    /**
     * Creates a new Organizer
     *
     * @param userName The new user's username, *cannot be a duplicate within the system
     * @param password The new user's password
     * @return The newly created Organizer
     * @throws IllegalArgumentException If there is a duplicate userName
     */

    @Override
    public UserAccount createAccount(String userName, String password) {
        return new Organizer(userName, password);
    }
}
