package com.example.blanza;

import java.time.LocalDate;
import java.util.List;

public class ExpenseDB extends Database<Expense> {
    @Override
    public void insertToDatabase(Expense e) {
        executeUpdateQuery("insert_expense", (stmt) -> {
            stmt.setInt(1, e.getCurrentUserId());
            stmt.setString(2, e.getCategory());
            stmt.setDouble(3, e.getAmount());
            stmt.setString(4, e.getDate().toString());
            stmt.setString(5, e.getPaymentMethod());
        });
    }

    @Override
    public List<Expense> getAllFromDatabase() {
        if (currentUserId <= 0) {
            System.err.println("Error: Invalid user ID");
            return List.of();
        }

        return executeQuery("select_all_expenses", stmt -> {
            stmt.setInt(1, currentUserId);
        }, rs -> {
            int id = rs.getInt("id");
            String category = rs.getString("category");
            double amount = rs.getDouble("amount");
            LocalDate date = LocalDate.parse(rs.getString("date"));
            String method = rs.getString("payment_method");
            
            return new Expense(currentUserId, category, amount, date, method);
        });
    }
}