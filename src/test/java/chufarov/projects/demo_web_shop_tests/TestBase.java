package chufarov.projects.demo_web_shop_tests;

import chufarov.projects.helpers.Attachments;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    protected final String userEmail = "bogdan30-95@mail.ru";
    protected final String password = "fifa2020new";

    @BeforeAll
    static void setUp() {

        // Получаем параметры из системных свойств (Jenkins передает их как -D параметры)
        String remoteUrl = System.getProperty("remote.url", "https://user1:1234@selenoid.autotests.cloud/wd/hub");
        String baseUrl = System.getProperty("base.url", "https://demowebshop.tricentis.com");
        String browser = System.getProperty("browser", "chrome");
        // String browserVersion = System.getProperty("browser.version", "100.0");
        String browserSize = System.getProperty("browser.size", "1920x1080");

        // Настройка Selenide
        Configuration.baseUrl = baseUrl;
        Configuration.browserSize = browserSize;
        Configuration.browser = browser;
        // Configuration.browserVersion = browserVersion;
        Configuration.timeout = 10000;
        Configuration.pageLoadStrategy = "eager";
        Configuration.holdBrowserOpen = false;
        RestAssured.baseURI = "https://demowebshop.tricentis.com";

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)      // Делать скриншоты
                .savePageSource(true)   // Сохранять HTML
                .includeSelenideSteps(true) // true - показывать все шаги Selenide
        );

        // Удаленный браузер (если передан)
        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            Configuration.remote = remoteUrl;

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true,
                    "enableLog", true,
                    "sessionTimeout", "5m"
            ));

            Configuration.browserCapabilities = capabilities;
        }
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

