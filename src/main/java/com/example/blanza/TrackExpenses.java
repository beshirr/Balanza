package com.example.blanza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

public class TrackExpenses {
    private ExpenseManager expenseManager;

    @FXML
    private void initialize() {
        expenseManager = new ExpenseManager();
        updateDashboard();
        loadExpenses();
    }

    @FXML
    private void handleAddExpense() {
        try {
            String category = categoryField.getText();
            float amount = Float.parseFloat(amountField.getText());
            String paymentMethod = paymentMethodField.getText();
            LocalDate date = LocalDate.now();

            Expense expense = new Expense(Session.getCurrentUserId(), category, amount, date, paymentMethod);
            expenseManager.addExpense(expense);

            // Confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Expense added successfully!");
            alert.show();

            // Clear input fields
            categoryField.clear();
            amountField.clear();
            paymentMethodField.clear();

            // Update UI
            updateDashboard();
            loadExpenses();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid amount entered.");
            alert.show();
        }
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }

    private void updateDashboard() {
        expenses.setText("Expenses: " + expenseManager.getTotalExpenses());
    }

    private void loadExpenses() {
        expenseListView.getItems().clear();
        for (Expense expense : expenseManager.getExpenses()) {
            expenseListView.getItems().add(expense.getCategory() + " - " + expense.getAmount() + " - " + expense.getDate());
        }
    }
    @FXML
    private Label balance;
    @FXML
    private Label income;
    @FXML
    private Label expenses;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField paymentMethodField;
    @FXML
    private ListView<String> expenseListView;
}
