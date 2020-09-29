package coinClasses;

/**
 * This class calls the Alpha Vantage API and stores the data using the
 * AlphaSingleCoin.java class.
 * @author Kyle
 */

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.json.JSONObject;


public class AlphaVantage{

    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private final String ENDPOINT = "alpha-vantage.p.rapidapi.com";
    private final String KEY = "81757143b0msh4237319349ebdffp1da0c0jsn4cc7c80702be";
    private String symbol;
    private LinkedList<LinkedHashMap<String, String>> dailySeries;
    private LinkedList<LinkedHashMap<String, String>> weeklySeries;

    /**
     * Constructor, saves symbol of coin for API call.
     * @param _symbol
     */
    public AlphaVantage(String _symbol) {
        this.symbol = _symbol;
        if (DEBUG) {System.out.println("Connecting to Alpha Vantage API...");}
    }

    /**
     * Returns linked list of daily (open, high, low, and close) values.
     * @return
     */
    public LinkedList<LinkedHashMap<String, String>> getDaily() {
        String url = "https://alpha-vantage.p.rapidapi.com/query?market=CNY&symbol=" + this.symbol + "&function=DIGITAL_CURRENCY_DAILY";
        ConnectToApi api = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Daily)");
        Iterator<String> keys = innerJob.keys();
        this.dailySeries = new LinkedList<>();
        while (keys.hasNext()) {
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();
            System.out.println(keys.next());
            JSONObject ob = innerJob.getJSONObject(keys.next());
            Iterator<String> inKeys = ob.keys();
            while (inKeys.hasNext()) {
                temp.put(inKeys.next(), ob.getString(inKeys.next()));
            }
            this.dailySeries.add(temp);
        }
        return this.dailySeries;
    }

    /**
     * Returns linked list of weekly (open, high, low, and close) values.
     * @return
     */
    public LinkedList<LinkedHashMap<String, String>> getWeekly() {
        String url = "https://alpha-vantage.p.rapidapi.com/query?function=DIGITAL_CURRENCY_WEEKLY&symbol=" + this.symbol + "&market=CNY";
        ConnectToApi api = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Weekly)");
        Iterator<String> keys = innerJob.keys();
        this.weeklySeries = new LinkedList<>();
        while (keys.hasNext()) {
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();
            System.out.println(keys.next());
            JSONObject ob = innerJob.getJSONObject(keys.next());
            Iterator<String> inKeys = ob.keys();
            while (inKeys.hasNext()) {
                temp.put(inKeys.next(), ob.getString(inKeys.next()));
            }
            this.weeklySeries.add(temp);
        }
        return this.weeklySeries;
    }
}
