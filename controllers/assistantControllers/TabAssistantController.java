package controllers.assistantControllers;

import models.CoinRankApi;
import models.ConnectToDatabase;
import models.SingleCoin;
import models.UserCoin;
import coinTrack.FXMLDocumentController;
import static coinTrack.FXMLDocumentController.user;
import controllers.AlertMessages;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import models.viewModels.BarChartClass;
import models.viewModels.CandleChartClass;
import models.viewModels.PieChartClass;
import models.viewModels.ListClass;
import models.viewModels.TableClass;
import controllers.assistantControllers.Theme;
import models.User;
import models.CoinHistory;
import models.GetMarketsApi;
import models.viewModels.TableClassFriendsCoins;

/**
 * This class contains methods that assist the tabControllers.
 *
 * Methods to create tables, lists and graphs are
 * avaliable here.
 *
 * @author Kyle
 * @param <T>
 */
public class TabAssistantController<T> {

    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    private static String UNAME = FXMLDocumentController.uname;
    private static User USER = FXMLDocumentController.getUser();
    private TableClass tbl;
    private TableClassFriendsCoins tblFriends;
    private static Theme theme;
    private static Scene scene = FXMLDocumentController.scene;
    private static User currentUser;
    private LinkedList<UserCoin> userCoinList;
    private static LinkedList<SingleCoin> coinList;
    private LinkedList<SingleCoin> userSingleCoins;
    private LinkedList<UserCoin> savedCoins;
    private LinkedList<String> friendList;
    private LinkedList<String> onlineUserList;
    private final String TIMEFRAME = "24h";
    private LinkedHashMap<Double, String> singleHistoryMap;
    private LinkedHashMap<Double, String> userHistoryMap;
    private LinkedList<LinkedHashMap<Double, String>> linkedUserHistoryMap;
    private LinkedList<Object> objectList;

    /**
     * Set current user.
     * @param _user
     */
    public void setUser(User _user) {
        TabAssistantController.USER = _user;
    }

    /**
     * Add listener to theme menu item.
     */
    public void addThemeListener() {
        theme = new Theme("light");
        FXMLDocumentController.darkMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theme = new Theme("dark");
                scene.getStylesheets().add(theme.getTheme());
            }
        });
        FXMLDocumentController.lightMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theme = new Theme("light");
                scene.getStylesheets().add(theme.getTheme());
            }
        });
    }

    /**
     * Creates the full table on Tab 1
     * @param _classType
     * @param _objList
     * @param _colNames
     * @param _tableViewT1
     * @param _webView
     * @param _currencyRate
     */
    public void coinGenericTable(String _classType, LinkedList<Object> _objList, LinkedList<String> _colNames, TableView _tableViewT1, WebView _webView, long _currencyRate) {

        this.tbl = new TableClass(_classType, _objList, _tableViewT1, _webView, _colNames, _currencyRate);
        this.tbl.displayTable();
        this.tbl.colorChangeCol("#09de57", "#ff0000");
    }

    /**
     * Display coin table on the Dashboard
     *
     * @param tableView
     * @param coinList
     */
    public void coinTableDash(TableView tableView, LinkedList<T> _list) {
        // Create columns
        SingleCoin sc = new SingleCoin();
        LinkedList<String> colNames = new LinkedList<>();
        // Add single coin param names for column names.
        colNames.add("Name");
        colNames.add("Symbol");
        colNames.add("Price");
        colNames.add("Change");
//        TabAssistantController tas = new TabAssistantController();
//        LinkedList<T> objList = getObjSavedCoinList();
//        LinkedList<T> objList = USER.getTList();
        this.tbl = new TableClass("SingleCoin", tableView, _list, colNames);
        this.tbl.displayTable();
        this.tbl.colorChangeCol("#09de57", "#ff0000");
    }

    /**
     * This method builds the table for friends coin
     * @param tableView
     */
    public void coinTableFriendsCoin(TableView tableView) {
        this.tblFriends = new TableClassFriendsCoins(tableView);
    }

    /**
     * This method is used to populate the table with the friend's saved coins list.
     * @param coinList
     */
    public void displayFriendsCoins(LinkedList<UserCoin> coinList) {
         this.tblFriends.displayTable(coinList);
    }
    /**
     * This creates the right click menu on the onlineUsers list. It also maps
     * each button to an action.
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
     *
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
     *
     * @param savedCoinsList
     * @param savedCoins
     */
    public void populateSavedCoins(ListView savedCoinsList, LinkedList<UserCoin> savedCoins) {
        ListClass lc = new ListClass(UNAME);
        lc.populateSavedCoins(savedCoinsList);
    }

    /**
     * Change a users online status. i.e. when they log on/off .
     *
     * @param _uname
     * @param _status
     */
