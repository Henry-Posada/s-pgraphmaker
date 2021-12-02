import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.*;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.*; 
import javafx.geometry.*;

import java.io.File;
import java.util.*;

//C:\Users\Henry\Desktop\School\Grad\CSC 678\S&PGraphMakerFiles\s-pgraphmaker\ 


/**
 * Homework8 make a picture that involves at least one rectangle, polygon, oval, text, color, gradient and effect
 * 
 * @author Scott Shannon
 * @version Version 1
 */
public class GUI extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }
 
    Data dataSet;

    ArrayList<Menu> menuList = new ArrayList<Menu>();
    ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();

    final String[] CHART_TYPES = new String[] {"Scatter Chart", "Line Chart", "Bar Chart", "Pie Chart", "Histogram"};
    final String[] CHART_EXPLAINER_TEXT = new String[] {//TODO: give accurate explainers
        "Scatter Plot: Pick a series X and series Y for the X axis. If you do not choose an X series the X series will be based off the index of the records.", 
        "LineGraph: Pick a series X and series Y for the X axis. If you do not choose an X series the X series will be based off the index of the records.", 
        "Bar Chart: Pick a series X and series Y for the X axis. If you do not choose an X series the X series will be based off the index of the records.",
        "Pie Chart: Pick a series X and series Y for the X axis. If you do not choose an X series the X series will be based off the index of the records.",
        "Histogram: Pick a series X and series Y for the X axis. If you do not choose an X series the X series will be based off the index of the records."
    };

    private static TableView<List<Object>> dataTable;

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("S-P Graph Maker");
        
        //set up main section
        BorderPane root = new BorderPane();
        VBox mainContent = new VBox();
        Scene mainScene = new Scene(root, 600, 600);

        mainStage.setScene(mainScene);

        //*************Menu**************/
        //set up menu bar
        MenuBar menuBar = new MenuBar();

        menuList.add(new Menu("File"));
        menuList.add(new Menu("Information"));
        menuList.add(new Menu("Graphs"));
        menuList.add(new Menu("Calculations"));

        //because the add method doesnt return the item we assign after adding the items
        Menu fileMenu = menuList.get(0);
        Menu informationMenu = menuList.get(1);
        Menu graphsMenu = menuList.get(2);
        Menu calcuationsMenu = menuList.get(3);

        fileMenu.getItems().add(new MenuItem("Open"));
        fileMenu.getItems().add(new MenuItem("Export"));
        fileMenu.getItems().add(new MenuItem("Close"));

        graphsMenu.getItems().add(new MenuItem(CHART_TYPES[0]));
        graphsMenu.getItems().add(new MenuItem(CHART_TYPES[1]));
        graphsMenu.getItems().add(new MenuItem(CHART_TYPES[2]));
        graphsMenu.getItems().add(new MenuItem(CHART_TYPES[3]));
        graphsMenu.getItems().add(new MenuItem(CHART_TYPES[4]));

        calcuationsMenu.getItems().add(new MenuItem("Mean, Median, Mode, Range"));

        informationMenu.getItems().add(new MenuItem("About the Program"));
        //TODO: add graphics to menu items? menuItemList.get(i).setGraphic( new ImageView( new Image(iconArray[i]) ) );

        //add our menu "columns"
        for (int i = 0; i < menuList.size(); i++) {            
            menuBar.getMenus().add(menuList.get(i));
        }
            //*********Events******************/
            //"About the program button"
            informationMenu.getItems().get(0).setOnAction(
            new EventHandler<ActionEvent>()
            {
                    public void handle(ActionEvent event)
                    {
                        Alert infoAlert = new Alert(AlertType.INFORMATION);
                        
                        infoAlert.setTitle("About the program");
                        infoAlert.setHeaderText(null); 
                        infoAlert.setContentText("You can open a csv file following our format which can be found at 'https://github.com/Henry-Posada/s-pgraphmaker/wiki'.\n You can edit the data run calculations on the data and create graphs.");        
                        
                        infoAlert.showAndWait();
                    }
            });//end click evennt for "About the program"


            //"Import CSV"
            fileMenu.getItems().get(0).setOnAction(
            new EventHandler<ActionEvent>()
            {
                    public void handle(ActionEvent event)
                    {
                        //TODO: find a way to clear the table properly
                        FileChooser fileChooser = new FileChooser();
                        File selectedFile = fileChooser.showOpenDialog(mainStage);

                        System.out.println(selectedFile.getAbsolutePath());

                        dataSet = new Data(selectedFile.getAbsolutePath());

                        //create our columns dynamically
                        for (int i = 0; i < dataSet.getRecordSize(); i++) {
                            dataTable.getColumns().add(createColumn(i));
                        }

                        //get each record from the data
                        for (int i = 0; i < dataSet.getTableSize(); i++) {
                            List<Object> row = new ArrayList<>();
                            ArrayList<String> currentRecord = dataSet.getEntireRowAsStringList(i);

                            //create record for each row of the table.
                            for (int columnIndex = 0 ; columnIndex < dataSet.getRecordSize() ; columnIndex++) {
                                Object cell = columnIndex < dataSet.getTableSize() ? currentRecord.get(columnIndex).toString() : "";
                                
                                row.add(cell);
                            }
                            dataTable.getItems().add(row);
                        }                        
                    }
            });//end click evennt for "Import CSV"


            //"Export File"
            fileMenu.getItems().get(1).setOnAction(
            new EventHandler<ActionEvent>()
            {
                    public void handle(ActionEvent event)
                    {
                        try {
                            //check if the data is effectively empty if so then we throw a null pointer which is slightly redudnat but by making a check like thsi we also properly see if dataSet is null
                            if (dataSet.getTableSize() == 0)
                                throw new NullPointerException();

                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Export");
                            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Commma Separated Values", ".csv"), new ExtensionFilter("All Files", "*.*"));

                            String filePath = fileChooser.showSaveDialog(mainStage).getAbsolutePath();

                            dataSet.exportCSV(filePath);
                        }
                        catch (NullPointerException ex) {
                            Alert infoAlert = new Alert(AlertType.ERROR);
                        
                            infoAlert.setTitle("There is no data");
                            infoAlert.setHeaderText("ERROR"); 
                            infoAlert.setContentText("There is no data to be exported, please fill in some data first.");        
                            
                            infoAlert.showAndWait();
                        }
                    }
            });//end click evennt for "Export File"


            //"Close"
            fileMenu.getItems().get(2).setOnAction(
                new EventHandler<ActionEvent>()
                {
                        public void handle(ActionEvent event)
                        {
                            System.exit(0);
                        }
                });//end click evennt for "Close"



            //*********BEGIN: Events-Graphs******************/
                //Scatter Chart
                graphsMenu.getItems().get(0).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        createGraphPopup(CHART_TYPES[0], "My Scatter Plot", 0);
                    }
                });


                //Line Chart
                graphsMenu.getItems().get(1).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        createGraphPopup(CHART_TYPES[1], "My Line Plot", 1);
                    }
                });


                //Bar Chart
                graphsMenu.getItems().get(2).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        createGraphPopup(CHART_TYPES[2], "My Bar Chart", 2);
                    }
                });


                //Pie Chart
                graphsMenu.getItems().get(3).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        createGraphPopup(CHART_TYPES[3], "My Pie Chart", 3);
                    }
                });


                //Histogram
                graphsMenu.getItems().get(4).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        createGraphPopup(CHART_TYPES[4], "My Histogram", 4);
                    }
                });
            //*********END: Events-Graphs******************/
            

            //*********BEGIN: Events-Calculations******************/
            //Mean Median Mode, Range
            calcuationsMenu.getItems().get(0).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        Stage popupwindow = new Stage();

                        popupwindow.initModality(Modality.APPLICATION_MODAL);
                        popupwindow.setTitle("Mean, Median, Mode, Range");

                        Label explainer = new Label("Calculate the Mean, Median, Mode, or Range. Just pick a column.");
                        Label content = new Label("");
                        ChoiceBox<String> seriesXBox = new ChoiceBox<>();

                        Button meanButton = new Button("Mean");
                        Button medianButton = new Button("Median");
                        Button modeButton = new Button("Mode");
                        Button rangeButton = new Button("Range");

                        ArrayList<String> attributesList = dataSet.getAttributesList();

                        //NOTE: we start at 1 to not add the record Type
                        for (int i = 1; i < attributesList.size(); i++){
                            seriesXBox.getItems().add(attributesList.get(i));
                        }
        
                        //select initial values
                        seriesXBox.getSelectionModel().select(0);

                        //line containing the interface to determine labels
                        HBox seriesLine = new HBox();
                        HBox buttonLine = new HBox();
                        //holds the content (calculated result)
                        VBox layout= new VBox(10);

                        meanButton.setOnAction(
                            new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent event) {
                                    ArrayList<Double> columnData = dataSet.getEntireColumn(seriesXBox.getValue());

                                    content.setText(calculations.findMean(columnData) + "");
                            }
                        });

                        medianButton.setOnAction(
                            new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent event) {
                                    ArrayList<Double> columnData = dataSet.getEntireColumn(seriesXBox.getValue());

                                    content.setText(calculations.findMedian(columnData) + "");
                            }
                        });

                        modeButton.setOnAction(
                            new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent event) {
                                    ArrayList<Double> columnData = dataSet.getEntireColumn(seriesXBox.getValue());

                                    String result = "";
                                    ArrayList<Double> dataResult = calculations.findMode(columnData);

                                    if (dataResult.size() > 0){
                                        for (int i = 0; i < dataResult.size(); i++){
                                            result += dataResult.get(i);
                                        }
                                    } else {
                                        result = "There is no mode";
                                    }
                                    

                                    content.setText(result);
                            }
                        });

                        rangeButton.setOnAction(
                            new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent event) {
                                    ArrayList<Double> columnData = dataSet.getEntireColumn(seriesXBox.getValue());

                                    String result = "";
                                    ArrayList<Double> dataResult = calculations.findRange(columnData);

                                    for (int i = 0; i < dataResult.size(); i++){
                                        result += dataResult.get(i);
                                    }

                                    content.setText(result);
                            }
                        });

                        buttonLine.getChildren().addAll(meanButton, medianButton, modeButton, rangeButton); 
                        seriesLine.getChildren().add(seriesXBox);

                        layout.getChildren().addAll(explainer, buttonLine, seriesLine, content);            
                        layout.setAlignment(Pos.CENTER);
                        buttonLine.setAlignment(Pos.CENTER);
                        seriesLine.setAlignment(Pos.CENTER);
                        content.setAlignment(Pos.CENTER);
                            
                        Scene scene1= new Scene(layout, 600, 500);
                            
                        popupwindow.setScene(scene1);            
                        popupwindow.showAndWait();
                    }
                });
            //*********END: Events-Calculations******************/
            //*********Events******************/
        //*************Menu**************/

        //*************Data Table***************/
        dataTable = new TableView();
        dataTable.setEditable(true);

        mainContent.getChildren().add(dataTable);
        //*************Data Table***************/

        //place our "wrapping" objects like the menu, data-table, graphs etc
        root.setTop(menuBar);
        root.setCenter(mainContent);

        mainStage.show();
    }


    //helper functions
    /**
     * 
     * @param newProperty
     * @param newData
     * @return
     */
    private static boolean addTableColumn( String newProperty, ArrayList<Double> newData){
        try {
            TableColumn<String, String> newColumn = new TableColumn<String, String>(newProperty);
            //TODO: actually figure out what the below does
            newColumn.setCellValueFactory(new PropertyValueFactory<>(newProperty));

            //dataTable.getColumns().add(newColumn);

            return true;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }


    //from stack overflow
    private TableColumn<List<Object>, ?> createColumn(int index) {        
        TableColumn<List<Object>, String> col = new TableColumn<>(dataSet.getAttributesList().get(index));
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(index).toString()));
        
        return col ;        
    }


    /**
     * Creates a popup window to create a chart.
     * @param chartType the type of chart to determine what XY chart will be made.
     * @param graphName tbe name to give.
     * @param graphIndex Index relative to CHART_TYPES
     */
    private void createGraphPopup(String chartType, String graphName, int graphIndex) {
        //notify the user that there is no data
        try {
            //TODO: we should promt them to import data and actually open up the file chooser (or give them a cancel option)
            //check if the data is effectively empty if so then we throw a null pointer which is slightly redudnat but by making a check like thsi we also properly see if dataSet is null
            if (dataSet.getTableSize() == 0)
                throw new NullPointerException();
        }
        catch (NullPointerException ex) {
            Alert infoAlert = new Alert(AlertType.ERROR);
        
            infoAlert.setTitle("There is no data");
            infoAlert.setHeaderText("ERROR"); 
            infoAlert.setContentText("There is no data to be graphed, please fill in some data first.");        
            
            infoAlert.showAndWait();
        }

        Stage popupwindow = new Stage();

        boolean removeYSeries =  chartType.equals(CHART_TYPES[2]) || chartType.equals(CHART_TYPES[3]) || chartType.equals(CHART_TYPES[4]);

        final String EMPTY_SERIES_OPTION = "None";

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle(String.format("Making a %s.", graphName));

        Label explainer = new Label(CHART_EXPLAINER_TEXT[graphIndex]);
                                     
        TextField  axisXInput = new TextField ("");
        TextField  axisYInput = new TextField ("");
        TextField  titleInput = new TextField ("");

        ChoiceBox<String> seriesXBox = new ChoiceBox<>();
        ChoiceBox<String> seriesYBox = new ChoiceBox<>();

        titleInput.setPromptText("Graph Title");
        axisXInput.setPromptText("Axis X");
        axisYInput.setPromptText("Axis Y");
        Label seriesXLabel = new Label("Series X:");
        Label seriesYLabel = new Label("Series Y:");

        Button createGraphButton = new Button(String.format("Create a %s", graphName));

        ArrayList<String> attributesList = dataSet.getAttributesList();
        ArrayList<String> identitfierList = dataSet.getIdentifiersList();

        //NOTE: we start at 1 to not add the record Type
        for (int i = 1; i < attributesList.size(); i++){
            seriesXBox.getItems().add(attributesList.get(i));
            seriesYBox.getItems().add(attributesList.get(i));
        }
        
        seriesXBox.getItems().add(EMPTY_SERIES_OPTION);
        seriesYBox.getItems().add(EMPTY_SERIES_OPTION);

        //select initial values
        seriesXBox.getSelectionModel().select(0);
        //TODO: if there are less than 2 columns this will be a problem
        seriesYBox.getSelectionModel().select(1);

        //line containing the interface to determine labels
        HBox labelLine = new HBox();
        HBox seriesLine = new HBox();
        //holds the content (graphs)
        HBox contentLine = new HBox();
        VBox layout= new VBox(10);

        //if its NOT a histogram add the series x label
        if(!chartType.equals(CHART_TYPES[3])){
            labelLine.getChildren().add(axisXInput);
        }

        //only add Y series if relevant
        if (removeYSeries){
            labelLine.getChildren().add(titleInput);
            seriesLine.getChildren().add(seriesXBox);
        } else {
            labelLine.getChildren().addAll(axisXInput, axisYInput, titleInput);
            seriesLine.getChildren().addAll(seriesXBox, seriesYLabel, seriesYBox);
        }

        createGraphButton.setOnAction(
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    ArrayList<Double> seriesX = dataSet.getEntireColumn(seriesXBox.getValue());
                    ArrayList<Double> seriesY = dataSet.getEntireColumn(seriesYBox.getValue());

                    boolean hasSeriesY = !seriesYBox.getValue().equals(EMPTY_SERIES_OPTION);
                    String chartLabel = hasSeriesY ? seriesXBox.getValue() + " by " + seriesYBox.getValue() : seriesXBox.getValue();

                    ISPGraph chosenGraph;

                    if (hasSeriesY){
                        switch (chartType) {
                            case "Scatter Chart":
                                chosenGraph = new scatterChart(seriesX, seriesY, chartLabel);

                                break;
                            case "Line Chart":
                                chosenGraph = new lineChart(seriesX, seriesY, chartLabel);

                                break;
                            case "Bar Chart":
                                chosenGraph = new barChart(identitfierList, seriesX, chartLabel);

                                break;
                            case "Pie Chart":
                                //if the x series is at default value use the identifiers
                                if(seriesXBox.getValue().equals(EMPTY_SERIES_OPTION)){
                                    chosenGraph = new pieChart(identitfierList);
                                }
                                else {
                                    chosenGraph = new pieChart(dataSet.getEntireColumnAsStringList(seriesXBox.getValue()));
                                }                                

                                break;
                            case "Histogram":
                                chosenGraph = new histogramChart(seriesX, chartLabel);

                                break;
                            //need a default for compiler
                            default:
                                chosenGraph = new scatterChart(seriesX, seriesY, chartLabel);

                                break;
                        }
                    } else{
                        switch (chartType) {
                            case "Scatter Chart":
                                chosenGraph = new scatterChart(seriesX, identitfierList);

                                break;
                            case "Line Graph":
                                chosenGraph = new lineChart(seriesX, identitfierList);

                                break;
                            case "Bar Chart":
                                chosenGraph = new barChart(seriesX, identitfierList);

                                break;                            
                            //need a default for compiler
                            default:
                                chosenGraph = new scatterChart(seriesX, identitfierList);

                                break;
                        }
                    }

                    //set the labels if applicable
                    if (titleInput.getLength() != 0)
                        chosenGraph.setChartTitle(titleInput.getText());
                    if (axisXInput.getLength() != 0)
                        chosenGraph.setXLabel(axisXInput.getText());
                    if (axisYInput.getLength() != 0)
                        chosenGraph.setYLabel(axisYInput.getText());

                    Chart contentChart = chosenGraph.getChartObj();

                    //clear in case of already containing a chart
                    contentLine.getChildren().clear();
                    contentLine.getChildren().add(contentChart);
                }                            
        });

        layout.getChildren().addAll(explainer, createGraphButton, labelLine ,seriesLine, contentLine);
        
        //"CSS"
        layout.setAlignment(Pos.CENTER);
        createGraphButton.setAlignment(Pos.CENTER);
        labelLine.setAlignment(Pos.CENTER);
        seriesLine.setAlignment(Pos.CENTER);
        explainer.setAlignment(Pos.CENTER);
        explainer.setMaxWidth(400);
        explainer.setWrapText(true);
            
        Scene scene1= new Scene(layout, 600, 500);
            
        popupwindow.setScene(scene1);            
        popupwindow.showAndWait();
    }//end createGraphPopup
}