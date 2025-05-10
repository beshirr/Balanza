package com.example.blanza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IncomeController extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file and create the scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Income.fxml"));
            StackPane root = loader.load();

            // Set up the scene and the stage
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Income Tracking");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}

