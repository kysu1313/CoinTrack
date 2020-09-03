package coinClasses;

import interfaces.CoinRankInterface;
import interfaces.SingleCoinInterface;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This is a general class that embodies a call to the Coinrank api. It parses
 * the JSON and has getter methods for individual coin information as well as a
 * LinkedList and LinkedHashMap methods
 *
 * @author Kyle
 */
public class CoinRankApi implements Runnable, CoinRankInterface {

    private Thread t;
    private HttpResponse<JsonNode> response;
    private JSONObject data;
    private JSONObject stats;
    private JSONObject base;
    private JSONArray coins;
    private LinkedList<SingleCoin> coinList;
    private LinkedHashMap<String, String> namePrice;

    public final String[] nameIds = {
        "Bitcoin", "1",
        "Ethereum", "2",
        "Tether", "8",
        "XRP", "3",
        "Chainlink", "59",
        "Polkadot", "71983",
        "Bitcoin", "4",
        "Litecoin", "7",
        "Cardano", "9",
        "Crypto.com", "10296",
        "Bitcoin", "4875",
        "Binance", "14",
        "EOS", "5",
        "Tezos", "18",
        "TRON", "11",
        "Stellar", "6",
        "OKB", "1524",
        "Monero", "10",
        "Cosmos", "4966",
        "USDC", "1760",
        "NEO", "15",
        "Algorand", "14585",
        "UMA", "69380",
        "LEO", "14755",
        "NEM", "17",
        "Huobi", "71",
        "VeChain", "19",
        "yearn.finance", "71246",
        "IOTA", "12",
        "Dash", "13",
        "Aave", "227",
        "Zcash", "21",
        "Ethereum", "16",
        "Compound", "70838",
        "OmiseGo", "24",
        "Synthetix", "10883",
        "Maker", "22",
        "Ontology", "26",
        "Kusama", "57612",
        "Numeraire", "422",
        "Theta", "96",
        "Basic", "46",
        "Nexus", "71022",
        "Orchid", "64962",
        "0x", "25",
        "FTX", "62569",
        "Dogecoin", "20",
        "Energy", "63102",
        "Waves", "43",
        "QTUM", "29"
    };

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
        try {
            this.response = Unirest.get("https://coinranking1.p.rapidapi.com/coins")
                    .header("x-rapidapi-host", "coinranking1.p.rapidapi.com")
                    .header("x-rapidapi-key", "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc")
                    .asJson();

            JSONObject coinRank = new JSONObject(response.getBody().toString());

            data = coinRank.getJSONObject("data");
            stats = data.getJSONObject("stats");
            coins = data.getJSONArray("coins");
            coinList = getCoinList();
        } catch (UnirestException ex) {
            Logger.getLogger(CoinRankApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create a new thread if the thread is not already started.
     */
    public void start() {
        System.out.println("Starting Thread");
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    /**
     * Wait for the thread to complete before calling the methods for data.
     */
    public void join() {
        try {
            t.join();
            System.out.println("Closing thread");
        } catch (InterruptedException ex) {
            Logger.getLogger(CoinRankApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAlive() {
        return t.isAlive();
    }

    /**
     * Creates a LinkedList of each coin in the "coins" json array.
     *
     * @return
     */
    public LinkedList<SingleCoin> getCoinList() {
        LinkedList<SingleCoin> tmpList = new LinkedList<>();
        for (int i = 0; i < coins.length(); i++) {
            JSONObject obj = coins.getJSONObject(i);
            SingleCoin coin = new SingleCoin(obj);
            tmpList.add(coin);
        }

        return tmpList;
    }

    public LinkedHashMap<String, String> getNamePrice() {

        namePrice = new LinkedHashMap<>();
        for (int i = 0; i < 45; i++) {  // coinList.size()
            String name = coinList.get(i).getName();
            String price = coinList.get(i).getPrice();
            namePrice.put(name, price);
        }
        return namePrice;
    }

    // ========== GETTERS ==========
    @Override
    public int getTotal() {
        return stats.getInt("total");
    }

    @Override
    public int getOffset() {
        return stats.getInt("offset");
    }

    @Override
    public int getLimit() {
        return stats.getInt("limit");
    }

    @Override
    public String getOrder() {
        return stats.getString("order");
    }

    @Override
    public String getBase() {
        return stats.getString("base");
    }

    @Override
    public int getTotalMarkets() {
        return stats.getInt("totalMarkets");
    }

    @Override
    public int getTotalExchanges() {
        return stats.getInt("totalExchanges");
    }

    @Override
    public int getTotalMarketCap() {
        return stats.getInt("totalMarketCap");
    }

    @Override
    public int getTotal24hVolume() {
        return stats.getInt("total24hVolume");
    }

}
