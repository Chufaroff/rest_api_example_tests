package chufarov.projects.petstore_final_build_tests;

import chufarov.projects.models.request.PlaceOrderRequest;
import chufarov.projects.models.request.UserRequest;
import chufarov.projects.models.response.*;
import chufarov.projects.specs.ApiSpecs;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Owner("Chufarov Bogdan")
@Severity(SeverityLevel.NORMAL)
@Feature("User Management")
@DisplayName("Pet Store Api Tests")
public class PetStoreApiTests {

    @Test
    @Tag("Positive")
    @Story("Login request")
    @DisplayName("Successful login with correct credentials")
    void successfulLoginTest() {

        String username = "Vegas";
        String password = "fifa2020";

        LoginResponse loginResponse = step("Authenticate user in PetStore system", () ->
                given(ApiSpecs.requestSpec)
                        .queryParam("username", username)
                        .queryParam("password", password)
                .when()
                        .get("/user/login")
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(LoginResponse.class)
                );

        step("Check HTTP status code is 200", () ->
                assertThat(loginResponse.getCode())
                        .isEqualTo(200)
                );

        step("Verify response type is correct", () ->
                assertThat(loginResponse.getType())
                        .isEqualTo("unknown")
                );

        step("Confirm session was created", () ->
                assertThat(loginResponse.getMessage())
                        .isNotNull()
                        .isNotEmpty()
                        .contains("logged in user session:")
                );
    }

    @Test
    @Tag("Positive")
    @Story("Create user request")
    @DisplayName("Create user and verification response body")
    void createUserTest() {

        UserRequest userRequest = new UserRequest();
        userRequest.setId(7);
        userRequest.setUsername("Vegas");
        userRequest.setFirstName("Bogdan");
        userRequest.setLastName("Chufarov");
        userRequest.setEmail("bogdan@mail.ru");
        userRequest.setPassword("fifa2020");
        userRequest.setPhone("0123456789");

        UserResponse userResponse = step("Sending user creating request", () ->
                given(ApiSpecs.requestSpec)
                        .body(userRequest)
                .when()
                        .post("/user")
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(UserResponse.class)
                );

        step("Code verification response", () ->
                assertThat(userResponse.getCode())
                        .isEqualTo(200)
                );

        step("Cheking response type", () ->
                assertThat(userResponse.getType())
                        .isEqualTo("unknown")
                );

        step("Message check with ID", () ->
                assertThat(userResponse.getMessage())
                        .isEqualTo("7")
                );
    }

    @Test
    @Tag("Positive")
    @Story("Update user request")
    @DisplayName("Update user and response verification")
    void updateUserAndResponseVerificationTest() {

        UserRequest userRequest = new UserRequest();
        userRequest.setId(7);
        userRequest.setUsername("Vegas");
        userRequest.setFirstName("Bogdan");
        userRequest.setLastName("Chufarov");
        userRequest.setEmail("chufarov@mail.ru");
        userRequest.setPassword("fifa2025");
        userRequest.setPhone("1234567891");

        Response response = step("Sending an update user request", () ->
                given(ApiSpecs.requestSpec)
                        .body(userRequest)
                .when()
                        .put("/user/Vegas")
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .response()
                );

        UserResponse userResponse = response.as(UserResponse.class);

        step("Verify response body", () -> {
           assertThat(userResponse.getCode()).isEqualTo(200);
           assertThat(userResponse.getType()).isEqualTo("unknown");
           assertThat(userResponse.getMessage()).isEqualTo("7");
        });

        step("Validate response headers", () -> {
           assertThat(response.getHeader("Content-Type"))
                   .contains("application/json");
           assertThat(response.getHeader("Server"))
                   .contains("Jetty(9.2.9.v20150224)");
           assertThat(response.getHeader("Access-Control-Allow-Origin"))
                   .isEqualTo("*");
        });
    }

    @Test
    @Tag("Negative")
    @Story("404 Not Found")
    @DisplayName("Delete non-existent user - should return 404")
    void deleteNonExistentUserTest() {
        String username = "Vegas";

        step("Attempt to delete non-existent user", () ->
                given(ApiSpecs.requestSpec)
                .when()
                        .delete("/user/" + username)
                .then()
                        .spec(ApiSpecs.response404)
        );
    }

