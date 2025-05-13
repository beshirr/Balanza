package com.example.blanza;

import java.util.List;

/**
 * Manages expense-related operations in the Balanza financial management application.
 * <p>
 * The ExpenseManager class extends FinancialEntityManager to provide specialized
 * functionality for handling Expense objects. It acts as a facade between the
 * application logic and the expense data storage layer, providing validation,
 * persistence, and retrieval operations.
 * <p>
 * This manager encapsulates the expense-specific business rules and data access
 * operations, ensuring that all expenses are valid before they are stored in the
 * database. It maintains a clear separation between the business logic and data
 * access layers of the application.
 *
 * @see FinancialEntityManager
 * @see Expense
 * @see ExpenseDB
 */
public class ExpenseManager extends FinancialEntityManager<Expense> {
    /** The database access object used for expense persistence operations */
    private final ExpenseDB db = new ExpenseDB();
    
    /**
     * Validates an expense before it can be saved.
     * <p>
     * This method enforces the business rule that expenses must have a
     * non-negative amount. An expense with a negative amount is considered
     * invalid and will not be saved to the database.
     *
     * @param expense The expense to validate
     * @return true if the expense is valid (amount >= 0), false otherwise
     */
    @Override
    protected boolean validate(Expense expense) {
        return expense.getAmount() >= 0;
    }

    /**
     * Saves a valid expense to the database.
     * <p>
     * This method is called by the parent class after an expense has been
     * validated. It delegates the actual database operation to the ExpenseDB
     * instance.
     *
     * @param expense The validated expense to save
     */
    @Override
    protected void saveToDatabase(Expense expense) {
        db.insertToDatabase(expense);
    }
    
    /**
     * Loads all expenses for the current user from the database.
     * <p>
     * This method retrieves the complete list of expenses associated with
     * the current user ID from the database. The current user ID must be
     * properly set in the ExpenseDB instance before calling this method.
     *
     * @return A list of all expenses for the current user
     */
    @Override
    protected List<Expense> loadFromDatabase() {
        return db.getAllFromDatabase();
    }
}