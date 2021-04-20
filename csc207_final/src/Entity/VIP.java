package Entity;

/**
 * A class that represents a VIP.
 */
public class VIP extends UserAccount {
    /**
     * Create VIP account by given username and password
     * @param userName- name of new user
     * @param passWord- password of said user
     */
    public VIP(String userName, String passWord){
        super(userName, passWord);
        this.userType = "VIP";
    }
}
