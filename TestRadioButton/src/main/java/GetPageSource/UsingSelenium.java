package GetPageSource;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class UsingSelenium extends ProcessGetPageSource {
    public static String getHtmlContent(String linkHtml) {
//    System.setProperty("Webdriver.chrome.driver", "C:\\webdriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(linkHtml);

        String htmlContent = driver.findElement(By.tagName("body")).getText();
//        String htmlContent = driver.getPageSource();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
//        System.out.println(wait.until(ExpectedConditions.jsReturnsValue("return document.readyState;")).toString());
//        String htmlContent = driver.getPageSource();
        driver.quit();
        return htmlContent;
    }

//    public static void main(String[] args) {
//        String linkHtml = "https://form.jotform.com/233591282365460";
//        String content = getHtmlContent(linkHtml);
//        try {
//            writeHtmlContentToFile(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



}
