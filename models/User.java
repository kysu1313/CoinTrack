package models;

import controllers.AlertMessages;
import interfaces.GenericClassInterface;
import interfaces.GlobalClassInterface;
import interfaces.UserInterface;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents a user's account.
 * @author Kyle
 */
public class User implements GlobalClassInterface, GenericClassInterface, UserInterface{

    private final String USERNAME;
    private final String PASSWORD;
    private final ConnectToDatabase CONN;
    private final LinkedList<UserCoin> USER_COINS;
    private final HashMap<String, String> USER_DATA;
    private LinkedList<Object> objList;

    public User(String _username, String _password){
        this.USERNAME = _username;
        this.PASSWORD = _password;
        this.CONN = new ConnectToDatabase();
        this.USER_COINS = this.CONN.getSavedCoins(_username);
        this.USER_DATA = this.CONN.getUserInfo(_username);
        this.CONN.close();
    }

    // ============= GETTERS ============= //

    /**
     * Return a generic list of userCoin objects.
     * @return
     */
    @Override
    public LinkedList<Object> getGenericCoinList() {
        this.CONN.getSavedCoins(this.USERNAME).forEach((item) -> {
            Object obj = item;
            this.objList.add(obj);
        });
        return this.objList;
    }

    /**
     * Validates login parameters.
     * @return
     */
    public boolean validateLogin() {
        ConnectToDatabase conn = new ConnectToDatabase();
        boolean accepted = conn.validateLogin(this.USERNAME, this.PASSWORD);
        conn.close();
        return accepted;
    }

    /**
     * Sets the user to online or offline in the database.
     * @param _value
     */
    public void onlineStatus(int _value) {
        ConnectToDatabase conn = new ConnectToDatabase();
        conn.setUserOnlineStatus(this.USERNAME, 1);
        conn.close();
    }

    /**
     * Check password during registration for validity.
     * @param _password
     * @return
     */
    public static boolean isPasswordValid(String _password) {
        if(_password.length() < 8) {
            AlertMessages.showErrorMessage("Register User", "Password must be 8 characters long.");
            return true;
        }
        boolean isCapital = false;
        boolean isNumber = false;
        for (int i = 0; i < _password.length(); i++) {
           char ch = _password.charAt(i);

           if (Character.isUpperCase(ch)) {
               isCapital = true;
           } else if (Character.isDigit(ch)) {
               isNumber = true;
           }
        }
        if (!isCapital || !isNumber) {
            AlertMessages.showErrorMessage("Register User", "Password must contain a digit and an uppercase letter.");
        }
       return !isCapital && !isNumber;
    }

    @Override
    public String getUsername(){
        return this.USERNAME;
    }

    @Override
    public LinkedList<UserCoin> getSavedCoins(){
        return this.USER_COINS;
    }

    @Override
    public HashMap<String, String> getUserInfo(){
        return this.USER_DATA;
    }

    @Override
    public String getClassName() {
        return "User";
    }
}
