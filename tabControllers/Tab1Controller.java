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
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;



/**
 *
 * @author Kyle
 */
public class Tab1Controller implements Initializable{
    
    private LinkedHashMap<String, String> coinNamePrice;
    private LinkedList<SingleCoin> coinList;
    private int count;
    private Task copyWorker;
    private Image icon;
    private WebEngine webEngine;
    private CoinHistory coinHistory;
    private LinkedList<SingleCoinHistory> history;
    private LinkedHashMap<String, Integer> historyMap;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private int coinsToGraph = 25;
    private boolean isSingleCoinScan;
    private GlobalCoinStats globalStats;
    private CoinRankApi cri;
    private static Stage mainPage;
    private String uname;
    private LinkedList<String> onlineUsers;
    
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    
    // Accordion
    @FXML private ListView onlineUsersList;
    @FXML private ListView savedCoinsList;
    @FXML private ListView friendsList;
    
    // Bottom portion (button bar)
    @FXML private TextArea txtAreaT1;
    @FXML private WebView webViewT1;
    @FXML private CheckBox searchCoins;
    @FXML private CheckBox searchGlobalStats;
    @FXML private Text messageText;
    
    // Table View
    @FXML private TableView<SingleCoin> tableViewT1;
    
    
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
        ConnectToDatabase dbConn;
        
        LinkedList<SingleCoin> temp = cri.getCoinList();
        for (int i = 0; i < temp.size(); i++) {
            SingleCoin cn = temp.get(i);
            String uuid = cn.getUuid();
            String symbol = cn.getSymbol();
            String name = cn.getName();
            String price = cn.getPrice();
            long millis=System.currentTimeMillis();
            Date date = new Date(millis);
            /**
             * Testing database
             */
//            dbConn = new ConnectToDatabase();
//            dbConn.coinDatabase(uuid, symbol, name, price, date);
        }
        count = 50;
        System.out.println(cri.getLimit());
        coinNamePrice = cri.getNamePrice();
        coinList = cri.getCoinList();
        displayMultiCoinTable();
        
    }
    
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
     * Display coin data to the table
     */
    private void displayMultiCoinTable() {
        // Create columns
        TableColumn col1 = new TableColumn("Symbol");
        TableColumn col2 = new TableColumn("Name");
        TableColumn col3 = new TableColumn("Price (USD)");
        TableColumn col4 = new TableColumn("Rank");
        TableColumn col5 = new TableColumn("Change");
        TableColumn col6 = new TableColumn("Volume");
        // Link columns to properties in SingleCoin class
        col1.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<>("price"));
        col4.setCellValueFactory(new PropertyValueFactory<>("rank"));
        col5.setCellValueFactory(new PropertyValueFactory<>("stringChange"));
        col6.setCellValueFactory(new PropertyValueFactory<>("volume"));
        // Add columns to tableView
        tableViewT1.getColumns().addAll(col1,col2,col3,col4,col5,col6);
        ObservableList<SingleCoin> obvList = FXCollections.observableArrayList(coinList);
        // Change text color of "change" column if positive or negative change.
        col5.setCellFactory(new Callback<TableColumn, TableCell>() {
        public TableCell call(TableColumn param) {
            return new TableCell<SingleCoin, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!isEmpty()) {
                        this.setStyle("-fx-text-fill: green");
                        // Get fancy and change color based on data
                        if(item.contains("-")) 
                            this.setStyle("-fx-text-fill: red");
                        setText(item);
                    }
                }
            };
        }
    });
        tableViewT1.setItems(obvList);
        tableViewT1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Allows user to double click a table row and display info in textArea
        tableViewT1.setRowFactory(tv -> {
            TableRow<SingleCoin> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        SingleCoin rowData = row.getItem();
                        System.out.println(rowData);
//                        txtAreaT1.setText(rowData.getIconUrl());
                        String imgPath = rowData.getIconUrl();

                        // Attempting to resize the coin logo image.
                        webViewT1.setPrefHeight(56);
                        webViewT1.setPrefWidth(56);
                        webViewT1.getEngine().load(imgPath);
                    }
                }
            });
            return row;
        });
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
    
    /**
     * Log out user and change to login stage.
     * Also update the users isOnline status in the database.
     * @param event 
     */
    @FXML
    private void handleLogOutT1(ActionEvent event) {
        System.out.println("logging out");
        Parent root;
            try {
                closeOldStage();
                // Update users online status in database
                setOnlineStatus(coinTrack.FXMLDocumentController.uname, 0);
                this.mainPage = new Stage();
                root = FXMLLoader.load(getClass().getClassLoader().getResource("coinTrack/FXMLLogin.fxml"));
                this.scene = new Scene(root);
                this.mainPage.setScene(this.scene);
                this.mainPage.show();
//                closeOldStage();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    /**
     * Change a users online status. i.e. when they log off .
     * @param _uname
     * @param _status 
     */
    private void setOnlineStatus(String _uname, int _status) {
        ConnectToDatabase conn = new ConnectToDatabase();
        conn.setUserOnlineStatus(_uname, _status);
        conn.close();
    }
    
    private void addOnlineUsersToList() {
        ConnectToDatabase conn = new ConnectToDatabase();
        this.onlineUsers = conn.getOnlineUsers();
        conn.close();
        for (int i = 0; i < this.onlineUsers.size(); i++) {
            onlineUsersList.getItems().add(this.onlineUsers.get(i));
        }
    }
    
    /**
     * A probably bad method of closing old screens when a new one opens
     */
    private void closeOldStage() {
        coinTrack.FXMLDocumentController.getCurrentStage().close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        this.uname = coinTrack.FXMLDocumentController.uname;
        messageText.setText("Hello " + uname);
        
        addOnlineUsersToList();
        
    }
    
}
