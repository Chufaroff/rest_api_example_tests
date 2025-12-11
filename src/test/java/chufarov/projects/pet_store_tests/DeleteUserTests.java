package chufarov.projects.pet_store_tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class DeleteUserTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    void deleteUserTest() {

        String userName = "Brand";

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(userName)
                .log().all()
        .when()
                .delete("/user")
        .then()
                .statusCode(405)
                .log().all();
    }

    @Test
    void deleteUserAndCheckHeadersTest() {

        String userName = "Brand";

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(userName)
                .log().all()
        .when()
                .delete("/user")
        .then()
                .statusCode(405)
                .log().all()

                .contentType("application/xml")
                .header("date", notNullValue())
                .header("server", "Jetty(9.2.9.v20150224)");
    }

    @Test
    void simpleCreateOrderTest() {

        int orderId = 999;

        String orderJson = """
        {
          "id": 999,
          "petId": 1,
          "quantity": 1,
          "shipDate": "2025-12-10T16:11:16.833+0000",
          "status": "placed",
          "complete": false
        }
        """;

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(orderJson)
        .when()
                .post("/store/order")
        .then()
                .statusCode(200)
                .body("id", equalTo(999))
                .body("status", equalTo("placed"));

        given()
                .baseUri(BASE_URL)
                .log().all()
        .when()
                .delete("/store/order/" + orderId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", notNullValue())
                .header("Server", "Jetty(9.2.9.v20150224)")
                .header("Connection", "keep-alive");
    }
}
