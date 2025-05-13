package com.example.blanza;

import java.time.LocalDate;
import java.util.List;

public class IncomeDB extends Database<Income> {
    @Override
    public void insertToDatabase(Income income) {
        executeUpdateQuery("insert_income", (stmt) -> {
            stmt.setInt(1, currentUserId);
            stmt.setString(2, income.getIncome_source());
            stmt.setDouble(3, income.getAmount());
            stmt.setString(4, income.getPay_date().toString());
        });
    }

    @Override
    public List<Income> getAllFromDatabase() {
        return executeQuery("select_all_incomes", stmt -> {
            stmt.setInt(1, SessionService.getCurrentUserId());
        }, rs -> {
            int userId = rs.getInt("user_id");
            String source = rs.getString("source"); // Changed from "income_source" to "source"
            double amount = rs.getDouble("amount");
            LocalDate payDate = LocalDate.parse(rs.getString("date")); // Changed from "pay_date" to "date"

            return new Income(userId, source, amount, payDate);
        });
    }
}