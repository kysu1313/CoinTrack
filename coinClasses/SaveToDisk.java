package coinClasses;

import static com.sun.deploy.config.OSType.isMac;
import static com.sun.javafx.PlatformUtil.isWindows;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.filechooser.FileSystemView;

/**
 * This class saves a users saved coins to either a text file,
 * excel file, or JSON file to their computer.
 * @author Kyle
 */
public class SaveToDisk {

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
//            this.path = Paths.get(DEFAULT_WINDOWS_LOCATION);
            Files.createDirectories(this.path);
        } else if (isMac()) {
            this.system = "mac";
            this.dir = _dir;
            this.path = Paths.get(DEFAULT_MAC_LOCATION);
            Files.createDirectories(this.path);
        }
    }

    public void saveTableAsText(LinkedList<SingleCoin> _data) throws IOException {
        Date date = new Date(System.currentTimeMillis());
        String fileName = "coin-track-" + TIME_FORMAT.format(date) + ".txt";
        CoinRankApi cri = new CoinRankApi();
        File file = new File(fileName);
	FileOutputStream fos = new FileOutputStream(fileName);
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

}
