package com.example.blanza;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface for mapping rows from a JDBC ResultSet to domain objects.
 * <p>
 * The ResultSetMapper interface provides a standardized way to convert database query results
 * into application domain objects. It follows the functional interface pattern, allowing
 * implementations to be provided as lambda expressions or method references for concise,
 * readable code.
 * <p>
 * This interface is a key component in the data access layer of the Balanza application,
 * used by database access objects (DAOs) to transform raw database records into rich domain
 * objects that can be used by the business logic layer.
 * <p>
 * Example usage:
 * <pre>{@code
 * // Define a mapper for Income objects
 * ResultSetMapper<Income> incomeMapper = rs -> new Income(
 *     rs.getInt("id"),
 *     rs.getInt("user_id"),
 *     rs.getString("source"),
 *     rs.getDouble("amount"),
 *     rs.getDate("pay_date").toLocalDate()
 * );
 *
 * // Use the mapper in a database query method
 * public List<Income> getAllIncomes() {
 *     List<Income> incomes = new ArrayList<>();
 *     String sql = "SELECT * FROM incomes WHERE user_id = ?";
 *     
 *     try (PreparedStatement stmt = connection.prepareStatement(sql)) {
 *         stmt.setInt(1, getCurrentUserId());
 *         try (ResultSet rs = stmt.executeQuery()) {
 *             while (rs.next()) {
 *                 incomes.add(incomeMapper.map(rs));
 *             }
 *         }
 *     } catch (SQLException e) {
 *         // Handle exception
 *     }
 *     return incomes;
 * }
 * }</pre>
 *
 * @param <T> The type of object that will be created from each ResultSet row
 * 
 * @see java.sql.ResultSet
 * @see java.sql.SQLException
 */
@FunctionalInterface
public interface ResultSetMapper<T> {
    
    /**
     * Maps a single row from a ResultSet to an object of type T.
     * <p>
     * This method is called for each row in a ResultSet, typically within a loop
     * that iterates through query results. The implementation should extract values
     * from the current row of the ResultSet and construct an appropriate domain object.
     * <p>
     * Implementations of this method should not call {@code rs.next()} or manipulate
     * the ResultSet's cursor position. The caller is responsible for positioning the
     * cursor at the correct row before invoking this method.
     * <p>
     * Any exceptions thrown during the extraction or mapping process are propagated
     * as SQLException, allowing the caller to handle database access errors uniformly.
     *
     * @param rs The ResultSet containing the data to be mapped, positioned at the
     *           current row that should be mapped to an object
     * @return An instance of T created from the current ResultSet row
     * @throws SQLException If a database access error occurs or if the column
     *                      names or types requested do not match those in the ResultSet
     */
    T map(ResultSet rs) throws SQLException;
}