package com.example.blanza;

/**
 * Base class for all financial entities in the Balanza application.
 * <p>
 * The FinancialEntity class serves as the foundation for various financial
 * records in the application, such as expenses and income. It provides common
 * attributes and behaviors that all financial entities share, including:
 * <ul>
 *   <li>User association through a user ID</li>
 *   <li>Monetary amount</li>
 * </ul>
 * <p>
 * This class supports the multi-user architecture of the application by
 * tracking which user each financial entity belongs to. Subclasses extend
 * this base class with additional attributes and behaviors specific to
 * different types of financial records.
 *
 * @see Expense
 * @see Income
 */
public class FinancialEntity {
    /** The ID of the user who owns this financial entity */
    protected int currentUserId;
    
    /** The monetary amount associated with this financial entity */
    protected double amount;

    /**
     * Creates a new financial entity with the specified user ID and amount.
     *
     * @param currentUserId The ID of the user who owns this financial entity
     * @param amount The monetary amount associated with this financial entity
     */
    public FinancialEntity(int currentUserId, double amount) {
        this.currentUserId = currentUserId;
        this.amount = amount;
    }

    /**
     * Returns the ID of the user who owns this financial entity.
     *
     * @return The user ID
     */
    public int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets the ID of the user who owns this financial entity.
     *
     * @param currentUserId The new user ID
     */
    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    /**
     * Returns the monetary amount associated with this financial entity.
     *
     * @return The monetary amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the monetary amount associated with this financial entity.
     *
     * @param amount The new monetary amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}