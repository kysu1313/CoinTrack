package coinClasses;
/**
 * Database connection class.
 * The database is hosted on freemysqlhosting.net
 * 
 * Constructor just opens a connection.
 * The methods allow interaction with specific tables.
 * 
 * If you open it. You MUST close it!
 * 
 * - Kyle
 */

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import tabControllers.AlertMessages;

public class ConnectToDatabase {
    
    Connection con;
    
    /**
     * Makes a simple connection to the database.
     */
    public ConnectToDatabase() {
        try {
            /**
             * This works
             */
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://sql9.freemysqlhosting.net:3306/sql9364184", "sql9364184", "vLIxP8hK8C");
            System.out.println("DB connection successful");
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
//            preparedStmt.setDate(15, _date);
            // execute the preparedstatement
            preparedStmt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
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
     */
    public boolean insertSavedCoin(String userName, int _coin_id) {
         try {
            // Insert statement, using prepared statements

            int id = getIdFromUsername(userName);
            if (checkSavedCoinsForDuplication(id,_coin_id)) {
                AlertMessages.showErrorMessage("Save Coin", "This coin already exists in the user saved coins.");
                return false;
            }

            String query = " INSERT INTO `user_coins` (`user_id`, `coin_id`) VALUES (?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setInt(2, _coin_id);
             System.out.println(preparedStmt.toString());
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
     */
    public void userDatabase(int _online, String _userEmail, String _userName, String _userPassword) {
        try {
            /**
             * This works
             */
            // Insert statement, using prepared statements
            String query = " INSERT INTO users (isOnline, userEmail, userName, userPassword)"
                    + " VALUES (?, ?, ?, ?)";
            String hashedPass = getSHA256(_userPassword);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setInt(1, _online);
            preparedStmt.setString(2, _userEmail);
            preparedStmt.setString(3, _userName);
            preparedStmt.setString(4, hashedPass);
            // execute the preparedstatement
            preparedStmt.execute();
            System.out.println("Registration Success");
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
    public void setUserOnlineStatus(String _username, int _isOnline) {
        try {
            // Insert statement, using prepared statements
            String query = " UPDATE users SET isOnline = " + "'" + _isOnline + "'" 
                    + " WHERE username = " + "'" + _username + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            // execute the preparedstatement
            preparedStmt.execute();
            System.out.println("Updated live status");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Returns a linked list of all users currently online.
     * @return 
     */
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
            System.out.println("Got list of live members");
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
            System.out.println("Added Friend");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Remove the user friend connection. Delete the row
     * from the friend table.
     * @param _username 
     */
    public void removeFriend(String _username) {
        try {
            // Insert statement, using prepared statements
            String query = "DELETE FROM friend WHERE friendUsername = '" + _username + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.execute();
            System.out.println(_username + " was removed from friend list");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Returns a list of friends for a given user ID.
     * @param _username
     * @return 
     */
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
    public int getIdFromUsername(String _username) {
        int id = -1;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _username + "'";
            // create the mysql insert preparedstatement
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
    public boolean usernameExists(String _userName) {
        boolean isValid = true;
        try {
            // Insert statement, using prepared statements
            String query = "SELECT * from users where username = '" + _userName + "'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
//            preparedStmt.setString(1, _userName);
//            preparedStmt.execute();
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
            System.out.println("Password reset success");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Use this to get user info inside the app.
     * @param _userName
     * @param _userPass
     * @return 
     */
    public boolean getUserInfo(String _userName, String _userPass) {
        return false;
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
    public void close() {
        try {
            this.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public LinkedList<String> getSavedCoins(String username) {
        LinkedList<String> list = new LinkedList<>();
        try {
            String query = "SELECT `all_coins`.`symbol`, `all_coins`.`name`, `users`.`username` FROM `user_coins` LEFT JOIN users ON user_coins.user_id = users.userID LEFT JOIN all_coins ON user_coins.coin_id = all_coins.coinID WHERE `user_coins`.`user_id` = " + getIdFromUsername(username);

            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            System.out.println(query);
            while(result.next()) {
                list.add(result.getString("symbol") + " " + result.getString("name"));
            }
            System.out.println("Got list of coins of user logged in.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return list;
    }    

    /**
     * This method checks that the coin user is saving, is saved previously or not. 
     * @param id
     * @param _coin_id
     * @return 
     */
    private boolean checkSavedCoinsForDuplication(int id, int _coin_id) {
        try {
            String query = "SELECT * FROM `user_coins` WHERE `user_coins`.`user_id` = " + id + " AND `user_coins`.`coin_id` = " + _coin_id;

            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            ResultSet result = preparedStmt.executeQuery(query);
            System.out.println("Query: " + query);
           // while(!result.isBeforeFirst()) {
           while(result.next()== true){
                return true;
            }            
            return false;
        } catch (SQLException ex) {
            System.out.println("Error in DB Connection: " + ex);
            return false;
        }
        
    }
}

    

