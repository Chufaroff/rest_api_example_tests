package chufarov.projects.pet_store_simple_tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PutUserTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    void createUpdateAndGetUserTest() {

        String userName = "Vegas";

        String createUserBody = String.format("""
        {
        "id": 2,
        "username": "%s",
        "firstName": "Bogdan",
        "lastName": "Chufarov",
        "email": "bogdan@mail.ru",
        "password": "fifa2020",
        "phone": "12345678910",
        "userStatus": 7
        }
        """, userName);

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(createUserBody)
                .log().all()
        .when()
                .post("/user")
        .then()
                .log().all()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", equalTo("2"));

        String updateUserBody = String.format("""
        {
        "id": 2,
        "username": "%s",
        "firstName": "Dimon",
        "lastName": "Chuf",
        "email": "dima@mail.ru",
        "password": "fifa2030",
        "phone": "22345678910",
        "userStatus": 9
        }
        """, userName);

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(updateUserBody)
                .log().all()
        .when()
                .put("/user/" + userName)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", equalTo("2"));

        given()
                .baseUri(BASE_URL)
                .log().all()
        .when()
                .get("/user/" + userName)
        .then()
                .log().all()
                .statusCode(200)
                .body("username", equalTo(userName))
                .body("firstName", equalTo("Dimon"))
                .body("lastName", equalTo("Chuf"))  // Проверяем новую фамилию
                .body("email", equalTo("dima@mail.ru"))     // Проверяем новый email
                .body("phone", equalTo("22345678910"))          // Проверяем новый телефон
                .body("userStatus", equalTo(9));
    }
}
