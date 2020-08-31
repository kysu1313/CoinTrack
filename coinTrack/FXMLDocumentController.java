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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;

/**
 *
 * @author kms
 */
public class FXMLDocumentController implements Initializable {
    
    private LinkedHashMap<String, String> coinNamePrice;
    private LinkedList<SingleCoin> coinList;
    private int count;
    
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
    @FXML private TableColumn<SingleCoin, String> coinNameT1;
    @FXML private TableColumn<SingleCoin, String> coinNameT2;
    @FXML private TableColumn<SingleCoin, Integer> coinPriceT1;
    @FXML private TableColumn<SingleCoin, Integer> coinPriceT2;
    
    
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
        System.out.println(cri.getLimit());
        
        coinNamePrice = cri.getNamePrice();
        String text = "";
        for (Map.Entry<String, String> entry : coinNamePrice.entrySet()) {
            text = text + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        txtAreaT1.setText(text);
    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        
//        progBarT1.setProgress(0.0);
//        progBarT2.setProgress(0.0);
        
    }    
    
}
