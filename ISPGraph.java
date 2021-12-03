import javafx.scene.chart.Chart;

// interface
interface ISPGraph {
    public void setChartTitle(String label);
    public void setXLabel(String label);
    public void setYLabel(String label);
    public Chart getChartObj();
    public void setYAxis(double lowerBound, double upperBound, double tickUnit);
    public void setXAxis(double lowerBound, double upperBound, double tickUnit);
  }
  