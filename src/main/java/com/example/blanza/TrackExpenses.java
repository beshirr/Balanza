package com.example.blanza;

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

/**
 * Controller for the expense tracking view in the Balanza application.
 * <p>
 * This controller manages the user interface and business logic for tracking personal expenses.
 * It provides functionality for viewing existing expenses, adding new expenses, and
 * displaying summary information about the user's spending habits.
 * <p>
 * Key features include:
 * <ul>
 *   <li>Displaying a list of recorded expenses</li>
 *   <li>Adding new expenses with categorization</li>
 *   <li>Showing expense summaries and totals</li>
 *   <li>Navigation to detailed expense reports</li>
 * </ul>
 * <p>
 * The controller interacts with the {@link ExpenseManager} service to persist and
 * retrieve expense data, and with the {@link SessionService} to identify the current user.
 * All expenses are associated with the currently logged-in user.
 * <p>
 * This controller is bound to the track_expenses.fxml view file, which defines the
 * layout and visual elements of the expense tracking interface.
 *
 * @see ExpenseManager
 * @see Expense
 * @see SessionService
 */
public class TrackExpenses {
    
    /**
     * Service for managing expense data persistence and retrieval.
     * <p>
     * This field is initialized in the {@link #initialize()} method and provides
     * access to all expense-related operations like adding, retrieving, and
     * calculating statistics for expenses.
     */
    private ExpenseManager expenseManager;

    /**
     * Label displaying the total count of expenses.
     * <p>
     * This label shows the number of expense records in the format "X items" or
     * "1 item" (singular form used when appropriate).
     */
    @FXML
    private Label expenseCount;

    /**
     * Label displaying the user's overall balance.
     * <p>
     * This field is updated when the dashboard is refreshed.
     */
    @FXML
    private Label balance;
    
    /**
     * Label displaying the user's total income.
     * <p>
     * This field is updated when the dashboard is refreshed.
     */
    @FXML
    private Label income;
    
    /**
     * Label displaying the user's total expenses.
     * <p>
     * This field shows the cumulative amount spent across all expense categories.
     * It is updated whenever the dashboard is refreshed.
     */
    @FXML
    private Label expenses;
    
    /**
     * Text field for entering or editing an expense category.
     */
    @FXML
    private TextField categoryField;
    
    /**
     * Text field for entering or editing an expense amount.
     */
    @FXML
    private TextField amountField;
    
    /**
     * Text field for entering or editing a payment method.
     */
    @FXML
    private TextField paymentMethodField;
    
    /**
     * ListView component displaying all user expenses.
     * <p>
     * Each item in the list represents an expense with its category, amount, and date.
     * The list is populated in the {@link #loadExpenses()} method.
     */
    @FXML
    private ListView<String> expenseListView;

    /**
     * Updates the displayed expense count.
     * <p>
     * This method retrieves the current number of expenses from the expense manager
     * and updates the expense count label with the appropriate singular or plural form.
     */
    private void updateExpenseCount() {
        int count = expenseManager.getAll().size();
        expenseCount.setText(count + (count == 1 ? " item" : " items"));
    }

    /**
     * Initializes the controller.
     * <p>
     * This method is automatically called by JavaFX when the view is loaded. It:
     * <ol>
     *   <li>Initializes the expense manager service</li>
     *   <li>Updates the financial dashboard summary</li>
     *   <li>Loads and displays existing expenses</li>
     *   <li>Updates the expense count indicator</li>
     * </ol>
     * <p>
     * All expense data is filtered to show only expenses for the currently
     * authenticated user, as determined by {@link SessionService}.
     */
    @FXML
    private void initialize() {
        expenseManager = new ExpenseManager();
        updateDashboard();
        loadExpenses();
        updateExpenseCount();
    }

