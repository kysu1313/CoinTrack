package tabControllers;

/**
 * Tab 2 document controller.
 * Handles actions on inputs. Main functions
 * are displaying graphs, searches, accordion.
 */

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.UserCoin;
import coinTrack.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tabControllers.assistantControllers.HoveredThresholdNode;
import tabControllers.assistantControllers.TabAssistantController;
import tabControllers.assistantControllers.graphs.BarChartClass;
import tabControllers.assistantControllers.graphs.LineChartClass;

/**
 *
 * @author Kyle
 */
public class Tab2Controller implements Initializable{
    
    private CoinHistory coinHistory;
    private LinkedHashMap<String, Integer> historyMap;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private int coinsToGraph = 25;
//    private boolean isSingleCoinScan;
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
    private static Stage mainPage2;
    private String uname;
    private LinkedList<String> onlineUsers;
    private LinkedList<String> friendList;
    private LinkedList<UserCoin> savedCoins;
    private HashMap<String, XYChart.Series> seriesMap;
    private LinkedList<XYChart.Series> seriesList;
    private LinkedList<String> linesToGraph;
    private LinkedList<XYChart.Data<String, Number>> dataList;
//    private XYChart.Series[] seriesArr;
    public HoveredThresholdNode node;
    private Tab currTab;
    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private LineChartClass lcc;
    
//    Tab2AssistantController assistT2;
//    Tab1AssistantController assistT1;
    private TabAssistantController tas;
    ContextMenu cm3;
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private TabPane graphTabPane;
    @FXML private Tab barChartTab;
    @FXML private Tab pieChartTab;
    @FXML private Tab lineChartTab;
    @FXML private Tab webTab;
    @FXML private Tab candleTab;
//    @FXML private ToolBar toolBarT2;
    @FXML private ComboBox addRemoveComboBox;
    @FXML public static Label coordsLabel;
    @FXML private VBox sideVBox;
//    @FXML private Label hoverLabel;

    // Accordion
    @FXML private ListView onlineUsersListT2;
    @FXML private ListView savedCoinsListT2;
    @FXML private ListView friendsListT2;
    @FXML private ContextMenu cm;

    // Bottom portion Tab 2
    @FXML private SplitMenuButton splitMenuT2;
    @FXML private TextField searchFieldT2;
    @FXML private SplitMenuButton numCoins;
    @FXML private TextArea txtAreaT2;
    @FXML private Spinner<String> spinnerT2;
    @FXML private ComboBox<String> comboBox;
    @FXML private Button scanBtnT2;
    @FXML private Button searchBtnT2;
    @FXML private Text messageText;
    @FXML private Pane candlePane;

