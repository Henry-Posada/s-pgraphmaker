import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*; 
import javafx.animation.*;
import javafx.geometry.*;

import java.io.File;
import java.util.*;

import javax.sound.sampled.Line;

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

    private static TableView<List<Object>> dataTable;

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("S-P Graph Maker");
        
        //set up main section
        BorderPane root = new BorderPane();
        VBox mainContent = new VBox();
        Scene mainScene = new Scene(root, 600, 600);

        mainStage.setScene(mainScene);

        //TODO: remove
        //Scene ymScene = new Scene (new LineChart<>(xAxis, yAxis), 400, 400);
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

        graphsMenu.getItems().add(new MenuItem("Scatter Plot"));
        graphsMenu.getItems().add(new MenuItem("Line Graph"));

        calcuationsMenu.getItems().add(new MenuItem("Mean"));

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
                            //File initialFilePath = new File("myData.csv");
                            //fileChooser.setInitialDirectory(initialFilePath);

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
            //Scatter plot
            graphsMenu.getItems().get(0).setOnAction(
                new EventHandler<ActionEvent>() 
                {
                    public void handle(ActionEvent event) {
                        Stage popupwindow=new Stage();
      
                        popupwindow.initModality(Modality.APPLICATION_MODAL);
                        popupwindow.setTitle("Making a scatterplot.");

                        Label explainer= new Label("Pick a series X and series Y for the X axis. If you do not choose an X series the X series will be based off the index of the records. (TO BE IMPLEMENTED)");
                                                       
                        ChoiceBox<String> seriesXBox = new ChoiceBox<>();
                        ChoiceBox<String> seriesYBox = new ChoiceBox<>();
     
                        Label xLabel = new Label("Series X:");
                        Label yLabel = new Label("Series Y:");

                        Button createGraphButton = new Button("Create a ScatterPlot");

                        ArrayList<String> attributesList = dataSet.getAttributesList();

                        //NOTE: we start at 1 to not add the record Type
                        for (int i = 1; i < attributesList.size(); i++){
                            seriesXBox.getItems().add(attributesList.get(i));
                            seriesYBox.getItems().add(attributesList.get(i));
                        }
                        
                        //select initial values
                        seriesXBox.getSelectionModel().select(0);
                        seriesYBox.getSelectionModel().select(1);

                        HBox firstLine = new HBox();

                        firstLine.getChildren().addAll(xLabel, seriesXBox, yLabel, seriesYBox);

                        VBox layout= new VBox(10);
                                       
                        createGraphButton.setOnAction(
                            new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent event) {
                                    String DELETEME1 = seriesXBox.getValue();
                                    String DELETEME2 = seriesYBox.getValue();
                                    ArrayList<Double> seriesX = dataSet.getEntireColumn(seriesXBox.getValue());
                                    ArrayList<Double> seriesY = dataSet.getEntireColumn(seriesYBox.getValue());


                                }                            
                        });

                        layout.getChildren().addAll(explainer, createGraphButton, firstLine);
                            
                        layout.setAlignment(Pos.CENTER);
                            
                        Scene scene1= new Scene(layout, 300, 250);
                            
                        popupwindow.setScene(scene1);
                            
                        popupwindow.showAndWait();
                    }
                });
            //*********END: Events-Graphs******************/
            

            //*********BEGIN: Events-Calculations******************/
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
}
