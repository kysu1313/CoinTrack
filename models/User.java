package models;

import coinTrack.FXMLDocumentController;
import static coinTrack.FXMLDocumentController.uname;
import controllers.AlertMessages;
import controllers.assistantControllers.TabAssistantController;
import interfaces.GenericClassInterface;
import interfaces.GlobalClassInterface;
import interfaces.UserInterface;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * This class represents a user's account.
 * @author Kyle
 * @param <T>
 */
public class User<T> implements GlobalClassInterface, GenericClassInterface, UserInterface{

    private String username;
    private String password;
    private String email;
    private String path;
    private final int USER_ID;
    private final ConnectToDatabase CONN;
    private final LinkedList<UserCoin> USER_COINS;
    private final HashMap<String, String> USER_DATA;
    private boolean isDataSet;
    private LinkedList<Object> objList;
    private CoinRankApi cri;
    private CoinHistory coinHist;
    private LinkedHashMap<Double, String> coinHistList;
    private LinkedList<SingleCoin> coinList;
    private LinkedList<SingleCoin> sortedList;
    private LinkedList<String> friendList;
    private LinkedList<T> tList;
    private TabAssistantController tas;
    private LinkedList<SingleCoin> userSingleCoins;
    private LinkedHashMap<Double, String> userHistoryMap;
    private LinkedList<LinkedHashMap<Double, String>> linkedUserHistoryMap;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;

    /**
     * Create a new user and build the basic coin lists.
     * @param _username
     * @param _password
     */
    public User(String _username, String _password){
        this.username = _username;
        this.password = _password;
        this.CONN = new ConnectToDatabase();
        this.USER_ID = this.CONN.getUserId(uname);
        this.USER_COINS = this.CONN.getSavedCoins(_username);
        this.USER_DATA = this.CONN.getUserInfo(_username);
        this.isDataSet = false;
    }

    /**
     * This is mainly user for resetting a forgotten password / email / username.
     */
    public User() {
        this.username = "";
        this.password = "";
        this.CONN = null;
        this.USER_ID = -1;
        this.USER_COINS = null;
        this.USER_DATA = null;
    }

    /**
     * Initialize all of the users data fields so they can be accessed quickly.
     * This also helps prevent over-usage of our api calls.
     */
    @Override
    public void createData() {
        this.cri = new CoinRankApi();
        this.coinHist = new CoinHistory();
        this.coinHist.start();
        this.cri.start();
        this.coinHist.join();
        this.cri.join();
        this.coinHistList = this.coinHist.getSingleHistory();
        this.coinList = this.cri.getCoinList();
        this.tList = this.cri.getTList();
        this.sortedList = this.cri.getSortedCoinList();
        this.friendList = this.CONN.getFriendList(this.username);
        this.CONN.close();
        this.tas = new TabAssistantController();
        this.tas.setCoinList(this.coinList);
        this.tas.setCurrentUser(this);
        this.tas.setSingleHistoryMap(this.coinHistList);
        this.tas.setUserCoinList(this.USER_COINS);
        this.tas.setUserSingleCoins(this.USER_COINS);
        this.userSingleCoins = new LinkedList<>();
        this.isDataSet = true;
    }

