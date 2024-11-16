package com.shep.marufx;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;

public class PrayerTimes {
    private int month;
    private int day;
    private int year;
    private double utcOffset;
    private boolean asrHanafi;
    private Location location;
    private CalcMethod calcMethod;

    public PrayerTimes(int month, int day, int year, double utcOffset, Location location, CalcMethod calcMethod) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.utcOffset = utcOffset;
        this.asrHanafi = false;
        this.location = location;
        this.calcMethod = calcMethod;
    }

    public PrayerTimes(int month, int day, int year, Location location, CalcMethod calcMethod) {
        this.asrHanafi = false;
        this.month = month;
        this.day = day;
        this.year = year;
        this.location = location;
        this.calcMethod = calcMethod;
    }

    public PrayerTimes(Location location){
        ZonedDateTime currentDateTime = getCurrentDateTime();
        this.month = currentDateTime.getMonthValue();
        this.day = currentDateTime.getDayOfMonth();
        this.year = currentDateTime.getYear();
        this.utcOffset = calcTimezone();
        this.asrHanafi = false;
        this.calcMethod = new CalcMethod();
        this.location = location;
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

    public static double[] calcSunDecl(double julianDay){
        // return array of doubles at fixed size 2 containing T (fraction of Earth's orbit cycle in rad since epoch) and
        // DELTA (precise sun declination angle) at indices 0 and 1 respectively
        double T = (2.0 * Math.PI * (julianDay - 2451545.0)) / 365.25;
        double DELTA = 0.37877 + (23.264 * Math.sin(Math.toRadians((57.297*T) - 79.547))) + (0.3812 * Math.sin(Math.toRadians((2*57.297*T) - 82.682))) + (0.17132 * Math.sin(Math.toRadians((3*57.297*T) - 59.722)));
        return new double[]{T, DELTA};
    }

    public static double calcEqTime(double julianDays){
        // takes julian days as parameter and returns equation of time at that parameter
        double U = (julianDays - 2451545.0) / 36525;
        double L0 = 280.46607 + 36000.7698 * U;
        double ET1000 = -(1789.0 + 237.0*U) * Math.sin(Math.toRadians(L0)) - (7146.0 - 62.0*U) * Math.cos(Math.toRadians(L0)) + (9934.0 - 14.0*U) * Math.sin(Math.toRadians(2.0*L0)) - (29.0 + 5.0*U) * Math.cos(Math.toRadians(2.0*L0)) + (74.0 + 10.0*U) * Math.sin(Math.toRadians(3.0*L0)) + (320.0 - 4.0*U) * Math.cos(Math.toRadians(3.0*L0)) - 212.0*Math.sin(Math.toRadians(4.0*L0));
        //System.out.printf("\nU: %f\nL0: %f\nET1000: %f\n", U, L0, ET1000);
        return ET1000 / 1000.0;
    }

    public static double calcSunTT(double utcOffset, double longitude, double eqTime){
        // takes UTC Offset, longitude, and Equation of Time and returns Sun Transit Time
        return (12.0 + utcOffset - (longitude / 15.0) - (eqTime / 60.0));
    }

    public static HashMap<String, Double> calcSunAltitudes(double fajrAngle, double ishaAngle, int elevation, boolean asrHanafi, double sunDelta, double latitude){
        HashMap<String, Double> sunAltitudes = new HashMap<>();

        int asrMethod = 1;
        if(asrHanafi){asrMethod = 2;}

        double saFajr = -(fajrAngle);
        double saSunrise = -0.8333 - (0.0347 * Math.sqrt((double)elevation));
        double saIsha  = -(ishaAngle);

        sunAltitudes.put("fajr", saFajr);
        sunAltitudes.put("sunrise", saSunrise);
        sunAltitudes.put("isha", saIsha);

        //System.out.println(sunAltitudes);

        return sunAltitudes;
    }

    public static HashMap<String, Double> calcHA(HashMap<String, Double> sunAltitudes, double latitude, double sunDecl){
        // takes sunAltitudes, latitude, and sun declination angle and returns hashmap containing relevant prayers
        HashMap<String, Double> hourAngles = new HashMap<>();

        double v = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(sunDecl));
        double v1 = Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(sunDecl));
        double cos_HA_fajr = (Math.sin(Math.toRadians(sunAltitudes.get("fajr"))) - v) / v1;
        double cos_HA_maghrib = (Math.sin(Math.toRadians(sunAltitudes.get("sunrise")))) - v / v1;
        double cos_HA_isha = (Math.sin(Math.toRadians(sunAltitudes.get("isha"))) - v) / v1;

        double haFajr = Math.toDegrees(Math.acos(cos_HA_fajr));
        double haMaghrib = Math.toDegrees(Math.acos(cos_HA_maghrib));
        double haIsha = Math.toDegrees(Math.acos(cos_HA_isha));

        hourAngles.put("fajr", haFajr);
        hourAngles.put("sunrise", haMaghrib);
        hourAngles.put("maghrib", haMaghrib);
        hourAngles.put("isha", haIsha);

        //System.out.println(hourAngles);

        return hourAngles;
    }

    public static double calcAsrDiff(double julianDays, double latitude, boolean asrHanafi, double sunDelta){
        // takes julian days and latitude as parameters and returns asr difference (from solar noon)
        // the asr difference has its own method because the way it is calculated is different from the rest and more complex
        // Derived from praytimes.org documentation and US Naval Observatory Sun Approx docs

        // Dominant hanafi opinion compensation (shadow length * 2)
        int asrMethod = 1;
        if(asrHanafi){asrMethod = 2;}

        // time from epoch (j2000)
        double d = julianDays - 2451545.0;
        // mean anomaly of sun in deg
        //double g = 357.529 + 0.98560028*d;
        // mean longitude of sun in deg
        //double q = 280.459 + 0.98564736*d;
        // apparent ecliptic longitude of sun in deg (takes into account earth orbit eccentricity and axis tilt)
        //double L = q + 1.915*Math.sin(Math.toRadians(g)) + 0.020 * Math.sin(Math.toRadians(2.0*g));
        // obliquity of ecliptic in deg
        //double e = 23.439 - 0.00000036*d;
        // declination of Sun (may be redundant)
        //double D = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(e)) * Math.sin(Math.toRadians(L))));
        //double D = -18.95;
        //double[] uyt = calcSunDecl(julianDays);
        //double D = uyt[1];
        //System.out.println("D: " + D);
        double D = sunDelta;

        // (from praytimes: ) The following formula computes the time difference between the mid-day and the time at which the object's shadow equals t times the length of the object itself plus the length of that object's shadow at noon:
        double top = Math.sin(Math.toRadians(Math.toDegrees(arccot(2.0+Math.tan(Math.toRadians(latitude - D)))) - Math.toDegrees((Math.sin(Math.toRadians(latitude))) * Math.sin(Math.toRadians(D)))));
        double bottom = Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(D));

        return (1.0/15.0) * Math.toDegrees(Math.acos(top/bottom));
    }

    public HashMap<String, LocalDateTime> calcPrayerTimes(){
        HashMap<String, LocalDateTime> prayerTimes = new HashMap<>();

        double julianDays = calcJD(this.year, this.month, this.day);
        double sunDecl = calcSunDecl(julianDays)[1];
        double eqTime = calcEqTime(julianDays);
        double sunTT = calcSunTT(this.utcOffset, this.location.getLongitude(), eqTime);
        HashMap<String, Double> sunAltitudes = calcSunAltitudes(this.calcMethod.getFajrAngle(), this.calcMethod.getIshaAngle(), 0, this.asrHanafi, sunDecl, this.location.getLatitude());
        HashMap<String, Double> hourAngles = calcHA(sunAltitudes, this.location.getLatitude(), sunDecl);

        double fajr = sunTT - (hourAngles.get("fajr") / 15.0);
        double sunrise = sunTT - hourAngles.get("sunrise") / 15.0;
        double dhuhr = sunTT + 2.0/60.0;
        double asr = dhuhr + calcAsrDiff(julianDays, location.getLatitude(), this.asrHanafi, sunDecl);
        double maghrib = sunTT + (hourAngles.get("maghrib") / 15.0);
        double isha = sunTT + hourAngles.get("isha") / 15.0;

        prayerTimes.put("fajr", convertHrs(fajr));
        prayerTimes.put("sunrise", convertHrs(sunrise));
        prayerTimes.put("dhuhr", convertHrs(dhuhr));
        prayerTimes.put("asr", convertHrs(asr));
        prayerTimes.put("maghrib", convertHrs(maghrib));
        prayerTimes.put("isha", convertHrs(isha));

        return prayerTimes;
    }

    public void printDateTime(){
        System.out.printf("\nMonth: %2d | ",this.month);
        System.out.printf("Day: %2d | ",this.day);
        System.out.printf("Year: %4d | ",this.year);
        System.out.printf("UTC Offset: %2.1f\n", this.utcOffset);
    }

    private static double arccot(double x){
        return Math.PI / 2.0 - Math.atan(x);
    }

    private LocalDateTime convertHrs(double decimalHours){
        // takes decimal hours and returns a LocalDateTime object representing that time on the day the object is in

        int hours = (int) decimalHours;
        double remainingMinutes = (decimalHours - hours) * 60;
        int minutes = (int) remainingMinutes;
        double remainingSeconds = (remainingMinutes - minutes) * 60;
        int seconds = (int) Math.round(remainingSeconds);

        return LocalDateTime.of(this.year, this.month, this.day, hours, minutes, seconds);
    }

    public static void main (String[] args){
        //PrayerTimes prayertimes = new PrayerTimes();
        //prayertimes.printDateTime();

        double julianDays = PrayerTimes.calcJD(2024, 11, 15.886111);
        System.out.println("Julian Days: "+julianDays);
        double[] sunDecl = calcSunDecl(julianDays);
        System.out.println(Arrays.toString(sunDecl));
        double eqTime = PrayerTimes.calcEqTime(julianDays);
        System.out.println("Equation of Time: " + eqTime);
        double sunTT = calcSunTT(julianDays, -111.940016, eqTime);
        System.out.println("Transit Time: " + sunTT);
        HashMap<String, Double> sunAltitudes = PrayerTimes.calcSunAltitudes(15.0, 15,0, true, sunDecl[1], 33.4255117);
        System.out.println(sunAltitudes);
        HashMap<String, Double> hourAngles = PrayerTimes.calcHA(sunAltitudes, 33.4255117, sunDecl[1]);
        System.out.println(hourAngles);
        double asrDiff = PrayerTimes.calcAsrDiff(julianDays, 33.4255117, true, sunDecl[1]);
        System.out.println("Asr Differences: "+asrDiff);

        try {
            Location location = new Location(true);
            System.out.println(location.getLocationName());
            PrayerTimes prayerTimes = new PrayerTimes(location);
            HashMap<String, LocalDateTime> prayTimes = prayerTimes.calcPrayerTimes();
            System.out.println(prayTimes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
