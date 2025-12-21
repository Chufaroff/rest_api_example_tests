package chufarov.projects.demo_qa_tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class TestConfig {

    @BeforeAll
    static void setUp() {

        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";
    }

    protected void injectCookie(String name, String value) {
        getWebDriver().manage().addCookie(new Cookie(name, value));
        getWebDriver().navigate().refresh();
    }

    protected void openAndInjectCookies(String token, String userId, String expires) {
        open("/");
        injectCookie("token", token);
        injectCookie("userID", userId);
        injectCookie("expires", expires);
    }
}
