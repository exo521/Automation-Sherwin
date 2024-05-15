package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class LoginOCOV extends PageObject { //

    @FindBy(xpath = "//*[contains(text(),'Email Add')]/parent::label/following-sibling::input")
    WebElement userEmail;

    @FindBy(xpath = "//*[@id='gigya-password-88773973433502080']")
    WebElement passwordEle;

    @FindBy(xpath = "//input[@value='LOGIN']")
    WebElement loginBtn;


    public LoginOCOV(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void loginPro(String email, String password) {

        if(!driver.findElements(By.xpath("//button[@id='ensAcceptBanner']")).isEmpty()){
            System.out.println("adentro del if");
            driver.findElement(By.xpath("//button[@id='ensAcceptBanner']")).click();
        }
        userEmail.sendKeys(email);
        passwordEle.sendKeys(password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
        loginBtn.click();
    }

    public boolean loginValidation(String email, String password) {
        int flag = 0;
        userEmail.sendKeys(email);
        passwordEle.sendKeys("WrongPassword");
        loginBtn.click();
        if (driver.findElement(By.xpath("//div[contains(text(),'Invalid login or password')]")).isDisplayed()) {
            flag = flag + 1;
        }
        userEmail.clear();
        passwordEle.clear();
        userEmail.sendKeys("Wrong user");
        passwordEle.sendKeys(password);
        if (driver.findElement(By.xpath("//div[contains(text(),'Invalid login or password')]")).isDisplayed()) {
            flag = flag + 1;
        }
        userEmail.clear();
        passwordEle.clear();
        if (driver.findElement(By.xpath("(//*[contains(text(),'This field is required')])[1]")).isDisplayed()) {
            if (driver.findElement(By.xpath("(//*[contains(text(),'This field is required')])[2]")).isDisplayed()) {
                flag = flag + 1;
            }
        }
        if (flag == 3) {
            return true;
        } else {
            return false;
        }

    }
}
