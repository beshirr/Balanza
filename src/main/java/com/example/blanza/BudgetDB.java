package com.example.blanza;

import java.util.List;

public class BudgetDB extends Database<Budget> {

    @Override
    public void insertToDatabase(Budget budget) {
        executeUpdateQuery("insert_budget", stmt -> {
            stmt.setString(1, budget.getCategory());
            stmt.setDouble(2, budget.getAmount());
            stmt.setDouble(3, budget.getActual_spend());
            stmt.setDouble(4, budget.getRemaining_budget());
            stmt.setInt(5, budget.getCurrentUserId());
        });
    }

    @Override
    public List<Budget> getAllFromDatabase() {
        if (currentUserId <= 0) {
            System.err.println("Error: Invalid user ID");
            return List.of();
        }

        return executeQuery("select_all_budgets", stmt -> {
            stmt.setInt(1, currentUserId);
        }, rs -> {
            String category = rs.getString("category");
            double amount = rs.getDouble("budget_amount");
            double actual_spent = rs.getDouble("actual_spend");
            return new Budget(category, amount, actual_spent, currentUserId
            );
        });
    }

    public void updateBudgetSpend(int budgetId, double actualSpend, double remainingBudget) {
        executeUpdateQuery("update_budget_spend", stmt -> {
            stmt.setDouble(1, actualSpend);
            stmt.setDouble(2, remainingBudget);
            stmt.setInt(3, budgetId);
        });
    }

    public void updateBudget(Budget budget) {
        executeUpdateQuery("update_budget", stmt -> {
            stmt.setDouble(1, budget.getActual_spend());
            stmt.setDouble(2, budget.getRemaining_budget());
            stmt.setString(3, budget.getCategory());
            stmt.setInt(4, budget.getCurrentUserId());
        });
    }

    public Budget getBudgetByCategory(String category, int userId) {
        List<Budget> budgets = executeQuery("select_budget_by_category_and_user", stmt -> {
            stmt.setString(1, category);
            stmt.setInt(2, userId);
        }, rs -> {
            String budgetCategory = rs.getString("category");
            double budgetAmount = rs.getDouble("budget_amount");
            double actualSpend = rs.getDouble("actual_spend");
            double remainingBudget = rs.getDouble("remaining_budget");

            Budget budget = new Budget(budgetCategory, budgetAmount, actualSpend, userId);
            budget.setRemaining_budget(remainingBudget);
            return budget;
        });

        return budgets.isEmpty() ? null : budgets.getFirst();
    }

    public void getBudgetsByUserId(int userId) {
        List<Budget> budgets = executeQuery("select_budget_by_user_id", stmt -> {
            stmt.setInt(1, userId);
        }, rs -> {
            String category = rs.getString("category");
            double budgetAmount = rs.getDouble("budget_amount");
            double actualSpend = rs.getDouble("actual_spend");
            return new Budget(category, budgetAmount, actualSpend, userId);
        });

        for (Budget b : budgets) {
            System.out.println("Category: " + b.getCategory() +
                    ", Budget Amount: " + b.getAmount() +
                    ", Actual Spend: " + b.getActual_spend() +
                    ", Remaining Budget: " + b.getRemaining_budget());
        }
    }
}