    // Bar Chart
    @FXML private BarChart barChart;
    private XYChart.Series series1;
    private XYChart.Series series4;
    private XYChart.Series series2; //add this line

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
    private void handleScanT2(ActionEvent event) {
        if (graphTabPane.getSelectionModel().getSelectedItem() == barChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            barChart.getData().clear();
            barChart.layout();
            // Get the comboBox entry
            boolean isComboBoxEmpty = comboBox.getSelectionModel().isEmpty();
            if (!isComboBoxEmpty) {
                timeSelection = (String)comboBox.getValue();
            } else {
                timeSelection = "7d";
            }
            // Make a CoinHistory api call
            coinHistory = new CoinHistory();
            displayMultiCoinGraph();

        } else if (graphTabPane.getSelectionModel().getSelectedItem() == pieChartTab) {
            if (DEBUG){System.out.println("pie chart selected");}
            pieChart.getData().clear();
            pieChart.layout();
            coinList = new CoinRankApi();
            coinList.run();
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
    private void handleSearchT2(ActionEvent event) {
        if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.barChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            this.barChart.getData().clear();
            this.barChart.layout();
            this.barChartData.clear();
            this.series1.getData().clear();
            this.barChartData2.clear();
            this.series4.getData().clear();
            // timeSelection
            boolean isComboBoxEmpty = this.comboBox.getSelectionModel().isEmpty();
            if (isComboBoxEmpty) {
                // Default timeframe will be 7 days.
                this.timeSelection = "7d";
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
                    if (this.linesToGraph.size() >= 10) {
                        this.txtAreaT2.setText("Too many coins, clear graph to view more.");
                    } else {
                        this.linesToGraph.add(this.searchFieldT2.getText());
                        displayLineGraph();
                    }
                }
            } else {
                this.lcc.removeCoin(this.searchFieldT2.getText());
                this.lcc.displayGraph();
//                removeCoinFromLineChart(this.searchFieldT2.getText());
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
    private void handleClearT2(ActionEvent event) {
        this.barChart.getData().clear();
        this.barChart.layout();
        this.pieChart.getData().clear();
        this.pieChart.layout();
        this.searchFieldT2.setText("");
        this.barChartData2.clear();
        this.series4.getData().clear();
        this.dataList.clear();
        this.sideVBox.getChildren().clear();
//        if (this.linesToGraph != null) {
//            this.lcc.clearChart();              // TODO: fix clear
//        }
    }

    /**
     * Changes depending on what graph tab is open.
     * @param event
     */
    @FXML
    private void handleComboBox(ActionEvent event) {
        this.timeSelection = this.comboBox.getValue();
    }

    /**
     * Handle log out button click.
     * Takes you back to log in screen.
     * @param event
     */
    @FXML
    private void handleLogOutT2(ActionEvent event) {
        if (DEBUG){System.out.println("logging out");}
        Parent root;
            try {
                Tab2Controller.mainPage2 = new Stage();
                this.tas.setOnlineStatus(coinTrack.FXMLDocumentController.uname, 0);
                root = FXMLLoader.load(getClass().getClassLoader().getResource("coinTrack/FXMLLogin.fxml"));
                this.scene = new Scene(root);
                Tab2Controller.mainPage2.setScene(this.scene);
                Tab2Controller.mainPage2.show();
                closeOldStage();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * Change a users online status. i.e. when they log off .
     * @param _uname
     * @param _status
     */
//    private void setOnlineStatus(String _uname, int _status) {
//        ConnectToDatabase conn = new ConnectToDatabase();
//        conn.setUserOnlineStatus(_uname, _status);
//        conn.close();
//    }

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
                this.singleHistoryMap = new CoinHistory(0, currCoin, this.timeSelection).getSingleHistory();
            } else {
                int temp = Integer.parseInt(currCoin);
                this.singleHistoryMap = new CoinHistory(temp, "", this.timeSelection).getSingleHistory();
            }
        }
        // Create a new BarChart object passing appropriate values.
        BarChartClass bcc = new BarChartClass(this.series4, this.singleHistoryMap,
                                              this.timeSelection, this.barChart, this.txtAreaT2);
        bcc.displaySingleGraph();
    }

    /**
     * Display coin historical prices in a line chart.
     */
    private void displayLineGraph() {
        System.out.println("displaying line graph");
//        XYChart.Series newSeries = new XYChart.Series();
        // Get the string/int from the text field.
        if (this.searchFieldT2.getText().isEmpty()){
            AlertMessages.showInformationMessage("Coin Name",
                            "You must enter the name, symbol, or id"
                            + "of the coin you would like to view.");
        } else {
            char ch = this.searchFieldT2.getText().charAt(0);
            if (Character.isAlphabetic(ch)){
                CoinHistory coinHist = new CoinHistory(0, this.searchFieldT2.getText(), this.timeSelection);
                this.singleHistoryMap = coinHist.getSingleHistory();
            } else {
                int temp = Integer.parseInt(this.searchFieldT2.getText());
                CoinHistory coinHist = new CoinHistory(temp, "", this.timeSelection);
                this.singleHistoryMap = coinHist.getSingleHistory();
            }
        }
        this.sideVBox.getChildren().clear();
        // Create line chart object to display graphs.
        this.lcc = new LineChartClass(this.lineChart, this.linesToGraph, this.timeSelection);
        this.lcc.displayGraph();
        this.lcc.getElements().forEach((item) -> {
            this.sideVBox.getChildren().add(new Label(item));
        });
    }

    /**
     * Display / Rank all coin prices
     */
    private void displayMultiCoinGraph() {
        if (DEBUG){System.out.println("displaying graph");}
//        coinHistory.join();
        this.historyMap = this.coinHistory.getPriceDate();
        int count = 0;
        this.historyMap.entrySet().forEach((entry) -> {
            this.series1.getData().add(new XYChart.Data(
                    entry.getKey(), entry.getValue()));
        });
        this.barChartData.add(this.series1);
        this.barChart.setData(this.barChartData);
    }

    private void saveCoin(String userName, int coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(userName, coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }

    /**
     * Populate saved coin side area.
     */
    private void populateSavedCoins() {
        this.tas.populateSavedCoins(savedCoinsListT2, savedCoins);
    }

    /**
     * Creates a LinkedList of SingleCoins.
     *
     * This method has been moved to Tab2AssistantController
     * in an effort to keep this controller as clean as possible.
     */
    private void displayPieChart() {
        this.coinList.join();
        this.tas.MakePieChart(coinList, pieChartCoins, comboBox, pieChartData, pieChart);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        tas.addOnlineUsers(this.onlineUsers, this.onlineUsersListT2);
    }

    /**
     * This creates the right click menu on the onlineUsers list. 
     * It also maps each button to an action.
     */
    private void createListCells() {
        tas.listCells(this.onlineUsersListT2, this.uname);
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        tas.friendList(this.friendList, this.uname, this.friendsListT2);
    }

    private void displayCandleChart() throws ParseException {
        tas.candleChart(this.candlePane);
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
                if (currentTab == barChartTab) {
                    currTab = barChartTab;
                    comboBox.setValue("Timeframe");
                    tabSelection = 1;
                    scanBtnT2.setDisable(true);
                    searchBtnT2.setDisable(false);
                    addRemoveComboBox.setVisible(false);
                    comboBox.setItems(TIMES);
                } else if (currentTab == pieChartTab) {
                    currTab = pieChartTab;
                    comboBox.setValue("Number of coins");
                    tabSelection = 2;
                    searchBtnT2.setDisable(true);
                    scanBtnT2.setDisable(false);
                    addRemoveComboBox.setVisible(false);
                    comboBox.setItems(NUMCOINS);
                } else if (currentTab == lineChartTab) {
                    currTab = lineChartTab;
                    comboBox.setValue("Timeframe");
                    tabSelection = 3;
                    searchBtnT2.setDisable(false);
                    scanBtnT2.setDisable(true);
                    addRemoveComboBox.setVisible(true);
                    comboBox.setItems(TIMES);
                    addRemoveComboBox.setValue("Add / Remove");
                    lineChart.setAnimated(false);
                } else if (currentTab == candleTab) {
                    currTab = candleTab;
                    comboBox.setValue("Timeframe");
                    tabSelection = 4;
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

    @FXML
    private void refresh(ActionEvent event) {
        populateSavedCoins();
    }

    /** Save coin graph in graph tab**/

    private LinkedHashMap<Double, String> userHistoryMap;

    private void addContextMenuToList() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem graphCoin = new MenuItem("Graph Coin");
        graphCoin.setOnAction(event -> createGraphsForSavedCoins());
        contextMenu.getItems().addAll(graphCoin);
        savedCoinsListT2.setContextMenu(contextMenu);
    }

    private void createGraphsForSavedCoins() {
        if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.barChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            this.barChart.getData().clear();
            this.series2 = new BarChart.Series<>();
            if (this.timeSelection == null){this.timeSelection = "24h";}
            UserCoin item = (UserCoin)savedCoinsListT2.getSelectionModel().getSelectedItem();
            this.userHistoryMap = new CoinHistory(item.getCoinID(), item.getName(), this.timeSelection).getSingleHistory();
            // Create new bar chart object
            BarChartClass bcc = new BarChartClass(this.series2, this.userHistoryMap, this.timeSelection, this.barChart, this.txtAreaT2);
            bcc.displaySingleGraph();
            bcc.alternateColors("green", "red");
        } else if (this.graphTabPane.getSelectionModel().getSelectedItem() == this.lineChartTab) {
            if (DEBUG){System.out.println("bar chart selected");}
            this.lineChart.getData().clear();
            this.series2 = new BarChart.Series<>();
            if (this.timeSelection == null){this.timeSelection = "24h";}
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
            _lbl.setStyle("-fx-background-color: white;");
        });
        ContextMenu cm = new ContextMenu();
        MenuItem m1 = new MenuItem("Remove");
        m1.setOnAction((event) -> {
            if (DEBUG) {System.out.println("Choice 1 clicked!");}
            this.lcc.removeCoin(_lbl.getText());
        });
        cm.getItems().addAll(m1);
        _lbl.setContextMenu(cm);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tas = new TabAssistantController();
        // Save uname from login page.
        String uname = coinTrack.FXMLDocumentController.uname;
        messageText.setText("Hello " + uname);
        this.uname = coinTrack.FXMLDocumentController.uname;
        System.out.println(DEBUG);
        messageText.setText("Hello " + uname);
        linesToGraph = new LinkedList<>();
        seriesList = new LinkedList<>();
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
        seriesMap = new HashMap<>();
        series1 = new BarChart.Series<>();
        series4 = new BarChart.Series<>();
        series2 = new BarChart.Series<>(); //add this line
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
        addContextMenuToList(); //add this line as well
    }
}
