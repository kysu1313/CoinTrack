package tabControllers;

/**
 * Tab 2 document controller.
 * Handles actions on inputs. Main functions
 * are displaying graphs, searches, accordion.
 */

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToApi;
import coinClasses.ConnectToDatabase;
import coinClasses.UserCoin;
import coinTrack.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tabControllers.assistantControllers.HoveredThresholdNode;
import tabControllers.assistantControllers.Tab2AssistantController;

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
    
    Tab2AssistantController assistT2;

    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private TabPane graphTabPane;
    @FXML private Tab barChartTab;
    @FXML private Tab pieChartTab;
    @FXML private Tab lineChartTab;
    @FXML private Tab webTab;
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

    // Bar Chart
    @FXML private BarChart barChart;
    private XYChart.Series series1;
    private XYChart.Series series4;
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
                removeCoinFromLineChart(this.searchFieldT2.getText());
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
        clearLineChart();
        
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
                setOnlineStatus(coinTrack.FXMLDocumentController.uname, 0);
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
    private void setOnlineStatus(String _uname, int _status) {
        ConnectToDatabase conn = new ConnectToDatabase();
        conn.setUserOnlineStatus(_uname, _status);
        conn.close();
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
                this.singleHistoryMap = new CoinHistory(0, currCoin, this.timeSelection).getSingleHistory();
            } else {
                int temp = Integer.parseInt(currCoin);
                this.singleHistoryMap = new CoinHistory(temp, "", this.timeSelection).getSingleHistory();
            }
        }
        // Prevent old data from showing back up  --  NOT WORKING
        this.barChartData2.clear();
        this.series4.getData().clear();
        // Add entries from singleHistoryMap into series1
        for (Map.Entry<Double, String> entry : this.singleHistoryMap.entrySet()) {
            long tempLong = Long.parseLong(entry.getValue());
            Date d = new Date(tempLong);
            String date = "" + d;
            double price = entry.getKey();
            this.series4.getData().add(new XYChart.Data(date, price));
        }
        // Add series1 to the barChartData, then add that to the barChart
        this.barChart.setTitle("Viewing the past " + this.timeSelection);
        this.barChartData2.add(this.series4);
        this.barChart.setData(this.barChartData2);
        colorBarChart("green", "red");
        scaleGraph();
    }
    
    private void colorBarGraph() {
        double lastPrice = 0;
        int count = 0;
        // A way to color the bars in the bargraph green or red.
        for (Map.Entry<Double, String> entry : this.singleHistoryMap.entrySet()) {
            double price = entry.getKey();
            if (count < this.singleHistoryMap.size()){
                if (price > lastPrice) {
                    Node n = this.barChart.lookup(".data" + count + ".chart-bar");
                    n.setStyle("-fx-bar-fill: green");
                } else {
                    Node n = this.barChart.lookup(".data" + count + ".chart-bar");
                    n.setStyle("-fx-bar-fill: red");
                }
            }
            // Add series1 to the barChartData, then add that to the barChart
//            this.barChart.setTitle("Viewing the past " + this.timeSelection);
//            this.barChartData2.add(this.series4);
//            this.barChart.setData(this.barChartData2);
            
        }
    
    }
    
    /**
    * THIS IS FOR TESTING.
    * THIS IS NOT MY CODE.
    *
    * Implements scrolling using the mouse wheel on the graph.
    */
    private void scaleGraph() {
        double delta = 1.1;
        this.barChart.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) {
                return;
            }
            // Keep the scale ratio as you zoom in/out
            double factor = (event.getDeltaY() > 0) ? delta : 1 / delta;
            barChart.setScaleX(barChart.getScaleX() * factor);
            barChart.setScaleY(barChart.getScaleY() * factor);
        });
        this.barChart.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                barChart.setScaleX(1.0);
                barChart.setScaleY(1.0);
            }
        });
    }
    
    /**
     * Color barChart bars the given colors.
     * 
     * This only takes basic colors, red, greed, blue, etc.
     */
    private void colorBarChart(String upPrice, String downPrice) {
        double lastPrice = 0;
        int count = 0;
        // A way to color the bars in the bargraph green or red.
        for (Map.Entry<Double, String> entry : this.singleHistoryMap.entrySet()) {
            double price = entry.getKey();
            if (count < this.singleHistoryMap.size() && price > lastPrice) {
                Node n = this.barChart.lookup(".data" + count + ".chart-bar");
                n.setStyle("-fx-bar-fill: " + upPrice);
            } else if (count < this.singleHistoryMap.size()){
                Node n = this.barChart.lookup(".data" + count + ".chart-bar");
                n.setStyle("-fx-bar-fill: " + downPrice);
            }
            lastPrice = price;
            count++;
        }
    }

    /**
     * Display coin historical prices in a line chart.
     */
    private void displayLineGraph() {
        System.out.println("displaying line graph");
        XYChart.Series newSeries = new XYChart.Series();
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
        getSeriesForChart2(this.linesToGraph);
        // Add series1 to the barChartData, then add that to the barChart
        this.lineChart.setTitle("Viewing the past 1y of: " + this.searchFieldT2.getText());
        this.lineChart.setData(this.lineChartData);
    }

    /**
     * This creates a linkedList of Series that will be added to the
     * Line Chart.
     *
     * I'm not sure what the best way to do this is...
     *
     * @param lines
     * @return
     */
    private void getSeriesForChart2(LinkedList<String> lines) {
        // For every coin entered, create a new CoinHistory object.
        for (String line : lines) {
            CoinHistory coinHist = new CoinHistory(0, line, this.timeSelection);
            this.singleHistoryMap = coinHist.getSingleHistory();
//            XYChart.Series newSeries = new XYChart.Series();
            XYChart.Series<Number, String> newSeries = new XYChart.Series();
            double previousPrice = 0;
            // Create the datapoints for the line chart.
            this.singleHistoryMap.entrySet().forEach((Map.Entry<Double, String> entry) -> {
                long tempLong = Long.parseLong(entry.getValue());
                Date d = new Date(tempLong);
                String date = "" + d;
                double price = entry.getKey();
                XYChart.Data item = new XYChart.Data(date, price);
                item.setExtraValue(line + ": " + price);
                newSeries.getData().add(item);
                newSeries.getNode();
//                Tooltip.install(item.getNode(), new Tooltip(line + ": " + item.getYValue()));
                
                this.dataList.add(new XYChart.Data(date, price));
            });
//            if (newSeries.getName() == null){newSeries.setName(line);}
            this.seriesList.add(newSeries);
            for (Data<Number, String> entry : newSeries.getData()) {
                Tooltip t = new Tooltip(entry.getExtraValue().toString());
                Tooltip.install(entry.getNode(), t);
            }
            this.lineChartData.add(newSeries);
        }
        Iterator<XYChart.Series<Number, String>> it1 = lineChartData.iterator();
        Iterator<String> it2 = lines.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            it1.next().setName(it2.next());
        }
    }
    
    /**
     * Create the different series for each coin the user adds.
     * Returns a linkedList of the XYChart.Series to add to the chart.
     * @param lines
     * @return 
     */
    
    private void addToolTips(LinkedList<XYChart.Data<String, Number>> dataLst) {
        dataLst.forEach((data) -> {
            Tooltip t = new Tooltip(data.getYValue().toString());
            Tooltip.install(data.getNode(), t);
        });
    }

    /**
     * Remove all coins from the line chart.
     */
    private void clearLineChart() {
        this.seriesMap.clear();
//        this.lineChart.getData().clear();
        this.lineChart.layout();
//        this.lineChartData.clear();
        this.seriesList.forEach((entry) -> {
            entry.getData().removeAll();
        });
    }

    /**
     * Remove the given coin from the line chart.
     * @param coin
     */
    private void removeCoinFromLineChart(String coin) {
        XYChart.Series toRemove = this.seriesMap.get(coin);
        this.seriesMap.remove(coin);
        this.lineChartData.remove(toRemove);
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

    private void populateSavedCoins() {
        ConnectToDatabase conn = new ConnectToDatabase();
        savedCoinsListT2.getItems().clear();
        this.savedCoins = conn.getSavedCoins(this.uname);
        conn.close();
        if (this.savedCoins != null && this.savedCoins.size() > 0) {
            for (int i = 0; i < this.savedCoins.size(); i++) {
                savedCoinsListT2.getItems().add(this.savedCoins.get(i));
            }
        }
    }
    
    /**
     * Creates a LinkedList of SingleCoins.
     * 
     * This method has been moved to Tab2AssistantController
     * in an effort to keep this controller as clean as possible.
     */
    private void displayPieChart() {
        assistT2.PieChart(this.coinList, this.pieChartCoins, this.comboBox, this.pieChartData, this.pieChart);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    private void addOnlineUsersToList() {
        assistT2.addOnlineUsers(this.onlineUsers, this.onlineUsersListT2);
    }

    /**
     * This creates the right click menu on the onlineUsers list. 
     * It also maps each button to an action.
     */
    private void createListCells() {
        assistT2.listCells(this.onlineUsersListT2, this.uname, this.txtAreaT2);
    }

    /**
     * Call database returning a list of friends.
     */
    private void addFriendsToList() {
        assistT2.friendList(this.friendList, this.uname, this.friendsListT2);
    }

    /**
     * Creates right-clickable cells in the friends list in the accordion.
     */
    private void createFriendListCells() {
        assistT2.friendListCells(this.friendsListT2);
    }
 
    /**
     * Returns and closes the main stage from the class where it was created
     */
    private void closeOldStage() {
        coinTrack.FXMLDocumentController.mainStage.close();
    }
    
    private void setMouseLineChart() {
        final Axis<String> xAx = lineChart.getXAxis();
        final Axis<Number> yAx = lineChart.getYAxis();
        final Label cursorCoords = new Label();
        sideVBox.getChildren().add(cursorCoords);
        final Node chartBackground = lineChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAx && n != yAx) {
                n.setMouseTransparent(true);
            }
        }
        lineChart.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        lineChart.setOnMouseMoved((MouseEvent mouseEvent) -> {
            String x = mouseEvent.getX() + "";
            String xy = x + ", " + yAx.getValueForDisplay(mouseEvent.getY());
            cursorCoords.setText(xy);
        });
        lineChart.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });
        xAx.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        xAx.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText( xAx.getValueForDisplay(mouseEvent.getX()));
        });
        xAx.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });

        yAx.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        yAx.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "y = %.2f",
                            yAx.getValueForDisplay(mouseEvent.getY())
                    )
            );
        });
        yAx.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });
    }
    
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
                    setMouseLineChart();
                    addRemoveComboBox.setValue("Add / Remove");
                    lineChart.setAnimated(false);
                }
            }
        });
    }
    
    private void addListEvents() {
        onlineUsersListT2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            ContextMenu cmu = new ContextMenu();
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cmu.show(onlineUsersListT2, event.getScreenX(), event.getScreenY());
                }
            }
        });
        savedCoinsListT2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            ContextMenu cmu = new ContextMenu();
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cmu.show(savedCoinsListT2, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assistT2 = new Tab2AssistantController();
        // Save uname from login page.
        String uname = coinTrack.FXMLDocumentController.uname;
        messageText.setText("Hello " + uname);
        this.uname = coinTrack.FXMLDocumentController.uname;
//        this.DEBUG = Tab1Controller.DEBUG;
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
        addListEvents();
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
        dataList = new LinkedList<>();
        series1.setName("Data");
        series4.setName("Prices");
        addRemoveComboBox.setItems(ADDREMOVE);
        tabChanges();
        // Set the default comboBox values
        comboBox.setValue("Timeframe");
        comboBox.setItems(TIMES);
        scanBtnT2.setDisable(true);
        addRemoveComboBox.setVisible(false);
    }
    
    @FXML
    private void refresh(ActionEvent event) {
        populateSavedCoins();
    }
}
