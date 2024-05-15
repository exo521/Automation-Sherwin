package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * This is an example of a Page Object that covers the Sherwin Home page.
 */
public final class SherwinHomePage extends PageObject {

    public SherwinHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Here's an example of a test that checks to make sure we can navigate to the Homeowners Page.
     *
     * DISCLAIMER: This is only intended to be used to show how Selenium works.
     *             This is NOT intended to be a good example of a good Acceptance test.
     *
     * @return Did the test navigate to the Homeowners page?
     */
    public boolean shouldGoToHomeownersPage() {

        /*
        This locator object informs Selenium how it can find the object you seek.
        ------------------------------------------------------------------------------
        This locator is used to find the link with this HTML:
        <a class="cmp-teaser__action-link" href="https://www.sherwin-williams.com/homeowners/color" target="_self">
            Explore Color
        </a>
        ------------------------------------------------------------------------------
        Let's break down this XPath expression:
        "//" - Search for the element anywhere on the page.
        "a" - Look for an element that has "a" for it's HTML tag.
        "[text()='Explore Color']" - Look for an element that exactly has "Explore Color" for its text.
        ------------------------------------------------------------------------------
        CSS Selectors: You can also use CSS Selectors to locate elements. These will be covered in the future.
        ------------------------------------------------------------------------------
        Both Chrome and Firefox have a Console you can use to help build and test your locators.
        On the console you can use the below syntax to return elements that match the corresponding query.
        $x("xpath syntax here")
        $$("css selector here")
         */
        var exploreColorLocator = By.xpath("//a[text()='Explore Color']");

        // Before clicking on any WebElements it's helpful to wait for the element to be clickable first.
        // Otherwise, you may click on the link before the page has loaded.
        // ExpectedConditions has many other useful functions to wait on various aspects of WebElements.
        wait.until(driver -> ExpectedConditions.elementToBeClickable(exploreColorLocator));

        // Given the locator, find the associated WebElement.
        // In Selenium, every object on the page is a WebElement, no matter if it's a link, text field, button, drop down menu, etc.
        var shopPaintAndColorLink = driver.findElement(exploreColorLocator);

        // At this point in the execution, the link should be available to click on, so click on it.
        click(shopPaintAndColorLink);

        // The until method will return the WebElement when it finds it visible.
        // If it doesn't find it within the allotted time, it will return null
        // The timeout for the wait object is specified when it's created. In this case, that is defined in the PageObject class.
        // It may also throw an exception when trying the find the header. It really depends on what problem it runs into.
        var headerLocator = By.xpath("//h2[text()='Paint Colors']");
        var paintHeaderElement = wait.until(driver -> ExpectedConditions.visibilityOfElementLocated(headerLocator));

        // The expression below will return true if the until function finds the header on the page.
        return paintHeaderElement != null;
    }

    private void click(WebElement element) {
        var attempts = 0;
        var done = false;
        while(!done) {
            try {
                element.click();
                ++attempts;
            }
            // A lot of times this Exception can be fixed by simply trying again.
            catch(ElementClickInterceptedException ignored) {}

            // If we tried many times, and it's still not working, then exit.
            if(attempts >= 10) {
                done = true;
                break;
            }

            done = true;
        }
    }

    /*
    ------------------------------------------------------------------------------
    Adding Additional Page Object Methods
    ------------------------------------------------------------------------------
    Let's say we want to add the ability to login our application.
    In online guides, you'll often see the following style shown:
        pageObject.enterUserName(username)
        pageObject.enterPassword(password)
        pageObject.clickLoginButton()
        var success = pageObject.areLoggedIn()

    This isn't the best practice because we don't need to expose how we are accomplishing logging in.
    Consider this alternative:

        var success = pageObject.login(username, password)

    This accomplishes the same task as before all in one line and self-documents what the code is doing.
    */

}
