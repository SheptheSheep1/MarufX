package com.shep.marufx;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsMenuApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the TabPane that will hold the tabs
        TabPane tabPane = new TabPane();

        // Create "General" tab
        Tab generalTab = new Tab("General");
        generalTab.setClosable(false);  // Disable the ability to close the tab

        // Create content for "General" tab
        VBox generalContent = new VBox(10);
        generalContent.getChildren().addAll(
                new Label("App Language"),
                new ComboBox<String>(),  // Example: ComboBox to select language
                new CheckBox("Enable notifications"),
                new Button("Save Settings")
        );
        generalTab.setContent(generalContent);

        // Create "Display" tab
        Tab displayTab = new Tab("Display");
        displayTab.setClosable(false);

        // Create content for "Display" tab
        VBox displayContent = new VBox(10);
        displayContent.getChildren().addAll(
                new Label("Brightness"),
                new Slider(0, 100, 50),  // Example: Slider for brightness
                new Label("Screen Resolution"),
                new ComboBox<String>() // Example: ComboBox for screen resolution
        );
        displayTab.setContent(displayContent);

        // Create "Audio" tab
        Tab audioTab = new Tab("Audio");
        audioTab.setClosable(false);

        // Create content for "Audio" tab
        VBox audioContent = new VBox(10);
        audioContent.getChildren().addAll(
                new Label("Volume"),
                new Slider(0, 100, 50),  // Example: Slider for volume control
                new CheckBox("Mute")
        );
        audioTab.setContent(audioContent);

        // Add the tabs to the TabPane
        tabPane.getTabs().addAll(generalTab, displayTab, audioTab);

        // Set the first tab as the selected tab
        tabPane.getSelectionModel().selectFirst();

        // Create the Scene and set it on the Stage
        Scene scene = new Scene(tabPane, 400, 300);
        primaryStage.setTitle("Settings Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
