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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    @FXML private TabPane mainTabPane;
    @FXML private TabPane graphTabPane;
    @FXML private Tab barChartTab;
    @FXML private Tab pieChartTab;
    @FXML private Tab bubbleChartTab;
    
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
    @FXML private Spinner<Integer> spinnerT2;
    
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
    
    /**
     * Login button action handler.
     * If the username and password are correct
     * the button redirects users to the main 
     * FXMLDocument.fxml page.
     * @param event 
     */
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
    
    
    
    
    
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
     
        
    }

}
