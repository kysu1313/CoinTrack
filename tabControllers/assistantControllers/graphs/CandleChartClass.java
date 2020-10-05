/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers.graphs;

import coinClasses.AlphaVantage;
import interfaces.GraphInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Toolkit;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.Pane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

/**
 *
 * @author Kyle
 */
public class CandleChartClass implements GraphInterface {
    
    public CandleChartClass (Pane _pane) throws ParseException {
        AlphaVantage av = new AlphaVantage("BTC");
        List<OHLCDataItem> dataItems = av.getOHLCData();
        
        OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
        OHLCDataset dataset = new DefaultOHLCDataset("MSFT", data);

        // 2. Create chart
        JFreeChart chart = ChartFactory.createCandlestickChart("MSFT", "Time", "Price", dataset, false);

        // 3. Set chart background
        chart.setBackgroundPaint(Color.white);

        // 4. Set a few custom plot features
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE); // light yellow = new Color(0xffffe0)
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        ((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
        
        /**
         * embed swing node in javafx
         */
        final SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode, chart, _pane);
        _pane.getChildren().add(swingNode);
        ((CandlestickRenderer) plot.getRenderer()).setDrawVolume(false);
        

        // 7. Create and display full-screen JFrame
//        JFrame myFrame = new JFrame();
//        myFrame.setResizable(true);
//        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        myFrame.add(new ChartPanel(chart), BorderLayout.CENTER);
//        Toolkit kit = Toolkit.getDefaultToolkit();
//        Insets insets = kit.getScreenInsets(myFrame.getGraphicsConfiguration());
//        Dimension screen = kit.getScreenSize();
//        myFrame.setSize((int) (screen.getWidth() - insets.left - insets.right), (int) (screen.getHeight() - insets.top - insets.bottom));
//        myFrame.setLocation((int) (insets.left), (int) (insets.top));
//        myFrame.setVisible(true);
    }
    
    private void createSwingContent(final SwingNode swingNode, JFreeChart chart, Pane _pane) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 JPanel panel = new JPanel();
                 panel.add(new ChartPanel(chart), BorderLayout.CENTER);
//                 panel.setLayout();
                 panel.setBounds(184, 77, 638, 399);
                 swingNode.setContent(panel);
             }
         });
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