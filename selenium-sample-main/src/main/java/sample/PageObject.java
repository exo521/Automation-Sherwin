package sample;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

/**
 * The concept of Page Objects is a common design pattern is commonly used when interacting with Selenium.
 * Each web page typically has their own corresponding web page.
 */
public abstract class PageObject {

    // Provide convenient access to the WebDriver because all page objects will need it to interact with the Browser.
    // Page Objects shouldn't care what kind of WebDriver (ex. Chrome, FireFox, etc.) we are working with.
    protected WebDriver driver;

    // Have a default Wait object that child classes can easily access to wait on web objects.
    protected Wait<WebDriver> wait;

    public PageObject(WebDriver driver) {
        this.driver = driver;

        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMinutes(1))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
    }
}
