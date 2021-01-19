package models;
/**
 * This class saves all coins to either a text file,
 * excel file, or JSON file to the users computer.
 * @author Kyle
 */

//import static com.sun.deploy.config.OSType.isMac;
import static com.sun.javafx.PlatformUtil.isWindows;
import interfaces.SaveFileInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.filechooser.FileSystemView;
import controllers.AlertMessages;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SaveToDisk implements SaveFileInterface{

    private static String OS = System.getProperty("os.name").toLowerCase();
    private String system;
    private String fileName;
    private String filePath;
    private final String DEFAULT_WINDOWS_LOCATION = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
    private final String DEFAULT_MAC_LOCATION = "/Users/$USER/Documents/CoinTrack";
    private File dir;
    private final SimpleDateFormat TIME_FORMAT= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Path path;
    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;

    /**
     * Constructor class determines what OS the user is running.
     * Then changes the directory appropriately.
     * @throws IOException
     */
    public SaveToDisk() throws IOException {
        // Used to determine if user is on Mac or Windows
        if (isWindows()) {
            this.system = "windows";
            this.dir = new File(this.DEFAULT_WINDOWS_LOCATION);
//        } else if (isMac()) {
//            this.system = "mac";
//            this.dir = new File(this.DEFAULT_MAC_LOCATION);
//            this.path = Paths.get(DEFAULT_MAC_LOCATION);
//            Files.createDirectories(this.path);
        }
    }

    /**
     * Constructor class determines what OS the user is running.
     * Then changes the directory appropriately adding the given
     * file path.
     * @throws IOException
     */
    public SaveToDisk(File _dir) throws IOException {
        // Used to determine if user is on Mac or Windows
        if (isWindows()) {
            this.system = "windows";
            this.dir = _dir;
        }
//        } else if (isMac()) {
//            this.system = "mac";
//            this.dir = _dir;
//            this.path = Paths.get(DEFAULT_MAC_LOCATION);
//            Files.createDirectories(this.path);
//        }
    }

    /**
     * Create file with given file name and file type.
     * @param _fileName
     * @param _fileType
     * @throws IOException
     */
    @Override
    public void createFile(String _fileName, String _fileType) throws IOException {
        CoinRankApi cri = new CoinRankApi();
        cri.join();
        LinkedList<SingleCoin> coinList = cri.getCoinList();
        switch (_fileType) {
            case ".txt":
                saveTableAsText(_fileName, coinList);
                break;
            case ".xlsx":
                saveAsExcel(_fileName, coinList);
                break;
            case ".json":
                saveAsJson(_fileName, coinList);
                break;
            default:
                AlertMessages.showErrorMessage("Missing info", "Please select a file format.");
        }
        switch (_fileType) {
            case ".txt":
                    saveTableAsText(coinList);
                break;
            case ".xlsx":
                    saveAsExcel(coinList);
                break;
            case ".json":
                    saveAsJson(coinList);
                break;
            default:
                AlertMessages.showErrorMessage("Missing info", "Please select a file format.");
        }
    }

    /**
     * Saves a LinkedList of single coins as a text file.
     * @param _data
     * @throws IOException
     */
    @Override
    public void saveTableAsText(LinkedList<SingleCoin> _data) throws IOException {
        Date date = new Date(System.currentTimeMillis());
        String fileName = "coin-track-" + TIME_FORMAT.format(date) + ".txt";
	FileOutputStream fos = new FileOutputStream(this.dir + "\\" + fileName);
        addDataToTextFile(fos, _data);
    }

    /**
     * Saves a LinkedList of single coins as a text file with given file name.
     * @param _data
     * @throws IOException
     */
    @Override
    public void saveTableAsText(String _fileName, LinkedList<SingleCoin> _data) throws IOException {
        Date date = new Date(System.currentTimeMillis());
	FileOutputStream fos;
        // Verify file path is acceptable
        try {
            fos = new FileOutputStream(this.dir + "\\" + _fileName + ".txt");
        } catch (FileNotFoundException ex) {
            AlertMessages.showErrorMessage("Bad File Path", "The specified file location could not be found.");
            fos = new FileOutputStream(_fileName + ".txt");
        }
        addDataToTextFile(fos, _data);
    }

    /**
     * Add data to text file.
     * @param _fos
     * @param _data
     * @throws IOException
     */
    private void addDataToTextFile(FileOutputStream _fos, LinkedList<SingleCoin> _data) throws IOException {
        // Loop over coins and add a row for each
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(_fos))) {
            for (SingleCoin coin : _data) {
                bw.write(coin.getName() + ": ");
                bw.write("id: " + coin.getId()+", ");
                bw.write("uuid: " + coin.getUuid()+", ");
                bw.write("symbol: " + coin.getSymbol()+", ");
                bw.write("iconUrl: " + coin.getIconUrl()+", ");
                bw.write("confirmedSupply: " + coin.getConfirmedSupply()+", ");
                bw.write("numberOfMarkets: " + coin.getNumberOfMarkets()+", ");
                bw.write("numberOfExchanges: " + coin.getNumberOfExchanges()+", ");
                bw.write("type: " + coin.getType()+", ");
                bw.write("volume: " + coin.getVolume()+", ");
                bw.write("marketCap: " + coin.getMarketCap()+", ");
                bw.write("price: " + coin.getPrice()+", ");
//                bw.write("circulatingSupply: " + coin.getCirculatingSupply()+", ");
//                bw.write("totalSupply: " + coin.getTotalSupply()+", ");
                bw.write("approvedSupply: " + coin.getApprovedSupply()+", ");
                bw.write("firstSeen: " + coin.getFirstSeen()+", ");
                bw.write("change: " + coin.getChange()+", ");
                bw.write("rank: " + coin.getRank()+", ");
                bw.newLine();
            }
            bw.close();
            _fos.close();
        }
    }

    /**
     * Save LinkedList of single coins as a excel file.
     * @param _data
     * @throws IOException
     */
    @Override
    public void saveAsExcel(LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("Coin Data");
        addExcelHeaders(spreadsheet);
        addExcelData(spreadsheet, _data);
        Date date = new Date(System.currentTimeMillis());
        String fileName = "coin-track-" + TIME_FORMAT.format(date) + ".xlsx";
        FileOutputStream out = new FileOutputStream(new File(this.dir + "\\" + fileName + ".xlsx"));
        workbook.write(out);
        out.close();
        System.out.println(".xlsx written successfully");
    }

    /**
     * Save LinkedList of single coins as a text excel with given file name.
     * @param _fileName
     * @param _data
     * @throws java.io.FileNotFoundException
     * @throws IOException
     */
    @Override
    public void saveAsExcel(String _fileName, LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("Coin Data");
        addExcelHeaders(spreadsheet);
        addExcelData(spreadsheet, _data);
        // Save file using output stream
        FileOutputStream out = new FileOutputStream(new File(this.dir + "\\" + _fileName + ".xlsx"));
        workbook.write(out);
        out.close();
        System.out.println(".xlsx written successfully");
    }

    /**
     * Add data to the excel sheet.
     * @param _sheet
     * @param _data
     */
    private void addExcelData(XSSFSheet _sheet, LinkedList<SingleCoin> _data) {
        int colNum = 0;
        int rowNum = 0;
        for (SingleCoin coin : _data) {
            Row currentRow = _sheet.createRow(rowNum);
            rowNum++;
            currentRow.createCell(colNum).setCellValue(coin.getName());
            currentRow.createCell(colNum + 1).setCellValue(coin.getId() + "");
            currentRow.createCell(colNum + 2).setCellValue(coin.getUuid());
            currentRow.createCell(colNum + 3).setCellValue(coin.getSymbol());
            currentRow.createCell(colNum + 4).setCellValue(coin.getIconUrl());
            currentRow.createCell(colNum + 5).setCellValue(coin.getConfirmedSupply() + "");
            currentRow.createCell(colNum + 6).setCellValue(coin.getNumberOfMarkets() + "");
            currentRow.createCell(colNum + 7).setCellValue(coin.getNumberOfExchanges() + "");
            currentRow.createCell(colNum + 8).setCellValue(coin.getType());
            currentRow.createCell(colNum + 9).setCellValue(coin.getVolume() + "");
            currentRow.createCell(colNum + 10).setCellValue(coin.getMarketCap() + "");
            currentRow.createCell(colNum + 11).setCellValue(coin.getPrice() + "");
//            currentRow.createCell(colNum + 12).setCellValue(coin.getCirculatingSupply() + "");
//            currentRow.createCell(colNum + 13).setCellValue(coin.getTotalSupply() + "");
            currentRow.createCell(colNum + 14).setCellValue(coin.getApprovedSupply() + "");
            currentRow.createCell(colNum + 15).setCellValue(coin.getFirstSeen() + "");
            currentRow.createCell(colNum + 16).setCellValue(coin.getChange() + "");
            currentRow.createCell(colNum + 17).setCellValue(coin.getRank() + "");
        }
    }

    /**
     * Create the column headers for excel files.
     * @param _sheet
     */
    private void addExcelHeaders(XSSFSheet _sheet) {
        int colNum = 0;
        int rowNum = 0;
        Row header = _sheet.createRow(rowNum);
        header.createCell(colNum).setCellValue("name");
        header.createCell(colNum+1).setCellValue("id");
        header.createCell(colNum+2).setCellValue("uuid");
        header.createCell(colNum+3).setCellValue("symbol");
        header.createCell(colNum+4).setCellValue("iconUrl");
        header.createCell(colNum+5).setCellValue("confirmedSupply");
        header.createCell(colNum+6).setCellValue("numberOfMarkets");
        header.createCell(colNum+7).setCellValue("numberOfExchanges");
        header.createCell(colNum+8).setCellValue("type");
        header.createCell(colNum+9).setCellValue("volume");
        header.createCell(colNum+10).setCellValue("marketCap");
        header.createCell(colNum+11).setCellValue("price");
//        header.createCell(colNum+12).setCellValue("circulatingSupply");
//        header.createCell(colNum+13).setCellValue("totalSupply");
        header.createCell(colNum+14).setCellValue("approvedSupply");
        header.createCell(colNum+15).setCellValue("firstSeen");
        header.createCell(colNum+16).setCellValue("change");
        header.createCell(colNum+17).setCellValue("rank");
    }

    /**
     * Save LinkedList of single coins as a json file with given file name.
     * @param _fileName
     * @param _data
     * @throws java.io.FileNotFoundException
     * @throws IOException
     */
    @Override
    public void saveAsJson(String _fileName, LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException {
	FileOutputStream fos;
        // Verify file path is acceptable
        try {
            fos = new FileOutputStream(this.dir + "\\" + _fileName + ".json");
        } catch (FileNotFoundException ex) {
            AlertMessages.showErrorMessage("Bad File Path", "The specified file location could not be found.");
            fos = new FileOutputStream(_fileName + ".txt");
        }
        // Write data to file
        writeJson(fos, _data);
    }

    /**
     * Save LinkedList of single coins as a json file.
     * @param _data
     * @throws java.io.FileNotFoundException
     * @throws IOException
     */
    @Override
    public void saveAsJson(LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException {
	FileOutputStream fos;
        Date date = new Date(System.currentTimeMillis());
        String fileName = "coin-track-" + TIME_FORMAT.format(date) + ".json";
        // Verify file path is acceptable
        try {
            fos = new FileOutputStream(this.dir + "\\" + fileName + ".json");
        } catch (FileNotFoundException ex) {
            AlertMessages.showErrorMessage("Bad File Path", "The specified file location could not be found.");
            fos = new FileOutputStream(fileName + ".txt");
        }
        // Write data to file
        writeJson(fos, _data);
    }

    /**
     * Write data to file formatted as JSON.
     * @param _fos
     * @param _data
     * @throws IOException
     */
    private void writeJson(FileOutputStream _fos, LinkedList<SingleCoin> _data) throws IOException {
        int count = 0;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(_fos))) {
            bw.write("{\"coins\": {");
            bw.newLine();
            for (SingleCoin coin : _data) {
                bw.write("\"" + coin.getName() + "\"" + ": {");
                bw.newLine();
                bw.write("\t\"" + "id\":" + "\"" + coin.getId() + "\",\n");
                bw.write("\t\"" + "uuid\":" + "\"" + coin.getUuid() + "\",");bw.newLine();
                bw.write("\t\"" + "symbol\":" + "\"" + coin.getSymbol() + "\",");bw.newLine();
                bw.write("\t\"" + "iconUrl\":" + "\"" + coin.getIconUrl() + "\",");bw.newLine();
                bw.write("\t\"" + "confirmedSupply\":" + "\"" + coin.getConfirmedSupply() + "\",");bw.newLine();
                bw.write("\t\"" + "numberOfMarkets\":" + "\"" + coin.getNumberOfMarkets() + "\",");bw.newLine();
                bw.write("\t\"" + "numberOfExchanges\":" + "\"" + coin.getNumberOfExchanges() + "\",");bw.newLine();
                bw.write("\t\"" + "type\":" + "\"" + coin.getType() + "\",");bw.newLine();
                bw.write("\t\"" + "volume\":" + "\"" + coin.getVolume() + "\",");bw.newLine();
                bw.write("\t\"" + "marketCap\":" + "\"" + coin.getMarketCap() + "\",");bw.newLine();
                bw.write("\t\"" + "price:\":" + "\"" + coin.getPrice() + "\",");bw.newLine();
//                bw.write("\t\"" + "circulatingSupply\":" + "\"" + coin.getCirculatingSupply() + "\",");bw.newLine();
//                bw.write("\t\"" + "totalSupply\":" + "\"" + coin.getTotalSupply() + "\",");bw.newLine();
                bw.write("\t\"" + "approvedSupply\":" + "\"" + coin.getApprovedSupply() + "\",");bw.newLine();
                bw.write("\t\"" + "firstSeen\":" + "\"" + coin.getFirstSeen() + "\",");bw.newLine();
                bw.write("\t\"" + "change\":" + "\"" + coin.getChange() + "\",");bw.newLine();
                bw.write("\t\"" + "rank\":" + "\"" + coin.getRank() + "\"");
                bw.write("}");
                count++;
                if (count < _data.size()) {bw.write(",");}
                bw.newLine();
            }
            bw.write("}}");
            // Close file writers
            bw.close();
            _fos.close();
        }
    }
}
