package com.shep.marufx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;

public class MainViewController {
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
    private Label fajrTime;

    @FXML
    private Label sunriseTime;

    @FXML
    private Label dhuhrTime;

    @FXML
    private Label asrTime;

    @FXML
    private Label maghribTime;

    @FXML
    private Label ishaTime;

    private LocalDate currentDate;

    private PrayerTimes prayerTimes;

    private Location location;

    private CalcMethod calcMethod;

    @FXML
    private Label dateLabel;

    @FXML
    protected void onButtonClick(){
        //prayerLabels();
        setPrayers();
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
        dateLabel.setText(currentDate.format(dateFormatter));
    }
    // Check for a day change and update the date label
    private void checkForDayChange() {
        LocalDate newDate = LocalDate.now();
        if (!newDate.equals(currentDate)) {
            currentDate = newDate;
            updateDateLabel();
        }
    }
    private void updatePrayers(){
        fajr.setText(prayerTimes.getFajr());
        sunrise.setText(prayerTimes.getSunrise());
        dhuhr.setText(prayerTimes.getDhuhr());
        asr.setText(prayerTimes.getAsr());
        maghrib.setText(prayerTimes.getMaghrib());
        isha.setText(prayerTimes.getIsha());
    }
    @FXML
    public void initialize() {
        try {
            currentDate = LocalDate.now();
            location = new Location(false);
            calcMethod = new CalcMethod();
            prayerTimes = new PrayerTimes(currentDate.getMonthValue(), currentDate.getDayOfMonth(), currentDate.getYear(), PrayerTimes.calcTimezone(), location, calcMethod,  true, 0.0);
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
        catch(IOException e){
            System.out.println(e);
        }
    }
}
