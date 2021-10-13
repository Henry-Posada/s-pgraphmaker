import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
private ArrayList<ArrayList<Double>> totalData = new ArrayList<ArrayList<Double>>();
private Scanner dataFile;
//properties
private String csvFileName;
private int currentRowNumber = 0;
private int currentColumnNumber = 0;

private ArrayList<String> attributesList = new ArrayList<String>(); 

    public Data(String csvFileName) {
        try {
            File csvFile = new File(String.format("s-pgraphmaker\\%s.csv", csvFileName));
            System.out.println("below here");
            System.out.println(csvFile.getAbsolutePath());
            dataFile = new Scanner(csvFile);
            //since its a csv we want to sepearte by commas
            dataFile.useDelimiter(",");
            this.csvFileName = csvFileName;

            readAllInitialData(dataFile);
        }
        catch (FileNotFoundException exception){
            System.out.println(String.format("There was no file with the name %s found in this project, did you type the name correctly and put the file in the right folder?", csvFileName));
        }
    }// end Data Contructor


    /**
     * Gets the next cell of data in the same row this means that it will be the next attibute of the same record.
     * @return
     */
    public String getNextRowCell(){
        return "";
    }


    /**
     * Gets the next cell of data in the same column, this means that the data will be of the same attribute but of the next record.
     * @return
     */
    public String getNextColumnCell(){
        return "";
    }


    /**
     * Gets a cell of a specific row and column. This should mainly be used to get specific data.
     * @param rowNumber
     * @param columnNumber
     * @return
     */
    public String getNextCell(int rowNumber, int columnNumber){
        return "";
    }


    /**
     * Gets the entire row of data useful for the startup and getting all attributes as well as creating an object out of the data (if the system will even handle that())
     * @param rowNumber
     * @return
     */
    public ArrayList<Double> getEntireRow(int rowNumber){        
        return totalData.get(rowNumber);
    }


    /**
     * Gets the entire column of data, this is a good way to tally all of a "group" of data
     * @param columnNumber
     * @return
     */
    public ArrayList<String> getEntireColumn(int columnNumber){
        return new ArrayList<String>();
    }


    /**
     * Sets ALL of the data of the CSV this is essential since removing/updating data can be difficult.
     * And there is no other real way to get data by row or column then rescanning info.
     * @param fileToBeProcessed
     * @return
     */
    private ArrayList<ArrayList<Double>> readAllInitialData(Scanner fileToBeProcessed){
        if (fileToBeProcessed.hasNextLine()){
            String[] firstRow = fileToBeProcessed.nextLine().split(",");

            for (int i = 0; i < firstRow.length; i++){
                attributesList.add(firstRow[i]);
            }
        } else {
            //TODO: throw an error
            System.out.println("This file has no lines in it.");
        }

        //TODO: see about empty spaces as it may "mis-align some columns/data"
         while (fileToBeProcessed.hasNextLine()){
            ArrayList<Double> currentRowList = new ArrayList<Double>();
            String[] processDataRow = fileToBeProcessed.nextLine().split(",");
            
            //NOTE first row is expected to be strings so they are done as strings

            //convert this array of string to an ArrayList//NOTE we use "i + 1" to skip the first cell of the row as that will often be a string and cant be a double
            for (int i = 0; i < processDataRow.length - 1; i++){
                Double currentDouble = Double.parseDouble(processDataRow[i + 1]);

                currentRowList.add(currentDouble.doubleValue());
            }

            totalData.add(currentRowList);            
         }//end while

         //close the file as we should now be done with reading from this file and any other use with this file should be editing it.
         //totalData should now be effectively our "open" version of the file.
         fileToBeProcessed.close();

         return totalData;
    }
}// end Data