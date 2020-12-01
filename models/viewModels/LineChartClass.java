package models.viewModels;

import models.CoinHistory;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 * General class for making / displaying LineCharts.
 * @author Kyle
 */
public class LineChartClass implements interfaces.GraphInterface, interfaces.LineChartInterface{

    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    private final LineChart LINE_CHART;
    private LinkedList<String> LINES;
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

    /**
     * Display the line chart to screen.
     */
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

    /**
     * Add specified coin data to the graph.
     * @param _coin
     */
    @Override
    public void addCoin(String _coin) {
        this.LINES.add(_coin);
        createData();
    }

    /**
     * Remove specified coin from graph.
     * @param _coin
     */
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

    /**
     * Clear all coin data from chart.
     */
    @Override
    public void clearChart() {
        this.LINE_CHART.layout();
        this.seriesList.forEach((entry) -> {
            entry.getData().removeAll();
        });
    }

    /**
     * Clean data and add it to the lineChart series.
     */
    private void createData() {
        Instant instant = Instant.now();
        long timestamp = instant.toEpochMilli();
        // For every coin entered, create a new CoinHistory object.
        for (String line : this.LINES) {
            CoinHistory coinHist = new CoinHistory(0, line, this.TIME_SELECTION);
            if (coinHist.checkValidData()) {
                // If response is valid, get all data
                coinHist.getData();
                this.singleHistoryMap = coinHist.getSingleHistory();
                XYChart.Series<Number, String> newSeries = new XYChart.Series();
                double previousPrice = 0;
                // Create the datapoints for the line chart.
                this.singleHistoryMap.entrySet().forEach((Map.Entry<Double, String> entry) -> {
                    long tempLong = Long.parseLong(entry.getValue());
                    Date d = new Date(timestamp - tempLong);
                    String date = "" + d;
                    if (DEBUG){
                        System.out.println(tempLong);
                        System.out.println(date);
                    }
                    double price = entry.getKey();
                    XYChart.Data item = new XYChart.Data(date, price);
                    item.setExtraValue(line + ": " + price);
                    newSeries.getData().add(item);
                    Node node = newSeries.getNode();
                    // Add XYChart node to list. Used to add tooltips
                    this.dataList.add(new XYChart.Data(date, price));
                });
                this.seriesList.add(newSeries);
                this.lineChartData.add(newSeries);
            }
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

    // ========== SETTERS ========== //

    public void setLines(LinkedList<String> _lines) {
        this.LINES = _lines;
    }

    // ========== GETTERS ========== //

    public LinkedList<String> getLines() {
        return this.LINES;
    }

    @Override
    public void scaleGraph() {
        // TODO: implement
    }

    /**
     * Get the lines currently on the chart.
     * @return
     */
    @Override
    public LinkedList<String> getElements() {
        return this.LINES;
    }

    /**
     * Get coordinates of the line chart.
     * @param lineChart
     * @return
     */
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

    @Override
    public void alternateColors(BarChart _barChart, String _upColor, String _downColor) {
        // Not implemented yet
    }
}
