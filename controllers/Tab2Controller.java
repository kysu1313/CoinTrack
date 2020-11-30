package controllers;

/**
 * Tab 2 document controller.
 * Handles actions on inputs. Main functions
 * are displaying graphs, searches, accordion.
 */

import coinTrack.FXMLDocumentController;
import controllers.assistantControllers.TabAssistantController;
import models.CoinHistory;
import models.CoinRankApi;
import models.UserCoin;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.User;
import models.viewModels.BarChartClass;
import models.viewModels.LineChartClass;

/**
 *
 * @author Kyle
 */
public class Tab2Controller implements Initializable{

    private final User USER = FXMLDocumentController.user;
    private CoinHistory coinHistory;
    private LinkedHashMap<String, Integer> historyMap;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private int coinsToGraph = 25;
    private CoinRankApi coinList;
    private String timeSelection;
    private int tabSelection;
    private int pieChartCoins;
    private final ObservableList<String> TIMES = FXCollections.
            observableArrayList("24h", "7d", "30d", "1y", "5y");
    private final ObservableList<String> NUMCOINS = FXCollections.
            observableArrayList("3", "5", "7", "15", "25", "35", "50");
    private final ObservableList<String> ADDREMOVE = FXCollections.
            observableArrayList("add", "remove");
    private final String DEFAULT_TIME_SELECTION = "7d";
    private final int COIN_ID_LOCATION = 0;
    private final int TAB1 = 1;
    private final int TAB2 = 2;
    private final int TAB3 = 3;
    private final int TAB4 = 4;
//    private static Stage mainPage2;
    private String uname;
    private LinkedList<String> onlineUsers;
    private LinkedList<String> friendList;
    private LinkedList<UserCoin> savedCoins;
    private LinkedList<String> linesToGraph;
    private LinkedList<XYChart.Data<String, Number>> dataList;
    private Tab currTab;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    private LineChartClass lcc;
    private TabAssistantController tas;
    ContextMenu cm3;
    protected Scene scene;
    private LinkedHashMap<Double, String> userHistoryMap;

    // General FXML properties
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private TabPane graphTabPane;
    @FXML private Tab barChartTab;
    @FXML private Tab pieChartTab;
    @FXML private Tab lineChartTab;
//    @FXML private Tab webTab;
    @FXML private Tab candleTab;
    @FXML private ComboBox addRemoveComboBox;
    @FXML public static Label coordsLabel;
    @FXML private VBox sideVBox;

    // Accordion
    @FXML private ListView onlineUsersListT2;
    @FXML private ListView savedCoinsListT2;
    @FXML private ListView friendsListT2;
    @FXML private ContextMenu cm;

    // Bottom portion Tab 2
    @FXML private TextField searchFieldT2;
    @FXML private TextArea txtAreaT2;
    @FXML private ComboBox<String> comboBox;
    @FXML private Button scanBtnT2;
    @FXML private Button searchBtnT2;
    @FXML private Pane candlePane;

    // Bar Chart
    @FXML private BarChart barChart;
    private XYChart.Series series1;
    private XYChart.Series series4;
    private XYChart.Series series2;
    private ObservableList<XYChart.Series<String, Number>> barChartData;
    private ObservableList<XYChart.Series<String, Number>> barChartData2;

    // Line Chart
    @FXML private LineChart lineChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private ObservableList<XYChart.Series<Number, String>> lineChartData;

    // Pie Chart
    @FXML
    protected PieChart pieChart;
    protected ObservableList<PieChart.Data> pieChartData;

