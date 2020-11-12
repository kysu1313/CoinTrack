package models.viewModels;

import models.CoinHistory;
import java.awt.Color;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;

/**
 * General class for making / displaying LineCharts.
 * @author Kyle
 */
public class LineChartClass implements interfaces.GraphInterface, interfaces.LineChartInterface{

    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    private final LineChart LINE_CHART;
    private final LinkedList<String> LINES;
    private final String TIME_SELECTION;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private LinkedList<XYChart.Data<String, Number>> dataList;
    private LinkedList<XYChart.Series<Number, String>> seriesList;
    private ObservableList<XYChart.Series<Number, String>> lineChartData;

    /**
     * CONSTRUCTOR.
     * @param _lineChart: line chart to be graphed.
     * @param _lines: names of coins to be added to graph.
     * @param _timeSelection: 24h, 7d, 30d, 1y, 5y.
     */
    public LineChartClass(LineChart _lineChart, LinkedList<String> _lines, String _timeSelection) {
        this.LINE_CHART = _lineChart;
        this.LINES = _lines;
        this.TIME_SELECTION = _timeSelection;
        this.singleHistoryMap = new LinkedHashMap<>();
        this.dataList = new LinkedList<>();
        this.seriesList = new LinkedList<>();
        this.lineChartData = FXCollections.observableArrayList();
        createData();
    }

    @Override
    public void displayGraph() {
        this.LINE_CHART.setTitle("Viewing the past " + this.TIME_SELECTION + " of: " + this.LINES.peek());
        this.LINE_CHART.setData(this.lineChartData);
        addToolTips(this.seriesList);
    }

    @Override
    public void colorGraph() {
        // Not implemented yet
    }

    @Override
    public void alternateColors(String color1, String color2) {
        // Not implemented yet
    }

    @Override
    public void addCoin(String _coin) {
        this.LINES.add(_coin);
        createData();
    }

    @Override
    public void removeCoin(String _coin) {
        this.LINE_CHART.getData().clear();
        for (int i = 0; i < this.LINES.size(); i++) {
            if (this.LINES.get(i).equalsIgnoreCase(_coin)) {
                this.LINES.remove(i);
            }
        }
        createData();
    }

    @Override
    public void clearChart() {
//        this.lineChart.getData().clear();
        this.LINE_CHART.layout();
//        this.lineChartData.clear();
        this.seriesList.forEach((entry) -> {
            entry.getData().removeAll();
        });
    }
    
    private void createData() {
        // For every coin entered, create a new CoinHistory object.
        for (String line : this.LINES) {
            CoinHistory coinHist = new CoinHistory(0, line, this.TIME_SELECTION);
            this.singleHistoryMap = coinHist.getSingleHistory();
            XYChart.Series<Number, String> newSeries = new XYChart.Series();
            double previousPrice = 0;
            // Create the datapoints for the line chart.
            this.singleHistoryMap.entrySet().forEach((Map.Entry<Double, String> entry) -> {
                long tempLong = Long.parseLong(entry.getValue());
                Date d = new Date(tempLong);
                String date = "" + d;
                double price = entry.getKey();
                XYChart.Data item = new XYChart.Data(date, price);
                item.setExtraValue(line + ": " + price);
                newSeries.getData().add(item);
                newSeries.getNode();
                this.dataList.add(new XYChart.Data(date, price));
            });
            this.seriesList.add(newSeries);
            this.lineChartData.add(newSeries);
        }
    }

    /**
     * Add tool tips to each node on the line chart.
     * @param _series
     */
    private void addToolTips(LinkedList<XYChart.Series<Number, String>> _series) {
        for (XYChart.Series<Number, String> series : _series) {
            for (XYChart.Data<Number, String> entry : series.getData()) {
                Tooltip t = new Tooltip(entry.getExtraValue().toString());
                Tooltip.install(entry.getNode(), t);
            }
        }
    }

    @Override
    public void scaleGraph() {
        // TODO: implement
    }

    @Override
    public LinkedList<String> getElements() {
        return this.LINES;
    }
    
    public Label getLineChartCoords(LineChart<String, Number> lineChart) {
        final Axis<String> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();
        final Label cursorCoords = new Label();
        final Node chartBackground = lineChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }
        chartBackground.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        chartBackground.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "(%.2f, %.2f)",
                            xAxis.getValueForDisplay(mouseEvent.getX()),
                            yAxis.getValueForDisplay(mouseEvent.getY())
                    )
            );
        });
        chartBackground.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });
        xAxis.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        xAxis.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "x = %.2f",
                            xAxis.getValueForDisplay(mouseEvent.getX())
                    )
            );
        });
        xAxis.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });

        yAxis.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        yAxis.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "y = %.2f",
                            yAxis.getValueForDisplay(mouseEvent.getY())
                    )
            );
        });
        yAxis.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });

        return cursorCoords;
    }
    
}
