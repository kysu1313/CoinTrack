/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static controllers.Tab1Controller.tas;
import controllers.assistantControllers.TabAssistantController;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import models.UserCoin;

/**
 * FXML Controller class
 *
 * @author HBadr
 */
public class Tab4Controller implements Initializable {

    private String uname;
    public static TabAssistantController tas;
    private LinkedList<UserCoin> savedCoins;
    private LinkedList<String> friendList;
    @FXML private ListView savedCoinsList;
    @FXML private ListView friendsList;
    @FXML private TableView tableViewT1;

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        tas.createFriendList(this.friendsList, 1);
    }

    /**
     * Pull saved coin data from database and add it to the accordion.
     */
    public void populateSavedCoins() {
        this.tas.populateSavedCoins(savedCoinsList, savedCoins);
    }

    /**
     * Initialize the tab
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tas = new TabAssistantController();
        populateSavedCoins();
        addFriendsToList();
    }
}
