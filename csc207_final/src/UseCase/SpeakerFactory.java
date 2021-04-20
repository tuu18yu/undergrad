package UseCase;

import Entity.Speaker;
import Entity.UserAccount;

/**
 * A Class that manages Speaker accounts
 */
public class SpeakerFactory implements AccountFactory {
    /**
     * Creates a new Speaker
     *
     * @param userName The new user's username, *cannot be a duplicate within the system
     * @param password The new user's password
     * @return The newly created Speaker
     * @throws IllegalArgumentException If there is a duplicate userName
     */
    @Override
    public UserAccount createAccount(String userName, String password) {
        return new Speaker(userName, password);
    }
}