package models;
/**
 * This is user class which is parent class to other type of users and provide basic functions such as login
 * @author Dai Jiayu
 * @version 1.0
 */
public abstract class User {
    protected String userID;
    protected String name;
    protected String password;

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
        this.password = "password";
    }

    public boolean login(String userID, String password) {
        return this.userID.equals(userID) && this.password.equals(password);
    }

    public void logout() {
        System.out.println(name + " has logged out.");
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password changed successfully.");
    }

    public String getUserID() { return userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
