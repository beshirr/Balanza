package com.example.blanza;

import java.time.LocalDate;
import java.util.Objects;

/**
 * The type Financial task.
 * Represents a financial task such as bill payment, budget allocation, etc.
 */
public class FinancialTask {
    private int id;
    private int userId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private double amount;
    private String category;
    private TaskStatus status;
    
    /**
     * Status of the financial task
     */
    public enum TaskStatus {
        PENDING,
        COMPLETED,
        OVERDUE,
        CANCELLED
    }
    
    /**
     * Instantiates a new Financial task.
     *
     * @param id          the id
     * @param userId      the user id
     * @param title       the title
     * @param description the description
     * @param dueDate     the due date
     * @param amount      the amount
     * @param category    the category
     * @param status      the status
     */
    public FinancialTask(int id, int userId, String title, String description, LocalDate dueDate, 
                         double amount, String category, TaskStatus status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.amount = amount;
        this.category = category;
        this.status = status;
    }
    
    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets due date.
     *
     * @return the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    /**
     * Gets amount.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Gets status.
     *
     * @return the status
     */
    public TaskStatus getStatus() {
        return status;
    }
    
    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialTask that = (FinancialTask) o;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return title + " - $" + amount + " (" + status + ")";
    }
}