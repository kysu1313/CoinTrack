package tabControllers;

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.SingleCoin;
import coinClasses.UserCoin;
import coinTrack.FXMLDocumentController;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import tabControllers.assistantControllers.Tab1AssistantController;
import tabControllers.assistantControllers.Tab2AssistantController;


/**
 * This is the "Dashboard" tab. It contains data for all the users saved coins.
 * @author Kyle
 */
public class Tab3Controller implements Initializable{

    private static final String USERNAME = FXMLDocumentController.uname;
    private LinkedList<UserCoin> userCoinList;
    private LinkedList<SingleCoin> coinList;
    private LinkedList<SingleCoin> userSingleCoins;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private LinkedHashMap<Double, String> userHistoryMap;
    private LinkedList<LinkedHashMap<Double, String>> linkedUserHistoryMap;
    private final String TIMEFRAME = "24h";

    @FXML private BarChart barChartDash;
    @FXML private PieChart pieChartDash;
    @FXML private TableView tableDash;
    @FXML private ListView listDash;

    /**
     * Get users saved coins from database then create SingleCoin objects
     * for each.
     */
    private void getCoinList() {
        ConnectToDatabase conn = new ConnectToDatabase();
        this.userCoinList = conn.getSavedCoins(USERNAME);
        conn.close();
        CoinRankApi cri = new CoinRankApi();
        cri.run();
        cri.join();
        this.coinList = cri.getCoinList();
        this.coinList.forEach((item) -> {
            userCoinList.forEach((entry) -> {
                if (item.getName().equalsIgnoreCase(entry.getName())){
                    userSingleCoins.add(item);
                }
            });
        });
        this.singleHistoryMap = new CoinHistory().getSingleHistory();
        this.userCoinList.forEach((item) -> {
            userHistoryMap = new CoinHistory(item.getCoinID(), item.getName(), this.TIMEFRAME).getSingleHistory();
            linkedUserHistoryMap.add(userHistoryMap);
        });
        
    }

    /**
     * Create the table.
     * Uses tab1AssistantController to format the table.
     */
    private void createTable() {
        Tab1AssistantController tas1 = new Tab1AssistantController();
        tas1.coinTableDash(this.tableDash, this.userSingleCoins);
    }
    
    private void createPieChart() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.PieChartDashboard(this.userSingleCoins, this.pieChartDash);
    }
    
    private void createBarChart() {
        Tab2AssistantController tas2 = new Tab2AssistantController();
        tas2.multiBarChart(this.barChartDash, this.linkedUserHistoryMap, this.userCoinList.size());
        this.barChartDash.setLegendVisible(false);
//        this.barChartDash.
    }

    /**
     * Initialize tab3, "dashboard".
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userCoinList = new LinkedList<>();
        this.coinList = new LinkedList<>();
        this.userSingleCoins = new LinkedList<>();
        this.singleHistoryMap = new LinkedHashMap<>();
        this.userHistoryMap = new LinkedHashMap<>();
        this.linkedUserHistoryMap = new LinkedList<>();
        getCoinList();
        createTable();
        createPieChart();
        createBarChart();
    }
}
