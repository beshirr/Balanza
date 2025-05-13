package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

public class BudgetingTrackingController {
    private final BudgetDB budgetDB;
    private final ExpenseDB expenseDB;

    /**
     * Default constructor required by JavaFX
     */
    public BudgetingTrackingController() {
        this.budgetDB = new BudgetDB();
        this.expenseDB = new ExpenseDB();
    }

    /**
     * Initialize the controller after FXML fields have been set
     */
    @FXML
    public void initialize() {
        
        setupCategoryComboBox();

        
        refreshBudgets();

        
        remainingBudgetLabel.setText("");
    }

    /**
     * Set up the category combo box with standard and existing categories
     */
    private void setupCategoryComboBox() {
        ObservableList<String> categories = FXCollections.observableArrayList(getAvailableCategories());
        categoryComboBox.setItems(categories);
        categoryComboBox.setEditable(true);
        categoryComboBox.setPromptText("Select or enter a category");
    }

    /**
     * Get all available categories from standard list, expenses, and budgets
     */
    private List<String> getAvailableCategories() {
        Set<String> categories = new HashSet<>();

        
        categories.addAll(Arrays.asList(
                "Food", "Housing", "Transportation", "Utilities",
                "Entertainment", "Healthcare", "Education",
                "Shopping", "Personal Care", "Travel", "Other"
        ));

        
        for (Expense expense : expenseDB.getAllFromDatabase()) {
            if (expense.getCategory() != null && !expense.getCategory().isEmpty()) {
                categories.add(expense.getCategory());
            }
        }

        
        for (Budget budget : budgetDB.getAllFromDatabase()) {
            if (budget.getCategory() != null && !budget.getCategory().isEmpty()) {
                categories.add(budget.getCategory());
            }
        }

        
        List<String> sortedCategories = new ArrayList<>(categories);
        Collections.sort(sortedCategories);

        return sortedCategories;
    }

    /**
     * Refresh the budget list from the database and display it
     */
    private void refreshBudgets() {
        
        budgetsVBox.getChildren().clear();


        List<Budget> currentBudgets = budgetDB.getAllFromDatabase();

        
        if (currentBudgets.isEmpty()) {
            Label noDataLabel = new Label("No budgets found. Create your first budget above.");
            noDataLabel.getStyleClass().add("info-message");
            budgetsVBox.getChildren().add(noDataLabel);
            return;
        }

        
        budgetsVBox.getChildren().add(createHeader());

        
        for (Budget budget : currentBudgets) {
            budgetsVBox.getChildren().add(createBudgetRow(budget));
        }
    }

    /**
     * Create a header row for the budget list
     */
    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setPadding(new Insets(5, 10, 5, 10));
        header.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Label categoryHeader = new Label("Category");
        Label budgetHeader = new Label("Budget Amount");
        Label spentHeader = new Label("Spent");
        Label remainingHeader = new Label("Remaining");
        Label actionsHeader = new Label("Actions");

        categoryHeader.setPrefWidth(150);
        budgetHeader.setPrefWidth(120);
        spentHeader.setPrefWidth(120);
        remainingHeader.setPrefWidth(120);
        actionsHeader.setPrefWidth(180);

        categoryHeader.setStyle("-fx-font-weight: bold;");
        budgetHeader.setStyle("-fx-font-weight: bold;");
        spentHeader.setStyle("-fx-font-weight: bold;");
        remainingHeader.setStyle("-fx-font-weight: bold;");
        actionsHeader.setStyle("-fx-font-weight: bold;");

        header.getChildren().addAll(categoryHeader, budgetHeader, spentHeader, remainingHeader, actionsHeader);

