import java.util.List;

public class ExpenseManager {
    private List<Expense> expenses;

    public void addExpense(Expense expense) {
        if (validateExpense(expense)) {
            expenses.add(expense);
            updateBudget(expense);
        }
    }

    private boolean validateExpense(Expense expense) {
        return expense.getAmount() >= 0; // TODO: logic
    }

    private void updateBudget(Expense expense) {
        // TODO: Add logic
    }
}
