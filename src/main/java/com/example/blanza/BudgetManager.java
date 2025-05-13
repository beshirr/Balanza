package com.example.blanza;

import java.util.List;

/**
 * Manager class for handling Budget entities in the Balanza application.
 * This class extends the generic FinancialEntityManager to provide specific
 * functionality for Budget objects.
 * 
 * BudgetManager is responsible for:
 * - Validating budget data before saving
 * - Persisting budgets to the database
 * - Retrieving budgets from the database
 * 
 * This implementation uses BudgetDB for database operations.
 * 
 * @author Your Name
 * @version 1.0
 * @see Budget
 * @see FinancialEntityManager
 * @see BudgetDB
 */
public class BudgetManager extends FinancialEntityManager<Budget> {
    /** Database access object for budget operations */
    private final BudgetDB db = new BudgetDB();
    
    /**
     * Validates a budget entity before saving.
     * A budget is considered valid if its amount is not negative.
     *
     * @param budget The budget to validate
     * @return true if the budget is valid, false otherwise
     */
    @Override
    protected boolean validate(Budget budget) {
        return budget.getAmount() >= 0;
    }

    /**
     * Saves a validated budget to the database.
     * This method delegates the database operation to the BudgetDB instance.
     *
     * @param budget The budget to be saved to the database
     */
    @Override
    protected void saveToDatabase(Budget budget) {
        db.insertToDatabase(budget);
    }

    /**
     * Loads all budgets from the database.
     * Retrieves the complete list of budgets stored in the system.
     *
     * @return A list of all budgets in the database
     */
    @Override
    protected List<Budget> loadFromDatabase() {
        return db.getAllFromDatabase();
    }
}