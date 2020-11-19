/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.viewModels;

import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.UserCoin;

/**
 *
 * @author
 */
public class TableClassFriendsCoins {

    private final TableView TABLE_VIEW;
    private ObservableList<UserCoin> obvList;

//    Instantiating the table view object
    public TableClassFriendsCoins(TableView tableView) {
        this.TABLE_VIEW = tableView;
        buildTable();
    }

    /**
     * This method creates the table columns and add it to table view
     */
    private void buildTable() {
        TableColumn<UserCoin, String> colName = new TableColumn<>("Name");
        TableColumn<UserCoin, String> colSymbol = new TableColumn<>("Symbol");
        TableColumn<UserCoin, String> colPrice = new TableColumn<>("Price");
        TableColumn<UserCoin, String> colID = new TableColumn<>("Coin Id");
//        TableColumn<UserCoin, String> colUserID = new TableColumn<>("User Id");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colID.setCellValueFactory(new PropertyValueFactory<>("coinID"));
//        colUserID .setCellValueFactory(new PropertyValueFactory<>("userID"));
        colID.setPrefWidth(100);
        colSymbol.setPrefWidth(100);
        colName.setPrefWidth(100);
        colPrice.setPrefWidth(100);
        this.TABLE_VIEW.getColumns().addAll(colName, colSymbol, colPrice, colID);
    }

    /**
     * This method displays the coin list that is passed as a parameter in
     * table.
     *
     * @param coinList
     */
    public void displayTable(LinkedList<UserCoin> coinList) {
        this.obvList = FXCollections.observableArrayList(coinList);
        this.TABLE_VIEW.setItems(this.obvList);
        this.TABLE_VIEW.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
