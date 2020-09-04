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
import java.util.Date;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.StringUtils;

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
    @FXML private Button scanBtnT1;
    @FXML private Button searchBtnT1;
    @FXML private TextArea txtAreaT1;
    @FXML private WebView webViewT1;
    @FXML private ProgressBar progBarT1;
    @FXML private CheckBox searchSymbolT1;
    @FXML private CheckBox searchNameT1;
    @FXML private SplitMenuButton splitMenuBtn;
    @FXML private CheckBox searchCoins;
    @FXML private CheckBox searchGlobalStats;
    
    // Bottom portion Tab 2
    @FXML private SplitMenuButton splitMenuT2;
    @FXML private Button searchBtnT2;
    @FXML private MenuItem searchAllCoins;
    @FXML private MenuItem searchSingleCoin;
    @FXML private WebView webViewT2;
    @FXML private ProgressBar progBarT2;
    @FXML private CheckBox searchSymbolT2;
    @FXML private CheckBox searchNameT2;
    @FXML private TextField searchFieldT2;
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
    private XYChart.Series series4;
    private ObservableList<XYChart.Series<String, Number>> barChartData;
    private ObservableList<XYChart.Series<String, Number>> barChartData2;
    
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
        tableViewT1.getItems().clear();
//        displayCoinText();
        if (searchGlobalStats.isArmed()){
            displayGlobalStats();
        } else {
            displayCoinText();
        }
        
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
        searchGlobalStats.setSelected(false);
        searchCoins.setSelected(true);
    }
    
    @FXML
    private void handleGlobalStats(ActionEvent event) {
        splitMenuBtn.setText("Search Globals");
        searchGlobalStats.setSelected(true);
        searchCoins.setSelected(false);
    }
    
    @FXML
    private void handleClearT1(ActionEvent event) {
        tableViewT1.getItems().clear();
        txtAreaT1.setText("");
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
        barChart.getData().clear();
        barChart.layout();
        coinHistory = new CoinHistory();
        if (isSingleCoinScan) {
            System.out.println("Sorry, havn't added this yet");
            
        } else {
            displayMultiCoinGraph();
        }
    }
    
    @FXML
    private void handleSearchT2(ActionEvent event) {
        barChart.getData().clear();
        barChart.layout();
        if (coinHistory == null) {
            displaySingleCoinGraph();
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
    private void handleClearT2(ActionEvent event) {
        barChart.getData().clear();
        barChart.layout();
        searchFieldT2.setText("");
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
         * Need to implement functionality that determines
         * what tab the user is currently viewing.
         */
        TableColumn col1 = new TableColumn("Symbol");
        TableColumn col2 = new TableColumn("Name");
        TableColumn col3 = new TableColumn("Price");
        TableColumn col4 = new TableColumn("Rank");
        TableColumn col5 = new TableColumn("Change");
        TableColumn col6 = new TableColumn("Volume");
        
        col1.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<>("price"));
        col4.setCellValueFactory(new PropertyValueFactory<>("rank"));
        col5.setCellValueFactory(new PropertyValueFactory<>("change"));
        col6.setCellValueFactory(new PropertyValueFactory<>("volume"));
        
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
        // Get the string/int from the text field.
        if (searchFieldT2.getText().isEmpty()){
            searchFieldT2.setText("Coin name or id");
        } else if (Character.isDigit(searchFieldT2.getText().charAt(0))) {
            // Create a CoinHistory object and pass it the id.
            CoinHistory coinHist = new CoinHistory(Integer.parseInt(
                    searchFieldT2.getText()));
            // Save the data to a HashMap variable
            singleHistoryMap = coinHist.getSingleHistory();
        } else {
            // TODO:
            // Here we should add the ability to check for a name entry
            searchFieldT2.setText("no string compatability yet");
        }
        // Add entries from singleHistoryMap into series1
        for (Map.Entry<Double, String> entry : singleHistoryMap.entrySet()) {
            series4.getData().add(new XYChart.Data(
                    entry.getValue(), entry.getKey()));
        }
        // Add series1 to the barChartData, then add that to the barChart
        barChartData2.add(series4);
        barChart.setData(barChartData2);
        
        /**
         * THIS IS FOR TESTING.
         * THIS IS NOT MY CODE.
         * 
         * Implements scrolling using the mouse wheel on the graph.
         */
        final double SCALE_DELTA = 1.1;
        barChart.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

                barChart.setScaleX(barChart.getScaleX() * scaleFactor);
                barChart.setScaleY(barChart.getScaleY() * scaleFactor);
            }
        });

        barChart.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    barChart.setScaleX(1.0);
                    barChart.setScaleY(1.0);
                }
            }
        });
        
    }
    
    /**
     * Display / Rank all coin prices
     */
    public void displayMultiCoinGraph() {
        System.out.println("displaying graph");
//        coinHistory.join();
        historyMap = coinHistory.getPriceDate();
        int count = 0;
        for (Map.Entry<String, Integer> entry : historyMap.entrySet()) {
//            count++;
//            if (coinsToGraph < 25 && count < coinsToGraph) {
//                break;
//            }
            series1.getData().add(new XYChart.Data(
                    entry.getKey(), entry.getValue()));
        }
        barChartData.add(series1);
        barChart.setData(barChartData);
    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
     
        barChartData = FXCollections.observableArrayList();
        barChartData2 = FXCollections.observableArrayList();
        series1 = new BarChart.Series<>();
        series4 = new BarChart.Series<>();
        series1.setName("Data");
        series4.setName("Prices");

    }

}
