package chufarov.projects.demo_web_shop_tests;

import chufarov.projects.api.AuthApi;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@Owner("Chufarov Bogdan")
@Story("Working with user data")
@DisplayName("Update account user tests")
public class UpdateAccountTest extends TestBase {

    @Test
    @DisplayName("Edit address user")
    void editAddressTest() {

        Map<String, String> data = new HashMap<>();
        data.put("Address.Id", "0");
        data.put("Address.FirstName", "Bog");
        data.put("Address.LastName", "Dan");
        data.put("Address.Email", "bogdan30-95@mail.ru");
        data.put("Address.Company", "Company");
        data.put("Address.CountryId", "66");
        data.put("Address.StateProvinceId", "0");
        data.put("Address.City", "Moscow");
        data.put("Address.Address1", "Street1");
        data.put("Address.Address2", "Street2");
        data.put("Address.ZipPostalCode", "77889900");
        data.put("Address.PhoneNumber", "1234567891");
        data.put("Address.FaxNumber", "1234567");

        String authCookieValue = AuthApi.login(login, password);

        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie(AuthApi.AUTH_COOKIE_NAME, authCookieValue)
                .formParams(data)
                .when()
                .post("/customer/addresses")
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode())
                .withFailMessage("Expected status 302, but get it 200",
                        response.getStatusCode(), response.getBody().asString())
                .isEqualTo(200);
    }

    @Test
    @DisplayName("Edit address to turn off redirect")
    void editAddressToTurnOffRedirectTest() {
        Map<String, String> addressData = new HashMap<>();
        addressData.put("Address.Id", "0");
        addressData.put("Address.FirstName", "Bog");
        addressData.put("Address.LastName", "Dan");
        addressData.put("Address.Email", "bogdan30-95@mail.ru");
        addressData.put("Address.Company", "Company");
        addressData.put("Address.CountryId", "66");
        addressData.put("Address.StateProvinceId", "0");
        addressData.put("Address.City", "Moscow");
        addressData.put("Address.Address1", "Street1");
        addressData.put("Address.Address2", "Street2");
        addressData.put("Address.ZipPostalCode", "77889900");
        addressData.put("Address.PhoneNumber", "1234567891");
        addressData.put("Address.FaxNumber", "1234567");

        String authCookieValue = AuthApi.login(login, password);

        // КЛЮЧЕВОЕ ИЗМЕНЕНИЕ
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", authCookieValue)
                .formParams(addressData)
                .redirects().follow(false) // ← ОТКЛЮЧАЕМ авто-редирект
                .when()
                .post("/customer/addressadd")
                .then()
                .log().all()
                .statusCode(302) // ← Теперь 302, а не 200!
                .header("Location", is("/customer/addresses")); // Проверяем куда редиректит
    }
}
