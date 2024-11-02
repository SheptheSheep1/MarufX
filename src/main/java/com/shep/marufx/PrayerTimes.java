package com.shep.marufx;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
public class PrayerTimes {
    private int month;
    private int day;
    private int year;
    private double utcOffset;
    private boolean asrHanafi;

    public PrayerTimes(int month, int day, int year, double utcOffset) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.utcOffset = utcOffset;
        this.asrHanafi = false;
    }

    public PrayerTimes(){
        ZonedDateTime currentDateTime = getCurrentDateTime();
        this.month = currentDateTime.getMonthValue();
        this.day = currentDateTime.getDayOfMonth();
        this.year = currentDateTime.getYear();
        this.utcOffset = calcTimezone();
        this.asrHanafi = false;
    }

    private ZonedDateTime getCurrentDateTime(){
        // Get the current date and time with timezone information
        return ZonedDateTime.now();
    }

    private double calcTimezone(){
        // Get the system default timezone
        ZoneId systemZoneId = ZoneId.systemDefault();
        // Get the current time with the system's timezone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(systemZoneId);
        // Get the offset from UTC
        ZoneOffset offset = zonedDateTime.getOffset();

        return offset.getTotalSeconds() / 3600.0;
    }

    public void printDateTime(){
        System.out.printf("Month: %2d | ",this.month);
        System.out.printf("Day: %2d | ",this.day);
        System.out.printf("Year: %4d | ",this.year);
        System.out.printf("UTC Offset: %1.1f", this.utcOffset);
    }


    public static void main (String[] args){
        PrayerTimes prayertimes = new PrayerTimes();
        prayertimes.printDateTime();
        Location location = new Location(true);
        System.err.println(location.getLocationName());
    }
}
