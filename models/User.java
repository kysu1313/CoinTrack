package models;

import static coinTrack.FXMLDocumentController.uname;
import controllers.AlertMessages;
import interfaces.GenericClassInterface;
import interfaces.GlobalClassInterface;
import interfaces.UserInterface;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * This class represents a user's account.
 * @author Kyle
 */
public class User implements GlobalClassInterface, GenericClassInterface, UserInterface{

    private final String USERNAME;
    private final String PASSWORD;
    private final int USER_ID;
    private final ConnectToDatabase CONN;
    private final LinkedList<UserCoin> USER_COINS;
    private final HashMap<String, String> USER_DATA;
    private LinkedList<Object> objList;

    public User(String _username, String _password){
        this.USERNAME = _username;
        this.PASSWORD = _password;
        this.CONN = new ConnectToDatabase();
        this.USER_ID = this.CONN.getUserId(uname);
        this.USER_COINS = this.CONN.getSavedCoins(_username);
        this.USER_DATA = this.CONN.getUserInfo(_username);
        this.CONN.close();
    }

    public void saveCoin(int _coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(this.USERNAME, _coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }

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


        // ============= GETTERS ============= //

    /**
     * Checks if a username already exists in database.

     * @return
     */
    public static boolean usernameAcceptable(String _uname, String _password, String _email, String _imgPath, Text _registerText) {
        // Call DB connection class
        ConnectToDatabase conn = new ConnectToDatabase();
        // Check is username exists in DB
        if (!conn.usernameExists(_uname) && !conn.emailExists(_email)) {
            // If all good, submit info to DB
            conn.userDatabase(0, _email, _uname, _password, _imgPath);
            conn.close();
            // Save username so it can be displayed in the application
            // After login is successful, you are taken to the main page
            _registerText.setFill(Color.GREEN);
            _registerText.setText("SUCCESS!");
            return true;
        } else if (conn.usernameExists(_uname)){
            AlertMessages.showErrorMessage("Register User", "Username already taken. Try another one.");
            _registerText.setText("Username already taken. Try another one.");
            _registerText.setFill(Color.RED);
            return false;
        } else
            AlertMessages.showErrorMessage("Register User", "Email already taken. Try another one.");
            _registerText.setText("Email already taken. Try another one.");
            _registerText.setFill(Color.RED);
            return false;
    }


    @Override
    public String getUsername(){
        return this.USERNAME;
    }

    public int getUserID() {
        return this.USER_ID;
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
