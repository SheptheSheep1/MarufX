package com.shep.marufx;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Location location = new Location(false);
            PrayerTimes prayerTimes = new PrayerTimes(location, 0.0);
            SharedModel sharedModel = new SharedModel();
            CalcMethod calcMethod = new CalcMethod("ISNA", 15.0, 15.0, false, "Islamic Society of North America");
            sharedModel.setObjPrayerTimes(prayerTimes);
            sharedModel.setLocation(location);
            sharedModel.setCalcMethod(calcMethod);
            sharedModel.setMonthDayYear(LocalDateTime.now());
            System.out.println("Set");

            UserAgentBuilder.builder()
                    .themes(JavaFXThemes.MODENA) // Optional if you don't need JavaFX's default theme, still recommended though
                    .themes(MaterialFXStylesheets.forAssemble(true)) // Adds the MaterialFX's default theme. The boolean argument is to include legacy controls
                    .setDeploy(true) // Whether to deploy each theme's assets on a temporary dir on the disk
                    .setResolveAssets(true) // Whether to try resolving @import statements and resources urls
                    .build() // Assembles all the added themes into a single CSSFragment (very powerful class check its documentation)
                    .setGlobal(); // Finally, sets the produced stylesheet as the global User-Agent stylesheet
            //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            //MFXThemeManager.setTheme(MFXTheme.DARK);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_view.fxml"));
            // Create the controller manually
            MainViewController mainController = new MainViewController();
            mainController.setSharedModel(sharedModel);

            // Associate the manually created controller with the FXML loader
            loader.setController(mainController);

            Parent mainRoot = loader.load(); // Load the FXML file with the custom controller
            mainController.setSharedModel(sharedModel);
            mainController.setStage(stage);
            // Set up navigation in the main controller
            Scene scene = new Scene(mainRoot);
            // Load settings scene
            FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("settings_view.fxml"));
            Parent settingsRoot = settingsLoader.load();
            SettingsViewController settingsController = settingsLoader.getController();
            settingsController.setSettingsModel(sharedModel);

            settingsController.setMainScene(scene); // Pass the main scene to the settings controller
            System.out.println("ja");
            Scene settingsScene = new Scene(settingsRoot);

            mainController.setSettingsScene(settingsScene);
            scene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                if (newWindow != null) {
                    newWindow.setOnShown(event -> {
                        mainController.onSceneActivated();
                    });
                }
            });
            stage.setOnShown(event -> mainController.onSceneActivated());

            stage.setWidth(800);
            stage.setHeight(600);
            stage.setTitle("MarufX");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}