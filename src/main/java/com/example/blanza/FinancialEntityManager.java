package com.example.blanza;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for managing collections of financial entities in the Balanza application.
 * <p>
 * The FinancialEntityManager provides a generic implementation for managing lists of
 * financial entities (like expenses or income), with support for:
 * <ul>
 *   <li>Lazy loading from a data source</li>
 *   <li>Entity validation</li>
 *   <li>Persistence to a database</li>
 *   <li>Calculation of aggregate values</li>
 * </ul>
 * <p>
 * This class implements the Template Method design pattern, where the base class defines
 * the skeleton of operations, deferring some steps to subclasses. Concrete subclasses must
 * implement the abstract methods to provide specific validation rules and database operations
 * for different types of financial entities.
 * <p>
 * The manager maintains an in-memory cache of entities and handles the synchronization
 * between the cache and the persistent storage.
 *
 * @param <T> The type of financial entity managed by this class, must extend FinancialEntity
 *
 * @see FinancialEntity
 * @see ExpenseManager
 * @see IncomeManager
 */
public abstract class FinancialEntityManager<T extends FinancialEntity> {
    /**
     * The in-memory cache of financial entities managed by this class.
     * This list is lazy-loaded from the database when first accessed.
     */
    protected List<T> entities = new ArrayList<>();
    
    /**
     * Retrieves all financial entities managed by this class.
     * <p>
     * If the in-memory cache is empty, this method triggers a database load
     * operation to populate the cache before returning the entities. This lazy
     * loading approach helps minimize database access and improve performance.
     *
     * @return A list of all financial entities of type T
     */
    public List<T> getAll() {
        
        if (entities.isEmpty()) {
            entities = loadFromDatabase();
        }
        return entities;
    }
    
    /**
     * Calculates the sum of all financial entity amounts.
     * <p>
     * This method provides an aggregation function that computes the total
     * monetary value across all entities managed by this class. The calculation
     * is performed using a stream operation on the collection of entities.
     *
     * @return The sum of all entity amounts as a double
     */
    public double getTotal() {
        return getAll().stream()
                .mapToDouble(FinancialEntity::getAmount)
                .sum();
    }
    
    /**
     * Adds a new financial entity to the collection if it passes validation.
     * <p>
     * This method:
     * <ol>
     *   <li>Validates the entity using the subclass-specific validation rules</li>
     *   <li>If valid, adds it to the in-memory cache</li>
     *   <li>Persists the entity to the database</li>
     * </ol>
     * <p>
     * The entity will not be added if validation fails. No error is explicitly
     * thrown, but the entity will not appear in subsequent calls to {@link #getAll()}.
     *
     * @param entity The financial entity to add
     */
    public void addEntity(T entity) {
        if (validate(entity)) {
            entities.add(entity);
            saveToDatabase(entity);
        }
    }
    
    /**
     * Validates a financial entity according to type-specific rules.
     * <p>
     * This abstract method must be implemented by subclasses to define
     * the validation rules specific to each type of financial entity.
     * For example, an expense implementation might check that the amount
     * is non-negative, while an income implementation might have different rules.
     *
     * @param entity The financial entity to validate
     * @return true if the entity is valid, false otherwise
     */
    protected abstract boolean validate(T entity);
    
    /**
     * Saves a financial entity to the persistent storage.
     * <p>
     * This abstract method must be implemented by subclasses to define
     * the database operations specific to each type of financial entity.
     * It is called automatically when a valid entity is added through
     * the {@link #addEntity(FinancialEntity)} method.
     *
     * @param entity The validated financial entity to save
     */
    protected abstract void saveToDatabase(T entity);
    
    /**
     * Loads financial entities from the persistent storage.
     * <p>
     * This abstract method must be implemented by subclasses to define
     * how entities are retrieved from the database. It is called automatically
     * when {@link #getAll()} is invoked with an empty cache.
     *
     * @return A list of financial entities loaded from the database
     */
    protected abstract List<T> loadFromDatabase();
}