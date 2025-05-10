package com.example.blanza;



import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BudgetingTrackingController {

    @FXML
    private TextField categoryField;

    @FXML
    private TextField budgetAmountField;

    @FXML
    private Label remainingBudgetLabel;

    private Budgeting budget;

    // Initialize the Budgeting model
    public void initialize() {
        budget = new Budgeting("", 0.0, 0.0, 0); // Initialize with default values
    }


    @FXML
    public void handleSaveBudget() {
        String category = categoryField.getText(); // Get the category from the input field
        String budgetAmountText = budgetAmountField.getText(); // Get the budget amount from the input field

        try {

            double budgetAmount = Double.parseDouble(budgetAmountText);
            budget.setCategory(category);
            budget.setBudgetAmount(budgetAmount);
            budget.updateRemainingBudget();


            remainingBudgetLabel.setText("Remaining Budget: " + budget.getRemaining_budget());


            categoryField.clear();
            budgetAmountField.clear();

        } catch (NumberFormatException ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid budget amount entered.");
            alert.show();
        }
    }
}
