import java.util.ArrayList;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.ScatterChart;


public class scatterChart extends Application{
    
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private ScatterChart<Number,Number> sChart = new ScatterChart<Number,Number>(xAxis,yAxis);
    Data testData = new Data("Data");


    public void start(Stage stage){
        this.addSeries(testData.getEntireColumn(1), testData.getEntireColumn(3), "Test");
        Scene scene  = new Scene(sChart, 500, 400);
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
     * Set the title of the scatter chart.
     * 
     * @param label : A string to be set as the title of the chart.
     */
    public void setChartTitle(String label){
        sChart.setTitle(label);
    }

    /**
     * Add a series of data to the scatter chart. Data must be passed as an ArrayList of type double, and two arrays must be passed.
     * Ensure the that the data based is related to each other, the X-Axis of the data and the Y-Axis of the Data.
     * 
     * @param x : An ArrayList of type double to be added to the chart, should be the X-Axis of the data.
     * @param y : An ArrayList of type double to be added to the chart, should be the Y-Axis of the data.
     * @param label : A string to be set as the title of the series.
     */
    public void addSeries(ArrayList<Double> x, ArrayList<Double> y, String label){
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i<x.size(); i++){
            series.getData().add(new XYChart.Data(x.get(i),y.get(i)));
        }
        series.setName(label);
        sChart.getData().addAll(series);
    }

    //Create the plot
    public static void main(String args[]){
        launch(args);
    }
}
