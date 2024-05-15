package org.selenium.pages;

import org.selenium.maps.SampleMap;
import org.selenium.utilities.BaseCommands;
import org.selenium.utilities.ReadProperty;

public class SamplePage extends SampleMap {
    private static ReadProperty readProperty = ReadProperty.getInstance();
    private static SamplePage object;
    private SamplePage() {}
    public static SamplePage getInstance()
    {
        if(object==null)
        {
            object=new SamplePage();
        }
        return object;
    }

    // Logging into application by calling credentials by test.properties file
    public void searchGoogle(String text) {
        BaseCommands.sendKeys(getSearch(), text, "text to search");
        BaseCommands.click(getSearchButton(),"search button");
    }

}