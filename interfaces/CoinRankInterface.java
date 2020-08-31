package interfaces;

/**
 * Interface for a coin's "stats".
 * Stats is the name of a JSONObject
 * containing data on all cryptocurrencies
 * in the api response.
 * @author Kyle
 */
public interface CoinRankInterface {
    
    public int getTotal();
    public int getOffset();
    public int getLimit();
    public String getOrder();
    public String getBase();
    public int getTotalMarkets();
    public int getTotalExchanges();
    public int getTotalMarketCap();
    public int getTotal24hVolume();
    
}
