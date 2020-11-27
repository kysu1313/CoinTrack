package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class is used to test the method called getStats in CoinRankApi class.
 * @author Kyle
 */
public class TestClass {

    private LinkedList<SingleCoin> testList1;
    private LinkedList<SingleCoin> testList2;
    private LinkedList<SingleCoin> testList3;
    private LinkedList<SingleCoin> testList4;
    private LinkedList<SingleCoin> testList5;
    private double RANGE_MIN_POS = 1.00;
    private double RANGE_MIN_NEG = -100.00;
    private double RANGE_MAX_POS = 100.00;
    // Edge case: even length list
    private final String[] testPrices1 = {"0.0001", "0.10", "5.002", "100", "17.9", "11.11"};
    // Edge case: empty list
    private final String[] testPrices2 = {};
    // Edge Case: two lowest values
    private final String[] testPrices3 = {"9.53", "0.00", "1900.32245", "100", "0.000012", "13.665", "0.00"};
    private CoinRankApi cri;


    public TestClass() {
        this.cri = new CoinRankApi();
        this.cri.start();
        this.cri.join();
        this.testList2 = new LinkedList();
        this.testList3 = new LinkedList();
        this.testList4 = new LinkedList();
        this.testList5 = new LinkedList();
    }

    /**
     * Test api list.
     */
    public void createList1() {
        this.testList1 = this.cri.getCoinList();
        System.out.println("Test number of coins from api");
        if (this.testList1.size() == 49){
            System.out.println("Pass");
        } else {
            System.out.println("Fail");
        }
    }

    /**
     * Test random list
     */
    public void createList2() {

        for (int i = 0; i < 25; i++) {
            Random r = new Random();
            String rand = Double.toString(this.RANGE_MIN_POS + (this.RANGE_MAX_POS - this.RANGE_MIN_POS) * r.nextDouble());
            SingleCoin coin = new SingleCoin(rand);
            this.testList2.add(coin);
        }
    }

    /**
     * Create custom lists.
     */
    public void createCustomLists() {
        for(String item : testPrices1) {
            SingleCoin coin = new SingleCoin(item);
            this.testList3.add(coin);
        }
        for(String item : testPrices2) {
            SingleCoin coin = new SingleCoin(item);
            this.testList4.add(coin);
        }
        for(String item : testPrices3) {
            SingleCoin coin = new SingleCoin(item);
            this.testList5.add(coin);
        }
    }

    public void testCustomList1() {
        HashMap<String, Double> map1 = this.cri.getStats(this.testList3);
        System.out.println(map1.get("Median"));

        System.out.println("Highest Price: ");
        if (map1.get("Highest Price") == 100){
            System.out.println("Pass (T)");
        } else {
            System.out.println("Fail (F)");
        }

        System.out.println("Lowest Price: ");
        if (map1.get("Lowest Price") == 0.0001){
            System.out.println("Pass (T)");
        } else {
            System.out.println("Fail (F)");
        }

        System.out.println("Range: ");
        if (map1.get("Range") == 99.9999){
            System.out.println("Pass (T)");
        } else {
            System.out.println("Fail (F)");
        }

        System.out.println("Mean: ");
        if (map1.get("Mean") == 22.35201667){
            System.out.println("Pass (T)");
        } else {
            System.out.println("Fail (F)");
        }

        System.out.println("Total: ");
        if (map1.get("Total") == 134.1121){
            System.out.println("Pass (T)");
        } else {
            System.out.println("Fail (F)");
        }
    }

}
