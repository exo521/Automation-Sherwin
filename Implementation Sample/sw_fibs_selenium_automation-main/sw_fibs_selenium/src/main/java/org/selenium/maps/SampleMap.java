package org.selenium.maps;

import org.openqa.selenium.By;

public class SampleMap {
    private By search = By.xpath("//textarea[@title='Search']");
    private By searchButton = By.xpath("//input[@aria-label ='Google Search']");

    public By getSearch() {
        return search;
    }
    public By getSearchButton() {
        return searchButton;
    }

}

