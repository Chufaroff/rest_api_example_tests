package chufarov.projects.pet_store_tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTests {

    // 1. Устанавливаем базовый URL API (необходимо для всех запросов)
    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    void createUserBasicTest() {

            // 2. Подготавливаем тело запроса в формате JSON
            //    В реальных тестах лучше использовать POJO классы или Map
            String requestBody = """
            {
              "id": 2,
              "username": "Vegas",
              "firstName": "Bogdan",
              "lastName": "Chufarov",
              "email": "bogdan@mail.ru",
              "password": "fifa2020",
              "phone": "12345678910",
              "userStatus": 7
            }
            """;

            // 3. Формируем и выполняем запрос используя паттерн Given-When-Then
            given()                         // Given - подготовка запроса
                    .baseUri(BASE_URL)          // Устанавливаем базовый URL
                    .contentType(JSON)         // Указываем Content-Type: application/json
                    .body(requestBody)         // Передаем тело запроса
                    .log().all()               // Логируем все детали запроса (опционально, для отладки)
            .when()                         // When - выполнение действия
                    .post("/user")             // Выполняем POST запрос по указанному пути
            .then()                         // Then - проверки ответа
                    .statusCode(200)           // Проверяем что HTTP статус код равен 200
                    .log().all();              // Логируем все детали ответа
        }

    @Test
    void createUserAndVerifyAllFields() {

        String requestBody = """
            {
              "id": 2,
              "username": "Vegas",
              "firstName": "Bogdan",
              "lastName": "Chufarov",
              "email": "bogdan@mail.ru",
              "password": "fifa2020",
              "phone": "12345678910",
              "userStatus": 7
            }
            """;

            given()
                    .baseUri(BASE_URL)
                    .contentType(JSON)
                    .body(requestBody)
                    .log().all()
            .when()
                    .post("/user")
            .then()
                    .log().all()
                    .statusCode(200)

                    .body("code", equalTo(200))
                    .body("type", equalTo("unknown"))
                    .body("message", equalTo("2"))

                    .contentType(JSON)
                    .header("server", "Jetty(9.2.9.v20150224)");
    }

    @Test
    void createUserWithEmptyBody() {

            String emptyBody = """
                    {}
                    """;

            given()
                    .baseUri(BASE_URL)
                    .contentType(JSON)
                    .body(emptyBody)
            .when()
                    .post("/user")
            .then()
                    .statusCode(200)
                    .log().all();
    }

    @Test
    void createUserWithInvalidData() {

        String invalidJson = "{ invalid json }";

        given()
                .baseUri(BASE_URL)
                .contentType(JSON)
                .body(invalidJson)
        .when()
                .post("/user")
        .then()
                .statusCode(400)
                .log().all();
        }
}

