package tabControllers;
/**
 * This is the document controller for Tab 1.
 * Main function is to handle scans, table view
 * and the side accordion.
 *
 * - Kyle
 */
import coinClasses.AlphaVantage;
import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToApi;
import coinClasses.ConnectToDatabase;
import coinClasses.FixerApi;
import coinClasses.GlobalCoinStats;
import coinClasses.SingleCoin;
import coinClasses.SingleCoinHistory;
import coinClasses.UserCoin;
import coinTrack.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tabControllers.assistantControllers.Tab1AssistantController;

/**
 *
 * @author Kyle
 */
public class Tab1Controller implements Initializable{

    private LinkedHashMap<String, String> coinNamePrice;
    private LinkedList<SingleCoin> coinList;
    private int count;
    private CoinHistory coinHistory;
    private GlobalCoinStats globalStats;
    private CoinRankApi cri;
    private String uname;
    private LinkedList<String> onlineUsers;
    private LinkedList<String> friendList;
    public static Stage mainPage1;
    private LinkedList<UserCoin> savedCoins;
    private ObservableList<String> currencies;
    private String selectedCurrency;
    private String previousCurrency;
    private ComboBox cb;
    private long currencyRate;
    public static boolean DEBUG;
    Tab1AssistantController assistT1;

    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private ComboBox currencyCombo;
    @FXML private Menu editBtn;

    // Accordion
    @FXML private ListView onlineUsersList;
    @FXML private ListView savedCoinsList;
    @FXML private ListView friendsList;
    @FXML private ContextMenu cm;

    // Bottom portion (button bar)
    @FXML private CheckBox debugBtn;
    @FXML private TextArea txtAreaT1;
    @FXML private WebView webViewT1;
    @FXML private CheckBox searchCoins;
    @FXML private CheckBox searchGlobalStats;
    @FXML private Text messageText;
    @FXML private ToolBar bottomToolbar;

    // Table View
    @FXML public TableView<SingleCoin> tableViewT1;


    /**
     * Search for a specific coin.
     *
     * This needs work.
     *
     * Determine if the entered text is a string
     * or an integer. Then call the coin api, and
     * display the information for that coin.
     * @param event
     */
    @FXML
    public void handleSearch(ActionEvent event) {
        txtAreaT1.setText("Searching...");
        if (searchCoins.getText() != "") {
//            coinHistory = new CoinHistory()
        }
        coinHistory = new CoinHistory();
    }

    /**
     * This method handles both scanning for all coins
     * and all markets / exchanges.
     * Simple logic determines the selected checkBoxes,
     * clears existing data, then calls the appropriate
     * classes and displays data.
     * @param event
     */
    @FXML
    private void handleScan(ActionEvent event) {
        System.out.println("Scanning");
        if (searchGlobalStats.isSelected() && searchCoins.isSelected()){
            tableViewT1.getItems().clear();
            tableViewT1.getColumns().clear();
            txtAreaT1.setText("");
            displayGlobalStats();
            displayCoinText();
        } else if (searchGlobalStats.isSelected()){
            txtAreaT1.setText("");
            displayGlobalStats();
        } else if (searchCoins.isSelected()) {
            tableViewT1.getItems().clear();
            tableViewT1.getColumns().clear();
            if(DEBUG){System.out.println("search coins");}
            displayCoinText();
        }
    }

    /**
     * Clears the tableView and textArea.
     * @param event
     */
    @FXML
    private void handleClearT1(ActionEvent event) {
        System.out.println("clearing data");
        tableViewT1.getItems().clear();
        txtAreaT1.setText("");
    }

