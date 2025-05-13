package com.example.blanza;

import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


import java.io.IOException;

public class BudgetingTrackingController {


    @FXML
    private VBox budgetsVBox;  // VBox to hold and display all budgets



    @FXML
    private TextField categoryField;

    @FXML
    private TextField budgetAmountField;

    @FXML
    private Label remainingBudgetLabel;

    @FXML
    private TextField updateCategoryField;
    @FXML
    private TextField updateActualSpendField;

    @FXML
    private Label updateCategoryLabel;
    @FXML
    private Label updateActualSpendLabel;
    @FXML
    private Button updateBudgetButton;

    private Budgeting budget;

    // Initialize the Budgeting model
    public void initialize() {
        int userId = SessionService.getCurrentUserId();
        budget = new Budgeting("", 0.0, 0.0, userId);

    }

    public void displayBudgetData(String category, double budgetAmount, double actualSpend, double remainingBudget) {
        Label budgetLabel = new Label(
                "Category: " + category + ", " +
                        "Budget Amount: " + budgetAmount + ", " +
                        "Actual Spend: " + actualSpend + ", " +
                        "Remaining Budget: " + remainingBudget
        );
        budgetsVBox.getChildren().add(budgetLabel);  // Add each Label to VBox
    }


    @FXML
    public void handleSaveBudget() {
        String category = categoryField.getText();
        String budgetAmountText = budgetAmountField.getText();

        try {

            double budgetAmount = Double.parseDouble(budgetAmountText);
            budget.setCategory(category);
            budget.setBudgetAmount(budgetAmount);
            budget.updateRemainingBudget();

            BudgetDB.insertBudget(budget);


//            remainingBudgetLabel.setText("Remaining Budget: " + budget.getRemaining_budget());


            categoryField.clear();
            budgetAmountField.clear();

        } catch (NumberFormatException ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid budget amount entered.");
            alert.show();
        }
    }
    public void handleDisplayBudgets() {
            int userId = SessionService.getCurrentUserId();
        budgetsVBox.getChildren().clear();
        String sql = SQLLoader.get("select_budget_by_user_id");  // Query: SELECT * FROM budgets WHERE user_id = ?

        try (Connection conn = DriverManager.getConnection(Database.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;

                String category = rs.getString("category");
                double budgetAmount = rs.getDouble("budget_amount");
                double actualSpend = rs.getDouble("actual_spend");
                double remainingBudget = rs.getDouble("remaining_budget");

                Label budgetLabel = new Label(
                        "Category: " + category +
                                ", Budget: " + budgetAmount +
                                ", Spent: " + actualSpend +
                                ", Remaining: " + remainingBudget
                );

                budgetsVBox.getChildren().add(budgetLabel);
            }

            if (!hasResults) {
                budgetsVBox.getChildren().add(new Label("No budgets found."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            budgetsVBox.getChildren().add(new Label("Error loading budgets."));
        }
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }
    @FXML
    public void handleUpdateBudget() {

        updateCategoryLabel.setVisible(true);
        updateCategoryField.setVisible(true);
        updateActualSpendLabel.setVisible(true);
        updateActualSpendField.setVisible(true);
        updateBudgetButton.setVisible(true);



    }

    @FXML
    public void handleConfirmUpdate() {
        String category = updateCategoryField.getText();
        String actualSpendText = updateActualSpendField.getText();

        // Validate that the category and actual spend fields are not empty
        if (category.isEmpty() || actualSpendText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Both fields are required.");
            alert.show();
            return;
        }

        try {

            double actualSpend = Double.parseDouble(actualSpendText);


            Budgeting budget = BudgetDB.getBudgetByCategory(category, SessionService.getCurrentUserId());  // Modify this method to retrieve by category and user ID

            if (budget == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No budget found for this category.");
                alert.show();
                return;
            }


            if (actualSpend > budget.getBudgetAmount()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Actual spend cannot exceed the budget amount.");
                alert.show();
                return;
            }


            budget.setActual_spend(actualSpend);
            budget.updateRemainingBudget();


            BudgetDB.updateBudget(budget);


            remainingBudgetLabel.setText("Remaining Budget: " + budget.getRemaining_budget());


            updateCategoryField.clear();
            updateActualSpendField.clear();




        } catch (NumberFormatException ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid actual spend entered.");
            alert.show();
        }

    }
}
