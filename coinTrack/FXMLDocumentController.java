/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coinTrack;

import coinClasses.AlphaVantageApi;
import coinClasses.CoinRankApi;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author kms
 */
public class FXMLDocumentController implements Initializable {
    
    protected Scene scene;
    @FXML protected TextField usernamePhone;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    
    @FXML private Button sendBtnT1;
    @FXML private TextArea txtAreaT1;
    
    
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
        CoinRankApi cri = new CoinRankApi();
        System.out.println(cri.getLimit());
        
        LinkedHashMap<String, String> lhm = cri.getNamePrice();
        String text = "";
        for (Map.Entry<String, String> entry : lhm.entrySet()) {
            text = text + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        txtAreaT1.setText(text);
        
    }
    
    @FXML
    private void handleClear(ActionEvent event) {
        System.out.println("hello");
    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        
    }    
    
}