    /**
     * Handles adding a new expense.
     * <p>
     * This method is triggered when the user clicks the "Add Expense" button. It:
     * <ol>
     *   <li>Creates and configures a dialog for entering expense details</li>
     *   <li>Sets up form fields for category, amount, payment method, and date</li>
     *   <li>Populates category and payment method dropdowns with existing values</li>
     *   <li>Validates user input</li>
     *   <li>Creates and saves the new expense when input is valid</li>
     *   <li>Updates the UI to reflect the new expense</li>
     * </ol>
     * <p>
     * The dialog supports both selecting from existing categories/payment methods
     * and creating new ones through editable ComboBox components.
     */
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

        
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.setEditable(true);
        paymentMethodComboBox.setPromptText("Select or create payment method");
        paymentMethodComboBox.getStyleClass().add("combo-box");
        paymentMethodComboBox.setPrefWidth(250);
        
        
        populatePaymentMethodComboBox(paymentMethodComboBox);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.getStyleClass().add("text-field");
        amountField.setPrefWidth(250);
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("date-picker");
        datePicker.setPrefWidth(250);

        
        Label categoryLabel = new Label("Category:");
        categoryLabel.getStyleClass().add("stat-label");
        
        Label amountLabel = new Label("Amount:");
        amountLabel.getStyleClass().add("stat-label");
        
        Label paymentMethodLabel = new Label("Payment Method:");
        paymentMethodLabel.getStyleClass().add("stat-label");
        
        Label dateLabel = new Label("Date:");
        dateLabel.getStyleClass().add("stat-label");
        
        
        grid.add(categoryLabel, 0, 0);
        grid.add(categoryComboBox, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(paymentMethodLabel, 0, 2);
        grid.add(paymentMethodComboBox, 1, 2);
        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);

        
        VBox content = new VBox(10);
        Label formTitle = new Label("New Expense");
        formTitle.getStyleClass().add("section-title");
        content.getChildren().addAll(formTitle, new Separator(), grid);
        dialog.getDialogPane().setContent(content);

        
        Platform.runLater(categoryComboBox::requestFocus);

        
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

        
        Optional<Expense> result = dialog.showAndWait();
        
