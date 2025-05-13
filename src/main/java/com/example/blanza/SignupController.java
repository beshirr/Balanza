package com.example.blanza;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Controller for the user registration (signup) view in the Balanza application.
 * <p>
 * This controller manages the user interface and business logic for the user registration
 * process. It collects user input from form fields, validates the input, and delegates
 * to the {@link UserManager} for processing the registration request.
 * <p>
 * The signup view contains fields for:
 * <ul>
 *   <li>Username</li>
 *   <li>Email address</li>
 *   <li>Phone number</li>
 *   <li>Password (with confirmation)</li>
 * </ul>
 * <p>
 * Upon successful registration, users are redirected to the authentication screen.
 * In case of validation failures or other errors, appropriate error messages are
 * displayed to guide the user in correcting their input.
 * <p>
 * This controller is associated with the signup.fxml view file, which defines the
 * layout and visual elements of the registration form.
 *
 * @see UserManager
 * @see SceneController
 */
public class SignupController {
    
    /**
     * Button to trigger the signup process when clicked.
     * <p>
     * This button is public to allow direct access from FXML, though typically
     * interaction is handled through the {@link #handleSignup} method.
     */
    public Button signup;
    
    /**
     * Text field for entering the desired username.
     * <p>
     * The username serves as the user's primary identifier for login purposes
     * and should be unique within the system.
     */
    @FXML 
    private TextField usernameField;
    
    /**
     * Text field for entering the user's email address.
     * <p>
     * The email may be used for account verification, password recovery,
     * and communications from the application.
     */
    @FXML 
    private TextField emailField;
    
    /**
     * Text field for entering the user's phone number.
     * <p>
     * The phone number can be used as an alternative contact method or
     * for multi-factor authentication purposes.
     */
    @FXML 
    private TextField phoneNumberField;
    
    /**
     * Password field for entering the desired account password.
     * <p>
     * The password should meet the application's security requirements,
     * which are validated during the signup process.
     */
    @FXML 
    private PasswordField passwordField;
    
    /**
     * Password field for confirming the user's password entry.
     * <p>
     * This second password entry helps prevent typos during password creation
     * by requiring the user to enter the same password twice.
     */
    @FXML 
    private PasswordField confirmPasswordField;

    /**
     * Handles the user signup request when the signup button is clicked.
     * <p>
     * This method:
     * <ol>
     *   <li>Collects all user input from the form fields</li>
     *   <li>Delegates to {@link UserManager#signupProcess} for validation and processing</li>
     *   <li>On success, redirects to the authentication screen</li>
     *   <li>On failure, displays an error alert to the user</li>
     * </ol>
     * <p>
     * The validation logic is contained within the UserManager, allowing this controller
     * to focus on UI interaction. Validation checks may include:
     * <ul>
     *   <li>Ensuring all required fields are completed</li>
     *   <li>Verifying password and confirmation match</li>
     *   <li>Checking that the username is not already taken</li>
     *   <li>Validating email format</li>
     *   <li>Ensuring password meets complexity requirements</li>
     * </ul>
     * <p>
     * If validation fails or an error occurs during registration, a generic error
     * message is displayed to the user. For security reasons, specific validation
     * failures are not detailed in the error message to prevent information disclosure.
     *
     * @param event The action event triggered by the signup button click
     * @throws IOException If an error occurs during scene transition
     * 
     * @see UserManager#signupProcess
     * @see SceneController#switchScene
     */
    @FXML
    private void handleSignup(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (UserManager.signupProcess(username, email, phoneNumber, password, confirmPassword)) {
            SceneController.switchScene("authentication.fxml", "Authentication");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your credentials");
            alert.showAndWait();
        }
    }
}