import java.util.ArrayList;

//Both imports are needed junit testing.
//import static org.junit.Assert.*;
//import org.junit.Test;

//This class contains all static calculations methods. This includes calculations like mean, median, mode, and range.
public class calculations{
    public static void main(String args[]){
       //Initial load of data//TODO: make the file name based on input
       Data testData = new Data("Data");

       ArrayList<Double> modelColumn = testData.getEntireColumn(1);

       System.out.println(modelColumn.get(0) == 1997.0);

    testData.changeCellValue(1, 0, 1555.0);

       System.out.println(modelColumn.get(0) == 1997.0);
       System.out.println(modelColumn.get(0) == 1555.0);

       testData.exportCSV("firstSet");

       testData.appendRecordToDataFile(1);

       testData.exportCSV("secondSet");
    }

    /** 
     * 
     * @param x : An array of doubles that the user wants to find the mean of.
     * @return : Returns the calculated mean of the passed array x.
     * 
     */
    public static double mean(double x[]){
        //Variable mean, to be used by adding all values of x and then dividing by the length of x.
        double mean = 0;
        for(double a:x){
            mean+=a;
        }
        //Dividing by the length of x after calculating sum of all x values.
        mean = mean/x.length;
        //Rounding the mean to two decimal places.
        mean = Math.round(mean*100.0)/100.0;
        return mean;
    }
    
    /** Testing for mean method. If using IDE like VSCode be sure to have junit-platform-console-standalone.jar file somewhere on the machine you are running this on.
     * Additionally have settings.json configured correctly.
     */
    //@Test
        public void meanTest(){
            double test[] = {2,4,5,6,7,10};
          //  assertEquals(5.67, mean(test), 0.001);
        }
        
}

