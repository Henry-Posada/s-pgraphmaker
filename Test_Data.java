import java.util.ArrayList;

public class Test_Data {
        /** Testing for mean method. If using IDE like VSCode be sure to have junit-platform-console-standalone.jar file somewhere on the machine you are running this on.
         * Additionally have settings.json configured correctly.
         */
        //@Test
        public void getEntireColumnTest(){
            Data testData = new Data("Data");

            ArrayList<Double> modelColumn = testData.getEntireColumn(1);

            System.out.println(modelColumn.get(0) == 1997.0);
            //assertEquals(5.67, mean(test), 0.001);
        }
}
