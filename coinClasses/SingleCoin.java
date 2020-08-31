/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import interfaces.SingleCoinInterface;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kyle
 */
public class SingleCoin implements SingleCoinInterface{
    
    private JSONObject coinList;

    /**
     * Constructor
     * @param job 
     */
    public SingleCoin(JSONObject job) {
        coinList = job;
    }
    
    @Override
    public int getId() {
        return coinList.getInt("id");
    }

    @Override
    public String getUuid() {
        return coinList.getString("uuid");
    }

    @Override
    public String getSlug() {
        return coinList.getString("slug");
    }

    @Override
    public String getSymbol() {
        return coinList.getString("symbol");
    }

    @Override
    public String getName() {
        return coinList.getString("name");
    }

    @Override
    public String getDescription() {
        return coinList.getString("description");
    }

    @Override
    public String getColor() {
        return coinList.getString("color");
    }

    @Override
    public String getIconType() {
        return coinList.getString("iconType");
    }

    @Override
    public String getIconUrl() {
        return coinList.getString("iconUrl");
    }

    @Override
    public String getWebsiteUrl() {
        return coinList.getString("websiteUrl");
    }

    @Override
    public String getConfirmedSupply() {
        return coinList.getString("confirmedSupply");
    }

    @Override
    public int getNumberOfMarkets() {
        return coinList.getInt("numberOfMarkets");
    }

    @Override
    public int getNumberOfExchanges() {
        return coinList.getInt("numberOfExchanges");
    }

    @Override
    public String getType() {
        return coinList.getString("type");
    }

    @Override
    public int getVolume() {
        return coinList.getInt("volume");
    }

    @Override
    public int getMarketCap() {
        return coinList.getInt("marketCap");
    }

    @Override
    public String getCoinMarketCap() {
        return coinList.getString("marketCap");
    }

    @Override
    public String getPrice() {
        return coinList.getString("price");
    }

    @Override
    public int getCirculatingSupply() {
        return coinList.getInt("circulatingSupply");
    }

    @Override
    public int getTotalSupply() {
        return coinList.getInt("supply");
    }

    @Override
    public boolean getApprovedSupply() {
        return coinList.getBoolean("approvedSupply");
    }

    @Override
    public int getFirstSeen() {
        return coinList.getInt("firstSeen");
    }

    @Override
    public double getChange() {
        return coinList.getDouble("change");
    }

    @Override
    public int getRank() {
        return coinList.getInt("rank");
    }

    
}
