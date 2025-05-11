package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class ReminderController implements Initializable {

    @FXML
    private TextField reminderTitleField;
    
    @FXML
    private TextArea reminderDescriptionArea;
    
    @FXML
    private DatePicker reminderDatePicker;
    
    @FXML
    private TextField reminderTimeField;
    
    @FXML
    private ComboBox<FinancialTask> financialTaskComboBox;
    
    @FXML
    private TableView<Reminder> remindersTable;
    
    @FXML
    private TableColumn<Reminder, String> titleColumn;
    
    @FXML
    private TableColumn<Reminder, LocalDateTime> timeColumn;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button deleteButton;
    
    private ReminderManager reminderManager;
    private ObservableList<Reminder> remindersList = FXCollections.observableArrayList();
    private ObservableList<FinancialTask> financialTasksList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        // Set up date picker with the current date
        reminderDatePicker.setValue(LocalDate.now());
        
        // Initialize reminder manager
        reminderManager = new ReminderManager();
        reminderManager.startReminderService();
        
        // Load reminders
        loadReminders();
        
        // Load financial tasks for the combo box
        loadFinancialTasks();
    }
    
    private void loadReminders() {
        // Clear and reload reminders from the manager
        remindersList.clear();
        remindersList.addAll(reminderManager.getAllReminders());
        remindersTable.setItems(remindersList);
    }
    
    private void loadFinancialTasks() {
        // Clear and load financial tasks from database
        financialTasksList.clear();
        List<FinancialTask> tasks = FinancialTaskDB.getAllFinancialTasks();
        financialTasksList.addAll(tasks);
        financialTaskComboBox.setItems(financialTasksList);
    }
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        String title = reminderTitleField.getText().trim();
        String description = reminderDescriptionArea.getText().trim();
        LocalDate date = reminderDatePicker.getValue();
        String timeText = reminderTimeField.getText().trim();
        FinancialTask selectedTask = financialTaskComboBox.getValue();
        
        // Validate input
        if (title.isEmpty() || date == null || timeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all required fields.");
            return;
        }
        
        // Parse time
        LocalTime time;
        try {
            time = LocalTime.parse(timeText);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid time in HH:MM format.");
            return;
        }
        
        // Create LocalDateTime from date and time
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        
        // Create reminder object
        Integer taskId = selectedTask != null ? selectedTask.getId() : null;
        Reminder reminder = new Reminder(
                Session.getCurrentUserId(),
                title,
                description,
                dateTime,
                taskId
        );
        
        // Add reminder using manager
        boolean success = reminderManager.addReminder(reminder);
        
        if (success) {
            // Clear fields
            clearFields();
            
            // Reload reminders
            loadReminders();
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reminder saved successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save reminder. Please check your inputs.");
        }
    }
    
    @FXML
    private void handleDeleteButton(ActionEvent event) {
        Reminder selectedReminder = remindersTable.getSelectionModel().getSelectedItem();
        
        if (selectedReminder == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a reminder to delete.");
            return;
        }
        
        reminderManager.deleteReminder(selectedReminder.getId());
        loadReminders();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Reminder deleted successfully.");
    }
    
    private void clearFields() {
        reminderTitleField.clear();
        reminderDescriptionArea.clear();
        reminderDatePicker.setValue(LocalDate.now());
        reminderTimeField.clear();
        financialTaskComboBox.setValue(null);
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}