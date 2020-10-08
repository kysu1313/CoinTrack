package interfaces;

import java.util.LinkedList;

/**
 * Interface for all graph types.
 * @author Kyle
 */
public interface GraphInterface {

    public void displayGraph();
    public void colorGraph();
    public void alternateColors(String color1, String color2);
    public void scaleGraph();
    public LinkedList<String> getElements();

}
