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
    private int coinID;
    private int userID;

    public UserCoin(String symbol, String coin, String username, int coinID, int userID) {
        this.symbol = symbol;
        this.name = coin;
        this.username = username;
        this.coinID = coinID;
        this.userID = userID;
    }

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

    @Override
    public String toString() {
        return  symbol + "  " + name;
    }

    @Override
    public String getClassName() {
        return "UserCoin";
    }
}