//    public void setOnlineStatus(String _uname, int _status) {
//        if (DEBUG) {
//            System.out.println("Update " + _uname + "'s online status");
//        }
//        ConnectToDatabase conn = new ConnectToDatabase();
//        conn.setUserOnlineStatus(_uname, _status);
//        conn.close();
//    }

    /**
     * Testing this method using piechart class.
     *
     * @param coinList
     * @param pieChartCoins
     * @param comboBox
     * @param pieChartData
     * @param pieChart
     */
    public void MakePieChart(LinkedList<SingleCoin> coinList, int pieChartCoins, ComboBox<String> comboBox, ObservableList<PieChart.Data> pieChartData, PieChart pieChart) {
        if (comboBox.getSelectionModel().isEmpty()) {
            AlertMessages.showErrorMessage("Missing info", "Please select an amount of coins from the bottom left dropdown.");
        } else {
            PieChartClass pcc = new PieChartClass(coinList, pieChartCoins, comboBox, pieChartData, pieChart);
            pcc.displayGraph();
        }
    }

    /**
     * Make pie chart for the dashboard.
     *
     * @param coinList
     * @param pieChart
     */
    public void PieChartDash(LinkedList<SingleCoin> coinList, PieChart pieChart) {
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
        LinkedList<SingleCoin> temp = USER.getSortedCoinList();
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

    /**
     * Create bar chart using current prices of all saved coins.
     * @param _barChart
     * @param _linkedMap
     * @param _numCoins
     * @param _userCoinList
     * @param _textArea
     */
    public void multiBarChart(BarChart _barChart, LinkedList<LinkedHashMap<Double, String>> _linkedMap, int _numCoins, LinkedList<UserCoin> _userCoinList, TextArea _textArea) {
        BarChartClass bcc = new BarChartClass(_barChart, _linkedMap, _numCoins, _userCoinList, _textArea);
        bcc.displayGraph();
    }

    /**
     * Create bar chart of the price history for a single coin.
     * @param _singleHistoryMap
     * @param _timeSelection
     * @param _bc
     * @param _textArea
     */
    public void singleBarChart(LinkedHashMap<Double, String> _singleHistoryMap,
            String _timeSelection, BarChart _bc, TextArea _textArea) {
        BarChartClass bcc = new BarChartClass(_singleHistoryMap, _timeSelection, _bc, _textArea);
        bcc.displayGraph();
    }

    /**
     * This creates the right click menu on the onlineUsers list. It also maps
     * each button to an action.
     */
    public void listCells(ListView onlineUsersListT2, String uname) {
        ListClass lcc = new ListClass(uname);
        lcc.populateFriends(onlineUsersListT2, 0);
    }

    /**
     * Creates the table used on the dashboard.
     * Slightly slimmed down from the table on tab1.
     * @param _tableDash
     * @param _userSingleCoins
     */
    public void createTable(TableView _tableDash, LinkedList<SingleCoin> _userSingleCoins) {
        TabAssistantController tas = new TabAssistantController();
        tas.coinTableDash(_tableDash, USER.getTList());
    }

    /**
     * THIS calls and create the table object.
     * @param _tableDash
     */
    public void createTableFriendsCoins(TableView _tableDash) {
        TabAssistantController tas = new TabAssistantController();
        tas.coinTableFriendsCoin(_tableDash);
    }

    public void createFriendList(ListView friendsListT2, int _code) {
        ListClass lc = new ListClass(UNAME);
        if (_code == 0) {
            lc.populateFriends(friendsListT2, 0);
        } else {
            lc.populateFriends(friendsListT2, 1);
        }
    }

    // ========== GETTERS ========== //


    /**
     * Get the current user
     * @return User
     */
    public User getCurrentUser(){
        return currentUser;
    }

    public LinkedList<String> getOnlineUsers() {
        return this.onlineUserList;
    }

    public LinkedList<LinkedHashMap<Double, String>> getLinkedUserHistoryMap() {
        return this.linkedUserHistoryMap;
    }

    public LinkedList<UserCoin> getUserCoinList() {
        return this.userCoinList;
    }

    public LinkedList<SingleCoin> getUserSingleCoins() {
        return this.userSingleCoins;
    }

    /**
     * Get coin list
     * @return
     */
    public LinkedList<SingleCoin> getCoinList(){
        return coinList;
    }

    /**
     * Return generic User Coin linked list.
     * @param _username
     * @return temp
     */
    public LinkedList<T> getObjUserCoinList(String _username){
        ConnectToDatabase conn = new ConnectToDatabase();
        LinkedList<T> temp = new LinkedList<>();
        conn.getSavedCoins(UNAME).forEach(item -> {
            temp.add((T)item);
        });
        return temp;
    }

    /**
     * Return generic User Coin linked list.
     * @return temp
     */
    public LinkedList<T> getObjSavedCoinList(){
//        this.userSingleCoins = USER.getUserSingleCoins();
        LinkedList<T> temp = new LinkedList<>();
        this.userSingleCoins.forEach(item -> {
            temp.add((T)item);
        });
        return temp;
    }

    /**
     * Return the generic list of market objects;
     * @return
     */
    public LinkedList<T> getObjMarketList() {
        GetMarketsApi gma = new GetMarketsApi();
        return gma.getGenericMarketList();
    }

        /**
     * Get users saved coins from database then create SingleCoin objects
     * for each.
     */
    public void createCoinLists() {
        ConnectToDatabase conn = new ConnectToDatabase();
        this.userCoinList = USER.getUserCoinList();
        this.friendList = USER.getFriendList();
        this.onlineUserList = conn.getOnlineUsers();
        conn.close();
        coinList = USER.getCoinList();
        coinList.forEach((item) -> {
            this.userCoinList.forEach((entry) -> {
                if (item.getName().equalsIgnoreCase(entry.getName())){
                    this.userSingleCoins.add(item);
                }
            });
        });
        this.singleHistoryMap = new CoinHistory().getSingleHistory();
        this.userCoinList.forEach((item) -> {
            this.userHistoryMap = new CoinHistory(item.getCoinID(), item.getName(), this.TIMEFRAME).getSingleHistory();
            this.linkedUserHistoryMap.add(this.userHistoryMap);
        });

    }

    // ========= SETTERS ==========

    /**
     * Set object list for tabAssistantController.
     * @param _objList
     */
    public void setObjectList(LinkedList<Object> _objList){
        this.objectList = _objList;
    }

    /**
     * Set coin list;
     * @param _list
     */
    public void setCoinList(LinkedList<SingleCoin> _list){
        coinList = _list;
    }

    /**
     * Get object list for tabAssistantController.
     * @return
     */
    public LinkedList<Object> setObjectList(){
        return this.objectList;
    }

    /**
     * Set the current user
     * @param _user
     */
    public void setCurrentUser(User _user){
        currentUser = _user;
    }

    public void setUserCoinList(LinkedList<UserCoin> _list) {
        this.userCoinList = _list;
    }

    public void setUserSingleCoins(LinkedList<SingleCoin> _list) {
        this.userSingleCoins = _list;
    }

    public void setSingleHistoryMap(LinkedHashMap<Double, String> _map) {
        this.singleHistoryMap = _map;
    }

    public void setUserHistoryMap(LinkedHashMap<Double, String> _map) {
        this.userHistoryMap = _map;
    }

    public void setLinkedUserHistoryMap(LinkedList<LinkedHashMap<Double, String>> _map) {
        this.linkedUserHistoryMap = _map;
    }

}
