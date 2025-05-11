package com.example.blanza;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Reminder db.
 */
public class ReminderDB extends Database {
    
    /**
     * Creates reminder table if not exists.
     */
    public static void createReminderTable() {
        createTable("create_reminder_table");
    }
    
    /**
     * Insert reminder into database.
     *
     * @param reminder the reminder to insert
     * @return the id of the inserted reminder
     */
    public static int insertReminder(Reminder reminder) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("insert_reminder");
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, reminder.getCurrent_user_id());
                stmt.setString(2, reminder.getTitle());
                stmt.setString(3, reminder.getDescription());
                stmt.setTimestamp(4, Timestamp.valueOf(reminder.getTime()));
                
                if (reminder.getTask_id() != null) {
                    stmt.setInt(5, reminder.getTask_id());
                } else {
                    stmt.setNull(5, java.sql.Types.INTEGER);
                }
                
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
     * Delete reminder from database.
     *
     * @param id the reminder id
     */
    public static void deleteReminder(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("delete_reminder");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Get all reminders for the current user.
     *
     * @return list of reminders
     */
    public static List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        
        int userId = Session.getCurrentUserId();
        if (userId <= 0) {
            System.err.println("Error: Invalid user ID");
            return reminders;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("get_user_reminders");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    LocalDateTime time = rs.getTimestamp("time").toLocalDateTime();
                    
                    // Get the associated financial task if exists
                    Integer taskId = null;
                    FinancialTask task = null;
                    
                    if (!rs.wasNull() && rs.getObject("task_id") != null) {
                        taskId = rs.getInt("task_id");
                        task = FinancialTaskDB.getFinancialTaskById(taskId);
                    }
                    
                    Reminder reminder = new Reminder(id, userId, title, description, time, taskId, task);
                    reminders.add(reminder);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    /**
     * Update reminder in database.
     *
     * @param reminder the reminder to update
     */
    public static void updateReminder(Reminder reminder) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = SQLLoader.get("update_reminder");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, reminder.getTitle());
                stmt.setString(2, reminder.getDescription());
                stmt.setTimestamp(3, Timestamp.valueOf(reminder.getTime()));
                
                if (reminder.getTask_id() != null) {
                    stmt.setInt(4, reminder.getTask_id());
                } else {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                }
                
                stmt.setInt(5, reminder.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}