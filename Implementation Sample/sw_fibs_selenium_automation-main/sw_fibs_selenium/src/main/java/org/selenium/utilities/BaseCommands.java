package org.selenium.utilities;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.selenium.utilities.ListenersImplementation.test;


public class BaseCommands extends BaseTest {
    static WebDriverWait wait;
    public static String dateName;
    private static final int maxRetryCount = 2;
    public static boolean result = false;
    public static int retry = 0;

    public BaseCommands() {
    }

    //This method is used To enter the URL
    public static void getURL(String inputURL) {
        try {
            driver.get(inputURL);
            test.pass("Entered application URL successfully");
            test.info("Application URL is " + inputURL);
        } catch (Exception var4) {
            failureScenario("Failed to enter application URL ............. " + var4);
        }
    }

    //This method is used To find the element
    public static boolean findElement(By locator) {
        try {
            waitForElement(locator);
            driver.findElement(locator);
            //test.pass("Able to find the element");
        } catch (Exception var4) {
            failureScenario("Finding element failed " + var4);
        }
        return false;
    }

    //This method is used To wait for an element until its located
    public static WebElement waitForElement(By locator) {
        WebElement element = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            //  test.pass("Able to wait for an element");
        } catch (Exception var3) {
            failureScenario("waiting for an element failed " + var3);
        }
        return element;
    }

    //This method is used to wait until an element is clickable
    public static WebElement waitForClickable(By locator) {
        WebElement element;
        while (true) {
            try {
                waitForVisibilityOfElement(locator);
                element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                //test.pass("Able to wait for an element");
                break;
            } catch (StaleElementReferenceException e) {
                waitForElement(locator);
            } catch (Exception var3) {
                failureScenario("waiting for clickable element failed " + var3);
            }
        }
        return element;
    }

    //This method is used to get attribute of specified element
    public static String getAttribute(By locator) {
        try {
            //test.pass("Able to get the getAttribute from element");
            return driver.findElement(locator).getAttribute("title");
        } catch (Exception var4) {
            failureScenario("Failed to retrieve the getAttribute from an element" + var4);
        }
        return null;
    }

    //This method is used To clear the field
    public static void clear(By locator) {
        try {
            waitForClickable(locator).clear();
            //test.pass("Able to clear the input field successfully");
        } catch (Exception var4) {
            failureScenario("Failed to clear the input field .......... " + var4);
        }
    }

    //This method is used To verify that checkbox is checked or not
    public static void checkBoxChecked(By locator) {
        try {
            waitForElement(locator).isSelected();
            test.pass("Able to verify the check box is selected");
        } catch (Exception var4) {
            failureScenario("check box is not selected .......... " + var4);
        }
    }

    //This method is used To verify that expected and actual statements
    public static void AssertEquals(String Actual, String Expected) {
        try {
            Assert.assertEquals(Actual, Expected);
            test.pass("Verified expected and actual message successfully");
        } catch (AssertionError var4) {
            failureScenario("Assertion failed ..........." + var4);
        }
    }

    //To provide the value for input field
    public static void sendKeys(By element, String value, String elementName) {
        while (retry <= 3) {
            try {
                try {
                    //waitForClickable(element).sendKeys(value);
                    waitForVisibilityOfElement(element);
                    driver.findElement(element).sendKeys(value);
                    test.pass("Entered " + value + " for input field " + elementName + " successfully");
                } catch (StaleElementReferenceException e) {
                    //  Thread.sleep(2000);
                    driver.findElement(element).sendKeys(value);
                }
                result = true;
                break;
            } catch (Exception var4) {
                failureScenario("Failed to send the value for input field " + elementName + ".......... " + var4);
            }
        }
    }

    //This method is used to send values and click on it
    public static void sendAndClick(By element, String value) {
        try {
            waitForClickable(element).sendKeys(value);
            driver.findElement(element).click();
            test.pass("Entered " + value + " for input field " + driver.findElement(element).getText() + " successfully");
        } catch (Exception var4) {
            failureScenario("Failed to send the value for input field failed.......... " + var4);
        }
    }

    // This method is used to click on an element
    public static void click(By locator) {
        while (true) {
            try {
                waitForVisibilityOfElement(locator);
                waitForElementToBeClickable(locator);
                try {
                    driver.findElement(locator).click();
                } catch (ElementClickInterceptedException e) {
                    Actions action = new Actions(driver);
                    WebElement element = driver.findElement(locator);
                    action.moveToElement(element).click();
                }
                break;
            } catch (Exception var4) {
                failureScenario("Failed to click the Element......... " + var4);
            }
        }
    }

    public static void click1(By locator) {
        int count = 0;
        int maxTries = 2;
        while (true) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                wait.until(ExpectedConditions.elementToBeClickable(locator));
                try {
                    driver.findElement(locator).click();
                } catch (ElementClickInterceptedException e) {
                    Actions action = new Actions(driver);
                    WebElement element = driver.findElement(locator);
                    action.moveToElement(element).click();
                    count++;
                }
                break;
            } catch (Exception var4) {
                failureScenario("Failed to click the Element......... " + var4);
            }
        }
    }

    //To click the button or element
    public static void click(By locator, String elementName) {
        while (retry <= 3) {
            try {
                waitForElementVisibility(locator);
                waitForElementToBeClickable(locator,elementName);
                try {
                    driver.findElement(locator).click();
                    test.pass("Able to click on the " + elementName + " successfully");
                } catch (ElementClickInterceptedException e) {
                    try {
                        Actions action = new Actions(driver);
                        WebElement element = driver.findElement(locator);
                        action.moveToElement(element).click();
                        test.pass("Able to click the " + elementName + " successfully");
                    } catch (ElementClickInterceptedException d) {
                        Thread.sleep(1500);
                        clickUsingJS(locator, elementName);
                    }
                }
                result = true;
                break;
            } catch (Exception var4) {
                failureScenario("Failed to click the Element........." + elementName + var4);
            }
        }
    }

    //To capture the screenshot
    public static String takeScreenshot(WebDriver driver) {
        dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takeScreenShot = (TakesScreenshot) driver;
        File sourceFile = takeScreenShot.getScreenshotAs(OutputType.FILE);
        String destFile = "./sw_hcm_selenium/screenshots/" + dateName + System.currentTimeMillis() + ".png";
        File Destn = new File(destFile);
        try {
            Files.copy(sourceFile.toPath(), Destn.toPath());
        } catch (IOException e) {
            System.out.println("take screenshot is failed" + e.getMessage());
        }
        return destFile;
    }

    //To add screenshot into the report
    public static void addScreenShotInReport(String Message) {
        String temp = BaseCommands.takeScreenshot(driver);
        test.info(Message, MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
    }

    //This method adds screenshot of entire desktop to the report
    public static void addDesktopScreenShotInReport(String Message) throws
            IOException, AWTException, InterruptedException {
        Thread.sleep(1000);
        String temp1 = BaseCommands.desktopScreenshot();
        test.info(Message, MediaEntityBuilder.createScreenCaptureFromPath(temp1).build());
    }

    // This method is used to Click on any element by using Actions
    public static void clickUsingActions(By locator) {
        try {
            Actions action = new Actions(driver);
            WebElement element = driver.findElement(locator);
            action.moveToElement(element).click();
            test.pass("Able to click on element using actions successfully");
        } catch (Exception e) {
            failureScenario("Failed to click on element using actions .......... " + e);
        }
    }

    //Clicking on an element using JavaScript
    public static void clickUsingJS(By locator) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", driver.findElement(locator));
            test.pass("Able to click on element using javascript executor successfully");
        } catch (Exception e) {
            failureScenario("Failed to click on element using javascript executor .......... " + e);
        }
    }

    //Clicking on an element using JavaScript
    public static void clickUsingJS(By locator, String element) {
        try {
            WebElement ele = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", ele);
            test.pass("Able to click on " + element + " using javascript executor successfully");
        } catch (Exception e) {
            failureScenario("Failed to click on element using javascript executor .......... " + e);
        }
    }

    //Switching to Frame using locators
    public static void switchToFrame(By locator) {
        try {
            waitForElement(locator);
            WebElement frame = driver.findElement(locator);
            driver.switchTo().frame(frame);
//            test.pass("Able to switch into frame successfully");
        } catch (Exception var4) {
            failureScenario("Failed to switching into frame......... " + var4);
        }
    }

    //Switching into the frame using frame number
    public static void switchToFrame(int frameNumber) {
        try {
            driver.switchTo().frame(frameNumber);
            test.pass("Able to switch into the frame successfully");
        } catch (Exception var4) {
            failureScenario("Failed to switching into frame........ " + var4);
        }
    }

    //Switching into the frame using frame name
    public static void switchToFrame(String frameName) {
        try {
            driver.switchTo().frame(frameName);
            test.pass("Able to switch into the frame successfully");
        } catch (Exception var4) {
            failureScenario("Failed to switching into frame........ " + var4);
        }
    }

    // This method is used to switch back to parent frame
    public static void switchToParentFrame() {
        try {
            driver.switchTo().parentFrame();
            test.pass("Able to switch Back into parent frame");
        } catch (Exception var4) {
            failureScenario("Failed to switch Back into parent frame........ " + var4);
        }
    }

    //Switching back from frame
    public static void switchBackFromFrame() {
        driver.switchTo().defaultContent();
    }

    //Switching into new window
    public static void switchToNewTab() {
        try {
            for (String childWindow : driver.getWindowHandles()) {
                driver.switchTo().window(childWindow);
                Thread.sleep(5000);
            }
            test.pass("Switched to new window successfully");
        } catch (Exception var3) {
            failureScenario("Failed to switch to new window " + var3);
        }
    }

    //This method is used to switch to nre window
    public static void switchToNewWindow() {
        try {
            for (String childWindow : driver.getWindowHandles()) {
                driver.switchTo().window(childWindow);
                Thread.sleep(5000);
            }
            test.pass("Switched to new window successfully");
        } catch (Exception var3) {
            failureScenario("Failed to switch to new window " + var3);
        }
    }

    //scroll to middle with Javascript Executor
    public static void scrollToCenter(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
        } catch (Exception e) {
            failureScenario("Failed to scroll........ " + e);
        }
    }

    //scroll into view using javascript executor
    public static void scrollIntoView(By locator) {
        try {
            WebElement element = BaseCommands.waitForVisibilityOfElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            //   test.pass("Able to scrollIntoView to the element");
        } catch (Exception e) {
            failureScenario("ScrollIntoView on the element failed " + e);
        }
    }


    //Wait until the element is visible
    public static WebElement waitForVisibilityOfElement(By locator) {
        WebElement element = null;
        try {
            waitForElement(locator);
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            element = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            // test.pass("Able to wait until the element is visible");
        } catch (Exception e) {
            failureScenario("Failed to find the visible element........ " + e);
        }
        return element;
    }

    //Select value from Dropdown by using visible Text
    public static void dropDownSelectByText(By locator, String visibleText) {
        try {
            waitForClickable(locator);
            Select option = new Select(driver.findElement(locator));
            option.selectByVisibleText(visibleText);
            Thread.sleep(1000);
            test.pass("Able to select the " + visibleText + " in drop down successfully");
        } catch (Exception e) {
            failureScenario("Selecting value from drop down by visible text failed " + e);
        }
    }

    //Select value from Dropdown by using Value
    public static void dropDownSelectByValue(By locator, String value) {
        try {
            waitForVisibilityOfElement(locator);
            Select dataAccess = new Select(driver.findElement(locator));
            dataAccess.selectByValue(value);
            test.pass("Able to select " + value + "in drop down successfully");
        } catch (Exception e) {
            failureScenario("Failed to select the value .......... " + e);
        }
    }

    //Mouse over on a Element
    public static void mouseHover(By locator) {
        try {
            WebElement hoverTo = driver.findElement(locator);
            Actions action = new Actions(driver);
            action.moveToElement(hoverTo).perform();
            //test.pass("Able to mouse hover on the element");
        } catch (Exception e) {
            failureScenario("Mouse hovering on the element failed " + e);
        }
    }

    //Get Text value of an Element
    public static String getText(By locator) {
        String text = null;
        try {
            text = waitForClickable(locator).getText();
            //test.pass("Able to get Text successfully");
        } catch (Exception e) {
            failureScenario("Failed to get Text......... " + e);
        }
        return text;
    }

    // Page Refresh
    public static void refresh() {
        try {
            driver.navigate().refresh();
            //test.pass("Able to refresh the page successfully");
        } catch (Exception var) {
            failureScenario("Failed to refresh the page" + var);
        }
    }

    // Get element attribute value as String by passing attribute value
    public static String getElementAttribute(By locator, String attributeName) {
        String element = null;
        try {
            element = driver.findElement(locator).getAttribute(attributeName);
            // test.pass("Able to get the getAttribute from element");
            // return element;
        } catch (Exception var4) {
            failureScenario("Failed to retrieve the getAttribute from an element" + var4);
        }
        return element;
    }

    //Assert Element attribute value by passing the expected String value
    public static void assertElementAttribute(By locator, String attributeName, String expected) {
        try {
            String actual = getElementAttribute(locator, attributeName);
            Assert.assertEquals(actual, expected);
            //test.pass("Able to Assert element attribute");
        } catch (AssertionError var5) {
            failureScenario("AssertElement Failed......... " + var5);
        }
    }

    //Assert Element attribute value by passing the expected String value by using Css
    public static void assertCssAttribute(By locator, String attribute, String expected) {
        try {
            String actual = getCssAttribute(locator, attribute);
            Assert.assertEquals(actual, expected);
            test.pass("Able to assert the CSS attribute successfully");
        } catch (AssertionError var5) {
            failureScenario("CSS Assert Attribute Failed......... " + var5);
        }
    }

    // Get element attribute value as String by passing attribute value by using Css
    public static String getCssAttribute(By locator, String attribute) {
        String element = null;
        try {
            element = waitForVisibilityOfElement(locator).getCssValue(attribute);
            //test.pass("Able to get CSS attribute successfully");
            //  return element;
        } catch (Exception var4) {
            failureScenario("Failed to get CSS attribute .......... " + var4);
        }
        return element;
    }

    // To assert the page title
    public static void assertTitle(String expected) {
        try {
            Assert.assertEquals(getPageTitle(), expected);
            test.pass("Assert page title successfully");
        } catch (AssertionError var4) {
            failureScenario("Failed to Assert page title .......... " + var4);
        }
    }

    // To get the page title
    public static String getPageTitle() {
        String title = null;
        try {
            title = driver.getTitle();
            // test.pass("Able to get the page title successfully");
        } catch (Exception var4) {
            failureScenario("Failed to get the page title .......... " + var4);
        }
        return title;
    }

    //To wait until ana element is invisible
    public static void waitForElementInvisible(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            // test.pass("Wait for element invisible was successful");
        } catch (Exception var4) {
            failureScenario("Failed to wait for invisibility of an element ............. " + var4);
        }
    }

    //Enter using sendkeys
    public static void enter(By locator) {
        try {
            driver.findElement(locator).sendKeys(Keys.ENTER);
            //  test.pass("Able to enter successfully");
        } catch (Exception var4) {
            failureScenario("Failed to enter .......... " + var4);
        }
    }

    //To split the String
    public static String[] splitString(String account) {
        String[] accountDetails = account.split("[.]");
        return accountDetails;
    }

    //To accept the alert
    public static void switchToAlertAccept() {
        try {
            driver.switchTo().alert().accept();
            // test.pass("Able to switch into alert");
        } catch (Exception var2) {
            failureScenario("switching into alert failed " + var2);
        }
    }

    //To decline the alert
    public static void switchToAlertDecline() {
        try {
            driver.switchTo().alert().dismiss();
            //test.pass("Switching into alert rejected");
        } catch (Exception var2) {
            failureScenario("Failed to reject the alert " + var2);
        }
    }

    //Wait until alert present
    public static void waitForAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            //test.pass("Able to wait until the alert is present");
        } catch (Exception var) {
            failureScenario("Failed to find the alert........ " + var);
        }
    }

    //To verify an element is displayed
    public static boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
