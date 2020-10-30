/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import models.UserCoin;
import java.sql.Date;
import java.util.LinkedList;

/**
 *
 * @author Kyle
 */
public interface DatabaseInterface {
    
    public void addCoinToDatabase(int _coinID, String _uuid, String _slug, 
                            String _symbol, String _name,
                            int _numMarkets, int _numExchanges, 
                            int _volume, int _marketCap, String _price, 
                            double _change, int _coinRank);
    public boolean checkIfCoinExists(String _uuid);
    public void updateCoinPrices(int _id, double _newPrice, double _newChange, int _newVolume);
    public void coinDatabase(String _uuid, String _symbol, String _name, String _price, Date _date);
    public boolean insertSavedCoin(String userName, int _coin_id);
    public void userDatabase(int _online, String _userEmail, String _userName, String _userPassword);
    public void setUserOnlineStatus(String _username, int _isOnline);
    public int getCoinID(String _symbol);
    public LinkedList<String> getOnlineUsers();
    public void addFriend(String _username, String _friendUsername);
    public void removeFriend(String _username);
    public LinkedList<String> getFriendList(String _username);
    public int getIdFromUsername(String _username);
    public boolean validateLogin(String _userName, String _userPass);
    public boolean usernameExists(String _userName);
    public boolean emailExists(String _userEmail);
    public String getEmailFromUsername(String _username);
    public String getUsernameFromEmail(String _email);
    public void changePassword(String _uname, String _newPassword);
    public boolean getUserInfo(String _userName, String _userPass);
    public String getSHA256(String _input);
    public void close();
    public LinkedList<UserCoin> getSavedCoins(String username);
    public LinkedList<String> getAllCoins();
    public boolean deleteSavedCoin(UserCoin userCoin);
    
    
    
}
