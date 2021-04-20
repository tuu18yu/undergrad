package UseCase;

import Entity.UserAccount;

/**
 * A Class that manages accounts.
 */
public interface AccountFactory {

    UserAccount createAccount(String userName, String password);

}
