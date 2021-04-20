package Entity;

/**
 * A class that represents a Speaker.
 */
public class Speaker extends UserAccount {
    /**
     * Create user account by given user name and password
     * @param userName- name of user
     * @param passWord- identify user by password
     */
    public Speaker(String userName, String passWord){
        super(userName, passWord);
        this.userType = "Speaker";
    }
}