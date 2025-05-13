package com.example.blanza;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * The type Financial task db.
 * Handles database operations for financial tasks.
 */
public class FinancialTaskDB extends Database<FinancialTask> {
    /**
     * Insert financial task into database.
     **/
    @Override
    public void insertToDatabase(FinancialTask financialTask) {
        executeUpdateQuery("insert_financial_task", (stmt -> {
            stmt.setInt(1, currentUserId);
            stmt.setString(2, financialTask.getTitle());
            stmt.setString(3, financialTask.getDescription());
            stmt.setDate(4, Date.valueOf(financialTask.getDueDate()));
            stmt.setDouble(5, financialTask.getAmount());
            stmt.setString(6, financialTask.getCategory());
            stmt.setString(7, financialTask.getStatus().name());
        }));
    }   

    /**
     * Delete financial task from database.
     *
     * @param financialTask the financial task to delete
     */
    public void deleteFinancialTask(FinancialTask financialTask) {
        executeUpdateQuery("delete_financial_task", stmt -> {
            stmt.setInt(1, financialTask.getId());
        });
    }

    /**
     * Get all financial tasks for the current user.
     *
     * @return list of financial tasks
     */
    @Override
    public List<FinancialTask> getAllFromDatabase() {
        if (currentUserId <= 0) {
            System.err.println("Error: Invalid user ID");
            return List.of();
        }
        return executeQuery("select_all_financial_tasks", stmt -> {
            stmt.setInt(1, currentUserId);
        }, rs -> {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            double amount = rs.getDouble("amount");
            String category = rs.getString("category");
            FinancialTask.TaskStatus status = FinancialTask.TaskStatus.valueOf(rs.getString("status"));

            return new FinancialTask(id, currentUserId, title, description, dueDate, amount, category, status);
        });
    }

    /**
     * Get a specific financial task by id.
     *
     * @param taskId the task id
     * @return the financial task or null if not found
     */
    public FinancialTask getFinancialTaskById(int taskId) {
        List<FinancialTask> tasks = executeQuery("get_financial_task_by_id", stmt -> {
            stmt.setInt(1, taskId);
        }, rs -> {
            int id = rs.getInt("id");
            int userId = rs.getInt("user_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            double amount = rs.getDouble("amount");
            String category = rs.getString("category");
            FinancialTask.TaskStatus status = FinancialTask.TaskStatus.valueOf(rs.getString("status"));

            return new FinancialTask(id, userId, title, description, dueDate, amount, category, status);
        });

        return tasks.isEmpty() ? null : tasks.getFirst();
    }

    /**
     * Update financial task status.
     *
     * @param financialTask the financial task with updated status
     */
    public void updateFinancialTaskStatus(FinancialTask financialTask) {
        executeUpdateQuery("update_financial_task_status", stmt -> {
            stmt.setString(1, financialTask.getStatus().name());
            stmt.setInt(2, financialTask.getId());
        });
    }

    /**
     * Update financial task.
     *
     * @param financialTask the financial task with updated information
     */
    public void updateFinancialTask(FinancialTask financialTask) {
        executeUpdateQuery("update_financial_task", stmt -> {
            stmt.setString(1, financialTask.getTitle());
            stmt.setString(2, financialTask.getDescription());
            stmt.setDate(3, Date.valueOf(financialTask.getDueDate()));
            stmt.setDouble(4, financialTask.getAmount());
            stmt.setString(5, financialTask.getCategory());
            stmt.setString(6, financialTask.getStatus().name());
            stmt.setInt(7, financialTask.getId());
        });
    }

    /**
     * Get upcoming financial tasks.
     *
     * @return list of upcoming financial tasks
     */
    public List<FinancialTask> getUpcomingFinancialTasks() {
        if (currentUserId <= 0) {
            System.err.println("Error: Invalid user ID");
            return List.of();
        }

        return executeQuery("get_upcoming_financial_tasks", stmt -> {
            stmt.setInt(1, currentUserId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
        }, rs -> {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            double amount = rs.getDouble("amount");
            String category = rs.getString("category");
            FinancialTask.TaskStatus status = FinancialTask.TaskStatus.valueOf(rs.getString("status"));

            return new FinancialTask(id, currentUserId, title, description, dueDate, amount, category, status);
        });
    }
}