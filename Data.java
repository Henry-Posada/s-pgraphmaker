import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//TODO: Refactoring notes. Better determine what methods should be private e.g. getIdentifier

/**
 * An object tranlation of a CSV that is tailored for this graphing system that meets the specified formatting.
 */
public class Data {
private ArrayList<ArrayList<Double>> totalData = new ArrayList<ArrayList<Double>>();

private File currentlyOpenFile;
private FileWriter currentlyOpenFileWriter;
private Scanner dataFile;

private ArrayList<String> attributesList = new ArrayList<String>();
private ArrayList<String> identifiersList = new ArrayList<String>(); 

//properties
private String csvFileName;
private int currentRowNumber = 0;
private int currentColumnNumber = 0;
//the amount of attributes
private int recordSize = 0;
//the amount of records 
private int tableSize = 0;

//Off sets for row and column to cater to if a row or column is not numerical data
final int ROW_OFFSET = 1;
final int COLUMN_OFFSET = 1;

    public Data(String csvFileName) {
        try {
            File csvFile = new File(String.format("s-pgraphmaker\\%s.csv", csvFileName));
            dataFile = new Scanner(csvFile);
            this.csvFileName = csvFileName;

            //since its a csv we want to sepearte by commas
            dataFile.useDelimiter(",");
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
    public double getNextRowCell(){
        try {
            //get to the next row first, then we get the cell
            currentColumnNumber++;

            return getCell(currentRowNumber, currentColumnNumber);   
        } catch (IndexOutOfBoundsException e) {
            return 0.0;
        }
    }


    /**
     * Gets the next cell of data in the same column, this means that the data will be of the same attribute but of the next record.
     * @return
     */
    public double getNextColumnCell(){
        try {
            //get to the next row first, then we get the cell
            currentRowNumber++;

            return getCell(currentRowNumber, currentColumnNumber);   
        } catch (IndexOutOfBoundsException e) {
            return 0.0;
        }
    }


    /**
     * Gets a cell of a specific row and column. This should mainly be used to get specific data.
     * @param rowNumber
     * @param columnNumber
     * @return
     */
    public double getNextCell(){
        try {
            //if at the end of a row, go to the next row at the first column
            if (currentColumnNumber == recordSize){
                currentColumnNumber = 1;
                currentRowNumber++;
            } else {
                currentColumnNumber++;
            }

            return getCell(currentRowNumber, currentColumnNumber);   
        } catch (IndexOutOfBoundsException e) {
            return 0.0;
        }
    }


    /**
     * Gets a cell of a specific row and column. This should mainly be used to get specific data.
     * @param rowNumber
     * @param columnNumber
     * @return
     */
    public double getCell(int rowNumber, int columnNumber){
        try {
            return totalData.get(rowNumber - ROW_OFFSET).get(columnNumber - COLUMN_OFFSET);   
        } catch (IndexOutOfBoundsException e) {
            return 0.0;
        }
    }


    /**
     * Gets the entire row of data useful for the startup and getting all attributes as well as creating an object out of the data (if the system will even handle that())
     * NOTE: Count here starts at 1 because the 0th row is discounted.
     * @param rowNumber
     * @return
     */
    public ArrayList<Double> getEntireRow(int rowNumber){        
        try {
            currentRowNumber = rowNumber;

            return totalData.get(rowNumber - ROW_OFFSET);   
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    /**
     * Gets the entire column of data, this is a good way to tally all of a "group" of data.
     * NOTE: Count here starts at 1 because the 0th column is discounted.
     * @param columnNumber
     * @return
     */
    public ArrayList<Double> getEntireColumn(int columnNumber){
        try {
            ArrayList<Double> columnData = new ArrayList<Double>();
            currentColumnNumber = columnNumber;
            //start at zero since it will be incremented before the first add
            currentRowNumber = 0;

            for (int i = 0; i < tableSize; i++){
                columnData.add(getNextColumnCell());
            }

            //TODO: maybe save these as we get them so we dont have to recalculate these? perhaps a higher level will keep track of that.
            return columnData;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    /**
     * Gets the identifier for the specified record.
     * @param recordIndex
     * @return
     */
    public String getIdentifier(int recordIndex){
        try{
            return identifiersList.get(recordIndex - 1);
         } catch (IndexOutOfBoundsException e) {
            return null;
        }
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

            recordSize = firstRow.length;
        } else {
            //TODO: throw an error
            System.out.println("This file has no lines in it.");
        }

        //TODO: see about empty spaces as it may "mis-align some columns/data"
         while (fileToBeProcessed.hasNextLine()){
            String[] processDataRow = fileToBeProcessed.nextLine().split(",");
            
            ArrayList<Double> currentRowList = createRecord(processDataRow);

            totalData.add(currentRowList);            
         }//end while

         //process all records so we now have the size of the table
         tableSize = totalData.size();

         //close the file as we should now be done with reading from this file and any other use with this file should be editing it.
         //totalData should now be effectively our "open" version of the file.
         fileToBeProcessed.close();

         return totalData;
    }


    /**
     * Creates a record (an arrayList of doubles) and returns it for later use.
     * @param processDataRow
     * @return
     */
    private ArrayList<Double> createRecord(String[] processDataRow) {
        ArrayList<Double> currentRowList = new ArrayList<Double>();

        //get the identifier
        identifiersList.add(processDataRow[0]);

        //NOTE first row is expected to be strings so they are done as strings
        //convert this array of string to an ArrayList//NOTE we use "i + 1" to skip the first cell of the row as that will often be a string and cant be a double
        for (int i = 0; i < processDataRow.length - 1; i++){
            double currentDouble = Double.parseDouble(processDataRow[i + 1]);

            currentRowList.add(currentDouble);
        }

        return currentRowList;
    }


    
    /**BEGIN Editing Data BEGIN **/

    /**
     * Changes the value of a specific cell of a specific record.
     * @param rowNumber
     * @param columnNumber
     * @param newValue
     * @return
     */
    public boolean changeCellValue(int rowNumber, int columnNumber, double newValue){
        try {
            //TODO: currently a change here will be saved correctly but if something already has that cell loaded the old value will be there so any UI will have to make a change along side this.
            ArrayList<Double> currentRecord = getEntireRow(rowNumber);

            currentRecord.set(columnNumber, newValue);

            //now that the change is made overwrite the csv to preserve the data
            exportCSV(csvFileName);

            return true;
        } catch (Exception e) {
            //TODO: handle exception

            return false;
        }
    }


    /**
     * Exports a whole CSV file in the form of the records that are currently made up.
     * @return
     */
    public boolean exportCSV(String fileName){
        try {
            currentlyOpenFile = new File(String.format("s-pgraphmaker\\%s.csv", fileName));
            //if its the same name as the current CSV then we are saving (overwriting)
            boolean overwriteOrAppend = !(currentlyOpenFile.exists());
            currentlyOpenFileWriter = new FileWriter(currentlyOpenFile, overwriteOrAppend);

            String[] attributesListAsArray = new String[recordSize];

            for(int i = 0; i < recordSize; i++){
                attributesListAsArray[i] = attributesList.get(i);
            }

            //write the attributes first
            StringBuilder line = new StringBuilder();

            for (int i = 0; i < attributesListAsArray.length; i++) {
                line.append(attributesListAsArray[i]);

                if (i != attributesListAsArray.length - 1) {
                    line.append(',');
                }
            }

            currentlyOpenFileWriter.write(line.toString());

            for(int i = 1; i < tableSize + 1; i++){
                String[] recordToBeWritten = convertRecordToStringArray(i);

                writeRecord(recordToBeWritten);
            }

            currentlyOpenFileWriter.close();

            return true;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }


    /**
     * Writes the record of the specified index to the csv file.
     * @param recordNumber
     * @return
     */
    public boolean appendRecordToDataFile(int recordNumber) {
        try{
            //TODO: make it more efficient about opening the file so we dont open and close the file for every update
            currentlyOpenFile = new File(String.format("s-pgraphmaker\\%s.csv", csvFileName));
            //set the below to true to append and NOT overwrite
            currentlyOpenFileWriter = new FileWriter(currentlyOpenFile, true);
            String[] recordToBeWritten = convertRecordToStringArray(recordNumber);

            writeRecord(recordToBeWritten);
            
            currentlyOpenFileWriter.close();

            insertRecord(recordToBeWritten);
            //one new record means we need to increment the table size
            tableSize++;

            return true;
        } catch (IOException e){
            System.out.println("There wasnt a file found");

            return false;
        }        
    }


    /**
     * Writes a record to the currently open file.
     * @param currentStringArrayRecord
     */
    public void writeRecord(String[] currentStringArrayRecord){
        try {
                StringBuilder line = new StringBuilder();

                line.append("\n");

                for (int i = 0; i < currentStringArrayRecord.length; i++) {
                    line.append(currentStringArrayRecord[i]);
                    if (i != currentStringArrayRecord.length - 1) {
                        line.append(',');
                    }
                }

                currentlyOpenFileWriter.write(line.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("There was an IO exception");
        }
    }


    /**
     * Finds the record based on the passed int. With that record we convert it to String array and return that.
     * Likely used for saving and exporting.
     * NOTE: Count here starts at 1 because the 0th row is discounted.
     * @param recordIndex
     * @return
     */
    public String[] convertRecordToStringArray(int recordIndex){
        ArrayList<Double> currentRecord = getEntireRow(recordIndex);
        String[] result = new String[recordSize];

        //NOTE: we start with a newline so we dont append to the end of the last record
        result[0] = identifiersList.get(recordIndex - ROW_OFFSET);

        for (int i = 1; i < recordSize; i++){
            result[i] = currentRecord.get(i - 1).toString();
        }

        return result;
    }


    /**
     * Deletes AND removes record from a csv by overwriting the current csv file
     * @param recordNumber
     * @return
     */
    public boolean deleteRecord(int recordNumber) {
        try {
            removeRecord(recordNumber);
            //Save the file since we want to fully remove the record
            exportCSV(csvFileName);

            //removed a record reduce the table size
            tableSize--;

            return true;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }


    /**
     * Removes the record from totalData but does NOT "save" that change to the file. This should only really be used for complete deletion but we seperate this as a method for clarity.
     * NOTE This count starts at one.
     * @param recordNumber
     */
    private void removeRecord(int recordNumber) {
        totalData.remove(recordNumber - ROW_OFFSET);
        identifiersList.remove(recordNumber - ROW_OFFSET);
    }


    /**
     * Inserts record at the bottom of totalData
     * @param recordAsStringArray
     */
    private void insertRecord(String[] recordAsStringArray) {
        ArrayList<Double> result = createRecord(recordAsStringArray);

        totalData.add(result);
    }
    /**END Editing Data END**/

    /**BEGIN Getter/Setters BEGIN **/
    public int getRecordSize() {return recordSize;}
    public int getTableSize() {return tableSize;}
    public ArrayList<String> getIdentifiersList() {return identifiersList;}
    public ArrayList<String> getAttributesList() {return attributesList;}
    /**END Getter/Setters END **/
}// end Data