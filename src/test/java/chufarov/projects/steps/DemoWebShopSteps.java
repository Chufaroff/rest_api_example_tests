package chufarov.projects.steps;

import chufarov.projects.models.request.LoginRequestDemoWebShop;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;

import static chufarov.projects.specs.ApiSpecs.requestSpecDemoWebShop;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemoWebShopSteps {

    private final String AUTH_COOKIE_NAME = "NOPCOMMERCE.AUTH";

    private String authCookie;

    @Step("Авторизоваться через API (userEmail: {userEmail})")
    public void apiLogin(String userEmail, String password) {

        Response response = given(requestSpecDemoWebShop)
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", userEmail)
                .formParam("Password", password)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .response();

        this.authCookie = response.getCookie(AUTH_COOKIE_NAME);
    }

    @Step("Добавить товар в корзину через API (ID товара: {productId})")
    public Response apiAddToCart(String productId) {

        String requestBody = "addtocart_" + productId + ".EnteredQuantity=1";

        return given(requestSpecDemoWebShop)
                .cookie(AUTH_COOKIE_NAME, authCookie)
                .contentType("application/x-www-form-urlencoded")
                .body(requestBody)
                .when()
                .post("/addproducttocart/details/" + productId + "/1")
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Добавить товар в корзину и проверить ответ (ID: {productId})")
    public void apiAddToCartWithCheck(String productId) {

        Response response = apiAddToCart(productId);
        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));
    }

    @Step("Установить авторизационную куку в браузер")
    public void setAuthCookieInBrowser() {
        open("/");
        getWebDriver().manage().addCookie(new Cookie(AUTH_COOKIE_NAME, authCookie));
        refresh();
    }

    @Step("Перейти в корзину")
    public void goToCart() {
        $("#topcartlink").hover().click();
    }

    @Step("Проверить, что в корзине есть товар")
    public void verifyProductInCart(String productName) {
        $(".shopping-cart-page").shouldHave(text(productName));
    }

    @Step("Удалить товар из корзины")
    public void removeProductFromCart() {
        $("input[name='removefromcart']").click();
        $("#termsofservice").click();
        $("input[name='updatecart']").click();
    }

    @Step("Проверить, что корзина пуста")
    public void verifyCartIsEmpty() {
        $(".shopping-cart-page").shouldHave(text("Your Shopping Cart is empty!"));
    }
}
