package models;

/**
 * This class calls the "Fixer" api to convert currencies.
 * It takes the current currency and the desired currency,
 * then returns a conversion rate.
 *
 * - Kyle
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.json.JSONObject;

public class FixerApi implements interfaces.ConvertCurrencyInterface{

    private final String ENDPOINT = "fixer-fixer-currency-v1.p.rapidapi.com";
    private final String KEY = "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc";
    private HashMap<String, String> map;
    private String[] symbolArr;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;

    public FixerApi() throws IOException {
        if (this.DEBUG) {System.out.println("Calling Fixer API");}
        this.map = new HashMap<>();
    }

    /**
     * Convert _amount _from currency _to currency,
     *
     * @param _from
     * @param _to
     * @param _amount
     * @return
     */
    @Override
    public float convert(String _from, String _to, double _amount) {

        String url = "https://fixer-fixer-currency-v1.p.rapidapi.com/convert?from=" + _from + "&to=" + _to + "&amount=" + _amount;
        ConnectToApi con = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        float res = con.getJsonObject().getLong("result");
        return res;
    }

    /**
     * Returns a hash map of accepted currencies and their symbols.
     * i.e.  "ARS":"Argentine Peso"
     *
     * @return
     */
    @Override
    public HashMap<String, String> getSupportedSymbols() {
        String url = "https://fixer-fixer-currency-v1.p.rapidapi.com/symbols";
        ConnectToApi con = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject symbols = con.getJsonObject().getJSONObject("symbols");
        Iterator<String> keys = symbols.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            this.map.put(key.toString(), symbols.getString(key));
        }
        return this.map;
    }

    /**
     * Get the exchange rate from one currency to another.
     * @param _from
     * @param _to
     * @return
     */
    @Override
    public long getExchangeRate(String _from, String _to) {
        String url = "https://fixer-fixer-currency-v1.p.rapidapi.com/latest?base=" 
                    + _from + "&symbols=" + _to; // GBP%252C + "%252CEUR"
        ConnectToApi con = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject obj = con.getJsonObject();
        if (this.DEBUG){System.out.println(url);}
        if (this.DEBUG){System.out.println(obj.getJSONObject("rates").getLong(_to));}
        return obj.getJSONObject("rates").getLong(_to);
    }

}
