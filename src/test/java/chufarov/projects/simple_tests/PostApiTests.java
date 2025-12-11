package chufarov.projects.simple_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/*
 * Упрощенные автотесты для API https://jsonplaceholder.typicode.com/posts
 * Этот класс содержит базовые примеры для понимания тестирования REST API
 */
public class PostApiTests {

    // Базовый URL для всех тестов
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // Эндпоинт для постов
    private static final String POST_ENDPOINT = "/posts";

    // Настраиваем базовые параметры для RestAssured
    @BeforeAll
    static void setUp() {

        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /*
     * ТЕСТ 1: Получение всех постов (GET запрос)
     * Проверяем, что API возвращает список постов
     */
    @Test
    void testGetAllPost() {
        // given() - настраиваем параметры запроса (предусловия)
        // when() - выполняем действие (отправляем запрос)
        // then() - проверяем результат (постусловия)

        given() // Шаг 1: Подготовка запроса

        .when() // Шаг 2: Выполнение действия
                .get(POST_ENDPOINT) // Отправляем GET запрос на /posts

        .then() // Шаг 3: Проверки результата
                .statusCode(200) // Проверяем, что статус код 200 (OK)
                .contentType(ContentType.JSON)  // Проверяем, что ответ в формате JSON
                .body("size()", greaterThan(0))  // Проверяем, что массив не пустой
                .body("id", everyItem(notNullValue()))  // У каждого элемента есть id
                .body("title", everyItem(notNullValue()))  // У каждого элемента есть title
                .body("body", everyItem(notNullValue()));  // У каждого элемента есть body
    }

    @Test
    @DisplayName("GET /posts/{id} - должен вернуть конкретный пост")
    public void testGetPostById() {
        System.out.println("=== Тест 3: Получение поста по ID ===");

        int postId = 1;

        given()
                .pathParam("id", postId)  // Устанавливаем параметр пути {id}
        .when()
                .get(POST_ENDPOINT + "/{id}")  // GET /posts/1
        .then()
                .statusCode(200)
                .body("id", equalTo(postId))  // ID должен быть 1
                .body("userId", equalTo(1))  // userId должен быть 1
                .body("title", not(emptyOrNullString()))  // Заголовок не пустой
                .body("body", not(emptyOrNullString()));  // Тело не пустое

        System.out.println("✅ Тест пройден: получен пост с ID=" + postId);
    }
}
