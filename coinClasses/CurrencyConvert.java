package coinClasses;

/**
 * This class calls the "Fixer" api to convert 
 */
import java.io.IOException;

public class CurrencyConvert implements interfaces.ConvertCurrencyInterface{
    
    private final String ENDPOINT = "fixer-fixer-currency-v1.p.rapidapi.com";
    private final String KEY = "310c3610fcmsheb7636d5c15a024p1a11dajsnf459d4f82cfc";

    public CurrencyConvert() throws IOException {
        System.out.println("converting currency");
    }

    @Override
    public float convert(String _from, String _to, double _amount) {

        String url = "https://fixer-fixer-currency-v1.p.rapidapi.com/convert?from=" + _from + "&to=" + _to + "&amount=" + _amount;
        ConnectToApi con = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        float res = con.getJsonObject().getLong("result");
        return res;
    }
}