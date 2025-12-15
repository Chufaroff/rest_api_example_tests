package chufarov.projects.pet_store_simple_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class StoreOrderTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    void createOrderBasicTest() {

        String orderJson = """
                {
                "id": 5,
                   "petId": 6,
                   "quantity": 38,
                   "shipDate": "2025-12-10T16:11:16.833+0000",
                   "status": "placed",
                   "complete": true
                }
                """;

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(orderJson)
                .log().all()
        .when()
                .post("/store/order")
        .then()
                .statusCode(200)
                .log().all()
                .contentType(JSON)

                .body("id", equalTo(5))
                .body("petId", equalTo(6))
                .body("quantity", equalTo(38))
                .body("shipDate", equalTo("2025-12-10T16:11:16.833+0000"))
                .body("status", equalTo("placed"))
                .body("complete", equalTo(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"placed", "approved", "delivered"})
    void createOrderWithDifferentStatusesImproved(String status) {
        // Используем меньшие числа, чтобы не было переполнения при преобразовании long->int
        int orderId = (int) (System.currentTimeMillis() % 1000000);

        String orderJson = String.format("""
        {
          "id": %d,
          "petId": 100,
          "quantity": 5,
          "shipDate": "2025-12-10T16:11:16.833+0000",
          "status": "%s",
          "complete": true
        }
        """, orderId, status);

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(orderJson)
        .when()
                .post("/store/order")
        .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .body("status", equalTo(status));
    }

    @Test
    void simpleCreateOrderTest() {

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
    }

    @Test
    void createAndRetrieveOrderTest() {

        int orderId = 899;

        String orderJson = """
        {
          "id": 899,
          "petId": 2,
          "quantity": 3,
          "shipDate": "2025-12-10T16:11:16.833+0000",
          "status": "approved",
          "complete": true
        }
        """;

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(orderJson)
        .when()
                .post("/store/order")
        .then()
                .log().all()
                .statusCode(200);

        given()
                .baseUri(BASE_URL)
        .when()
                .get("/store/order/" + orderId)
        .then()
                .statusCode(200)
                .body("id", equalTo(899))
                .body("petId", equalTo(2))
                .body("quantity", equalTo(3))
                .body("shipDate", equalTo("2025-12-10T16:11:16.833+0000"))
                .body("status", equalTo("approved"))
                .body("complete", equalTo(true));
    }
}
