
package coinTrack;

/**
 * The base document controller. This controls the anchor pane that contains the
 * main tab pane.
 *
 * - Kyle
 */

import models.CoinRankApi;
import models.ConnectToDatabase;
import models.Email;
import models.SaveToDisk;
import models.SingleCoin;
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
import controllers.AlertMessages;
import controllers.Tab1Controller;
import static controllers.Tab1Controller.DEBUG;
import static controllers.Tab1Controller.tas;
import controllers.assistantControllers.TabAssistantController;
import controllers.assistantControllers.Theme;
import java.awt.Desktop;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser.ExtensionFilter;
import models.EmailValidation;
import models.User;

/**
 *
 * @author kms
 */
public class FXMLDocumentController implements Initializable {

    public static String uname;
    public static User user;
    public static Tab currTab;
    public static Scene scene;
    private static String tempUsernameStorage;
    private TabAssistantController tas;
    private String code;
    private int _code;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    private static Theme theme;
    private File saveLoc;
    private final ObservableList<Theme> THEMES = FXCollections.
            observableArrayList(new Theme("Dark"), new Theme("Light"));
    private final ObservableList<String> FILE_TYPES = FXCollections.
            observableArrayList(".txt", ".xlsx", ".json");
    private final int PIC_WIDTH = 50;
    private final int PIC_HEIGHT = 30;
    private final int RAND_RANGE = 1000;
    private final String LOGIN_VIEW_LOCATION = "/views/FXMLLogin.fxml";
    private final String RESET_PASSWORD_VIEW_LOCATION = "/views/PasswordResetFXML.fxml";
    private final String REGISTER_VIEW_LOCATION = "/views/RegisterUserFXML.fxml";
    private final String FORGOT_PASSWORD_VIEW = "/views/ForgotPasswordFXML.fxml";
    private final String SAVE_VEW = "/views/SaveFXML.fxml";
    @FXML protected TextField username;
    @FXML protected PasswordField txtPassword;
    @FXML protected Label lblStatus;
    @FXML private Label registerLabel;
    @FXML private TextField emailEntry;
    @FXML private TextField usernameEntry;
    @FXML private PasswordField passwordEntry;
    @FXML private PasswordField passwordRepeatEntry;
    @FXML private Button registerSubmitBtn;
    @FXML public static Stage mainStage;
    @FXML private static Stage forgotPassStage;
    @FXML private static Stage forgotUserStage;
    @FXML private static Stage resetPassStage;
    @FXML private static Stage loginStage;
    @FXML private static Stage registerStage;
    @FXML private static Stage currentStage;
    @FXML private static Stage saveStage;
    @FXML private static Stage browseStage;
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
    @FXML private BorderPane layout;
    @FXML private File file;
    @FXML private FileChooser fileChooser;
    @FXML private ImageView imageView;
    @FXML private Image image;
    @FXML private String path;
    @FXML private CheckBox skip;
    @FXML private final Desktop desktop = Desktop.getDesktop();
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
    @FXML private Label lblWelcomeMessage;
    @FXML private BorderPane layout1;
    Pattern emailRegex = Pattern.compile("\\b[\\w.%-]+@[\\w]+\\.[A-Za-z]{2,4}\\b");

