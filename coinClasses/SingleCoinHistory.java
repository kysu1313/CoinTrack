/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class SingleCoinHistory {
    
    private JSONArray object;
    private int coinId;
    private LinkedHashMap<Double, Date> coinList;

    /**
     * Constructor.
     * @param job 
     */
    public SingleCoinHistory(JSONArray jar) {
        object = jar;
        JSONObject tmp = object.getJSONObject(0);
        coinId = tmp.getInt("id");
        
        for (int i = 0; i < object.length(); i++) {
            JSONObject cn = object.getJSONObject(i);
            double price = Double.parseDouble(cn.getString("price"));
            Date date = new Date(cn.getInt("timestamp"));
            coinList.put(price, date);
        }
    }
    
    public LinkedHashMap<Double, Date> getCoinList() {
        return this.coinList;
    }
    
    public int getCoinId() {
        return this.coinId;
    }
    
}
