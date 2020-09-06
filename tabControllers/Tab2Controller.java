/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers;

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ParseCoinName;
import coinClasses.SingleCoin;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.T;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.StringConverter;

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
    private CoinRankApi coinList;
    private final ObservableList<String> TIMES = FXCollections.
            observableArrayList("24h", "7d", "30d", "1y", "5y");
    private String timeSelection;
    private int tabSelection;
    
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
    @FXML private TextArea txtAreaT2;
    @FXML private Spinner<String> spinnerT2;
    @FXML private ComboBox comboBox;
    
    // Bar Chart
    @FXML private BarChart barChart;
    private XYChart.Series series1;
    private XYChart.Series series4;
    private ObservableList<XYChart.Series<String, Number>> barChartData;
    private ObservableList<XYChart.Series<String, Number>> barChartData2;
    
    // Pie Chart
    @FXML
    protected PieChart pieChart;
    protected ObservableList<PieChart.Data> pieChartData;
    
    
    @FXML
    private void handleScanT2(ActionEvent event) {
        if (graphTabPane.getSelectionModel().getSelectedItem() == barChartTab) {
            System.out.println("bar chart selected");
            barChart.getData().clear();
            barChart.layout();
            // timeSelection
            if (comboBox.getValue() != "Timeframe") {
                timeSelection = (String)comboBox.getValue();
            } else {
                timeSelection = "7d";
            }
            coinHistory = new CoinHistory();
            if (isSingleCoinScan) {
                System.out.println("Sorry, havn't added this yet");

            } else {
                displayMultiCoinGraph();
            }
        } else if (graphTabPane.getSelectionModel().getSelectedItem() == pieChartTab) {
            System.out.println("pie chart selected");
            coinList = new CoinRankApi();
            coinList.run();
            displayPieChart();
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
            // timeSelection
            if (comboBox.getValue() != "Timeframe") {
                timeSelection = (String)comboBox.getValue();
            } else {
                timeSelection = "7d";
            }
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
    
    @FXML
    private void handleTest(ActionEvent event) {
        File coinFile = new File("src\\coinClasses\\coinNamesIds.txt");
        System.out.println(coinFile.toString());
        try {
            Scanner scanner = new Scanner(coinFile);
            String lines = "";
            // Scan the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                lines += line + "\n";
                
            }
            txtAreaT2.setText(lines);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseCoinName.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleComboBox(ActionEvent event) {
        String[] times = {"24h", "7d", "30d", "1y", "5y"};
        
        comboBox = new ComboBox(FXCollections.observableArrayList(times));
    }
    
    /**
     * Display historical data for a single coin
     */
    private void displaySingleCoinGraph() {
        System.out.println("displaying graph");
        // Get the string/int from the text field.
        if (searchFieldT2.getText().isEmpty()){
            searchFieldT2.setText("Coin name or id");
        } else {
            char ch = searchFieldT2.getText().charAt(0);
            if (Character.isAlphabetic(ch)){
                CoinHistory coinHist = new CoinHistory(0, searchFieldT2.getText(), timeSelection);
                singleHistoryMap = coinHist.getSingleHistory();
            } else {
                int temp = Integer.parseInt(searchFieldT2.getText());
                CoinHistory coinHist = new CoinHistory(temp, "", timeSelection);
                singleHistoryMap = coinHist.getSingleHistory();
            }
        }
        // Add entries from singleHistoryMap into series1
        for (Map.Entry<Double, String> entry : singleHistoryMap.entrySet()) {
            series4.getData().add(new XYChart.Data(
                    entry.getValue(), entry.getKey()));
        }
        // Add series1 to the barChartData, then add that to the barChart
        barChart.setTitle("Viewing the past " + timeSelection);
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
                // Keep the scale ratio as you zoom in/out
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
    private void displayMultiCoinGraph() {
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
    
    private void displayPieChart() {
        coinList.join();
        LinkedList<SingleCoin> temp = coinList.getCoinList();
        // temp.size()
        for (int i = 0; i < 10; i++) {
            SingleCoin coin = temp.get(i);
            DecimalFormat df = new DecimalFormat("#.#####");
            double price = Double.parseDouble(coin.getPrice());
            double rounded = (double)Math.round(price * 100000d) / 100000d;
            pieChartData.add(new PieChart.Data(coin.getName(), rounded));
        }
        pieChart.setData(pieChartData);
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        barChartData = FXCollections.observableArrayList();
        barChartData2 = FXCollections.observableArrayList();
        pieChartData = FXCollections.observableArrayList();
        pieChart.setTitle("Top 10 coins");
        series1 = new BarChart.Series<>();
        series4 = new BarChart.Series<>();
        series1.setName("Data");
        series4.setName("Prices");
//        
        comboBox.setValue("Timeframe");
        comboBox.setItems(TIMES);
        
        
    }
    
}
