package chufarov.projects.demo_qa_tests;

import chufarov.projects.api_classes.AccountApiDemoQa;
import chufarov.projects.models.response.AuthResponseDemoQa;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class LoginTests extends TestConfig {

    private final AccountApiDemoQa accountApiDemoQa = new AccountApiDemoQa();

    @Test
    void successfulLoginWithUITest() {
        open("/login");
        $("#userName").setValue(TestData.LOGIN_USER);
        $("#password").setValue(TestData.PASSWORD_USER);
        $("#login").click();
        $("#userName-value").shouldHave(text(TestData.LOGIN_USER));
    }

    @Test
    void successfulLoginWithAPITest() {
        AuthResponseDemoQa authResponseDemoQa = accountApiDemoQa.login(
                TestData.LOGIN_USER,
                TestData.PASSWORD_USER
        );

        openAndInjectCookies(
                authResponseDemoQa.getToken(),
                authResponseDemoQa.getUserId(),
                authResponseDemoQa.getExpires()
        );

        open("/profile");
        $("#userName-value").shouldHave(text(TestData.LOGIN_USER));
    }
}
