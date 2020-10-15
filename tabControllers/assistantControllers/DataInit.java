/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers;

import coinClasses.CoinHistory;
import coinClasses.CoinRankApi;
import coinClasses.FixerApi;
import coinClasses.GlobalCoinStats;
import coinClasses.SingleCoin;
import coinClasses.UserCoin;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kyle
 */
public class DataInit {

    private final CoinHistory COIN_HIST;
    private final CoinRankApi COIN_RANK;
    private final GlobalCoinStats GLOBAL_STATS;
    private final FixerApi FIXER;

    /** TAB 3 **/
    private static LinkedHashMap<Double, String> singleHistoryMap;
    private static LinkedHashMap<Double, String> userHistoryMap;
    private static LinkedList<LinkedHashMap<Double, String>> linkedUserHistoryMap;
    private static LinkedList<UserCoin> userCoinList;
    protected static LinkedList<SingleCoin> coinList;
    private static LinkedList<SingleCoin> userSingleCoins;
    private static LinkedList<UserCoin> savedCoins;
    private static LinkedList<String> friendList;
    private static LinkedList<String> onlineUserList;

    /** TAB 1 **/
    private static LinkedHashMap<String, String> coinNamePrice;
    private static GlobalCoinStats globalStats;
    private static ObservableList<String> currencies;
    private static long currencyRate;

    /** TAB 2 **/
    private static LinkedHashMap<String, Integer> historyMap;
    private static final ObservableList<String> TIMES = FXCollections.
            observableArrayList("24h", "7d", "30d", "1y", "5y");
    private static final ObservableList<String> NUMCOINS = FXCollections.
            observableArrayList("3", "5", "7", "15", "25", "35", "50");
    private static final ObservableList<String> ADDREMOVE = FXCollections.
            observableArrayList("add", "remove");

    public DataInit() throws IOException {
        this.COIN_HIST = new CoinHistory();
        this.COIN_RANK = new CoinRankApi();
        this.GLOBAL_STATS = new GlobalCoinStats();
        this.FIXER = new FixerApi();
        this.singleHistoryMap = this.COIN_HIST.getSingleHistory();
        this.historyMap = this.COIN_HIST.getPriceDate();
    }
    
    /** ============ GETTERS ============ **/

    public CoinRankApi getCoinRankApi() {
        return this.COIN_RANK;
    }

    public CoinHistory getCoinHistory() {
        return this.COIN_HIST;
    }

    public GlobalCoinStats getGlobalStats() {
        return this.GLOBAL_STATS;
    }

}
