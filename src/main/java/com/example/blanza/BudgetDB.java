package com.example.blanza;

import java.util.List;

/**
 * BudgetDB is a database access class for managing Budget entities.
 * It provides methods to insert, retrieve, and update budget records
 * associated with users in the application.
 *
 * This class extends the generic Database class, providing
 * Budget-specific implementations for CRUD operations.
 *
 * Usage:
 * - Insert a new budget for a user
 * - Retrieve all budgets for the current user
 * - Retrieve a budget by category and user
 * - Update an existing budget
 *
 * SQL queries are referenced by name and are expected to be defined
 * in the underlying Database class or configuration.
 */
public class BudgetDB extends Database<Budget> {

    /**
     * Inserts a new Budget record into the database.
     * The parameters are set in the order expected by the SQL statement.
     *
     * @param budget The Budget object to insert.
     */
    @Override
    public void insertToDatabase(Budget budget) {
        executeUpdateQuery("insert_budget", (stmt) -> {
            
            stmt.setInt(1, budget.getCurrentUserId());         
            stmt.setString(2, budget.getCategory());    
            stmt.setDouble(3, budget.getAmount());      
            stmt.setDouble(4, budget.getActual_spend()); 
            stmt.setDouble(5, budget.getRemaining_budget()); 
        });
    }

    /**
     * Retrieves all Budget records for the current user from the database.
     *
     * @return List of Budget objects for the current user.
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
     * Retrieves a Budget record by category for a specific user.
     *
     * @param category The budget category to search for.
     * @param userId The user ID associated with the budget.
     * @return The Budget object if found, or null if not found.
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
     * Updates an existing Budget record in the database.
     *
     * @param budget The Budget object containing updated values.
     */
    public void updateBudget(Budget budget) {
        executeUpdateQuery("update_budget", stmt -> {
            stmt.setString(1, budget.getCategory());
            stmt.setDouble(2, budget.getAmount());
            stmt.setDouble(3, budget.getActual_spend());
            stmt.setDouble(4, budget.getRemaining_budget());
            stmt.setInt(5, budget.getId());
            stmt.setInt(6, budget.getCurrentUserId());
        });
    }
}