package coinClasses;
/**
 * This class implements the historical data
 * of a single crypto currency.
 * 
 * - Kyle
 */

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;


public class SingleCoinHistory {
    
    private JSONArray object;
    private int coinId;
    private LinkedHashMap<Double, String> coinList;

    /**
     * Constructor.
     * This simply takes the JSONArray and id, 
     * and saves the price and timestamp, 
     * and adds the data to a linkedHashMap
     * 
     * @param _jar
     * @param _id
     */
    public SingleCoinHistory(JSONArray _jar, int _id) {
        this.object = _jar;
        this.coinId = _id;
        if (coinList == null) {
            coinList = new LinkedHashMap<>();
        }
        // Loop that adds historical data to appropriate data vields
        // this.object.length(), commented out b/c too long
        for (int i = 0; i < 5; i++) {
            JSONObject cn = this.object.getJSONObject(i);
            double price = Double.parseDouble(cn.getString("price"));
            String date = Integer.toString(cn.getInt("timestamp"));
            this.coinList.put(price, date);
        }
    }
    
    // ========== GETTERS ==========
    
    /**
     * Returns the LinkedHashMap of price and date
     * @return coinList
     */
    public LinkedHashMap<Double, String> getCoinList() {
        return this.coinList;
    }
    
    /**
     * Returns the id of the specific crypto currency
     * @return coinId
     */
    public int getCoinId() {
        return this.coinId;
    }
    
}
