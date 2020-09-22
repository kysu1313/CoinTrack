package coinClasses;

import org.json.JSONObject;

/**
 *  Data for the bottom text area.
 * I'm not sure if this is for a single coin, single market, or all coins.
 * @author Kyle
 */
public class GlobalCoinStats {
    
    private JSONObject resp;
    private JSONObject data;
    private int totalCoins;
    private int totalMarkets;
    private int totalExchanges;
    private int totalMarketCap;
    private int total24hVolume;
    
    public GlobalCoinStats() {
        
        // Call API connector class
        this.resp = new ConnectToApi("https://coinranking1.p.rapidapi.com/stats",
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
