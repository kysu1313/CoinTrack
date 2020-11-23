package models;

import interfaces.GlobalClassInterface;

/**
 * This class represents a coin that has been saved by a user.
 * This class is used when pulling data from the database.
 * @author Parth
 */
public class UserCoin implements GlobalClassInterface {
    private String symbol;
    private String name;
    private String username;
    private double price;
    private int coinID;
    private int userID;

    public UserCoin(String symbol, String coin, String username, int coinID, int userID, double price) {
        this.symbol = symbol;
        this.name = coin;
        this.username = username;
        this.coinID = coinID;
        this.userID = userID;
        this.price = price;
    }

    // ========== GETTERS ==========

    /** return symbol of coin
     * 
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**Return name of coin
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

     /**Return username of coin
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

     /**Return price of coin
     * 
     * @return price
     */
    public double getPrice() {
        return price;
    }

     /**Return coinID of coin
     * 
     * @return coinID
     */
    public int getCoinID() {
        return coinID;
    }

     /**Return userID of coin
     * 
     * @return userID
     */
    public int getUserID() {
        return userID;
    }
    
    /**
     * 
     * @return class name
     */
    @Override
    public String getClassName() {
        return "UserCoin";
    }
    // ========== SETTERS ==========

    /**
     * 
     * @param symbol 
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @param price 
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * 
     * @param coinID 
     */
    public void setCoinID(int coinID) {
        this.coinID = coinID;
    }

    /**
     * 
     * @param userID 
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    @Override
    public String toString() {
        return  symbol + "  " + name;
    }
}