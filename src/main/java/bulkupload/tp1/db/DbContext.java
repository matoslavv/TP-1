package bulkupload.tp1.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbContext {
    /**
     * Create a connection to the database
     * @return Connection to the database to create queries or null if connection failed
     */
    public static Connection create() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // TODO: Move to environment variables
            Connection connection = DriverManager.getConnection("jdbc:mysql://mysql80.r2.websupport.sk:3314/CryptogramsTestDB?serverTimezone=Europe/Prague", "lew51f33public", "Af7MaDIp1W");

            if (connection != null) {
                System.out.println("Connection established successfully!");
            } else {
                System.out.println("Failed to establish connection!");
            }

            return connection;
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }
}
