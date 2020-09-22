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
    
    private JSONArray markets;
    private JSONObject resp;
    private JSONObject data;
    
    public GetMarketsApi() {

        // Call API connector class
        this.resp = new ConnectToApi("coinranking1.p.rapidapi.com",
        "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc").getJsonObject();
        data = this.resp.getJSONObject("data");
        markets = data.getJSONArray("markets");
        for (int i = 0; i < markets.length(); i++) {
            SingleMarket mkt = new SingleMarket(markets.getJSONObject(i));
        }
    }
}
