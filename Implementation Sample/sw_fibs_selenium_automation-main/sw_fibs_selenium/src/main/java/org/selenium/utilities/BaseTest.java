package org.selenium.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.selenium.utilities.ZephyrIntegration.setUpTestCycle;
import static org.selenium.utilities.ZephyrIntegration.zephyrTestResult;

@Listeners(ListenersImplementation.class)
public class BaseTest {
    public static WebDriver driver;
    public static EdgeOptions edgeOptions;
    public static ChromeOptions chromeOptions;
    public static FirefoxOptions firefoxOptions;
    private static final ReadProperty readProperty = ReadProperty.getInstance();
    public final String URL = readProperty.readProperties("Url");
    public static final String testDataExcel = System.getProperty("user.dir") + readProperty.readProperties("testDataPath");
    public static final String user = readProperty.readProperties("userName");
    public static final String password = readProperty.readProperties("password");
    public static String downloadFolder = System.getProperty("user.dir") + "\\downloads\\";
    public static final String GridURL = readProperty.readProperties("GridUrl");
    public static final String browser = readProperty.readProperties("browserName");
    public static String testCycKey = "";
    public static final String remoteFlag = readProperty.readProperties("remoteExecution");
    public static final String jiraURL = readProperty.readProperties("jiraURL");
    public static final String jiraUserName = readProperty.readProperties("jiraUserName");
    public static final String jiraAPIToken = readProperty.readProperties("jiraAPIToken");
    public static final String jiraFlag = readProperty.readProperties("jiraFlag");

    public static final String zephyrProjectKey = readProperty.readProperties("zephyrProjectKey");
    public static final String cycleKey = readProperty.readProperties("testCycleKey");
    public static int smallWait=5000;
    public static int shortWait=2000;
    public static int longWait=8000;
    public static int veryLongWait=15000;
    public BaseTest() {
    }

    //Clears Screenshot directory
    @BeforeSuite
    public void clearScreenshotDir() {
        BaseCommands.clearDir(System.getProperty("user.dir") + "/screenshots");
    }

    //Clears Downloads directory
    @BeforeSuite
    public void clearDownloadDir() {
        BaseCommands.clearDir(System.getProperty("user.dir") + "/downloads");
    }

    @BeforeMethod
    public void initBrowser() throws MalformedURLException {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            System.setProperty("webdriver.http.factory", "jdk-http-client");
            getChromeBrowserOptions();
            if (remoteFlag.equalsIgnoreCase("Yes")) {
                driver = new RemoteWebDriver(new URL(GridURL), chromeOptions);
            } else {
                driver = new ChromeDriver(chromeOptions);
            }
        }
        if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            getEdgeBrowserOptions();
            if (remoteFlag.equalsIgnoreCase("Yes")) {
                driver = new RemoteWebDriver(new URL(GridURL), edgeOptions);
            } else {
                driver = new EdgeDriver(edgeOptions);
            }
        }
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            getFirefoxBrowserOptions();
            if (remoteFlag.equalsIgnoreCase("Yes")) {
                driver = new RemoteWebDriver(new URL(GridURL), firefoxOptions);
            } else {
                driver = new FirefoxDriver(firefoxOptions);
            }
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @BeforeSuite
    public void createDirectory() throws Exception {
        BaseCommands.createDirectory("screenshots");
        BaseCommands.createDirectory("downloads");
        setUpTestCycle();
    }


    @AfterMethod
    protected void tearDown(ITestResult result, Method method) throws IOException, InterruptedException {
        // Sometimes the driver may be null because there was a problem in creating the driver initially.
        if (driver != null)
            driver.quit();
        long time = result.getEndMillis()-result.getStartMillis();
        System.out.println("Time taken to run test is :"+time+" miliiseconds");
        zephyrTestResult(result,method,time);

    }

    public static int getWait() {
        int wait = 5;
        return wait;
    }

    protected void setWindowSize(int height, int width) {
        Dimension targetSize = new Dimension(width, height);
        driver.manage().window().setSize(targetSize);
    }

    public void getChromeBrowserOptions() {
        chromeOptions = new ChromeOptions();
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.default_directory", downloadFolder);
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");
         chromeOptions.addArguments("--remote-allow-origins=*");
    }

    public void getEdgeBrowserOptions() {
        edgeOptions = new EdgeOptions();
        HashMap<String, Object> edgePrefs = new HashMap<String, Object>();
        edgePrefs.put("download.default_directory", downloadFolder);
        edgePrefs.put("download.prompt.for_download", false);
        edgePrefs.put("plugins.always_open_pdf_externally", true);
        edgeOptions.setExperimentalOption("prefs", edgePrefs);
        edgeOptions.setAcceptInsecureCerts(true);

    }

    public void getFirefoxBrowserOptions() {
        firefoxOptions = new FirefoxOptions();
        HashMap<String, Object> firefoxPrefs = new HashMap<String, Object>();
        firefoxPrefs.put("download.default_directory", downloadFolder);
        firefoxPrefs.put("download.prompt.for_download", true);
        firefoxPrefs.put("plugins.always_open_pdf_externally", true);
        firefoxOptions.setAcceptInsecureCerts(true);
        //FirefoxProfile ffprofile = new FirefoxProfile();
    }
}
