package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for database operations in the Balanza application.
 * This class implements a generic data access layer that handles common database 
 * operations like connecting to the database, executing queries, and processing results.
 * <p>
 * The class uses environment variables to configure database connections
 * and provides methods for creating tables, executing update queries, and
 * retrieving data through queries.
 * <p>
 * Specific entity database classes should extend this class and implement
 * the abstract methods for entity-specific database operations.
 *
 * @param <T> The entity type that this database class handles
 */
public abstract class Database<T> {
    /** Environment configuration loaded from .env file */
    private final static Dotenv dotenv = Dotenv.configure().directory(".").load();
    
    /** Database URL retrieved from environment configuration */
    protected final static String DB_URL = dotenv.get("DB_URL");
    
    /** Current user ID from the active session */
    protected final int currentUserId = SessionService.getCurrentUserId();

    /**
     * Creates a database table using the provided SQL query.
     * Retrieves the SQL query from SQLLoader using the provided key.
     *
     * @param sql_query_name The key to retrieve the SQL CREATE TABLE query from SQLLoader
     */
    protected static void createTable(String sql_query_name) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get(sql_query_name);

                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes an update SQL query (INSERT, UPDATE, DELETE).
     * The SQL query is retrieved from SQLLoader using the provided key.
     * Parameters for the prepared statement are set using the provided StatementSetter.
     *
     * @param sql_query_name The key to retrieve the SQL query from SQLLoader
     * @param setter The StatementSetter to set parameters in the prepared statement
     */
    protected void executeUpdateQuery(String sql_query_name, StatementSetter setter) {
        String sql = SQLLoader.get(sql_query_name);
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setter.setParameters(stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes a SELECT query and maps the results to a list of entities.
     * The SQL query is retrieved from SQLLoader using the provided key.
     * Parameters for the prepared statement are set using the provided StatementSetter.
     * Results are mapped to entity objects using the provided ResultSetMapper.
     *
     * @param sqlKey The key to retrieve the SQL query from SQLLoader
     * @param setter The StatementSetter to set parameters in the prepared statement
     * @param mapper The ResultSetMapper to map database results to entity objects
     * @return A list of entity objects created from the query results
     */
    protected List<T> executeQuery(String sqlKey, StatementSetter setter, ResultSetMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        String sql = SQLLoader.get(sqlKey);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setter.setParameters(stmt);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Database query error: " + e.getMessage());
        }

        return results;
    }

    /**
     * Inserts an entity into the database.
     * Implementing classes should provide the specific logic needed
     * to insert an entity of type T into the appropriate database table.
     *
     * @param entity The entity to insert into the database
     */
    public abstract void insertToDatabase(T entity);

    /**
     * Retrieves all entities of type T from the database.
     * Implementing classes should provide the specific logic needed
     * to retrieve all records from the appropriate database table.
     *
     * @return A list of all entities of type T in the database
     */
    public abstract List<T> getAllFromDatabase();
}