package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Database<T> {
    private final static Dotenv dotenv = Dotenv.configure().directory(".").load();
    protected final static String DB_URL = dotenv.get("DB_URL");
    protected final int currentUserId = SessionService.getCurrentUserId();

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

    public abstract void insertToDatabase(T entity);

    public abstract List<T> getAllFromDatabase();
}
