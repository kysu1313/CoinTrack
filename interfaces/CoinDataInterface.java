package interfaces;

import models.SingleCoin;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Interface for a coin's "stats".
 * Stats is the name of a JSONObject
 * containing data on all cryptocurrencies
 * in the api response.
 * @author Kyle
 */
public interface CoinDataInterface {

    public void start();
    public void join();
    public boolean isAlive();
    public void updateDatabaseCoins(LinkedList<SingleCoin> coinList);
    public LinkedList<SingleCoin> getCoinList();
    public LinkedList<SingleCoin> getSortedCoinList();
    public LinkedHashMap<String, String> getNamePrice();
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
