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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static tabControllers.Tab1Controller.DEBUG;
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

    private static final String USERNAME = coinTrack.FXMLDocumentController.uname;
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
                LinkedList<Label> temp = new LinkedList<>();
                coinList.forEach((item) -> {
                    if (item.getName().toLowerCase().contains(newValue.toLowerCase()) || item.getSymbol().toLowerCase().contains(newValue.toLowerCase())) {
                        Label txt = new Label(item.getSymbol() + ": " + item.getName());
                        temp.add(txt);
                        addListListener(txt);
                        txt.setMaxWidth(Double.MAX_VALUE);
                    }
                });
                vbox.getChildren().clear();
                for (int i = 0; i < temp.size(); i++) {
                    Label tmp = new Label(temp.get(i).getText());
                    vbox.getChildren().add(tmp);
                    addListListener(tmp);
                    tmp.setMaxWidth(Double.MAX_VALUE);
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
            for (int i = 0; i < this.userCoinList.size(); i++) {
                savedCoinsListT3.getItems().add(this.userCoinList.get(i));
            }
        }
    }
    
    /**
     * Save coin using coinID and username.
     * @param userName
     * @param coinID
     */
    private void saveCoin(String userName, int coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(userName, coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }
    
    /**
     * Adds mouse handler for search VBox labels.
     *
     * @param _lbl
     */
    private void addListListener(Label _lbl) {
        _lbl.setOnMouseEntered((MouseEvent mouseEvent) -> {
            _lbl.setStyle("-fx-background-color: #bababa;");
        });
        _lbl.setOnMouseExited((MouseEvent mouseEvent) -> {
            _lbl.setStyle("-fx-background-color: white;");
        });
        ContextMenu cm = new ContextMenu();
        MenuItem m1 = new MenuItem("Save Coin");
        m1.setOnAction((event) -> {
            if (DEBUG) {System.out.println("Choice 1 clicked!");}
        });
        cm.getItems().addAll(m1);
        _lbl.setContextMenu(cm);
    }
    
    /**
     * Add coins to the search vbox.
     * This is updated when the user types something in the search field.
     */
    private void populateSearch() {
        System.out.println(coinList.size());
        for (int i = 0; i < coinList.size(); i++) {
            Label tmp = new Label(coinList.get(i).getSymbol() + ": " + coinList.get(i).getName());
            this.vbox.getChildren().add(tmp);
            addListListener(tmp);
            tmp.setMaxWidth(Double.MAX_VALUE);
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
    
    @FXML
    private void refresh(ActionEvent event) {
        populateSavedCoins();
    }
}
