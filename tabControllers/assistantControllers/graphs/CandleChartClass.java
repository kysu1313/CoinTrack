/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers.graphs;

import coinClasses.AlphaVantage;
import interfaces.GraphInterface;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.layout.Pane;

import com.BarData;
import com.CandleStickChart;
import com.DecimalAxisFormatter;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kyle
 */
public class CandleChartClass implements GraphInterface {
    
    public CandleChartClass (Pane _pane) throws ParseException {
        
        /**
         * Times are wrong.
         * 
         * Do I need to change line 61 in CandleStickChart?????
         * 
         * Look at this tmwr
         */
        
        AlphaVantage av = new AlphaVantage("BTC");
        
        List<BarData> barData = av.getBarData();
        CandleStickChart candleStickChart = new CandleStickChart("S&P 500 Index", barData);
        Scene scene = new Scene(candleStickChart);
        
        scene.getStylesheets().add("/styles/CandleStickChartStyles.css");
        
        Stage stage = new Stage();
        stage.setTitle("BTC");
        stage.setScene(scene);
        stage.show();
        System.out.println("candle test");
        candleStickChart.setYAxisFormatter(new DecimalAxisFormatter("#000.00"));
    }

    @Override
    public void displayGraph() {
        
    }

    @Override
    public void colorGraph() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alternateColors(String color1, String color2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void scaleGraph() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> getElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}