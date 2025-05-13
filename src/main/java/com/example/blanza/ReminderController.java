package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Reminder Management screen in the Balanza application.
 * <p>
 * This controller manages the user interface for creating, viewing, and managing
 * reminders. It provides functionality for:
 * <ul>
 *   <li>Displaying existing reminders in a table</li>
 *   <li>Creating new reminders with optional task associations</li>
 *   <li>Setting reminder dates, times, and descriptions</li>
 *   <li>Linking reminders to financial tasks</li>
 * </ul>
 * <p>
 * The controller uses the ReminderManager to handle business logic and persistence
 * of reminder objects. It also manages a background reminder service that runs
 * to check for and display reminders when they become due.
 * <p>
 * This class implements the JavaFX Initializable interface to properly set up UI
 * components and load data when the screen is displayed.
 *
 * @see Reminder
 * @see ReminderManager
 * @see FinancialTask
 * @see Initializable
 */
public class ReminderController implements Initializable {
    /** Manager object for handling reminder business logic and persistence */
    private ReminderManager reminderManager;
    
    /** Observable collection of reminders for display in the table */
    private final ObservableList<Reminder> remindersList = FXCollections.observableArrayList();
    
    /** Observable collection of financial tasks for selection in the combo box */
    private final ObservableList<FinancialTask> financialTasksList = FXCollections.observableArrayList();
    
    /** Data access object for retrieving financial tasks */
    private final FinancialTaskDB db = new FinancialTaskDB();
    
    /** Text field for entering the reminder title */
    @FXML
    private TextField reminderTitleField;
    
    /** Text area for entering a detailed reminder description */
    @FXML
    private TextArea reminderDescriptionArea;
    
    /** Date picker for selecting the reminder date */
    @FXML
    private DatePicker reminderDatePicker;
    
    /** Text field for entering the reminder time */
    @FXML
    private TextField reminderTimeField;
    
    /** Combo box for selecting an associated financial task */
    @FXML
    private ComboBox<FinancialTask> financialTaskComboBox;
    
    /** Table view for displaying existing reminders */
    @FXML
    private TableView<Reminder> remindersTable;
    
    /** Table column for displaying reminder titles */
    @FXML
    private TableColumn<Reminder, String> titleColumn;
    
    /** Table column for displaying reminder dates and times */
    @FXML
    private TableColumn<Reminder, LocalDateTime> timeColumn;
    
    /** Button for saving new reminders */
    @FXML
    private Button saveButton;

    /**
     * Initializes the reminder management screen.
     * <p>
     * This method is automatically called by the JavaFX framework after
     * the FXML has been loaded and all @FXML annotated fields are injected.
     * It performs the following setup tasks:
     * <ol>
     *   <li>Configures the table columns to display Reminder properties</li>
     *   <li>Sets the default date in the date picker to the current date</li>
     *   <li>Initializes the ReminderManager and starts the reminder service</li>
     *   <li>Loads existing reminders into the table</li>
     *   <li>Loads financial tasks into the combo box for selection</li>
     * </ol>
     *
     * @param url The location used to resolve relative paths for resources
     * @param resourceBundle The resources for this controller's UI
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        reminderDatePicker.setValue(LocalDate.now());
        
        reminderManager = new ReminderManager();
        reminderManager.startReminderService();
        
        loadReminders();
        
        loadFinancialTasks();
    }
    
    /**
     * Loads all reminders for the current user from the database into the UI.
     * <p>
     * This method:
     * <ol>
     *   <li>Clears the current list of reminders</li>
     *   <li>Retrieves all reminders for the current user through the ReminderManager</li>
     *   <li>Adds the retrieved reminders to the observable list</li>
     *   <li>Updates the table view to display the loaded reminders</li>
     * </ol>
     * <p>
     * The method is called during initialization and after adding a new reminder
     * to ensure the table always shows the current state of reminders.
     */
    private void loadReminders() {
        remindersList.clear();
        remindersList.addAll(reminderManager.getAllReminders());
        remindersTable.setItems(remindersList);
    }
    
    /**
     * Loads all financial tasks for the current user from the database into the UI.
     * <p>
     * This method:
     * <ol>
     *   <li>Clears the current list of financial tasks</li>
     *   <li>Retrieves all tasks for the current user from the database</li>
     *   <li>Adds the retrieved tasks to the observable list</li>
     *   <li>Updates the combo box to allow selection from the loaded tasks</li>
     * </ol>
     * <p>
     * This allows users to associate reminders with existing financial tasks
     * when creating new reminders.
     */
    private void loadFinancialTasks() {
        financialTasksList.clear();
        List<FinancialTask> tasks = db.getAllFromDatabase();
        financialTasksList.addAll(tasks);
        financialTaskComboBox.setItems(financialTasksList);
    }
    
    /**
     * Handles the action for saving a new reminder.
     * <p>
     * This method is triggered when the user clicks the save button.
     * It performs the following operations:
     * <ol>
     *   <li>Retrieves and validates user input from the form fields</li>
     *   <li>Parses and validates the time input</li>
     *   <li>Creates a LocalDateTime by combining the date and time</li>
     *   <li>Creates a new Reminder object with the user's inputs</li>
     *   <li>Persists the reminder through the ReminderManager</li>
     *   <li>Updates the UI and provides feedback to the user</li>
     * </ol>
     * <p>
     * Validation is performed to ensure all required fields are filled and
     * the time format is valid before attempting to save the reminder.
     *
     * @param event The ActionEvent triggered by clicking the save button
     */
    @FXML
    private void handleSaveButton(ActionEvent event) {
        String title = reminderTitleField.getText().trim();
        String description = reminderDescriptionArea.getText().trim();
        LocalDate date = reminderDatePicker.getValue();
        String timeText = reminderTimeField.getText().trim();
        FinancialTask selectedTask = financialTaskComboBox.getValue();
        
        if (title.isEmpty() || date == null || timeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all required fields.");
            return;
        }
        
        LocalTime time;
        try {
            time = LocalTime.parse(timeText);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid time in HH:MM format.");
            return;
        }
        
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        
        Integer taskId = selectedTask != null ? selectedTask.getId() : null;
        Reminder reminder = new Reminder(
                SessionService.getCurrentUserId(),
                title,
                description,
                dateTime,
                taskId
        );
        
        boolean success = reminderManager.addReminder(reminder);
        
        if (success) {
            clearFields();
            
            loadReminders();
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reminder saved successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save reminder. Please check your inputs.");
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
    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }
    
    /**
     * Clears all input fields in the reminder form.
     * <p>
     * This method:
     * <ol>
     *   <li>Clears the title and description text fields</li>
     *   <li>Resets the date picker to the current date</li>
     *   <li>Clears the time text field</li>
     *   <li>Clears the selected task in the combo box</li>
     * </ol>
     * <p>
     * This is called after successfully saving a reminder to prepare the form
     * for the next entry.
     */
    private void clearFields() {
        reminderTitleField.clear();
        reminderDescriptionArea.clear();
        reminderDatePicker.setValue(LocalDate.now());
        reminderTimeField.clear();
        financialTaskComboBox.setValue(null);
    }
    
    /**
     * Displays an alert dialog with the specified type, title, and message.
     * <p>
     * This utility method creates and shows a JavaFX Alert dialog
     * with the provided parameters. It's used throughout the controller
     * to provide feedback to the user about errors, warnings, or successful operations.
     *
     * @param alertType The type of alert to display (e.g., ERROR, INFORMATION)
     * @param title The title for the alert dialog
     * @param message The message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}