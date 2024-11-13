package com.shep.marufx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent parent = FXMLLoader.<Parent>load(HelloApplication.class.getResource("main_view.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}