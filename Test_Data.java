import java.io.File;
import java.util.ArrayList;

//Both imports are needed junit testing.
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

/**
 * READ ME !IMPORTANT!
 * testing here requires having a proper CSV file (specified in our documentation)
 * AND the file may need to be in a specific directory 
 * for example i had to go here
 * C:\Users\Scott\AppData\Roaming\Code\User\workspaceStorage\901e0529482250ba4d82a9d172113e65\redhat.java\jdt_ws\Vs Code java_62038294
 * and then add a folder called "s-pgraphmaker"
 * then add the "Data.csv" file
 * 
 * To find out where you need to put yours will be outputted via the below test (we force it to fail to output the message while in the correct context)
 * //assertNotNull("Working Directory = " + System.getProperty("user.dir"), null);
 * 
 * Side note, you may want to occasionally cull the "Data.csv" as with enough testing the append method will overtime grow the file(s)
 */
public class Test_Data {

    final String TEST_FILE_PATH = "s-pgraphmaker\\Data.csv";

    @Test
    public void importDataTest() {        
        Data testData = new Data(TEST_FILE_PATH);

        assertNotNull("There are no attributes", testData.getAttributesList());
        assertNotNull("There are no identifiers in the file.", testData.getIdentifiersList());
        assertNotNull("There are no records.", testData.getEntireRow(1));        
    }    


    @Test
    public void getCellTest() {
         Data testData = new Data(TEST_FILE_PATH);

         assertEquals(5345.0, testData.getCell(2,3), 0.0001);
    }


    @Test
    public void changeCellValueTest() {
         Data testData = new Data(TEST_FILE_PATH);

         testData.changeCellValue(1, 0, 1555.0);

         //we change the date first since a previous test may overwrite this
         testData.changeCellValue(1, 0, 1997.0);

         assertEquals(1997.0, testData.getCell(1, 1),.0001);
    }


    @Test 
    public void getEntireColumnTest(){
         Data testData = new Data(TEST_FILE_PATH);

         testData.changeCellValue(1, 0, 1555.0);

         //we change the date first since a previous test may overwrite this
         testData.changeCellValue(1, 0, 1997.0);

         ArrayList<Double> modelColumn = testData.getEntireColumn(1);
         
         assertEquals(1997, modelColumn.get(0), 0.001);

         testData.changeCellValue(1, 0, 1555.0);

         //have to re-get the column since the column isnt retrieved by reference
         modelColumn = testData.getEntireColumn(1);

         assertEquals(1555.0, modelColumn.get(0), 0.001);
    }       


    @Test
    public void exportCSVTest() {
        Data testData = new Data(TEST_FILE_PATH);
        String fileName = "s-pgraphmaker\\fileToCreate.csv";
        File csvFile = new File(fileName);

        //if file exists from previous test delete it
        if (csvFile.exists()){
            csvFile.delete();
        }

        testData.exportCSV(fileName);

        csvFile = new File(fileName);

        assertTrue(csvFile.exists(), "File was not created");
    }


    @Test
    public void appendRecordToDataFileTest(){
        //TODO: fix this test when the new file export works (paths are different/not pre set since we use UI now)
         Data testData = new Data(TEST_FILE_PATH);

         String firstSetFilePath = "s-pgraphmaker\\firstSet.csv";
         String secondSetFilePath = "s-pgraphmaker\\secondSet.csv";

         testData.exportCSV(firstSetFilePath);

         testData.appendRecordToDataFile(1);

         testData.exportCSV(secondSetFilePath);

         Data firstSet = new Data(firstSetFilePath);
         Data secondSet = new Data(secondSetFilePath);

         int firstSize = firstSet.getTableSize();
         int secondSize = secondSet.getTableSize();

         assertTrue(firstSet.getTableSize() < secondSet.getTableSize(), "Second set should be larger but isn't.");
    }
}
