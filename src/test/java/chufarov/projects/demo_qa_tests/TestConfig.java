package chufarov.projects.demo_qa_tests;

import chufarov.projects.config.ConfigProperties;
import chufarov.projects.helpers.Attachments;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class TestConfig {

    protected static final ConfigProperties config =
            ConfigFactory.create(ConfigProperties.class, System.getProperties());

    protected final String userName = config.getUserName();
    protected final String password = config.getPassword();

    @BeforeAll
    static void setUp() {

        Configuration.baseUrl = config.getBaseUrl();
        Configuration.browser = config.getBrowser();
        Configuration.browserSize = config.getBrowserSize();
        RestAssured.baseURI = config.getBaseUrl();

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)      // Делать скриншоты
                .savePageSource(true)   // Сохранять HTML
                .includeSelenideSteps(true) // true - показывать все шаги Selenide
        );

        // Удаленный браузер (если передан в конфиге)
        String remoteUrl = config.getRemoteUrl();
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
