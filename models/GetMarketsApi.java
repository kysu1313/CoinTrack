package models;

import interfaces.GlobalClassInterface;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;
/*
 * Connect to CoinRank api using the markets endpoint.
 * Collect data on different markets.
 * @author Kyle
 */

public class GetMarketsApi<T> implements interfaces.MarketsInterface, GlobalClassInterface{

    private JSONArray markets;
    private JSONObject resp;
    private JSONObject data;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
    private LinkedList<SingleMarket> marketList;
    private LinkedList<T> genericList;

    /**
     * Constructor to call api and build data.
     */
    public GetMarketsApi() {

        // Call API connector class
        this.resp = new ConnectToApi("https://coinranking1.p.rapidapi.com/markets",
            "coinranking1.p.rapidapi.com",
            "1b5d991760mshc823c72a6dfc70ep158a44jsnab15b3f64449").getJsonObject();
        this.data = this.resp.getJSONObject("data");
        this.markets = this.data.getJSONArray("markets");
        this.marketList = new LinkedList<>();
        this.genericList = new LinkedList<>();
        for (int i = 0; i < this.markets.length(); i++) {
            SingleMarket mkt = new SingleMarket(this.markets.getJSONObject(i));
            this.marketList.add(mkt);
            this.genericList.add((T)mkt);
        }
    }

    /**
     * Return list of SingleMarkets.
     * @return
     */
    @Override
    public LinkedList<SingleMarket> getMarketList() {
        return this.marketList;
    }

    @Override
    public LinkedList<T> getGenericMarketList() {
        return this.genericList;
    }

    @Override
    public String getClassName() {
        return "GetMarketsApi";
    }
}
