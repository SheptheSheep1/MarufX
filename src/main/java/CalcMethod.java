import java.util.ArrayList;

class CalcMethod {
    private String name;
    private double fajrAngle;
    private double ishaAngle;
    private boolean fixed;
    private String description;
    public CalcMethod(String name, double fajrAngle, double ishaAngle, boolean fixed, String description) {
        this.name = name;
        this.fajrAngle = fajrAngle;
        this.ishaAngle = ishaAngle;
        this.fixed = fixed;
        this.description = description;
    }/*
    public CalcMethod() {
        this.name = "MWL";
        this.fajrAngle = 18.0;
        this.ishaAngle = 17.0;
        this.fixed = false;
        this.description = "Muslim World League";
    }*/

    public String toString(){
        return String.format("%s Fajr Angle: %f Isha Angle: %f", description, fajrAngle, ishaAngle);
    }

}