package interfaces;

/**
 * Interface for global coin stats class.
 * @author Kyle
 */
public interface GlobalStatsInterface {
    public int getTotalCoins();
    public int getTotalMarkets();
    public int getTotalExchanges();
    public int getTotalMarketCap();
    public int getTotal24hVolume();
}
