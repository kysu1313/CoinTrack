/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Kyle
 */
public interface CoinRankInterface {
    
    public int getTotal();
    public int getOffset();
    public int getLimit();
    public String getOrder();
    public String getBase();
    public int getTotalMarkets();
    public int getTotalExchanges();
    public int getTotalMarketCap();
    public int getTotal24hVolume();
    
}
