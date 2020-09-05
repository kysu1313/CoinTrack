/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers;

import coinClasses.CoinHistory;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Kyle
 */
public class Tab2Controller implements Initializable{
    private CoinHistory coinHistory;
    private LinkedHashMap<String, Integer> historyMap;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private int coinsToGraph = 25;
    private boolean isSingleCoinScan;
    
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private TabPane graphTabPane;
    @FXML private Tab barChartTab;
    @FXML private Tab pieChartTab;
    
    // Bottom portion Tab 2
    @FXML private SplitMenuButton splitMenuT2;
    @FXML private TextField searchFieldT2;
    @FXML private SplitMenuButton numCoins;
    
    // Bar Chart
    @FXML private BarChart barChart;
    private XYChart.Series series1;
    private XYChart.Series series4;
    private ObservableList<XYChart.Series<String, Number>> barChartData;
    private ObservableList<XYChart.Series<String, Number>> barChartData2;
    
    
    // ========== Tab 2 ==========
    
    
    /**
     * TODO Fix Graphing, not displaying correct
     * number of coins. 
     *  
     */
    
    @FXML
    private void handleScanT2(ActionEvent event) {
        if (graphTabPane.getSelectionModel().getSelectedItem() == barChartTab) {
            System.out.println("bar chart selected");
            barChart.getData().clear();
            barChart.layout();
            coinHistory = new CoinHistory();
            if (isSingleCoinScan) {
                System.out.println("Sorry, havn't added this yet");

            } else {
                displayMultiCoinGraph();
            }
        } else if (graphTabPane.getSelectionModel().getSelectedItem() == pieChartTab) {
            System.out.println("pie chart selected");
        } else {
            System.out.println("bubble chart selected");
        }

    }
    
    @FXML
    private void handleSearchT2(ActionEvent event) {
        if (graphTabPane.getSelectionModel().getSelectedItem() == barChartTab) {
            System.out.println("bar chart selected");
            barChart.getData().clear();
            barChart.layout();
            if (coinHistory == null) {
                displaySingleCoinGraph();
            }
        } else if (graphTabPane.getSelectionModel().getSelectedItem() == pieChartTab) {
            System.out.println("pie chart selected");
        } else {
            System.out.println("bubble chart selected");
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
                    searchFieldT2.getText()), "");
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
    public void initialize(URL location, ResourceBundle resources) {
        barChartData = FXCollections.observableArrayList();
        barChartData2 = FXCollections.observableArrayList();
        series1 = new BarChart.Series<>();
        series4 = new BarChart.Series<>();
        series1.setName("Data");
        series4.setName("Prices");
        
        final int initialVal = 25;
        SpinnerValueFactory<Integer> valFact = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 100, initialVal);
//        spinnerT2.setValueFactory(valFact);
    }
    
}
