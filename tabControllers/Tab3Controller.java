package tabControllers;

/**
 * Tab controller for the dashboard, "tab 3".
 * @author Kyle
 */

import coinClasses.SingleCoin;
import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.UserCoin;
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
import static tabControllers.Tab1Controller.DEBUG;
import tabControllers.assistantControllers.TabAssistantController;

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
    @FXML private Text textLabel;

    @FXML private void handleSearch(ActionEvent event) {
        System.out.println("nothing here yet");
    }

    /**
     * Auto-Updating search bar.
     */
    private void installTextFieldEvent() {
        this.searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LinkedList<Label> temp = new LinkedList<>();
                // Interactively add / remove coin names to the VBox
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
        // Create list of SingleCoins by pulling full list and comparing to saved list.
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
     * Uses tabAssistantController to format the table.
     */
    private void createTable() {
        this.tas.coinTableDash(this.tableDash, this.userSingleCoins);
    }

    /**
     * Create the pie chart.
     * Uses tabAssistantController to create graph.
     */
    private void createPieChart() {
        this.tas.PieChartDash(this.userSingleCoins, this.pieChartDash);
    }

    /**
     * Create the bar chart.
     * Uses tabAssistantController to create graph.
     */
    private void createBarChart() {
        this.tas.multiBarChart(this.barChartDash, this.linkedUserHistoryMap, this.userCoinList.size(), this.userCoinList, this.textArea);
        this.barChartDash.setLegendVisible(true);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        this.tas.addOnlineUsers(this.onlineUserList, this.onlineUsersListT3);
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
        tas.createFriendList(this.friendsListT3);
    }

    /**
     * Add saved coins to the right side accordion.
     */
    private void populateSavedCoins() {
        this.tas.populateSavedCoins(this.savedCoinsListT3, this.savedCoins);
    }

    /**
     * Save coin using coinID and username.
     * @param userName
     * @param coinID
     */
    private void saveCoin(String _userName, int _coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(_userName, _coinID)) {
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
        System.out.println(coinList.size());
        for (int i = 0; i < coinList.size(); i++) {
            Label tmp = new Label(coinList.get(i).getSymbol() + ": " + coinList.get(i).getName());
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
        this.userCoinList = new LinkedList<>();
        coinList = new LinkedList<>();
        this.userSingleCoins = new LinkedList<>();
        this.singleHistoryMap = new LinkedHashMap<>();
        this.userHistoryMap = new LinkedHashMap<>();
        this.linkedUserHistoryMap = new LinkedList<>();
        this.friendList = new LinkedList<>();
        this.onlineUserList = new LinkedList<>();
        this.savedCoins = new LinkedList<>();
        this.textLabel.setText("Hello " + USERNAME);
        getCoinList();
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
