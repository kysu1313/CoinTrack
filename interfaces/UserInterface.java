/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import controllers.assistantControllers.TabAssistantController;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import models.SingleCoin;
import models.UserCoin;

/**
 *
 * @author Kyle
 */
public interface UserInterface {

    public void createData();
    public void saveCoin(int _coinID);
    public boolean validateLogin();
    public void onlineStatus(int _value);
    public String getPicturePath(String _name);
    public void resetPassword(String _username, String _newPass);
    public boolean forgotUsernameEmail(String _email);
    public LinkedList<Object> getGenericCoinList();
    public LinkedList<SingleCoin> getUserSingleCoins();
    public LinkedList<LinkedHashMap<Double, String>> getLinkedUserHistoryMap(String _timeframe);
    public LinkedList<SingleCoin> getSavedSingleCoins();
    public String getUsername();
    public int getUserID();
    public LinkedList<UserCoin> getSavedCoins();
    public HashMap<String, String> getUserInfo();
    public LinkedHashMap<Double, String> getCoinHistoryList();
    public LinkedList<SingleCoin> getCoinList();
    public LinkedList<SingleCoin> getSortedCoinList();
    public LinkedList<String> getFriendList();
    public LinkedList<UserCoin> getUserCoinList();
    public TabAssistantController getTas();
    public boolean getIsDataSet();
    public String getClassName();

}
