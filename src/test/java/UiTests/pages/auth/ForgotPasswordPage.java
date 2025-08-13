package UiTests.pages.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPasswordPage {

    private static final String URL_FORGOT_PASSWORD =
            "https://practice.expandtesting.com/forgot-password";
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By emailField        = By.id("email");
    private final By retrievePassword  = By.cssSelector("button[type='submit']");
    private final By flashMessage      = By.cssSelector("div[id='confirmation-alert'] p");
    private final By invalidEmailMsg   = By.cssSelector(".ms-1.invalid-feedback");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL_FORGOT_PASSWORD);
    }

    public void sendResetPasswordEmail(String email) {
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailEl.clear();
        emailEl.sendKeys(email);

        driver.findElement(retrievePassword).click();
    }

    public String getFlashMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(flashMessage)).getText();
    }

    public String getInvalidMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(invalidEmailMsg)).getText();
    }

    public void sendResetEmailByEnter(String email) {
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailEl.clear();
        emailEl.sendKeys(email, Keys.ENTER);
    }

    public void sendResetEmailByTabSpace(String email) {
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailEl.clear();
        emailEl.sendKeys(email, Keys.TAB);

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(retrievePassword));
        new Actions(driver).sendKeys(btn, " ").perform();
    }
}