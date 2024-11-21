package com.shep.marufx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Year;
import java.util.stream.IntStream;

public class SettingsViewController {



    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private ComboBox<Integer> monthComboBox;

    @FXML
    private ComboBox<Integer> dayComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private ComboBox<String> calcMethodComboBox;


    @FXML
    private TextField locationTextField;
    @FXML
    private CheckBox hanafiAsrCheckBox;

    private SharedModel settingsModel;
    private Scene mainScene;

    @FXML
    private void handleBackButton(ActionEvent event) {
        System.out.println("back button pressed");
        saveSettings();
        // Switch to the main scene
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainScene);
        System.out.println("back processed");
    }

    public void setSettingsModel(SharedModel settingsModel) {
        this.settingsModel = settingsModel;
        loadSettings();
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }


    @FXML
    public void initialize() {
        // Populate month, day, and year dropdowns
        monthComboBox.getItems().addAll(IntStream.rangeClosed(1, 12).boxed().toList());
        dayComboBox.getItems().addAll(IntStream.rangeClosed(1, 31).boxed().toList());
        yearComboBox.getItems().addAll(IntStream.rangeClosed(2000, Year.now().getValue()).boxed().toList());

        // Update Hanafi Asr method when checkbox changes
        hanafiAsrCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                settingsModel.setHanafiAsrMethod(newValue)
        );
        // Load calculation methods into the ComboBox
        try {
            String[][] methods = CSVReader.readCSV("./src/main/resources/calc_test.csv");
            for (String[] row : methods) {
                calcMethodComboBox.getItems().add(row[0]); // Add the first column (method name)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Listener for selected method
        calcMethodComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                settingsModel.setCalculationMethod(newValue); // Save to model
            }
        });
        hanafiAsrCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                settingsModel.setHanafiAsrMethod(newValue)
        );
    }

    private void loadSettings() {
        // Load the selected calculation method
        calcMethodComboBox.setValue(settingsModel.getCalculationMethod());
        hanafiAsrCheckBox.setSelected(settingsModel.isHanafiAsrMethod());
    }

    @FXML
    public void saveSettings() {
        // Save settings from the UI to the model
        settingsModel.setCalculationMethod(calcMethodComboBox.getValue());
        settingsModel.setHanafiAsrMethod(!hanafiAsrCheckBox.isSelected());
        if(monthComboBox.getValue() != null && monthComboBox.getValue() != null && yearComboBox.getValue() != null) {
            settingsModel.setMonth(monthComboBox.getValue());
            settingsModel.setYear(yearComboBox.getValue());
            settingsModel.setDay(dayComboBox.getValue());
        }
        System.out.println("settings loaded"+settingsModel.isHanafiAsrMethod());
    }
}

