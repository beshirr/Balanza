package com.example.blanza;

import java.time.LocalDate;
import java.util.List;

/**
 * Database access class for managing Income records in the Balanza application.
 * <p>
 * The IncomeDB class extends the abstract Database class to provide specific
 * implementations for storing and retrieving Income entities in the application's
 * database. This class handles all database operations related to user income records,
 * including:
 * <ul>
 *   <li>Inserting new income entries</li>
 *   <li>Retrieving all income records for the current user</li>
 *   <li>Mapping between database records and Income objects</li>
 * </ul>
 * <p>
 * The class uses prepared SQL statements defined elsewhere (likely in a properties file
 * or resource) referenced by keys like "insert_income" and "select_all_incomes".
 * Database connections are managed by the parent Database class.
 * <p>
 * This class follows the Data Access Object (DAO) pattern, providing a clean separation
 * between the business logic and the database storage implementation.
 *
 * @see Income
 * @see Database
 */
public class IncomeDB extends Database<Income> {
    
    /**
     * Inserts a new Income record into the database.
     * <p>
     * This method persists the provided Income object in the database by:
     * <ol>
     *   <li>Preparing an SQL insert statement using the "insert_income" query</li>
     *   <li>Setting parameters from the Income object's properties</li>
     *   <li>Executing the prepared statement</li>
     * </ol>
     * <p>
     * The method uses the currentUserId from the Database parent class as the user
     * identifier for the record. The income's source, amount, and payment date are
     * also stored.
     * <p>
     * The payment date is converted to a string representation before storage.
     *
     * @param income The Income object to persist in the database
     * @throws RuntimeException If a database error occurs during the insert operation
     */
    @Override
    public void insertToDatabase(Income income) {
        executeUpdateQuery("insert_income", (stmt) -> {
            stmt.setInt(1, currentUserId);
            stmt.setString(2, income.getIncome_source());
            stmt.setDouble(3, income.getAmount());
            stmt.setString(4, income.getPay_date().toString());
        });
    }

    /**
     * Retrieves all income records for the current user from the database.
     * <p>
     * This method:
     * <ol>
     *   <li>Prepares an SQL select statement using the "select_all_incomes" query</li>
     *   <li>Sets the current user ID as a parameter to filter records</li>
     *   <li>Executes the query and processes the result set</li>
     *   <li>Maps each database row to an Income object</li>
     *   <li>Returns a list of all Income objects for the current user</li>
     * </ol>
     * <p>
     * The method uses the SessionService to get the current user's ID for filtering
     * records. For each row in the result set, the method creates a new Income object
     * with properties populated from the database columns.
     * <p>
     * Note: The database column names ("source" and "date") differ from the corresponding
     * field names in the Income class ("income_source" and "pay_date").
     *
     * @return A List of Income objects representing all income records for the current user
     * @throws RuntimeException If a database error occurs during the query operation
     */
    @Override
    public List<Income> getAllFromDatabase() {
        return executeQuery("select_all_incomes", stmt -> {
            stmt.setInt(1, SessionService.getCurrentUserId());
        }, rs -> {
            int userId = rs.getInt("user_id");
            String source = rs.getString("source"); 
            double amount = rs.getDouble("amount");
            LocalDate payDate = LocalDate.parse(rs.getString("date")); 

            return new Income(userId, source, amount, payDate);
        });
    }
}