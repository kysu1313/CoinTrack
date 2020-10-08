/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import coinClasses.UserCoin;
import java.util.LinkedList;
import javafx.scene.control.ListView;

/**
 *
 * @author Kyle
 */
public interface ListClassInterface {
    public void populateList(ListView _list, LinkedList<String> _items);
    public void populateSavedCoins(ListView savedCoins);
    public void populateOnlineUsers(ListView onlineUsers);
}
