/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coinTrack;

import coinClasses.CoinRankApi;
import coinClasses.SingleCoin;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    
    // Bottom portion
    @FXML private Button sendBtnT1;
    @FXML private TextArea txtAreaT1;
    @FXML private ProgressBar progBarT1;
    @FXML private ProgressBar progBarT2;
    
    // Table View
    @FXML private TableView<SingleCoin> tableViewT1;
    @FXML private TableView<SingleCoin> tableViewT2;
    @FXML private TableColumn<SingleCoin, String> coinSymbolT1;
    @FXML private TableColumn<SingleCoin, String> coinSymbolT2;
    @FXML private TableColumn<SingleCoin, String> coinNameT1;
    @FXML private TableColumn<SingleCoin, String> coinNameT2;
    @FXML private TableColumn<SingleCoin, Integer> coinPriceT1;
    @FXML private TableColumn<SingleCoin, Integer> coinPriceT2;
    @FXML private TableColumn<SingleCoin, Integer> coinRankT1;
    @FXML private TableColumn<SingleCoin, Integer> coinRankT2;
    @FXML private TableColumn<SingleCoin, Double> coinChangeT1;
    @FXML private TableColumn<SingleCoin, Double> coinChangeT2;
    @FXML private TableColumn<SingleCoin, Integer> coinVolumeT1;
    @FXML private TableColumn<SingleCoin, Integer> coinVolumeT2;
    
    
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
    @FXML public void handleSend (ActionEvent event) {
        txtAreaT1.setText("Searching...");
        displayCoinText();
        
    }
    
    @FXML
    private void handleScan(ActionEvent event) {
        System.out.println("Scanning");
        displayCoinText();
    }
    
    @FXML
    private void handleClear(ActionEvent event) {
        System.out.println("hello");
    }
    
    
    // ========== HELPER METHODS ==========
    
    /**
     * Display the api data to the screen. 
     * 
     * Currently this just posts it into the
     * TextArea at the bottom of the page.
     */
    private void displayCoinText() {
        CoinRankApi cri = new CoinRankApi();
        
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
        displayTableView();
        
        String text = "";
        for (Map.Entry<String, String> entry : coinNamePrice.entrySet()) {
            text = text + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        txtAreaT1.setText(text);
    }
    
    /**
     * Display coin data to the table
     */
    private void displayTableView() {
//        tableViewT1 = new TableView<SingleCoin>();
        coinSymbolT1.setCellValueFactory(new PropertyValueFactory<SingleCoin, String>("symbol"));
//        coinSymbolT2.setCellValueFactory(new PropertyValueFactory<SingleCoin, String>("symbol"));
        coinNameT1.setCellValueFactory(new PropertyValueFactory<SingleCoin, String>("name"));
//        coinNameT2.setCellValueFactory(new PropertyValueFactory<SingleCoin, String>("name"));
        coinPriceT1.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("price"));
//        coinPriceT2.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("price"));
        coinRankT1.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("rank"));
//        coinRankT2.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("rank"));
        coinChangeT1.setCellValueFactory(new PropertyValueFactory<SingleCoin, Double>("change"));
//        coinChangeT2.setCellValueFactory(new PropertyValueFactory<SingleCoin, Double>("change"));
        coinVolumeT1.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("volume"));
//        coinVolumeT2.setCellValueFactory(new PropertyValueFactory<SingleCoin, Integer>("volume"));
        
        ObservableList<SingleCoin> obvList = FXCollections.observableArrayList(coinList);
        tableViewT1.setItems(obvList);
//        for (Iterator i = coinList.iterator(); i.hasNext();) {
//            
//        }
        
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
        
//        progBarT1.setProgress(0.0);
//        progBarT2.setProgress(0.0);
        
    }    
    
}
