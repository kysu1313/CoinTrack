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
    
    public void addCoin(String _coin);
    public void removeCoin(String _coin);
    public void clearChart();
    public void addToolTips();
    
}
