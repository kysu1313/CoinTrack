package interfaces;

/**
 * Single Market interface.
 * @author Kyle
 */
public interface SingleMarketInterface {

    public void setId();
    public void setUuid();
    public void setRank();
    public void setBaseSymbol();
    public int getId();
    public String getUuid();
    public int getRank();
    public String getBaseSymbol();
    public String getQuoteSymbol();
    public String getSourceName();
    public String getSourceIconUrl();
    public int getTickerCreatedAt();
    public int getTickerClose();
    public double getTickerBaseVolume();
    public double getMarketShare();
    public double getPrice();
    public double getVolume();
}
