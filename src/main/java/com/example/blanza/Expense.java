package com.example.blanza;

import java.time.LocalDate;

/**
 * Represents an expense entry in the Balanza financial management application.
 * <p>
 * The Expense class extends FinancialEntity to track specific expenditures made by users.
 * Each expense includes category information, the transaction date, and the payment method used.
 * This allows for detailed expense tracking and categorization for financial reporting
 * and budget management.
 * <p>
 * Expenses are associated with a specific user through the user ID inherited from FinancialEntity,
 * enabling multi-user support within the application.
 *
 * @see FinancialEntity
 */
public class Expense extends FinancialEntity {
    /** The category of the expense (e.g., "Food", "Transportation", "Housing") */
    private String category;
    
    /** The date when the expense occurred */
    private LocalDate date;
    
    /** The payment method used for the expense (e.g., "Cash", "Credit Card", "Bank Transfer") */
    private String paymentMethod;

    /**
     * Creates a new Expense with the specified details.
     *
     * @param current_user_id The ID of the user who incurred this expense
     * @param category The category of the expense (e.g., "Food", "Transportation")
     * @param amount The monetary amount of the expense
     * @param time The date when the expense occurred
     * @param paymentMethod The method used to pay for the expense
     */
    public Expense(int current_user_id, String category, double amount, LocalDate time, String paymentMethod) {
        super(current_user_id, amount);
        this.category = category;
        this.amount = amount;
        this.date = time;
        this.paymentMethod = paymentMethod;
    }

    /**
     * Returns the category of this expense.
     *
     * @return The expense category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of this expense.
     *
     * @param category The new expense category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the date when this expense occurred.
     *
     * @return The expense date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date when this expense occurred.
     *
     * @param date The new expense date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the payment method used for this expense.
     *
     * @return The payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method used for this expense.
     *
     * @param paymentMethod The new payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}