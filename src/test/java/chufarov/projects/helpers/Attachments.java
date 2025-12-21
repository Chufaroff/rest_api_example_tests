package chufarov.projects.helpers;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.logging.LogType.BROWSER;

public class Attachments {

    private static boolean isBrowserOpened() {
        try {
            return getWebDriver() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] screenshotAs(String attachName) {
        if (!isBrowserOpened()) {
            return new byte[0];
        }

        try {
            return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        if (!isBrowserOpened()) {
            return "Browser not opened (API test)".getBytes(StandardCharsets.UTF_8);
        }

        try {
            return getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return ("Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8);
        }
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachAsText(String attachName, String message) {
        return message;
    }

    public static void browserConsoleLogs() {
        if (!isBrowserOpened()) {
            attachAsText("Browser console logs", "Browser not opened");
            return;
        }

        try {
            String logs = String.join("\n", Selenide.getWebDriverLogs(BROWSER));
            attachAsText("Browser console logs", logs);
        } catch (Exception e) {
            attachAsText("Browser console logs", "Error: " + e.getMessage());
        }
    }

    // Удали метод addVideo() если не используешь Selenoid
    // Или замени на простой:
    @Attachment(value = "Test execution", type = "text/plain")
    public static String testExecutionInfo() {
        return "Test executed. Browser opened: " + isBrowserOpened();
    }
}