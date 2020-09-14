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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class GetMarketsApi {
    
    JSONArray markets;
    JSONObject data;
    
    public GetMarketsApi() {

        String url = "https://coinranking1.p.rapidapi.com/markets";
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get(url)
                    .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                    .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                    .asJson();

            JSONObject resp = new JSONObject(response.getBody().toString());
            data = resp.getJSONObject("data");
            
            markets = data.getJSONArray("markets");
            
            for (int i = 0; i < markets.length(); i++) {
                SingleMarket mkt = new SingleMarket(markets.getJSONObject(i));
            }

        } catch (UnirestException ex) {
            Logger.getLogger(GetMarketsApi.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
}
