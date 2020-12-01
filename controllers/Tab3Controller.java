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
import models.ParseCoinName;
import models.User;

/**
 * Tab controller for the dashboard, "tab 3".
 * @author Kyle
 * @param <T>
 */

public class Tab3Controller<T> implements Initializable{

    private User USER = FXMLDocumentController.getUser();
    private static String USERNAME = coinTrack.FXMLDocumentController.uname;
    private final String TIMEFRAME = "24h";
    private TextArea textArea;
    private TabAssistantController tas;
    private LinkedList<SingleCoin> coinList;

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

    /**
     * Add a listener to the search bar on the dashboard.
     * When you type in the bar, it updates with coins containing
     * the typed words / letters.
     */
    private void installTextFieldEvent() {
        this.coinList = USER.getCoinList();
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
     * Create the table.
     * Uses tabAssistantController to format the table.
     */
    private void createTable() {
        LinkedList<T> tlist = this.USER.createTListFronSingleCoins(this.USER.getUserSingleCoins());
        this.tas.coinTableDash(this.tableDash, tlist);
    }

    /**
     * Create the pie chart.
     * Uses tabAssistantController to create graph.
     */
    private void createPieChart() {
        this.tas.PieChartDash(this.USER.getUserSingleCoins(), this.pieChartDash);
    }

    /**
     * Create the bar chart.
     * Uses tabAssistantController to create graph.
     */
    private void createBarChart() {
        this.tas.multiBarChart(this.barChartDash, this.USER.getLinkedUserHistoryMap(this.TIMEFRAME), this.USER.getUserCoinList().size(), this.tas.getUserCoinList(), this.textArea);
        this.barChartDash.setLegendVisible(true);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        this.tas.addOnlineUsers(this.tas.getOnlineUsers(), this.onlineUsersListT3);
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

        this.tas.populateSavedCoins(this.savedCoinsListT3, this.USER.getSavedCoins());
    }

    /**
     * Save coin using coinID and username.
     * Calls coinDatabaseConnection which links to
     * actual database class.
     * @param userName
     * @param coinID
     */
    private void saveCoin(String _userName, int _coinID) {
        this.USER.saveCoin(_coinID);
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
            int id = this.USER.getCoinID(_lbl.getText().split(":")[0]);
            if (id != -1) {
                this.USER.saveCoin(id);
            }
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
        this.coinList = this.tas.getCoinList();
        System.out.println(this.tas.getCoinList().size());
        for (int i = 0; i < this.tas.getCoinList().size(); i++) {
            Label tmp = new Label(this.coinList.get(i).getSymbol() + ": " + this.coinList.get(i).getName());
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
        this.tas = USER.getTas();
        USERNAME = FXMLDocumentController.uname;
        this.tas.getCoinList();
        populateSearch();
        createTable();
        createPieChart();
        createBarChart();
        createListCells();
        addOnlineUsersToList();
        populateSavedCoins();
        addFriendsToList();
        installTextFieldEvent();
    }
}
