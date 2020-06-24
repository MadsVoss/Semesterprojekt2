/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Connects us to the database that we use
public class SQLConnector {
    private static Connection connection;
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mysql://db.diplomportal.dk/s190608?"
                        + "user=s190608&password=5USibIiZSIh7RR85vBFgH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}