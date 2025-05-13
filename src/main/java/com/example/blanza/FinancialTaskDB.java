package com.example.blanza;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Financial task db.
 * Handles database operations for financial tasks.
 */
public class FinancialTaskDB extends Database {
    /**
     * Insert financial task into database.
     *
     * @param title       the title
     * @param description the description
     * @param dueDate     the due date
     * @param amount      the amount
     * @param category    the category
     * @param status      the status
     * @return the id of the inserted financial task
     */
    public static int insertFinancialTask(String title, String description, 
                                         LocalDate dueDate, double amount, String category, 
                                         FinancialTask.TaskStatus status) {
        int userId = SessionService.getCurrentUserId();
        if (userId <= 0) {
            System.err.println("Error: Invalid user ID");
            return -1;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("insert_financial_task");
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, userId);
                stmt.setString(2, title);
                stmt.setString(3, description);
                stmt.setDate(4, Date.valueOf(dueDate));
                stmt.setDouble(5, amount);
                stmt.setString(6, category);
                stmt.setString(7, status.name());
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    
    /**
     * Delete financial task from database.
     *
     * @param id the financial task id
     */
    public static void deleteFinancialTask(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("delete_financial_task");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Get all financial tasks for the current user.
     *
     * @return list of financial tasks
     */
    public static List<FinancialTask> getAllFinancialTasks() {
        List<FinancialTask> tasks = new ArrayList<>();
        int userId = SessionService.getCurrentUserId();
        if (userId <= 0) {
            System.err.println("Error: Invalid user ID");
            return tasks;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("get_user_financial_tasks");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                    double amount = rs.getDouble("amount");
                    String category = rs.getString("category");
                    FinancialTask.TaskStatus status = FinancialTask.TaskStatus.valueOf(rs.getString("status"));
                    
                    FinancialTask task = new FinancialTask(id, userId, title, description, dueDate, amount, category, status);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return tasks;
    }
    
    /**
     * Get a specific financial task by id.
     *
     * @param taskId the task id
     * @return the financial task or null if not found
     */
    public static FinancialTask getFinancialTaskById(int taskId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("get_financial_task_by_id");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, taskId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int userId = rs.getInt("user_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                    double amount = rs.getDouble("amount");
                    String category = rs.getString("category");
                    FinancialTask.TaskStatus status = FinancialTask.TaskStatus.valueOf(rs.getString("status"));
                    
                    return new FinancialTask(id, userId, title, description, dueDate, amount, category, status);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update financial task status.
     *
     * @param taskId the task id
     * @param status the new status
     */
    public static void updateFinancialTaskStatus(int taskId, FinancialTask.TaskStatus status) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("update_financial_task_status");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, status.name());
                stmt.setInt(2, taskId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update financial task.
     *
     * @param id          the task id
     * @param title       the updated title
     * @param description the updated description
     * @param dueDate     the updated due date
     * @param amount      the updated amount
     * @param category    the updated category
     * @param status      the updated status
     */
    public static void updateFinancialTask(int id, String title, String description, 
                                          LocalDate dueDate, double amount, String category, 
                                          FinancialTask.TaskStatus status) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("update_financial_task");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, title);
                stmt.setString(2, description);
                stmt.setDate(3, Date.valueOf(dueDate));
                stmt.setDouble(4, amount);
                stmt.setString(5, category);
                stmt.setString(6, status.name());
                stmt.setInt(7, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Get upcoming financial tasks.
     *
     * @return list of upcoming financial tasks
     */
    public static List<FinancialTask> getUpcomingFinancialTasks() {
        List<FinancialTask> tasks = new ArrayList<>();
        int userId = SessionService.getCurrentUserId();
        if (userId <= 0) {
            System.err.println("Error: Invalid user ID");
            return tasks;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("get_upcoming_financial_tasks");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setDate(2, Date.valueOf(LocalDate.now()));
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                    double amount = rs.getDouble("amount");
                    String category = rs.getString("category");
                    FinancialTask.TaskStatus status = FinancialTask.TaskStatus.valueOf(rs.getString("status"));
                    
                    FinancialTask task = new FinancialTask(id, userId, title, description, dueDate, amount, category, status);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return tasks;
    }
}