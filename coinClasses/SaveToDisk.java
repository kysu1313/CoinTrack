package coinClasses;

import static com.sun.deploy.config.OSType.isMac;
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
import tabControllers.AlertMessages;
//import org.apache.poi.ss.usermodel.Cell;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * This class saves a users saved coins to either a text file,
 * excel file, or JSON file to their computer.
 * @author Kyle
 */
public class SaveToDisk implements SaveFileInterface{

    private static String OS = System.getProperty("os.name").toLowerCase();
    private String system;
    private final String DEFAULT_WINDOWS_LOCATION = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
    private final String DEFAULT_MAC_LOCATION = "/Users/$USER/Documents/CoinTrack";
    private File dir;
    private final SimpleDateFormat TIME_FORMAT= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Path path;
    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;


    public SaveToDisk() throws IOException {
        // Used to determine if user is on Mac or Windows
        if (isWindows()) {
            this.system = "windows";
            this.dir = new File(this.DEFAULT_WINDOWS_LOCATION);
        } else if (isMac()) {
            this.system = "mac";
            this.dir = new File(this.DEFAULT_MAC_LOCATION);
            this.path = Paths.get(DEFAULT_MAC_LOCATION);
            Files.createDirectories(this.path);
        }
    }

    public SaveToDisk(File _dir) throws IOException {
        // Used to determine if user is on Mac or Windows
        if (isWindows()) {
            this.system = "windows";
            this.dir = _dir;
        } else if (isMac()) {
            this.system = "mac";
            this.dir = _dir;
            this.path = Paths.get(DEFAULT_MAC_LOCATION);
            Files.createDirectories(this.path);
        }
    }

