package chufarov.projects.simple_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Rest Api Tests")
public class UsersApiTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String USERS_ENDPOINT = "/users";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /*
     * ТЕСТ 1: Проверка доступности API (самый простой тест)
     */
    @DisplayName("Проверка доступности API")
    @Test
    void testApiIsAvailable() {
        given()  // Подготовка
        .when()  // Действие
                .get(USERS_ENDPOINT)  // GET запрос на /users
        .then()                       // Проверки
                .statusCode(200);  // Ожидаем статус 200 OK
    }

    /*
     * ТЕСТ 2: Получение всех пользователей
     */
    @DisplayName("Получение всех пользователей")
    @Test
    public void testGetAllUsers() {
        System.out.println("=== Тест 2: Получение всех пользователей ===");

        given()

        .when()
                .get(USERS_ENDPOINT)
                
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)  // Проверяем формат ответа
                .body("size()", equalTo(10))  // Должно быть 10 пользователей
                .body("id", everyItem(notNullValue()))  // У всех есть ID
                .body("name", everyItem(not(emptyOrNullString())))  // У всех есть имя
                .body("email", everyItem(containsString("@")));  // Email содержит @

        System.out.println("✅ Получены все пользователи (10 штук)");
    }

    /*
     * ТЕСТ 3: Получение пользователя по ID
     */
    @DisplayName("Получение пользователя по ID")
    @Test
    void testGetUserById() {
        System.out.println("=== Тест 3: Получение пользователя по ID ===");

        int userId = 1;

        given()
                .pathParam("id", userId)

        .when()
                .get(USERS_ENDPOINT + "/{id}")

        .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Leanne Graham"))
                .body("username", equalTo("Bret"))
                .body("email", equalTo("Sincere@april.biz"))
                .body("address.street", equalTo("Kulas Light"))
                .body("phone", containsString("770"))  // Проверяем часть номера
                .body("website", equalTo("hildegard.org"));  // Проверяем сайт

        System.out.println("✅ Пользователь с ID=" + userId + " получен");
    }

    /*
     * ТЕСТ 4: Проверка вложенных объектов (address и company)
     */
    @DisplayName("Проверка вложенных объектов (address и company)")
    @Test
    public void testUserNestedObjects() {
        System.out.println("=== Тест 4: Проверка вложенных объектов ===");

        given()
                .pathParam("id", 1)
        .when()
                .get(USERS_ENDPOINT + "/{id}")
        .then()
                .statusCode(200)
                // Проверка объекта address
                .body("address.street", equalTo("Kulas Light"))
                .body("address.suite", equalTo("Apt. 556"))
                .body("address.city", equalTo("Gwenborough"))
                .body("address.zipcode", equalTo("92998-3874"))
                // Проверка вложенного объекта geo внутри address
                .body("address.geo.lat", equalTo("-37.3159"))
                .body("address.geo.lng", equalTo("81.1496"))
                // Проверка объекта company
                .body("company.name", equalTo("Romaguera-Crona"))
                .body("company.catchPhrase", containsString("neural-net"))
                .body("company.bs", containsString("real-time"));

        System.out.println("✅ Вложенные объекты корректны");
    }

    /*
     * ТЕСТ 5: Поиск пользователя по email (фильтрация)
     */
    @DisplayName("Поиск пользователя по email (фильтрация)")
    @Test
    public void testFindUserByEmail() {
        System.out.println("=== Тест 5: Поиск пользователя по email ===");

        given()
                .queryParam("email", "Sincere@april.biz")  // Параметр запроса
        .when()
                .get(USERS_ENDPOINT)
        .then()
                .statusCode(200)
                .body("size()", equalTo(1))  // Должен найтись 1 пользователь
                .body("[0].email", equalTo("Sincere@april.biz"))
                .body("[0].name", equalTo("Leanne Graham"));

        System.out.println("✅ Пользователь найден по email");
    }

    /*
     * ТЕСТ 6: Создание нового пользователя (POST запрос)
     */
    @DisplayName("Создание нового пользователя (POST запрос)")
    @Test
    public void testCreateNewUser() {
        System.out.println("=== Тест 6: Создание нового пользователя ===");

        // Подготавливаем данные для нового пользователя
        // Используем HashMap для избежания проблем с сериализацией
        Map<String, Object> address = new HashMap<>();
        address.put("street", "Новая улица");
        address.put("suite", "Квартира 123");
        address.put("city", "Новый город");
        address.put("zipcode", "123456");

        Map<String, Object> geo = new HashMap<>();
        geo.put("lat", "40.7128");
        geo.put("lng", "-74.0060");
        address.put("geo", geo);

        Map<String, Object> company = new HashMap<>();
        company.put("name", "Новая компания");
        company.put("catchPhrase", "Инновационные решения");
        company.put("bs", "оптимизация бизнес-процессов");

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", "Иван Иванов");
        newUser.put("username", "ivanov");
        newUser.put("email", "ivan@example.com");
        newUser.put("address", address);
        newUser.put("phone", "123-456-7890");
        newUser.put("website", "ivanov.com");
        newUser.put("company", company);

        Response response = given()
                .contentType(ContentType.JSON)  // Указываем, что отправляем JSON
                .body(newUser)  // Передаем данные пользователя
                .when()
                .post(USERS_ENDPOINT)  // POST запрос
                .then()
                .statusCode(201)  // 201 Created - ресурс создан
                .body("id", notNullValue())  // Должен вернуться ID
                .body("name", equalTo("Иван Иванов"))
                .body("email", equalTo("ivan@example.com"))
                .extract()
                .response();

        int createdId = response.path("id");
        System.out.println("✅ Создан пользователь с ID=" + createdId);
    }

    /*
     * ТЕСТ 7: Обновление пользователя (PUT запрос)
     */
    @DisplayName("Обновление пользователя (PUT запрос)")
    @Test
    public void testUpdateUser() {
        System.out.println("=== Тест 7: Обновление пользователя ===");

        int userId = 1;

        // Создаем обновленные данные
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("id", userId);
        updatedUser.put("name", "Обновленное Имя");
        updatedUser.put("username", "updateduser");
        updatedUser.put("email", "updated@example.com");
        updatedUser.put("phone", "999-999-9999");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .body(updatedUser)
                .when()
                .put(USERS_ENDPOINT + "/{id}")  // PUT /users/1
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Обновленное Имя"))
                .body("email", equalTo("updated@example.com"));

        System.out.println("✅ Пользователь с ID=" + userId + " обновлен");
    }

    /*
     * ТЕСТ 8: Частичное обновление (PATCH запрос)
     */
    @DisplayName("Частичное обновление (PATCH запрос)")
    @Test
    public void testPartialUpdateUser() {
        System.out.println("=== Тест 8: Частичное обновление пользователя ===");

        int userId = 1;

        // Обновляем только email и телефон
        Map<String, String> partialUpdate = new HashMap<>();
        partialUpdate.put("email", "patched@example.com");
        partialUpdate.put("phone", "111-222-3333");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .body(partialUpdate)
                .when()
                .patch(USERS_ENDPOINT + "/{id}")  // PATCH /users/1
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("email", equalTo("patched@example.com"))
                .body("phone", equalTo("111-222-3333"));

        System.out.println("✅ Пользователь частично обновлен");
    }

    /*
     * ТЕСТ 9: Удаление пользователя (DELETE запрос)
     */
    @DisplayName("Удаление пользователя (DELETE запрос)")
    @Test
    public void testDeleteUser() {
        System.out.println("=== Тест 9: Удаление пользователя ===");

        int userId = 1;

        given()
                .pathParam("id", userId)
                .when()
                .delete(USERS_ENDPOINT + "/{id}")  // DELETE /users/1
                .then()
                .statusCode(200);  // JSONPlaceholder возвращает 200

        System.out.println("✅ Запрос на удаление пользователя отправлен");
    }

    /*
     * ТЕСТ 10: Отрицательный тест - несуществующий пользователь
     */
    @DisplayName("Отрицательный тест - несуществующий пользователь")
    @Test
    public void testNonExistentUser() {
        System.out.println("=== Тест 10: Запрос несуществующего пользователя ===");

        given()
                .pathParam("id", 99999)  // Несуществующий ID
                .when()
                .get(USERS_ENDPOINT + "/{id}")
                .then()
                .statusCode(404);  // 404 Not Found

        System.out.println("✅ Получен ожидаемый статус 404");
    }
}
