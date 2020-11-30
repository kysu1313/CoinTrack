package models;
/**
 * This class implements the historical data
 * of a single cryptocurrency.
 *
 * - Kyle
 */

import interfaces.SingleCoinHistoryInterface;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;


public class SingleCoinHistory implements SingleCoinHistoryInterface{

    private final JSONArray OBJECT;
    private final int COIN_ID;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
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
        this.OBJECT = _jar;
        this.COIN_ID = _id;
        if (coinList == null) {
            coinList = new LinkedHashMap<>();
        }
        // Loop that adds historical data to appropriate data vields
        // this.object.length(), commented out b/c too long
        for (int i = 0; i < 5; i++) {
            JSONObject cn = this.OBJECT.getJSONObject(i);
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
    @Override
    public LinkedHashMap<Double, String> getCoinList() {
        return this.coinList;
    }

    /**
     * Returns the id of the specific crypto currency
     * @return coinId
     */
    @Override
    public int getCoinId() {
        return this.COIN_ID;
    }

}
