package UiTests.base;


import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
public class RemoteDriverProvider implements WebDriverProvider {

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        String browser = capabilities.getBrowserName(); // chrome / firefox



        try {
            if ("chrome".equals(browser)) {
                ChromeOptions options = new ChromeOptions()
                        .addArguments("--window-size=1920,1080",
                                "--no-sandbox",
                                "--disable-dev-shm-usage");

                File uBlock = new File("C:\\Users\\Sergey\\IdeaProjects\\TestNgGridDocker\\CJPALHDLNBPAFIAMEJDNHCPHJBKEIAGM_1_65_0_0.crx");
                if (uBlock.exists()) options.addExtensions(uBlock);

                return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
            }

            if ("firefox".equals(browser)) {
                FirefoxOptions options = new FirefoxOptions()
                        .addArguments("--width=1920", "--height=1080");
                return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
            }

            throw new IllegalArgumentException("Unsupported browser: " + browser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}