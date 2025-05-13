package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the Income Tracking screen in the Balanza application.
 * <p>
 * This controller manages the user interface for recording and viewing income entries.
 * It provides functionality for:
 * <ul>
 *   <li>Displaying a table of income entries</li>
 *   <li>Adding new income records</li>
 *   <li>Showing summary information like total income</li>
 *   <li>Navigating back to the home screen</li>
 * </ul>
 * <p>
 * The controller uses the IncomeDB class to interact with the database for retrieving
 * and storing income records. It maintains an ObservableList of Income objects that
 * serves as the data model for the TableView.
 * <p>
 * This class follows the JavaFX Controller pattern and is connected to the income
 * tracking FXML view through FXML annotations and element IDs.
 *
 * @see Income
 * @see IncomeDB
 */
public class IncomeTrackingController {
    /** Data access object for income-related database operations */
    private final IncomeDB incomeDB = new IncomeDB();
    
    /** Observable collection of income records for binding to the TableView */
    private final ObservableList<Income> incomeList = FXCollections.observableArrayList();

    /** Text field for entering the income source */
    @FXML
    private TextField sourceField;
    
    /** Text field for entering the income amount */
    @FXML
    private TextField amountField;
    
    /** Label displaying the total income sum */
    @FXML
    private Label totalIncomeLabel;
    
    /** Table view for displaying income records */
    @FXML
    private TableView<Income> incomeTable;
    
    /** Table column for the income source */
    @FXML
    private TableColumn<Income, String> sourceColumn;
    
    /** Table column for the income amount */
    @FXML
    private TableColumn<Income, Double> amountColumn;
    
    /** Table column for the income date */
    @FXML
    private TableColumn<Income, LocalDate> dateColumn;

    /**
     * Initializes the income tracking screen.
     * <p>
     * This method is automatically called by the JavaFX framework after
     * the FXML has been loaded and all @FXML annotated fields are injected.
     * It performs the following setup tasks:
     * <ol>
     *   <li>Configures the table columns to display Income properties</li>
     *   <li>Loads income records from the database</li>
     *   <li>Sets up the table data binding</li>
     *   <li>Calculates and displays the total income</li>
     * </ol>
     * <p>
     * The method uses PropertyValueFactory to map Income object properties
     * to the corresponding table columns.
     */
    public void initialize() {
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("income_source"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("pay_date"));
        
        loadIncomes();
        
        incomeTable.setItems(incomeList);
        
        updateTotalIncomeDisplay();
    }
    
    /**
     * Loads income records from the database into the observable list.
     * <p>
     * This method:
     * <ol>
     *   <li>Retrieves all income records for the current user</li>
     *   <li>Clears the current observable list</li>
     *   <li>Adds the retrieved records to the list</li>
     * </ol>
     * <p>
     * Any errors during loading are caught, logged, and displayed to the user
     * via an alert dialog.
     */
    private void loadIncomes() {
        try {
            List<Income> incomes = incomeDB.getAllFromDatabase();
            incomeList.clear();
            incomeList.addAll(incomes);
        } catch (Exception e) {
            System.err.println("Error loading incomes: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading income data");
        }
    }

    /**
     * Calculates and displays the total sum of all income records.
     * <p>
     * This method:
     * <ol>
     *   <li>Sums the amount field from all income records in the list</li>
     *   <li>Formats the total as a currency string</li>
     *   <li>Updates the totalIncomeLabel with the formatted value</li>
     * </ol>
     * <p>
     * The total is displayed with a dollar sign and formatted to two decimal places.
     */
    private void updateTotalIncomeDisplay() {
        double total = incomeList.stream()
                .mapToDouble(Income::getAmount)
                .sum();
        totalIncomeLabel.setText(String.format("$%.2f", total));
    }

    /**
     * Handles the action for saving a new income record.
     * <p>
     * This method is triggered when the user submits the income form.
     * It performs the following operations:
     * <ol>
     *   <li>Retrieves and validates user input from the form fields</li>
     *   <li>Creates a new Income object with the current date</li>
     *   <li>Persists the new income record to the database</li>
     *   <li>Refreshes the income list and updates the total display</li>
     *   <li>Clears the input fields for the next entry</li>
     * </ol>
     * <p>
     * Validation checks include:
     * <ul>
     *   <li>Ensuring the source is not empty</li>
     *   <li>Validating that the amount is a valid number</li>
     *   <li>Ensuring the amount is greater than zero</li>
     * </ul>
     * <p>
     * Error messages are displayed to the user if validation fails.
     */
    @FXML
    public void handleSaveIncome() {
        String source = sourceField.getText();
        String amountText = amountField.getText();

        if (source == null || source.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter an income source");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Amount must be greater than zero");
                return;
            }
            
            Income newIncome = new Income(
                SessionService.getCurrentUserId(),
                source,
                amount,
                LocalDate.now()
            );
            
            incomeDB.insertToDatabase(newIncome);
            
            loadIncomes();
            
            updateTotalIncomeDisplay();
            
            sourceField.clear();
            amountField.clear();
            
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount entered");
        } catch (Exception e) {
            System.err.println("Error saving income: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error saving income");
        }
    }

    /**
     * Handles navigation back to the home screen.
     * <p>
     * This method is triggered when the user clicks the back button.
     * It uses the SceneController to switch to the home screen.
     *
     * @param actionEvent The event triggered by clicking the back button
     * @throws IOException If the home.fxml file cannot be loaded or another I/O error occurs
     */
    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }
    
    /**
     * Displays an alert dialog with the specified type and message.
     * <p>
     * This utility method creates and shows a JavaFX Alert dialog
     * with the provided alert type and content text. It's used throughout
     * the controller to provide feedback to the user about errors or
     * important information.
     *
     * @param type The type of alert to display (e.g., ERROR, INFORMATION)
     * @param content The message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}