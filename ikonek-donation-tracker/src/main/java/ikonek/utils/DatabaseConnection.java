package ikonek.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {

    private static Connection connection = null;

    // Load the database connection properties from db.properties file
    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("Unable to find db.properties");
            }
            properties.load(input);
        }
        return properties;
    }

    // Establish the database connection using the loaded properties
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties properties = loadProperties();
                String url = properties.getProperty("db.url");
                String user = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");
                String driver = properties.getProperty("db.driver");

                Class.forName(driver); // Load the database driver

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established successfully.");
            } catch (SQLException e) {
                System.err.println("Database connection error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error loading database properties: " + e.getMessage());
            } catch (ClassNotFoundException e) {  // Handles ClassNotFoundException if driver not found
                System.err.println("Database driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    // Close the database connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
