/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coinTrack;

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.GlobalCoinStats;
import coinClasses.SingleCoin;
import coinClasses.SingleCoinHistory;
import javafx.scene.image.Image ;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author kms
 */
public class FXMLDocumentController implements Initializable {
    
    private LinkedHashMap<String, String> coinNamePrice;
    private LinkedList<SingleCoin> coinList;
    private int count;
    private Task copyWorker;
    private Image icon;
    private WebEngine webEngine;
    private CoinHistory coinHistory;
    private LinkedList<SingleCoinHistory> history;
    private LinkedHashMap<String, Integer> historyMap;
    private int coinsToGraph = 25;
    private boolean isSingleCoinScan;
    private GlobalCoinStats globalStats;
    private CoinRankApi cri;
    
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    
    // Bottom portion (button bar)
    @FXML private Button scanBtnT1;
    @FXML private Button searchBtnT1;
    @FXML private TextArea txtAreaT1;
    @FXML private WebView webViewT1;
    @FXML private ProgressBar progBarT1;
    @FXML private CheckBox searchSymbolT1;
    @FXML private CheckBox searchNameT1;
    @FXML private SplitMenuButton splitMenuBtn;
    @FXML private MenuItem searchCoins;
    @FXML private MenuItem searchGlobalStats;
    
    // Bottom portion Tab 2
    @FXML private SplitMenuButton splitMenuT2;
    @FXML private Button searchBtnT2;
    @FXML private MenuItem searchAllCoins;
    @FXML private MenuItem searchSingleCoin;
    @FXML private WebView webViewT2;
    @FXML private ProgressBar progBarT2;
    @FXML private CheckBox searchSymbolT2;
    @FXML private CheckBox searchNameT2;
    @FXML private SplitMenuButton numCoins;
    @FXML private MenuItem numCoins5;
    @FXML private MenuItem numCoins10;
    @FXML private MenuItem numCoins25;
    @FXML private MenuItem numCoinsAll;
    
    // Bar Chart
    @FXML private BarChart barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    private XYChart.Series series1;
    private BarChart.Series<String, Number> series2;
    private ObservableList<XYChart.Series<String, Number>> barChartData;
    
    // Table View
    @FXML private TableView<SingleCoin> tableViewT1;
    
    
    //========== Action Handlers ==========
    