//            test.pass("Able to display an element successfully");
        } catch (Exception var4) {
            return false;
        }
    }

    //waits until stale elements visible
    public static void staleWait(By locator) {
        try {
            wait.until(ExpectedConditions.stalenessOf(driver.findElement(locator)));
            //test.pass("stalewait for element is successful");
        } catch (Exception var4) {
            failureScenario("Failed to stale wait for element ............. " + var4);
        }
    }

    //Scroll down using javascript
    public static void scrollDown() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,500)");
        } catch (Exception var) {
            failureScenario("Failed to scroll down ............. " + var);
        }
    }

    //This method is used to scroll up on a page
    public static void scrollUp() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,-500)");
        } catch (Exception var) {
            failureScenario("Failed to scroll up ............. " + var);
        }
    }

    //This method is used to scroll to the element specified
    public static void scroll(By locator) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            WebElement elem = driver.findElement(locator);
            executor.executeScript("arguments[0].scrollIntoView()", elem);
        } catch (Exception e) {
            failureScenario("ScrollIntoView on the element failed " + e);
        }
    }

    //Switching to window with index number 'n'
    public static void switchToWindow(int indexOfWindow) {
        try {
            int tries = 0;
            while (driver.getWindowHandles().size() < 2 && tries < 10) {
                Thread.sleep(2000);
                tries++;
            }
            Set<String> windowHandles = driver.getWindowHandles();
            List<String> windowHandlesList = new ArrayList<>(windowHandles);
            driver.switchTo().window(windowHandlesList.get(indexOfWindow));
            driver.manage().window().maximize();
            test.pass("Able to switch to child window successfully.");
        } catch (Exception var4) {
            failureScenario("Failed to switch window............." + var4);
        }
    }

    //This method is used to scroll to element specified
    public static void scrollToElement(By locator) {
        try {
            waitForElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            WebElement elem = driver.findElement(locator);
            executor.executeScript("arguments[0].scrollIntoView(true)", elem);
            // test.pass("Able to scroll on the element");
        } catch (Exception var4) {
            failureScenario("Mouse scroll on the element failed " + var4);
        }
    }

    //This method decodes the string and passes it to the specified element
    public static void sendSecureKeys(By element, String value, String elementName) {
        try {
            waitForClickable(element);
            WebElement pwd = driver.findElement(element);
            byte[] decodedBytes = Base64.decodeBase64(value.getBytes());
            pwd.sendKeys(new String(decodedBytes));
            test.pass("Entered ******* for input field " + elementName + " successfully");
        } catch (Exception var4) {
            failureScenario("Failed to send the value for input field " + elementName + ".......... " + var4);
        }
    }

    //Waits until the element is enabled
    public static void waitForElementToBeEnabled(By locator) {
        try {
            Thread.sleep(2000);
            int counter = 0;
            while (!driver.findElement(locator).isEnabled() && counter < 5) {
                Thread.sleep(1000);
                counter++;
            }
        } catch (Exception e) {
            failureScenario("Failed to wait for the Element to be Enabled.......... " + e);
        }
    }

    // This method is used to close current browser instance
    public static void closeCurrentBrowser() throws InterruptedException {
        try {
            Thread.sleep(1000);
            driver.close();
        } catch (Exception e) {
            failureScenario("Failed to Close the Current Browser.......... " + e);
        }
    }

    //wait for element visibility for 30 seconds
    public static By waitForElementVisibility(By locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return locator;
    }

    //This method is used to send value to specified element
    public static void sendKeys(By element, String value) {
        while (retry <= 3) {
            try {
                //waitForClickable(element).sendKeys(value);
                waitForVisibilityOfElement(element);
                driver.findElement(element).sendKeys(value);
                //test.pass("Entered "+ value+" for input field successfully");
                result = true;
                break;
            } catch (Exception e) {
                failureScenario("Failed to send the value for input field.......... " + e);
            }
        }
    }

    //To verify files downloaded or not
    public static boolean isFileDownloaded(String downloadPath, String fileName) throws InterruptedException {
        Thread.sleep(3000);
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();
        for (File dir_content : dir_contents) {
            if (dir_content.getName().equals(fileName)) {
                test.pass("File downloaded successfully");
                return true;
            } else {
                failureScenario("Failed to download the file...... ");
            }
        }
        return false;
    }

    //To verify the files downloaded or not with dynamic format
    public static boolean isJobHistoryReportDownloaded(String downloadPath, String fileName) throws
            InterruptedException {
        Thread.sleep(5000);
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();
        for (File dir_content : dir_contents) {
            if (dir_content.getName().contains(fileName)) {
                test.pass("File downloaded successfully");
                return true;
            } else {
                failureScenario("Failed to download the file...... ");
            }
        }
        return false;
    }

    //To clear the directory
    public static void clearDir(String path) {
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            // Assert.fail("failed to clear the directory");
            e.printStackTrace();
        }
    }

    //This method takes desktop screenshot
    public static String desktopScreenshot() throws IOException, AWTException {
        String filePath = "../sw_hcm_selenium/screenshots/" + System.currentTimeMillis() + ".png";
        Robot robot = new Robot();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rectangle = new Rectangle(dimension);
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
        File file = new File(filePath);
        ImageIO.write(bufferedImage, "png", file);
        return filePath;
    }

    //This method is used to open specified file present in the downloads folder
    public static void openFile(String fileName) {
        try {
            driver.get(downloadFolder + fileName);
            Thread.sleep(10000);
//            BaseCommands.addDesktopScreenshotInReport(downloadFolder+fileName,"temp");
            Thread.sleep(6000);
            pressKeyUsingActions("ESCAPE");
            Thread.sleep(5000);
        } catch (Exception e) {
            failureScenario("Failed to view the download file...... " + e);
        }
    }

    // This method is used to press any specified key using actions class
    public static void pressKeyUsingActions(String key) {
        try {
            Actions action = new Actions(driver);
            // action.sendKeys(Keys.ARROW_DOWN)
            action.sendKeys(Keys.valueOf(key)).build().perform();
        } catch (Exception var2) {
            failureScenario("Clicking on" + key + "key failed " + var2);
        }
    }

    public static void cursorWait() {
        if (isElementDisplayed(By.xpath("//body[@style]"))) {
            waitForElementVisibility(By.xpath("//body[@style]"));
            wait = new WebDriverWait(driver, Duration.ofSeconds(40));
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    WebElement cursor = driver.findElement(By.xpath("//body[@style]"));
                    String enabled = cursor.getAttribute("style");
                    if (enabled.contains("cursor: auto;"))
                        return true;
                    else {
                        return false;
                    }
                }
            });
        }
    }

    //This method is used to get name of latest downloaded file
    public static String getLatestFilefromDir() {
        File dir = new File(System.getProperty("user.dir") + "\\sw_hcm_selenium\\downloads\\");
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Inside if loop");
            return null;
        }
        System.out.println("After file check");
        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
                System.out.println(">>files[i]");
            }
        }
        return lastModifiedFile.getName();
    }

    public static void addDesktopScreenshotInReport(String Message) throws
            IOException, AWTException {
        String temp = BaseCommands.desktopScreenshot();
        test.info(Message, MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
    }

    //This method checks whether the expected element is displayed or not
    public static void assertElementDisplayed(By locator, boolean expected, String name) {

        try {
            boolean actual = isElementDisplayed(locator);
            Assert.assertEquals(actual, expected);
            test.pass(name + " is Displayed");
        } catch (Exception var4) {
            failureScenario("AssertElement Display Failed......... " + var4);
        }
    }

    //This method is used to zoom the webpage by specified percentage
    public static void zoomInOutWebPage(String percentage) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.body.style.zoom = '" + percentage + "';");
    }

    // This method checks whether the alert is present
    public static boolean isAlertPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10) /*timeout in seconds*/);
        return wait.until(ExpectedConditions.alertIsPresent()) != null;
    }


    //This method is used to get date and time as a string value
    public static String getDateAndTimeStamp() {
        return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    }

    //This method opens file containing a few specified characters
    public static void openFileContainingName(String fileName) {
        try {
            File downloadsDir = new File(downloadFolder);
            File[] downloadDirFiles = downloadsDir.listFiles();
            String actualName;
            if (downloadDirFiles == null) throw new AssertionError();
            for (File file : downloadDirFiles) {
                actualName = file.getName();
                if (actualName.contains(fileName)) {
                    driver.get(downloadFolder + actualName);
                }
            }
            Thread.sleep(6000);
            pressKeyUsingActions("ESCAPE");
            Thread.sleep(5000);
        } catch (Exception e) {
            failureScenario("Failed to view the download file...... " + e);
        }
    }

    //This method is used to double-click on specified element
    public static void doubleClick(By locator) {
        try {
            waitForVisibilityOfElement(locator);
            Actions actions = new Actions(driver);
            WebElement elementLocator = driver.findElement(locator);
            actions.doubleClick(elementLocator).perform();
            test.pass("Able to clear the input field successfully");
        } catch (Exception e) {
            failureScenario("Failed to clear the input field .......... " + e);
        }
    }

    public static void doubleClick(By locator, String element) {
        try {
            waitForVisibilityOfElement(locator);
            Actions actions = new Actions(driver);
            WebElement elementLocator = driver.findElement(locator);
            actions.doubleClick(elementLocator).perform();
            test.pass("Able to double click the " + element + " successfully");
        } catch (Exception e) {
            failureScenario("Failed to clear the input field .......... " + e);
        }
    }

    public static void changeExtension(String extension1, String extension2) {
        File file = new File(System.getProperty("user.dir") + "/sw_hcm_selenium/downloads/" + extension1);
        File file2 = new File(System.getProperty("user.dir") + "/sw_hcm_selenium/downloads/" + extension2);
        boolean success = file.renameTo(file2);
        if (success) {
            test.pass("File extension has been renamed");
        } else {
            failureScenario("Failed to rename the file extension...... ");
        }
    }

    public static void failureScenario(String failureMessage) {
        String temp = takeScreenshot(driver);
        test.fail(failureMessage, MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
        Assert.fail(failureMessage);
    }

    public static void createDirectory(String directory) {
        File file = new File(System.getProperty("user.dir") + "/sw_hcm_selenium/" + directory);
        file.mkdir();
    }

    //This method is used to open a new tab.
    public static void openNewTab() {
        try {
            driver.switchTo().newWindow(WindowType.TAB);
            //test.pass("Open New Tab successfully");
        } catch (Exception e) {
            failureScenario("Failed to Open a new tab.......... " + e);
        }
    }

    // Validating the title by passing page name
    public static void validateTitle(String PageName) {
        try {
            driver.getTitle().contains(PageName);
            //test.pass("Validated title successfully");
        } catch (Exception var4) {
            failureScenario("Failed to validate the title .......... " + var4);
        }
    }

    public static void switchTONewTab() {
        try {
            driver.switchTo().newWindow(WindowType.TAB);
        } catch (Exception e) {
            failureScenario("Failed to Open a new tab.......... " + e);
        }
    }

    //To provide the value for input field
    public static void clearAndSendKeys(By locator, String value, String elementName) {
        try {
            waitForVisibilityOfElement(locator);
            clear(locator);
            driver.findElement(locator).sendKeys(value);
            test.pass("Entered " + value + " for input field " + elementName + " successfully");
        } catch (StaleElementReferenceException e) {
            driver.findElement(locator).sendKeys(value);
        } catch (Exception var4) {
            failureScenario("Failed to send the value for input field " + elementName + ".......... " + var4);
        }
    }


    public static String getSelectedOption(By locator) {
        String selectedOption = null;
        try {
            waitForVisibilityOfElement(locator);
            Select select = new Select(driver.findElement(locator));
            selectedOption = select.getFirstSelectedOption().getText().trim();
        } catch (Exception var4) {
            failureScenario("Failed to capture the selected value  .......... " + var4);
        }

        return selectedOption;
    }

    public static String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public static Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public static void moveToElementAndClick(By locator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(locator)).click().build().perform();
    }

    public static By waitForElementToBeClickable(By locator) {
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception var4) {
            failureScenario("Failed to capture the selected element as Clickable  " + var4);
        }
        return locator;
    }

    public static By waitForElementToBeClickable(By locator, String element) {
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception var4) {
            failureScenario("Failed to capture the selected element as Clickable  " + element+var4);
        }
        return locator;
    }

    public static void scrollHorizontal(String id) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('" + id + "').scrollLeft += 350", "");
    }

    public static List<WebElement> findElements(By locator) {
        List<WebElement> listOfWebElements = new ArrayList<>();
        try {
            //waitForElement(locator);
            listOfWebElements = driver.findElements(locator);
            //test.pass("Able to find the element");
        } catch (Exception var4) {
            System.out.println(">>Exception: " + var4);
            failureScenario("Finding element failed " + var4);
        }
        return listOfWebElements;
    }

    public static void moveToElement(By locator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(locator)).build().perform();
    }

    //
    public static int numberOfRowsInsideTable(By locator) {
        waitForVisibilityOfElement(locator);
        WebElement table = driver.findElement(locator);
        List<WebElement> rows = table.findElements(By.xpath("./tr"));
        return rows.size();
    }


    public static void zoomIn() throws AWTException {
        Robot robot = new Robot();
        for (int i = 0; i < 4; i++) {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ADD);
            robot.keyRelease(KeyEvent.VK_ADD);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
    }

    public static void zoomOut() throws  AWTException {
        Robot robot = new Robot();
        for (int i = 0; i < 4; i++) {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
    }

    public static Boolean isCheckBoxChecked(By locator) {
        boolean flag = false;
        try {
            flag = waitForElement(locator).isSelected();
        } catch (Exception var4) {
            failureScenario("Finding element failed " + var4);
        }
        return flag;
    }

    public static void rightClick(By locator, String element) {
        while (true) {
            try {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//                wait.until(ExpectedConditions.elementToBeClickable(locator));
                waitForVisibilityOfElement(locator);
                waitForElementToBeClickable(locator);
                try {
                    Actions actions = new Actions(driver);
                    WebElement elementLocator = driver.findElement(locator);
                    actions.contextClick(elementLocator).perform();
                    test.pass("Able to right click on " + element + " successfully");
                } catch (ElementClickInterceptedException e) {
                    Actions actions = new Actions(driver);
                    WebElement elementLocator = driver.findElement(locator);
                    actions.contextClick(elementLocator).perform();
                }
                break;
            } catch (Exception var4) {
                failureScenario("Failed to right click on the Element......... " + var4);
            }
        }
    }
    public static void setSmallWait() throws InterruptedException {
        Thread.sleep(smallWait);
    }
    public static void setLongWait() throws InterruptedException {
        Thread.sleep(longWait);
    }
    public static void setShortWait() throws InterruptedException {
        Thread.sleep(shortWait);
    }
    public static void setVerylongWait() throws InterruptedException {
        Thread.sleep(veryLongWait);
    }
}