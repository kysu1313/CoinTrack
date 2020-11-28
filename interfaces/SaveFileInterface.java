package interfaces;

import models.SingleCoin;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Interface for file saving classes.
 * @author Kyle
 */
public interface SaveFileInterface {

    public void saveTableAsText(LinkedList<SingleCoin> _data) throws IOException;
    public void saveTableAsText(String _fileName, LinkedList<SingleCoin> _data) throws IOException;
    public void saveAsExcel(LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException;
    public void saveAsExcel(String _fileName, LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException;
    public void saveAsJson(String _fileName, LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException;
    public void saveAsJson(LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException;
    public void createFile(String _fileName, String _fileType) throws IOException;
}
