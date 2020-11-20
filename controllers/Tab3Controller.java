package controllers;

import controllers.assistantControllers.TabAssistantController;
import models.SingleCoin;
import models.CoinHistory;
import models.CoinRankApi;
import models.ConnectToDatabase;
import models.UserCoin;
import coinTrack.FXMLDocumentController;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static controllers.Tab1Controller.DEBUG;
import models.CoinDatabaseConnection;

/**
 * Tab controller for the dashboard, "tab 3".
 * @author Kyle
 */

public class Tab3Controller implements Initializable{

    private static String USERNAME = coinTrack.FXMLDocumentController.uname;
    private final String TIMEFRAME = "24h";
    private TextArea textArea;
    private TabAssistantController tas;

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
        System.out.println("nothing here yet");
    }

    private void installTextFieldEvent() {
        this.searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LinkedList<Label> temp = new LinkedList<>();
                tas.coinList.forEach((item) -> {
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
     * Create the table.
     * Uses tabAssistantController to format the table.
     */
    private void createTable() {
        this.tas.coinTableDash(this.tableDash, this.tas.userSingleCoins);
    }

    /**
     * Create the pie chart.
     * Uses tabAssistantController to create graph.
     */
    private void createPieChart() {
        this.tas.PieChartDash(this.tas.userSingleCoins, this.pieChartDash);
    }

    /**
     * Create the bar chart.
     * Uses tabAssistantController to create graph.
     */
    private void createBarChart() {
        this.tas.multiBarChart(this.barChartDash, this.tas.linkedUserHistoryMap, this.tas.userCoinList.size(), this.tas.userCoinList, this.textArea);
        this.barChartDash.setLegendVisible(true);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        this.tas.addOnlineUsers(this.tas.onlineUserList, this.onlineUsersListT3);
    }

    /**
     * This creates the right click menu on the onlineUsers list.
     * It also maps each button to an action.
     */
    private void createListCells() {
//        Tab2AssistantController tas = new Tab2AssistantController();
        this.tas.listCells(this.onlineUsersListT3, USERNAME);
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        tas.createFriendList(this.friendsListT3, 0);
    }

    /**
     * Add saved coins to the right side accordion.
     */
    private void populateSavedCoins() {
        this.tas.populateSavedCoins(this.savedCoinsListT3, this.tas.savedCoins);
    }

    /**
     * Save coin using coinID and username.
     * Calls coinDatabaseConnection which links to
     * actual database class.
     * @param userName
     * @param coinID
     */
    private void saveCoin(String _userName, int _coinID) {
        CoinDatabaseConnection coinConn = new CoinDatabaseConnection();
        coinConn.saveCoin(_userName, _coinID);
    }

    /**
     * Adds mouse handler for search VBox labels.
     *
     * @param _lbl
     */
    private void addListListener(Label _lbl) {
        _lbl.setOnMouseEntered((MouseEvent mouseEvent) -> {
            _lbl.setStyle("-fx-background-color: #8f8f8f;");
        });
        _lbl.setOnMouseExited((MouseEvent mouseEvent) -> {
            _lbl.setStyle("-fx-background-color: #323232;");
        });
        ContextMenu cm = new ContextMenu();
        MenuItem m1 = new MenuItem("Save Coin");
        m1.setOnAction((event) -> {
            if (DEBUG) {System.out.println("Choice 1 clicked!");}
        });
        cm.getItems().addAll(m1);
        _lbl.setContextMenu(cm);
    }

    private void setTheme() {
        this.tas.addThemeListener();
    }

    /**
     * Add coins to the search vbox.
     * This is updated when the user types something in the search field.
     */
    private void populateSearch() {
        System.out.println(this.tas.coinList.size());
        for (int i = 0; i < this.tas.coinList.size(); i++) {
            Label tmp = new Label(this.tas.coinList.get(i).getSymbol() + ": " + this.tas.coinList.get(i).getName());
            this.vbox.getChildren().add(tmp);
            addListListener(tmp);
            tmp.setMaxWidth(Double.MAX_VALUE);
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        populateSavedCoins();
    }

    /**
     * Initialize tab3, "dashboard".
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tas = new TabAssistantController();
        this.tas.userCoinList = new LinkedList<>();
        this.tas.coinList = new LinkedList<>();
        this.tas.userSingleCoins = new LinkedList<>();
        this.tas.singleHistoryMap = new LinkedHashMap<>();
        this.tas.userHistoryMap = new LinkedHashMap<>();
        this.tas.linkedUserHistoryMap = new LinkedList<>();
        this.tas.friendList = new LinkedList<>();
        this.tas.onlineUserList = new LinkedList<>();
        this.tas.savedCoins = new LinkedList<>();
        USERNAME = FXMLDocumentController.uname;
        this.tas.getCoinList(USERNAME,3);
        populateSearch();
        createTable();
        createPieChart();
        createBarChart();
        createListCells();
        addOnlineUsersToList();
        populateSavedCoins();
        addFriendsToList();
        installTextFieldEvent();
//        setTheme();
    }
}
