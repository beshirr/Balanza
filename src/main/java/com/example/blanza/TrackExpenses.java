package com.example.blanza;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

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
        // Create a custom dialog
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle("Add New Expense");
        dialog.setHeaderText("Enter expense details");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create editable combo box for categories
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setEditable(true);
        categoryComboBox.setPromptText("Select or create category");
        // Populate with existing categories
        populateCategoryComboBox(categoryComboBox);

        // Create editable combo box for payment methods
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.setEditable(true);
        paymentMethodComboBox.setPromptText("Select or create payment method");
        // Populate with existing payment methods
        populatePaymentMethodComboBox(paymentMethodComboBox);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        
        DatePicker datePicker = new DatePicker(LocalDate.now());

        grid.add(new Label("Category:"), 0, 0);
        grid.add(categoryComboBox, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Payment Method:"), 0, 2);
        grid.add(paymentMethodComboBox, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the category field by default
        Platform.runLater(categoryComboBox::requestFocus);

        // Convert the result to an expense when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String category = categoryComboBox.getValue();
                    float amount = Float.parseFloat(amountField.getText());
                    String paymentMethod = paymentMethodComboBox.getValue();
                    LocalDate date = datePicker.getValue();
                    
                    if (category == null || category.trim().isEmpty()) {
                        showErrorAlert("Category cannot be empty");
                        return null;
                    }
                    
                    if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                        showErrorAlert("Payment method cannot be empty");
                        return null;
                    }
                    
                    return new Expense(SessionService.getCurrentUserId(), category, amount, date, paymentMethod);
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid amount entered");
                    return null;
                }
            }
            return null;
        });

        // Show the dialog and process the result
        Optional<Expense> result = dialog.showAndWait();
        
        result.ifPresent(expense -> {
            expenseManager.addExpense(expense);
            
            // Confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Expense added successfully!");
            alert.show();
            
            // Update UI
            updateDashboard();
            loadExpenses();
        });
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }

    // Helper method to populate category ComboBox with unique categories from existing expenses
    private void populateCategoryComboBox(ComboBox<String> comboBox) {
        // Use a Set to avoid duplicates
        Set<String> categories = new HashSet<>();
        
        // Add some default categories if needed
        categories.add("Food");
        categories.add("Transportation");
        categories.add("Housing");
        categories.add("Entertainment");
        categories.add("Utilities");
        categories.add("Healthcare");
        
        // Add categories from existing expenses
        for (Expense expense : expenseManager.getExpenses()) {
            categories.add(expense.getCategory());
        }
        
        // Add to combo box
        comboBox.getItems().addAll(categories);
    }

    // Helper method to populate payment method ComboBox with unique payment methods from existing expenses
    private void populatePaymentMethodComboBox(ComboBox<String> comboBox) {
        // Use a Set to avoid duplicates
        Set<String> paymentMethods = new HashSet<>();
        
        // Add some default payment methods
        paymentMethods.add("Cash");
        paymentMethods.add("Credit Card");
        paymentMethods.add("Debit Card");
        paymentMethods.add("Mobile Payment");
        paymentMethods.add("Bank Transfer");
        
        // Add payment methods from existing expenses
        for (Expense expense : expenseManager.getExpenses()) {
            paymentMethods.add(expense.getPaymentMethod());
        }
        
        // Add to combo box
        comboBox.getItems().addAll(paymentMethods);
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
    @FXML
    private void handleViewReports(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("expense_reports.fxml", "Expense Reports");
    }
}