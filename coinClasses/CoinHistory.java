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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class CoinHistory implements CoinHistoryInterface{
    
    private LinkedList<SingleCoinHistory> history;
    private LinkedHashMap<String, Integer> historyMap;
    private double change;

    public CoinHistory() {

        // Increment the coin "id" for each call. Not sure how many coins there are though.
        for (int i = 0; i < 70; i++) {
            try {
                String url = "https://coinranking1.p.rapidapi.com/coin/" + i + "/history/7d";
                HttpResponse<JsonNode> response = Unirest.get(url)
                        .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                        .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                        .asJson();
                
                // For each response, extract body, data, and change
                JSONObject resp = new JSONObject(response.getBody().toString());
                JSONObject data = resp.getJSONObject("data");
                change = data.getDouble("change");

                // JSONArray containing price history for the particular coin.
                JSONArray histArr = data.getJSONArray("history");
                
                // This creates the individual SingleCoinHistory objects
                // And creates a LinkedHashMap for coin prices and dates.
                makeSingleCoins(histArr);
                
                try {
                    TimeUnit.SECONDS.sleep((long) 0.05);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CoinHistory.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (UnirestException ex) {
                Logger.getLogger(CoinHistory.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    private void makeSingleCoins(JSONArray jar) {
        
        for (int i = 0; i < jar.length(); i++) {
            SingleCoinHistory single = new SingleCoinHistory(jar.getJSONObject(i));
            this.history.add(single);
            this.historyMap.put(single.getPrice(), single.getTimeStamp());
        }
    }

    @Override
    public LinkedHashMap<String, Integer> getPriceDate() {
        return this.historyMap;
    }

    @Override
    public double getChange() {
        return this.change;
    }

    @Override
    public LinkedList<SingleCoinHistory> getHistory() {
        return this.history;
    }


}
