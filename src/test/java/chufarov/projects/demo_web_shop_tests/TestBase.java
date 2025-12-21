package chufarov.projects.demo_web_shop_tests;

import chufarov.projects.helpers.Attachments;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    protected final String userEmail = "bogdan30-95@mail.ru";
    protected final String password = "fifa2020new";

    @BeforeAll
    static void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        Configuration.timeout = 10000;

        RestAssured.baseURI = "https://demowebshop.tricentis.com";
    }

    @AfterEach
    void afterEach() {
        // Attachments проверяют сами, открыт ли браузер
        Attachments.screenshotAs("Final screenshot");
        Attachments.pageSource();
        Attachments.browserConsoleLogs();
        Attachments.testExecutionInfo(); // новый метод

        try {
            closeWebDriver();
        } catch (Exception e) {
            // Игнорируем для API тестов
        }
    }
}

