package com.example.blanza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

/**
 * The type Index controller.
 */
public class IndexController {
    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        SceneController.switchScene("login.fxml", "Login");
    }

    @FXML
    private void goToSignUp(ActionEvent event) throws IOException {
        SceneController.switchScene("signup.fxml", "Sign up");
    }
}

