package models;

/**
 * This is a general class that embodies a call to the Coinrank api. 
 * It parses the JSON and has getter methods for individual coin
 * information as well as a LinkedList and LinkedHashMap methods.
 *
 * @author Kyle
 */
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public final class CoinRankApi implements Runnable, interfaces.CoinDataInterface {

    private Thread t;
    private HttpResponse<JsonNode> response;
    private JSONObject resp;
    private JSONObject data;
    private JSONObject stats;
    private JSONObject base;
    private JSONArray coins;
    private LinkedList<SingleCoin> coinList;
    private LinkedHashMap<String, String> namePrice;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;

    /**
     * Constructor, Automatically makes a call to the "coins" url and parses the
     * data into easily usable data;
     */
    public CoinRankApi() {
        start();
    }

    /**
     * TODO: fix?
     *
     * Created a threaded version of this class.
     *
     * idk if this is a good way to do this or not....
     */
    @Override
    public void run() {
            JSONObject coinRank = new ConnectToApi("https://coinranking1.p.rapidapi.com/coins",
                "coinranking1.p.rapidapi.com",
                "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc").getJsonObject();
            this.data = coinRank.getJSONObject("data");
            this.stats = data.getJSONObject("stats");
            this.coins = data.getJSONArray("coins");
            this.coinList = getCoins();
    }

    /**
     * Create a new thread if the thread is not already started.
     */
    @Override
    public void start() {
        if (DEBUG){System.out.println("Starting Thread");}
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    /**
     * Wait for the thread to complete before calling the methods for data.
     */
    @Override
    public void join() {
        try {
            t.join();
            if (DEBUG){System.out.println("Closing thread");}
        } catch (InterruptedException ex) {
            Logger.getLogger(CoinRankApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks if the thread is alive.
     * @return
     */
    @Override
    public boolean isAlive() {
        return t.isAlive();
    }

    
    // ========== GETTERS ==========
    
    /**
     * Update the coins in the database to their current prices, volume, etc.
     * @param _coinList
     */
    @Override
    public void updateDatabaseCoins(LinkedList<SingleCoin> _coinList) {
//        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        Date date = new Date ();
//        date.setTime((long)ts*1000);
        ConnectToDatabase conn = new ConnectToDatabase();
        _coinList.forEach((coin) -> {
            conn.addCoinToDatabase(coin.getId(), coin.getUuid(), coin.getSlug(),
                            coin.getSymbol(), coin.getName(),
                            coin.getNumberOfMarkets(), coin.getNumberOfExchanges(),
                            coin.getVolume(), coin.getMarketCap(), coin.getPrice(), 
                            coin.getChange(), coin.getRank());
            
        });
    }
    
    /**
     * Creates a LinkedList of each coin in the "coins" json array.
     * @return
     */
    private LinkedList<SingleCoin> getCoins() {
        LinkedList<SingleCoin> tmpList = new LinkedList<>();
        for (int i = 0; i < this.coins.length()-1; i++) {
            JSONObject obj = this.coins.getJSONObject(i);
            SingleCoin coin = new SingleCoin(obj);
            tmpList.add(coin);
        }
        return tmpList;
    }

    /**
     * Returns a sorted list of coins based on their price.
     * @return
     */
    @Override
    public LinkedList<SingleCoin> getSortedCoinList() {
        LinkedList<SingleCoin> temp = this.getCoinList();
        Collections.sort(temp, (SingleCoin o1, SingleCoin o2) -> {
            double p1 = Double.parseDouble(o1.getPrice());
            double p2 = Double.parseDouble(o2.getPrice());
            if (p1 < p2) {
                return -1;
            }
            if (p1 > p2) {
                return 1;
            }
            return 0;
        });
        return temp;
    }

    public LinkedList<SingleCoin> getCoinList() {
        return this.coinList;
    }

    /**
     * Creates a LinkedHashMap of coin names and
     * their prices.
     * @return 
     */
    @Override
    public LinkedHashMap<String, String> getNamePrice() {
        this.namePrice = new LinkedHashMap<>();
        for (int i = 0; i < 45; i++) {  // coinList.size()
            String name = this.coinList.get(i).getName();
            String price = this.coinList.get(i).getPrice();
            this.namePrice.put(name, price);
        }
        return this.namePrice;
    }

    @Override
    public int getTotal() {
        return this.stats.getInt("total");
    }

    @Override
    public int getOffset() {
        return this.stats.getInt("offset");
    }

    @Override
    public int getLimit() {
        return this.stats.getInt("limit");
    }

    @Override
    public String getOrder() {
        return this.stats.getString("order");
    }

    @Override
    public String getBase() {
        return this.stats.getString("base");
    }

    @Override
    public int getTotalMarkets() {
        return this.stats.getInt("totalMarkets");
    }

    @Override
    public int getTotalExchanges() {
        return this.stats.getInt("totalExchanges");
    }

    @Override
    public int getTotalMarketCap() {
        return this.stats.getInt("totalMarketCap");
    }

    @Override
    public int getTotal24hVolume() {
        return this.stats.getInt("total24hVolume");
    }

}