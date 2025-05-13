package com.example.blanza;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDB extends Database {
    public static void insertExpense(Expense e) {
        String sql = SQLLoader.get("insert_expense");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(2, e.getCategory());
            pstmt.setFloat(3, e.getAmount());
            pstmt.setString(4, e.getDate().toString());
            pstmt.setString(5, e.getPaymentMethod());
            pstmt.setInt(1, e.getCurrent_user_id());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        int userId = SessionService.getCurrentUserId();
        if (userId <= 0) {
            System.err.println("Error: Invalid user ID");
            return expenses;
        }

        String sql = SQLLoader.get("select_all_expenses");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("category");
                float amount = rs.getFloat("amount");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String method = rs.getString("payment_method");

                expenses.add(new Expense(id, category, amount, date, method));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return expenses;
    }
}