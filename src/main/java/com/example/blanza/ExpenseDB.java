package com.example.blanza;

import java.time.LocalDate;
import java.util.List;

/**
 * Data access object for expense-related database operations in the Balanza application.
 * <p>
 * ExpenseDB extends the generic Database class to provide specific implementation
 * for storing, retrieving, and managing Expense objects in the database. It handles
 * the mapping between Expense domain objects and their database representation,
 * including parameter binding for prepared statements and result set parsing.
 * <p>
 * This class uses SQL query templates that are retrieved by key from the SQLLoader.
 * These SQL query templates include:
 * <ul>
 *   <li><code>insert_expense</code> - For adding new expense records</li>
 *   <li><code>select_all_expenses</code> - For retrieving all expenses for a user</li>
 * </ul>
 * <p>
 * All database operations in this class are user-specific and rely on the currentUserId
 * that is set on the Database base class.
 *
 * @see Database
 * @see Expense
 * @see SQLLoader
 */
public class ExpenseDB extends Database<Expense> {
    
    /**
     * Inserts a new expense record into the database.
     * <p>
     * This method takes an Expense object and maps its properties to the corresponding
     * database columns using a prepared statement. The SQL query used is retrieved 
     * from SQLLoader using the "insert_expense" key.
     * <p>
     * The following expense properties are stored:
     * <ul>
     *   <li>User ID - to associate the expense with a specific user</li>
     *   <li>Category - the expense category (e.g., "Food", "Transportation")</li>
     *   <li>Amount - the monetary value of the expense</li>
     *   <li>Date - the date when the expense occurred (stored as string)</li>
     *   <li>Payment Method - how the expense was paid (e.g., "Cash", "Credit Card")</li>
     * </ul>
     *
     * @param e The Expense object to be stored in the database
     */
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

    /**
     * Retrieves all expense records for the current user from the database.
     * <p>
     * This method executes a query to fetch all expenses associated with the currently
     * set user ID. It maps each database row to an Expense object, converting database
     * column values to the appropriate Java types.
     * <p>
     * The SQL query used is retrieved from SQLLoader using the "select_all_expenses" key.
     * <p>
     * If the current user ID is not set or is invalid (less than or equal to 0), 
     * an error message is logged and an empty list is returned.
     *
     * @return A list of Expense objects for the current user, or an empty list if the user ID is invalid
     */
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