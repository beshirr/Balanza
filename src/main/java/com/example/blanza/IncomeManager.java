package com.example.blanza;

import java.util.List;

/**
 * Manager class responsible for income-related business logic in the Balanza application.
 * <p>
 * The IncomeManager class extends the abstract FinancialEntityManager to provide
 * specific implementation for handling Income entities. It serves as an intermediary
 * between the user interface and the data access layer, managing:
 * <ul>
 *   <li>Validation of income records before persistence</li>
 *   <li>Storage of income records to the database</li>
 *   <li>Retrieval of income records from the database</li>
 * </ul>
 * <p>
 * This class follows the Manager pattern, encapsulating business rules and data access
 * logic related to income management. It works closely with IncomeDB for data persistence
 * while enforcing business rules such as preventing negative income amounts.
 * <p>
 * IncomeManager inherits common functionality from FinancialEntityManager, including
 * methods for adding, retrieving, and managing collections of financial entities.
 *
 * @see Income
 * @see IncomeDB
 * @see FinancialEntityManager
 */
public class IncomeManager extends FinancialEntityManager<Income> {
    /** Database access object for income-related database operations */
    private final IncomeDB db = new IncomeDB();
    
    /**
     * Validates an Income object before saving it to the database.
     * <p>
     * This method implements the abstract validation method from FinancialEntityManager
     * with income-specific validation rules. Currently, it ensures that:
     * <ul>
     *   <li>The income amount is non-negative (zero or positive)</li>
     * </ul>
     * <p>
     * Additional validation rules for income records could be added here in the future,
     * such as checking for required fields or validating the payment date.
     *
     * @param income The Income object to validate
     * @return true if the income record passes all validation checks, false otherwise
     */
    @Override
    protected boolean validate(Income income) {
        return income.getAmount() >= 0;
    }

    /**
     * Persists an Income object to the database.
     * <p>
     * This method implements the abstract saveToDatabase method from FinancialEntityManager
     * by delegating the database operation to the IncomeDB instance. It's called after
     * validation succeeds to store the income record in the application database.
     * <p>
     * The method assumes that the Income object has already been validated using the
     * validate() method.
     *
     * @param income The validated Income object to save to the database
     * @see #validate(Income)
     */
    @Override
    protected void saveToDatabase(Income income) {
        db.insertToDatabase(income);
    }
    
    /**
     * Retrieves all Income records for the current user from the database.
     * <p>
     * This method implements the abstract loadFromDatabase method from FinancialEntityManager
     * by delegating the retrieval operation to the IncomeDB instance. It's used to fetch
     * all income records associated with the current user from the application database.
     * <p>
     * The returned list can be used for displaying income history, generating reports,
     * or performing calculations such as total income by source or time period.
     *
     * @return A List of Income objects representing all income records for the current user
     */
    @Override
    protected List<Income> loadFromDatabase() {
        return db.getAllFromDatabase();
    }
}