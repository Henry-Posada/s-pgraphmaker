import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import java.util.ArrayList;
import java.util.Arrays;

public class pieChart extends Application implements ISPGraph{ 
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    final PieChart pChart = null;
    ArrayList<String> testCates = new ArrayList<String>(Arrays.asList("Apple", "Oranges", "Peachs", "Pears", "Bananas"));
    ArrayList<Double> testDoubles = new ArrayList<Double>(Arrays.asList(15.0,25.0,10.0,30.0,20.0));
    public void start(Stage stage){
        this.addData(testCates, testDoubles);
        final PieChart chart = new PieChart(pieChartData);
        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();
    }

    //Constructor
    public pieChart(){
        final PieChart pChart = new PieChart(pieChartData);
    }

    public pieChart(ArrayList<String> x, ArrayList<Double> y){
        this.addData(x, y);
        final PieChart pChart = new PieChart(pieChartData);
    }

    public void addData(ArrayList<String> x, ArrayList<Double> y){
        for(int i = 0; i < x.size(); i++){
        pieChartData.add(new PieChart.Data(x.get(i), y.get(i)));
       }
    }

    public void setLegendSide(String side){
        side = side.toLowerCase();
        switch(side) {
            case "right":
                pChart.setLegendSide(Side.RIGHT);
                break;
            case "left":
                pChart.setLegendSide(Side.LEFT);
                break;
            case "top":
                pChart.setLegendSide(Side.TOP);
                break;
            case "bottom":
                pChart.setLegendSide(Side.BOTTOM);
                break;
        }
    }

   public void setChartTitle(String label){
        pChart.setTitle(label);
    }

    public void setXLabel(String Label){}

    public void setYLabel(String Label){}

    public static void main(String args[]){
        launch(args);
    }

    public Chart getChartObj(){return null;}
}
