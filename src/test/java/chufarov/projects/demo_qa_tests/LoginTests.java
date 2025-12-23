package chufarov.projects.demo_qa_tests;

import chufarov.projects.api_classes.AccountApiDemoQa;
import chufarov.projects.models.response.AuthResponseDemoQa;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@Owner("Chufarov Bogdan")
@Story("Композиция API и UI тестов")
@Tag("smoke")
@DisplayName("Гибридные тесты (API + UI)")
public class LoginTests extends TestConfig {

    private final AccountApiDemoQa accountApiDemoQa = new AccountApiDemoQa();

    @DisplayName("Simple UI login test")
    @Severity(SeverityLevel.MINOR)
    @Test
    void successfulLoginWithUITest() {
        open("/login");
        $("#userName").setValue(userName);
        $("#password").setValue(password);
        $("#login").click();
        $("#userName-value").shouldHave(text(userName));
    }

    @DisplayName("Login test with API and verification with UI")
    @Severity(SeverityLevel.NORMAL)
    @Test
    void successfulLoginWithAPITest() {
        AuthResponseDemoQa authResponseDemoQa = accountApiDemoQa.login(userName, password);

        openAndInjectCookies(
                authResponseDemoQa.getToken(),
                authResponseDemoQa.getUserId(),
                authResponseDemoQa.getExpires()
        );

        open("/profile");
        $("#userName-value").shouldHave(text(userName));
    }
}
