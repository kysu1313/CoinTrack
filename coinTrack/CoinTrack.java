package coinTrack;

/**
 * SMS Messaging Desktop Application
 * 
 * Created by team: Not Fast, Just Furious
 * Members: Kyle, Haj, Parth
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author kms
 */
public class CoinTrack extends Application {
    
    public static Stage newStage;
    protected Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception {
        newStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args); 
    }
    
}
