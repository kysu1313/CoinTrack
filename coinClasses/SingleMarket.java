package coinClasses;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class SingleMarket {
    
    JSONObject arr;
    
    private int id;
    private String uuid;
    private int rank;
    private String baseSymbol;
    private String sourceName;
    private String sourceIconUrl;
    private int tickerCreatedAt;
    private int tickerClose;
    private double tickerBaseVolume;
    private double marketShare;
    private double price;
    private double volume;
    
    public SingleMarket(JSONObject jar) {
        arr = jar;
        fillValues();
    }

    private void fillValues() {
        
    }
    
    public void setId() {
        this.id = arr.getInt("id");
    }
    
    public void setUuid() {
        this.uuid = arr.getString("uuid");
    }
    
    public void setRank() {
        this.rank = arr.getInt("rank");
    }
    
    public void setBaseSymbol() {
        this.baseSymbol = arr.getString("baseSymbol");
    }
    
}
