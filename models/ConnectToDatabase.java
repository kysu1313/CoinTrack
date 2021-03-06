package models;
/**
 * Database connection class.
 * The database is hosted on freemysqlhosting.net
 *
 * Constructor just opens a connection.
 * The methods allow interaction with specific tables.
 *
 * If you open it. You MUST close it!
 *
 * - Kyle, Parth
 */

import interfaces.DatabaseInterface;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import controllers.AlertMessages;
import java.util.HashMap;

public class ConnectToDatabase implements DatabaseInterface{

    private Connection con;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;

    /**
     * CONSTRUCTOR
     * Makes a simple connection to the database.
     */
    public ConnectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://sql9.freemysqlhosting.net:3306/sql9364184", "sql9364184", "vLIxP8hK8C");
            if (DEBUG){System.out.println("DB connection successful");}
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Insert data into the "all_coins" table.
     *
     * @param _coinID
     * @param _uuid
     * @param _slug
     * @param _symbol
     * @param _name
     * @param _numMarkets
     * @param _numExchanges
     * @param _volume
     * @param _marketCap
     * @param _price
     * @param _change
     * @param _coinRank
     */
    @Override
    public void addCoinToDatabase(int _coinID, String _uuid, String _slug,
                            String _symbol, String _name,
                            int _numMarkets, int _numExchanges,
                            int _volume, int _marketCap, String _price,
                            double _change, int _coinRank) {
        try {
            // Parse price to a float because DB requires it.
            float newPrice = Float.parseFloat(_price);
            // Insert statement, using prepared statements
            String query = " insert into all_coins (coinID, uuid, slug, \n"
                                                + "symbol, name, "
                                                + "numMarkets, numExchanges, \n"
                                                + "volume, marketCap, price, \n"
                                                + "changePrice, coinRank)"
                                                + " values (?, ?, ?, ?, ?, ?, ?, "
                                                + "?, ?, ?, ?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setInt(1, _coinID);
            preparedStmt.setString(2, _uuid);
            preparedStmt.setString(3, _slug);
            preparedStmt.setString(4, _symbol);
            preparedStmt.setString(5, _name);
            preparedStmt.setInt(6, _numMarkets);
            preparedStmt.setInt(7, _numExchanges);
            preparedStmt.setInt(8, _volume);
            preparedStmt.setInt(9, _marketCap);
            preparedStmt.setString(10, _price);
            preparedStmt.setDouble(11, _change);
            preparedStmt.setInt(12, _coinRank);
            // execute the preparedstatement if coin is not already in table.
            if (DEBUG){System.out.println("checking coin in db " + _name);}
            if (!checkIfCoinExists(_uuid)) {
                if (DEBUG){System.out.println("Adding coin: " + _coinID + " exists. ");}
                if (DEBUG){System.out.println("adding " + _name);}
                preparedStmt.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Check if a coin already exists in the database.
     * @param _uuid
     * @return
     */
    @Override
    public boolean checkIfCoinExists(String _uuid) {
        boolean doesExist = false;
        try {
            // Insert statement, using prepared statements
            String query = " SELECT * FROM all_coins WHERE uuid = '" + _uuid + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            // execute the preparedstatement
            ResultSet result = preparedStmt.executeQuery(query);
            doesExist = result.next();
            if (DEBUG){System.out.println("Checking if coin: " + _uuid + " exists. ");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return doesExist;
    }

    /**
     * Update price, change, and volume in the all_coins table in the database.
     * @param _id
     * @param _newPrice
     * @param _newChange
     * @param _newVolume
     */
    @Override
    public void updateCoinPrices(int _id, double _newPrice, double _newChange, int _newVolume) {
        try {
            // Insert statement, using prepared statements
            String query = " UPDATE all_coins SET price = ?, changePrice = ?, volume = ? WHERE coinID = ?";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setDouble(1, _newPrice);
            preparedStmt.setDouble(2, _newChange);
            preparedStmt.setInt(3, _newVolume);
            preparedStmt.setInt(4, _id);
            // execute the preparedstatement
            preparedStmt.execute();
            if (DEBUG){System.out.println("Updated coin price: " + _id + " to: " + _newPrice);}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Insert data into the "coins" table.
     *
     * THIS TABLE IS AN OLD VERSION. USE all_coins FROM NOW ON.
     *
     * @param _uuid
     * @param _symbol
     * @param _name
     * @param _price
     * @param _date
     */
    @Override
    public void coinDatabase(String _uuid, String _symbol, String _name, String _price, Date _date) {
        try {
            // Parse price to a float because DB requires it.
            float newPrice = Float.parseFloat(_price);
            // Insert statement, using prepared statements
            String query = " insert into coins (uuid, symbol, name, price, date)"
                    + " values (?, ?, ?, ?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setString(1, _uuid);
            preparedStmt.setString(2, _symbol);
            preparedStmt.setString(3, _name);
            preparedStmt.setFloat(4, newPrice);
            preparedStmt.setDate(5, _date);
            // execute the preparedstatement
            preparedStmt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Insert data to the "user_coins" table
     *
     * @param userName
     * @param _coin_id
     * @return
     */
    @Override
    public boolean insertSavedCoin(String userName, int _coin_id) {
         try {
            // Insert statement, using prepared statements
            int id = getIdFromUsername(userName);
            if (checkSavedCoinsForDuplication(id, _coin_id)) {
                AlertMessages.showErrorMessage("Save Coin", "This coin already exists in the user saved coins.");
                return false;
            }
            String query = " INSERT INTO `user_coins` (`user_id`, `coin_id`) VALUES (?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setInt(2, _coin_id);
            if (DEBUG){System.out.println(preparedStmt.toString());}
            // execute the preparedstatement
            preparedStmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            AlertMessages.showErrorMessage("Save COin", "Coin saving failed against users.");
            return false;
        }
    }

    /**
     * Insert data to the "users" table.
     *
     * @param _online
     * @param _userEmail
     * @param _userName
     * @param _userPassword
     * @param _profilePicture
     */
    @Override
    public void userDatabase(int _online, String _userEmail, String _userName, String _userPassword, String _profilePicture) {
        try {
            /**
             * This works
             */
            // Insert statement, using prepared statements
            String query = " INSERT INTO users (isOnline, userEmail, userName, userPassword, profilePicture)"
                    + " VALUES (?, ?, ?, ?, ?)";
            String hashedPass = getSHA256(_userPassword);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setInt(1, _online);
            preparedStmt.setString(2, _userEmail);
            preparedStmt.setString(3, _userName);
            preparedStmt.setString(4, hashedPass);
            preparedStmt.setString(5, _profilePicture);
            // execute the preparedstatement
            preparedStmt.execute();
            if (DEBUG){System.out.println("Registration Success");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Update user in database showing when someone is online.
     * Online : 1
     * Offline : 0
     * @param _username
     * @param _isOnline
     */
    @Override
    public void setUserOnlineStatus(String _username, int _isOnline) {
        try {
            // Insert statement, using prepared statements
            String query = " UPDATE users SET isOnline = " + "'" + _isOnline + "'"
                    + " WHERE username = " + "'" + _username + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            // execute the preparedstatement
            preparedStmt.execute();
            if (DEBUG){System.out.println("Updated live status");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Get a coins ID given its symbol. The ID comes from the api.
     * @param _symbol
     * @return
     */
    @Override
    public int getCoinID(String _symbol) {
        int res = 0;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from all_coins where symbol = '" + _symbol + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            if (DEBUG){System.out.println(query);}
            ResultSet result = preparedStmt.executeQuery(query);
            res = result.getInt("coinID");
            if (DEBUG){System.out.println("Got list of live members");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return res;
    }

    /**
     * Returns a linked list of all users currently online.
     * @return
     */
    @Override
    public LinkedList<String> getOnlineUsers() {
        LinkedList<String> list = new LinkedList<>();
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where isOnline = '" + 1 + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                list.add(result.getString("username"));
            }
            if (DEBUG){System.out.println("Got list of live members");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    /**
     * Create a connection between two users and add
     * it to the table: friend.
     * @param _username
     * @param _friendUsername
     */
    @Override
    public void addFriend(String _username, String _friendUsername) {
        int fid = getIdFromUsername(_friendUsername);
        int uid = getIdFromUsername(_username);
        try {
            // Insert statement, using prepared statements
            String query = "INSERT INTO friend (friendID, userOfFriendID, friendUsername, friendOnline)"
                    + " values (?, ?, ?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setInt(1, fid);
            preparedStmt.setInt(2, uid);
            preparedStmt.setString(3, _friendUsername);
            preparedStmt.setInt(4, 1);
            // execute the preparedstatement
            preparedStmt.execute();
            if (DEBUG){System.out.println("Added Friend");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Remove the user friend connection. Delete the row
     * from the friend table.
     * @param _username
     */
    @Override
    public void removeFriend(String _username) {
        try {
            // Insert statement, using prepared statements
            String query = "DELETE FROM friend WHERE friendUsername = '" + _username + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.execute();
            if (DEBUG){System.out.println(_username + " was removed from friend list");}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Returns a list of friends for a given user ID.
     * @param _username
     * @return
     */
    @Override
    public LinkedList<String> getFriendList(String _username) {
        LinkedList<String> friendList = new LinkedList<>();
        int userId = getIdFromUsername(_username);
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from friend where userOfFriendID = '" + userId + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                friendList.add(result.getString("friendUsername"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return friendList;
    }

    /**
     * Get the id of a user given their username.
     * @param _username
     * @return
     */
    @Override
    public int getIdFromUsername(String _username) {
        int id = -1;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _username + "'";
            // create the mysql insert preparedstatement
            if (DEBUG){
                System.out.println(_username + " user");
            }
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                id = result.getInt("userID");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return id;
    }

    /**
     * Validate the login information submitted by the user.
     *
     * @param _userName
     * @param _userPass
     * @return
     */
    @Override
    public boolean validateLogin(String _userName, String _userPass) {
        boolean isValid = false;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _userName + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
//            preparedStmt.setString(1, _userName);
//            preparedStmt.execute();
            ResultSet result = preparedStmt.executeQuery(query);
            String userName = "";
            String hash = "";
            // Get values from data fields in database
            while (result.next()) {
                userName = result.getString("username");
                hash = result.getString("userPassword");
            }
            // Hash user input
            String hashedPass = getSHA256(_userPass);
            // Compare both hashes
            return validatePassword(_userPass, hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    /**
     * Check if the entered username already exists
     * in the database.
     * @param _userName
     * @return
     */
    @Override
    public boolean usernameExists(String _userName) {
        boolean isValid = true;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _userName + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            return result.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    /**
     * Checks if the given email exists in the database.
     * @param _userEmail
     * @return
     */
    @Override
    public boolean emailExists(String _userEmail) {
        boolean isValid = false;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where userEmail = '" + _userEmail + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            return result.next();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    /**
     * Return the email associated with the given username
     * @param _username
     * @return
     */
    @Override
    public String getEmailFromUsername(String _username) {
        boolean isValid = true;
        String email = "";
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _username + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                email = result.getString("userEmail");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return email;
    }

    /**
     * Finds the username associated with the given email address.
     * @param _email
     * @return
     */
    @Override
    public String getUsernameFromEmail(String _email) {
        boolean isValid = true;
        String username = "";
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where userEmail = '" + _email + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                username = result.getString("username");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return username;
    }
    /**
     * returns the path of the profile picture associated with the given username
     * @param _username
     * @return
     */
    @Override
    public String getPicturePath(String _username){
        String profilePicture = "";
        try{
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _username + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            // execute the preparedstatement
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()){
                profilePicture = result.getString("ProfilePicture");
            }
        }catch(SQLException ex){
            System.out.println(ex);
        }
        return profilePicture;
    }

    @Override
    public void changePassword(String _uname, String _newPassword) {
        try {
            String hashedPass = getSHA256(_newPassword);
            // Update row value
            String query = " UPDATE users SET userPassword = " + "'" + hashedPass + "'"
                    + " WHERE username = " + "'" + _uname + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            // execute the preparedstatement
            preparedStmt.execute();
            if (DEBUG){System.out.println("Password reset success");}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Use this to get user info inside the app.
     * @param _userName
     * @return
     */
    @Override
    public HashMap<String, String> getUserInfo(String _userName) {
        HashMap<String, String> map = new HashMap<>();
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where userName = '" + _userName + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                map.put("userID", Integer.toString(result.getInt("userID")));
                map.put("isOnline", Boolean.toString(result.getBoolean("isOnline")));
                map.put("userEmail", result.getString("userEmail"));
                map.put("username", result.getString("username"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return map;
    }

    /**
     * This method generates a SHA-256 hash from
     * a given string input.
     *
     * Yea I know it's not salted. Who even asked you.
     *
     * @param _input
     * @return
     */
    @Override
    public String getSHA256(String _input) {
        // MessageDigest used to hash bytes from input string
        MessageDigest md = null;
        try {
            // Select hash type
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Add hashed bytes to byte array
        byte[] hash = md.digest(_input.getBytes(StandardCharsets.UTF_8));
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexStr = new StringBuilder(number.toString(16));
        while (hexStr.length() < 32) {
            hexStr.insert(0, '0');
        }
        return hexStr.toString();
    }

    /**
     * Hash the input and compare to a database hash.
     * @param _password
     * @param _dbHash
     * @return
     */
    private boolean validatePassword(String _password, String _dbHash) {
        return getSHA256(_password).equals(_dbHash);
    }

    /**
     * Close the database connection.
     *
     * Must do this after every ConnectionToDatabase object
     * is done being used!!
     */
    @Override
    public void close() {
        try {
            this.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return the coins a user has saved.
     * @param username
     * @return
     */
    @Override
     public LinkedList<UserCoin> getSavedCoins(String username) {
        LinkedList<UserCoin> list = new LinkedList<>();
        try {
            String query = "SELECT `all_coins`.`symbol`, `all_coins`.`price`,"
                    + "`all_coins`.`name`, `users`.`username`,user_coins.coin_id,`user_coins`.`user_id` "
                    + "FROM `user_coins` LEFT JOIN users "
                    + "ON user_coins.user_id = users.userID "
                    + "LEFT JOIN all_coins ON user_coins.coin_id = "
                    + "all_coins.coinID WHERE `user_coins`.`user_id` = "
                    + "" + getIdFromUsername(username);

            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            if (DEBUG){System.out.println(query);}
            while(result.next()) {
                list.add(new UserCoin(result.getString("symbol"), result.getString("name"), result.getString("username"), result.getInt("coin_id"), result.getInt("user_id"),result.getDouble("price")));
            }
            if (DEBUG){System.out.println("Got list of coins of user logged in.");}
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * Returns a list of all coins in the database.
     * @return
     */
    @Override
    public LinkedList<String> getAllCoins() {
        LinkedList<String> temp = new LinkedList<>();
        try {
            String query = "SELECT * FROM `all_coins`";
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            if (DEBUG){System.out.println("Query: " + query);}
            while(result.next()== true){
                temp.add(result.getString("symbol") + ": " + result.getString("name"));
            }
            return temp;
        } catch (SQLException ex) {
            System.out.println("Error in DB Connection: " + ex);
        }
        return temp;
    }

    /**
     * This method checks that the coin user is saved previously or not.
     * @param id
     * @param _coin_id
     * @return
     */
    private boolean checkSavedCoinsForDuplication(int id, int _coin_id) {
        try {
            String query = "SELECT * FROM `user_coins` WHERE `user_coins`.`user_id` = " + id + " AND `user_coins`.`coin_id` = " + _coin_id;

            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            if (DEBUG){System.out.println("Query: " + query);}
            while(result.next()== true){
                return true;
            }
            return false;
        } catch (SQLException ex) {
            System.out.println("Error in DB Connection: " + ex);
            return false;
        }
    }

    /**
     * this method deletes the saved coin from the user's list
     * @param userCoin
     * @return
     */
    @Override
    public boolean deleteSavedCoin(UserCoin userCoin) {
        try {
            String query = "DELETE FROM `user_coins` WHERE `user_coins`.`user_id` = " + userCoin.getUserID() + " AND `user_coins`.`coin_id` = " + userCoin.getCoinID();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println("Error in DB Connection: " + ex);
            return false;
        }
    }

    /**
     * Returns the users ID from their username.
     * @param _uname
     * @return
     */
    @Override
    public int getUserId(String _uname) {
        // Default id negative
        int id = -1;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _uname + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            while(result.next()) {
                id = result.getInt("userID");
                if (DEBUG){
                    System.out.println(_uname + ": " + id);
                }
            }
            return id;
        } catch (SQLException ex) {
            System.out.println("Error in DB Connection: " + ex);
            return id;
        }
    }

    /**
     * Delete a saved coin using username and the coins ID.
     * @param _user
     * @param _coinID
     * @return
     */
    @Override
    public boolean deleteSavedCoin(User _user, int _coinID) {
        try {
            String query = "DELETE FROM `user_coins` WHERE `user_coins`.`user_id` = " + _user.getUserID() + " AND `user_coins`.`coin_id` = " + _coinID;
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println("Error in DB Connection: " + ex);
            return false;
        }
    }
}



