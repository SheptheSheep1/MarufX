package com.shep.marufx;

import java.io.IOException;
import java.time.LocalDateTime;

public class SharedModel {
    private PrayerTimes prayerTimes;
    private Location location;
    private CalcMethod calcMethod;

    public void setObjPrayerTimes(PrayerTimes prayerTimes){
        this.prayerTimes = prayerTimes;
    }

    public PrayerTimes getObjPrayerTimes(){
        return prayerTimes;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setLocationString(String location){
        try {
            this.location = new Location(location, false);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Location getLocation(){
        return this.location;
    }

    public void setCalcMethod(CalcMethod calcMethod){
        this.calcMethod = calcMethod;
    }

    public CalcMethod getCalcMethod(){
        return calcMethod;
    }
    private int month;
    private int day;
    private int year;
    private String calculationMethod;
    private boolean hanafiAsrMethod;

    // Getters and setters
    public int getMonth() {
        return month;
    }

    public void setMonthDayYear(LocalDateTime localDateTime){
        this.month = localDateTime.toLocalDate().getMonthValue();
        this.day = localDateTime.toLocalDate().getDayOfMonth();
        this.year = localDateTime.toLocalDate().getYear();
    }

    public LocalDateTime getLocalDateTime(){
        return LocalDateTime.of(this.year, this.month, this.day, 12, 0, 0);
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public boolean isHanafiAsrMethod() {
        return hanafiAsrMethod;
    }

    public void setHanafiAsrMethod(boolean hanafiAsrMethod) {
        this.hanafiAsrMethod = hanafiAsrMethod;
    }

}
