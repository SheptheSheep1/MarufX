package com.shep.marufx;

import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class MainViewController {
    @FXML
    private Label labell;
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
    protected void onButtonClick(){
        labell.setText("Guy Toubes");
        prayerLabels();
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
            PrayerTimes prayerTimes = new PrayerTimes(location);
            HashMap<String, LocalDateTime> hashmap = prayerTimes.calcPrayerTimes();
            fajrTime.setText(hashmap.get("fajr").toString());
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}
