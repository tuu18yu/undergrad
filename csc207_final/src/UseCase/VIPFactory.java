package UseCase;

import Entity.VIP;
import Entity.UserAccount;

/**
 * A Class that manages VIP accounts
 */
public class VIPFactory implements AccountFactory {
    /**
     * Creates a new VIP
     *
     * @param userName The new user's username, *cannot be a duplicate within the system
     * @param password The new user's password
     * @return The newly created VIP
     */
    @Override
    public UserAccount createAccount(String userName, String password) {
        return new VIP(userName, password);
    }
}
