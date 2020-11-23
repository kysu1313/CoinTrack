package models;

import interfaces.GlobalClassInterface;

/**
 * This class represents a coin that has been saved by a user.
 * This class is used when pulling data from the database.
 * @author Parth
 */
public class UserCoin implements GlobalClassInterface{
    private String symbol;
    private String name;
    private String username;
    private double price;
    private int coinID;
    private int userID;

    public UserCoin(String _symbol, String _coin, String _username, int _coinID, int _userID, double _price) {
        this.symbol = _symbol;
        this.name = _coin;
        this.username = _username;
        this.coinID = _coinID;
        this.userID = _userID;
        this.price = _price;
    }

    /* ======= SETTERS ======= */

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCoinID() {
        return coinID;
    }

    public void setCoinID(int coinID) {
        this.coinID = coinID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    
    @Override
    public String toString() {
        return  symbol + "  " + name;
    }

    @Override
    public String getClassName() {
        return "UserCoin";
    }
}
