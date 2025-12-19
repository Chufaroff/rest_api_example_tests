package chufarov.projects.demo_web_shop_tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class TestBase {

    String login = "bogdan30-95@mail.ru";
    String password = "fifa2020new";

    @BeforeAll
    static void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        RestAssured.baseURI = "https://demowebshop.tricentis.com";
    }

    protected void injectCookie(String name, String value) {
        getWebDriver().manage().addCookie(new Cookie(name, value));
        getWebDriver().navigate().refresh();
    }
}