    /**
     * This handles the scan button on tab 2.
     * It first determines which graphTab is selected
     * then clears the existing data if any.
     * @param event
     */
    @FXML
    private void handleScanT2(ActionEvent _event) {
        if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.barChartTab) {
            if (this.DEBUG){System.out.println("bar chart selected");}
            this.barChart.getData().clear();
            this.barChart.layout();
            // Get the comboBox entry
            boolean isComboBoxEmpty = this.comboBox.getSelectionModel().isEmpty();
            if (!isComboBoxEmpty) {
                this.timeSelection = (String)comboBox.getValue();
            } else {
                this.timeSelection = "7d";
            }
            // Display the graph
            displayMultiCoinGraph();

        } else if (graphTabPane.getSelectionModel().getSelectedItem() == pieChartTab) {
            if (DEBUG){System.out.println("pie chart selected");}
            pieChart.getData().clear();
            pieChart.layout();
            // Display the pie chart
            displayPieChart();
        } else {
            if (DEBUG){System.out.println("bubble chart selected");}
        }

    }

    /**
     * Handles the search button on tab 2.
     * This button is not applicable on the pieChartTab
     * @param event
     */
    @FXML
    private void handleSearchT2(ActionEvent _event) {
        if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.barChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            this.barChart.getData().clear();
            this.barChart.layout();
            this.barChartData.clear();
            this.series1.getData().clear();
            this.barChartData2.clear();
            this.series4.getData().clear();
            // timeSelection
            if (this.comboBox.getSelectionModel().isEmpty()) {
                // Default timeframe will be 7 days.
                this.timeSelection = this.DEFAULT_TIME_SELECTION;
            } else {
                this.timeSelection = (String)this.comboBox.getValue();
            }
            // Call method to search for coin and display its graph.
            displaySingleCoinGraph();
        } else if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.lineChartTab) {
            if (DEBUG){System.out.println("line chart selected");}
            boolean isComboBoxEmpty = this.addRemoveComboBox.getSelectionModel().isEmpty();
            boolean isTimeBoxEmpty = this.comboBox.getSelectionModel().isEmpty();
            String arSelection = (String)this.addRemoveComboBox.getValue();
            String warning = "";
            if (isComboBoxEmpty) {
                warning += "Select 'add' or 'remove' from the drop down menu.\n";
                this.txtAreaT2.setText(warning);
            } else if (arSelection.equalsIgnoreCase("add")){
                if (this.searchFieldT2.getText().isEmpty()) {
                    warning += "Enter the name of a coin to add.\n";
                    this.txtAreaT2.setText(warning);
                } else {
                    this.linesToGraph.add(this.searchFieldT2.getText());
                    displayLineGraph();
                }
            } else {
                this.lcc.removeCoin(this.searchFieldT2.getText());
                this.lcc.displayGraph();
                this.sideVBox.getChildren().forEach(item -> {
                    if (((Label)item).getText().equalsIgnoreCase(this.searchFieldT2.getText())){
                        this.sideVBox.getChildren().remove(item);
                    }
                });
            }
        } else {
            if (DEBUG){System.out.println("pie chart selected");}
        }
    }

    /**
     * Clears data from all graphs.
     *
     * This DOES NOT work 100%. New graphs spawn on top of old graphs.
     *
     * @param event
     */
    @FXML
    private void handleClearT2(ActionEvent _event) {
        this.barChart.getData().clear();
        this.barChart.layout();
        this.pieChart.getData().clear();
        this.pieChart.layout();
        this.searchFieldT2.setText("");
        this.barChartData2.clear();
        this.series4.getData().clear();
        this.dataList.clear();
        this.sideVBox.getChildren().clear();
        if (currTab == lineChartTab) {
            this.lineChart.getData().clear();
        }
    }

    /**
     * Changes depending on what graph tab is open.
     * @param event
     */
    @FXML
    private void handleComboBox(ActionEvent _event) {
        this.timeSelection = this.comboBox.getValue();
    }

    /**
     * Display historical data for a single coin
     */
    private void displaySingleCoinGraph() {
        if (DEBUG){System.out.println("displaying graph");}
        String currCoin = "";
        // Get the string/int from the text field.
        if (this.searchFieldT2.getText().isEmpty()){
            AlertMessages.showInformationMessage("Coin Name",
                            "You must enter the name, symbol, or id"
                            + "of the coin you would like to view.");
        } else {
            currCoin = this.searchFieldT2.getText();
            char ch = currCoin.charAt(0);
            // Create CoinHistory object from given input.
            if (Character.isAlphabetic(ch)){
                CoinHistory chist = new CoinHistory(0, currCoin, this.timeSelection);
                if (chist.checkValidData()){
                    chist.getData();
                    this.singleHistoryMap = chist.getSingleHistory();
                }
            } else {
                int temp = Integer.parseInt(currCoin);
                CoinHistory chist = new CoinHistory(temp, "", this.timeSelection);
                if (chist.checkValidData()){
                    chist.getData();
                    this.singleHistoryMap = chist.getSingleHistory();
                }
            }
        }
        // Create a new BarChart object passing appropriate values.
        BarChartClass bcc = new BarChartClass(this.singleHistoryMap,
                                              this.timeSelection, this.barChart, this.txtAreaT2);
        bcc.displaySingleGraph();
        bcc.alternateColors(this.barChart, "green", "red");
    }

    /**
     * Display coin historical prices in a line chart.
     */
    private void displayLineGraph() {
        System.out.println("displaying line graph");
        // Get the string/int from the text field.
        if (this.searchFieldT2.getText().isEmpty()){
            AlertMessages.showInformationMessage("Coin Name",
                            "You must enter the name, symbol, or id"
                            + "of the coin you would like to view.");
        } else {
            char ch = this.searchFieldT2.getText().charAt(0);
            // Verify data is valid
            if (Character.isAlphabetic(ch)){
                CoinHistory coinHist = new CoinHistory(this.COIN_ID_LOCATION, this.searchFieldT2.getText(), this.timeSelection);
                if (coinHist.checkValidData()){
                    coinHist.getData();
                    this.singleHistoryMap = coinHist.getSingleHistory();
                }
            } else {
                int temp = Integer.parseInt(this.searchFieldT2.getText());
                CoinHistory coinHist = new CoinHistory(temp, "", this.timeSelection);
                if (coinHist.checkValidData()){
                    coinHist.getData();
                    this.singleHistoryMap = coinHist.getSingleHistory();
                }
            }
        }
        this.sideVBox.getChildren().clear();
        if (this.timeSelection == null){
            this.timeSelection = "24h";
        }
        // Create line chart object to display graphs.
        this.lcc = new LineChartClass(this.lineChart, this.linesToGraph, this.timeSelection);
        this.lcc.displayGraph();
        this.lcc.getElements().forEach((item) -> {
            Label lbl = new Label(item);
            this.sideVBox.getChildren().add(lbl);
            lbl.setMaxWidth(Double.MAX_VALUE);
            addListListener(lbl);
        });
    }

    /**
     * Display / Rank all coin prices
     */
    private void displayMultiCoinGraph() {
        if (DEBUG){System.out.println("displaying graph");}

        this.historyMap = this.USER.getCoinHistoryList();

        int count = 0;
        this.historyMap.entrySet().forEach((entry) -> {
            this.series1.getData().add(new XYChart.Data(
                    entry.getKey(), entry.getValue()));
        });
        this.barChartData.add(this.series1);
        this.barChart.setData(this.barChartData);
    }

    /**
     * Save coin using coinDatabaseConnection class which calls
     * the actual database class.
     * @param _userName
     * @param _coinID
     */
    private void saveCoin(int _coinID) {
        this.USER.saveCoin(_coinID);
    }

    /**
     * Populate saved coin side area.
     */
    private void populateSavedCoins() {
        this.tas.populateSavedCoins(this.savedCoinsListT2, this.savedCoins);
    }

    /**
     * Creates a LinkedList of SingleCoins.
     *
     * This method has been moved to Tab2AssistantController
     * in an effort to keep this controller as clean as possible.
     */
    private void displayPieChart() {
        this.tas.MakePieChart(this.USER.getSortedCoinList(), this.pieChartCoins, this.comboBox, this.pieChartData, this.pieChart);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        this.tas.addOnlineUsers(this.onlineUsers, this.onlineUsersListT2);
    }

    /**
     * This creates the right click menu on the onlineUsers list.
     * It also maps each button to an action.
     */
    private void createListCells() {
        this.tas.listCells(this.onlineUsersListT2, this.uname);
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        this.tas.friendList(this.friendList, this.uname, this.friendsListT2);
    }

    /**
     * Creates the candlestick chart.
     * @throws ParseException
     */
    private void displayCandleChart() throws ParseException {
        this.tas.candleChart(this.candlePane);
    }

    /**
     * Returns and closes the main stage from the class where it was created
     */
    private void closeOldStage() {
        coinTrack.FXMLDocumentController.mainStage.close();
    }

    /**
     * Detect tab changed within Tab 2.
     */
    private void tabChanges() {
        // Tab listener. Detects which graph tab is selected
        graphTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab currentTab) {
                if (DEBUG){System.out.println("Tab Change");}
                // Unable to use 'this' keyword here due to lambda expression.
                if (currentTab == barChartTab) {
                    currTab = barChartTab;
                    comboBox.setValue("Timeframe");
                    tabSelection = TAB1;
                    scanBtnT2.setDisable(true);
                    searchBtnT2.setDisable(false);
                    addRemoveComboBox.setVisible(false);
                    comboBox.setItems(TIMES);
                } else if (currentTab == pieChartTab) {
                    currTab = pieChartTab;
                    comboBox.setValue("Number of coins");
                    tabSelection = TAB2;
                    searchBtnT2.setDisable(true);
                    scanBtnT2.setDisable(false);
                    addRemoveComboBox.setVisible(false);
                    comboBox.setItems(NUMCOINS);
                } else if (currentTab == lineChartTab) {
                    currTab = lineChartTab;
                    comboBox.setValue("Timeframe");
                    tabSelection = TAB3;
                    searchBtnT2.setDisable(false);
                    scanBtnT2.setDisable(true);
                    addRemoveComboBox.setVisible(true);
                    comboBox.setItems(TIMES);
                    addRemoveComboBox.setValue("Add / Remove");
                    lineChart.setAnimated(false);
                } else if (currentTab == candleTab) {
                    currTab = candleTab;
                    comboBox.setValue("Timeframe");
                    tabSelection = TAB4;
                    searchBtnT2.setDisable(false);
                    scanBtnT2.setDisable(true);
                    addRemoveComboBox.setVisible(true);
                    comboBox.setItems(TIMES);
                    addRemoveComboBox.setVisible(false);
                    lineChart.setAnimated(false);
                    try {
                        displayCandleChart();
                    } catch (ParseException ex) {
                        Logger.getLogger(Tab2Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    /**
     * Refresh lists of saved coins.
     * @param event
     */
    @FXML
    private void refresh(ActionEvent event) {
        populateSavedCoins();
        addContextMenuToList();
    }

    /** Save coin graph in graph tab**/

    /**
     * Add right click menu to saved coins list on left side of window.
     */
    private void addContextMenuToList() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem graphCoin = new MenuItem("Graph Coin");
        graphCoin.setOnAction(event -> createGraphsForSavedCoins());
        contextMenu.getItems().addAll(graphCoin);
        savedCoinsListT2.setContextMenu(contextMenu);
    }

    /**
     * Create graph for selected saved graph.
     */
    private void createGraphsForSavedCoins() {
        if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.barChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            this.barChart.getData().clear();
            this.series2 = new BarChart.Series<>();
            if (this.timeSelection == null){
                this.timeSelection = "24h";
            }
            UserCoin item = (UserCoin)savedCoinsListT2.getSelectionModel().getSelectedItem();
            CoinHistory ch = new CoinHistory(item.getCoinID(), item.getName(), this.timeSelection);
            // Verify the coin actually has data to display for given timeframe
            if (ch.checkValidData()) {
                ch.getData();
                this.userHistoryMap = ch.getSingleHistory();
                // Create new bar chart object
                BarChartClass bcc = new BarChartClass(this.userHistoryMap, this.timeSelection, this.barChart, this.txtAreaT2);
                bcc.displaySingleGraph();
                bcc.alternateColors(this.barChart, "green", "red");
            } else {
                this.userHistoryMap = new LinkedHashMap<>();
            }
        } else if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.lineChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            this.lineChart.getData().clear();
            this.series2 = new BarChart.Series<>();
            if (this.timeSelection == null){
                this.timeSelection = "24h";
            }
            UserCoin item = (UserCoin)savedCoinsListT2.getSelectionModel().getSelectedItem();
            // Create new bar chart object
            this.sideVBox.getChildren().clear();
            this.linesToGraph.add(item.getName());
            // Create line chart object to display graphs.
            this.lcc = new LineChartClass(this.lineChart, this.linesToGraph, this.timeSelection);
            this.lcc.displayGraph();
            this.lcc.getElements().forEach((coin) -> {
                Label lbl = new Label(coin);
                this.sideVBox.getChildren().add(lbl);
                addListListener(lbl);
                lbl.setMaxWidth(Double.MAX_VALUE);
            });
            lcc.displayGraph();
        }
    }

    /**
     * Adds mouse handler for search VBox labels.
     *
     * @param _lbl
     */
    private void addListListener(Label _lbl) {
        _lbl.setOnMouseEntered((MouseEvent mouseEvent) -> {
            _lbl.setStyle("-fx-background-color: #bababa;");
        });
        _lbl.setOnMouseExited((MouseEvent mouseEvent) -> {
            _lbl.setStyle("-fx-background-color: #323232;");
        });
        ContextMenu cm = new ContextMenu();
        MenuItem m1 = new MenuItem("Remove");
        m1.setOnAction((event) -> {
            if (DEBUG) {System.out.println("Choice 1 clicked!");}
            this.lcc.removeCoin(_lbl.getText());
            this.sideVBox.getChildren().remove(_lbl);
        });
        cm.getItems().addAll(m1);
        _lbl.setContextMenu(cm);
    }


    @Override
    public void initialize(URL location, ResourceBundle _resources) {
        this.tas = new TabAssistantController();
        // Save uname from login page.
        String uname = coinTrack.FXMLDocumentController.uname;
        this.uname = coinTrack.FXMLDocumentController.uname;
        System.out.println(DEBUG);
        linesToGraph = new LinkedList<>();
        coordsLabel = new Label();
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        populateSavedCoins();
        createListCells();
        addOnlineUsersToList();
        addFriendsToList();
        // Initialize the barChart & lineChart arrays they will pull data from.
        barChartData = FXCollections.observableArrayList();
        barChartData2 = FXCollections.observableArrayList();
        pieChartData = FXCollections.observableArrayList();
        lineChartData = FXCollections.observableArrayList();
        pieChart.setTitle("Coin Prices");
        lineChart.setTitle("Compare Prices");
        series1 = new BarChart.Series<>();
        series4 = new BarChart.Series<>();
        series2 = new BarChart.Series<>();
        dataList = new LinkedList<>();
        series1.setName("Data");
        series4.setName("Prices");
        series2.setName("Prices");
        addRemoveComboBox.setItems(ADDREMOVE);
        tabChanges();
        // Set the default comboBox values
        comboBox.setValue("Timeframe");
        comboBox.setItems(TIMES);
        scanBtnT2.setDisable(true);
        addRemoveComboBox.setVisible(false);
        addContextMenuToList();
    }
}
