package models;

/**
 * This class represents a single market.
 * A market is an exchange for one or more types of cryptocurrencies.
 * @author Kyle
 */
import interfaces.GlobalClassInterface;
import interfaces.SingleMarketInterface;
import org.json.JSONObject;

public class SingleMarket implements GlobalClassInterface, SingleMarketInterface{

    JSONObject arr;

    private int id;
    private String uuid;
    private int rank;
    private String baseSymbol;
    private String quoteSymbol;
    private String sourceName;
    private String sourceIconUrl;
    private int tickerCreatedAt;
    private int tickerClose;
    private double tickerBaseVolume;
    private double marketShare;
    private double price;
    private double volume;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;

    public SingleMarket(JSONObject jar) {
        this.arr = jar;
        fillValues();
    }

    /**
     * Gather values from json.
     */
    private void fillValues() {
        this.uuid = this.arr.getString("uuid");
        this.rank = this.arr.getInt("rank");
        this.baseSymbol = this.arr.getString("baseSymbol");
        this.quoteSymbol = this.arr.getString("quoteSymbol");
        this.sourceName = this.arr.getString("sourceName");
        this.sourceIconUrl = this.arr.getString("sourceIconUrl");
        this.tickerCreatedAt = this.arr.getInt("tickerCreatedAt");
        this.tickerClose = this.arr.getInt("tickerClose");
        this.tickerBaseVolume = this.arr.getDouble("tickerBaseVolume");
        this.marketShare = this.arr.getDouble("marketShare");
        this.price = this.arr.getDouble("price");
        this.volume = this.arr.getDouble("volume");
    }

    @Override
    public void setId() {
        this.id = arr.getInt("id");
    }

    @Override
    public void setUuid() {
        this.uuid = arr.getString("uuid");
    }

    @Override
    public void setRank() {
        this.rank = arr.getInt("rank");
    }

    @Override
    public void setBaseSymbol() {
        this.baseSymbol = arr.getString("baseSymbol");
    }

    @Override
    public String getClassName() {
        return "SingleMarket";
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public int getRank() {
        return this.rank;
    }

    @Override
    public String getBaseSymbol() {
        return this.baseSymbol;
    }

    @Override
    public String getQuoteSymbol() {
        return this.quoteSymbol;
    }

    @Override
    public String getSourceName() {
        return this.sourceName;
    }

    @Override
    public String getSourceIconUrl() {
        return this.sourceIconUrl;
    }

    @Override
    public int getTickerCreatedAt() {
        return this.tickerCreatedAt;
    }

    @Override
    public int getTickerClose() {
        return this.tickerClose;
    }

    @Override
    public double getTickerBaseVolume() {
        return this.tickerBaseVolume;
    }

    @Override
    public double getMarketShare() {
        return this.marketShare;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public double getVolume() {
        return this.volume;
    }

}
