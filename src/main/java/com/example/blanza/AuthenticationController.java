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
        String email = emailField.getText();
        String OTP = OTPField.getText();
        UserInfo userInfo = UserDB.getUserInfoByEmail(email);
        if (userInfo.getOtp().equals(OTP)) {
            SceneController.switchScene("Budgeting.fxml", "Budgeting");
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
