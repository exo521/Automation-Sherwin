package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import sample.SherwinHomePage;

/**
 * This is an example of a test class where you can define your Selenium test scenarios.
 * Each test class typically contains several related test scenarios
 *
 * --------------------------------------------
 * Scaling
 * --------------------------------------------
 * As you add additional test classes you may find you want to run them in certain groupings (ex. Smoke tests).
 * You can use both TestNG Suite files (XML file) and TestNG groups to accomplish this.
 */
public class SampleTest extends BaseSeleniumTest {

    private WebDriver driver;

    /**
     * Setup all of our needed configuration before anything else gets executed.
     */
    @BeforeSuite
    public void setup() {
        setupConfig();
    }

    /**
     * Launch the WebDriver and navigate to our initial page that all the tests in this class expect.
     */
    @BeforeMethod
    public void launch() {
        // Defer driver creation to the Base class since the test shouldn't care what kind of WebDriver we are using.
        driver = createDriver();

        // Navigate to the SW home page.
        driver.get("https://www.sherwin-williams.com/");

        /*
        --------------------------------------------
        Scaling
        --------------------------------------------
        - Once you have multiple test classes you'll probably want to create a function elsewhere that
            launches the WebDriver, navigates to the page and potentially logs in so this code isn't
            copy and pasted in each of your test classes.
        - Try to avoid moving TestNG annotations into the base class (ex. BeforeSuite, BeforeMethod, etc.) as this can
            introduce additional complexity with the execution order of these annotations.
            Especially if there is the same annotation defined at multiple inheritance levels.
        */
    }

    /**
     * This is an example test scenario.
     * Each test scenario should be self-contained and not rely on any other test.
     */
    @Test
    public void shouldDoSomething() {
        // Create our page object and pass the WebDriver to it, so we don't need to interact with WebElements in this layer.
        var homePage = new SherwinHomePage(driver);

        // Call our method that starts going through the test steps.
        var success = homePage.shouldGoToHomeownersPage();

        // Asserts should only exist in this Test class and NOT in the Page Object.
        // With asserts being restricted to Test classes, it gives your Page Objects more utility.
        Assert.assertTrue(success, "Homeowner Page Navigation");
    }

    /**
     * Make sure to quit the WebDriver when the test is over otherwise the WebDriver will be running in the background.
     */
    @AfterTest(alwaysRun = true)
    public void teardown() {
        quit(driver);
    }

}
