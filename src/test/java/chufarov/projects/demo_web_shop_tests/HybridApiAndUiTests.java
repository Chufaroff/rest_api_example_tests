package chufarov.projects.demo_web_shop_tests;

import chufarov.projects.steps.DemoWebShopSteps;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@Owner("Chufarov Bogdan")
@Story("Композиция API и UI тестов")
@DisplayName("Гибридные тесты (API + UI)")
public class HybridApiAndUiTests extends TestBase {

    @DisplayName("Успешная авторизация через API")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    void successfulApiLoginTest() {

        DemoWebShopSteps steps = new DemoWebShopSteps();
        steps.apiLogin(userEmail, password);
    }

    @DisplayName("Добавление товара в корзину через API")
    @Severity(SeverityLevel.NORMAL)
    @Test
    void addToCartApiTest() {

        DemoWebShopSteps steps = new DemoWebShopSteps();
        steps.apiLogin(userEmail, password);
        steps.apiAddToCart("31");
    }

    @DisplayName("Добавление товара и проверка, что корзина не пустая через API")
    @Severity(SeverityLevel.NORMAL)
    @Test
    void addToCartAndCheckApiTest() {

        DemoWebShopSteps steps = new DemoWebShopSteps();
        steps.apiLogin(userEmail, password);
        steps.apiAddToCartWithCheck("31");
    }

    @DisplayName("Добавление и удаление товара через API и UI")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    void addAndRemoveProductToCartHybridTest() {

        DemoWebShopSteps steps = new DemoWebShopSteps();
        steps.apiLogin(userEmail, password);
        steps.apiAddToCartWithCheck("31");
        steps.setAuthCookieInBrowser();
        steps.goToCart();
        steps.verifyProductInCart("14.1-inch Laptop");
        steps.removeProductFromCart();
        steps.verifyCartIsEmpty();
    }

    @DisplayName("Добавление и удаление продукта с корзины авторизированного пользователя")
    @Test
    void userLoginAndCartUiTest() {

        step("Открываем главную страницу веб-магазина и авторизируемся", () -> {
            open("/");
            $("a[href='/login']").click();
            $("#Email").setValue(userEmail);
            $("#Password").setValue(password);
            $(".login-button").click();
        });

        step("Наводим курсор мыши на вкладку 'Computers' и проваливаемся по тексту 'Notebooks'", () ->{
            $("a[href='/computers']").hover();
            $("a[href='/notebooks']").click();
        });

        step("Кликаем на иконку с нужным продуктом и нажимаем кнопку 'Add to cart'", () ->{
            $("h2.product-title").click();
            $("#add-to-cart-button-31").click();
        });

        step("Переходим в корзину и проверяем, что отображается выбранный продукт", ()-> {
            $("#topcartlink").hover().click();
            $(".shopping-cart-page").shouldHave(text("14.1-inch Laptop"));
        });

        step("Ставим галочку чек-бокса('Remove') выбранного продукта и обновляем корзину", () ->{
            $("input[name='removefromcart']").click();
            $("#termsofservice").click();
            $("input[name='updatecart']").click();
        });

        step("Проверяем, что корзина обновилась и пуста", () ->
            $(".shopping-cart-page").shouldHave(text("Your Shopping Cart is empty!"))
        );
    }
}
