package coinClasses;

import interfaces.ApiInterface;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * This class creates a connection to an api and has methods
 * that allow access to the JSONBody response
 * @author Kyle
 */
public class ConnectToApi implements ApiInterface{
    
    private JSONObject job;
    private String response;
    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    
    // Constructor for all rapid apis
    public ConnectToApi(String _url, String _endpoint, String _key) {
        HttpResponse<JsonNode> resp;
        try {
            resp = Unirest.get(_url) // "https://coinranking1.p.rapidapi.com/stats"
                    .header("x-rapidapi-host", _endpoint)
                    .header("x-rapidapi-key", _key)
                    .asJson();
            this.response = resp.getBody().toString();
            this.job = new JSONObject(resp.getBody().toString());
        } catch (UnirestException ex) {
            Logger.getLogger(ConnectToApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public JSONObject getJsonObject() {
        return this.job;
    }

    @Override
    public String getResponseString() {
        return this.response;
    }
    
//    public String testing() {
//        return
//    }
    
}
