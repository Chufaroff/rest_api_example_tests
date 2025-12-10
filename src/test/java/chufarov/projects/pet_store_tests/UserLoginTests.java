package chufarov.projects.pet_store_tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserLoginTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    void loginUserBasicTest() {

        // GET /user/login?username={username}&password={password}
        given()
                .baseUri(BASE_URL)
                .log().all() // Логируем запрос
        .when()
                .get("/user/login?username=Bogdan&password=fifa2098")
        .then()
                .log().all()
                .statusCode(200)
                .contentType("application/json")

                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", containsString("logged in user session:"));
    }

    @Test
    void loginUserWithQueryParams() {

        given()
                .baseUri(BASE_URL)
                .log().all()
                .queryParam("username", "Bogdan")
                .queryParam("password", "fifa2098")
        .when()
                .get("/user/login")
        .then()
                .log().all()
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", matchesPattern("logged in user session:\\d+"));
    }

    @Test
    void loginUserAndCheckHeaders() {

        given()
                .baseUri(BASE_URL)
                .log().all()
                .queryParam("username", "Bogdan")
                .queryParam("password", "fifa2098")
        .when()
                .get("/user/login")
        .then()
                .log().all()
                .statusCode(200)
                // Проверяем заголовки
                .header("content-type", "application/json")
                .header("access-control-allow-origin", "*")
                .header("x-rate-limit", "5000")
                .header("server", "Jetty(9.2.9.v20150224)")
                .header("x-expires-after", notNullValue());
    }

    @Test
    void loginUserAndExtractData() {

        String sessionMessage =
                given()
                        .baseUri(BASE_URL)
                        .log().all()
                        .queryParam("username", "Bogdan")
                        .queryParam("password", "fifa2098")
                .when()
                        .get("/user/login")
                .then()
                        .statusCode(200)
                        .extract()
                        .path("message");

        System.out.println("Session message " + sessionMessage);

        String jsonResponse =
                given()
                        .baseUri(BASE_URL)
                        .log().all()
                        .queryParam("username", "Bogdan")
                        .queryParam("password", "fifa2098")
                .when()
                        .get("/user/login")
                .then()
                        .statusCode(200)
                        .extract()
                        .asString();

        System.out.println("Full Json Response " + jsonResponse);

        int code =
                given()
                        .baseUri(BASE_URL)
                        .log().all()
                        .queryParam("username", "Bogdan")
                        .queryParam("password", "fifa2098")
                .when()
                        .get("/user/login")
                .then()
                        .statusCode(200)
                        .extract()
                        .path("code");

        String type =
                given()
                        .baseUri(BASE_URL)
                        .log().all()
                        .queryParam("username", "Bogdan")
                        .queryParam("password", "fifa2098")
                .when()
                        .get("/user/login")
                .then()
                        .statusCode(200)
                        .extract()
                        .path("type");

        System.out.println("Code: " + code + ", Type: " + type);
    }

    @Test
    void loginUserWithInvalidCredentials() {

        given()
                .baseUri(BASE_URL)
                .queryParam("username", "Bogdan")
                .queryParam("password", "wrongpassword")
        .when()
                .get("/user/login")
        .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", containsString("logged in user session:"))
                .log().all();

        given()
                .baseUri(BASE_URL)
                .queryParam("username", "NonExistentUser")
                .queryParam("password", "anypassword")
                .when()
                .get("/user/login")
                .then()
                .statusCode(200)
                .log().all();
    }
}
