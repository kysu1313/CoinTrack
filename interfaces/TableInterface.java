package interfaces;

import coinClasses.UserCoin;
import java.util.LinkedList;
import javafx.scene.control.ListView;

/**
 *
 * @author Kyle
 */
public interface TableInterface {
    public void displayTable();
    public void colorChangeCol(String _colorUp, String _colorDown);
    public void addDoubleClick();
    public void createTableCells(String _username, ListView _savedCoinsList, LinkedList<UserCoin> _savedCoins);
}
