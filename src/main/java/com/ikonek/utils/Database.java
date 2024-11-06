package com.ikonek.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Database {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public Database() {
        loadDbProperties();
        test();
    }

    private void loadDbProperties() {
        try (FileInputStream input = new FileInputStream("src/main/resources/db.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            this.dbUrl = prop.getProperty("db.url");
            this.dbUsername = prop.getProperty("db.username");
            this.dbPassword = prop.getProperty("db.password");
        } catch (IOException ex) {
            System.err.println("Error loading database properties: " + ex.getMessage());
            throw new RuntimeException("Failed to load database properties", ex);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }

    }

    // for dbms sample testing
    public void test(){
        try{
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM HOSPITALS");

            while(resultSet.next()){
                System.out.println(resultSet.getString("hospital_id"));
                System.out.println(resultSet.getString("hospital_add_line1"));
                System.out.println(resultSet.getString("hospital_city"));
                System.out.println(resultSet.getString("hospital_province"));
                System.out.println(resultSet.getString("hospital_region"));
                System.out.println(resultSet.getString("hospital_contact_number"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
