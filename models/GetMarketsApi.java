/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author Kyle
 */
package models;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetMarketsApi {
    
    private JSONArray markets;
    private JSONObject resp;
    private JSONObject data;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    
    public GetMarketsApi() {

        // Call API connector class
        this.resp = new ConnectToApi("coinranking1.p.rapidapi.com",
            "coinranking1.p.rapidapi.com",
            "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc").getJsonObject();
        data = this.resp.getJSONObject("data");
        markets = data.getJSONArray("markets");
        for (int i = 0; i < markets.length(); i++) {
            SingleMarket mkt = new SingleMarket(markets.getJSONObject(i));
        }
    }
}
