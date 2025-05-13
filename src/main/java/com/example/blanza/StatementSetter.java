package com.example.blanza;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Functional interface for setting parameters on a JDBC PreparedStatement.
 * <p>
 * The StatementSetter interface provides a standardized callback mechanism for 
 * setting parameters on prepared SQL statements. It acts as a bridge between
 * generic database operations and the specific parameter values needed for a
 * particular query execution.
 * <p>
 * This functional interface is primarily used in the application's database 
 * access layer to separate the concerns of:
 * <ul>
 *   <li>SQL query definition (provided by {@link SQLLoader})</li>
 *   <li>Database connection management (handled by {@link Database})</li>
 *   <li>Parameter binding (implemented via this interface)</li>
 *   <li>Result processing (typically handled by result handlers)</li>
 * </ul>
 * <p>
 * The interface follows the Java 8+ functional interface pattern, allowing for 
 * concise lambda expression implementations at call sites. This approach reduces
 * boilerplate code and improves readability when executing parameterized queries.
 * <p>
 * Example usage:
 * <pre>{@code
 * // Simple implementation with lambda expression
 * StatementSetter userParams = stmt -> {
 *     stmt.setString(1, username);
 *     stmt.setString(2, hashedPassword);
 * };
 * 
 * // Using the setter with a database operation
 * User user = DatabaseManager.executeQuery(
 *     SQLLoader.get("findUserByCredentials"),
 *     userParams,
 *     resultSet -> {
 *         // Map result set to User object
 *     }
 * );
 * }</pre>
 * <p>
 * By using this functional interface, the application can maintain a clean
 * separation between SQL queries and their parameters, which improves code
 * maintainability and reduces the risk of SQL injection vulnerabilities.
 * 
 * @see Database
 * @see SQLLoader
 * @see PreparedStatement
 */
@FunctionalInterface
public interface StatementSetter {
    
    /**
     * Sets parameters on a prepared statement before execution.
     * <p>
     * This method is called to bind parameter values to the placeholders (?) in
     * a SQL statement. Implementations should set all required parameters in the
     * correct order, corresponding to the parameter indices in the SQL statement.
     * <p>
     * Parameter indices in JDBC PreparedStatements are 1-based (not 0-based).
     * For example, the first parameter is set with:
     * <pre>{@code stmt.setString(1, value);}</pre>
     * <p>
     * The method can handle all parameter types supported by JDBC, including:
     * <ul>
     *   <li>Primitive types (using setInt, setLong, setBoolean, etc.)</li>
     *   <li>Strings (using setString)</li>
     *   <li>Dates and times (using setDate, setTimestamp)</li>
     *   <li>Binary data (using setBytes, setBinaryStream)</li>
     *   <li>Null values (using setNull)</li>
     * </ul>
     * <p>
     * The implementation must properly handle type conversion and null values to
     * ensure SQL statements execute correctly. Any SQLException thrown during
     * parameter setting is propagated to the caller.
     *
     * @param stmt The prepared statement on which to set parameters
     * @throws SQLException If a database access error occurs or the parameter
     *                      types are incompatible with the SQL statement
     */
    void setParameters(PreparedStatement stmt) throws SQLException;
}