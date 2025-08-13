package UiTests.tests.auth;

import UiTests.base.UiBaseTest;
import UiTests.pages.auth.ForgotPasswordPage;
import UiTests.utils.FlashMessage;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Authentication")
@Feature("Password recovery")
@Owner("SergeyQA")
@Severity(SeverityLevel.CRITICAL)
@Listeners({AllureTestNg.class})

public class ForgotPasswordTests extends UiBaseTest {

    private ForgotPasswordPage forgotPassword;

    @BeforeMethod
    public void initPage() {
        forgotPassword = new ForgotPasswordPage(driver);
    }

    @Test(description = "Send password reset request to valid email", priority = 1)
    @Story("Trigger request via button")
    public void validSendingResetPassword() {
        String email = new Faker().internet().emailAddress();

        forgotPassword.open();
        forgotPassword.sendResetPasswordEmail(email);

        assertThat(forgotPassword.getFlashMessage())
                .containsIgnoringCase(FlashMessage.SEND_FORGOT_PASSWORD.text());
    }

    @Test(description = "Send password reset request using Enter", priority = 2)
    @Story("Trigger request via Enter")
    @Severity(SeverityLevel.NORMAL)
    public void sendingResetPasswordByEnter() {
        forgotPassword.open();
        forgotPassword.sendResetEmailByEnter("user@example.com");

        assertThat(forgotPassword.getFlashMessage())
                .containsIgnoringCase(FlashMessage.SEND_FORGOT_PASSWORD.text());
    }

    @Test(description = "Send password reset request via Tab → Space", priority = 3)
    @Story("Trigger request via Tab + Space")
    @Severity(SeverityLevel.MINOR)
    public void sendingResetPasswordByTabSpace() {
        forgotPassword.open();
        forgotPassword.sendResetEmailByTabSpace("user@example.com");

        assertThat(forgotPassword.getFlashMessage())
                .containsIgnoringCase(FlashMessage.SEND_FORGOT_PASSWORD.text());
    }

    @Test(dataProvider = "negativeForgotPasswordCasesProvider",
            description = "Negative Forgot Password test cases", priority = 4)
    @Story("Negative email validation")
    @Severity(SeverityLevel.NORMAL)
    public void negativeForgotPassword(String description, String email, String expectedMessage) {
        forgotPassword.open();
        forgotPassword.sendResetPasswordEmail(email);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(forgotPassword.getInvalidMessage())
                .as("Error message for case: %s", description)
                .containsIgnoringCase(expectedMessage);
        softly.assertAll();
    }

    @DataProvider(name = "negativeForgotPasswordCasesProvider")
    public Object[][] negativeForgotPasswordCasesProvider() {
        return new Object[][]{
                {"XSS <script>", "<script>alert(1)</script>@test.com", "Please enter a valid email address."},
                {"XSS img tag", "<img src=x onerror=alert(1)>@test.com", "Please enter a valid email address."},
                {"XSS javascript:", "javascript:alert(1)@test.com", "Please enter a valid email address."},
                {"SQL OR 1=1", "' OR 1=1--@test.com", "Please enter a valid email address."},
                {"SQL UNION", "admin' UNION SELECT * FROM users--@test.com", "Please enter a valid email address."},
                {"SQL DROP", "test'; DROP TABLE users;--@test.com", "Please enter a valid email address."},
                {"Missing @ symbol", "invalidemail.com", "Please enter a valid email address."},
                {"Double @ symbol", "user@@example.com", "Please enter a valid email address."},
                {"Email with spaces", "user name@example.com", "Please enter a valid email address."},
                {"Numeric-only input", "12345", "Please enter a valid email address."},
                {"Empty input", "", "Please enter a valid email address."},
                {"255 characters", "a".repeat(64) + "@" + "b".repeat(190) + ".com", "Please enter a valid email address."},
                {"Very long domain", "a@" + "b".repeat(300) + ".com", "Please enter a valid email address."},
                {"Special characters", "user<>?@example.com", "Please enter a valid email address."}
        };
    }
}