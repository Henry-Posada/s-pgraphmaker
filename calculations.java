//Both imports are needed junit testing.
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//This class contains all static calculations methods. This includes calculations like mean, median, mode, and range.
public class calculations{
    public static void main(String args[]){
       
    }

    /** 
     * Returns a double representing the mean of the passed ArrayList<Double> object. The method will iterate through the ArrayList with a foreach method
     *  adding all elements in the ArrayList to a double variable mean where it is then divided by the number of elements in the ArrayList.
     * 
     * @param findMeanOf : An ArrayList<Double> of doubles that the user wants to find the mean of.
     * @return : Returns the calculated mean of the passed ArrayList<Double> findMeanOf.
     * 
     */
    public static double findMean(ArrayList<Double> findMeanOf){
        //Variable mean, to be used by adding all values of findMeanOf and then dividing by the length of findMeanOf.
        double mean = 0;
        for(Double e : findMeanOf){
            mean+=e;
        }
        //Dividing by the length of findMeanOf after calculating sum of all findMeanOf values.
        mean = mean/findMeanOf.size();
        //Rounding the mean to two decimal places.
        mean = Math.round(mean*100.0)/100.0;
        return mean;
    }


    /**
     * Returns a double representing the median of a passed ArrayList<Double> object. The method will return the middle element of the sorted ArrayList
     *  if the list is of odd length. The method will return the average of the middle two elements if the ArrayList is of even length.
     * 
     * @param findMedianOf : An ArrayList<Double> of doubles that the user wants to find the median of.
     * @return : Returns the calculated median of the passed ArrayList<Double> findMedianOf.
     * 
     */

    public static double findMedian(ArrayList<Double> findMedianOf){
        Collections.sort(findMedianOf);
        int size = findMedianOf.size();
        if(size %2 == 0)
            return (findMedianOf.get(size/2) + findMedianOf.get((size/2)-1))/2;
        else 
            return findMedianOf.get(size/2);
    }

    /**
     * Returns a double representing the mode of passed ArrayList<Double> object. The method sorts the ArrayList passed to it,
     *  and calculates the frequency of each element. That frequency is then stored as the index it was found at and only changed 
     *  once a new element with higher frequency is found.
     * 
     * @param findModeOf : An ArrayList<Dobule> of doubles that the user wants to find the mode of.
     * @return : Returns the calculated mode of the paseed ArrayList<Double> findModeOf.
     * 
     */
    public static double findMode(ArrayList<Double> findModeOf){
        Collections.sort(findModeOf);
        int[] freq = new int[]{};
        for(Double e: findModeOf){
            freq[freq.length] = Collections.frequency(findModeOf, e);
        }

        return 0.0;
    }

    /**
     * Returns an ArrayList<Double> containing two elements, the lower range of the original ArrayList<Double> passed in and
     *  the upper range of the ArrayList<Double> passed in. The ArrayList is first sorted placing the lower bound at index 0 and
     *  the upper at index n, the size of the original ArrayList.
     * 
     * @param findRangeOf : An ArrayList<Double> of doubles that the user wants to find the range of.
     * @return : Returns an ArrayList<Double> with two elements, index 0 contains the lower range and index 1 contains the upper range.
     */
    public static ArrayList<Double> findRange(ArrayList<Double> findRangeOf){
        Collections.sort(findRangeOf);
        ArrayList<Double> range = new ArrayList<Double>();
        range.add(findRangeOf.get(0));
        range.add(findRangeOf.get(findRangeOf.size()));
        return range;
    }
    
    /** 
    //##############NOTE###################//
    //Testing for mean method. If using IDE like VSCode be sure to have junit-platform-console-standalone.jar file somewhere on the machine you are running this on.
        Additionally have settings.json configured correctly.
    //This test has not fully been completed and method not fully tested.    
    //#####################################//
    */
    @Test
        public void findMeanTest(){
            ArrayList<Double> testMean1 = new ArrayList<Double>(Arrays.asList(2.0,4.0,5.0,6.0,7.0,10.0));
            assertEquals(5.67, findMean(testMean1), 0.001);
        }
    
    //##############NOTE###################//
    //This test has not fully been completed and method not fully tested.
    //#####################################//
    @Test
        public void findMedianTest(){
            ArrayList<Double> testMedian1 = new ArrayList<Double>(Arrays.asList(4.0,1.0,2.0,7.0,6.0,8.0,3.0,5.0,10.0,9.0,11.0));
            ArrayList<Double> testMedian2 = new ArrayList<Double>(Arrays.asList(4.0,1.0,2.0,7.0,6.0,8.0,3.0,5.0,10.0,9.0));

            assertEquals(6.0, findMedian(testMedian1), 0.001);
            assertEquals(5.5, findMedian(testMedian2), 0.001);
        }
    
    //##############NOTE###################//
    //This test has not fully been completed and method not fully tested.
    //#####################################//
    @Test
        public void findModeTest(){}
    

    //##############NOTE###################//
    //This test has not fully been completed and method not fully tested.
    //#####################################//
    @Test
        public void findRangeTest(){
            ArrayList<Double> testRange1 = new ArrayList<Double>(Arrays.asList(0.0));
            ArrayList<Double> testRange2 = new ArrayList<Double>(Arrays.asList());
            ArrayList<Double> testRange3 = new ArrayList<Double>(Arrays.asList(0.0));

            ArrayList<Double> expectedRange1 = new ArrayList<Double>(Arrays.asList(0.0));
            ArrayList<Double> expectedRange2 = new ArrayList<Double>(Arrays.asList());
            ArrayList<Double> expectedRange3 = new ArrayList<Double>(Arrays.asList(0.0));

            assertEquals(expectedRange1, testRange1);
            assertEquals(expectedRange2, testRange2);
            assertEquals(expectedRange3, testRange3);
        }    
}




