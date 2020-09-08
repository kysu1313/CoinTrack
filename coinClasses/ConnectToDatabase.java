package coinClasses;
/**
 * Database connection class.
 * The database is hosted on freemysqlhosting.net
 * 
 * - Kyle
 */

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle
 */
public class ConnectToDatabase {
    
    Connection con;
    
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
     * Insert data into the "coins" table.
     * 
     * @param _uuid
     * @param _symbol
     * @param _name
     * @param _price
     * @param _date 
     */
    public void coinDatabase(String _uuid, String _symbol, String _name, String _price, Date _date) {
        try {
            /**
             * This works
             */
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
     * Insert data to the "users" table.
     * 
     * @param _userEmail
     * @param _userName
     * @param _userPassword
     */
    public void userDatabase(String _userEmail, String _userName, String _userPassword) {
        try {
            /**
             * This works
             */
            // Insert statement, using prepared statements
            String query = " insert into users (userEmail, userName, userPassword)"
                    + " values (?, ?, ?)";

            String hashedPass = getSHA256(_userPassword);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setString(1, _userEmail);
            preparedStmt.setString(2, _userName);
            preparedStmt.setString(3, hashedPass);
            // execute the preparedstatement
            preparedStmt.execute();
            System.out.println("Login Success");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            String query = "select * from users where username = '" + _userName + "'";
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
            String query = "select * from users where username = '" + _userName + "'";
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
    
}
