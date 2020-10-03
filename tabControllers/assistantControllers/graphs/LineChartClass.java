package tabControllers.assistantControllers.graphs;

import coinClasses.CoinHistory;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

/**
 * General class for making / displaying LineCharts.
 * @author Kyle
 */
public class LineChartClass implements interfaces.GraphInterface, interfaces.LineChartInterface{

    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private final LineChart LINE_CHART;
    private final LinkedList<String> LINES;
    private final String TIME_SELECTION;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private LinkedList<XYChart.Data<String, Number>> dataList;
    private LinkedList<XYChart.Series> seriesList;
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

    @Override
    public void addToolTips() {
        this.dataList.forEach((data) -> {
            Tooltip t = new Tooltip(data.getYValue().toString());
            Tooltip.install(data.getNode(), t);
        });
    }
    
    private void createData() {
        // For every coin entered, create a new CoinHistory object.
        for (String line : this.LINES) {
            CoinHistory coinHist = new CoinHistory(0, line, this.TIME_SELECTION);
            this.singleHistoryMap = coinHist.getSingleHistory();
//            XYChart.Series newSeries = new XYChart.Series();
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
//            if (newSeries.getName() == null){newSeries.setName(line);}
            this.seriesList.add(newSeries);
            for (XYChart.Data<Number, String> entry : newSeries.getData()) {
                Tooltip t = new Tooltip(entry.getExtraValue().toString());
                Tooltip.install(entry.getNode(), t);
            }
            this.lineChartData.add(newSeries);
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
    
}
