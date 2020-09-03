/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
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
        
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://coinranking1.p.rapidapi.com/stats")
                    .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                    .header("x-rapidapi-key", "f20725d47cmshb045d494d70d075p19a1bcjsn0a052376e5cc")
                    .asJson();
            
            this.resp = new JSONObject(response.getBody().toString());
            this.data = resp.getJSONObject("data");
            
            this.totalCoins = data.getInt("totalCoins");
            this.totalMarkets = data.getInt("totalMarkets");
            this.totalExchanges = data.getInt("totalExchanges");
            this.totalMarketCap = data.getInt("totalMarketCap");
            this.total24hVolume = data.getInt("total24hVolume");
            
        } catch (UnirestException ex) {
            Logger.getLogger(GlobalCoinStats.class.getName()).log(Level.SEVERE, null, ex);
        }

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
