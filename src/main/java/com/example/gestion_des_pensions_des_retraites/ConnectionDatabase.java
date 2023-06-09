package com.example.gestion_des_pensions_des_retraites;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {
    private static final String url = "jdbc:postgresql://localhost/gestion_des_pensions_des_retraites";
    private static final String user = "postgres";
    private static final String password = "1532";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the PostgreSQL server: " + e.getMessage());
        }
        return conn;
    }
}
