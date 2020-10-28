
package coinTrack;

/**
 * The base document controller. This controls the anchor pane that contains the
 * main tab pane.
 *
 * - Kyle
 */

import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.Email;
import coinClasses.SaveToDisk;
import coinClasses.SingleCoin;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;//Click "x" event
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tabControllers.AlertMessages;
import tabControllers.Tab1Controller;
import static tabControllers.Tab1Controller.DEBUG;
import tabControllers.assistantControllers.Theme;

/**
 *
 * @author kms
 */
public class FXMLDocumentController implements Initializable {

    public static String uname;
    public static Tab currTab;
    public static Scene scene;
    private static String tempUsernameStorage;
    private String code;
    private int _code;
    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private static Theme theme;
    private File saveLoc;
    private final ObservableList<Theme> THEMES = FXCollections.
            observableArrayList(new Theme("Dark"), new Theme("Light"));
    private final ObservableList<String> FILE_TYPES = FXCollections.
            observableArrayList(".txt", ".xlsx", ".json");
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
    @FXML private static Stage forgotUserStage;
    @FXML private static Stage resetPassStage;
    @FXML private static Stage loginStage;
    @FXML private static Stage registerStage;
    @FXML private static Stage currentStage;
    @FXML private static Stage saveStage;
    @FXML private Text registerInfo;
    @FXML private TextField recoveryEmail;
    @FXML private TextField recoveryCode;
    @FXML private Button recoveryEmailBtn;
    @FXML private Text forgotWarning;
    @FXML private PasswordField resetPassword;
    @FXML private PasswordField resetPasswordRepeat;
    @FXML private Text passwordResetWarning;
    @FXML private TabPane mainTabPane;
    @FXML private Tab dashboard;
    @FXML private Tab tab1;
    @FXML private Tab tab2;
//    @FXML private ComboBox<Theme> themeComboBox;
    @FXML public static MenuItem darkMenuItem;
    @FXML public static MenuItem lightMenuItem;
    @FXML private static MenuItem saveBtn;
    @FXML private static MenuItem SaveAsBtn;
    @FXML private TextField saveLocation;
    @FXML private Button saveMenuBtn;
    @FXML private Button browseBtn;
    @FXML private ComboBox fileTypeMenu;
    @FXML private TextField fileName;
    @FXML private RadioButton saveSavedCoins;
    @FXML private RadioButton saveAllCoins;

