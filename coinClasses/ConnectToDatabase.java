/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author Kyle
 */
public class ConnectToDatabase {
    
    public ConnectToDatabase() {
        try {
            
            /**
             * This works
             */
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/coins", "root", "nfjf340");
            Statement st = con.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