        return header;
    }

    /**
     * Create a row for a single budget with data and action buttons
     */
    private HBox createBudgetRow(Budget budget) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(10, 10, 10, 10));
        row.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Label categoryLabel = new Label(budget.getCategory());
        Label budgetLabel = new Label(String.format("$%.2f", budget.getAmount()));
        Label spentLabel = new Label(String.format("$%.2f", budget.getActual_spend()));

        
        Label remainingLabel = new Label(String.format("$%.2f", budget.getRemaining_budget()));
        double remaining = budget.getRemaining_budget();
        if (remaining < 0) {
            remainingLabel.setTextFill(Color.RED);
        } else if (remaining < (budget.getAmount() * 0.2)) {
            remainingLabel.setTextFill(Color.ORANGE);
        } else {
            remainingLabel.setTextFill(Color.GREEN);
        }

        
        Button editButton = new Button("Edit");
        Button updateSpendButton = new Button("Update Spend");

        editButton.getStyleClass().add("small-button");
        updateSpendButton.getStyleClass().add("small-button");

        
        editButton.setOnAction(e -> showEditBudgetDialog(budget));
        updateSpendButton.setOnAction(e -> showUpdateSpendDialog(budget));

        
        HBox actionButtons = new HBox(5);
        actionButtons.getChildren().addAll(editButton, updateSpendButton);

        
        categoryLabel.setPrefWidth(150);
        budgetLabel.setPrefWidth(120);
        spentLabel.setPrefWidth(120);
        remainingLabel.setPrefWidth(120);
        actionButtons.setPrefWidth(180);

        row.getChildren().addAll(categoryLabel, budgetLabel, spentLabel, remainingLabel, actionButtons);

        return row;
    }

    /**
     * Handle the save budget button click
     */
    @FXML
    public void handleSaveBudget(ActionEvent event) {
        String category = categoryComboBox.getValue();
        String amountText = budgetAmountField.getText();

        
        if (category == null || category.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select or enter a category");
            return;
        }

        if (amountText == null || amountText.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a budget amount");
            return;
        }

        try {
            
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Budget amount must be greater than zero");
                return;
            }

            
            Budget existingBudget = budgetDB.getBudgetByCategory(category, SessionService.getCurrentUserId());
            if (existingBudget != null) {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "A budget for '" + category + "' already exists. Please use the edit function instead.");
                return;
            }

            
            Budget newBudget = new Budget(category, amount, 0.0, SessionService.getCurrentUserId());
            newBudget.updateRemainingBudget();

            budgetDB.insertToDatabase(newBudget);

            
            categoryComboBox.setValue(null);
            budgetAmountField.clear();

            
            refreshBudgets();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Budget created successfully");

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid budget amount. Please enter a valid number.");
        }
    }

    /**
     * Show dialog to edit an existing budget
     */
    private void showEditBudgetDialog(Budget budget) {
        
        Dialog<Budget> dialog = new Dialog<>();
        dialog.setTitle("Edit Budget");
        dialog.setHeaderText("Edit Budget for " + budget.getCategory());

        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField amountField = new TextField(String.valueOf(budget.getAmount()));

        grid.add(new Label("Budget Amount:"), 0, 0);
        grid.add(amountField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        
        amountField.requestFocus();

        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Budget amount must be greater than zero");
                        return null;
                    }

                    budget.setAmount(amount);
                    budget.updateRemainingBudget();
                    return budget;
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid budget amount");
                    return null;
                }
            }
            return null;
        });

        
        Optional<Budget> result = dialog.showAndWait();

        result.ifPresent(updatedBudget -> {
            
            budgetDB.updateBudget(updatedBudget);
            
            refreshBudgets();
        });
    }

    /**
     * Show dialog to update spending for a budget
     */
    private void showUpdateSpendDialog(Budget budget) {
        
        Dialog<Budget> dialog = new Dialog<>();
        dialog.setTitle("Update Spending");
        dialog.setHeaderText("Update Spending for " + budget.getCategory());

        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField spendField = new TextField(String.valueOf(budget.getActual_spend()));

        grid.add(new Label("Current Budget:"), 0, 0);
        grid.add(new Label(String.format("$%.2f", budget.getAmount())), 1, 0);
        grid.add(new Label("Spent So Far:"), 0, 1);
        grid.add(new Label(String.format("$%.2f", budget.getActual_spend())), 1, 1);
        grid.add(new Label("New Spent Amount:"), 0, 2);
        grid.add(spendField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        
        spendField.requestFocus();

        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    double spend = Double.parseDouble(spendField.getText());
                    if (spend < 0) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Spent amount cannot be negative");
                        return null;
                    }

                    budget.setActual_spend(spend);
                    budget.updateRemainingBudget();
                    return budget;
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid amount");
                    return null;
                }
            }
            return null;
        });

        
        Optional<Budget> result = dialog.showAndWait();

        result.ifPresent(updatedBudget -> {
            
            budgetDB.updateBudget(updatedBudget);
            
            refreshBudgets();
        });
    }

    /**
     * Navigate back to the home screen
     */
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }

    /**
     * Display an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private VBox budgetsVBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField budgetAmountField;
    @FXML private Label remainingBudgetLabel;
}