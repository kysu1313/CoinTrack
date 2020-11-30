package interfaces;

import java.util.LinkedList;
import javafx.scene.chart.BarChart;

/**
 * Interface for all graph types.
 * @author Kyle
 */
public interface GraphInterface {

    public void displayGraph();
    public void colorGraph();
    public void alternateColors(BarChart _barChart, String _upColor, String _downColor);
    public void scaleGraph();
    public LinkedList<String> getElements();

}
