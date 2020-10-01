/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.LinkedList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Kyle
 */
public interface LineChartInterface {
    
    public void addCoin(LinkedList<String> lines);
    public void removeCoin(LinkedList<String> lines);
    public void clearChart();
    public void addToolTips(LinkedList<XYChart.Data<String, Number>> dataLst);
    
}
