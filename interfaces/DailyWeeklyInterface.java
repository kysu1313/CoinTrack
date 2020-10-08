package interfaces;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Interface to get more granular data for each coin.
 * The LinkedLists have hash maps with open, high, low, close prices.
 * @author Kyle
 */
public interface DailyWeeklyInterface {
    
    public LinkedList<LinkedHashMap<String, String>> getDaily();
    public LinkedList<LinkedHashMap<String, String>> getWeekly();
    
}
