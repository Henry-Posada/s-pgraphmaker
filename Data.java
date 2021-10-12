import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {

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
            this.csvFileName = csvFileName;
            attributesList = getEntireRow(0);
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
    public ArrayList<String> getEntireRow(int rowNumber){
        ArrayList<String> newRow = new ArrayList<String>();

        while (dataFile.hasNext()){
            newRow.add(dataFile.next());
        }

        return newRow;
    }


    /**
     * Gets the entire column of data, this is a good way to tally all of a "group" of data
     * @param columnNumber
     * @return
     */
    public ArrayList<String> getEntireColumn(int columnNumber){
        return new ArrayList<String>();
    }
}// end Data