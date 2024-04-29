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

public class WebDriver_Waits {
    private static final String BASE_URL = "https://devexpress.github.io/devextreme-reactive/react/grid/docs/guides/filtering/";
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
            //throw new RuntimeException(e);
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
        // TODO
        // Close Session
//        System.out.println("\t@AfterEach executed");

    }


    @Test
    public void citiesLondonLasVegas() {
        WebElement acceptButton = null;
        try {
            acceptButton = driver.findElement(By.xpath("//button[text()='I Understand']"));
        } catch (NoSuchElementException e) {
            // Если кнопка не найдена, продолжить выполнение основного кода
        }
// Если кнопка найдена, кликнуть на неё
        if (acceptButton != null) {
            acceptButton.click();
        }

        Actions action = new Actions(driver);
        WebElement mode = driver.findElement(By.id("controlled-mode"));
        action.moveToElement(mode).perform();

        WebElement iframeElement = driver.findElement(By.tagName("iframe"));
// Переключитесь на контекст фрейма
        driver.switchTo().frame(iframeElement);
        List<WebElement> inputElements = driver.findElements(By.cssSelector("input.MuiInputBase-input.MuiInput-input.Editor-input.css-mnn31"));
// Проверить, есть ли хотя бы три элемента
        if (inputElements.size() >= 3) {
            // Найти третий элемент в списке
            WebElement thirdInputElement = inputElements.get(2);
            // Ввести букву "L" в третий элемент списка
            thirdInputElement.sendKeys("L");
        } else {
            System.out.println("ThirdInput is existed");
        }

        WebElement lasVegasElement = driver.findElement(By.xpath("//td[contains(text(),'Las Vegas')]"));
// Проверяем, существует ли элемент с указанным текстом города
        if (lasVegasElement != null) {
            System.out.println("Город Las Vegas найден в списке.");
        } else {
            System.out.println("Город Las Vegas не найден в списке.");
        }

        WebElement londonElement = driver.findElement(By.xpath("//td[contains(text(),'London')]"));
// Проверяем, существует ли элемент с указанным текстом города
        if (londonElement != null) {
            System.out.println("Город London найден в списке.");
        } else {
            System.out.println("Город London не найден в списке.");
        }
        driver.switchTo().defaultContent();

        isTestSuccessful = true;

    }
}

