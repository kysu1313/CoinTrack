package models;
/**
 * This class acts the connection between coins and the database class.
 * @author Kyle
 */

import controllers.AlertMessages;
import java.util.LinkedList;

public class CoinDatabaseConnection {

    private ConnectToDatabase conn;
    private SingleCoin coin;

    /**
     * Create a new database connection.
     */
    public CoinDatabaseConnection() {
        this.conn = new ConnectToDatabase();
    }

    /**
     * Update a single coin in the database.
     * @param _coin
     */
    public void updateSingleCoin(SingleCoin _coin) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        this.coin = _coin;
        dbConn.updateCoinPrices(this.coin.getId(), Double.parseDouble(this.coin.getPrice()),
                this.coin.getChange(), this.coin.getVolume());
        dbConn.close();
    }

    /**
     * Update a list of coins in the database.
     * @param _coinList
     */
    public void updateMultipleCoins(LinkedList<SingleCoin> _coinList) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        _coinList.forEach((item) -> {
            dbConn.updateCoinPrices(item.getId(), Double.parseDouble(item.getPrice()),
                    item.getChange(), item.getVolume());
        });
        dbConn.close();
    }

    /**
     * Save a coin in the database.
     * @param _userName
     * @param _coinID
     */
    public void saveCoin(String _userName, int _coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(_userName, _coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }

    /**
     * Delete a coin saved by the user.
     * @param _user
     * @param _coinID
     */
    public void deleteCoin(User _user, int _coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.deleteSavedCoin(_user, _coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }
}
