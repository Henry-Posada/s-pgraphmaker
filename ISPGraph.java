import javafx.scene.chart.XYChart;

// interface
interface ISPGraph {
    public void setChartTitle(String label);
    public void setXLabel(String label);
    public void setYLabel(String label);
    public XYChart getChartObj();
  }
  