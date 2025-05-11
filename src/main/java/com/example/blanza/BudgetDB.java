package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BudgetDB {
    private final static Dotenv dotenv = Dotenv.configure().directory(".").load();
    private final static String DB_URL = dotenv.get("DB_URL");

    public static void createBudgetTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("create_budget_table");

                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}