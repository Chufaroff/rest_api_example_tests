package chufarov.projects.specs;

import chufarov.projects.helpers.CustomAllureListener;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class ApiSpecs {

    // 1. СПЕЦИФИКАЦИЯ ЗАПРОСА (Request Specification)
    // Определяет общие настройки для ВСЕХ запросов к API
    public static RequestSpecification requestSpec = with() // Начинаем создавать спецификацию
            .log().all()                                    // Логируем
            .filter(CustomAllureListener.withCustomTemplates()) // Allure фильтр для красивых отчетов
            .contentType(ContentType.JSON)                      // Устанавливаем заголовок
            .baseUri("https://petstore.swagger.io")             // Базовый URL API
            .basePath("/v2");                                   // Базовый путь API


    // 2. СПЕЦИФИКАЦИИ ОТВЕТОВ (Response Specifications)
    // Предустановленные проверки для разных статус кодов
    // Для успешных ответов (200 OK)
    public static ResponseSpecification response200 = new ResponseSpecBuilder() // Создаем билдер
            .expectStatusCode(200) // Ожидаем статус код 200
            .expectContentType(ContentType.JSON)    // Ожидаем JSON в ответе
            .log(LogDetail.ALL)                     // Логируем все детали ответа
            .build();                               // Завершаем построение

    // Для создания ресурсов (201)
    public static ResponseSpecification response201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    // Для ошибок клиента (400 Bad request)
    public static ResponseSpecification response400 = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .log(LogDetail.ALL)
            .build();

    // Для не найденных ресурсов (404 Not Found)
    public static ResponseSpecification response404 = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .log(LogDetail.ALL)
            .build();

    public static ResponseSpecification response500 = new ResponseSpecBuilder()
            .expectStatusCode(500)
            .expectContentType(ContentType.JSON)
            .build();
}
