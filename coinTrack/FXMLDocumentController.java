package coinTrack;
/**
 * The base document controller.
 * This controls the anchor pane that
 * contains the main tab pane.
 * 
 * - Kyle
 */

import coinClasses.ConnectToDatabase;
import coinClasses.RecoveryEmail;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    private static String tempUsernameStorage;
    private String code;
    @FXML protected TextField username;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private Label registerLabel;
    @FXML private TextField emailEntry;
    @FXML private TextField usernameEntry;
    @FXML private PasswordField passwordEntry;
    @FXML private PasswordField passwordRepeatEntry;
    @FXML private Button registerSubmitBtn;
    @FXML public static Stage mainStage;           // Need to make getters for public stuff
    @FXML private static Stage forgotPassStage;
    @FXML private static Stage resetPassStage;
    @FXML private static Stage loginStage;
    @FXML private static Stage registerStage;
    @FXML private static Stage currentStage;
    @FXML private Text registerInfo;
    @FXML private TextField recoveryEmail;
    @FXML private TextField recoveryCode;
    @FXML private Button recoveryEmailBtn;
    @FXML private Text forgotPasswordWarning;
    @FXML private PasswordField resetPassword;
    @FXML private PasswordField resetPasswordRepeat;
    @FXML private Text passwordResetWarning;
    
    
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
        if (conn.validateLogin(this.username.getText(), this.txtPassword.getText())) {
            this.lblStatus.setText("Login Success");
            conn.setUserOnlineStatus(this.username.getText(), 1);
            conn.close();
            this.uname = this.username.getText();
            // After login is successful, you are taken to the main page
            Parent root;
            try {
                getCurrentStage().close();
                this.mainStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
                root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                this.scene = new Scene(root);
                this.mainStage.setScene(scene);
                this.mainStage.show();
                coinTrack.CoinTrack.newStage.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.lblStatus.setText("Login Failed");
        }
    }
    
    /**
     * Create new stage to take registration information from a user.
     * @param event 
     */
    @FXML public void handleRegister(ActionEvent event) {
        this.lblStatus.setText("Register User");
        // After login is successful, you are taken to the main page
        Parent root;
        try {
            // Create registration stage
            this.registerStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.registerStage;
            root = FXMLLoader.load(getClass().getResource("RegisterUserFXML.fxml"));
            this.scene = new Scene(root);
            this.registerStage.setScene(scene);
            this.registerStage.show();
            
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
        // Ugly if else to check for bad inputs.
        if (this.emailEntry.getText().isEmpty()){
            this.emailEntry.setPromptText("Enter an email address");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter an email address");
        } else if (usernameEntry.getText().isEmpty()) {
            this.usernameEntry.setPromptText("Enter a username");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter a username");
        } else if (this.passwordEntry.getText().isEmpty()) {
            this.passwordEntry.setPromptText("Enter a password");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter a password");
        } else if (this.passwordRepeatEntry.getText().isEmpty()) {
            this.passwordRepeatEntry.setPromptText("Repeat your password");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Repeat your password");
        } else if (!this.passwordEntry.getText().equals(this.passwordRepeatEntry.getText())) {
            this.passwordEntry.setPromptText("Passwords must match");
            this.passwordRepeatEntry.setPromptText("Passwords must match");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Passwords must match");
        } else {
            // Call DB connection class
            ConnectToDatabase conn = new ConnectToDatabase();
            // Check is username exists in DB
            if (!conn.usernameExists(this.usernameEntry.getText())) { // TODO: invert  boolean ???
                String email = this.emailEntry.getText();
                String uname = this.usernameEntry.getText();
                String pass = this.passwordEntry.getText();
                // If all good, submit info to DB
                conn.userDatabase(0, email, uname, pass);
                conn.close();
                // Save username so it can be displayed in the application
                this.uname = uname;
                // After login is successful, you are taken to the main page
                this.registerInfo.setFill(Color.GREEN);
                this.registerInfo.setText("SUCCESS!");
                Parent root;
                try {
                    getCurrentStage().close();
                    Stage stage = new Stage();
                    FXMLDocumentController.currentStage = stage;
                    root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
                    this.scene = new Scene(root);
                    stage.setScene(this.scene);
                    stage.show();
//                    FXMLDocumentController.registerStage.close();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.registerInfo.setFill(Color.RED);
                this.registerInfo.setText("username taken");
            }
        }
    }
    
    /**
     * Change to the password recovery stage if the forgot password
     * button is clicked.
     * @param event 
     */
    @FXML
    private void handleForgotPassword(ActionEvent event) {
        System.out.println("Forgot your password already..");
        Parent root;
        try {
            getCurrentStage().close();
            this.forgotPassStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.forgotPassStage;
            root = FXMLLoader.load(getClass().getResource("ForgotPasswordFXML.fxml"));
            this.scene = new Scene(root);
            this.forgotPassStage.setScene(this.scene);
            this.forgotPassStage.show();
//            coinTrack.CoinTrack.newStage.close();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Handles action of clicking the forgot password button.
     * @param event 
     */
    @FXML
    private void handleRecoveryEmail(ActionEvent event) {
        String toEmail = this.recoveryEmail.getText();
        ConnectToDatabase conn = new ConnectToDatabase();
        if (conn.emailExists(toEmail)) {
            tempUsernameStorage = conn.getUsernameFromEmail(toEmail);
            System.out.println(tempUsernameStorage);
            conn.close();
            this.code = generateRecoveryCode();
            RecoveryEmail sendMail = new RecoveryEmail(toEmail, this.tempUsernameStorage, this.code);
        } else {
            this.forgotPasswordWarning.setText("hmm, can't find that email");
            this.forgotPasswordWarning.setFill(Color.RED);
        }
    }
    
    /**
     * Send an email containing a code that the user will type
     * into the box to confirm their identity.
     * @param event 
     */
    @FXML
    private void handleRecoveryCode(ActionEvent event) {
        if (this.recoveryCode.getText().equals(this.code)) {
//            this.forgotPassStage.close();
            Parent root;
            try {
                getCurrentStage().close();
                this.resetPassStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.resetPassStage;
                root = FXMLLoader.load(getClass().getResource("PasswordResetFXML.fxml"));
                this.scene = new Scene(root);
                this.resetPassStage.setScene(this.scene);
                this.resetPassStage.show();
//                this.forgotPassStage.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.forgotPasswordWarning.setText("Incorrect Code");
            this.forgotPasswordWarning.setFill(Color.RED);
        }
    }
    
    /**
     * When the user presses the "reset password" button on 
     * PasswordResetFXML.fxml page a connection to the database 
     * is made submitting the updated password;
     * @param event 
     */
    @FXML
    private void handleResetPassword(ActionEvent event) {
        System.out.println("register");
        System.out.println(tempUsernameStorage);
        // Make sure new password is not empty
        if (this.resetPassword.getText().isEmpty()){
            this.resetPassword.setPromptText("Enter new password");
            this.passwordResetWarning.setFill(Color.RED);
            this.passwordResetWarning.setText("Password field can't be empty");
        // Make sure the two passwords match
        } else if (this.resetPassword.getText().equals(this.resetPasswordRepeat.getText())) {
            ConnectToDatabase conn = new ConnectToDatabase();
            String newPass = this.resetPassword.getText();
            // Submit changed passwords to the database
            conn.changePassword(tempUsernameStorage, newPass);
            // Close the connection
            conn.close();
            this.passwordResetWarning.setText("Success");
            this.passwordResetWarning.setFill(Color.GREEN);
            // Then change the stage back to the login screen
            Parent root;
            try {
                getCurrentStage().close();
                this.mainStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
                root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
                this.scene = new Scene(root);
                this.mainStage.setScene(this.scene);
                this.mainStage.show();
//                this.resetPassStage.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.resetPassword.setPromptText("Passwords must match");
            this.passwordResetWarning.setFill(Color.RED);
        }
    }

    /**
     * Generate simple random string of numbers.
     * @return 
     */
    private String generateRecoveryCode() {
        String newCode = "";
        Random rand = new Random();
        int digits = rand.nextInt(1000);
        newCode += digits;
        return newCode;
    }
    
    public void closeStage() {
        // Hacky way to find last stage and close it, doesn't really work
            if (getCurrentStage() != null) {getCurrentStage().close();}
            if (coinTrack.CoinTrack.newStage != null) {coinTrack.CoinTrack.newStage.close();}
            if (coinTrack.FXMLDocumentController.getCurrentStage() != null) {coinTrack.FXMLDocumentController.getCurrentStage().close();}
    }
    
    /**
     * Gets the main stage so it can be closed from other stages.
     * @return 
     */
    public static Stage getMainStage() {
        return FXMLDocumentController.mainStage;
    }
    
    /**
     * This is supposed to return the current stage so it
     * can be closed. But I don't think it works... idk.
     * @return 
     */
    public static Stage getCurrentStage() {
        return FXMLDocumentController.currentStage;
    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        FXMLDocumentController.currentStage = coinTrack.CoinTrack.newStage;
        ConnectToDatabase conn = new ConnectToDatabase();
        
        /**
         * HOW DO I DO SOMETHING AFTER PROGRAM IS CLOSED !?!?
         * ARGHHH
         */
        
        getCurrentStage().setOnCloseRequest(evt -> 
                conn.setUserOnlineStatus(this.uname, 0)
        );
    }

}
