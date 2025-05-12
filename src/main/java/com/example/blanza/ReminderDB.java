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
     * Insert reminder into database.
     *
     * @param reminder the reminder to insert
     */
    public static void insertReminder(Reminder reminder) {
        String sql = SQLLoader.get("insert_reminder");
        try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement stmt = conn.prepareStatement(sql)) {
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


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
}