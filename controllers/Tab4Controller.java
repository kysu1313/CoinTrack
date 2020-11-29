/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import coinTrack.FXMLDocumentController;
import controllers.assistantControllers.TabAssistantController;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import models.User;
import models.UserCoin;

/**
 * FXML Controller class
 *
 * @author HBadr
 */
public class Tab4Controller implements Initializable {

    private static String USERNAME = coinTrack.FXMLDocumentController.uname;
    private static final User USER = FXMLDocumentController.user;
    public static TabAssistantController tas;
    private LinkedList<UserCoin> savedCoins;
    @FXML private ListView savedCoinsList;
    @FXML private ListView friendsList;
    @FXML private TableView tableViewT1;
    @FXML private TableView tableViewT11;
    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        Tab4Controller.tas.createFriendList(this.friendsList, 1);
    }

    /**
     * Pull saved coin data from database and add it to the accordion.
     */
    public void populateSavedCoins() {
        Tab4Controller.tas.populateSavedCoins(this.savedCoinsList, this.savedCoins);
    }


    /**
     * Create table, fill with saved coins.
     */
    private void createTable() {
        Tab4Controller.tas.coinTableDash(this.tableViewT1, Tab4Controller.USER.createTListFronSingleCoins(Tab4Controller.USER.getCoinList()));
    }

    /**
     * Initialize the tab
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab4Controller.tas = new TabAssistantController();
        Tab4Controller.USERNAME = FXMLDocumentController.uname;
        populateSavedCoins();
        addFriendsToList();
        Tab4Controller.tas.coinTableFriendsCoin(this.tableViewT11);
        Tab4Controller.tas.getCoinList();
        createTable();
    }
}
