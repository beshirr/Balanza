package com.example.blanza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for loading and managing SQL queries from an external resource file.
 * <p>
 * The SQLLoader provides a centralized mechanism for managing SQL queries in the Balanza
 * application, separating SQL code from Java code to improve maintainability. Rather than
 * embedding SQL statements directly in Java classes, queries are stored in a dedicated
 * SQL file and loaded at application startup.
 * <p>
 * The class loads queries from the file "/sql/queries.sql" in the classpath. Within this file,
 * queries are defined using a special comment-based format:
 * 
 * <pre>
 * -- @queryName
 * SELECT column1, column2
 * FROM table
 * WHERE condition = ?
 * -- @anotherQueryName
 * INSERT INTO table (column1, column2)
 * VALUES (?, ?)
 * </pre>
 * 
 * Each query is identified by a unique name following the "@" symbol in a comment, and the
 * query content continues until the next query definition or the end of the file.
 * <p>
 * Benefits of this approach include:
 * <ul>
 *   <li>Clear separation of SQL from Java code</li>
 *   <li>Easier SQL query maintenance and optimization</li>
 *   <li>Ability to use SQL syntax highlighting in the queries file</li>
 *   <li>Centralized management of database interactions</li>
 *   <li>Reduced duplication of similar queries</li>
 * </ul>
 * <p>
 * The class uses a static initialization block to load all queries when the class is first
 * referenced, making queries immediately available throughout the application lifecycle.
 * 
 * @see Database
 */
public class SQLLoader {
    
    /**
     * Map storing all loaded SQL queries, with query names as keys and SQL statements as values.
     * <p>
     * This map is populated during class initialization and provides O(1) lookup 
     * of queries by their identifier.
     */
    private static final Map<String, String> sqlQueries = new HashMap<>();
    
    /**
     * Regular expression pattern for extracting named queries from the SQL file.
     * <p>
     * This pattern identifies:
     * <ul>
     *   <li>Group 1: The query name (alphanumeric characters following "@")</li>
     *   <li>Group 2: The query content (all text until the next query definition or end of file)</li>
     * </ul>
     * <p>
     * The pattern matches comments in the format "-- @queryName" and captures all text
     * until the next such comment or the end of the file.
     */
    private static final Pattern QUERY_PATTERN = Pattern.compile("--\\s*@(\\w+)\\s*([\\s\\S]*?)(?=--\\s*@|$)");
    
    /**
     * Static initialization block that loads all SQL queries when the class is first accessed.
     * <p>
     * This block:
     * <ol>
     *   <li>Attempts to locate and open the SQL file from the resources directory</li>
     *   <li>Reads the entire file content into memory</li>
     *   <li>Uses regex pattern matching to extract named queries</li>
     *   <li>Stores each query in the sqlQueries map for later retrieval</li>
     * </ol>
     * <p>
     * If the SQL file cannot be found, a RuntimeException is thrown to prevent the application
     * from starting with missing queries. Other IO errors are logged but do not prevent
     * application startup, although some functionality may be impaired if queries are missing.
     */
    static {
        try {
            InputStream inputStream = SQLLoader.class.getResourceAsStream("/sql/queries.sql");
            
            if (inputStream == null) {
                String errorMsg = "Error: Could not find SQL file in resources at /sql/queries.sql";
                System.err.println(errorMsg);
                throw new RuntimeException(errorMsg); // Make the error explicit and prevent partial initialization
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder fileContent = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            
            Matcher matcher = QUERY_PATTERN.matcher(fileContent.toString());
            while (matcher.find()) {
                String queryName = matcher.group(1);
                String queryContent = matcher.group(2).trim();
                sqlQueries.put(queryName, queryContent);
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading SQL queries: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves a SQL query by its name.
     * <p>
     * This method provides access to the pre-loaded SQL queries, allowing database
     * operations throughout the application to reference queries by name rather than
     * embedding SQL directly.
     * <p>
     * Example usage:
     * <pre>{@code
     * String query = SQLLoader.get("findUserByUsername");
     * try (PreparedStatement stmt = connection.prepareStatement(query)) {
     *     stmt.setString(1, username);
     *     ResultSet rs = stmt.executeQuery();
     *     // Process results
     * }
     * }</pre>
     * <p>
     * If the requested query name is not found in the loaded queries, a warning is logged
     * to the console and null is returned. Callers should handle potential null returns
     * appropriately.
     *
     * @param queryName The name of the SQL query to retrieve (without the "@" prefix)
     * @return The SQL query string, or null if no query with the given name exists
     */
    public static String get(String queryName) {
        String query = sqlQueries.get(queryName);
        if (query == null) {
            System.err.println("No SQL query found with name: " + queryName);
        }
        return query;
    }
}