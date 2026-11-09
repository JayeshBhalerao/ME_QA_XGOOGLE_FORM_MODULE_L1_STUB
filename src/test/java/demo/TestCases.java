package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {

    ChromeDriver driver;
    Wrappers wrapper;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */

    @Test
    public void testCase01() throws InterruptedException{

        wrapper.navigateURL("https://forms.gle/wjPkzeSEk1CM7KgGA");
        wrapper.waitForPageLoad();
        wrapper.enterText("Why are you practicing Automation?", "I want to be the best QA Engineer! ");
        wrapper.enterText("Name", "Crio Learner");
        wrapper.radioButton("9");
        wrapper.checkBox("jAva, seleNium, TestNG");
        wrapper.dropDown("Mr");
        wrapper.date();
        wrapper.time("7", "30");
        // wrapper.time("7", "30", "AM");
        wrapper.submit();
        wrapper.waitForPageLoad();
        wrapper.successfullCompletion("Thanks");


    }



    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        wrapper = new Wrappers(driver);
    }

    // @AfterTest
    // public void endTest()
    // {
    //     driver.close();
    //     driver.quit();

    // }
}