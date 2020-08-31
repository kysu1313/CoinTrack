
package coinClasses;

import interfaces.CoinRankInterface;
import interfaces.SingleCoinInterface;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class CoinRankApi implements CoinRankInterface{
    
    HttpResponse<JsonNode> response;
    JSONObject data;
    JSONObject stats;
    JSONObject base;
    JSONArray coins;
    LinkedList<SingleCoin> coinList;
    LinkedHashMap<String, String> namePrice;

    
    /**
     * Constructor,
     * Automatically makes a call to the "coins" url 
     * and parses the data into easily usable data;
     */
    public CoinRankApi() {
        try {
            this.response = Unirest.get("https://coinranking1.p.rapidapi.com/coins")
                    .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                    .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                    .asJson();
            
            JSONObject coinRank = new JSONObject(response.getBody().toString());
            
            data = coinRank.getJSONObject("data");
            stats = data.getJSONObject("stats");
            coins = data.getJSONArray("coins");
            
//            System.out.println(stats.getInt("total"));
        } catch (UnirestException ex) {
            Logger.getLogger(CoinRankApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Creates a LinkedList of each coin in the 
     * "coins" json array.
     * @param jar
     * @return 
     */
    public LinkedList<SingleCoin> getCoinList(JSONArray jar){
        
        
        for (int i = 0; i < jar.length(); i++) {
            coinList.add(new SingleCoin(jar.getJSONObject(i)));
        }
     
        return coinList;
    }
    
    public LinkedHashMap<String, String> getNamePrice() {
        
        for (int i = 0; i < coinList.size(); i++) {
            String name = coinList.get(i).getName();
            String price = coinList.get(i).getPrice();
            namePrice.put(name, price);
        }
        return namePrice;
    }

    
    // ========== GETTERS ==========
    
    @Override
    public int getTotal() {
        return stats.getInt("total");
    }

    @Override
    public int getOffset() {
        return stats.getInt("offset");
    }

    @Override
    public int getLimit() {
        return stats.getInt("limit");
    }

    @Override
    public String getOrder() {
        return stats.getString("order");
    }

    @Override
    public String getBase() {
        return stats.getString("base");
    }

    @Override
    public int getTotalMarkets() {
        return stats.getInt("totalMarkets");
    }

    @Override
    public int getTotalExchanges() {
        return stats.getInt("totalExchanges");
    }

    @Override
    public int getTotalMarketCap() {
        return stats.getInt("totalMarketCap");
    }

    @Override
    public int getTotal24hVolume() {
        return stats.getInt("total24hVolume");
    }
    
}
