package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final static Dotenv dotenv = Dotenv.configure().directory(".").load();
    protected final static String DB_URL = dotenv.get("DB_URL");

    protected static void createTable(String sql_query_name) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get(sql_query_name);

                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
