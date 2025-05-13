package com.example.blanza;

import java.time.LocalDate;
import java.util.List;

public class IncomeDB extends Database<Income> {
    @Override
    public void insertToDatabase(Income income) {
        executeUpdateQuery("insert_income", (stmt) -> {
            stmt.setInt(1, income.getCurrentUserId());
            stmt.setString(2, income.getIncome_source());
            stmt.setDouble(3, income.getAmount());
            stmt.setString(4, income.getPay_date().toString());
        });
    }

    @Override
    public List<Income> getAllFromDatabase() {
        if (currentUserId <=0 ) {
            System.err.println("Error: Invalid user Id");
        }

        return executeQuery("select_all_incomes", stmt -> {
            stmt.setInt(1, currentUserId);
        }, rs -> {
            String source = rs.getString("income_source");
            Double amount = rs.getDouble("amount");
            LocalDate pay_date = LocalDate.parse(rs.getString("pay_date"));
            return new Income(currentUserId, source, amount, pay_date);
        });
    }
}