    @FXML public void login (ActionEvent event) {
        
        // If statement for testing purposes
        // TODO: add database
        if (usernamePhone.getText().equals("user") && txtPassword.getText().equals("pass")) {
            lblStatus.setText("Login Success");
            
            // After login is successful, you are taken to the main page
            Parent root;
            try { 
                Stage stage = new Stage();
                root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            lblStatus.setText("Login Failed");
        }
    }
    
    
    // Testing this function
    @FXML public void handleSearch(ActionEvent event) {
        txtAreaT1.setText("Searching...");
        if (coinHistory != null) {
            if (searchSymbolT1.isSelected()) {
                System.out.println("search by symbol");

            } else {
                System.out.println("search by name");
                
            }
        } else {
            coinHistory = new CoinHistory();

            if (searchSymbolT1.isSelected()) {
                System.out.println("search by symbol");

            } else {
                System.out.println("search by name");
            }
        }

//        displayCoinText();
    }
    
    @FXML
    private void handleScan(ActionEvent event) {
        System.out.println("Scanning");
        displayCoinText();
//        if (searchGlobalStats.isVisible()){
//            displayGlobalStats();
//        } else {
//            displayCoinText();
//        }
        
    }
    
    @FXML
    private void handleClear(ActionEvent event) {
        System.out.println("hello");
    }
    
    @FXML
    private void handleSymbolSearch(ActionEvent event) {
        searchNameT1.setSelected(false);
        searchSymbolT1.setSelected(true);
    }
    
    @FXML
    private void handleNameSearch(ActionEvent event) {
        searchNameT1.setSelected(true);
        searchSymbolT1.setSelected(false);
    }
    
    @FXML
    private void handleSplitMenu(ActionEvent event) {
        System.out.println("split menu clicked");
    }
    
    @FXML
    private void handleSearchCoins(ActionEvent event) {
        splitMenuBtn.setText("Search Coins");
    }
    
    @FXML
    private void handleGlobalStats(ActionEvent event) {
        splitMenuBtn.setText("Search Globals");
    }
    
    @FXML
    private void handleTest(ActionEvent event) {
        System.out.println("testing");
    }
    
    
    // ========== Tab 2 ==========
    
    
    /**
     * TODO Fix Graphing, not displaying correct
     * number of coins. 
     *  
     */
    
    @FXML
    private void handleScanT2(ActionEvent event) {
        coinHistory = new CoinHistory();
        if (isSingleCoinScan) {
            displaySingleCoinGraph();
        } else {
            displayMultiCoinGraph();
        }
    }
    
    @FXML
    private void handle5Coins(ActionEvent event) {
        coinsToGraph = 5;
        numCoins.setText("5");
    }
    
    @FXML
    private void handle10Coins(ActionEvent event) {
        coinsToGraph = 10;
        numCoins.setText("10");
    }
    
    @FXML
    private void handle25Coins(ActionEvent event) {
        coinsToGraph = 25;
        numCoins.setText("25");
    }
    
    @FXML
    private void handleTotalCoins(ActionEvent event) {
        coinsToGraph = 30;
        numCoins.setText("All");
    }
    
    @FXML
    private void handleAllCoins(ActionEvent event) {
        isSingleCoinScan = false;
        splitMenuT2.setText("All Coins");
    }
    
    @FXML
    private void handleSingleCoin(ActionEvent event) {
        isSingleCoinScan = true;
        splitMenuT2.setText("Single Coin");
    }
    
    @FXML
    private void handleClearT1(ActionEvent event) {
        tableViewT1.getItems().clear();
        txtAreaT1.setText("");
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
        displayMultiCoinView();
        
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
    private void displayMultiCoinView() {
        
        // READ THIS ************
        /**
         * The column data factories for tab2 are
         * commented out. 
         * Need to implement functionality that determines
         * what tab the user is currently viewing.
         */
        TableColumn col1 = new TableColumn("Symbol");
        TableColumn col2 = new TableColumn("Name");
        TableColumn col3 = new TableColumn("Price");
        TableColumn col4 = new TableColumn("Rank");
        TableColumn col5 = new TableColumn("Change");
        TableColumn col6 = new TableColumn("Volume");
        
        col1.setCellValueFactory(new PropertyValueFactory<SingleCoin, String>("symbol"));
        col2.setCellValueFactory(new PropertyValueFactory<SingleCoin, String>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("price"));
        col4.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("rank"));
        col5.setCellValueFactory(new PropertyValueFactory<SingleCoin, Double>("change"));
        col6.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("volume"));
        tableViewT1.getColumns().addAll(col1,col2,col3,col4,col5,col6);
        
        ObservableList<SingleCoin> obvList = FXCollections.observableArrayList(coinList);
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
     * Display historical data for a single coin
     */
    public void displaySingleCoinGraph() {
        System.out.println("displaying graph");
        coinHistory.join();
        historyMap = coinHistory.getPriceDate();
        
        for (Map.Entry<String, Integer> entry : historyMap.entrySet()) {
            series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        barChartData.add(series1);
        barChart.setData(barChartData);
    }
    
    /**
     * Display / Rank all coin prices
     */
    public void displayMultiCoinGraph() {
        System.out.println("displaying graph");
        coinHistory.join();
        historyMap = coinHistory.getPriceDate();
        int count = 0;
        for (Map.Entry<String, Integer> entry : historyMap.entrySet()) {
//            count++;
//            if (coinsToGraph < 25 && count < coinsToGraph) {
//                break;
//            }
            series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        barChartData.add(series1);
        barChart.setData(barChartData);
    }

    // This is probably temporary.
    // Attempting to fix progress bar thread.
    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                    System.out.println("count: " + count);
                for (int i = 0; i < (count/2); i++) {
                    Thread.sleep(1000);
//                    updateMessage("2000 milliseconds");
                    updateProgress(i + 1, count/2);
                    System.out.println(i);
                }
                return true;
            }
        };
    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
     
        barChartData = FXCollections.observableArrayList();
        series1 = new BarChart.Series<String, Number>();
        series1.setName("Data");

    }

}
