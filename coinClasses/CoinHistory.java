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
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class CoinHistory {

    public CoinHistory() {
        
        LinkedList<JSONObject> list = new LinkedList<>();

        for (int i = 0; i < 70; i++) {
            try {
                String url = "https://coinranking1.p.rapidapi.com/coin/" + i + "/history/7d";
                HttpResponse<JsonNode> response = Unirest.get(url)
                        .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                        .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                        .asJson();
                list.add(new JSONObject(response.getBody().toString()));
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


}
