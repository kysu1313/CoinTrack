package coinClasses;

/**
 * This class calls the "Fixer" api to convert 
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class FixerCurrencyApi implements interfaces.ConvertCurrencyInterface{
    
    private final String ENDPOINT = "fixer-fixer-currency-v1.p.rapidapi.com";
    private final String KEY = "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc";

    public FixerCurrencyApi() throws IOException {
        System.out.println("Calling Fixer API");
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
     * @return
     */
    public HashMap<String, String> getSupportedSymbols() {
        HashMap<String, String> map = new HashMap<>();
        String url = "https://fixer-fixer-currency-v1.p.rapidapi.com/symbols";
        ConnectToApi con = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject obj = con.getJsonObject();
        JSONObject symbols = obj.getJSONObject("symbols");
        Iterator<String> keys = symbols.keys();
        
        while (keys.hasNext()) {
            String key = keys.next();
            if (symbols.get(key) instanceof JSONObject) {
                map.put(key.toString(), symbols.getString(key));
            }
        }
        return map;
    }
}
