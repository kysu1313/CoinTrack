package coinClasses;

/**
 * This class calls the Alpha Vantage API and stores the data using the
 * AlphaSingleCoin.java class.
 * @author Kyle
 */

import com.BarData;
import interfaces.DailyWeeklyInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jfree.data.xy.OHLCDataItem;
import org.json.JSONObject;


public class AlphaVantage implements DailyWeeklyInterface{

    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;
    private final String ENDPOINT = "alpha-vantage.p.rapidapi.com";
    private final String KEY = "81757143b0msh4237319349ebdffp1da0c0jsn4cc7c80702be";
    private String symbol;
    private LinkedList<LinkedHashMap<String, String>> dailySeries;
    private LinkedList<LinkedHashMap<String, String>> weeklySeries;
    private List<OHLCDataItem> ohlc;
    private List<BarData> barData;

    /**
     * Constructor, saves symbol of coin for API call.
     * @param _symbol
     */
    public AlphaVantage(String _symbol) {
        this.symbol = _symbol;
        if (DEBUG) {System.out.println("Connecting to Alpha Vantage API...");}
    }

    /**
     * Returns linked list of daily (open, high, low, and close) values.
     * @return
     */
    @Override
    public LinkedList<LinkedHashMap<String, String>> getDaily() {
        String url = "https://alpha-vantage.p.rapidapi.com/query?market=CNY&symbol=" + this.symbol + "&function=DIGITAL_CURRENCY_DAILY";
        ConnectToApi api = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Daily)");
        Iterator<String> keys = innerJob.keys();
        this.dailySeries = new LinkedList<>();
        while (keys.hasNext()) {
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();
            System.out.println(keys.next());
            JSONObject ob = innerJob.getJSONObject(keys.next());
            Iterator<String> inKeys = ob.keys();
            while (inKeys.hasNext()) {
                temp.put(inKeys.next(), ob.getString(inKeys.next()));
            }
            this.dailySeries.add(temp);
        }
        return this.dailySeries;
    }

    /**
     * Returns linked list of weekly (open, high, low, and close) values.
     * @return
     */
    @Override
    public LinkedList<LinkedHashMap<String, String>> getWeekly() {
        String url = "https://alpha-vantage.p.rapidapi.com/query?function=DIGITAL_CURRENCY_WEEKLY&symbol=" + this.symbol + "&market=CNY";
        ConnectToApi api = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Weekly)");
        Iterator<String> keys = innerJob.keys();
        this.weeklySeries = new LinkedList<>();
        while (keys.hasNext()) {
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();
            System.out.println(keys.next());
            JSONObject ob = innerJob.getJSONObject(keys.next());
            Iterator<String> inKeys = ob.keys();
            while (inKeys.hasNext()) {
                temp.put(inKeys.next(), ob.getString(inKeys.next()));
            }
            this.weeklySeries.add(temp);
        }
        return this.weeklySeries;
    }
    
    public List<OHLCDataItem> getOHLCData() throws ParseException {
        String url = "https://alpha-vantage.p.rapidapi.com/query?market=CNY&symbol=" + this.symbol + "&function=DIGITAL_CURRENCY_DAILY";
        ConnectToApi api = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Daily)");
        Iterator<String> keys = innerJob.keys();
        this.dailySeries = new LinkedList<>();
//        this.ohlc = new LinkedList<>();
        this.ohlc = new ArrayList<>();
        while (keys.hasNext()) {
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();
            JSONObject ob = innerJob.getJSONObject(keys.next());
            String time = keys.next();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = formatter.parse(time);
            double open = Double.parseDouble(ob.getString("1b. open (USD)"));
            double high = Double.parseDouble(ob.getString("2b. high (USD)"));
            double low = Double.parseDouble(ob.getString("3b. low (USD)"));
            double close = Double.parseDouble(ob.getString("4b. close (USD)"));
            double volume = Double.parseDouble(ob.getString("5. volume"));
            OHLCDataItem odi = new OHLCDataItem(date, open, high, low, close, volume);
            this.ohlc.add(odi);
        }
        return this.ohlc;
    }
    
    public List<BarData> getBarData() throws ParseException {
        String url = "https://alpha-vantage.p.rapidapi.com/query?market=CNY&symbol=" + this.symbol + "&function=DIGITAL_CURRENCY_DAILY";
        ConnectToApi api = new ConnectToApi(url, this.ENDPOINT, this.KEY);
        JSONObject job = api.getJsonObject();
        JSONObject innerJob = job.getJSONObject("Time Series (Digital Currency Daily)");
        Iterator<String> keys = innerJob.keys();
        this.dailySeries = new LinkedList<>();
//        this.ohlc = new LinkedList<>();
        this.barData = new ArrayList<>();
        while (keys.hasNext()) {
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();
            JSONObject ob = innerJob.getJSONObject(keys.next());
            String time = keys.next();
            System.out.println(time);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = formatter.parse(time);
            GregorianCalendar gc = new GregorianCalendar();
            GregorianCalendar now = new GregorianCalendar();
            System.out.println(date);
            gc.setTime(date);
            double open = Double.parseDouble(ob.getString("1b. open (USD)"));
            double high = Double.parseDouble(ob.getString("2b. high (USD)"));
            double low = Double.parseDouble(ob.getString("3b. low (USD)"));
            double close = Double.parseDouble(ob.getString("4b. close (USD)"));
            double dubVolume = Double.parseDouble(ob.getString("5. volume"));
            long volume = (new Double(dubVolume)).longValue();
//            OHLCDataItem odi = new OHLCDataItem(date, open, high, low, close, volume);
//            System.out.println(gc);
            BarData bd = new BarData(gc, open, high, low, close, volume);
            this.barData.add(bd);
        }
        return this.barData;
    }
}
