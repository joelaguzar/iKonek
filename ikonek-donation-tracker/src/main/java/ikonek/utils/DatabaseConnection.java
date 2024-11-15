package ikonek.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {

    private static Connection connection = null;

    // load the database connection properties from db.properties file
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

    //database connection using the loaded properties
    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        Properties properties = loadProperties();
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        //String driver = properties.getProperty("db.driver");

        //Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

}
