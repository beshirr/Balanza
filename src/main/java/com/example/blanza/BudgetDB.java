package com.example.blanza;

import java.util.List;

public class BudgetDB extends Database<Budget> {

    @Override
    public void insertToDatabase(Budget budget) {
        executeUpdateQuery("insert_budget", (stmt) -> {
            // Make sure parameters are in the correct order as defined in the SQL
            stmt.setInt(1, budget.getCurrentUserId());         // user_id
            stmt.setString(2, budget.getCategory());    // category
            stmt.setDouble(3, budget.getAmount());      // budget_amount
            stmt.setDouble(4, budget.getActual_spend()); // actual_spend
            stmt.setDouble(5, budget.getRemaining_budget()); // remaining_budget
        });
    }

    /**
     * Get all budgets for the current user
     */
    @Override
    public List<Budget> getAllFromDatabase() {
        return executeQuery("select_all_budgets", stmt -> {
            stmt.setInt(1, SessionService.getCurrentUserId());
        }, rs -> {
            int id = rs.getInt("id");
            int userId = rs.getInt("user_id");
            String category = rs.getString("category");
            double amount = rs.getDouble("budget_amount");
            double actualSpend = rs.getDouble("actual_spend");
            double remainingBudget = rs.getDouble("remaining_budget");

            Budget budget = new Budget(category, amount, actualSpend, userId);
            budget.setId(id);
            budget.setRemaining_budget(remainingBudget);

            return budget;
        });
    }

    /**
     * Get a budget by category for the current user
     * @param category Budget category
     * @param userId User ID
     * @return Budget or null if not found
     */
    public Budget getBudgetByCategory(String category, int userId) {
        List<Budget> budgets = executeQuery("select_budget_by_category", stmt -> {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
        }, rs -> {
            int id = rs.getInt("id");
            double amount = rs.getDouble("budget_amount");
            double actualSpend = rs.getDouble("actual_spend");
            double remainingBudget = rs.getDouble("remaining_budget");

            Budget budget = new Budget(category, amount, actualSpend, userId);
            budget.setId(id);
            budget.setRemaining_budget(remainingBudget);

            return budget;
        });

        return budgets.isEmpty() ? null : budgets.get(0);
    }

    /**
     * Update an existing budget
     * @param budget Budget to update
     * @return true if successful
     */
    public void updateBudget(Budget budget) {
        executeUpdateQuery("update_budget", stmt -> {
            stmt.setString(1, budget.getCategory());
            stmt.setDouble(2, budget.getAmount());
            stmt.setDouble(3, budget.getActual_spend());
            stmt.setDouble(4, budget.getRemaining_budget());
            stmt.setInt(5, budget.getId());
            stmt.setInt(6, budget.getCurrentUserId());;
        });
    }
}