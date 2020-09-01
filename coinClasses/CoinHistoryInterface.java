/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author Kyle
 */
interface CoinHistoryInterface {
    
    public LinkedHashMap<String, Integer> getPriceDate();
    public LinkedList<SingleCoinHistory> getHistory();
    public double getChange();
    
}
