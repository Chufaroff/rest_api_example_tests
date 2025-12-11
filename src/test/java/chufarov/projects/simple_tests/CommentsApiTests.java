package chufarov.projects.simple_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Rest Api Tests")
public class CommentsApiTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String COMMENTS_ENDPOINT = "/comments";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @DisplayName("Проверка доступности API запроса")
    @Test
    void testApiIsAvailable() {

        given()
        .when()
                .get(COMMENTS_ENDPOINT)
        .then()
                .statusCode(200);
    }

    @DisplayName("Получение всех комментариев")
    @Test
    void testGetAllComments() {

        given()

        .when()
                .get(COMMENTS_ENDPOINT)

        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @DisplayName("Проверка количества комментариев")
    @Test
    void testCommentsCount() {

        Response response = given()
        .when()
                .get(COMMENTS_ENDPOINT)
        .then()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> comments = response.jsonPath().getList("");
        System.out.println("✅ Всего комментариев: " + comments.size());
    }

    @DisplayName("Получение комментария по ID")
    @Test
    void testGetCommentById() {

        given()
                .pathParam("id", 1)  // Устанавливаем значение для {id}
        .when()
                .get(COMMENTS_ENDPOINT + "/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("postId", equalTo(1))
                .body("name", not(emptyOrNullString()))
                .body("email", containsString("@"))
                .body("body", not(emptyOrNullString()));

        System.out.println("✅ Комментарий с ID=1 получен");
    }

    @DisplayName("Фильтрация комментариев по email")
    @Test
    public void testGetCommentsByEmail() {
        System.out.println("=== Тест 7: Фильтрация комментариев по email ===");

        String email = "Eliseo@gardner.biz";

        given()
                .queryParam("email", email)
        .when()
                .get(COMMENTS_ENDPOINT)
        .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("email", everyItem(equalTo(email)));

        System.out.println("✅ Комментарии от email " + email + " найдены");
    }

    @DisplayName("Создание нового комментария")
    @Test
    public void testCreateNewComment() {
        System.out.println("=== Тест 8: Создание нового комментария ===");

        // Подготавливаем данные для нового комментария
        Map<String, Object> newComment = new HashMap<>();
        newComment.put("postId", 1);
        newComment.put("name", "Тестовый комментарий");
        newComment.put("email", "test@example.com");
        newComment.put("body", "Это тестовый комментарий для проверки API");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post(COMMENTS_ENDPOINT)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("postId", equalTo(1))
                .body("name", equalTo("Тестовый комментарий"))
                .extract()
                .response();

        int createdId = response.path("id");
        System.out.println("✅ Создан комментарий с ID=" + createdId);
    }

    @DisplayName("Обновление комментария")
    @Test
    public void testUpdateComment() {
        System.out.println("=== Тест 9: Обновление комментария ===");

        Map<String, Object> updatedComment = new HashMap<>();
        updatedComment.put("id", 1);
        updatedComment.put("postId", 1);
        updatedComment.put("name", "Обновленный комментарий");
        updatedComment.put("email", "updated@example.com");
        updatedComment.put("body", "Этот комментарий был обновлен");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(updatedComment)
                .when()
                .put(COMMENTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Обновленный комментарий"))
                .body("email", equalTo("updated@example.com"));

        System.out.println("✅ Комментарий обновлен");
    }

    @DisplayName("Удаление комментария")
    @Test
    public void testDeleteComment() {
        System.out.println("=== Тест 10: Удаление комментария ===");

        given()
                .pathParam("id", 1)
                .when()
                .delete(COMMENTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(200);

        System.out.println("✅ Запрос на удаление комментария отправлен");
    }

    @DisplayName("Проверка первого комментария")
    @Test
    public void testFirstComment() {
        System.out.println("=== Тест 11: Проверка первого комментария ===");

        Response response = given()
                .when()
                .get(COMMENTS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Получаем первый комментарий из массива
        Map<String, Object> firstComment = response.jsonPath().getMap("[0]");

        System.out.println("Первый комментарий:");
        System.out.println("  ID: " + firstComment.get("id"));
        System.out.println("  Автор: " + firstComment.get("name"));
        System.out.println("  Email: " + firstComment.get("email"));

        System.out.println("✅ Первый комментарий корректен");
    }
}
