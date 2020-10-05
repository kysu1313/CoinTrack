/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers.graphs;

import coinClasses.CoinRankApi;
import coinClasses.SingleCoin;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Kyle
 */
public class PieChartClass implements interfaces.GraphInterface{

    private CoinRankApi coinList;
    private LinkedList<SingleCoin> singleCoinList;
    private int pieChartCoins;
    private ComboBox<String> comboBox;
    private ObservableList<PieChart.Data> pieChartData;
    private PieChart pieChart;

    /**
     * Creates a pie chart from the given list of SingleCoins.
     * This is used in Tab 2.
     *
     * @param _coinList
     * @param _pieChartCoins
     * @param _comboBox
     * @param _pieChartData
     * @param _pieChart
     */
    public PieChartClass(CoinRankApi _coinList, int _pieChartCoins, ComboBox<String> _comboBox, ObservableList<PieChart.Data> _pieChartData, PieChart _pieChart) {
        this.coinList = _coinList;
        this.singleCoinList = _coinList.getCoinList();
        this.pieChartCoins = _pieChartCoins;
        this.comboBox = _comboBox;
        this.pieChartData = _pieChartData;
        this.pieChart = _pieChart;
        createFullGraph();
    }

    /**
     * Creates a pie chart from given list of SingleCoins .
     * This is used in Tab 3 "dashboard".
     *
     * @param _coinList
     * @param _pieChart
     */
    public PieChartClass(LinkedList<SingleCoin> _coinList, PieChart _pieChart) {
        this.singleCoinList = _coinList;
        this.pieChart = _pieChart;
        createPartialGraph();
    }

    @Override
    public void displayGraph() {
        this.pieChart.setData(this.pieChartData);
    }

    @Override
    public void colorGraph() {
        // Not implemented yet
    }

    @Override
    public void alternateColors(String color1, String color2) {
        // Not implemented yet
    }

    private void createFullGraph() {
        // Make sure the thread is finished
//        this.coinList.join();
        LinkedList<SingleCoin> temp = this.coinList.getSortedCoinList();
        this.pieChartCoins = Integer.parseInt(this.comboBox.getValue());
        // Loops over SingleCoin list and adds data to pieChart
        for (int i = 0; i <= this.pieChartCoins - 1; i++) {
            SingleCoin coin = temp.get(i);
            double price = Double.parseDouble(coin.getPrice());
            // Allow 5 decimal places
            double rounded = (double) Math.round(price * 100000d) / 100000d;
            this.pieChartData.add(new PieChart.Data(coin.getName(), rounded));
        }
    }
    
    private void createPartialGraph() {
        this.pieChartData = FXCollections.observableArrayList();
        // Loops over SingleCoin list and adds data to pieChart
        for (int i = 0; i <= this.singleCoinList.size() - 1; i++) {
            SingleCoin coin = this.singleCoinList.get(i);
            double price = Double.parseDouble(coin.getPrice());
            // Allow 5 decimal places
            double rounded = (double) Math.round(price * 100000d) / 100000d;
            this.pieChartData.add(new PieChart.Data(coin.getName(), rounded));
        }
    }

    @Override
    public void scaleGraph() {
        // TODO: implement
    }

    @Override
    public LinkedList<String> getElements() {
        LinkedList<String> temp = new LinkedList<>();
        this.singleCoinList.forEach(item -> {
            temp.add(item.getName());
        });
        return temp;
    }

}