        result.ifPresent(expense -> {
            expenseManager.addEntity(expense);
            
            
            showSuccessAlert("Expense added successfully!");
            
            
            updateDashboard();
            loadExpenses();
            updateExpenseCount();
        });
    }

    /**
     * Displays an error alert with the specified message.
     * <p>
     * This utility method creates and shows a styled error dialog with the given
     * message. It applies consistent styling from the application's CSS theme.
     *
     * @param message The error message to display
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("list-container");
        
        
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("secondary-button");
        
        
        alert.show();
    }
    
    /**
     * Displays a success alert with the specified message.
     * <p>
     * This utility method creates and shows a styled success dialog with the given
     * message. It applies consistent styling from the application's CSS theme.
     *
     * @param message The success message to display
     */
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("list-container");
        
        
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("primary-button");
        
        
        alert.show();
    }

    /**
     * Populates the category ComboBox with predefined and existing categories.
     * <p>
     * This method:
     * <ol>
     *   <li>Adds common expense categories as default options</li>
     *   <li>Adds all unique categories from existing expenses</li>
     *   <li>Ensures no duplicate categories by using a Set</li>
     * </ol>
     * <p>
     * The ComboBox is configured to be editable, allowing users to create
     * custom categories not in the predefined list.
     *
     * @param comboBox The ComboBox component to populate with categories
     */
    private void populateCategoryComboBox(ComboBox<String> comboBox) {
        
        Set<String> categories = new HashSet<>();
        
        
        categories.add("Food");
        categories.add("Transportation");
        categories.add("Housing");
        categories.add("Entertainment");
        categories.add("Utilities");
        categories.add("Healthcare");
        
        
        for (Expense expense : expenseManager.getAll()) {
            categories.add(expense.getCategory());
        }
        
        comboBox.getItems().addAll(categories);
    }

    /**
     * Populates the payment method ComboBox with predefined and existing methods.
     * <p>
     * This method:
     * <ol>
     *   <li>Adds common payment methods as default options</li>
     *   <li>Adds all unique payment methods from existing expenses</li>
     *   <li>Ensures no duplicate methods by using a Set</li>
     * </ol>
     * <p>
     * The ComboBox is configured to be editable, allowing users to create
     * custom payment methods not in the predefined list.
     *
     * @param comboBox The ComboBox component to populate with payment methods
     */
    private void populatePaymentMethodComboBox(ComboBox<String> comboBox) {
        
        Set<String> paymentMethods = new HashSet<>();
        
        
        paymentMethods.add("Cash");
        paymentMethods.add("Credit Card");
        paymentMethods.add("Debit Card");
        paymentMethods.add("Mobile Payment");
        paymentMethods.add("Bank Transfer");
        
        
        for (Expense expense : expenseManager.getAll()) {
            paymentMethods.add(expense.getPaymentMethod());
        }
        
        
        comboBox.getItems().addAll(paymentMethods);
    }

    /**
     * Handles navigation back to the home screen.
     * <p>
     * This method is triggered when the user clicks the "Back" or "Home" button.
     * It uses the {@link SceneController} to transition to the application's
     * home screen.
     *
     * @param actionEvent The event that triggered this handler
     * @throws IOException If an error occurs during scene transition
     */
    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }

    /**
     * Updates the financial dashboard summary.
     * <p>
     * This method refreshes the expense total displayed in the UI based on
     * the current expense data. It formats the total with a currency symbol.
     */
    private void updateDashboard() {
        expenses.setText("$" + expenseManager.getTotal());
    }

    /**
     * Loads and displays the user's expenses in the ListView.
     * <p>
     * This method:
     * <ol>
     *   <li>Clears the existing list items</li>
     *   <li>Configures a custom cell factory for formatting expense entries</li>
     *   <li>Creates column headers if expenses exist</li>
     *   <li>Populates the list with formatted expense entries</li>
     *   <li>Sets a meaningful placeholder when no expenses exist</li>
     * </ol>
     * <p>
     * Each expense is displayed with its category, amount, and date in a
     * structured layout with consistent styling.
     */
    private void loadExpenses() {
        expenseListView.getItems().clear();
        expenseListView.setCellFactory(param -> new ListCell<String>() {
            private final HBox container = new HBox(15);
            private final Label categoryLabel = new Label();
            private final Label amountLabel = new Label();
            private final Label dateLabel = new Label();
            
            {
                
                container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                container.setPadding(new Insets(8, 5, 8, 5));
                
                
                categoryLabel.getStyleClass().add("stat-label");
                categoryLabel.setMinWidth(120);
                categoryLabel.setMaxWidth(120);
                
                
                amountLabel.getStyleClass().add("stat-value");
                amountLabel.setMinWidth(100);
                
                
                dateLabel.getStyleClass().add("timestamp-label");
                
                
                container.getChildren().addAll(categoryLabel, amountLabel, dateLabel);
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    
                    String[] parts = item.split(" - ");
                    if (parts.length >= 3) {
                        categoryLabel.setText(parts[0]);
                        amountLabel.setText(parts[1]);
                        dateLabel.setText(parts[2]);
                        
                        
                        amountLabel.setStyle("-fx-text-fill: -accent-color;");
                        
                        setGraphic(container);
                    }
                }
            }
        });
        
        
        if (expenseListView.getItems().isEmpty() && !expenseManager.getAll().isEmpty()) {
            
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
        
        
        for (Expense expense : expenseManager.getAll()) {
            expenseListView.getItems().add(expense.getCategory() + " - $" + expense.getAmount() + " - " + expense.getDate());
        }
        
        
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

    /**
     * Handles navigation to the expense reports view.
     * <p>
     * This method is triggered when the user clicks the "View Reports" button.
     * It uses the {@link SceneController} to transition to the expense reports
     * screen, which provides detailed analytics and visualizations of spending patterns.
     *
     * @param actionEvent The event that triggered this handler
     * @throws IOException If an error occurs during scene transition
     */
    @FXML
    private void handleViewReports(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("expense_reports.fxml", "Expense Reports");
    }
}