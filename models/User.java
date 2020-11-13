package models;

import interfaces.GenericClassInterface;
import interfaces.UserInterface;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents a user's account.
 * @author Kyle
 */
public class User implements GenericClassInterface, UserInterface{

    private final String USERNAME;
    private final ConnectToDatabase CONN;
    private final LinkedList<UserCoin> USER_COINS;
    private final HashMap<String, String> USER_DATA;
    private LinkedList<Object> objList;

    public User(String _username){
        this.USERNAME = _username;
        this.CONN = new ConnectToDatabase();
        this.USER_COINS = this.CONN.getSavedCoins(_username);
        this.USER_DATA = CONN.getUserInfo(_username);
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
}
