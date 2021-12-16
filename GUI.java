import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.*;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*; 
import javafx.geometry.*;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.function.UnaryOperator;

import javax.imageio.ImageIO;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;

//C:\Users\Henry\Desktop\School\Grad\CSC 678\S&PGraphMakerFiles\s-pgraphmaker\ 


/**
 * @author Scott Shannon
 * @version Version 2.0
 */
public class GUI extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }
 
    Data dataSet;

    Stage mainStageRef;
    Scene graphCreationPopup;

    Chart contentChart;

    ArrayList<Menu> menuList = new ArrayList<Menu>();
    ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();

    final String[] CHART_TYPES = new String[] {"Scatter Chart", "Line Chart", "Bar Chart", "Pie Chart", "Histogram"};
    final String[] CHART_EXPLAINER_TEXT = new String[] {
        "Scatter Plot: Pick a series X and series Y. If you do not choose an X series the X series will be based off the index of the records.", 
        "LineGraph: Pick a series X and series Y. If you do not choose an X series the X series will be based off the index of the records.", 
        "Bar Chart: Pick a Y. If you do not choose a series the series will be based off the index of the records.",
        "Pie Chart: Pick a series. If you do not choose a series the series will be based off the index of the records.",
        "Histogram: Pick a Y. If you do not choose a series the series will be based off the index of the records."
    };

    private static TableView<List<Object>> dataTable;

    public void start(Stage mainStage) 
    {
        mainStageRef = mainStage;
        
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
                        //clear the data cells
                        dataTable.getColumns().clear();
                        //clear the columns (otherwise we will just add new ones every import)
                        dataTable.getItems().clear();
                        
                        FileChooser fileChooser = new FileChooser();
                        File selectedFile = fileChooser.showOpenDialog(mainStage);

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
                });//end click event for "Close"
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
                            infoAlert.setContentText("There is no data to be calculated, please import some data first.");        
                            
                            infoAlert.showAndWait();
                        }

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

                                            if (dataResult.size() > 1 && i == 0){
                                                result += " - ";
                                            }
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

                                        if (dataResult.size() > 1 && i == 0){
                                            result += " - ";
                                        }
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
        dataTable = new TableView();
        //TODO: if we can figure out how to make editing more fluid with dynamic data sizes
        //dataTable.setEditable(true);

        mainContent.getChildren().add(dataTable);

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

            newColumn.setCellValueFactory(new PropertyValueFactory<>(newProperty));

            return true;
        } catch (Exception e) {
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
            infoAlert.setContentText("There is no data to be graphed, please fill import some data first.");        
            
            infoAlert.showAndWait();
        }

        Stage popupwindow = new Stage();
        boolean removeXSeries = chartType.equals(CHART_TYPES[2]) || chartType.equals(CHART_TYPES[3]) || chartType.equals(CHART_TYPES[4]);
        //pie chart doesnt use the scale
        boolean hideScale = chartType.equals(CHART_TYPES[3]);

        final String EMPTY_SERIES_OPTION = "None";

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle(String.format("Making a %s.", graphName));

        Label explainer = new Label(CHART_EXPLAINER_TEXT[graphIndex]);
                                     
        TextField  axisXInput = new TextField ("");
        TextField  axisYInput = new TextField ("");
        TextField  titleInput = new TextField ("");

        TextField  xScaleLowerBoundInput = new TextField ("");
        TextField  xScaleUpperBoundInput = new TextField ("");
        TextField  xScaleTickInput = new TextField ("");

        TextField  yScaleLowerBoundInput = new TextField ("");
        TextField  yScaleUpperBoundInput = new TextField ("");
        TextField  yScaleTickInput = new TextField ("");

        //set as number text fields
        setAsNumTextField(new TextField[] {xScaleLowerBoundInput, xScaleUpperBoundInput, xScaleTickInput, yScaleLowerBoundInput, yScaleUpperBoundInput, yScaleTickInput});

        xScaleLowerBoundInput.setPromptText("X Scale Lower Bound");
        xScaleUpperBoundInput.setPromptText("X Scale Upper Bound");
        xScaleTickInput.setPromptText("X Scale Tick Interval");

        yScaleLowerBoundInput.setPromptText("Y Scale Lower Bound");
        yScaleUpperBoundInput.setPromptText("Y Scale Upper Bound");
        yScaleTickInput.setPromptText("Y Scale Tick Interval");

        ChoiceBox<String> seriesXBox = new ChoiceBox<>();
        ChoiceBox<String> seriesYBox = new ChoiceBox<>();

        titleInput.setPromptText("Graph Title");
        axisXInput.setPromptText("Axis X");
        axisYInput.setPromptText("Axis Y");

        Label seriesXLabel = new Label("Series X:");
        Label seriesYLabel = new Label("Series Y:");

        Button createGraphButton = new Button(String.format("Create a %s", graphName));
        Button saveGraphAsImageButton = new Button("Save Graph As Image");

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
        HBox xScaleModifierLine = new HBox();
        HBox yScaleModifierLine = new HBox();
        HBox saveButtonLine = new HBox();
        VBox layout= new VBox(10);

        //start button as invisible
        saveButtonLine.setVisible(false);

        saveButtonLine.getChildren().add(saveGraphAsImageButton);

        if (!chartType.equals(CHART_TYPES[3])){
            labelLine.getChildren().addAll(axisXInput, axisYInput);
        }

        //only add Y series if relevant
        if (removeXSeries){
            yScaleModifierLine.getChildren().addAll(yScaleLowerBoundInput, yScaleUpperBoundInput, yScaleTickInput);
            //NOTE: we still want the x axis label input incase they want to edit it
            labelLine.getChildren().add(titleInput);
            seriesLine.getChildren().addAll(seriesYLabel, seriesYBox);
        } else {
            xScaleModifierLine.getChildren().addAll(xScaleLowerBoundInput, xScaleUpperBoundInput, xScaleTickInput);
            yScaleModifierLine.getChildren().addAll(yScaleLowerBoundInput, yScaleUpperBoundInput, yScaleTickInput);
            labelLine.getChildren().add(titleInput);
            seriesLine.getChildren().addAll(seriesXLabel, seriesXBox, seriesYLabel, seriesYBox);
        }

        //START EVENT HANDLERS
        saveGraphAsImageButton.setOnAction(
            new EventHandler<ActionEvent>(){
                public void handle(ActionEvent event){
                    FileChooser fileChooser = new FileChooser();

                    fileChooser.setTitle("Save Image");
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Images", ".PNG"), new ExtensionFilter("All Files", "*.*"));

                    String filePath = fileChooser.showSaveDialog(mainStageRef).getAbsolutePath();

                    Stage popupwindow = new Stage();
                    Scene scene = new Scene(new Group(), 595, 400);

                    popupwindow.setTitle("Charts Example");
                    ((Group) scene.getRoot()).getChildren().add(contentChart);

                    WritableImage image = scene.snapshot(null);
                    File file = new File(filePath);

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    //put the chart back
                    contentLine.getChildren().add(contentChart);
                }
            }
        );//end save graph as image on click


        createGraphButton.setOnAction(
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    ArrayList<Double> seriesX = dataSet.getEntireColumn(seriesXBox.getValue());
                    ArrayList<Double> seriesY = dataSet.getEntireColumn(seriesYBox.getValue());

                    boolean hasSeriesX = !seriesXBox.getValue().equals(EMPTY_SERIES_OPTION);
                    String chartLabel = hasSeriesX ? seriesXBox.getValue() + " by " + seriesYBox.getValue() : seriesXBox.getValue();

                    ISPGraph chosenGraph;

                    if (hasSeriesX){
                        switch (chartType) {
                            case "Scatter Chart":
                                chosenGraph = new scatterChart(seriesX, seriesY, chartLabel);

                                break;
                            case "Line Chart":
                                chosenGraph = new lineChart(seriesX, seriesY, chartLabel);

                                break;
                            case "Bar Chart":
                                chosenGraph = new barChart(identitfierList, seriesY, chartLabel);

                                break;
                            case "Pie Chart":
                                //if the y series is at default value use the identifiers
                                if(seriesYBox.getValue().equals(EMPTY_SERIES_OPTION)){
                                    chosenGraph = new pieChart(identitfierList);
                                }
                                else {
                                    chosenGraph = new pieChart(dataSet.getEntireColumnAsStringList(seriesYBox.getValue()));
                                }                                

                                break;
                            case "Histogram":
                                chosenGraph = new histogramChart(seriesY, chartLabel);

                                break;
                            //need a default for compiler
                            default:
                                chosenGraph = new scatterChart(seriesX, seriesY, chartLabel);

                                break;
                        }
                    } else{
                        switch (chartType) {
                            case "Scatter Chart":
                                chosenGraph = new scatterChart(seriesY, identitfierList);

                                break;
                            case "Line Graph":
                                chosenGraph = new lineChart(seriesY, identitfierList);

                                break;
                            case "Bar Chart":
                                chosenGraph = new barChart(seriesY, identitfierList);

                                break;                            
                            //need a default for compiler
                            default:
                                chosenGraph = new scatterChart(seriesY, identitfierList);

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
                    //set the scales if applicable
                    if (numTextFieldsValuesValid(new TextField[] {xScaleLowerBoundInput, xScaleUpperBoundInput, xScaleTickInput})){
                        double xScaleLowerBoundValue = Double.parseDouble(xScaleLowerBoundInput.getText());
                        double xScaleUpperBoundValue = Double.parseDouble(xScaleUpperBoundInput.getText());

                        //determine if the "lower" is actually lower and vice versa
                        double xLowerBound = xScaleLowerBoundValue < xScaleUpperBoundValue ? xScaleLowerBoundValue : xScaleUpperBoundValue ;
                        double xUpperBound = xScaleLowerBoundValue < xScaleUpperBoundValue ? xScaleUpperBoundValue : xScaleLowerBoundValue ;

                        chosenGraph.setXAxis(xLowerBound, xUpperBound, Math.abs(Double.parseDouble(xScaleTickInput.getText())));
                    }
                    if (numTextFieldsValuesValid(new TextField[] {yScaleLowerBoundInput, yScaleUpperBoundInput, yScaleTickInput})){
                        double yScaleLowerBoundValue = Double.parseDouble(yScaleLowerBoundInput.getText());
                        double yScaleUpperBoundValue = Double.parseDouble(yScaleUpperBoundInput.getText());

                        //determine if the "lower" is actually lower and vice versa
                        double yLowerBound = yScaleLowerBoundValue < yScaleUpperBoundValue ? yScaleLowerBoundValue : yScaleUpperBoundValue ;
                        double yUpperBound = yScaleLowerBoundValue < yScaleUpperBoundValue ? yScaleUpperBoundValue : yScaleLowerBoundValue ;

                        chosenGraph.setYAxis(yLowerBound, yUpperBound, Math.abs(Double.parseDouble(yScaleTickInput.getText())));
                    }

                    contentChart = chosenGraph.getChartObj();

                    //clear in case of already containing a chart
                    contentLine.getChildren().clear();
                    contentLine.getChildren().add(contentChart);

                    //start button as invisible
                    saveButtonLine.setVisible(true);
                }                            
        });//end create graph onclick
        //END EVENT HANDLERS

        if (hideScale)
        {
            xScaleModifierLine.setVisible(false);
            yScaleModifierLine.setVisible(false);
        }            

        layout.getChildren().addAll(explainer, createGraphButton, labelLine, xScaleModifierLine, yScaleModifierLine ,seriesLine, contentLine, saveButtonLine);
        
        //"CSS"
        layout.setAlignment(Pos.CENTER);
        createGraphButton.setAlignment(Pos.CENTER);
        labelLine.setAlignment(Pos.CENTER);
        seriesLine.setAlignment(Pos.CENTER);
        xScaleModifierLine.setAlignment(Pos.CENTER);
        yScaleModifierLine.setAlignment(Pos.CENTER);
        contentLine.setAlignment(Pos.CENTER);
        explainer.setAlignment(Pos.CENTER);
        saveButtonLine.setAlignment(Pos.CENTER);
        explainer.setMaxWidth(400);
        explainer.setWrapText(true);
            
        graphCreationPopup = new Scene(layout, 600, 500);
            
        popupwindow.setScene(graphCreationPopup);            
        popupwindow.showAndWait();
    }//end createGraphPopup


    //helper functions
    /**
     * Validates if all passed in textfields (THAT SHOULD BE NUMBER BASED) are actually a valid number and not either empty or just '-' or '+'.
     * @param arrayOfTextFields
     * @return
     */
    public static boolean numTextFieldsValuesValid(TextField[] arrayOfTextFields){
        try {
            for (TextField textField : arrayOfTextFields) {
                //no empty fields
                if(textField.getLength() == 0){
                    return false;
                }

                String text = textField.getText();

                //text is only '-' or '+'
                if (textField.getLength() == 1 && (text.equals("-") || text.equals("+"))){
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            //not valid double 
            return false;
        }
        
    }


    /**
     * Takes an array of TextInputs and makes them only accept number format
     * @param arrayOfTextFields
     */
    public static void setAsNumTextField(TextField[] arrayOfTextFields){
        for (TextField textField : arrayOfTextFields) {
            textField.setTextFormatter(new TextFormatter<>(c -> {
                if (c.getControlNewText().isEmpty()) {
                    return c;
                }

                try {
                    //if the whole string is just - or + they are just signifying a sign
                    if (c.getControlNewText().equals("-") || c.getControlNewText().equals("+")){
                        return c;
                    }

                    //try to parse if it fails then somethings wrong and we arent updating the textfield
                    Double.parseDouble(c.getControlNewText());

                    return c;
                } catch (Exception e) {
                    return null;
                }
            }));
        }
    }
}