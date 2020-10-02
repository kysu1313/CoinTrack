

package tabControllers.assistantControllers;

import coinClasses.ConnectToDatabase;
import coinClasses.SingleCoin;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
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
        TableClass tbl = new TableClass(_tableViewT1, _coinList, _webViewT1, colNames, _currency, _currencyRate);
        tbl.displayTable();
        tbl.colorChangeCol("#09de57", "#ff0000");
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
        TableClass tbl = new TableClass(tableView, coinList, colNames);
        tbl.displayTable();
        tbl.colorChangeCol("#09de57", "#ff0000");
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
