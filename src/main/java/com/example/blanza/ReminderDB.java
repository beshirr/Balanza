package com.example.blanza;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Reminder db.
 */
public class ReminderDB extends Database<Reminder> {
    /**
     * Insert reminder into database.
     *
     * @param reminder the reminder to insert
     */
    @Override
    public void insertToDatabase(Reminder reminder) {
        executeUpdateQuery("insert_reminder", (stmt -> {
            stmt.setInt(1, reminder.getCurrent_user_id());
            stmt.setString(2, reminder.getTitle());
            stmt.setString(3, reminder.getDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(reminder.getTime()));
            if (reminder.getTask_id() != null) {
                stmt.setInt(5, reminder.getTask_id());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
        }));
    }

    /**
     * Get all reminders for the current user.
     *
     * @return list of reminders
     */
    @Override
    public List<Reminder> getAllFromDatabase() {
        if (currentUserId <= 0) {
            System.err.println("Error: Invalid user ID");
            return List.of();
        }

        return executeQuery("get_user_reminders", stmt -> {
            stmt.setInt(1, currentUserId);
        }, rs -> {
            FinancialTaskDB db = new FinancialTaskDB();
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            LocalDateTime time = rs.getTimestamp("time").toLocalDateTime();
            Integer taskId = null;
            FinancialTask task = null;
            if (!rs.wasNull() && rs.getObject("task_id") != null) {
                taskId = rs.getInt("task_id");
                task = db.getFinancialTaskById(taskId);
            }
            return new Reminder(id, currentUserId, title, description, time, taskId, task);
        });
    }
}