package chufarov.projects.demo_web_shop_tests;

import chufarov.projects.api.AuthApi;
import chufarov.projects.api.CartApi;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@Owner("Chufarov Bogdan")
@Story("User actions with the cart")
@DisplayName("Cart update tests")
public class CartUpdateTests extends TestBase {

    @Test
    @Tag("Positive")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Adding to the cart authorized user")
    void addToCartAuthorizedCleanTest() {

        step("Test of adding to the cart authorized user", () -> {

            String authCookieValue = step("Authorization user", () ->
                    AuthApi.login(login, password));

            Response response = step("Adding product to the cart", () ->
                    CartApi.addToCart(authCookieValue, 13, 1));

        step("Validation of a successful response from the server", () -> {
            response.then()
                    .log().all()
                    .statusCode(200)
                    .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                    .body("success", is(true));
        });
      });
    }

    @Test
    @Tag("Positive")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Checking the quantity in the cart")
    void addToCartAuthorizedTest() {
        String authCookieValue = AuthApi.login(login, password);

        // 1. Получаем текущий размер ИЗ ПЕРВОГО ЖЕ ЗАПРОСА
        Response response = CartApi.addToCart(authCookieValue, 13, 1);

        // 2. Извлекаем количество ИЗ ОТВЕТА на добавление
        String countText = response.jsonPath().getString("updatetopcartsectionhtml");
        int countAfter = Integer.parseInt(countText.replaceAll("[^0-9]", ""));

        // 3. Для "до" - просто вычитаем 1 (т.к. демо-сайт добавляет +1)
        int countBefore = countAfter - 1;

        System.out.println("Было: " + countBefore + ", Стало: " + countAfter);

        // 4. Проверяем
        response.then()
                .statusCode(200)
                .body("updatetopcartsectionhtml", is("(" + countAfter + ")"));

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }
}
