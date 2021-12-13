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


public class barChart extends Application implements ISPGraph{ 
    
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private BarChart<String,Number> bChart = new BarChart<String,Number>(xAxis,yAxis);
    ArrayList<String> x1 = new ArrayList<String>(Arrays.asList("Henry","Scott"));
    ArrayList<Double> y1 = new ArrayList<Double>(Arrays.asList(5.0,10.0));

    ArrayList<String> x2 = new ArrayList<String>(Arrays.asList("Henry","Scott"));
    ArrayList<Double> y2 = new ArrayList<Double>(Arrays.asList(15.0,26.0));

    public barChart(){

    }

    public barChart(ArrayList<String> x, ArrayList<Double> y, String label){
        this.addSeries(x, y, label);
    }
 
    public barChart(ArrayList<Double> y, ArrayList<String> labels){
        //TODO: If only a Y series is given, X axis will be 1, 2, 3, etc.
        this.addSeries(y, labels);
    }
 

    public void start(Stage stage){
        //some test data for barchart
        this.addSeries(x1,y1,"Test1");
        this.addSeries(x2,y2,"Test2");
        Scene scene  = new Scene(bChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    

    /**
     * Set the bounds of the Y-Axis and the amount of units between each tick.
     * 
     * 
     * @param lowerBound : A double representing the lower bound.
     * @param upperBound : A double representing the upper bound.
     * @param tickUnit : A double representing the amount of units between each tick.
     */
    public void setYAxis(double lowerBound, double upperBound, double tickUnit){
        this.yAxis.setAutoRanging(false);
        this.yAxis.setLowerBound(lowerBound);
        this.yAxis.setUpperBound(upperBound);
        this.yAxis.setTickUnit(tickUnit);
    }

    /**
     * Set the label of the X-Axis.
     * 
     * 
     * @param label : A String to be set as the X-Axis label.
     */
    public void setXLabel(String label){
        xAxis.setLabel(label);
    }

    /**
     * Set the label of the Y-Axis.
     * 
     * 
     * @param label : A String to be set as the Y-Axis label.
     */
    public void setYLabel(String label){
        yAxis.setLabel(label);
    }

    /**
     * Set the title of the line chart.
     * 
     * @param label : A string to be set as the title of the chart.
     */
    public void setChartTitle(String label){
        bChart.setTitle(label);
    }

    /**
     * Add a series of data to the bar chart. Data must be passed as an Array of doubles, and two arrays must be passed.
     * Ensure the that the data based is related to each other, the X-Axis of the data and the Y-Axis of the Data.
     * 
     * @param x : An Array of Strings to be added to the chart, should be the X-Axis of the data.
     * @param y : An Array of doubles to be added to the chart, should be the Y-Axis of the data.
     * @param label : A string to be set as the title of the series.
     */
    public void addSeries(ArrayList<String> x, ArrayList<Double> y, String label){
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i<x.size(); i++){
            series.getData().add(new XYChart.Data(x.get(i),y.get(i)));
        }
        series.setName(label);
        bChart.getData().addAll(series);
    }

    /**
     * Add a series of data to the line chart. This method is only for plotting a y axis of data so that data can be compared to one another for that specific
     * category.
     * 
     * @param y : An Array of doubles to be added to the chart, should be the Y-Axis of the data.
     * @param label2 : A Array of Strings to be set alongside each Y value as its label.
     */
    public void addSeries(ArrayList<Double> y, ArrayList<String> labels){
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i<y.size(); i++){
            series = new XYChart.Series();
            series.getData().add(new XYChart.Data(i+1,y.get(i)));
            bChart.getData().addAll(series);
        }
        
    }

    //*******NOTE***************//
    // SOME CHANGES NEED TO BE MADE HERE, EVERYTHING IS ADDED AS 1 CATEGORY, LOOK INTO SPLITTING THEM UP //
    //*******END NOTE***********//

        /**
     * Set the bounds of the X-Axis and the amount of units between each tick.
     * 
     * 
     * @param lowerBound : A double representing the lower bound.
     * @param upperBound : A double representing the upper bound.
     * @param tickUnit : A double representing the amount of units between each tick.
     */
    public void setXAxis(double lowerBound, double upperBound, double tickUnit){}

    //gets the Bar Chart
    public BarChart getChartObj(){return bChart;}


    //Create the plot
    public static void main(String args[]){
        launch(args);
    }
}

