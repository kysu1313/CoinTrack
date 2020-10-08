package tabControllers.assistantControllers;

import java.text.DecimalFormat;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import tabControllers.Tab2Controller;

public class ShowCoordinates extends StackPane {
    
    private Label newLabel;

    public ShowCoordinates(String x, double y) {

        createDataLabel(x, y);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setScaleX(1);
                setScaleY(1);
                getChildren().setAll(Tab2Controller.coordsLabel);
                setCursor(Cursor.NONE);
                toFront();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            }
        });
    }

    private void createDataLabel(String x, double y) {
        DecimalFormat df = new DecimalFormat("0.##");
        final Label label = new Label("(" + x + "; " + df.format(y) + ")");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
//        Tab2Controller.coordsLabel = label;
    }
    
    public Label getLabel() {
        return this.newLabel;
    }
}