    Pattern emailRegex = Pattern.compile("\\b[\\w.%-]+@[\\w]+\\.[A-Za-z]{2,4}\\b"); // nice regex
    //========== Action Handlers ==========
    /**
     * Login button action handler. If the username and password are correct the
     * button redirects users to the main FXMLDocument.fxml page.
     *
     * @param event
     */
    @FXML
    public void login(ActionEvent event) {
        ConnectToDatabase conn = new ConnectToDatabase();
        // If statement for testing purposes
        if (conn.validateLogin(this.username.getText(), this.txtPassword.getText())) {
            this.lblStatus.setText("Login Success");
            conn.setUserOnlineStatus(this.username.getText(), 1);
            uname = this.username.getText();
            System.out.println(uname);
            // After login is successful, you are taken to the main page
            Parent root;
            try {
                if (Tab1Controller.mainPage1 != null) {
                    Tab1Controller.mainPage1.close();
                }
                getCurrentStage().close();
                this.mainStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
                root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                this.scene = new Scene(root);
                this.mainStage.setScene(scene);
                this.mainStage.show();
//                addThemeListener();
                // When You click on "X" will take you back to login page
                mainStage.setOnCloseRequest(new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        try {
                            System.out.println("Closing");
                            FXMLDocumentController.mainStage = new Stage();
                            Tab1Controller.tas.setOnlineStatus(coinTrack.FXMLDocumentController.uname, 0);
                            Parent root = FXMLLoader.load(Tab1Controller.class.getClassLoader().getResource("coinTrack/FXMLLogin.fxml"));
                            FXMLDocumentController.scene = new Scene(root);
                             FXMLDocumentController.mainStage.setScene(FXMLDocumentController.scene);
                             FXMLDocumentController.mainStage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                coinTrack.CoinTrack.newStage.close();
                
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.lblStatus.setText("Login Failed");
        }
        conn.close();
    }

    /**
     * Create new stage to take registration information from a user.
     *
     * @param event
     */
    @FXML
    public void handleRegister(ActionEvent event) {
        //This shows "Register User on the main login screen.
        //this.lblStatus.setText("Register User");
        // After login is successful, you are taken to the main page
        Parent root;
        try {
            // Create registration stage
            getCurrentStage().close();
            this.registerStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.registerStage;
            root = FXMLLoader.load(getClass().getResource("RegisterUserFXML.fxml"));
            this.scene = new Scene(root);
            this.registerStage.setScene(scene);
            this.registerStage.show();
            this.currentStage = this.registerStage;
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Registration page info verification and insertion into database.
     *
     * @param event
     */
    @FXML
    private void handleRegisterSubmit(ActionEvent event) {
        if (DEBUG) {
            System.out.println("register");
        }
        // Check good input and if username exists in DB
        if (checkGoodInput() && usernameAcceptable()) {
            String toEmail3 = this.emailEntry.getText();
            ConnectToDatabase conn = new ConnectToDatabase();
            if (conn.emailExists(toEmail3)) {
            tempUsernameStorage = conn.getUsernameFromEmail(toEmail3);
            if (DEBUG) {
                System.out.println(tempUsernameStorage);
            }
            conn.close();
            _code = 1;
            Email sendMail = new Email(toEmail3, this.tempUsernameStorage, _code);
            Parent root;
            try {
                getCurrentStage().close();
                Stage stage = new Stage();
                FXMLDocumentController.currentStage = stage;
                root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
                this.scene = new Scene(root);
                stage.setScene(this.scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }}
        } else {
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Username or Email already taken");
        }
    }

    /**
     * Handles the event when user clicks the "save" button in the file menu.
     * Creates a new scene where the user specifies where the file will
     * be saved and what it will be called.
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleSave(ActionEvent event) throws IOException {
        if(DEBUG){System.out.println("Saving data");}
        Parent root;
        try {
            Tab1Controller.mainPage1 = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource("coinTrack/SaveFXML.fxml"));
            Scene saveScene = new Scene(root);
            saveStage = new Stage();
            saveStage.setScene(saveScene);
            saveStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Handles the save button click inside the save menu.
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleSaveMenu(ActionEvent event) throws IOException {
        CoinRankApi cri = new CoinRankApi();
        cri.join();
        LinkedList<SingleCoin> coinList = cri.getCoinList();
        if (!this.saveLocation.getText().isEmpty() && !this.fileName.getText().isEmpty()) {
            SaveToDisk save = new SaveToDisk(new File(this.saveLocation.getText()));
            switch (fileTypeMenu.getValue().toString()) {
                case ".txt":
                    save.saveTableAsText(this.fileName.getText(), coinList);
                    break;
                case ".xlsx":
                    save.saveAsExcel(this.fileName.getText(), coinList);
                    break;
                case ".json":
                    save.saveAsJson(this.fileName.getText(), coinList);
                    break;
                default:
                    AlertMessages.showErrorMessage("Missing info", "Please select a file format.");
            }
        } else if (!this.saveLocation.getText().isEmpty() && this.fileName.getText().isEmpty()) {
            SaveToDisk save = new SaveToDisk(new File(saveLocation.getText()));
            switch (fileTypeMenu.getValue().toString()) {
                case ".txt":
                        save.saveTableAsText(coinList);
                    break;
                case ".xlsx":
                        save.saveAsExcel(coinList);
                    break;
                case ".json":
                    save.saveAsJson(coinList);
                    break;
                default:
                    AlertMessages.showErrorMessage("Missing info", "Please select a file format.");
            }
        } else {
            AlertMessages.showErrorMessage("Missing info", "Please fill out all fields.");
        }
        saveStage.close();
    }

    /**
     * Handles the browse button inside the file save menu.
     * Creates a directory browser window allowing the user to select
     * where they want the file saved.
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBrowse(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        System.out.println("get dir");
        this.saveLoc = directoryChooser.showDialog(saveStage);
        this.saveLocation.setText(this.saveLoc.toString());
    }

    /**
     * Verifies the path entered to save file.
     * @param path
     * @return
     */
    public static boolean isPathValid(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            AlertMessages.showErrorMessage("Bad File Path", "The specified file location could not be found.");
            return false;
        }
        return true;
    }

    /**
     * Checks the register user information entered. Returns true if acceptable
     * input, false if not acceptable.
     *
     * @return
     */
    private boolean checkGoodInput() {
        boolean isGood = false;
        if (this.emailEntry.getText().isEmpty()) {
            AlertMessages.showErrorMessage("Register User", "Enter an email address.");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter an email address");
            this.emailEntry.requestFocus();
        } else if (!isEmailValid(this.emailEntry.getText().trim())) {
            AlertMessages.showErrorMessage("Register User", "Email format is not correct.");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Email format is not correct.");
            this.emailEntry.requestFocus();
        } else if (usernameEntry.getText().isEmpty()) {
            AlertMessages.showErrorMessage("Register User", "Enter a username.");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter a username");
            this.usernameEntry.requestFocus();

        } else if (this.passwordEntry.getText().isEmpty()) {
            AlertMessages.showErrorMessage("Register User", "Enter a password.");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter a password");
            this.passwordEntry.requestFocus();

        } else if (isPasswordValid( this.passwordEntry.getText())) {
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText(" Password must be 8 characters long and should contain a digit and an uppercase letter.");
            this.passwordEntry.requestFocus();
        
        } else if (this.passwordRepeatEntry.getText().isEmpty()) {
            AlertMessages.showErrorMessage("Register User", "Repeat your password.");

            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Repeat your password");
            this.passwordRepeatEntry.requestFocus();
        
        } else if (!this.passwordEntry.getText().equals(this.passwordRepeatEntry.getText())) {
            AlertMessages.showErrorMessage("Register User", "Passwords must match.");
            this.passwordRepeatEntry.setPromptText("Passwords must match");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Passwords must match");
            this.passwordRepeatEntry.requestFocus();

        } else {
            isGood = true;
        }
        return isGood;
    }

    /**
     * Add listener to theme menu item.
     */
    private void addThemeListener() {
        theme = new Theme("light");
        scene = this.mainTabPane.getScene();
        darkMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theme = new Theme("dark");
                scene.getStylesheets().add(theme.getTheme());
            }
        });
        lightMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theme = new Theme("light");
                scene.getStylesheets().add(theme.getTheme());
            }
        });
    }
    
    public boolean isEmailValid(String email) {
        Matcher matcher = emailRegex.matcher(email);
        return matcher.find();
    }
    
    public boolean isPasswordValid(String password) {
        if(password.length() < 8) {
            AlertMessages.showErrorMessage("Register User", "Password must be 8 characters long.");
            return true;
        }
        boolean isCapital = false;
        boolean isNumber = false;
        
        for (int i = 0; i < password.length(); i++) {
           char ch = password.charAt(i);
           
           if (Character.isUpperCase(ch)) {
               isCapital = true;
           } else if (Character.isDigit(ch)) {
               isNumber = true;
           }
        }
        
        if (!isCapital || !isNumber) {
            AlertMessages.showErrorMessage("Register User", "Password must contain a digit and an uppercase letter.");
        }
       return !isCapital && !isNumber;
    }

    /**
     * Checks if a username already exists in database.

     * @return
     */
    private boolean usernameAcceptable() {
        // Call DB connection class
        ConnectToDatabase conn = new ConnectToDatabase();
        // Check is username exists in DB
        if (!conn.usernameExists(this.usernameEntry.getText()) && !conn.emailExists(this.emailEntry.getText())) {
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
            return true;
        } else if (conn.usernameExists(this.usernameEntry.getText())){
            AlertMessages.showErrorMessage("Register User", "Username already taken. Try another one.");
            this.registerInfo.setText("Username already taken. Try another one.");
            this.registerInfo.setFill(Color.RED);
            return false;
        } else
            AlertMessages.showErrorMessage("Register User", "Email already taken. Try another one.");
            this.registerInfo.setText("Email already taken. Try another one.");
            this.registerInfo.setFill(Color.RED);
            return false;
    }

    /**

     * Change to the password recovery stage if the forgot password button is
     * clicked.
     *

     * @param event
     */
    @FXML
    private void handleForgotPassword(ActionEvent event) {
        if (DEBUG) {
            System.out.println("Forgot your password already..");
        }
        Parent root;
        try {
            getCurrentStage().close();
            this.forgotPassStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.forgotPassStage;
            root = FXMLLoader.load(getClass().getResource("ForgotPasswordFXML.fxml"));
            this.scene = new Scene(root);
            this.forgotPassStage.setScene(this.scene);
            this.forgotPassStage.show();
            this.currentStage = this.forgotPassStage;
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Change to the password recovery stage if the forgot password button is
     * clicked.
     *
     * @param event
     */
    @FXML
    private void handleForgotUsername(ActionEvent event) {
        if (DEBUG) {
            System.out.println("Forgot your username already..");
        }
        Parent root;
        try {
            getCurrentStage().close();
            this.forgotUserStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.forgotUserStage;
            root = FXMLLoader.load(getClass().getResource("ForgotUsernameFXML.fxml"));
            this.scene = new Scene(root);
            this.forgotUserStage.setScene(this.scene);
            this.forgotUserStage.show();
            this.currentStage = this.forgotUserStage;
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Change to the password recovery stage if the forgot password button is
     * clicked.
     *
     * @param event
     */
    @FXML
    private void handleBackToMain(ActionEvent event) {
        if (DEBUG) {
            System.out.println("Misclicking already..");  // lol Haj
        }
        Parent root;
        try {
            getCurrentStage().close();
        //    System.out.println(getCurrentStage());
            this.mainStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
            root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
            this.scene = new Scene(root);
            this.mainStage.setScene(this.scene);
            this.mainStage.show();
            System.out.println("back to main worked!");
            this.currentStage = this.mainStage;
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
        if (!isEmailValid(this.recoveryEmail.getText().trim())) {
            AlertMessages.showErrorMessage("Forgot Password", "Email format is not correct.");
            this.recoveryEmail.requestFocus();
            return;
        }
        if (conn.emailExists(toEmail) ) {
            tempUsernameStorage = conn.getUsernameFromEmail(toEmail);
            if (DEBUG) {
                System.out.println(tempUsernameStorage);
            }
            conn.close();
            this.code = generateRecoveryCode();
            Email sendMail = new Email(toEmail, this.tempUsernameStorage, this.code);
        } else {
            AlertMessages.showErrorMessage("Forgot Password", "hmm, can't find that email.");
            this.recoveryEmail.requestFocus();
        }
    }

    /**

     * Send an email containing a welcome message
     *
     * @param event
     */
    @FXML
    private void handleForgotUsernameEmail(ActionEvent event){
        String toEmail2 = this.recoveryEmail.getText();
        ConnectToDatabase conn = new ConnectToDatabase();
        if (conn.emailExists(toEmail2)) {
            tempUsernameStorage = conn.getUsernameFromEmail(toEmail2);
            if (DEBUG) {
                System.out.println(tempUsernameStorage);
            }
            conn.close();
            _code = 0;
            Email sendMail = new Email(toEmail2, this.tempUsernameStorage, _code);
        } else {
            this.forgotWarning.setText("hmm, can't find that email");
            this.forgotWarning.setFill(Color.RED);
        }
    }

    /**
     * Send an email containing a code that the user will type into the box to
     * confirm their identity.
     *

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
                this.currentStage = this.resetPassStage;
//                this.forgotPassStage.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.forgotWarning.setText("Incorrect Code");
            this.forgotWarning.setFill(Color.RED);
        }
    }

    /**
     * When the user presses the "reset password" button on

     * PasswordResetFXML.fxml page a connection to the database is made
     * submitting the updated password;
     *

     * @param event
     */
    @FXML
    private void handleResetPassword(ActionEvent event) {
        if (DEBUG) {
            System.out.println("register " + tempUsernameStorage);
        }
        // Make sure new password is not empty
        if (this.resetPassword.getText().isEmpty()) {
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
                this.currentStage = this.mainStage;
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
     *
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
        if (getCurrentStage() != null) {
            getCurrentStage().close();
        }
        if (coinTrack.CoinTrack.newStage != null) {
            coinTrack.CoinTrack.newStage.close();
        }
        if (coinTrack.FXMLDocumentController.getCurrentStage() != null) {
            coinTrack.FXMLDocumentController.getCurrentStage().close();
        }
    }

    /**
     * Gets the main stage so it can be closed from other stages.
     *
     * @return
     */
    public static Stage getMainStage() {
        return FXMLDocumentController.mainStage;
    }

    /**
     * Detect tab changed within Tab 2.
     */
    private void tabChanges() {
        // Tab listener. Detects which graph tab is selected
        mainTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab currentTab) {
                if (DEBUG){System.out.println("Tab Change");}
                if (currentTab == dashboard) {
                    currTab = dashboard;
                } else if (currentTab == tab1) {
                    currTab = tab1;
                } else if (currentTab == tab2) {
                    currTab = tab2;
                }
            }
        });
    }

    /**
     * This is supposed to return the current stage so it can be closed. But I
     * don't think it works...
     *
     * @return
     */
    public static Stage getCurrentStage() {
        return FXMLDocumentController.currentStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FXMLDocumentController.currentStage = coinTrack.CoinTrack.newStage;
//        ConnectToDatabase conn = new ConnectToDatabase();
        /**
         * HOW DO I DO SOMETHING AFTER PROGRAM IS CLOSED !?!? ARGHHH
         */
//        getCurrentStage().setOnCloseRequest(evt
//                -> conn.setUserOnlineStatus(this.uname, 0)
//        );
//        conn.close();
    }

}
