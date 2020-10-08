/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import coinClasses.SingleCoin;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author Kyle
 */
public interface CoinHistoryInterface {
    
    public LinkedHashMap<String, Integer> getPriceDate();
    public LinkedList<SingleCoin> getHistory();
    public double getChange();
    
}
