package com.example.blanza;

import java.time.LocalDate;

/**
 * Represents an income entry in the Balanza personal finance application.
 * <p>
 * The Income class extends the FinancialEntity base class to represent money
 * received by the user from various sources such as salary, dividends, rental
 * income, or other revenue streams. Each income entry tracks:
 * <ul>
 *   <li>The source of the income</li>
 *   <li>The amount received</li>
 *   <li>The date the payment was received</li>
 *   <li>A running total of all income from this source</li>
 * </ul>
 * <p>
 * Income objects are used to:
 * <ul>
 *   <li>Record and categorize money received by the user</li>
 *   <li>Track income history over time</li>
 *   <li>Calculate total income from specific sources</li>
 *   <li>Support budgeting and financial reporting functions</li>
 * </ul>
 * <p>
 * Like other financial entities, each income entry is associated with a specific user.
 *
 * @see FinancialEntity
 * @see Expense
 */
public class Income extends FinancialEntity {
    /** The source or origin of this income (e.g., "Salary", "Freelance Work", "Investment") */
    private String income_source;
    
    /** Running total of all income recorded from this source */
    private double total_income;
    
    /** The date when this income payment was received */
    private LocalDate pay_date;

    /**
     * Creates a new income entry with the specified details.
     * <p>
     * This constructor initializes an income record with the user ID, income source,
     * amount received, and payment date. The total income for this source is
     * initialized to zero and can be updated by calling {@link #updateTotalIncome()}.
     *
     * @param currentUserId The ID of the user who received this income
     * @param source The source or origin of the income (e.g., "Salary", "Freelance Work")
     * @param amount The amount of money received in this payment
     * @param pay_date The date when the payment was received
     */
    public Income(int currentUserId, String source, double amount, LocalDate pay_date) {
        super(currentUserId, amount);
        this.income_source = source;
        this.pay_date = pay_date;
        this.total_income = 0.0;
    }

    /**
     * Returns the source of this income.
     * <p>
     * The income source identifies where the money came from, such as
     * "Salary", "Freelance Work", "Investments", or "Rental Income".
     *
     * @return The source or origin of this income
     */
    public String getIncome_source() {
        return income_source;
    }

    /**
     * Sets or updates the source of this income.
     * <p>
     * This method allows changing the categorization of where the income originated.
     *
     * @param income_source The new source or origin for this income
     */
    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

    /**
     * Returns the running total of all income from this source.
     * <p>
     * This is a cumulative value that represents the sum of all income entries
     * from the same source. It must be manually updated by calling
     * {@link #updateTotalIncome()} when new income is added.
     *
     * @return The total accumulated income from this source
     */
    public double getTotal_income() {
        return total_income;
    }

    /**
     * Sets the running total for income from this source.
     * <p>
     * This method allows directly specifying the total income value,
     * which may be useful when importing or synchronizing data.
     *
     * @param total_income The new total accumulated income for this source
     */
    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }

    /**
     * Returns the date when this income payment was received.
     * <p>
     * The pay date is used for chronological tracking and reporting of income.
     *
     * @return The date when the payment was received
     */
    public LocalDate getPay_date() {
        return pay_date;
    }

    /**
     * Sets or updates the date when this income payment was received.
     * <p>
     * This method allows correcting the payment date if it was recorded incorrectly.
     *
     * @param pay_date The new date when the payment was received
     */
    public void setPay_date(LocalDate pay_date) {
        this.pay_date = pay_date;
    }

    /**
     * Updates the running total income by adding this entry's amount.
     * <p>
     * This method adds the current income amount to the running total for this source.
     * It should be called whenever a new income entry is created or when an existing
     * entry's amount is modified, to ensure the total remains accurate.
     * <p>
     * Note: This method assumes that the amount field represents a single income 
     * payment and adds it to the running total. It does not check for duplicates.
     */
    public void updateTotalIncome() {
        this.total_income += this.amount;
    }
}