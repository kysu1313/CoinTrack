/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers;

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.GlobalCoinStats;
import coinClasses.SingleCoin;
import coinClasses.SingleCoinHistory;
import java.awt.Color;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
    
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    
    // Bottom portion (button bar)
    @FXML private TextArea txtAreaT1;
    @FXML private WebView webViewT1;
    @FXML private CheckBox searchCoins;
    @FXML private CheckBox searchGlobalStats;
    
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
        count = 50;
        System.out.println(cri.getLimit());
        
        // TODO: fix progress bar...
        
//        progBarT1.setProgress(0.0);
//        progBarT1.progressProperty().unbind();
//        progBarT1.progressProperty().bind(copyWorker.progressProperty());
//        new Thread(copyWorker).start();
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
    
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}