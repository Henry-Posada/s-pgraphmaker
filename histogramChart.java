import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.util.LinkedHashMap;
import java.util.Map;


public class histogramChart extends Application implements ISPGraph{
    private double interval;
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private BarChart<String,Number> histoChart = new BarChart<String,Number>(xAxis,yAxis);
    public ArrayList<Double> testData = new ArrayList(Arrays.asList(1.1,1.1,1.1,3.8,3.8,3.8,3.8,5.1,5.1,7.2,7.2,7.2,8.0,91.0));


    public histogramChart(){}

    public histogramChart(ArrayList<Double> x, double i, String label){
        interval = i;
        this.setGap(0.0);
        this.addSeries(x, label);
        this.setXTicks(x, i);
    }

    public void start(Stage stage){
        //Test using test data.
        this.addSeries(testData, "Testing");
        this.setXTicks(testData, 1);
        this.setGap(0.0);
        Scene scene  = new Scene(histoChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Add a series of data to the histogram. Data must be passed as an Array of Doubles.
     * Passed data will be used to calculate the frequencies, in helper function.
     * 
     * @param x :Array of Doubles to find frequencies of and then add to the histogram
     * @param label :The label of this series of data.
     */
    public void addSeries(ArrayList<Double> x, String label){
        //CODE TO BE DONE
        Map<Double, Integer> freqMap = this.findFrequencies(x);
        XYChart.Series series = new XYChart.Series();
        for(Double e: freqMap.keySet()){
            series.getData().add(new XYChart.Data(Double.toString(e),freqMap.get(e)));
        }
        series.setName(label);
        histoChart.getData().addAll(series);
    }


    /**
     * Helper fucntion. Takes in a set of data and creates a map 
     * that keeps track of the frequencies of each entry. The map is
     * then returned.
     * @param fSet :Set of data that frequencies will be found for.
     * @return : A Map<Double,Integer> object. Double represents the elements
     * from the give set, Integer represents the frequencies of those objects.
     */
    public Map<Double,Integer> findFrequencies(ArrayList<Double> fSet){
        Collections.sort(fSet);
        Map<Double,Integer> freqMap = new LinkedHashMap<>();
        for(Double e: fSet){
            if(!freqMap.containsKey(e)){
                freqMap.put(e,1);
            }
            else if(freqMap.containsKey(e)){
                freqMap.put(e, freqMap.get(e)+1);
            }
        }
        return freqMap;
    }

    /**
     * Sets the histograms ticks on the x-axis to be between the smallest
     * and largest number in the given set. The distance between the 
     * major ticks is defined by the user.
     * 
     * @param tSet :Set of values to get smallest and largest from.
     * @param interval :The distance between major ticks, set by the user.
     */
    public void setXTicks(ArrayList<Double> tSet, double interval){
        Collections.sort(tSet);
        double min = tSet.get(0);
        double max = tSet.get(tSet.size()-1);
        //this.xAxis.setLowerBound(min);
        //this.xAxis.setUpperBound(max);
       // this.xAxis.setTickUnit(interval);
    }


    /**
     * Changes the gap between different categories, bar graphs contain a gap always, set this to 0 to create a histogram.
     * 
     * @param gap: A double representing the gap to leave between bars of different categories.
     */
    public void setGap(Double gap){
        histoChart.setCategoryGap(gap);
        histoChart.setBarGap(gap);
    }



    //GRAPH CUSTOMIZATION METHODS

        /**
     * Set the label of the X-Axis.
     * 
     * 
     * @param label : A String to be set as the X-Axis label.
     */
    public void setXLabel(String label){
        this. xAxis.setLabel(label);
    }

    /**
     * Set the label of the Y-Axis.
     * 
     * 
     * @param label : A String to be set as the Y-Axis label.
     */
    public void setYLabel(String label){
        this.yAxis.setLabel(label);
    }

    /**
     * Set the title of the scatter chart.
     * 
     * @param label : A string to be set as the title of the chart.
     */
    public void setChartTitle(String label){
        histoChart.setTitle(label);
    }
    //Gets the Histogram chart
    public BarChart getChartObj(){return histoChart;}

    
    public static void main(String args[]){
        launch(args);
    }
}