    @Test
    @Tag("Positive")
    @Story("Create and delete user request")
    @DisplayName("Create and delete user and verification response body")
    void createAndDeleteUserTest() {

        String username = "Vegas";

        UserRequest userRequest = new UserRequest();
        userRequest.setId(7);
        userRequest.setUsername(username);
        userRequest.setFirstName("Bogdan");
        userRequest.setLastName("Chufarov");
        userRequest.setEmail("bogdan@mail.ru");
        userRequest.setPassword("fifa2020");
        userRequest.setPhone("0123456789");

        UserResponse userResponse = step("Sending user creating request", () ->
                given(ApiSpecs.requestSpec)
                        .body(userRequest)
                .when()
                        .post("/user")
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(UserResponse.class)
        );

        step("Code verification response", () ->
                assertThat(userResponse.getCode())
                        .isEqualTo(200)
        );

        step("Cheking response type", () ->
                assertThat(userResponse.getType())
                        .isEqualTo("unknown")
        );

        step("Message check with ID", () ->
                assertThat(userResponse.getMessage())
                        .isEqualTo("7")
        );

        UserResponse deleteResponse = step("Sending user deletion request", () ->
                given(ApiSpecs.requestSpec)
                .when()
                        .delete("/user/" + username)
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(UserResponse.class)
        );

        // Верификация ответа на удаление
        step("Verify deletion response body", () -> {
            assertThat(deleteResponse.getCode()).isEqualTo(200);
            assertThat(deleteResponse.getType()).isEqualTo("unknown");
            assertThat(deleteResponse.getMessage()).isEqualTo(username);
        });
    }

    @Test
    @Tag("Positive")
    @DisplayName("Placing an order on the website 'PetStore'")
    void placeOrderOnTheWebSite() {

        PlaceOrderRequest orderRequest = new PlaceOrderRequest();
        orderRequest.setId(5);
        orderRequest.setPetId(5);
        orderRequest.setQuantity(5);
        String formattedDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        orderRequest.setShipDate(formattedDate);
        orderRequest.setStatus("placed");
        orderRequest.setComplete(true);

        PlaceOrderResponse orderResponse = step("Sending order creation request", () ->
                given(ApiSpecs.requestSpec)
                        .body(orderRequest)
                .when()
                        .post("/store/order")
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(PlaceOrderResponse.class)
                );

        step("Verify response body", () -> {
           assertThat(orderResponse.getId()).isEqualTo(5);
           assertThat(orderResponse.getPetId()).isEqualTo(5);
           assertThat(orderResponse.getQuantity()).isEqualTo(5);
           assertThat(orderResponse.getStatus()).isEqualTo("placed");
           assertThat(orderResponse.getComplete()).isTrue();
        });
    }

    @Test
    @Tag("Positive")
    @DisplayName("Getting an order by ID on the website 'PetStore'")
    void getOrderByIdOnTheWebSite() {

        Integer orderId = 5;

        GetOrderByIdResponse orderResponse = step("Getting order creation request", () ->
                given(ApiSpecs.requestSpec)

                .when()
                        .get("/store/order/" + orderId)
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(GetOrderByIdResponse.class)
        );

        step("Verify GET response body", () -> {
            assertThat(orderResponse.getId()).isEqualTo(5);
            assertThat(orderResponse.getPetId()).isEqualTo(5);
            assertThat(orderResponse.getQuantity()).isEqualTo(5);
            assertThat(orderResponse.getStatus()).isEqualTo("placed");
            assertThat(orderResponse.getComplete()).isTrue();
        });
    }

    @Test
    @Tag("Positive")
    @DisplayName("Delete an order by ID on the website 'PetStore'")
    void deleteOrderByIdOnTheWebSite() {

        Integer orderId = 5;

        // DELETE запрос
        GetOrderByIdResponse deleteResponse = step("Delete order by ID", () ->
                given(ApiSpecs.requestSpec)
                .when()
                        .delete("/store/order/" + orderId)
                .then()
                        .spec(ApiSpecs.response200)
                        .extract()
                        .as(GetOrderByIdResponse.class)
        );

        step("Verify DELETE response", () -> {
            assertThat(deleteResponse.getCode()).isEqualTo(200);
            assertThat(deleteResponse.getType()).isEqualTo("unknown");
            assertThat(deleteResponse.getMessage()).isEqualTo(String.valueOf(orderId));
        });
    }
}


