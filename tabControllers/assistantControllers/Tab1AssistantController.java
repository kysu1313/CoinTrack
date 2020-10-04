

package tabControllers.assistantControllers;

import coinClasses.ConnectToDatabase;
import coinClasses.SingleCoin;
import coinClasses.UserCoin;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import tabControllers.AlertMessages;
import static tabControllers.Tab1Controller.DEBUG;
import tabControllers.assistantControllers.tablesAndLists.TableClass;

/**
 * This Class contains additional methods used in Tab1 to display data to the
 * screen.
 *
 * The main purpose of this class is to keep the main tab controllers as clean
 * as possible.
 *
 * @author Kyle
 */
public class Tab1AssistantController {

    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private static String uname;
    private TableClass tbl;

    public void coinTable(TableView _tableViewT1, LinkedList<SingleCoin> _coinList, WebView _webViewT1, String _currency, long _currencyRate) {

        // Create columns
        SingleCoin sc = new SingleCoin();
        LinkedList<String> colNames = new LinkedList<>();
        // Add single coin param names for column names.
        colNames.add("Symbol");
        colNames.add("Name");
        colNames.add("Price");
        colNames.add("Rank");
        colNames.add("Change");
        colNames.add("Volume");
        this.tbl = new TableClass(_tableViewT1, _coinList, _webViewT1, colNames, _currency, _currencyRate);
        this.tbl.displayTable();
        this.tbl.colorChangeCol("#09de57", "#ff0000");
    }

    /**
     * Display coin table on the Dashboard
     * @param tableView
     * @param coinList
     */
    public void coinTableDash(TableView tableView, LinkedList<SingleCoin> coinList) {
        // Create columns
        SingleCoin sc = new SingleCoin();
        LinkedList<String> colNames = new LinkedList<>();
        // Add single coin param names for column names.
        colNames.add("Name");
        colNames.add("Symbol");
        colNames.add("Price");
        colNames.add("Change");
        this.tbl = new TableClass(tableView, coinList, colNames);
        this.tbl.displayTable();
        this.tbl.colorChangeCol("#09de57", "#ff0000");
        
    }
    
    /**
     * This creates the right click menu on the onlineUsers list.
     * It also maps each button to an action.
     *
     * @param _username
     * @param _savedCoinsList
     * @param _savedCoins
     */
    public void createCells(String _username, ListView _savedCoinsList, LinkedList<UserCoin> _savedCoins) {
        this.tbl.createTableCells(_username, _savedCoinsList, _savedCoins);
    }

    /**
     * Save coin using coinID and username.
     * @param userName
     * @param coinID
     */
    public void saveCoin(String userName, int coinID) {
        uname = userName;
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(userName, coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }

    /**
     * Pull saved coin data from database and add it to the accordion.
     */
    public void populateSavedCoins(ListView savedCoinsList, LinkedList<UserCoin> savedCoins) {
        ConnectToDatabase conn = new ConnectToDatabase();
        savedCoinsList.getItems().clear();
        savedCoins = conn.getSavedCoins(uname);
        conn.close();
        if (savedCoins != null && savedCoins.size() > 0) {
            for (int i = 0; i < savedCoins.size(); i++) {
                savedCoinsList.getItems().add(savedCoins.get(i));
            }
        }
    }

    /**
     * Change a users online status. i.e. when they log on/off .
     * @param _uname
     * @param _status
     */
    public void setOnlineStatus(String _uname, int _status) {
        if(DEBUG){System.out.println("Update " + _uname + "'s online status");}
        ConnectToDatabase conn = new ConnectToDatabase();
        conn.setUserOnlineStatus(_uname, _status);
        conn.close();
    }
}
