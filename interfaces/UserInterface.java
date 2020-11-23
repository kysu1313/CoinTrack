/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.HashMap;
import java.util.LinkedList;
import models.UserCoin;

/**
 *
 * @author Kyle
 */
public interface UserInterface {

    public String getUsername();
    public LinkedList<UserCoin> getSavedCoins();
    public HashMap<String, String> getUserInfo();

}
