package coinTrack;
/**
 * The base document controller.
 * This controls the anchor pane that
 * contains the main tab pane.
 * 
 * - Kyle
 */

import coinClasses.ConnectToDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author kms
 */
public class FXMLDocumentController implements Initializable {
    
    public static String uname;
    protected Scene scene;
    @FXML protected TextField username;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private Label registerLabel;
    @FXML private TextField emailEntry;
    @FXML private TextField usernameEntry;
    @FXML private PasswordField passwordEntry;
    @FXML private PasswordField passwordRepeatEntry;
    @FXML Button registerSubmitBtn;
    @FXML Stage mainStage;
    @FXML Stage loginStage;
    @FXML Stage registerStage;
    @FXML Text registerInfo;
    
    
    //========== Action Handlers ==========
    
    /**
     * Login button action handler.
     * If the username and password are correct
     * the button redirects users to the main
     * FXMLDocument.fxml page.
     * @param event
     */
    @FXML public void login (ActionEvent event) {
//        loginStage = CoinTrack.newStage;
        ConnectToDatabase conn = new ConnectToDatabase();
        // If statement for testing purposes
        if (conn.validateLogin(username.getText(), txtPassword.getText())) {
            lblStatus.setText("Login Success");
            uname = username.getText();
            // After login is successful, you are taken to the main page
            Parent root;
            try {
                mainStage = new Stage();
                root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                scene = new Scene(root);
                mainStage.setScene(scene);
                mainStage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            lblStatus.setText("Login Failed");
        }
    }
    
    /**
     * Create new stage to take registration information from a user.
     * @param event 
     */
    @FXML public void handleRegister(ActionEvent event) {
        lblStatus.setText("Register User");
        // After login is successful, you are taken to the main page
        Parent root;
        try {
            registerStage = new Stage();
            root = FXMLLoader.load(getClass().getResource("RegisterUserFXML.fxml"));
            scene = new Scene(root);
            registerStage.setScene(scene);
            registerStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Registration page info verification and insertion
     * into database.
     * @param event 
     */
    @FXML
    private void handleRegisterSubmit(ActionEvent event) {
        System.out.println("register");
        if (emailEntry.getText().isEmpty()){
            emailEntry.setPromptText("Enter an email address");
            registerInfo.setFill(Color.RED);
            registerInfo.setText("Enter an email address");
        } else if (usernameEntry.getText().isEmpty()) {
            usernameEntry.setPromptText("Enter a username");
            registerInfo.setFill(Color.RED);
            registerInfo.setText("Enter a username");
        } else if (passwordEntry.getText().isEmpty()) {
            passwordEntry.setPromptText("Enter a password");
            registerInfo.setFill(Color.RED);
            registerInfo.setText("Enter a password");
        } else if (passwordRepeatEntry.getText().isEmpty()) {
            passwordRepeatEntry.setPromptText("Repeat your password");
            registerInfo.setFill(Color.RED);
            registerInfo.setText("Repeat your password");
        } else if (!passwordEntry.getText().equals(passwordRepeatEntry.getText())) {
            passwordEntry.setPromptText("Passwords must match");
            passwordRepeatEntry.setPromptText("Passwords must match");
            registerInfo.setFill(Color.RED);
            registerInfo.setText("Passwords must match");
        } else {
            // Call DB connection class
            ConnectToDatabase conn = new ConnectToDatabase();
            // Check is username exists in DB
            if (!conn.usernameExists(usernameEntry.getText())) { // TODO: invert  boolean ???
                String email = emailEntry.getText();
                String uname = usernameEntry.getText();
                String pass = passwordEntry.getText();
                // If all good, submit info to DB
                conn.userDatabase(email, uname, pass);
                conn.close();
                // Save username so it can be displayed in the application
                this.uname = uname;
                // After login is successful, you are taken to the main page
                registerInfo.setFill(Color.GREEN);
                registerInfo.setText("SUCCESS!");
                Parent root;
                try {
                    Stage stage = new Stage();
                    root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
//                    registerStage.close();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                registerInfo.setFill(Color.RED);
                registerInfo.setText("username taken");
            }

        }

    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
     
    }

}