    /**
     * Save a coin with current username.
     * @param _coinID
     */
    @Override
    public void saveCoin(int _coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(this.username, _coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }

    /**
     * Validates login parameters.
     * @return
     */
    @Override
    public boolean validateLogin() {
        ConnectToDatabase conn = new ConnectToDatabase();
        boolean accepted = conn.validateLogin(this.username, this.password);
        conn.close();
        return accepted;
    }

    /**
     * Sets the user to online or offline in the database.
     * @param _value
     */
    @Override
    public void onlineStatus(int _value) {
        ConnectToDatabase conn = new ConnectToDatabase();
        if(_value == 1){
        conn.setUserOnlineStatus(this.username, 1);
        }
        else{
        conn.setUserOnlineStatus(this.username, 0);
        }
        conn.close();
    }

    /**
     * Gets the profile picture from the database
     * @param _name
     * @return
     */
    @Override
    public String getPicturePath(String _name) {
        ConnectToDatabase conn = new ConnectToDatabase();
        path = conn.getPicturePath(_name);
        conn.close();
        return path;
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

    /**
     * Reset password for given username.
     * @param _username
     * @param _newPass
     */
    @Override
    public void resetPassword(String _username, String _newPass) {
        ConnectToDatabase conn = new ConnectToDatabase();
        // Submit changed passwords to the database
        conn.changePassword(_username, _newPass);
        // Close the connection
        conn.close();
    }

    /**
     * Send an email to the users email address containing their username.
     * @param _email
     * @return
     */
    @Override
    public boolean forgotUsernameEmail(String _email) {
        ConnectToDatabase conn = new ConnectToDatabase();
        if (conn.emailExists(_email)) {
            String tmpUname = conn.getUsernameFromEmail(_email);
            if (DEBUG) {
                System.out.println(tmpUname);
            }
            conn.close();
            int code = 0;
            Email sendMail = new Email(_email, tmpUname, code);
            return true;
        } else {
            return false;
        }
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

    /**
     * Return a generic list of userCoin objects.
     * @return
     */
    @Override
    public LinkedList<Object> getGenericCoinList() {
        this.CONN.getSavedCoins(this.username).forEach((item) -> {
            Object obj = item;
            this.objList.add(obj);
        });
        return this.objList;
    }

    /**
     * Returns a linked list of SingleCoin objects created from
     * the UserCoin list pulled from database.
     * @return
     */
    @Override
    public LinkedList<SingleCoin> getUserSingleCoins() {
        if (this.userSingleCoins.isEmpty()) {
            this.coinList.forEach((item) -> {
                this.USER_COINS.forEach((entry) -> {
                    if (item.getName().equalsIgnoreCase(entry.getName())) {
                        this.userSingleCoins.add(item);
                    }
                });
            });
        }
        return this.userSingleCoins;
    }

    /**
     * Returns the linked list of coin historical data for the given timeframe.
     * @param _timeframe
     * @return
     */
    @Override
    public LinkedList<LinkedHashMap<Double, String>> getLinkedUserHistoryMap(String _timeframe) {
        this.linkedUserHistoryMap = new LinkedList<>();
        this.USER_COINS.forEach((item) -> {
            CoinHistory coinHistory = new CoinHistory(item.getCoinID(), item.getName(), _timeframe);
            if (coinHistory.checkValidData()) {
                coinHistory.getData();
                this.userHistoryMap = coinHistory.getSingleHistory();
                this.linkedUserHistoryMap.add(this.userHistoryMap);
            }
        });
        return this.linkedUserHistoryMap;
    }

    /**
     * Create list of singleCoins from the list of userCoins.
     * @return
     */
    @Override
    public LinkedList<SingleCoin> getSavedSingleCoins() {
        LinkedList<SingleCoin> savedSingleCoins = new LinkedList<>();
        this.coinList.forEach(item -> {
            this.USER_COINS.forEach(coin -> {
                if (item.getSymbol().equals(coin.getName())) {
                    savedSingleCoins.add(item);
                }
            });
        });
        return savedSingleCoins;
    }

    /**
     * Create list of T from SingleCoin list.
     * @param _list
     * @return
     */
    public LinkedList<T> createTListFronSingleCoins(LinkedList<Object> _list) {
        LinkedList<T> tlist = new LinkedList<>();
        _list.forEach(item -> {
            tlist.add((T)item);
        });
        return tlist;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
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
    public LinkedHashMap<Double, String> getCoinHistoryList() {
        return this.coinHistList;
    }

    @Override
    public LinkedList<SingleCoin> getCoinList() {
        return this.coinList;
    }

    public LinkedList<T> getTList() {
        return this.tList;
    }

    @Override
    public LinkedList<SingleCoin> getSortedCoinList() {
        return this.sortedList;
    }

    @Override
    public LinkedList<String> getFriendList() {
        return this.friendList;
    }

    @Override
    public LinkedList<UserCoin> getUserCoinList() {
        return this.USER_COINS;
    }

    @Override
    public TabAssistantController getTas() {
        return this.tas;
    }

    @Override
    public boolean getIsDataSet() {
        return this.isDataSet;
    }

    @Override
    public String getClassName() {
        return "User";
    }
}
