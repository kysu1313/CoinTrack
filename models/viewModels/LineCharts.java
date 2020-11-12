/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.viewModels;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 *
 * @author Kyle
 */
public class LineCharts {
    
    
    private XYChart.Series<Number,Number> hourDataSeries; 
    private NumberAxis xAxis;
    Timeline animation;
    private double hours = 0; 
    private double timeInHours = 0;
    private double minutes = 0;
    private double prevY = 10;
    private double y = 10; 
    
    public LineCharts () {
        // timeline to add new data every 60th of a second
    animation = new Timeline();
    animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
            // 6 minutes data per frame
            for(int count = 0; count < 6; count++) {
                nextTime();
                plotTime();
            }
        }
    }));
    animation.setCycleCount(Animation.INDEFINITE);
    xAxis = new NumberAxis(0, 24, 3);
    final NumberAxis yAxis = new NumberAxis(0, 100, 10);
    final LineChart<Number,Number> lc = new LineChart<Number,Number>(xAxis, yAxis);

    lc.setCreateSymbols(false);
    lc.setAnimated(false);
    lc.setLegendVisible(false);
    lc.setTitle("ACME Company Stock");

    xAxis.setLabel("Time");
    xAxis.setForceZeroInRange(false);
    yAxis.setLabel("Share Price");
    yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$", null));

    hourDataSeries = new XYChart.Series<Number,Number>();
    hourDataSeries.setName("Hourly Data");
    hourDataSeries.getData().add(new XYChart.Data<Number,Number>(timeInHours, prevY));
    lc.getData().add(hourDataSeries);

    
    }
    
    private void plotTime() {
        if ((timeInHours % 1) == 0) {
            // change of hour
            double oldY = y;
            y = prevY - 10 + (Math.random() * 20);
            prevY = oldY;
            while (y < 10 || y > 90) y = y - 10 + (Math.random() * 20);
            hourDataSeries.getData().add(new XYChart.Data<Number, Number>(timeInHours, prevY));
            // after 25hours delete old data
            if (timeInHours > 25) hourDataSeries.getData().remove(0);
            // every hour after 24 move range 1 hour
            if (timeInHours > 24) {
                xAxis.setLowerBound(xAxis.getLowerBound() + 1);
                xAxis.setUpperBound(xAxis.getUpperBound() + 1);
            }
        }
    } 

    private void nextTime() {
        if (minutes == 59) {
            hours++;
            minutes = 0;
        } else {
            minutes++;
        }
        timeInHours = hours + ((1d/60d) * minutes);
    }
    

    
    
}
