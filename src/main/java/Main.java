//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception{
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String line = "";
        ArrayList<CalcMethod> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./src/main/resources/calc_test.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] calcArray = line.split(",");
            arrayList.add(new CalcMethod(calcArray));
        }
        /*
        for(String[] array : arrayList){
            for(String string : array){
                System.out.print(string);
                System.out.print(", ");
            }
            System.out.println();
        }
        */
        for(CalcMethod calcMethod : arrayList){
            System.out.println(calcMethod);
        }
        //System.out.println(arrayList);
        //System.out.println(new File(".").getAbsoluteFile());

    }
}