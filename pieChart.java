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
import java.util.HashMap;
import java.util.Map;

public class pieChart extends Application implements ISPGraph{ 
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    final PieChart pChart = null;
    ArrayList<String> testCates = new ArrayList<String>(Arrays.asList("Apple", "Oranges", "Peachs", "Pears", "Bananas","Grapes"));
    public void start(Stage stage){
        this.addData(testCates);
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

    public pieChart(ArrayList<String> x){
        this.addData(x);
        final PieChart pChart = new PieChart(pieChartData);
    }

    public void addData(ArrayList<String> x){
        Map<String,Integer> slicesAndSize = this.getFrequencies(x);
        for(String element: slicesAndSize.keySet()){
            pieChartData.add(new PieChart.Data(element, slicesAndSize.get(element)));
        } 
    }

    public Map<String,Integer> getFrequencies(ArrayList<String> sclices){
        Map<String,Integer> freqMap = new HashMap<>();
        for(String element:sclices){
            if(!freqMap.containsKey(element)){
                freqMap.put(element,1);
            }
            else if(freqMap.containsKey(element)){
                freqMap.put(element, freqMap.get(element)+1);
            }
        }
        return freqMap;
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
