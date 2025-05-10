package com.example.blanza;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDB {
    private static final String DB_URL = "jdbc:sqlite:Balanza.db";

    public ExpenseDB() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = SQLLoader.get("create_table");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertExpense(Expense e) {
        String sql = SQLLoader.get("insert_expense");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getCategory());
            pstmt.setFloat(2, e.getAmount());
            pstmt.setString(3, e.getDate().toString());
            pstmt.setString(4, e.getPaymentMethod());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = SQLLoader.get("select_all_expenses");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String category = rs.getString("category");
                float amount = rs.getFloat("amount");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String method = rs.getString("payment_method");

                expenses.add(new Expense(category, amount, date, method));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return expenses;
    }
}
