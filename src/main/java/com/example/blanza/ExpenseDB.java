package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDB {
    Dotenv dotenv = Dotenv.configure().directory(".").load();
    private final String DB_URL = dotenv.get("DB_URL");

    public void insertExpense(Expense e) {
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

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = SQLLoader.get("select_all_expenses");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Session.getCurrentUserId()); 
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
