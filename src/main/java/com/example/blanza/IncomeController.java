package com.example.blanza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IncomeController  {


    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Income.fxml"));
            StackPane root = loader.load();


            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Income Tracking");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

