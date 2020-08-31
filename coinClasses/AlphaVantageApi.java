package coinClasses;

import com.sun.deploy.net.HttpResponse;
import java.util.Iterator;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import static java.awt.SystemColor.text;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.net.www.content.text.plain;

/**
 *
 * @author Kyle
 */
public class AlphaVantageApi {

    com.mashape.unirest.http.HttpResponse<JsonNode> response3;

    public AlphaVantageApi() {
        try {

            com.mashape.unirest.http.HttpResponse<JsonNode> response3 = Unirest.get("https://alpha-vantage.p.rapidapi.com/query?function=DIGITAL_CURRENCY_WEEKLY&symbol=BTC&market=CNY")
                    .header("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                    .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                    .asJson();

            System.out.println(response3.getBody().toString());
            
            JSONObject jo = new JSONObject(response3);
            JSONObject metaData = jo.getJSONObject("Meta Data");
            JSONObject timeSeries = jo.getJSONObject("Time Series (Digital Currency Weekly)");
            
            Iterator<String> keys = timeSeries.keys();
            
            while (keys.hasNext()) {
                String key = keys.next();
                if (timeSeries.get(key) instanceof JSONObject) {
//                    System.out.println(timeSeries.get(key));
                    JSONObject innerObj = timeSeries.getJSONObject(key);
                    Iterator<String> innerKeys = innerObj.keys();
                    
                    while (innerKeys.hasNext()) {
                        String innerKey = innerKeys.next();
                        if (innerObj.get(innerKey) instanceof JSONObject) {
//                            System.out.println(innerObj.get(innerKey));
                            if (innerObj.get(innerKey).toString() == "5. volume"){
                                System.out.println(innerObj.get(innerKey).toString());
                            }
                        }
                    }
                    
                }
            }
            
            

        } catch (UnirestException ex) {
            Logger.getLogger(AlphaVantageApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
