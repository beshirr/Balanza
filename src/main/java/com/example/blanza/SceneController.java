package com.example.blanza;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneController {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlFile));
        Parent root = loader.load();
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }
}