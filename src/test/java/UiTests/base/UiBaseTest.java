package UiTests.base;

import UiTests.utils.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class UiBaseTest {
    protected WebDriver driver;
    @Parameters("browserName")
    @BeforeTest
    public void setUp(String browserName) throws MalformedURLException {
        URL hub = new URL("http://localhost:4444/wd/hub");

        System.out.println(">>> Starting browser: " + browserName);

        switch (browserName.toLowerCase()) {
            case "chrome"  -> driver = new RemoteWebDriver(hub, new ChromeOptions());
            case "firefox" -> driver = new RemoteWebDriver(hub, new FirefoxOptions());
            default -> throw new IllegalArgumentException(browserName);
        }
        driver.manage().window().maximize();
    }


    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}