    //========== Action Handlers ==========
    /**
     * Login button action handler. If the username and password are correct the
     * button redirects users to the main FXMLDocument.fxml page.
     *
     * @param event
     */
    @FXML
    public void login(ActionEvent event) {

        uname = this.username.getText();
        String password = this.txtPassword.getText();
        FXMLDocumentController.user = new User(uname, password);
        if (user.validateLogin()) {
            this.lblStatus.setText("Login Success");
            // Create coin list data for user
            if (!FXMLDocumentController.user.getIsDataSet()) {
                FXMLDocumentController.user.createData();
            }
            // Set user as "online" in database
            user.onlineStatus(1);
            this.tas = new TabAssistantController();
            this.tas.setCurrentUser(user);
            // After login is successful, you are taken to the main page
            Parent root;
            try {
                if (Tab1Controller.mainPage1 != null) {
                    Tab1Controller.mainPage1.close();
                }
                getCurrentStage().close();
                this.mainStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
                root = FXMLLoader.load(getClass().getResource("/views/FXMLDocument.fxml"));
                this.scene = new Scene(root);
                this.mainStage.setScene(scene);
                this.mainStage.show();
                // Program will close and user will be logged out
                mainStage.setOnCloseRequest(new EventHandler() {
                    @Override
                    public void handle(Event event) {
                            System.out.println("Closing");
                            user.onlineStatus(0);
                            getCurrentStage().close();
                    }
                });
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
     *
     * @param event
     */
    @FXML
    public void handleRegister(ActionEvent event) {
        // After login is successful, you are taken to the main page
        Parent root;
        try {
            // Create registration stage
            getCurrentStage().close();
            this.registerStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.registerStage;
            root = FXMLLoader.load(getClass().getResource(this.REGISTER_VIEW_LOCATION));
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
            EmailValidation testEmail = new EmailValidation();
            if (testEmail.isEmailInDatabase(toEmail3)) {
                FXMLDocumentController.tempUsernameStorage = testEmail.getAssociatedUsername(toEmail3);
                if (this.DEBUG) {
                    System.out.println(FXMLDocumentController.tempUsernameStorage);
                }
                this._code = 1;
                Email sendMail = new Email(toEmail3, FXMLDocumentController.tempUsernameStorage, _code);
                Parent root;
                try {
                    getCurrentStage().close();
                    Stage stage = new Stage();
                    FXMLDocumentController.currentStage = stage;
                    root = FXMLLoader.load(getClass().getResource(this.LOGIN_VIEW_LOCATION));
                    FXMLDocumentController.scene = new Scene(root);
                    stage.setScene(FXMLDocumentController.scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Please follow on screen instructions");
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
            root = FXMLLoader.load(getClass().getResource(this.SAVE_VEW));
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
        // If fields are empty reject save click.
        if (!this.saveLocation.getText().isEmpty() && !this.fileName.getText().isEmpty()) {
            SaveToDisk save = new SaveToDisk(new File(this.saveLocation.getText()));
            save.createFile(this.fileName.getText(), fileTypeMenu.getValue().toString());
        // If file name field is empty create default filename save.
        } else if (!this.saveLocation.getText().isEmpty() && this.fileName.getText().isEmpty()) {
            SaveToDisk save = new SaveToDisk(new File(saveLocation.getText()));
            save.createFile(this.fileName.getText(), this.fileTypeMenu.getValue().toString());
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
     * Handles the browse button click in the register stage
     * it browse an image.
     * @param event
     */
    @FXML
    private void handleBrowsePicture() throws IOException {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        file = fileChooser.showOpenDialog(browseStage);
        if(file != null){
        path = file.getAbsolutePath();
        }
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
            String toEmail3 = this.emailEntry.getText();
            EmailValidation test = new EmailValidation(toEmail3);
            test.getTest();
        if (this.emailEntry.getText().isEmpty()) {
            AlertMessages.showErrorMessage("Register User", "Enter an email address.");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Enter an email address");
            this.emailEntry.requestFocus();
        } else if ("invalid".equals(test.getTest())) {
            AlertMessages.showErrorMessage("Register User", "Email is not valid.");
            this.registerInfo.setFill(Color.RED);
            this.registerInfo.setText("Email is not valid.");
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
        } else if (User.isPasswordValid(this.passwordEntry.getText())) {
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

        } else if (skip.isSelected() && this.path != null){
            AlertMessages.showErrorMessage("Register User", "Please uncheck the box as you've already chosen a picture");

        }else if (this.path == null && !skip.isSelected()){
            AlertMessages.showErrorMessage("Register User", "Please choose a picture or check the box");

        }else {
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

    /**
     * Checks if a username already exists in database.

     * @return
     */
    private boolean usernameAcceptable() {
        String uname = this.usernameEntry.getText();
        String password = this.passwordEntry.getText();
        String email = this.emailEntry.getText();
        if(skip.isSelected()){
            path = "";
        }
        String imgPath = this.path;
        if (User.usernameAcceptable(uname, password, email, imgPath, this.registerInfo)){
            User user = new User(uname, password);
            FXMLDocumentController.user = user;
            if (!FXMLDocumentController.user.getIsDataSet()) {
                FXMLDocumentController.user.createData();
            }
            return true;
        } else {
            return false;
        }
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
            root = FXMLLoader.load(getClass().getResource(this.FORGOT_PASSWORD_VIEW));
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
            root = FXMLLoader.load(getClass().getResource("/views/ForgotUsernameFXML.fxml"));
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
            System.out.println("Misclicking already..");
        }
        Parent root;
        try {
            getCurrentStage().close();
        //    System.out.println(getCurrentStage());
            this.mainStage = new Stage();
            FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
            root = FXMLLoader.load(getClass().getResource(this.LOGIN_VIEW_LOCATION));
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
        EmailValidation test = new EmailValidation();
        if (test.isEmailInDatabase(toEmail)) {
            tempUsernameStorage = test.getAssociatedUsername(toEmail);
            if (DEBUG) {
                System.out.println(tempUsernameStorage);
            }
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
        User tempUsr = new User();
        if (tempUsr.forgotUsernameEmail(toEmail2)) {
            if (DEBUG) {
                System.out.println("Email sent: " + tempUsernameStorage);
            }
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
            Parent root;
            try {
                getCurrentStage().close();
                FXMLDocumentController.resetPassStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.resetPassStage;
                root = FXMLLoader.load(getClass().getResource(this.RESET_PASSWORD_VIEW_LOCATION));
                FXMLDocumentController.scene = new Scene(root);
                FXMLDocumentController.resetPassStage.setScene(FXMLDocumentController.scene);
                FXMLDocumentController.resetPassStage.show();
                FXMLDocumentController.currentStage = FXMLDocumentController.resetPassStage;
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
//            ConnectToDatabase conn = new ConnectToDatabase();
            if (user != null) {
                user.resetPassword(tempUsernameStorage, this.resetPassword.getText());
            } else {
                User tmpUser = new User();
                tmpUser.resetPassword(tempUsernameStorage, this.resetPassword.getText());
            }
            // Submit changed passwords to the database
//            conn.changePassword(tempUsernameStorage, newPass);
            // Close the connection
//            conn.close();
            this.passwordResetWarning.setText("Success");
            this.passwordResetWarning.setFill(Color.GREEN);
            // Then change the stage back to the login screen
            Parent root;
            try {
                getCurrentStage().close();
                this.mainStage = new Stage();
                FXMLDocumentController.currentStage = FXMLDocumentController.mainStage;
                root = FXMLLoader.load(getClass().getResource(this.LOGIN_VIEW_LOCATION));
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
        int digits = rand.nextInt(this.RAND_RANGE);
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
     * Displaying the profile picture
     */
    private void profilePicture(){
        if(this.layout1 != null){
            User tempUser = new User();
            String pathFromUser = tempUser.getPicturePath(uname);
            if("".equals(pathFromUser)){
                Image image1 = new Image("/styles/bitCoin.jpg");
                this.imageView = new ImageView(image1);
            }else{
                this.image = new Image("file:///" + pathFromUser);
                this.imageView = new ImageView(this.image);
            }
            this.imageView.setFitWidth(this.PIC_WIDTH);
            this.imageView.setFitHeight(this.PIC_HEIGHT);
            this.imageView.setPreserveRatio(true);
            this.layout1.setCenter(this.imageView);
        }
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

    @FXML
    public void logout() {
        System.out.println("Logourt");
         Parent root;
        try {
            coinTrack.FXMLDocumentController.mainStage.close();
            FXMLDocumentController.mainStage = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource("views/FXMLLogin.fxml"));
            this.scene = new Scene(root);
            mainStage.setScene(this.scene);
            mainStage.show();
            this.currentStage = mainStage;

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return current user.
     * @return
     */
    public static User getUser() {
        return user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FXMLDocumentController.currentStage = coinTrack.CoinTrack.newStage;
        profilePicture();
        if (this.lblWelcomeMessage != null) {
            this.lblWelcomeMessage.setText("Hello " + uname);
        }
    }

}
