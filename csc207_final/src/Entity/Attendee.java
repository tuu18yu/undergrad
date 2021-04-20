package Entity;

/**
 * A class that represents an Attendee.
 */
public class Attendee extends UserAccount {
    /**
     * Create user account by given user name and password
     * @param userName- name of user
     * @param passWord- identify user by password
     */
    public Attendee(String userName, String passWord){
        super(userName, passWord);
        this.userType = "Attendee";
    }

}