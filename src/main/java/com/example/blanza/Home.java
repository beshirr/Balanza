package com.example.blanza;

import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Controller class for the home screen of the Balanza personal finance application.
 * <p>
 * The home screen serves as the main navigation hub for the application, providing
 * access to all primary functional areas through a set of navigation buttons. This
 * controller handles user interactions with these navigation elements and manages
 * transitions between different application screens.
 * <p>
 * Key responsibilities of this class include:
 * <ul>
 *   <li>Managing navigation to major application features</li>
 *   <li>Handling the user logout process</li>
 *   <li>Coordinating with the SceneController to perform scene transitions</li>
 * </ul>
 * <p>
 * This controller is automatically instantiated by the JavaFX FXMLLoader when
 * the home.fxml view is loaded. Each method in this class corresponds to a button
 * handler specified in the FXML file.
 */
public class Home {
    /**
     * Handles user navigation to the expense tracking screen.
     * <p>
     * This method is triggered when the user clicks the "Expenses" button on the home screen.
     * It uses the SceneController to switch the active scene to the expense tracking view,
     * allowing users to view, add, edit, and analyze their expenses.
     *
     * @param actionEvent The event triggered by clicking the expenses button
     * @throws IOException If the expense tracking FXML file cannot be loaded or another I/O error occurs
     */
    public void handleExpensesBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("track_expenses.fxml", "Track Expenses");
    }

    /**
     * Handles user navigation to the budgeting screen.
     * <p>
     * This method is triggered when the user clicks the "Budget" button on the home screen.
     * It uses the SceneController to switch the active scene to the budgeting view, allowing
     * users to create, view, and manage their budget plans and spending limits.
     *
     * @param actionEvent The event triggered by clicking the budget button
     * @throws IOException If the budgeting FXML file cannot be loaded or another I/O error occurs
     */
    public void handleBudgetBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("Budgeting.fxml", "Budgeting");
    }

    /**
     * Handles user navigation to the income tracking screen.
     * <p>
     * This method is triggered when the user clicks the "Income" button on the home screen.
     * It uses the SceneController to switch the active scene to the income tracking view,
     * allowing users to record, view, and analyze their income sources and amounts.
     *
     * @param actionEvent The event triggered by clicking the income button
     * @throws IOException If the income FXML file cannot be loaded or another I/O error occurs
     */
    public void handleIncomeBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("income.fxml", "Income");
    }
    
    /**
     * Handles user navigation to the reminders screen.
     * <p>
     * This method is triggered when the user clicks the "Reminders" button on the home screen.
     * It uses the SceneController to switch the active scene to the reminders view, allowing
     * users to set up, view, and manage financial reminders and scheduled tasks.
     *
     * @param actionEvent The event triggered by clicking the reminders button
     * @throws IOException If the reminders FXML file cannot be loaded or another I/O error occurs
     */
    public void handleRemindersBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("reminders.fxml", "Reminders");
    }

    /**
     * Handles the user logout process.
     * <p>
     * This method is triggered when the user clicks the "Logout" button on the home screen.
     * It performs the following operations:
     * <ol>
     *   <li>Clears the current user session by setting the user ID to -1</li>
     *   <li>Persists the session state through the SessionManager</li>
     *   <li>Redirects the user to the application index/login screen</li>
     * </ol>
     * <p>
     * After logout, the user must log in again to access any protected features
     * of the application.
     *
     * @param actionEvent The event triggered by clicking the logout button
     * @throws IOException If the index FXML file cannot be loaded or another I/O error occurs
     */
    public void handleLogoutBtn(ActionEvent actionEvent) throws IOException {
        SessionManager.saveSession(-1);
        SceneController.switchScene("index.fxml", "Index");
    }
}