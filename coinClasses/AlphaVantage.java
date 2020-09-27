/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import java.util.Iterator;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class AlphaVantage {
    
    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    
    public AlphaVantage() {
        if (DEBUG) {System.out.println("Connecting to Alpha Vantage API...");}
    }
    
    public String getWeekly() {
        ConnectToApi api = new ConnectToApi("https://alpha-vantage.p.rapidapi.com/query?function=DIGITAL_CURRENCY_WEEKLY&symbol=BTC&market=CNY", "alpha-vantage.p.rapidapi.com", "81757143b0msh4237319349ebdffp1da0c0jsn4cc7c80702be");
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Weekly)");
        Iterator<String> keys = innerJob.keys();
        while (keys.hasNext()) {
            System.out.println(keys.next());
            JSONObject ob = innerJob.getJSONObject(keys.next());
            Iterator<String> inKeys = ob.keys();
            while (inKeys.hasNext()) {
                System.out.println("     " + inKeys.next() + ": " + ob.getString(inKeys.next()));
            }
        }
        return job.getJSONObject("Time Series (Digital Currency Weekly)").toString();
    }
    
}
