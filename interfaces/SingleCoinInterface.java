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
public interface SingleCoinInterface {
    
    public int getId();
    public String getUuid();
    public String getSlug();
    public String getSymbol();
    public String getName();
    public String getDescription();
    public String getColor();
    public String getIconType();
    public String getIconUrl();
    public String getWebsiteUrl();
    public String getConfirmedSupply();
    public int getNumberOfMarkets();
    public int getNumberOfExchanges();
    public String getType();
    public int getVolume();
    public int getMarketCap();
    public String getCoinMarketCap();
    public String getPrice();
    public int getCirculatingSupply();
    public int getTotalSupply();
    public boolean getApprovedSupply();
    public int getFirstSeen();
    public double getChange();
    public int getRank();
    
}
