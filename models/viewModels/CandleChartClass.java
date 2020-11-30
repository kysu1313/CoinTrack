package models.viewModels;

import models.AlphaVantage;
import interfaces.GraphInterface;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.layout.Pane;

import com.BarData;
import com.CandleStickChart;
import com.DecimalAxisFormatter;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.stage.Stage;

//    NOT IMPLEMENTED PROPERLY YET

/**
 * This creates a candle stick chart using coin historical data.
 * @author Kyle
 */
public class CandleChartClass implements GraphInterface {

    public CandleChartClass (Pane _pane) throws ParseException {

        /**
         * Times are wrong.
         * Might scrap the candle chart...
         */

        AlphaVantage av = new AlphaVantage("BTC");
        List<BarData> barData = av.getBarData();
        CandleStickChart candleStickChart = new CandleStickChart("BTC", barData);
        _pane.getChildren().add(candleStickChart);
        candleStickChart.setYAxisFormatter(new DecimalAxisFormatter("#000.00"));
    }

    @Override
    public void displayGraph() {
        // Not implemented
    }

    @Override
    public void colorGraph() {
        // Not implemented
    }

    @Override
    public void scaleGraph() {
        // Not implemented
    }

    @Override
    public LinkedList<String> getElements() {
       // Not implemented
       return null;
    }

    @Override
    public void alternateColors(BarChart _barChart, String _upColor, String _downColor) {
       // Not implemented
    }
}