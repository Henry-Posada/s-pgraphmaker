import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;


public class lineChart extends Application implements ISPGraph{
    
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart<Number,Number> lChart = new LineChart<Number,Number>(xAxis,yAxis);
    ArrayList<Double> x1 = new ArrayList<Double>(Arrays.asList(10.0,15.0,20.0,25.0,30.0));
    ArrayList<Double> y1 = new ArrayList<Double>(Arrays.asList(5.0,10.0,15.0,20.0,25.0));

    ArrayList<Double> x2 = new ArrayList<Double>(Arrays.asList(10.0,20.0,30.0,40.0,50.0));
    ArrayList<Double> y2 = new ArrayList<Double>(Arrays.asList(10.0,20.0,30.0,40.0,50.0));

    public lineChart(){

    }

    public lineChart(ArrayList<Double> x, ArrayList<Double> y, String label){
        this.addSeries(x, y, label);
    }
 
    public lineChart(ArrayList<Double> y, ArrayList<String> labels){
        //TODO: If only a Y series is given, X axis will be 1, 2, 3, etc.
        this.addSeries(y, labels);
    }

    public void start(Stage stage){

        this.addSeries(x1, y1, "Test");
        this.addSeries(x2, y2, "Test2");
        Scene scene  = new Scene(lChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    
    /**
     * Set the bounds of the X-Axis and the amount of units between each tick.
     * 
     * 
     * @param lowerBound : A double representing the lower bound.
     * @param upperBound : A double representing the upper bound.
     * @param tickUnit : A double representing the amount of units between each tick.
     */
    public void setXAxis(double lowerBound, double upperBound, double tickUnit){
        this.xAxis.setAutoRanging(false);
        this.xAxis.setLowerBound(lowerBound);
        this.xAxis.setUpperBound(upperBound);
        this.xAxis.setTickUnit(tickUnit);
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
        lChart.setTitle(label);
    }

    /**
     * Add a series of data to the line chart. Data must be passed as an Array of doubles, and two arrays must be passed.
     * Ensure the that the data based is related to each other, the X-Axis of the data and the Y-Axis of the Data.
     * 
     * @param x : An Array of doubles to be added to the chart, should be the X-Axis of the data.
     * @param y : An Array of doubles to be added to the chart, should be the Y-Axis of the data.
     * @param label : A string to be set as the title of the series.
     */
    public void addSeries(ArrayList<Double> x, ArrayList<Double> y, String label){
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i<x.size(); i++){
            series.getData().add(new XYChart.Data(x.get(i),y.get(i)));
        }
        series.setName(label);
        lChart.getData().addAll(series);
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
            lChart.getData().addAll(series);
        }
        
    }

    


    //gets the LineChart
    public LineChart getChartObj(){return lChart;}


    //Create the plot
    public static void main(String args[]){
        launch(args);
    }
}

