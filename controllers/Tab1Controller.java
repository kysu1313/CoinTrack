package controllers;
/**
 * This is the document controller for Tab 1.
 * Main function is to handle scans, table view
 * and the side accordion.
 *
 * - Kyle
 */
import controllers.assistantControllers.TabAssistantController;
import models.AlphaVantage;
import models.User;
import models.CoinHistory;
import models.CoinRankApi;
import models.ConnectToDatabase;
import models.FixerApi;
import models.GlobalCoinStats;
import models.SaveToDisk;
import models.SingleCoin;
import models.UserCoin;
import coinTrack.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.CoinDatabaseConnection;
import models.GetMarketsApi;
import models.SingleMarket;
import models.viewModels.ListClass;

/**
 *
 * @author Kyle
 */
public class Tab1Controller implements Initializable{

    private LinkedHashMap<String, String> coinNamePrice; //
    private LinkedList<SingleCoin> coinList; //
    private int count;
    private CoinHistory coinHistory; //
    private GlobalCoinStats globalStats; //
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
    public static TabAssistantController tas;

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
    @FXML private ToolBar bottomToolbar;
    @FXML private Button saveBtnT1;
    @FXML private RadioButton allCoinsBtn;
    @FXML private RadioButton savedCoinsBtn;
    @FXML private RadioButton marketsBtn;

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
    private void handleScan(ActionEvent _event) {
        System.out.println("Scanning");
        boolean allCoins = this.allCoinsBtn.isSelected();
        boolean savedCoinsbtn = this.savedCoinsBtn.isSelected();
        boolean markets = this.marketsBtn.isSelected();
//        this.marketsBtn.isSelected()
        if (searchGlobalStats.isSelected() || allCoins || savedCoinsbtn || markets){
            tableViewT1.getItems().clear();
            tableViewT1.getColumns().clear();
            txtAreaT1.setText("");
            displayGlobalStats();
            displayCoinText();
        } else if (searchGlobalStats.isSelected() || allCoins || savedCoinsbtn || markets){
            txtAreaT1.setText("");
            displayGlobalStats();
        } else if (searchCoins.isSelected() || allCoins || savedCoinsbtn || markets) {
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
    private void handleClearT1(ActionEvent _event) {
        System.out.println("clearing data");
        tableViewT1.getItems().clear();
        txtAreaT1.setText("");
    }

    /**
     * Reset to default currency.
     */
    @FXML
    private void handleResetCurr(ActionEvent _event) {
        resetCurrency();
    }

    /**
     * Radio button on Tab 1 for all coin search
     * @param _event
     */
    @FXML
    private void handleAllCoins(ActionEvent _event) {
        this.savedCoinsBtn.setSelected(false);
        this.marketsBtn.setSelected(false);
    }

    /**
     * Radio button on Tab 1 for saved coin search
     * @param _event
     */
    @FXML
    private void handleSavedCoins(ActionEvent _event) {
        this.allCoinsBtn.setSelected(false);
        this.marketsBtn.setSelected(false);
    }

    /**
     * Radio button on Tab 1 for market search.
     * @param _event
     */
    @FXML
    private void handleMarkets(ActionEvent _event) {
        this.savedCoinsBtn.setSelected(false);
        this.allCoinsBtn.setSelected(false);
    }

    /**
     * Debug button enables debug for entire application.
     * @param _event
     */
    @FXML
    private void handleDebug(ActionEvent _event) {
        if (this.debugBtn.isArmed()) {
            System.out.println("Debug mode enabled");
            this.DEBUG = true;
        }
    }

    /**
     * Test button, used for any feature being tested.
     * @param _event
     * @throws ParseException
     * @throws IOException
     */
    @FXML
    private void handleTest(ActionEvent _event) throws ParseException, IOException {
        CoinRankApi cri = new CoinRankApi();
        cri.start();
        cri.join();
        cri.getSortedCoinList().forEach(item -> {
            SingleCoin sc = (SingleCoin)item;
            System.out.println(sc.getName() + ": " + sc.getPrice());
        });
    }

    /**
     * Creates the save view where users can save coin lists.
     * @param _event
     */
    @FXML
    private void handleSaveT1(ActionEvent _event) throws IOException {
        this.cri = new CoinRankApi();
        if(DEBUG){System.out.println("logging out");}
        Parent root;
        try {
            Tab1Controller.mainPage1 = new Stage();
            FXMLDocumentController.user.onlineStatus(0);
            root = FXMLLoader.load(getClass().getClassLoader().getResource("coinTrack/FXMLLogin.fxml"));
            this.scene = new Scene(root);
            Tab1Controller.mainPage1.setScene(this.scene);
            Tab1Controller.mainPage1.show();
            closeOldStage();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.cri.join();
        this.coinList = this.cri.getCoinList();
        SaveToDisk save = new SaveToDisk();
        save.saveTableAsText(coinList);
    }

    // ========== HELPER METHODS ==========


    /**
     * Reset to default currency.
     */
    private void resetCurrency() {
        this.selectedCurrency = "USD";
        this.cb.setPromptText(this.selectedCurrency);
        this.currencyRate = 1;
        this.tableViewT1.getItems().clear();
        this.tableViewT1.getColumns().clear();
        this.txtAreaT1.setText("");
        displayGlobalStats();
        displayCoinText();
        if(DEBUG){System.out.println("selected currency: " + this.selectedCurrency);}
    }

    /**
     * Display the api data to the screen.
     */
    private void displayCoinText() {
        this.cri = new CoinRankApi();
        this.cri.join();
        this.coinList = this.cri.getCoinList();
        tas.setCoinList(this.coinList);
        /**
         * Update coins in database. probably not necessary to run here.
         * It already runs at launch.
         */
        //updateCoinPricesDB();

        /**
         * Add coins to new database table all_coins.
         * If there is a new coin it will be added here.
         */
        this.cri.updateDatabaseCoins(this.coinList);

        this.count = 50;
        if(DEBUG){System.out.println("number of coins: " + cri.getCoinList().size());}
        this.coinList = this.cri.getCoinList();
        this.coinNamePrice = this.cri.getNamePrice();
        if(DEBUG){System.out.println("current currency: " + this.selectedCurrency);}
        TabAssistantController ast = new TabAssistantController();
        tas.setObjectList(this.cri.getGenericCoinList());

        // Determine which radio button is pressed then display corresponding data.
        if (this.allCoinsBtn.isSelected()) {
            LinkedList<String> colNames = new LinkedList<>();
            // Add single coin param names for column names.
            colNames.add("Symbol");
            colNames.add("Name");
            colNames.add("Price");
            colNames.add("Rank");
            colNames.add("Change");
            colNames.add("Volume");
            ast.coinGenericTable("SingleCoin", this.cri.getTList(), colNames, this.tableViewT1, this.webViewT1, this.currencyRate);
            ast.createCells(this.uname, this.savedCoinsList, this.savedCoins);
        } else if (this.savedCoinsBtn.isSelected()) {
            LinkedList<String> colNamesSaved = new LinkedList<>();
            colNamesSaved.add("Symbol");
            colNamesSaved.add("Name");
            colNamesSaved.add("coinID");
            ast.coinGenericTable("UserCoin", tas.getObjUserCoinList(this.uname), colNamesSaved, this.tableViewT1, this.webViewT1, this.currencyRate);
            ast.createCells(this.uname, this.savedCoinsList, this.savedCoins);
        } else if (this.marketsBtn.isSelected()) {
            LinkedList<String> colMarkets = new LinkedList<>();
            colMarkets.add("rank");
            colMarkets.add("baseSymbol");
            colMarkets.add("quoteSymbol");
            colMarkets.add("sourceName");
            colMarkets.add("tickerClose");
            colMarkets.add("marketShare");
            colMarkets.add("price");
            ast.coinGenericTable("SingleMarket", tas.getObjMarketList(), colMarkets, this.tableViewT1, this.webViewT1, this.currencyRate);
            ast.createCells(this.uname, this.savedCoinsList, this.savedCoins);
        }
    }

    /**
     * Update coin prices in the database.
     */
    private void updateCoinPricesDB() {
        if (this.coinList.isEmpty()) {
            this.cri = new CoinRankApi();
            this.cri.run();
            this.cri.join();
            this.coinList = this.cri.getCoinList();
        }
        
        CoinDatabaseConnection coinConn = new CoinDatabaseConnection();
        coinConn.updateMultipleCoins(this.coinList);
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
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        ListClass lc = new ListClass(this.uname);
        lc.populateOnlineUsers(onlineUsersList);
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        tas.createFriendList(this.friendsList, 0);
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
    private void saveCoin(String _userName, int _coinID) {
        CoinDatabaseConnection coinConn = new CoinDatabaseConnection();
        coinConn.saveCoin(_userName, _coinID);
    }

    /**
     * Pull saved coin data from database and add it to the accordion.
     */
    public void populateSavedCoins() {
        this.tas.populateSavedCoins(savedCoinsList, savedCoins);
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

    /**
     * Initialize the tab
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tas = new TabAssistantController();
        this.uname = coinTrack.FXMLDocumentController.uname;
        this.editBtn = new Menu();
        this.coinList = new LinkedList<>();
        // Set default currency
        this.selectedCurrency = "USD";
        this.currencyRate = 1;
        populateSavedCoins();
        addOnlineUsersToList();
        addFriendsToList();
        updateCoinPricesDB();
    }
}
