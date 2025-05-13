package com.example.blanza;
// TODO: too many calls fot getAll expenses
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.layout.HBox;

public class TrackExpenses {
    private ExpenseManager expenseManager;

    @FXML
    private Label expenseCount;

    private void updateExpenseCount() {
        int count = expenseManager.getAll().size();
        expenseCount.setText(count + (count == 1 ? " item" : " items"));
    }

    @FXML
    private void initialize() {
        expenseManager = new ExpenseManager();
        updateDashboard();
        loadExpenses();
        updateExpenseCount();
    }

    @FXML
    private void handleAddExpense() {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle("Add New Expense");
        dialog.setHeaderText("Enter expense details");
        
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("list-container");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.getStyleClass().add("primary-button");
        
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("secondary-button");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.getStyleClass().add("stats-grid");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setEditable(true);
        categoryComboBox.setPromptText("Select or create category");
        categoryComboBox.getStyleClass().add("combo-box");
        categoryComboBox.setPrefWidth(250);
        
        populateCategoryComboBox(categoryComboBox);

        // Create editable combo box for payment methods
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.setEditable(true);
        paymentMethodComboBox.setPromptText("Select or create payment method");
        paymentMethodComboBox.getStyleClass().add("combo-box");
        paymentMethodComboBox.setPrefWidth(250);
        
        // Populate with existing payment methods
        populatePaymentMethodComboBox(paymentMethodComboBox);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.getStyleClass().add("text-field");
        amountField.setPrefWidth(250);
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("date-picker");
        datePicker.setPrefWidth(250);

        // Style the labels
        Label categoryLabel = new Label("Category:");
        categoryLabel.getStyleClass().add("stat-label");
        
        Label amountLabel = new Label("Amount:");
        amountLabel.getStyleClass().add("stat-label");
        
        Label paymentMethodLabel = new Label("Payment Method:");
        paymentMethodLabel.getStyleClass().add("stat-label");
        
        Label dateLabel = new Label("Date:");
        dateLabel.getStyleClass().add("stat-label");
        
        // Add components to grid
        grid.add(categoryLabel, 0, 0);
        grid.add(categoryComboBox, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(paymentMethodLabel, 0, 2);
        grid.add(paymentMethodComboBox, 1, 2);
        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);

        // Add title to the form
        VBox content = new VBox(10);
        Label formTitle = new Label("New Expense");
        formTitle.getStyleClass().add("section-title");
        content.getChildren().addAll(formTitle, new Separator(), grid);
        dialog.getDialogPane().setContent(content);

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
            expenseManager.addEntity(expense);
            
            // Confirmation message with styling
            showSuccessAlert("Expense added successfully!");
            
            // Update UI
            updateDashboard();
            loadExpenses();
            updateExpenseCount();
        });
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply styling to the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("list-container");
        
        // Style the OK button
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("secondary-button");
        
        // Show the alert
        alert.show();
    }
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply styling to the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("list-container");
        
        // Style the OK button
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("primary-button");
        
        // Show the alert
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
        for (Expense expense : expenseManager.getAll()) {
            categories.add(expense.getCategory());
        }
        
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
        for (Expense expense : expenseManager.getAll()) {
            paymentMethods.add(expense.getPaymentMethod());
        }
        
        // Add to combo box
        comboBox.getItems().addAll(paymentMethods);
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }

    private void updateDashboard() {
        expenses.setText("$" + expenseManager.getTotal());
    }

    private void loadExpenses() {
        expenseListView.getItems().clear();
        expenseListView.setCellFactory(param -> new ListCell<String>() {
            private final HBox container = new HBox(15);
            private final Label categoryLabel = new Label();
            private final Label amountLabel = new Label();
            private final Label dateLabel = new Label();
            
            {
                // Set up container with proper spacing and alignment
                container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                container.setPadding(new Insets(8, 5, 8, 5));
                
                // Style the category label
                categoryLabel.getStyleClass().add("stat-label");
                categoryLabel.setMinWidth(120);
                categoryLabel.setMaxWidth(120);
                
                // Style the amount label
                amountLabel.getStyleClass().add("stat-value");
                amountLabel.setMinWidth(100);
                
                // Style the date label
                dateLabel.getStyleClass().add("timestamp-label");
                
                // Add all labels to container
                container.getChildren().addAll(categoryLabel, amountLabel, dateLabel);
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    // Parse the item string (format: "Category - $Amount - Date")
                    String[] parts = item.split(" - ");
                    if (parts.length >= 3) {
                        categoryLabel.setText(parts[0]);
                        amountLabel.setText(parts[1]);
                        dateLabel.setText(parts[2]);
                        
                        // Apply color to amount (red for expenses)
                        amountLabel.setStyle("-fx-text-fill: -accent-color;");
                        
                        setGraphic(container);
                    }
                }
            }
        });
        
        // Add header to the list
        if (expenseListView.getItems().isEmpty() && !expenseManager.getAll().isEmpty()) {
            // Add a label at the top of the list as a header
            HBox headerContainer = new HBox(15);
            headerContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            headerContainer.setPadding(new Insets(0, 0, 10, 5));
            
            Label categoryHeader = new Label("CATEGORY");
            categoryHeader.getStyleClass().add("sidebar-section");
            categoryHeader.setMinWidth(120);
            categoryHeader.setMaxWidth(120);
            
            Label amountHeader = new Label("AMOUNT");
            amountHeader.getStyleClass().add("sidebar-section");
            amountHeader.setMinWidth(100);
            
            Label dateHeader = new Label("DATE");
            dateHeader.getStyleClass().add("sidebar-section");
            
            headerContainer.getChildren().addAll(categoryHeader, amountHeader, dateHeader);
        }
        
        // Add expense items
        for (Expense expense : expenseManager.getAll()) {
            expenseListView.getItems().add(expense.getCategory() + " - $" + expense.getAmount() + " - " + expense.getDate());
        }
        
        // Add placeholder if list is empty
        if (expenseListView.getItems().isEmpty()) {
            Label placeholder = new Label("No expenses found. Click '+ Add Expense' to get started!");
            placeholder.getStyleClass().add("insight-label");
            placeholder.setWrapText(true);
            placeholder.setMaxWidth(400);
            VBox placeholderContainer = new VBox(placeholder);
            placeholderContainer.setAlignment(javafx.geometry.Pos.CENTER);
            expenseListView.setPlaceholder(placeholderContainer);
        }
    }

    @FXML
    private void handleViewReports(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("expense_reports.fxml", "Expense Reports");
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