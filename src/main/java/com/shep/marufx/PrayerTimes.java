package com.shep.marufx;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class PrayerTimes {
    private int month;
    private int day;
    private int year;
    private double utcOffset;
    private boolean asrHanafi;
    private Location location;

    public PrayerTimes(int month, int day, int year, double utcOffset, Location location) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.utcOffset = utcOffset;
        this.asrHanafi = false;
        this.location = location;
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

    public static double calcJD(int year, int month, double day){
        /*

        Adapted from Python function.

        Convert a date to Julian Day.

        Algorithm from 'Practical Astronomy with your Calculator or Spreadsheet',
            4th ed., Duffet-Smith and Zwart, 2011.

        Parameters
        ----------
        year : int
            Year as integer. Years preceding 1 A.D. should be 0 or negative.
            The year before 1 A.D. is 0, 10 B.C. is year -9.

        month : int
            Month as integer, Jan = 1, Feb. = 2, etc.

        day : float
            Day, may contain fractional part.

        Returns
        -------
        jd : float
            Julian Day

        Examples
        --------
        Convert 6 a.m., February 17, 1985 to Julian Day

        date_to_jd(1985,2,17.25)
        2446113.75

         */
        int yearp;
        int monthp;
        double A, B, C, D;

        if(month == 1 || month == 12) {
            yearp = year - 1;
            monthp = month + 12;
        }else{
            yearp = year;
            monthp = month;
        }
        if((yearp < 1582) || (year == 1582 && month < 10) || (year == 1582 && month == 10 && day < 15.0)) {
            B = 0.0;
        }else{
            A = (int)((double)yearp/100.0);
            B = 2.0 - A + (int)(A/4.0);
        }
        if(yearp < 0) {
            C = (int) ((365.25 * (double) yearp) - 0.75);
        }else{
            C = (int)(365.25*(double)yearp);
        }
        D = (int)(30.6001 * (monthp + 1));
        //System.out.println("ran");
        return (B + C + D + day + 1720994.5);
    }

    private static double[] calcSunDecl(double julianDay){
        // return array of doubles at fixed size 2 containing T (fraction of Earth's orbit cycle in rad since epoch) and
        // DELTA (sun declination) at indices 0 and 1 respectively
        double T = (2.0 * Math.PI * (julianDay - 2451545.0)) / 365.25;
        double DELTA = 0.37877 + (23.264 * Math.sin(Math.toRadians((57.297*T) - 79.547))) + (0.3812 * Math.sin(Math.toRadians((2*57.297*T) - 82.682))) + (0.17132 * Math.sin(Math.toRadians((3*57.297*T) - 59.722)));
        return new double[]{T, DELTA};
    }

    public void printDateTime(){
        System.out.printf("\nMonth: %2d | ",this.month);
        System.out.printf("Day: %2d | ",this.day);
        System.out.printf("Year: %4d | ",this.year);
        System.out.printf("UTC Offset: %2.1f\n", this.utcOffset);
    }


    public static void main (String[] args){
        PrayerTimes prayertimes = new PrayerTimes();
        prayertimes.printDateTime();
        try {
            Location location = new Location(true);
            System.out.println(location.getLocationName());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(PrayerTimes.calcJD(2024, 11, 15.024306));
    }
}
