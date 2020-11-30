package interfaces;

import org.json.JSONObject;

/**
 * Interface for the api connection class.
 * @author Kyle
 */
public interface ApiInterface {

    public JSONObject getJsonObject();
    public String getResponseString();
    public boolean getStatus();
}
