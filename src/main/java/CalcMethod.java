import java.util.ArrayList;

public class CalcMethod {
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
    }
    public CalcMethod(String[] stringArray){
        this.name = stringArray[1];
        this.description = stringArray[0];
        this.fajrAngle = Double.parseDouble(stringArray[2]);
        this.ishaAngle = Double.parseDouble(stringArray[3]);
        if(stringArray[4].equalsIgnoreCase("Y")){this.fixed = true;}
        else if (stringArray[4].equalsIgnoreCase("N")){this.fixed = false;}
    }
    public CalcMethod() {
        this.name = "MWL";
        this.fajrAngle = 18.0;
        this.ishaAngle = 17.0;
        this.fixed = false;
        this.description = "Muslim World League";
    }

    public String toString(){
        return String.format("%s, Fajr Angle: %.2f, Isha Angle: %.2f", description, fajrAngle, ishaAngle);
    }

}