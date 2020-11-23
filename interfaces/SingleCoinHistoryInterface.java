
package interfaces;

/**
 * Interface for singleCoinHistory class.
 * @author Kyle
 */
import java.util.LinkedHashMap;

public interface SingleCoinHistoryInterface {

    public LinkedHashMap<Double, String> getCoinList();
    public int getCoinId();

}
