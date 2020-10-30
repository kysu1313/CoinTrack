package models;

/**
 *  Data for the bottom text area.
 *  Gets data for a single market, or all coins.
 * @author Kyle
 */
import org.json.JSONObject;

public class GlobalCoinStats {
    
    private final JSONObject resp;
    private final JSONObject data;
    private final int totalCoins;
    private final int totalMarkets;
    private final int totalExchanges;
    private final int totalMarketCap;
    private final int total24hVolume;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    
    public GlobalCoinStats() {
        
        // Call API connector class
        this.resp = new ConnectToApi("https://coinranking1.p.rapidapi.com/stats",
            "coinranking1.p.rapidapi.com",
            "f20725d47cmshb045d494d70d075p19a1bcjsn0a052376e5cc").getJsonObject();
        this.data = resp.getJSONObject("data");
        this.totalCoins = data.getInt("totalCoins");
        this.totalMarkets = data.getInt("totalMarkets");
        this.totalExchanges = data.getInt("totalExchanges");
        this.totalMarketCap = data.getInt("totalMarketCap");
        this.total24hVolume = data.getInt("total24hVolume");
    }
    
    
    // ========== GETTERS ==========
    
    public int getTotalCoins() {
        return this.totalCoins;
    }
    
    public int getTotalMarkets() {
        return this.totalMarkets;
    }
    
    public int getTotalExchanges() {
        return this.totalExchanges;
    }
    
    public int getTotalMarketCap() {
        return this.totalMarketCap;
    }
    
    public int getTotal24hVolume() {
        return this.total24hVolume;
    }
    
}
