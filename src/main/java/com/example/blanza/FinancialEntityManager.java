package com.example.blanza;

import java.util.ArrayList;
import java.util.List;

public abstract class FinancialEntityManager<T extends FinancialEntity> {
    protected List<T> entities = new ArrayList<>();

    public List<T> getAll() {
        return entities;
    }

    public double getTotal() {
        double total = 0;
        for (T entity : entities) {
            total += entity.getAmount();
        }
        return total;
    }

    public void addEntity(T entity) {
        if (validate(entity)) {
            entities.add(entity);
            saveToDatabase(entity);
        }
    }

    protected abstract boolean validate(T entity);

    protected abstract void saveToDatabase(T entity);
}
