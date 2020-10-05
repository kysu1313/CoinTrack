/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers;

import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.SingleCoin;
import coinClasses.UserCoin;
import coinTrack.FXMLDocumentController;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import tabControllers.AlertMessages;
import tabControllers.assistantControllers.graphs.BarChartClass;
import tabControllers.assistantControllers.graphs.CandleChartClass;
import tabControllers.assistantControllers.graphs.PieChartClass;
import tabControllers.assistantControllers.tablesAndLists.ListClass;
import tabControllers.assistantControllers.tablesAndLists.TableClass;

/**
 *
 * @author Kyle
 */
public class TabAssistantController {
    
    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private static final String UNAME = FXMLDocumentController.uname;
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
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(UNAME, coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }
    
    /**
     * Pull saved coin data from database and add it to the accordion.
     * @param savedCoinsList
     * @param savedCoins
     */
    public void populateSavedCoins(ListView savedCoinsList, LinkedList<UserCoin> savedCoins) {
        ListClass lc = new ListClass(UNAME);
        lc.populateSavedCoins(savedCoinsList);
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

    /**
     * Testing this method using piechart class.
     * @param coinList
     * @param pieChartCoins
     * @param comboBox
     * @param pieChartData
     * @param pieChart
     */
    public void MakePieChart(CoinRankApi coinList, int pieChartCoins, ComboBox<String> comboBox, ObservableList<PieChart.Data> pieChartData, PieChart pieChart) {
        PieChartClass pcc = new PieChartClass(coinList, pieChartCoins, comboBox, pieChartData, pieChart);
        pcc.displayGraph();
    }

    /**
     * Make pie chart for the dashboard.
     * @param coinList
     * @param pieChart
     */
    public void PieChartDash(LinkedList<SingleCoin> coinList, PieChart pieChart){
        PieChartClass pcc = new PieChartClass(coinList, pieChart);
        pcc.displayGraph();
    }
    
    public void candleChart(Pane _pane) throws ParseException {
        CandleChartClass ccc = new CandleChartClass(_pane);
    }

    /**
     * Creates a LinkedList of SingleCoins.
     *
     * This method has been moved to Tab2AssistantController in an effort to
     * keep this controller as clean as possible.
     */
    public void PieChart(CoinRankApi coinList, int pieChartCoins, ComboBox<String> comboBox, ObservableList<PieChart.Data> pieChartData, PieChart pieChart) {
        // Make sure the thread is finished
        coinList.join();
        LinkedList<SingleCoin> temp = coinList.getSortedCoinList();
        pieChartCoins = Integer.parseInt(comboBox.getValue());
        // Loops over SingleCoin list and adds data to pieChart
        for (int i = 0; i <= pieChartCoins - 1; i++) {
            SingleCoin coin = temp.get(i);
            double price = Double.parseDouble(coin.getPrice());
            // Allow 5 decimal places
            double rounded = (double) Math.round(price * 100000d) / 100000d;
            pieChartData.add(new PieChart.Data(coin.getName(), rounded));
        }
        pieChart.setData(pieChartData);
    }
    
    /**
     * Call database returning a list of all users who are online.
     */
    public void addOnlineUsers(LinkedList<String> onlineUsers, ListView onlineUsersList) {
        onlineUsersList.getItems().clear();
        ListClass lcc = new ListClass(UNAME);
        lcc.populateOnlineUsers(onlineUsersList);
    }
    
    /**
     * Call database returning a list of friends.
     */
    public void friendList(LinkedList<String> friendList, String uname, ListView friendsListT2) {
        ConnectToDatabase conn = new ConnectToDatabase();
        friendList = conn.getFriendList(uname);
        conn.close();
        for (int i = 0; i < friendList.size(); i++) {
            friendsListT2.getItems().add(friendList.get(i));
        }
    }
    
    public void multiBarChart(BarChart _barChart, LinkedList<LinkedHashMap<Double, String>> _linkedMap, int _numCoins, LinkedList<UserCoin> _userCoinList) {
        BarChartClass bcc = new BarChartClass(_barChart, _linkedMap, _numCoins, _userCoinList);
        bcc.displayGraph();
    }

    /**
     * This creates the right click menu on the onlineUsers list. 
     * It also maps each button to an action.
     */
    public void listCells(ListView onlineUsersListT2, String uname) {
        ListClass lcc = new ListClass(uname);
        lcc.populateFriends(onlineUsersListT2);
    }
    
    public void createTable(TableView _tableDash, LinkedList<SingleCoin> _userSingleCoins) {
        TabAssistantController tas = new TabAssistantController();
        tas.coinTableDash(_tableDash, _userSingleCoins);
    }
    
    public void createFriendList(ListView friendsListT2) {
        ListClass lc = new ListClass(UNAME);
        lc.populateFriends(friendsListT2);
    }
}
