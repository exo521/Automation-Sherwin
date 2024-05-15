package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class HomeOCOV extends PageObject{

    @FindBy(xpath="(//input[@placeholder='MM/DD/YYYY'])[1]")
    WebElement startDate;

    @FindBy(xpath="(//input[@placeholder='MM/DD/YYYY'])[2]")
    WebElement finishDate;

    @FindBy(xpath="//*[@id='account-select']")
    WebElement accountSelect;


    public HomeOCOV(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean verifyAccounts(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(startDate));
        String acc=driver.findElement(By.xpath("//*[@id='account-select']")).getText();
        accountSelect.click();
        List<String> a = new ArrayList<>();
        List<WebElement> accounts=driver.findElements(By.xpath("//ul[@role='listbox']/li"));
        System.out.println("The user have: " +accounts.size()+" account(s)");
        System.out.println(acc);
        for(int i=0;i<accounts.size();i++){
            System.out.println("Account displayed in the Combobox: "+accounts.get(i).getText());
        }
        return true;
    }


}


