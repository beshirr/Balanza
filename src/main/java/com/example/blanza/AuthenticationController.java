package com.example.blanza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.IOException;

/**
 * The type Authentication controller.
 */
public class AuthenticationController {
    @FXML private TextField emailField;
    @FXML private TextField OTPField;

    @FXML
    private void handleVerify(ActionEvent event) throws IOException {
        String OTP = OTPField.getText();
        if (AuthenticationService.verifyOTP(OTP)) {
            UserDB.setVerified(SessionManager.loadSession());
            SceneController.switchScene("home.fxml", "Balanza");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect OTP");
            alert.showAndWait();
        }
    }
}
