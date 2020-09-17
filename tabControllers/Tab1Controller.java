package tabControllers;
/**
 * This is the document controller for Tab 1.
 * Main function is to handle scans, table view
 * and the side accordion.
 *
 * - Kyle
 */
import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.GlobalCoinStats;
import coinClasses.SingleCoin;
import coinClasses.SingleCoinHistory;
import coinTrack.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private static Stage mainPage1;
    private LinkedList<String> savedCoins;
    Tab1AssistantController assistT1;

    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;

    // Accordion
    @FXML private ListView onlineUsersList;
    @FXML private ListView savedCoinsList;
    @FXML private ListView friendsList;
    @FXML private ContextMenu cm;

    // Bottom portion (button bar)
    @FXML private TextArea txtAreaT1;
    @FXML private WebView webViewT1;
    @FXML private CheckBox searchCoins;
    @FXML private CheckBox searchGlobalStats;
    @FXML private Text messageText;

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
            displayCoinText();
        }
    }

    // ========== HELPER METHODS ==========

    /**
     * Display the api data to the screen.
     *
     * Currently this just posts it into the
     * TextArea at the bottom of the page.
     */
    private void displayCoinText() {
        cri = new CoinRankApi();
        cri.join();
        LinkedList<SingleCoin> temp = cri.getCoinList();
        
        /**
         * Add coins to new database table all_coins
         */
//        cri.updateDatabaseCoins(temp);
        
        count = 50;
        System.out.println(cri.getLimit());
        coinNamePrice = cri.getNamePrice();
        coinList = cri.getCoinList();
        displayMultiCoinTable();
        createTableCells();

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
        System.out.println("logging out");
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
     * Add data to tableView.
     * This is done in Tab1AssistantController.java to reduce space used here.
     */
    private void displayMultiCoinTable() {
        assistT1.coinTable(tableViewT1, coinList, webViewT1);
    }

    /**
     * Change a users online status. i.e. when they log on/off .
     * @param _uname
     * @param _status
     */
    private void setOnlineStatus(String _uname, int _status) {
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
    
    private void saveCoin(String userName, int coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(userName, coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }

    private void populateSavedCoins() {
        ConnectToDatabase conn = new ConnectToDatabase();
        savedCoinsList.getItems().clear();
        this.savedCoins = conn.getSavedCoins(this.uname);
        conn.close();
        if (this.savedCoins != null && this.savedCoins.size() > 0) {
            for (int i = 0; i < this.savedCoins.size(); i++) {
                savedCoinsList.getItems().add(this.savedCoins.get(i));
            }
        }
    }

    /**
     * Initialize the tab
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.uname = coinTrack.FXMLDocumentController.uname;
        messageText.setText("Hello " + uname);
        assistT1 = new Tab1AssistantController();
        populateSavedCoins();
        createListCells();
        createFriendListCells();
        addOnlineUsersToList();
        addFriendsToList();
        onlineUsersList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cm.show(onlineUsersList, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
}
