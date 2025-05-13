package com.example.blanza;

import java.util.ArrayList;
import java.util.List;

public abstract class FinancialEntityManager<T extends FinancialEntity> {
    protected List<T> entities = new ArrayList<>();
    
    public List<T> getAll() {
        // Load entities from database if list is empty
        if (entities.isEmpty()) {
            entities = loadFromDatabase();
        }
        return entities;
    }
    
    public double getTotal() {
        return getAll().stream()
                .mapToDouble(FinancialEntity::getAmount)
                .sum();
    }
    
    public void addEntity(T entity) {
        if (validate(entity)) {
            entities.add(entity);
            saveToDatabase(entity);
        }
    }
    
    protected abstract boolean validate(T entity);
    
    protected abstract void saveToDatabase(T entity);
    
    // New method to load entities from database
    protected abstract List<T> loadFromDatabase();
}