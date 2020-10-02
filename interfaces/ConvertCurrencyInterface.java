
package interfaces;

import java.util.HashMap;

/**
 * Interface for currency-conversion api.
 * @author Kyle
 */
public interface ConvertCurrencyInterface {

    public float convert(String currencyFrom, String currencyTo, double amount);
    public HashMap<String, String> getSupportedSymbols();
    public long getExchangeRate(String _from, String _to);
    
}