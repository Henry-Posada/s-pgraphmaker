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

        //because the add method doesnt return the item we assign after adding the items
        Menu fileMenu = menuList.get(0);
        Menu informationMenu = menuList.get(1);

        fileMenu.getItems().add(new MenuItem("Open"));
        fileMenu.getItems().add(new MenuItem("Export"));
        fileMenu.getItems().add(new MenuItem("Close"));

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

                        for (int i = 0; i < dataSet.getRecordSize(); i++) {
                            if (i == 0){
                                TableColumn<ArrayList<Object>, String> newColumn = new TableColumn(dataSet.getAttributesList().get(i));
                                
                                dataTable.getColumns().add(createColumn(i));
                            } else{
                                TableColumn<ArrayList<Object>, String> newColumn = new TableColumn(dataSet.getAttributesList().get(i));
                                
                                dataTable.getColumns().add(createColumn(i));
                            }                            
                        }

                        for (int i = 0; i < dataSet.getTableSize(); i++) {
                            List<Object> row = new ArrayList<>();
                            for (int columnIndex = 0 ; columnIndex < dataSet.getRecordSize() ; columnIndex++) {
                                Object cell = columnIndex < row.size() ? row.get(columnIndex) : "";
                                
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
        String text = "Column "+ (index + 1);
        
        TableColumn<List<Object>, String> col = new TableColumn<>(text);
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(index).toString()));
        return col ;        
    }
}