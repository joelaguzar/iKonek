package com.ikonek;

import java.util.Scanner;
import com.ikonek.utils.Database;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.getConnection();
    }
}
