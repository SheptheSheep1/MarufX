package com.shep.marufx;

import java.util.ArrayList;

public class CalcMethodMain {
    private ArrayList<CalcMethod> calcMethods;
    public CalcMethodMain(){
        calcMethods = new ArrayList<>();
        defaultCalcMethods();
    }

    private void defaultCalcMethods(){
        calcMethods.add(new CalcMethod("MWL", 18.0, 17.0, false, "Muslim World League, Used in Europe, Far East, parts of US"));
        calcMethods.add(new CalcMethod("ISNA", 15.0, 15.0, false, "Islamic Society of North America, Used in North America (US and Canada)"));
        calcMethods.add(new CalcMethod("Egypt", 19.5, 17.5, false, "Egyptian General Authority Survey, Used in Africa, Syria, Lebanon, Malaysia"));
        calcMethods.add(new CalcMethod("Makkah", 18.5, 0.0, true, "Umm al-Qura University, Makkah, Used in the Arabian Peninsula"));
        calcMethods.add(new CalcMethod("Karachi", 18.0, 18.0, false, "University of Islamic Sciences, Karachi, Used in Pakistan, Afghanistan, Bangladesh, and India"));
        calcMethods.add(new CalcMethod("Tehran", 17.7, 14.0, false, "Institute of Geophysics, University of Tehran, Used in Iran and some Shia communities"));
        calcMethods.add(new CalcMethod("Jafari", 16.0, 14.0, false, "Used in some Shia communities worldwide"));
    }

    public static void main(String[] args){
        CalcMethodMain calcMethodMain = new CalcMethodMain();
        
    }
}
