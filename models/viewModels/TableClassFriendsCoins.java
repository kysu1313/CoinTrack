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
    private final int COL_WIDTH = 100;
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
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colID.setCellValueFactory(new PropertyValueFactory<>("coinID"));
        colID.setPrefWidth(this.COL_WIDTH);
        colSymbol.setPrefWidth(this.COL_WIDTH);
        colName.setPrefWidth(this.COL_WIDTH);
        colPrice.setPrefWidth(this.COL_WIDTH);
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
