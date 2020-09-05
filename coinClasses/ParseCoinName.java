package coinClasses;
/**
 * This class reads from a file named "coinNamesIds.txt"
 * to convert strings to coin Id's and vice-versa.
 * 
 * The ID of a coin is used to make api calls for
 * more information regarding the coin.
 * 
 * - Kyle
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParseCoinName {
    
    private int coinId;
    private String coinName;
    private final File COINS;
    
    // ========== CONSTRUCTORS ==========
    
    public ParseCoinName(String _str) {
        COINS = new File("coinNamesIds.txt");
        String first = _str.split(" ")[0];
        // Make sure we can read the file with a try/catch block
        try {
            Scanner scanner = new Scanner(COINS);
            // Scan the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                // Search for given name
                if (parts[0].equalsIgnoreCase(first)){
                    coinId = Integer.parseInt(parts[parts.length - 1]);
                    coinName = parts[0];
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseCoinName.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ParseCoinName(int _id) {
        COINS = new File("coinNamesIds.txt");
        // Make sure we can read the file with a try/catch block
        try {
            Scanner scanner = new Scanner(COINS);
            // Scan the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                // Search for given ID
                if (Integer.parseInt(parts[parts.length - 1]) == _id){
                    coinId = Integer.parseInt(parts[parts.length - 1]);
                    coinName = parts[0];
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseCoinName.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ========== GETTERS ==========
    
    /**
     * Return the coin id
     * @return 
     */
    public int getId() {
        return this.coinId;
    }
    
    /**
     * Return the coin name
     * @return 
     */
    public String getName() {
        return this.coinName;
    }
    
}
