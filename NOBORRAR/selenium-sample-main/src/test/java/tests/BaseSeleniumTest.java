package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public abstract class BaseSeleniumTest {

    public final static String PROPERTY_FILE_NAME = "test.properties";
    protected Optional<URL> optionalGridUrl;

    public BaseSeleniumTest() {
        optionalGridUrl = Optional.empty();
    }

    protected void setupConfig()  {

        try {
            var propertyInputStream = this.getClass().getClassLoader()
                    .getResourceAsStream(PROPERTY_FILE_NAME);

            if (propertyInputStream == null)
                throw new IllegalArgumentException("Could not find the properties file in order to get the target environment.");

            var testProperties = new Properties();
            testProperties.load(propertyInputStream);

            // Check to see if the grid parameter was passed in via command line to determine whether we want to
            //      driver the configuration of the WebDriver that we need.
            var localExecution = System.getProperty("grid") == null;
            if (localExecution) {
                // Grab the Operating System specific path.
                Path chromeDriverPath;
                if (System.getProperty("os.name").toLowerCase().contains("win"))
                    chromeDriverPath = Path.of(testProperties.getProperty("windowsChromeDriverPath"));
                else
                    chromeDriverPath = Path.of(testProperties.getProperty("macChromeDriverPath"));

                if (chromeDriverPath.toString().isBlank())
                    throw new IllegalArgumentException("Could not find the ChromeDriver path stored in the '"
                            + PROPERTY_FILE_NAME + "' file.");

                System.setProperty("webdriver.chrome.driver", chromeDriverPath.toString());
            } else {
                optionalGridUrl = Optional.of(new URL(testProperties.getProperty("seleniumGrid")));
            }

        }
        catch (IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * Create WebDriver to kick off our test.
     * This should be run after the setup function.
     * @return The WebDriver instance.
     */
    protected WebDriver createDriver() {
        var chromeOptions = new ChromeOptions();

        if(optionalGridUrl.isPresent())
            return new RemoteWebDriver(optionalGridUrl.get(), chromeOptions);
        else
            return new ChromeDriver(chromeOptions);
    }

    /**
     * Quit WebDriver
     * @param driver The WebDriver
     */
    protected void quit(WebDriver driver) {
        // Sometimes the driver may be null because there was a problem in creating the driver initially.
        if(driver != null)
            driver.quit();
    }
}
