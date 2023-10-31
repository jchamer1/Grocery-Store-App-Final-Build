package edu.hfcc.grocery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDatabase {

        public static Connection connectToDatabase() {
            String databaseUrl = "jdbc:postgresql://castor.db.elephantsql.com/tvpgacyg";
            Connection connection = null;

            try {
                connection = DriverManager.getConnection(databaseUrl, "tvpgacyg", "k_0ELyF3KNpgVcx7h006WQjBXSm9BpzQ");
            } catch (SQLException e) {
                System.out.println("Trouble connecting to database: " + e.getMessage());
            }

            return connection;
        }
}
