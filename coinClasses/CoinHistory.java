package coinClasses;
/**
 * This class accesses the Coin History api. 
 * The main function of the class is to 
 * loop through the url's in the api call
 * to retrieve historical data on each coin.
 * 
 * - Kyle
 */

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoinHistory implements Runnable, CoinHistoryInterface{
    
    private LinkedList<SingleCoin> history;
    private LinkedHashMap<String, Integer> historyMap;
    private LinkedHashMap<Double, String> singleHistoryMap;
    private double change;
    private Thread t;
    private SingleCoin single;
    private int coinId;
    private HashMap<Integer, LinkedHashMap<Double, String>> allCoins;
    private String time;

    public CoinHistory() {
        start();
    }
    
    /**
     * This constructor makes a single api call using
     * the provided coin id. 
     * @param _id 
     */
    public CoinHistory(int _id, String _name, String _timeFrame) {
        singleHistoryMap = new LinkedHashMap<>();
        coinId = _id;
        time = _timeFrame;
        if (!_name.equals("")){
            ParseCoinName newName = new ParseCoinName(_name);
            coinId = newName.getId();
        }
        try {
            String url = "https://coinranking1.p.rapidapi.com/coin/" + coinId + "/history/" + time + "";
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                    .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                    .asJson();
            // Extract body, data, and change
            JSONObject resp = new JSONObject(response.getBody().toString());
            JSONObject data = resp.getJSONObject("data");
            change = data.getDouble("change");
            // JSONArray containing price history for the particular coin.
            JSONArray histArr = data.getJSONArray("history");
            // Create the individual SingleCoinHistory object
            // Pass the JSONArray and the id of the coin.
            single = new SingleCoin(histArr, _id);
            // Loop through the response adding data to a hashMap
            for (int i = 0; i < histArr.length(); i++) {
                JSONObject temp = histArr.getJSONObject(i);
                Double price = temp.getDouble("price");
                String date = "" + temp.getInt("timestamp");
                singleHistoryMap.put(price, date);
            }

        } catch (UnirestException ex) {
            Logger.getLogger(CoinHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        history = new LinkedList<>();
        // Increment the coin "id" for each call. Not sure how many coins there are though.
        for (int i = 1; i < 70; i++) {
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

                
                /**
                 * More efficient way to save single coin history?
                 */
//                LinkedHashMap<Double, String> temp = new LinkedHashMap<>();
//                for (int x = 0; x < histArr.length(); x++) {
//                    JSONObject inner = histArr.getJSONObject(i);
//                    double price = inner.getDouble("price");
//                    String time = inner.getString("timestamp");
//                    temp.put(price, time);
//                }
//                allCoins.put(i, temp);


                // This creates the individual SingleCoinHistory objects
                // Pass the JSONArray and the id of the coin.
                SingleCoin single = new SingleCoin(histArr, i);
                // Pause api calls to prevent timeouts.
                history.add(single);
                try {
                    TimeUnit.SECONDS.sleep((long) 0.03);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CoinHistory.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (UnirestException ex) {
                Logger.getLogger(CoinHistory.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    /**
     * Create a new thread if the thread
     * is not already started.
     */
    public void start() {
        System.out.println("Starting Thread");
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
    
    /**
     * Wait for the thread to complete before 
     * calling the methods for data. 
     */
    public void join() {
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(CoinRankApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // ========== GETTERS ==========
    
    /**
     * Check if the thread is alive
     * @return 
     */
    public boolean isAlive() {
        return t.isAlive();
    }
    
    public LinkedHashMap<Double, String> getSingleHistory() {
        return this.singleHistoryMap;
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
    public LinkedList<SingleCoin> getHistory() {
        return this.history;
    }
    

    


}
