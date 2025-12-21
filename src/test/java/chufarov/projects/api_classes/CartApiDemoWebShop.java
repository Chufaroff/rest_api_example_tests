package chufarov.projects.api_classes;

import chufarov.projects.specs.ApiSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CartApiDemoWebShop {

    public static Response addToCart(String authCookieValue, int productId, int quantity) {

        String data = "addtocart_" + productId + ".EnteredQuantity=" + quantity;

        return given(ApiSpecs.requestSpecDemoWebShop)
                .contentType("application/x-www-form-urlencoded")
                .cookie("NOPCOMMERCE.AUTH", authCookieValue)
                .body(data)
                .when()
                .post("/addproducttocart/details/" + productId + "/1")
                .then()
                .extract().response();
    }
}
