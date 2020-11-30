package interfaces;

import java.util.LinkedList;
import models.SingleMarket;

/**
 * Interface for collecting market data.
 * @author Kyle
 * @param <T>
 */
public interface MarketsInterface<T> {
    public LinkedList<SingleMarket> getMarketList();
    public LinkedList<T> getGenericMarketList();
}
