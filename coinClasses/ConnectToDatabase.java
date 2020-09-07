/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;

/**
 *
 * @author Kyle
 */
public class ConnectToDatabase {
    
    public ConnectToDatabase(String _uuid, String _symbol, String _name, String _price, Date _date) {
        try {
            
            /**
             * This works
             */
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://sql9.freemysqlhosting.net:3306/sql9364184", "sql9364184", "vLIxP8hK8C");
            Statement st = con.createStatement();
            float newPrice = Float.parseFloat(_price);
            // Insert statement, using prepared statements
            String query = " insert into coins (uuid, symbol, name, price, date)"
                    + " values (?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, _uuid);
            preparedStmt.setString(2, _symbol);
            preparedStmt.setString(3, _name);
            preparedStmt.setFloat(4, newPrice);
            preparedStmt.setDate(5, _date);

            // execute the preparedstatement
            preparedStmt.execute();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