    @FXML
    private void handleLogOutT1(ActionEvent event) {
        if(DEBUG){System.out.println("logging out");}
        Parent root;
        try {
            Tab1Controller.mainPage1 = new Stage();
            setOnlineStatus(coinTrack.FXMLDocumentController.uname, 0);
            root = FXMLLoader.load(getClass().getClassLoader().getResource("coinTrack/FXMLLogin.fxml"));
            this.scene = new Scene(root);
            Tab1Controller.mainPage1.setScene(this.scene);
            Tab1Controller.mainPage1.show();
            closeOldStage();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reset to default currency.
     */
    @FXML
    private void handleResetCurr(ActionEvent event) {
        resetCurrency();
    }
    
    @FXML
    private void handleDebug(ActionEvent event) {
        if (this.debugBtn.isArmed()) {
            System.out.println("Debug mode enabled");
            this.DEBUG = true;
        }
    }
    
    @FXML
    private void handleTest(ActionEvent event) {
        AlphaVantage av = new AlphaVantage("BTC");
        av.getDaily().forEach((item) -> {
            System.out.println(item.keySet());
        });
        
        
    }

    // ========== HELPER METHODS ==========


    /**
     * Reset to default currency.
     */
    private void resetCurrency() {
        this.selectedCurrency = "USD";
        this.cb.setPromptText(this.selectedCurrency);
        this.currencyRate = 1;
        tableViewT1.getItems().clear();
        tableViewT1.getColumns().clear();
        txtAreaT1.setText("");
        displayGlobalStats();
        displayCoinText();
//        displayCoinText();
        if(DEBUG){System.out.println("selected currency: " + this.selectedCurrency);}
        
    }

    /**
     * Display the api data to the screen.
     */
    private void displayCoinText() {
        this.cri = new CoinRankApi();
        this.cri.join();
        this.coinList = this.cri.getCoinList();
        /**
         * Update coins in database. probably not necessary to run here. 
         * It already runs at launch.
         */
        //updateCoinPricesDB();
        /**
         * Add coins to new database table all_coins
         */
        //cri.updateDatabaseCoins(temp);

        this.count = 50;
        if(DEBUG){System.out.println("number of coins: " + cri.getCoinList().size());}
        this.coinList = this.cri.getCoinList();
        this.coinNamePrice = this.cri.getNamePrice();
//        coinList = cri.getCoinList();
        displayMultiCoinTable();
        createTableCells();

    }
    
    /**
     * Update coin prices in the database.
     */
    private void updateCoinPricesDB() {
        ConnectToDatabase conn = new ConnectToDatabase();
        if (this.coinList.isEmpty()) {
            this.cri = new CoinRankApi();
            this.cri.run();
            this.cri.join();
            this.coinList = this.cri.getCoinList();
        }
        this.coinList.forEach((item) -> {
            conn.updateCoinPrices(item.getId(), Double.parseDouble(item.getPrice()),
                    item.getChange(), item.getVolume());
        });
        conn.close();
    }

    /**
     * Display global stats in bottom text area.
     */
    private void displayGlobalStats() {
        globalStats = new GlobalCoinStats();
        String text = "";
        text += "Total Coins: " + globalStats.getTotalCoins() + "\n";
        text += "Total Markets: " + globalStats.getTotalMarkets() + "\n";
        text += "Total Exchanges: " + globalStats.getTotalExchanges() + "\n";
        text += "Market Cap: " + globalStats.getTotalMarketCap() + "\n";
        text += "Total 24h Volume: " + globalStats.getTotal24hVolume() + "\n";

        txtAreaT1.setText(text);
    }

    /**
     * Add data to tableView.
     * This is done in Tab1AssistantController.java to reduce space used here.
     */
    private void displayMultiCoinTable() {
        if(DEBUG){System.out.println("current currency: " + this.selectedCurrency);}
        assistT1.coinTable(this.tableViewT1, this.coinList, this.webViewT1, this.selectedCurrency, this.currencyRate);
    }

    /**
     * Change a users online status. i.e. when they log on/off .
     * @param _uname
     * @param _status
     */
    private void setOnlineStatus(String _uname, int _status) {
        if(DEBUG){System.out.println("Update " + _uname + "'s online status");}
        ConnectToDatabase conn = new ConnectToDatabase();
        conn.setUserOnlineStatus(_uname, _status);
        conn.close();
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        ConnectToDatabase conn = new ConnectToDatabase();
        this.onlineUsers = new LinkedList<>();
        this.onlineUsers = conn.getOnlineUsers();
        conn.close();
        if(DEBUG){System.out.println("total online users: " + this.onlineUsers.size());}
        for (int i = 0; i < this.onlineUsers.size(); i++) {
            onlineUsersList.getItems().add(this.onlineUsers.get(i));
        }
    }

    /**
     * This creates the right click menu on the onlineUsers list.
     * It also maps each button to an action.
     */
    private void createListCells() {
        onlineUsersList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addFriendItem = new MenuItem();
            addFriendItem.textProperty().bind(Bindings.format("Add Friend"));
            addFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                String friendName = cell.getItem();
                if (friendName.equals(this.uname)) {
                    txtAreaT1.setText("Wow, so lonely. Can't add yourself as a friend..");
                } else {
                    conn.addFriend(this.uname, friendName);
                    addFriendsToList();
                    if(DEBUG){System.out.println("Added " + friendName + " to friend list");}
                    txtAreaT1.setText("Added " + friendName + " to friend list!");
                }
                conn.close();
            });
            MenuItem sendMessageItem = new MenuItem();
            sendMessageItem.textProperty().bind(Bindings.format("Send Message"));
            sendMessageItem.setOnAction(event -> {
                // Send a message to a friend
            });
            contextMenu.getItems().addAll(addFriendItem, sendMessageItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
    }
    /**
     * This creates the right click menu on the onlineUsers list.
     * It also maps each button to an action.
     */
    private void createTableCells() {
        ContextMenu cm2 = new ContextMenu();
        MenuItem mi1 = new MenuItem("Save Coin");
        mi1.setOnAction(event -> {
                SingleCoin item = tableViewT1.getSelectionModel().getSelectedItem();
                saveCoin(this.uname, item.getId());
                if(DEBUG){System.out.println("Added " + item.getName() + " to saved coin list");}
                populateSavedCoins();
            });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableViewT1.setContextMenu(menu);
        populateSavedCoins();

        cm2.getItems().add(mi1);
        MenuItem mi2 = new MenuItem("Share Coin");
        cm2.getItems().add(mi2);
        MenuItem mi3 = new MenuItem("Track Coin");
        cm2.getItems().add(mi3);
        tableViewT1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm2.show(tableViewT1, t.getScreenX(), t.getScreenY());
                }
            }
        });
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        ConnectToDatabase conn = new ConnectToDatabase();
        this.friendList = new LinkedList<>();
        this.friendList = conn.getFriendList(this.uname);
        conn.close();
        for (int i = 0; i < this.friendList.size(); i++) {
            friendsList.getItems().add(this.friendList.get(i));
        }
    }

    /**
     * Create right-clickable cells for the friend list.
     *
     * Allow the user to share coins, send messages, and
     * remove the friend.
     */
    private void createFriendListCells() {
        friendsList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addFriendItem = new MenuItem();
            addFriendItem.textProperty().bind(Bindings.format("Share coin"));
            addFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                String friendName = cell.getItem();
                // Do stuff
                conn.close();
            });
            MenuItem sendMessageItem = new MenuItem();
            sendMessageItem.textProperty().bind(Bindings.format("Send Message"));
            sendMessageItem.setOnAction(event -> {
                // Send a message to a friend
            });
            MenuItem removeFriendItem = new MenuItem();
            sendMessageItem.textProperty().bind(Bindings.format("Remove Friend"));
            sendMessageItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                conn.removeFriend(cell.getText());
                if(DEBUG){System.out.println("Removed " + cell.getText() + " from friend list");}
                addFriendsToList();
                conn.close();
            });
            contextMenu.getItems().addAll(addFriendItem, sendMessageItem, removeFriendItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
    }

    /**
     * Returns and closes the main stage from the class where it was created
     */
    private void closeOldStage() {
        coinTrack.FXMLDocumentController.mainStage.close();
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
     * Pull saved coin data from database and add it to the accordion.
     */
    private void populateSavedCoins() {
        ConnectToDatabase conn = new ConnectToDatabase();
        savedCoinsList.getItems().clear();
        this.savedCoins = conn.getSavedCoins(this.uname);
        conn.close();
        if (DEBUG){System.out.println("Populating saved coin list");}
        if (this.savedCoins != null && this.savedCoins.size() > 0) {
            for (int i = 0; i < this.savedCoins.size(); i++) {
                savedCoinsList.getItems().add(this.savedCoins.get(i));
                if (DEBUG){System.out.println("Adding: " + this.savedCoins.get(i));}
            }
        }
    }
    
    /**
     * Populates the currency changer drop down combo box located
     * under the "edit" button in the menu bar.
     */
    private void populateCurrencyDropdown() {
        
        try {
            FixerApi fca = new FixerApi();
            HashMap<String,String> map = fca.getSupportedSymbols();
            // Initialize currencies observable array
            this.currencies = FXCollections.observableArrayList();
            // Loop through and add symbols to comboBox
            for (Map.Entry<String,String> entry : map.entrySet()){
                this.currencies.add(entry.getKey() + ": " + entry.getValue());
            }
            this.cb = new ComboBox(FXCollections.observableArrayList(this.currencies));
            this.cb.setTooltip(new Tooltip("Select the type of currency to convert data to."));
            this.cb.setPromptText("Select Currency");
            this.bottomToolbar.getItems().add(this.cb);
            if (DEBUG){System.out.println("Adding currencies to drop-down menu");}
            // Add event handler to combobox items
            EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        previousCurrency = selectedCurrency;
                        selectedCurrency = cb.getValue().toString().split(":")[0];
                        currencyRate = fca.getExchangeRate("USD", selectedCurrency); //previousCurrency
                        System.out.println(previousCurrency + " x " + currencyRate + " = " + selectedCurrency);
                        if (currencyRate == 0){currencyRate = 1;}
                        if (DEBUG){System.out.println("currency rate: " + currencyRate);}
                    }
            };
            this.cb.setOnAction(event);
        } catch (IOException ex) {
            Logger.getLogger(Tab1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addContextMenuToList() {
        ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteCoin = new MenuItem();
            deleteCoin.textProperty().bind(Bindings.format("Delete"));
            deleteCoin.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                conn.deleteSavedCoin((UserCoin)savedCoinsList.getSelectionModel().getSelectedItem());
                populateSavedCoins();
                conn.close();
            });
           
            contextMenu.getItems().addAll(deleteCoin);
            savedCoinsList.setContextMenu(contextMenu);
    }

    /**
     * Initialize the tab
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.uname = coinTrack.FXMLDocumentController.uname;
        this.messageText.setText("Hello " + uname);
        this.assistT1 = new Tab1AssistantController();
        this.editBtn = new Menu();
        this.coinList = new LinkedList<>();
        // Set default currency
        this.selectedCurrency = "USD";
        this.currencyRate = 1;
        populateCurrencyDropdown();
        populateSavedCoins();
        createListCells();
        createFriendListCells();
        addOnlineUsersToList();
        addFriendsToList();
        updateCoinPricesDB();
        this.onlineUsersList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cm = new ContextMenu();
                    cm.show(onlineUsersList, event.getScreenX(), event.getScreenY());
                }
            }
        });
        addContextMenuToList();
    }
}
