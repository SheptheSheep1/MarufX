package com.shep.marufx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;

public class MainViewController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == mainScene) { // Check if the new scene is the main scene
                onSceneActivated();
            }
        });
    }
    private SharedModel sharedModel;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss a");

    @FXML
    private Label timeLabel;
    @FXML
    private Label fajr;
    @FXML
    private Label sunrise;
    @FXML
    private Label dhuhr;
    @FXML
    private Label asr;
    @FXML
    private Label maghrib;
    @FXML
    private Label isha;

    @FXML
    private Label yFajr;
    @FXML
    private Label ySunrise;
    @FXML
    private Label yDhuhr;
    @FXML
    private Label yAsr;
    @FXML
    private Label yMaghrib;
    @FXML
    private Label yIsha;

    @FXML
    private Label tFajr;
    @FXML
    private Label tSunrise;
    @FXML
    private Label tDhuhr;
    @FXML
    private Label tAsr;
    @FXML
    private Label tMaghrib;
    @FXML
    private Label tIsha;


    public LocalDate currentDate;

    public PrayerTimes prayerTimes;

    public PrayerTimes yprayerTimes;
    public PrayerTimes tprayerTimes;

    private Location location;

    public CalcMethod calcMethod;

    @FXML
    private Label dateLabel;
    @FXML
    private Label ydateLabel;
    @FXML
    private Label tdateLabel;

    private Scene mainScene;
    private Scene settingsScene;

    @FXML
    protected void onButtonClick(){
        //prayerLabels();
        //setPrayers();
        refreshView();
    }

    @FXML
    protected void prayerLabels(){
        fajr.setText("Fajr");
        sunrise.setText("Sunrise");
        dhuhr.setText("Dhuhr");
        asr.setText("Asr");
        maghrib.setText("Maghrib");
        isha.setText("Isha");
    }

    @FXML
    protected void setPrayers(){
        try{
            Location location = new Location(false);
            PrayerTimes prayerTimes = new PrayerTimes(location, 0.0);
            HashMap<String, LocalDateTime> hashmap = prayerTimes.calcPrayerTimes();
            fajr.setText(prayerTimes.getFajr());
            sunrise.setText(prayerTimes.getSunrise());
            dhuhr.setText(prayerTimes.getDhuhr());
            asr.setText(prayerTimes.getAsr());
            maghrib.setText(prayerTimes.getMaghrib());
            isha.setText(prayerTimes.getIsha());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void updateTime() {
        // Get the current time and update the label
        String currentTime = LocalTime.now().format(TIME_FORMATTER);
        timeLabel.setText(currentTime);
    }
    private void updateDateLabel() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter dateFormatter1 = DateTimeFormatter.ofPattern("MMMM dd");
        dateLabel.setText(sharedModel.getLocalDateTime().format(dateFormatter));
        ydateLabel.setText(sharedModel.getLocalDateTime().minusDays(1).format(dateFormatter1));
        tdateLabel.setText(sharedModel.getLocalDateTime().plusDays(1).format(dateFormatter1));
    }
    // Check for a day change and update the date label
    private void checkForDayChange() {
        LocalDate newDate = LocalDate.now();
        if (!newDate.equals(currentDate)) {
            currentDate = newDate;
            //updateDateLabel();
        }
        refreshView();
    }
    private void updatePrayers(){
        fajr.setText(prayerTimes.getFajr());
        sunrise.setText(prayerTimes.getSunrise());
        dhuhr.setText(prayerTimes.getDhuhr());
        asr.setText(prayerTimes.getAsr());
        maghrib.setText(prayerTimes.getMaghrib());
        isha.setText(prayerTimes.getIsha());

        yFajr.setText(yprayerTimes.getFajr());
        ySunrise.setText(yprayerTimes.getSunrise());
        yDhuhr.setText(yprayerTimes.getDhuhr());
        yAsr.setText(yprayerTimes.getAsr());
        yMaghrib.setText(yprayerTimes.getMaghrib());
        yIsha.setText(yprayerTimes.getIsha());

        tFajr.setText(tprayerTimes.getFajr());
        tSunrise.setText(tprayerTimes.getSunrise());
        tDhuhr.setText(tprayerTimes.getDhuhr());
        tAsr.setText(tprayerTimes.getAsr());
        tMaghrib.setText(tprayerTimes.getMaghrib());
        tIsha.setText(tprayerTimes.getIsha());
    }
    @FXML
    public void initialize() {
        currentDate = LocalDate.now();
        prayerTimes = new PrayerTimes(currentDate.getMonthValue(), currentDate.getDayOfMonth(), currentDate.getYear(), PrayerTimes.calcTimezone(), this.sharedModel.getLocation(), sharedModel.getCalcMethod(),  true, 0.0);
        yprayerTimes = new PrayerTimes(currentDate.getMonthValue(), currentDate.getDayOfMonth() -1, currentDate.getYear(), PrayerTimes.calcTimezone(), this.sharedModel.getLocation(), sharedModel.getCalcMethod(),  true, 0.0);
        tprayerTimes = new PrayerTimes(currentDate.getMonthValue(), currentDate.getDayOfMonth(), currentDate.getYear(), PrayerTimes.calcTimezone(), this.sharedModel.getLocation(), sharedModel.getCalcMethod(),  true, 0.0);

                // Update the label with the current time initially
        updateTime();
        updateDateLabel();
        updatePrayers();

        // Create a Timeline to update the time every second
        Timeline timeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTime())
        );
        // Timeline for checking day change every minute
        Timeline dateTimeline = new Timeline(
                new KeyFrame(Duration.minutes(1), event -> checkForDayChange())
        );
        timeTimeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeTimeline.play(); // Start the timeTimeline
        dateTimeline.setCycleCount(Timeline.INDEFINITE);
        dateTimeline.play();
    }

    public void setSettingsScene(Scene scene){
        this.settingsScene = scene;
    }

    public void setSharedModel(SharedModel sharedModel){
        System.out.println("Controller set");
        this.sharedModel = sharedModel;
    }
    public void onSceneActivated() {
        System.out.println("Main Scene activated");
        refreshView();
    }
    private void refreshView() {
        this.prayerTimes = new PrayerTimes(sharedModel.getMonth(), sharedModel.getDay(), sharedModel.getYear(), PrayerTimes.calcTimezone(), this.sharedModel.getLocation(), sharedModel.getCalcMethod(),  this.sharedModel.isHanafiAsrMethod(), 0.0);
        LocalDateTime yesterday = sharedModel.getLocalDateTime().minusDays(1);
        LocalDateTime tomorrow = sharedModel.getLocalDateTime().plusDays(1);
        this.yprayerTimes = new PrayerTimes(yesterday.getMonthValue(), yesterday.getDayOfMonth(), yesterday.getYear(), PrayerTimes.calcTimezone(), this.sharedModel.getLocation(), sharedModel.getCalcMethod(), this.sharedModel.isHanafiAsrMethod(), 0.0);
        this.tprayerTimes = new PrayerTimes(tomorrow.getMonthValue(), tomorrow.getDayOfMonth(), tomorrow.getYear(), PrayerTimes.calcTimezone(), this.sharedModel.getLocation(), sharedModel.getCalcMethod(),  this.sharedModel.isHanafiAsrMethod(), 0.0);
        System.out.println("Main Scene refresh view");
        System.out.println(sharedModel.isHanafiAsrMethod());
        updatePrayers();
        updateDateLabel();
    }
    @FXML
    public void goToSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings_view.fxml"));
            Scene settingsScene = new Scene(loader.load());

            // Pass the stage to the settings controller
            SettingsViewController controller = loader.getController();
            controller.setSettingsModel(sharedModel);
            controller.setStage(stage);

            // Switch to the settings scene
            stage.setScene(settingsScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSettingsButton(ActionEvent event) {
        // Switch to the settings scene
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(settingsScene);
    }
}
