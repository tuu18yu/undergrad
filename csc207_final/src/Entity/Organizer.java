package Entity;

/**
 * A class that represents an Organizer.
 */
public class Organizer extends UserAccount {
    /**
     * Create user account by given user name and password
     * @param userName- name of user
     * @param passWord- identify user by password
     */
    public Organizer(String userName, String passWord){
        super(userName, passWord);
        this.userType = "Organizer";
    }
}