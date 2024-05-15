package tests;

import org.selenium.pages.*;
import org.selenium.utilities.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class SampleTest extends BaseTest {
    SamplePage samplePage = SamplePage.getInstance();

    @DataProvider(name = "sampleData")
    public Object[][] sampleData() {
        Map<Integer, Map<String, String>> sampleData = ExcelDataProvider.excelDataMap(testDataExcel + "/SampleTest.xlsx", "SampleData");
        return new Object[][]{{sampleData}};
    }

    @Test(dataProvider = "sampleData")
    @ZephyrIntegration.JiraData(jiraId="")
    public void sampleTest(Map<Integer, Map<String, String>> sampleData) throws InterruptedException {
        BaseCommands.getURL(URL);
        samplePage.searchGoogle(sampleData.get(1).get("text"));
        BaseCommands.setShortWait();
        BaseCommands.setSmallWait();
    }
}
