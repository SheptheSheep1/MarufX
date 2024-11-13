package com.shep.marufx;

import javafx.scene.control.Label;
import javafx.fxml.FXML;

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
    protected void onButtonClick(){
        labell.setText("Guy Toubes");
        prayerLabels();
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


}
