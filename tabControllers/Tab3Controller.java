package tabControllers;

import coinClasses.SingleCoin;
import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.UserCoin;
import coinTrack.FXMLDocumentController;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tabControllers.assistantControllers.Tab1AssistantController;
import tabControllers.assistantControllers.Tab2AssistantController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kyle
 */

public class Tab3Controller implements Initializable{

    private static final String USERNAME = FXMLDocumentController.uname;
    private final String TIMEFRAME = "24h";
    private LinkedList<UserCoin> userCoinList;
    protected static LinkedList<SingleCoin> coinList;
    private LinkedList<SingleCoin> userSingleCoins;
    private LinkedList<UserCoin> savedCoins;
    private LinkedList<String> friendList;
    private LinkedList<String> onlineUserList;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private LinkedHashMap<Double, String> userHistoryMap;
    private LinkedList<LinkedHashMap<Double, String>> linkedUserHistoryMap;

    @FXML private BarChart barChartDash;
    @FXML private PieChart pieChartDash;
    @FXML private TableView tableDash;
    @FXML private ListView listDash;
    @FXML private ListView savedCoinsListT3;
    @FXML private ListView friendsListT3;
    @FXML private ListView onlineUsersListT3;
    @FXML private VBox vbox;
    @FXML private TextField searchField;
    
    @FXML private void handleSearch(ActionEvent event) {
        
    }

    private void installTextFieldEvent() {
        this.searchField.textProperty().addListener(new ChangeListener<String>() {
            
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                System.out.println(" Text Changed to  " + newValue + ")\n");
                LinkedList<Text> temp = new LinkedList<>();
                coinList.forEach((item) -> {
                    if (item.getName().toLowerCase().contains(newValue.toLowerCase()) || item.getSymbol().toLowerCase().contains(newValue.toLowerCase())){
                        temp.add(new Text(item.getSymbol() + ": " + item.getName()));
                    }
                });
                vbox.getChildren().clear();
                for (int i = 0; i < temp.size(); i++) {
                    Label tmp = new Label(temp.get(i).getText());
                    vbox.getChildren().add(tmp);
                }
            }
        });
    }

    /**
     * Get users saved coins from database then create SingleCoin objects
     * for each.
     */
    private void getCoinList() {
        ConnectToDatabase conn = new ConnectToDatabase();
        this.userCoinList = conn.getSavedCoins(USERNAME);
        this.friendList = conn.getFriendList(USERNAME);
        this.onlineUserList = conn.getOnlineUsers();
        conn.close();
        CoinRankApi cri = new CoinRankApi();
        cri.run();
        cri.join();
        coinList = cri.getCoinList();
        coinList.forEach((item) -> {
            userCoinList.forEach((entry) -> {
                if (item.getName().equalsIgnoreCase(entry.getName())){
                    userSingleCoins.add(item);
                }
            });
        });
        this.singleHistoryMap = new CoinHistory().getSingleHistory();
        this.userCoinList.forEach((item) -> {
            userHistoryMap = new CoinHistory(item.getCoinID(), item.getName(), this.TIMEFRAME).getSingleHistory();
            linkedUserHistoryMap.add(userHistoryMap);
        });
        
    }

    /**
     * Create the table.
     * Uses tab1AssistantController to format the table.
     */
    private void createTable() {
        Tab1AssistantController tas1 = new Tab1AssistantController();
        tas1.coinTableDash(this.tableDash, this.userSingleCoins);
    }
    
    private void createPieChart() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.PieChartDashboard(this.userSingleCoins, this.pieChartDash);
    }
    
    private void createBarChart() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.multiBarChart(this.barChartDash, this.linkedUserHistoryMap, this.userCoinList.size(), this.userCoinList);
        this.barChartDash.setLegendVisible(true);
    }
    
    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.addOnlineUsers(this.onlineUserList, this.onlineUsersListT3);
    }

    /**
     * This creates the right click menu on the onlineUsers list. 
     * It also maps each button to an action.
     */
    private void createListCells() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.listCells(this.onlineUsersListT3, USERNAME);
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.friendList(this.friendList, USERNAME, this.friendsListT3);
    }

    /**
     * Creates right-clickable cells in the friends list in the accordion.
     */
    private void createFriendListCells() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.friendListCells(this.friendsListT3);
    }
    
    private void addListEvents() {
        onlineUsersListT3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            ContextMenu cmu = new ContextMenu();
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cmu.show(onlineUsersListT3, event.getScreenX(), event.getScreenY());
                }
            }
        });
        savedCoinsListT3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            ContextMenu cmu = new ContextMenu();
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cmu.show(savedCoinsListT3, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }

    private void populateSavedCoins() {
        ConnectToDatabase conn = new ConnectToDatabase();
        savedCoinsListT3.getItems().clear();
        this.userCoinList = conn.getSavedCoins(USERNAME);
        conn.close();
        if (this.userCoinList != null && this.userCoinList.size() > 0) {
            for (int i = 0; i < this.savedCoins.size(); i++) {
                savedCoinsListT3.getItems().add(this.savedCoins.get(i));
            }
        }
    }
    
    private void populateSearch() {
        System.out.println(coinList.size());
        for (int i = 0; i < coinList.size(); i++) {
            Text tmp = new Text(coinList.get(i).getSymbol() + ": " + coinList.get(i).getName());
            this.vbox.getChildren().add(tmp);
        }
    }

    /**
     * Initialize tab3, "dashboard".
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userCoinList = new LinkedList<>();
        coinList = new LinkedList<>();
        this.userSingleCoins = new LinkedList<>();
        this.singleHistoryMap = new LinkedHashMap<>();
        this.userHistoryMap = new LinkedHashMap<>();
        this.linkedUserHistoryMap = new LinkedList<>();
        this.friendList = new LinkedList<>();
        this.onlineUserList = new LinkedList<>();
        this.savedCoins = new LinkedList<>();
        getCoinList();
        populateSearch();
        createTable();
        createPieChart();
        createBarChart();
        createListCells();
        addOnlineUsersToList();
        populateSavedCoins();
        addFriendsToList();
        createFriendListCells();
        installTextFieldEvent();
    }
}
