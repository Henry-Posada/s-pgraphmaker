import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;


public class histogramChart {
    private double interval;
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private BarChart<Number,Number> bChart = new BarChart<Number,Number>(xAxis,yAxis);




    public histogramChart(ArrayList<Double> x, double i, String label){
        interval = i;
        this.setGap(0.0);
        ArrayList<Double> frequencies = this.findFrequencies(x);
        ArrayList<Double> ticks = this.createHistoTicks(x, i);
        this.addSeries(ticks, frequencies, label);
    }

    public void addSeries(ArrayList<Double> x, ArrayList<Double> y, String label){
        //CODE TO BE DONE
    }


        /**
     * Changes the gap between different categories, bar graphs contain a gap always, set this to 0 to create a histogram.
     * 
     * @param gap: A double representing the gap to leave between bars of different categories.
     */
    public void setGap(Double gap){
        bChart.setCategoryGap(gap);
        bChart.setBarGap(gap);
    }

    public ArrayList<Double> findFrequencies(ArrayList<Double> fSet){
        //CODE TO BE DONE 
        //RETURN VALUE ADDED TO NOT CAUSE ERRORS
        return new ArrayList<Double>();
    }

    public ArrayList<Double> createHistoTicks(ArrayList<Double> tSet, double interval){
        //CODE TO BE DONE 
        //RETURN VALUE ADDED TO NOT CAUSE ERRORS
        return new ArrayList<Double>();
    }
}
