package com.softserve.edu01sel.edu.edu01se;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class Basic_Selenium {
    private static final String BASE_URL = "https://demo.opencart.com/index.php";
    private static final String TIME_TEMPLATE = "yyyy-MM-dd_HH-mm-ss-S";
    private static WebDriver driver;
    private boolean isTestSuccessful = true;

    private void takeScreenShot(String testname) {
        String currentTime = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("./" + currentTime + "_" + testname + "_screenshot.png"));
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    private void takePageSource(String testname) {
        String currentTime = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        String pageSource = driver.getPageSource();
        byte[] strToBytes = pageSource.getBytes();
        Path path = Paths.get("./" + currentTime + "_" + testname + "_source.html");
        try {
            Files.write(path, strToBytes, StandardOpenOption.CREATE);
        } catch (IOException e) {

        }
    }

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().window().maximize();
        System.out.println("@BeforeAll executed");
    }

    @AfterAll
    public static void tear() {
        if (driver != null) {
            driver.quit(); // close()
        }
        System.out.println("@AfterAll executed");
    }

    @BeforeEach
    public void setupThis() {
        isTestSuccessful = false;
        driver.get(BASE_URL);
        System.out.println("\t@BeforeEach executed");
    }


    @AfterEach
    public void tearThis(TestInfo testInfo) throws InterruptedException {
        System.out.println("\t@AfterEach executed, name = " + testInfo.getDisplayName());
        //
        if (!isTestSuccessful) {
            takeScreenShot(testInfo.getDisplayName());
            takePageSource(testInfo.getDisplayName());
            System.out.println("\t\t@AfterEach, ERRROR name = " + testInfo.getDisplayName());
        }
    }

    private WebElement getProductByName(String expectedName) {
        WebElement result = null;
        List<WebElement> containers = driver.findElements(By.cssSelector("div#content div.col"));
        for (WebElement current : containers) {
            if (current.findElement(By.cssSelector("h4 > a")).getText().equals(expectedName)) {
                result = current;
                break;
            }
        }
        if (result == null) {
            // Develop Custom Exception
            throw new RuntimeException("WebElement by title/name: " + expectedName + " not found");
        }
        return result;
    }

    String expectedPrice = "111.55€";
    String expectedName = "iMac";


    @Test
    public void findImac() {

        // Choose Curency
        driver.findElement(By.cssSelector("a.dropdown-toggle[href='#']")).click();

        WebElement eur = driver.findElement(By.cssSelector("a[href='EUR']"));
        System.out.println("\t\tEUR.isDisplayed = " + eur.isDisplayed());
        driver.findElement(By.cssSelector("a[href='EUR']")).click();

        driver.findElement(By.xpath("//a[contains(text(),'Desktops')]")).sendKeys(Keys.ENTER);
        WebElement mac = driver.findElement(By.xpath("//a[contains(text(),'Mac')]"));
        System.out.println("\t\tPage 'Mac' isDisplayed =" + mac.isDisplayed());
        driver.findElement(By.xpath("//a[contains(text(),'Mac')]")).click();

        WebElement price = getProductByName(expectedName).findElement(By.cssSelector("span.price-new"));

        System.out.println("\t\tprice.getText() = " + price.getText());
        //
        // Scrolling by Actions
        Actions action = new Actions(driver);
        action.moveToElement(price).perform();

        String actualPrice = price.getText();
        if (actualPrice.equals(expectedPrice)) {
            System.out.println("Price is correct " + actualPrice);
        } else {
            System.out.println("Price is incorrect: Actual price " + actualPrice + " Differs from expected price " + expectedPrice);
        }

        isTestSuccessful = true;
    }

}