    /**
     * Save LinkedList of single coins as a text file.
     * @param _data
     * @throws IOException
     */
    @Override
    public void saveTableAsText(LinkedList<SingleCoin> _data) throws IOException {
        Date date = new Date(System.currentTimeMillis());
        String fileName = "coin-track-" + TIME_FORMAT.format(date) + ".txt";
	FileOutputStream fos = new FileOutputStream(this.dir + "\\" + fileName);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            for (SingleCoin coin : _data) {
                bw.write(coin.getName() + ": ");
                bw.write("id: " + coin.getId()+"");
                bw.write("uuid: " + coin.getUuid());
                bw.write("symbol: " + coin.getSymbol());
                bw.write("iconUrl: " + coin.getIconUrl());
                bw.write("confirmedSupply: " + coin.getConfirmedSupply()+"");
                bw.write("numberOfMarkets: " + coin.getNumberOfMarkets()+"");
                bw.write("numberOfExchanges: " + coin.getNumberOfExchanges()+"");
                bw.write("type: " + coin.getType());
                bw.write("volume: " + coin.getVolume()+"");
                bw.write("marketCap: " + coin.getMarketCap()+"");
                bw.write("price: " + coin.getPrice()+"");
                bw.write("circulatingSupply: " + coin.getCirculatingSupply()+"");
                bw.write("totalSupply: " + coin.getTotalSupply()+"");
                bw.write("approvedSupply: " + coin.getApprovedSupply()+"");
                bw.write("firstSeen: " + coin.getFirstSeen()+"");
                bw.write("change: " + coin.getChange()+"");
                bw.write("rank: " + coin.getRank()+"");
                bw.newLine();
            }
            bw.close();
            fos.close();
        }
    }

    /**
     * Save LinkedList of single coins as a text file with given file name.
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
        // Loop over coins and add a row for each
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
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
                bw.write("circulatingSupply: " + coin.getCirculatingSupply()+", ");
                bw.write("totalSupply: " + coin.getTotalSupply()+", ");
                bw.write("approvedSupply: " + coin.getApprovedSupply()+", ");
                bw.write("firstSeen: " + coin.getFirstSeen()+", ");
                bw.write("change: " + coin.getChange()+", ");
                bw.write("rank: " + coin.getRank()+", ");
                bw.newLine();
            }
            bw.close();
            fos.close();
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
//        header.
        int colNum = 0;
        int rowNum = 0;
        Row header = spreadsheet.createRow(rowNum);
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
        header.createCell(colNum+12).setCellValue("circulatingSupply");
        header.createCell(colNum+13).setCellValue("totalSupply");
        header.createCell(colNum+14).setCellValue("approvedSupply");
        header.createCell(colNum+15).setCellValue("firstSeen");
        header.createCell(colNum+16).setCellValue("change");
        header.createCell(colNum+17).setCellValue("rank");
        colNum = 0;
        rowNum++;
        for (SingleCoin coin : _data) {
            Row currentRow = spreadsheet.createRow(rowNum);
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
            currentRow.createCell(colNum + 12).setCellValue(coin.getCirculatingSupply() + "");
            currentRow.createCell(colNum + 13).setCellValue(coin.getTotalSupply() + "");
            currentRow.createCell(colNum + 14).setCellValue(coin.getApprovedSupply() + "");
            currentRow.createCell(colNum + 15).setCellValue(coin.getFirstSeen() + "");
            currentRow.createCell(colNum + 16).setCellValue(coin.getChange() + "");
            currentRow.createCell(colNum + 17).setCellValue(coin.getRank() + "");
        }
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
//        header.
        int colNum = 0;
        int rowNum = 0;
        Row header = spreadsheet.createRow(rowNum);
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
        header.createCell(colNum+12).setCellValue("circulatingSupply");
        header.createCell(colNum+13).setCellValue("totalSupply");
        header.createCell(colNum+14).setCellValue("approvedSupply");
        header.createCell(colNum+15).setCellValue("firstSeen");
        header.createCell(colNum+16).setCellValue("change");
        header.createCell(colNum+17).setCellValue("rank");
        colNum = 0;
        rowNum++;
        for (SingleCoin coin : _data) {
            Row currentRow = spreadsheet.createRow(rowNum);
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
            currentRow.createCell(colNum + 12).setCellValue(coin.getCirculatingSupply() + "");
            currentRow.createCell(colNum + 13).setCellValue(coin.getTotalSupply() + "");
            currentRow.createCell(colNum + 14).setCellValue(coin.getApprovedSupply() + "");
            currentRow.createCell(colNum + 15).setCellValue(coin.getFirstSeen() + "");
            currentRow.createCell(colNum + 16).setCellValue(coin.getChange() + "");
            currentRow.createCell(colNum + 17).setCellValue(coin.getRank() + "");
        }
        FileOutputStream out = new FileOutputStream(new File(this.dir + "\\" + _fileName + ".xlsx"));
        workbook.write(out);
        out.close();
        System.out.println(".xlsx written successfully");
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
//        Date date = new Date(System.currentTimeMillis());
	FileOutputStream fos;
//        String fileName = "coin-track-" + TIME_FORMAT.format(date);
        // Verify file path is acceptable
        try {
            fos = new FileOutputStream(this.dir + "\\" + _fileName + ".json");
        } catch (FileNotFoundException ex) {
            AlertMessages.showErrorMessage("Bad File Path", "The specified file location could not be found.");
            fos = new FileOutputStream(_fileName + ".txt");
        }
        int count = 0;
        // Loop over coins and add a row for each
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
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
                bw.write("\t\"" + "circulatingSupply\":" + "\"" + coin.getCirculatingSupply() + "\",");bw.newLine();
                bw.write("\t\"" + "totalSupply\":" + "\"" + coin.getTotalSupply() + "\",");bw.newLine();
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
            fos.close();
        }
    }

    /**
     * Save LinkedList of single coins as a json file.
     * @param _data
     * @throws java.io.FileNotFoundException
     * @throws IOException
     */
    @Override
    public void saveAsJson(LinkedList<SingleCoin> _data) throws FileNotFoundException, IOException {
//        Date date = new Date(System.currentTimeMillis());
	FileOutputStream fos;
        Date date = new Date(System.currentTimeMillis());
        String fileName = "coin-track-" + TIME_FORMAT.format(date) + ".json";
        try {
            fos = new FileOutputStream(this.dir + "\\" + fileName + ".json");
        } catch (FileNotFoundException ex) {
            AlertMessages.showErrorMessage("Bad File Path", "The specified file location could not be found.");
            fos = new FileOutputStream(fileName + ".txt");
        }
        int count = 0;
        // Loop over coins and add a row for each
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
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
                bw.write("\t\"" + "circulatingSupply\":" + "\"" + coin.getCirculatingSupply() + "\",");bw.newLine();
                bw.write("\t\"" + "totalSupply\":" + "\"" + coin.getTotalSupply() + "\",");bw.newLine();
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
            fos.close();
        }
    }
}
